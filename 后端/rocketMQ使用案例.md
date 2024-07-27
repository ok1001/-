### RocketMQ在部门人员数据同步中的应用案例

#### 背景概述

在公司的日常运营中，股份人员变动是一个常见的场景，这些变动往往涉及到用户主数据的更新。为了确保数据的一致性和实时性，我们采用RocketMQ作为消息中间件来协调不同系统间的数据同步。当股份调用特定的接口完成用户主数据的更新后，会自动触发MQ消息的发送，进而通知相关系统执行后续的业务处理。

#### MQ消息发送流程

1. **股份接口调用**：当用户股份发生变动时，相关系统会调用一个特定的API接口来处理这些变动。此接口不仅负责更新数据库中的用户主数据，还承担着触发消息发送的重任。

2. **MQ消息发送微服务**：一旦用户主数据更新成功，股份接口会向MQ消息发送微服务发送一个包含更新信息的消息。这个微服务负责将消息封装成RocketMQ所能识别的格式，并发送到指定的Topic上。

   下面的代码展示了如何调用用户主数据同步接口并发送MQ消息：

   ```java
       @Resource
       RocketMQTemplate mqTemplate;
   
       @Override
       public String XmlStrToDZ_Employee(@WebParam(name = "empStr") String empStr) {
           log.info("调用用户主数据同步接口：XmlStrToDZ_Employee");
           DZData dzData;
           try {
               dzData = getXmlData(empStr.trim());
           } catch(Exception e) {
               log.error("解析用户主数据报文失败", e);
               addApiInvokeLog("XmlStrToDZ_Employee", "用户主数据同步", empStr, "解析报文错误，用户主数据同步失败", WebServiceStaticData.INTERFACE_LOG_STATUS_1);
               return WebServiceStaticData.API_INVOKE_FAILURE;
           }
           String result = "";
           if(dzData != null) {
               result = employeeService.parseEmployee(dzData);
           }
           addApiInvokeLog("XmlStrToDZ_Employee", "用户主数据同步", empStr, result, WebServiceStaticData.INTERFACE_LOG_STATUS_0);
           //发送MQ消息
           mqTemplate.asyncSend("mdm_employee_data_topic", "用户主数据同步", null);
           return WebServiceStaticData.API_INVOKE_SUCCESS;
       }
   ```

   ![image-20240713154229404](C:\Users\885767\AppData\Roaming\Typora\typora-user-images\image-20240713154229404.png)

   （注：此图为MQ消息的配置界面，展示了消息发送的基本设置。消息发送和接受微服务都需要进行配置）

#### MQ消息接收与处理

1. **消息接收微服务**：在RocketMQ的Topic上，有一个或多个消息接收微服务正在监听。当新的消息到达时，这些微服务会立即响应，从MQ中拉取消息，并进行后续的业务处理。

