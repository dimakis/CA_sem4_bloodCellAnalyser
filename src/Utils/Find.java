package Utils;

public class Find {
    //iterative
    public static int findDoubleArr(double[] arr, int id) {
        while (arr[id] > 0)
            id = (int) arr[id];
            return id;

    }

    //iterative
    public static int findIntArr(int[] a, int id) {
        while (a[id] > 0) id = a[id];
        return id;
    }
}
