package Utils;

public class Find {
    //iterative
    public static int find(double[] a, int id )  {
        while (a[id] > 0) id = (int)a[id];
        return id;
    }
}
