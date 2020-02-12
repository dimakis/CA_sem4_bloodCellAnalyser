package Utils;


import static Utils.Find.find;

public class Union {

    public static void unionByHeightAndSize(double[] a, int p, int q) {
        int rootP = find(a, p);
        int rootQ = find(a, q);
        if ((int) a[rootP] == (int) a[rootQ]) {
            int sizeP = -(int) ((a[rootP] - (int) a[rootP]) * 10000);
            int sizeQ = -(int) ((a[rootQ] - (int) a[rootQ]) * 10000);
            if (sizeP > sizeQ) a[rootQ] = rootP;
            else a[rootP] = rootQ;
        } else if (a[rootP] > a[rootQ]) a[rootP] = rootQ;
        else a[rootQ] = rootP;

        // initial value of array must be -1.0001
    }
}
