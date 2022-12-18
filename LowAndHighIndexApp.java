package gr.aueb.cf.exercises;

public class LowAndHighIndexApp {

    public static void main(String[] args) {
        int[] arr = {1, 4, 4, 4, 5, 7, 8, 8, 8, 8};
        int[] lowAndHigh;

        lowAndHigh = getLowAndHighIndexOf(arr, 8);
        System.out.printf("(%d, %d)", lowAndHigh[0] + 1, lowAndHigh[1] + 1);
    }

    public static int[] getLowAndHighIndexOf(int[] arr, int key) {
        int low = 0;
        int high = 0;
        int pivot;
        boolean found = false;

        if (arr == null) return new int[] {};

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == key) {
                low = i;
                found = true;
                break;
            }
        }

        if (!found) return new int[] {};

        high = low;
        pivot = low + 1;

        while ((pivot < arr.length) && (arr[pivot++] == key)) {
            high++;
        }

        return new int[] {low, high};
    }
}
