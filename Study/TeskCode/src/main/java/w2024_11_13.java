import java.util.*;

public class w2024_11_13 {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int [] nums1 = {1,2,1};
        int [] nums2 = {1,3,4,2};
        solution.nextGreaterElements(nums1);
    }
}

class Solution {
    public class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        int n1 = nums1.length, n2 = nums2.length;
        int[] res = new int[n1];
        HashMap<Integer, Integer> map = new HashMap<>();
        Deque<Integer> stack = new ArrayDeque<Integer>();
        for (int i = n2 - 1; i >= 0; i--){
            while (!stack.isEmpty() && stack.peek() < nums2[i]){
                stack.pop();
            }
            int flag = stack.isEmpty() ? -1 : stack.peek();
            stack.push(nums2[i]);
            map.put(nums2[i], flag);
        }
        for (int i = 0; i < n1; i++){
            res[i] = map.get(nums1[i]);
        }
        return res;
    }
    public int[] nextGreaterElements(int[] nums) {
        int n = nums.length;
        int[] res = new int[n];
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 2 * n - 1; i >= 0; i--){
            int j = i % n;
            while (!stack.isEmpty() && stack.peek() <= nums[j]){
                stack.pop();
            }
            if (stack.isEmpty()){
                res[j] = -1;
            }
            else {
                res[j] = stack.peek();
            }
            stack.push(nums[j]);
        }
        return res;
    }

    private int[] nextLargerNodesAns;
    private final Deque<Integer> nextLargerNodesSt = new ArrayDeque<Integer>();
    public int[] nextLargerNodes(ListNode head) {
        recursionNextLarger(head,0);
        return nextLargerNodesAns;
    }
    public void recursionNextLarger(ListNode head, int i){
        if (head == null){
            nextLargerNodesAns = new int[i];
            return;
        }
        recursionNextLarger(head.next, i + 1);
        while (!nextLargerNodesSt.isEmpty() && nextLargerNodesSt.peek() < head.val){
            nextLargerNodesSt.pop();
        }
        if (!nextLargerNodesSt.isEmpty()){
            nextLargerNodesAns[i] = nextLargerNodesSt.peek();
        }else {
            nextLargerNodesAns[i] = -1;
        }
        nextLargerNodesSt.push(head.val);
    }
}



