import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.*;


public class slidingWindow {
    public static void main(String[] args) {
        slidingWindowSoultion sliding = new slidingWindowSoultion();
        int [] nums = {3,9,6};
        int [] grumpy = {9930,9923,9983,9997,9934,9952,9945,9914,9985,9982,9970,9932,9985,9902,9975,9990,9922,9990,9994,9937,9996,9964,9943,9963,9911,9925,9935,9945,9933,9916,9930,9938,10000,9916,9911,9959,9957,9907,9913,9916,9993,9930,9975,9924,9988,9923,9910,9925,9977,9981,9927,9930,9927,9925,9923,9904,9928,9928,9986,9903,9985,9954,9938,9911,9952,9974,9926,9920,9972,9983,9973,9917,9995,9973,9977,9947,9936,9975,9954,9932,9964,9972,9935,9946,9966};
        String s = "aabaaaacaabc";
        sliding.takeCharacters(s, 2);

    }

}

class slidingWindowSoultion{
    /**
     *  1456. 定长子串中元音的最大数目
     * @param s 字符串 s
     * @param k 整数 k
     * @return 返回字符串 s 中长度为 k 的单个子字符串中可能包含的最大元音字母数。
     */
    public int maxVowels(String s, int k) {
        int left = 0, right = 0,flag = 0, res = 0;
        Set<Character> characterSet = new HashSet<>();
        characterSet.add('a');
        characterSet.add('e');
        characterSet.add('i');
        characterSet.add('o');
        characterSet.add('u');
        for (int i = 0; i < s.length(); i++){
            if ((i - left) >= k){
                if (characterSet.contains(s.charAt(left))){
                    flag--;
                }
                if (characterSet.contains(s.charAt(i))){
                    flag++;
                    res = Math.max(res, flag);
                }
                left++;
            }else {
                if (characterSet.contains(s.charAt(i))){
                    flag++;
                    res = Math.max(res, flag);
                }
            }
        }
        return res;
    }

    /**
     * 2269. 找到一个数字的 K 美丽值
     * @param num 子字符串能整除 num 。
     * @param k 子字符串长度为 k 。
     * @return 返回 num 的 k 美丽值
     */
    public int divisorSubstrings(int num, int k) {
        String numStr = Integer.toString(num);
        int left = 0;
        int res = 0;
        for (int i = k - 1; i < numStr.length(); i++){
            String sub = numStr.substring(i - k + 1, i + 1);
            int subNum = Integer.parseInt(sub);
            if (subNum != 0 && num % subNum == 0){
                res++;
            }
        }
        return res;
    }

    public int minimumDifference(int[] nums, int k) {
        Arrays.sort(nums);
        int left = 0;
        int res = Integer.MAX_VALUE;
        for (int i = k - 1; i < nums.length; i++){
            left = i - k + 1;
            res = Math.min(res, nums[i] - nums[left]);
        }
        return res;
    }

    public double findMaxAverage(int[] nums, int k) {
        int left = 0;
        int right = 0;
        int sum = 0;
        int res = 0;
        for (int i = 0; i < k; i++){
            sum += nums[i];
        }
        res = sum;
        for (int i = k; i < nums.length; i++){
            sum += nums[i] - nums[i - k];
            res = Math.max(res, sum);
        }
        return res * 1.0 / k;
    }

    public int numOfSubarrays(int[] arr, int k, int threshold) {
        int sum = 0;
        int res = 0;
        for (int i = 0; i < k; i++){
            sum += arr[i];
        }
        if (sum / k >= threshold){
            res++;
        }
        for (int i = k; i < arr.length; i++){
            sum += arr[i] - arr[i - k];
            if (sum / k >= threshold){
                res++;
           }
       }
        return res;
    }

    public int[] getAverages(int[] nums, int k) {
        int[] res = new int[nums.length];
        Arrays.fill(res, -1);
        if(nums.length <= (int)(2 * k))return res;
        long flag = 0;
        for(int i = 0; i <= k * 2; i++){
            flag += nums[i];
        }
        res[k] = (int)(flag / (2 * k + 1));
        for(int i = k + 1; i < nums.length - k; i++){
            flag += nums[i + k] - nums[i - k - 1];
            if(k == 0){
                return nums;
            }else{
                res[i] =(int)( flag / (2 * k + 1));
            }
        }
        return res;
    }

    public int minimumRecolors(String blocks, int k) {
        int res = 0;
        for (int i = 0; i < k; i++){
            if (blocks.charAt(i) == 'W'){
                res++;
            }
        }
        for(int i = k; i < blocks.length(); i++){
            if(blocks.charAt(i - k) == 'W'){
                res--;
            }
            if(blocks.charAt(i) == 'W'){
                res++;
            }
        }
        return res;
    }