2. **业务处理**：消息接收微服务在接收到用户主数据更新的消息后，会根据消息内容执行相应的业务逻辑。

   下面的代码展示了消息接收微服务如何接收并处理MQ中的消息：

   ```java
   @RocketMQMessageListener(topic = "mdm_employee_data_topic", consumerGroup = "dzdata-group1", messageModel = MessageModel.BROADCASTING)
   @Component
   @Slf4j
   public class employeeMQConsumer implements RocketMQListener<String> {
   
       @Autowired
       private SysUserDao sysUserDao;
       @Autowired
       private BaseSyncDataRecordDao baseSyncDataRecordDao;
       @Autowired
       private RedissonClient redissonClient;
   
       @Resource
       protected HTTPService httpService;
       @Value("${basicData.tokenDataUrl}")
       private String tokenDataUrl;
       @Value("${basicData.username}")
       private String username;
       @Value("${basicData.password}")
       private String password;
       @Value("${basicData.employee.DataUrl}")
       private String DataUrl;
       @Value("${basicData.isUse}")
       private String isUse;
   
       /**
        * 消息队列消费者锁名称。
        */
       private static final String HB_ADMIN_EMPLOYEE_MQ_CONSUMER_LOCK = "HB_ADMIN_EMPLOYEE_MQ_CONSUMER_LOCK";
   
       /**
        * 处理消息队列中的消息。
        *
        * @param message 消息内容
        */
       @Override
       public void onMessage(String message) {
           // 检查是否开启数据同步功能
           if (!Boolean.parseBoolean(isUse)) {
               log.info("未开启数据同步");
               return;
           }
           // 尝试获取分布式锁以确保同步操作的互斥
           RLock lock = redissonClient.getLock(HB_ADMIN_EMPLOYEE_MQ_CONSUMER_LOCK);
           boolean acquired = false;
           try {
               acquired = lock.tryLock(600, 5, TimeUnit.SECONDS);
               if (!acquired) {
                   log.error("获取锁失败");
                   return;
               }
               log.info("接收消息：" + message);
               processMessage(message);
           } catch (InterruptedException e) {
               log.error("获取锁失败", e);
           } finally {
               if (acquired) {
                   lock.unlock();
               }
           }
       }
   
       /**
        * 处理消息内容。
        *
        * @param message 消息内容
        */
       private void processMessage(String message){
           Integer pageNo = 1, pageSize = 50;
           String accessToken = httpService.getLoginToken(tokenDataUrl, username, password);
           if (accessToken == null) {
               log.error("未获取登录token");
           }
   
           EmployeeVo employeeVo = getLatestSyncInfo(pageSize);
   
   
           boolean hasPage = true;
           ResponseEmployeeDataVo responseEntity = null;
   
           while (hasPage) {
               try {
                   employeeVo.setPageNo(pageNo);
                   responseEntity = httpService.sendPostRequest(DataUrl, employeeVo, accessToken, ResponseEmployeeDataVo.class);
                   if (responseEntity == null) {
                       throw new RuntimeException("数据响应异常");
                   }
                   processData(responseEntity);
               } catch (Exception e) {
                   log.error("Error fetching data from DataUrl", e);
                   throw new RuntimeException("数据处理失败", e);
               }
               hasPage = responseEntity.getData().hasNextPage();
               if (hasPage) {
                   pageNo++;
               }
           }
           if (!responseEntity.getData().getList().isEmpty()) {
               saveSyncRecord(responseEntity.getData().getList().get(responseEntity.getData().getList().size() - 1));
           }
       }
   
       /**
        * 获取最新的同步信息，用于设定数据拉取的起始点。
        *
        * @param pageSize 每页数据量
        * @return 最新的同步信息
        */
       private EmployeeVo getLatestSyncInfo(Integer pageSize) {
           EmployeeVo employeeVo = new EmployeeVo();
           LambdaQueryWrapper<BaseSyncDataRecord> queryWrapper = new LambdaQueryWrapper<>();
           queryWrapper.eq(BaseSyncDataRecord::getDataType, BaseSyncDataConstant.DATA_TYPE.DZ_EMPLOYEE);
           BaseSyncDataRecord record = baseSyncDataRecordDao.selectOne(queryWrapper);
           if (record != null) {
               employeeVo.setSyncTime(record.getLastDealDate());
           }
           employeeVo.setPageSize(pageSize);
           return employeeVo;
       }
   
       /**
        * 处理获取到的员工数据。
        *
        * @param responseEntity 员工数据响应对象
        */
       private void processData(ResponseEmployeeDataVo responseEntity) {
           for (Employee employee : responseEntity.getData().getList()) {
               if (employee != null && !LTCDateConstants.HB_ZT.equals(employee.getBizUnitCode())) {
                   try {
                       parseLtcEmployee(employee);
                   } catch (Exception e) {
                       log.error("处理员工数据异常", e);
                   }
               }
           }
       }
   
       /**
        * 保存最新的同步记录。
        *
        * @param employee 最新的员工数据
        */
       private void saveSyncRecord(Employee employee) {
           BaseSyncDataRecord syncRecord = new BaseSyncDataRecord();
           syncRecord.setDataType(BaseSyncDataConstant.DATA_TYPE.DZ_EMPLOYEE);
           syncRecord.setLastDealDate(employee.getSyncTime());
           LambdaQueryWrapper<BaseSyncDataRecord> wrapper = new LambdaQueryWrapper<>();
           wrapper.eq(BaseSyncDataRecord::getDataType, BaseSyncDataConstant.DATA_TYPE.DZ_EMPLOYEE);
           if (baseSyncDataRecordDao.update(syncRecord, wrapper) == 0){
               baseSyncDataRecordDao.insert(syncRecord);
           }
       }
   
       /**
        * 解析并处理单个员工数据。
        *
        * @param employee 待处理的员工数据对象
        */
       public void parseLtcEmployee(Employee employee) {
            try {
                SysUser user = new SysUser();
                try{
                    user.setDepartmentId(Integer.valueOf(employee.getDeptCode()));
                    user.setGsDepartmentId(Integer.valueOf(employee.getDeptCode()));
                }catch (Exception e){
                    System.out.println("部门ID错误");
                }
                user.setName(employee.getEmpName());
                user.setCode(employee.getEmpCode());
                user.setIdentityType(LTCDateConstants.LTC_IDENTITYTYPE.identityCard);
                user.setIdentityCode(employee.getIdNumber());
                user.setPhoneNumber(employee.getPhoneNumber());
                user.setUpdateTime(new Date());
                user.setZtbm(employee.getBizUnitCode());
                user.setStatus(Objects.equals(employee.getOnJob(), "Y") ? 1 : 0);
                String sexType = employee.getEmpSex();
                user.setGender(sexType.equals(LTCDateConstants.LTC_SEXTYPE.man) ? "man"
                        : sexType.equals(LTCDateConstants.LTC_SEXTYPE.woman) ? "woman"
                        : "");
                user.setType(LTCDateConstants.LTC_TYPE.in);
                user.setAvailable(LTCDateConstants.LTC_AVAILABLE.y);
                user.setSource(BaseSyncDataConstant.DATA_SOURCE.mdmSys);
   
                SysUser userTemp = sysUserDao.getUserByCode(employee.getEmpCode());
                if (userTemp != null){
                    String delFlag = userTemp.getDelFlag();
                    if (Objects.equals(delFlag, LTCDateConstants.DEL_FLAG.y)){
                        BeanUtils.copyProperties(user, userTemp,"id");
                        sysUserDao.setDelFlagN(userTemp.getId());
                        sysUserDao.updateById(userTemp);
                    }else {
                        BeanUtils.copyProperties(user, userTemp,"id");
                        sysUserDao.updateById(userTemp);
                    }
                }else {
                    user.setPassword(MD5Util.encode(LTCDateConstants.DEFAULT_PASSWORD));
                    sysUserDao.insert(user);
                }
            } catch(Exception e) {
                log.error("员工主数据同步错误",e);
            }
   
       }
   
   }
   ```

#### 总结

通过RocketMQ的应用，我们成功实现了股份变动时用户主数据的快速同步和跨系统处理。这种方式不仅提高了数据的实时性和一致性，还降低了系统间的耦合度，使得各个系统可以更加独立和灵活地发展。同时，RocketMQ的高可用性和可靠性也为我们的数据同步工作提供了坚实的保障。

