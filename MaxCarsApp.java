package gr.aueb.cf.exercises;

import java.util.Arrays;
import java.util.Comparator;

public class MaxCarsApp {

    public static void main(String[] args) {
        int[][] arr = new int[][] {{1012, 1136}, {1317, 1417}, {1015, 1326}, {1015, 1025}, {945, 1120}};
        int[][] transformed;

        transformed = transformArray(arr);
        sortByTime(transformed);

        for (int[] row : transformed) {
            System.out.print(row[0] + " ");
            System.out.println(row[1]);
        }

        System.out.println("Max Concurrent Cars: " + getMaxConcurrentCars(transformed));
    }

    public static int[][] transformArray(int[][] arr) {
        int[][] transformed = new int[arr.length*2][2];

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                transformed[i*2][0] = arr[i][0];
                transformed[i*2][1] = 1;
                transformed[i*2+1][0] = arr[i][1];
                transformed[i*2+1][1] = 0;
            }
        }
        return transformed;
    }

    public static void sortByTime(int[][] arr) {
        Arrays.sort(arr, Comparator.comparing((int[] a) -> a[0]));
    }


    public static int getMaxOnes(int[][] arr) {
        int times = 0;
        int maxTimes = 0;
        int i = 0;

        while (i < arr.length) {
            times = 0;
            while ((i < arr.length) && (arr[i++][1] == 1)) {
                times++;
            }
            if (times > maxTimes) maxTimes = times;
        }

        return maxTimes;
    }

    public static int getMaxConcurrentCars(int[][] arr) {
        int count = 0;
        int maxCount = 0;

        for (int[] ints : arr) {
            if (ints[1] == 1){
                count++;
                if (count > maxCount) maxCount = count;
            }
            else count--;
        }

        return maxCount;
    }
}

