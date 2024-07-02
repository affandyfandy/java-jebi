import java.util.*;

public class Assignment4 {
    public static List<Integer> subarraySum(int[] nums) {
        List<Integer> temp = new ArrayList<>();
  
        if (nums == null || nums.length == 0) {
            return temp;
        }
        
        int pre_Sum = 0;
        Map<Integer, Integer> map = new HashMap<>();
        map.put(pre_Sum, -1);

        for (int i = 0; i < nums.length; i++) {
            pre_Sum += nums[i];
            
            if (map.containsKey(pre_Sum)) {
                temp.add(map.get(pre_Sum) + 1); 
                temp.add(i); 
                return temp;
            }
            
            map.put(pre_Sum, i);
        }
        
        return temp;
    }
    
    public static void main(String[] args) {
        int[] nums = {1, 2, 3, -6, 5, 4};

        System.out.println("Input: " + Arrays.toString(nums));
        System.out.println("Output: " + subarraySum(nums));
    }		
}