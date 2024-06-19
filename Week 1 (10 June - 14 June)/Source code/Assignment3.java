import java.util.ArrayList;
import java.util.List;

public class Assignment3 {
    public static List<Integer> getSecondLargestIndex(int[] arr) {
        List<Integer> indices = new ArrayList<>();
        if (arr.length < 2) {
            return indices; 
        }

        int max = Integer.MIN_VALUE;
        int secondMax = Integer.MIN_VALUE;
        int maxCount = 0;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max) {
                secondMax = max;
                indices.clear();
                max = arr[i];
                maxCount = 1;
            } else if (arr[i] == max) {
                maxCount++;
            } else if (arr[i] > secondMax) {
                secondMax = arr[i];
                indices.clear();
                indices.add(i);
            } else if (arr[i] == secondMax) {
                indices.add(i);
            }
        }

        if (maxCount == arr.length || secondMax == Integer.MIN_VALUE) {
            return new ArrayList<>();
        }

        return indices;
    }

    public static void main(String[] args) {
        int[] input = {1, 4, 3, -6, 5, 4};
        List<Integer> output = getSecondLargestIndex(input);
        System.out.println("Output: " + output);
    }
}
