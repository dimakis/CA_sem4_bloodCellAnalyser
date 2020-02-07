package Utils;

public class DisjointSetNode<T> {
    public DisjointSetNode<?> parent= null;
    public T data;
    public int size = 1,height =1;

    public DisjointSetNode(DisjointSetNode<?> parent, T data, int size, int height) {
        this.parent = parent;
        this.data = data;
        this.size = size;
        this.height = height;
    }

    public DisjointSetNode<?> getParent() {
        return parent;
    }

    public void setParent(DisjointSetNode<?> parent) {
        this.parent = parent;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
