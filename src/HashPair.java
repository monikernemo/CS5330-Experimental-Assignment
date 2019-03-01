public class HashPair<E> {

    private E x;
    private int y;

    public HashPair (E x) {
        this.x = x;
        this.y = 1;
    }

    public HashPair (E x, int y) {
        this.x = x;
        this.y = y;
    }

    public E getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        return x.hashCode();
    }
}