    public int maxSatisfied(int[] customers, int[] grumpy, int minutes) {
        int n = customers.length;
        int count = 0, res = 0;
        for(int i = 0; i < minutes; i++){
            if(grumpy[i] == 0){
                count += customers[i];
            }
        }
        res = count;
        for(int i = minutes; i < n; i++){
            if(grumpy[i - minutes] == 0){
                count -= customers[i - minutes];
            }
            if(grumpy[i] == 0){
                count += customers[i];
            }
            res = Math.max(res, count);
        }
        return res;
    }

    public long maxSumerro(List<Integer> nums, int m, int k) {

        HashMap<Integer, Integer> map = new HashMap<>();
        int flag = 0, val = 0, res = 0;
        for (int i = 0; i < k; i++){
            int num = nums.get(i);
            if (!map.containsKey(num)){
                flag++;
                val = 0;
            }else {
                val = map.get(num);
            }
            map.put(num, val + 1);
        }
        if (flag <= m){
            res++;
        }
        for (int i = k; i < nums.size(); i++){
            int rightNum = nums.get(i), leftNum = nums.get(i - k);
            if (map.get(leftNum) == 1){
                flag--;
                map.remove(leftNum);
            }else {
                int mapVal = map.get(leftNum);
                map.put(leftNum,mapVal - 1);
            }
            if (!map.containsKey(rightNum)){
                flag++;
                val = 0;
            }else {
                val = map.get(rightNum);
            }
            map.put(rightNum, val + 1);
            if (flag <= m){
                res++;
            }
        }
        return res;
    }

