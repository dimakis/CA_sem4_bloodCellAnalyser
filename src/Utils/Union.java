package Utils;


import static Utils.Find.*;

public class Union {

    public static void unionByHeightAndSize(double[] arr, int p, int q) {
        int rootP = findDoubleArr(arr, p);
        int rootQ = findDoubleArr(arr, q);
        if ((int) arr[rootP] == (int) arr[rootQ]) {
            int sizeP = -(int) ((arr[rootP] - (int) arr[rootP]) * 1000000);
            int sizeQ = -(int) ((arr[rootQ] - (int) arr[rootQ]) * 1000000);
//            int sizeP = -(int) ((arr[rootP])* -1000000); //- (int) arr[rootP]) * 1000000);
//            int sizeQ = -(int) ((arr[rootQ])* -1000000);// - (int) arr[rootQ]) * 1000000);

            if (sizeP > sizeQ) arr[rootQ] = rootP;
            else arr[rootP] = rootQ;
        } else if (arr[rootP] > arr[rootQ]) arr[rootP] = rootQ;
        else arr[rootQ] = rootP;

        // initial value of array must be -1.000001
    }

    public static void quickUnion(int[] arr, int p, int q) {
        arr[findIntArr(arr, q)] = findIntArr(arr, p);
//        arr[q]=findIntArr(arr,p);   }
    }
}
