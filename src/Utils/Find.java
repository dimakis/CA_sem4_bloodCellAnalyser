package Utils;

public class Find {
    //iterative
    public static int findDoubleArr(double[] arr, int id) {
        while (arr[id] > 0)
            id = (int) arr[id];
            return id;

    }

    //iterative
    public static int findIntArr2(int[] a, int id) {
        if (a[id] == -1) return a[id];
        while (a[id] != id) id = a[id];
        return id;
    }
    //recursive
    public static int findIntArr3(int[] a, int id) {
        if (a[id] == -1) return a[id];
        if (a[id] == id) return id;
        else
        return findIntArr3(a,a[id]);
    }

    //iterative with path compression
    public static int findIntArr(int[] arr, int id) {
        if (arr[id] == -1) return -1;
        while (arr[id] != id) {
//        arr[id] = arr[arr[id]];
        id=arr[id];
//        System.out.println(id);
        }
        return id;
        }
    }