    public long maxSum(List<Integer> nums, int m, int k) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int size = nums.size();
        long maxResult = 0;
        long res = 0;
        if (size < k)return 0;
        for (int i = 0; i < k; i++){
            maxResult += nums.get(i);
            int flag = map.getOrDefault(nums.get(i), 0) + 1;
            map.put(nums.get(i), flag);
        }
        if (map.size() >= m){
            res = maxResult;
        }
        for (int i = k; i < size; i++){
            maxResult = maxResult - nums.get(i - k) + nums.get(i);
            int flag = map.getOrDefault(nums.get(i), 0) + 1;
            map.put(nums.get(i), flag);
            if (map.get(nums.get(i - k)) == 1){
                map.remove(nums.get(i - k));
            }else {
                map.put(nums.get(i - k), map.get(nums.get(i - k)) - 1);
            }
            if (map.size() >= m && res < maxResult){
                res = maxResult;
            }
        }
        return res;
    }

    public long maximumSubarraySum(int[] nums, int k) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int size = nums.length;
        long maxResult = 0;
        long res = 0;
        if (size < k)return 0;
        for (int i = 0; i < k; i++){
            maxResult += nums[i];
            int flag = map.getOrDefault(nums[i], 0) + 1;
            map.put(nums[i], flag);
        }
        if (map.size() == k){
            res = maxResult;
        }
        for (int i = k; i < size; i++){
            maxResult = maxResult - nums[i - k] + nums[i];
            int flag = map.getOrDefault(nums[i], 0) + 1;
            map.put(nums[i], flag);
            if (map.get(nums[i-k]) == 1){
                map.remove(nums[i - k]);
            }else {
                map.put(nums[i - k], map.get(nums[i - k]) - 1);
            }
            if (map.size() == k && res < maxResult){
                res = maxResult;
            }
        }
        return res;
    }

    public int maxScore(int[] cardPoints, int k) {
        int res = 0, flag = 0, size = cardPoints.length;
        for (int i = 0; i < k; i++){
            flag += cardPoints[i];
        }
        if (k == size)return flag;
        res = flag;
        for (int i = 0;i < k; i++){
            flag += cardPoints[size - i - 1];
            flag -= cardPoints[k - i - 1];
            if (res < flag){
                res = flag;
            }
        }
        return res;
    }

    public int minSwaps(int[] nums) {
        int n = nums.length;
        int k = 0, flag = 0, res = 0;
        for (int i = 0; i < n; i++){
            if (nums[i] == 1){
                k++;
            }
        }
        for (int i = 0; i < k; i++){
            if (nums[i] == 0){
                flag++;
            }
        }
        res = flag;
        for (int i = 0; i < n; i++){
            int j = (i + k) % n;
            if (nums[j] == 0){
                flag++;
            }
            if (nums[i] == 0){
                flag--;
            }
            if (res > flag){
                res = flag;
            }
        }
        return res;
    }

    public int[] getSubarrayBeauty(int[] nums, int k, int x) {
        int n = nums.length;
        int[] res = new int[n - k + 1];
        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (int i = 0; i < k; i++){
            int flag = map.getOrDefault(nums[i],0) + 1;
            map.put(nums[i], flag);
        }
        res[0] = getMinX(map, x);
        for (int i = k; i < n; i++){
            int flag = map.getOrDefault(nums[i],0) + 1;
            map.put(nums[i], flag);
            if (map.get(nums[i - k]) == 1){
                map.remove(nums[i - k]);
            }else {
                map.put(nums[i - k], map.get(nums[i - k]) - 1);
            }
            res[i - k + 1] = getMinX(map, x);
        }
        return res;
    }
    public int getMinX(TreeMap<Integer, Integer> map, int x){
        for (Map.Entry<Integer, Integer> entry : map.entrySet()){
            int key = entry.getKey();
            int value = entry.getValue();
            x -= value;
            if (key >= 0){
                return 0;
            }
            if (x <= 0){
                return key;
            }
        }
        return 0;
    }

    public boolean checkInclusion(String s1, String s2) {
        int[] flag = new int[26];
        int[] flags = new int[26];
        if (s2.length() < s1.length())return false;
        for (int i = 0; i < s1.length(); i++){
            flags[s1.charAt(i) - 'a']++;
            flag[s2.charAt(i) - 'a']++;
        }
        if (Arrays.equals(flag,flags))return true;

        for (int i = s1.length(); i < s2.length(); i++){
            flag[s2.charAt(i - s1.length())  - 'a']--;
            flag[s2.charAt(i) - 'a']++;
            if (Arrays.equals(flag,flags))return true;
        }
        return false;
    }

    public int longestSubarray(int[] nums) {
        int res = 0, f0 = 0, f1 = 0;
        for (int i = 0; i < nums.length; i++){
            if (nums[i] != 0){
                f0++;
                f1++;
            }else {
                f1 = f0;
                f0 = 0;
            }
            res = Math.max(f1,res);
        }
        if (res == nums.length)return res - 1;
        return res;
    }

    public int longestSemiRepetitiveSubstring(String s) {
        int res = 0, right = 0, left = 0, flag = 0;
        for (int i = 1;i < s.length(); i++){
            if (s.charAt(i) == s.charAt(i - 1)){
                flag++;
                if (flag > 1){
                    res = Math.max(i - left, res);
                    left = right;
                }
                right = i;
            }
        }
        if (res == 0)return s.length();
        return Math.max(s.length() - left, res);
    }

    public int longestSemiRepetitiveSubstring2(String S) {
        char[] s = S.toCharArray();
        int ans = 1, left = 0, same = 0, n = s.length;
        for (int right = 1; right < n; right++){
            if (s[right] == s[right - 1] && ++same > 1){
                for (left++; s[left] != s[left - 1];left++){
                    same = 1;
                }
            }
            ans = Math.max(ans, right - left + 1);
        }
        return ans;
    }

    public int totalFruit(int[] fruits) {
        HashMap<Integer,Integer> map = new HashMap<>();
        int left = 0, ans = 0;
        for (int i = 0; i < fruits.length; i++){
            int flag = map.getOrDefault(fruits[i], 0) + 1;
            map.put(fruits[i], flag);
            int temp;
            while (map.size() >= 3 && left <= i){
                temp = map.get(fruits[left]) - 1;
                map.put(fruits[left], temp);
                if (temp <= 0){
                    map.remove(fruits[left]);
                }
                left++;
            }
            ans = Math.max(i - left + 1, ans);
        }
        return ans;
    }

    public int maxSubarrayLength(int[] nums, int k) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int left = 0, res = 0;
        for (int i = 0; i < nums.length; i++){
            int tmp = map.getOrDefault(nums[i], 0) + 1;
            map.put(nums[i], tmp);
            while (map.get(nums[i]) > k && left <= i){
                tmp = map.getOrDefault(nums[left], 0) - 1;
                map.put(nums[left++], tmp);
            }
            res = Math.max(res, i - left + 1);
        }
        return res;
    }

    public int maxConsecutiveAnswerserro(String answerKey, int k) {
        int left = 0, res = 0, count = 0;
        for (int i = 0; i < answerKey.length(); i++){
            if (answerKey.charAt(i) == 'T'){
                count++;
                if (count > k){
                    res = Math.max(res, i - left);
                    while (answerKey.charAt(left++) == 'F');
                    count--;
                }
            }
        }
        if ((answerKey.charAt(answerKey.length() - 1) == 'F'  && count < k) || answerKey.charAt(answerKey.length() - 1) == 'T'  && count <= k){
            res = Math.max(res, answerKey.length() - left);
        }

            count = 0;
        left = 0;
        for (int i = 0; i < answerKey.length(); i++){
            if (answerKey.charAt(i) == 'F'){
                count++;
                if (count > k){
                    res = Math.max(res, i - left);
                    while (answerKey.charAt(left++) == 'T');
                    count--;
                }
            }
        }
        if ((answerKey.charAt(answerKey.length() - 1) == 'T'  && count < k) || answerKey.charAt(answerKey.length() - 1) == 'F'  && count <= k){
            res = Math.max(res, answerKey.length() - left);
        }
        return res;
    }

    public int maxConsecutiveAnswers(String answerKey, int k) {
        return Math.max(getCnt('T', answerKey, k), getCnt('F', answerKey, k));
    }
    int getCnt(char c, String s, int k){
        int ans = 0, n = s.length();
        for (int i = 0, j = 0, cnt = 0; i < n; i++){
            if (s.charAt(i) == c)cnt++;
            while (cnt >k){
                if (s.charAt(j) == c)cnt--;
                j++;
            }
            ans = Math.max(ans, i - j + 1);
        }
        return ans;
    }

    public int longestOnes(int[] nums, int k) {
        int left = 0,res = 0, flag = 0;
        for (int i = 0; i < nums.length; i++){
            if (nums[i] == 0){
                flag++;
                while (flag > k){
                    if (nums[left++] == 0 ){
                        flag--;
                    }
                }
            }
            res = Math.max(res, i - left + 1);
        }
        return res;
    }

    public int longestSubarray2(int[] nums, int limit) {
        int maxnum = 0, minnum = Integer.MAX_VALUE, res = 0;
        for (int i = 0; i < nums.length; i++){
            if (res >= nums.length - i)break;
            for (int j = i; j < nums.length; j++){
                maxnum = Math.max(nums[j], maxnum);
                minnum = Math.min(nums[j], minnum);
                if (maxnum - minnum > limit){
                    break;
                }else {
                    res = Math.max(res, j - i + 1);
                }
            }
            maxnum = 0;
            minnum = Integer.MAX_VALUE;
        }
        return res;
    }

    public int longestSubarray(int[] nums, int limit) {
        // 创建一个双端队列，用于存储当前滑动窗口中的递增最大值序列
        Deque<Integer> queMax = new LinkedList<Integer>();
        // 创建一个双端队列，用于存储当前滑动窗口中的递增最小值序列
        Deque<Integer> queMin = new LinkedList<Integer>();

        // 数组的长度
        int n = nums.length;

        // 滑动窗口的左边界和右边界
        int left = 0, right = 0;

        // 存储最长子数组的长度
        int ret = 0;

        // 遍历数组
        while (right < n) {
            // 当queMax不为空且当前元素大于queMax的最后一个元素时，移除queMax的最后一个元素
            // 以保证queMax中的元素是递增的，移除的数据是在right之前的到这个元素之前的最大值
            while (!queMax.isEmpty() && queMax.peekLast() < nums[right]) {
                queMax.pollLast();
            }
            // 同理，当queMin不为空且当前元素小于queMin的最后一个元素时，移除queMin的最后一个元素
            // 以保证queMin中的元素是递增的
            while (!queMin.isEmpty() && queMin.peekLast() > nums[right]) {
                queMin.pollLast();
            }

            // 将当前元素加入queMax和queMin
            queMax.offerLast(nums[right]);
            queMin.offerLast(nums[right]);

            // 当queMax和queMin都不为空，且queMax的第一个元素与queMin的第一个元素之差大于limit时
            // 需要移动左边界，并更新queMax和queMin
            while (!queMax.isEmpty() && !queMin.isEmpty() && queMax.peekFirst() - queMin.peekFirst() > limit) {
                // 如果左边界的元素等于queMin的第一个元素，移除queMin的第一个元素
                if (nums[left] == queMin.peekFirst()) {
                    queMin.pollFirst();
                }
                // 如果左边界的元素等于queMax的第一个元素，移除queMax的第一个元素
                if (nums[left] == queMax.peekFirst()) {
                    queMax.pollFirst();
                }
                // 左边界右移
                left++;
            }

            // 更新最长子数组的长度
            ret = Math.max(ret, right - left + 1);

            // 右边界右移
            right++;
        }

        // 返回最长子数组的长度
        return ret;
    }

    public int longestNiceSubarray(int[] nums) {
        int ans = 1, left = 0;
        for (int i = 1; i < nums.length; i++){
            for (int j = i-1; j >= left; j--){
                if ((nums[j] & nums[i]) == 0){
                    ans = Math.max(ans, i - j);
                }else {
                    left = j + 1;
                }
            }
        }
        return ans;
    }

    public int minOperations(int[] nums, int x) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        int target = sum - x;
        if (target < 0) {
            return -1;
        }
        int ans = -1;
        int cur = 0;
        int left = 0;
        for (int right = 0; right < nums.length; right++) {
            cur += nums[right];
            while (cur > target) {
                cur -= nums[left++];
            }
            if (cur == target){
                ans = Math.max(ans, right - left);
            }
        }
        return ans == -1 ? -1 : nums.length - ans - 1;
    }

    public int maxFrequencycopy(int[] nums, int k) {
        int n = nums.length;
        int left = 0, count = 0, ans = 1;
        Arrays.sort(nums);
        for (int i = 0; i < n - 1; i++){
            count += nums[i - 1] - nums[i];
            while (count > k){
                count -= nums[left + 1] - nums[left];
                left++;
            }
            ans = Math.max(ans, i - left + 1);
        }
        return ans;
    }
    public int maxFrequency(int[] nums, int k) {
        Arrays.sort(nums);
        int n = nums.length;
        long total = 0;
        int l = 0, res = 0;
        for (int r = 1; r < n; ++r){
            total += (long) (nums[r] - nums[r - 1]) * (r - l);
            while (total > k){
                total -= nums[r] - nums[l];
                ++l;
            }
            res = Math.max(res,r - l + 1);
        }
        return res;
    }

    public int takeCharacterserro(String s, int k) {
        if(k == 0)return 0;
        if (k > s.length())return -1;
        int l = -1, r = s.length(), flag = 0;
        HashMap<Character, Integer> map = new HashMap<>();
        map.put('a',k);
        map.put('b',k);
        map.put('c',k);
        while (++l < r && map.get(s.charAt(l))  != null && map.get(s.charAt(l)) > 0){
            map.put(s.charAt(l),map.get(s.charAt(l)) - 1);
            if (map.get(s.charAt(l))  != null && map.get(s.charAt(l)) == 0){
                map.remove(s.charAt(l));
            }
        }
        if (l >= r - 1 && !map.isEmpty()){
            return -1;
        }
        while (--r > l &&map.get(s.charAt(r))  != null&& map.get(s.charAt(r)) > 0){
            map.put(s.charAt(r),map.get(s.charAt(r)) - 1);
            if (map.get(s.charAt(r))  != null && map.get(s.charAt(r)) == 0){
                map.remove(s.charAt(r));
            }
        }
        if (l >= r - 1 && !map.isEmpty()){
            return -1;
        }
        for (int i = l; i <= r;i++){
            if (map.get(s.charAt(i))  != null && map.get(s.charAt(i)) > 0){
                map.put(s.charAt(i),map.get(s.charAt(i)) - 1);
                flag = Math.max(Math.min(r - i + 1, i - l + 1), flag);
            }
            if (map.get(s.charAt(i))  != null && map.get(s.charAt(i)) == 0){
                map.remove(s.charAt(i));
            }
        }
        if (!map.isEmpty()){
            return -1;
        }
        return flag + (s.length() - r - 1) + l;
    }

    public int takeCharacters(String s, int k) {
        if(k == 0)return 0;
        if (k > s.length())return -1;
        int[] chars = new int[3];
        int[] flag = new int[3];
        for (int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            chars[c - 'a']++;
        }

        flag['a' - 'a'] = chars['a' - 'a'] - k;
        flag['b' - 'a'] = chars['b' - 'a'] - k;
        flag['c' - 'a'] = chars['c' - 'a'] - k;
        for (int cs : flag){
            if (cs < 0)return -1;
        }
        int l = 0, r = l, count = 0;
        for (;r < s.length(); r++){
            char tmpr = s.charAt(r);
            flag[tmpr - 'a']--;
            while (flag[tmpr - 'a'] < 0){
                char tmpl = s.charAt(l++);
                flag[tmpl - 'a']++;
            }
            count = Math.max(count,r - l + 1);
        }
        return s.length() - count;
    }

    public int longestEqualSubarray(List<Integer> nums, int k) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int maxCount = 0, l = 0;
        for (int i = 0; i < nums.size(); i++){
            int flag = map.getOrDefault(nums.get(i), 0) + 1;
            map.put(nums.get(i), flag);
            if (flag > maxCount){
                maxCount = flag;
            }
            while (i - flag - l > k){
                int numb = nums.get(l);
                int tmp = map.get(numb);
                map.put(numb, tmp - 1);
            }
        }
        return 0;
    }

    public int balancedString(String s) {

    }
}