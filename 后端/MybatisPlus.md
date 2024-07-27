wrapper的and()用法：

```java
// WHERE xxxx!=id And ( xxxx=ANo or xxxx=BNo)
            LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.ne(Student::getId,sysstudents.getId());
            queryWrapper.and((wrapper)->{
                wrapper.eq(Student::getANo,sysstudents.getBillNo())
                        .or().eq(Student::getBNo,sysstudents.getBillNo());
            });
```



