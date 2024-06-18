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

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max) {
                secondMax = max;
                max = arr[i];
            } else if (arr[i] > secondMax && arr[i] != max) {
                secondMax = arr[i];
            }
        }

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == secondMax) {
                indices.add(i);
            }
        }

        return indices;
    }

    public static void main(String[] args) {
        int[] input = {1, 4, 3, -6, 5, 4};
        List<Integer> output = getSecondLargestIndex(input);
        System.out.println("Output: " + output);
    }
}