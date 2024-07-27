import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Soultion S = new Soultion();
        int[] nums = {6,3,10,8,2,10,3,5,10,5,3};
        S.rob(nums);
    }
}

class Soultion{

    public int minCostClimbingStairs(int[] cost) {
        if(cost.length == 1)return cost[0];
        if(cost.length == 2)return Math.min(cost[0],cost[1]);
        for(int i = 2; i < cost.length; i++){
            cost[i] = Math.min(cost[i - 1], cost[i - 2]) + cost[i];
        }
        return Math.min(cost[cost.length - 1], cost[cost.length - 2]);
    }

    public int combinationSum4(int[] nums, int target) {
        int[] flag = new int[target + 1];
        Arrays.sort(nums);
        flag[0] = 0;
        for (int i = 1; i <= target; i++){
            for (int j = 0; j < nums.length; j++) {
                if (nums[j] <= i){
                    flag[i] =flag[i] + flag[i - nums[j]] + 1;
                }else {
                    break;
                }
            }
        }
        return flag[target];
    }

    public int countGoodStrings(int low, int high, int zero, int one) {
        final int MOD = (int)1e9 + 7;
        int res = 0;
        int[] dp = new int[high + 1];
        dp[0] = 1;
        for (int i = 1; i <= high; i++) {
           if (i - zero >= 0){
               dp[i] = (dp[i] + dp[i - zero]) % MOD;
           }
            if (i - one >= 0){
                dp[i] = (dp[i] + dp[i - one]) % MOD;
            }
            if (i >= low){
                res = (res + dp[i]) % MOD;
            }
        }
        return res;
    }

    public int countTexts(String pressedKeys) {
        int total = pressedKeys.length();
        final int MOD = (int)1e9 + 7;
        int flagsize = total + 1 > 3 ? total + 1 : 4;
        long[] flag1 = new long[flagsize];
        flag1[0] = 1;
        flag1[1] = 1;
        flag1[2] = 2;
        for (int i = 3; i < total + 1; i++) {
            flag1[i] = ((flag1[i - 1] + flag1[i - 2]) % MOD + flag1[i - 3])% MOD;
        }
        long[] flag2 = new long[flagsize];
        flag2[0] = 1;
        flag2[1] = 1;
        flag2[2] = 2;
        flag2[3] = 4;
        for (int i = 4; i < total + 1; i++) {
            flag2[i] = (((flag2[i - 1] + flag2[i - 2] )% MOD + (flag2[i - 3]) % MOD  + flag2[i - 4] % MOD)) % MOD;
        }
        long res = 1;
        char guard = pressedKeys.charAt(0);
        int left = 0, dif = 0;

        for (int i = 1; i < pressedKeys.length(); i++) {
            if (pressedKeys.charAt(i) != guard){
                dif = i - left;
                left = i;
                if (guard == '9' || guard == '7'){
                    res = (res * flag2[dif]) % MOD;
                }else {
                    res = (res * flag1[dif]) % MOD;
                }
                guard = pressedKeys.charAt(i);
            }
        }
        if (guard == '9' || guard == '7'){
            res = (res * flag2[total - left]) % MOD;
        }else {
            res = (res * flag1[total - left]) % MOD;
        }
        return (int)res;
    }

    public int rob1(int[] nums) {
        int len = nums.length;
        if (len == 3)return Math.max(nums[0] + nums[2], nums[1]);
        if (len == 2)return Math.max(nums[0], nums[1]);
        if (len == 1)return nums[0];
        nums[2] = nums[0] + nums[2];
        for (int i = 3; i < len; i++) {
            nums[i] += Math.max(nums[i - 2], nums[i - 3]);
        }
        return Math.max(nums[len - 1], nums[len - 2]);
    }

    public int deleteAndEarn(int[] nums) {
        int[] dp = new int[1005];
        int[] sum = new int[1005];
        for (int num: nums) {
            sum[num] += num;//获取集合数组
        }
        dp[1] = sum[1];
        for (int i = 2; i < 1001;i++){
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + i * sum[i]);
        }
        return dp[1000];
    }

    public int countHousePlacements(int n) {
        final long MOD = (long)(1e9 + 7);
        long[] flag = new long[n + 1];
        flag[0] = 1;
        flag[1] = 2;
        long res = 0;
        for (int i = 2; i <= n; i++){
            flag[i] = (flag[i - 1] + flag[i -2]) % MOD;
        }
        return (int)(flag[n] * flag[n] % MOD);
    }

    public int roberro(int[] nums) {
        if (nums.length == 1)return nums[0];
        if (nums.length == 2)return Math.max(nums[0], nums[1]);
        int[] nums0 = nums.clone();
        if (nums.length == 3){
            return Math.max(nums[0],Math.max(nums[1],nums[2]));
        }else {
            nums0[2] += nums0[0];
            nums0[3] += nums0[0];
            nums[2] = Math.max(nums[1],nums[2]);
        }
        for (int i = 4; i < nums0.length - 1; i++){
            nums0[i] = Math.max(nums0[i - 2] + nums0[i], nums0[i - 1]);
        }
        for (int i = 3; i < nums.length; i++){
            nums[i] = Math.max(nums[i - 2] + nums[i], nums[i - 1]);
        }
        int maxnums = Math.max(nums[nums.length - 1], nums[nums.length - 2]);
        int maxnums0 = Math.max(nums0[nums0.length - 2], nums0[nums0.length - 3]);
        return Math.max(maxnums0, maxnums);
    }

    public int rob(int[] nums) {
        if (nums.length == 1)return nums[0];
        if (nums.length == 2)return Math.max(nums[0], nums[1]);
        int[] nums0 = nums.clone();
        if (nums.length == 3){
            return Math.max(nums[0],Math.max(nums[1],nums[2]));
        }else {
            nums0[2] += nums0[0];
            nums0[3] += nums0[0];
            nums[2] = Math.max(nums[1],nums[2]);
        }
        for (int i = 4; i < nums0.length - 1; i++){
            nums0[i] = Math.max(nums0[i - 2] + nums0[i], nums0[i - 1]);
        }
        for (int i = 3; i < nums.length; i++){
            nums[i] = Math.max(nums[i - 2] + nums[i], nums[i - 1]);
        }
        int maxnums = Math.max(nums[nums.length - 1], nums[nums.length - 2]);
        int maxnums0 = Math.max(nums0[nums0.length - 2], nums0[nums0.length - 3]);
        return Math.max(maxnums0, maxnums);
    }
}