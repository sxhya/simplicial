import java.util.List;

/**
 * Created by sxh on 28.12.16.
 */
class ChainLink<T> {
    WreathProd<T> g0;
    WreathProd<T> g1;
    List<T> x0;
    List<T> x1;

    ChainLink(WreathProd<T> g0, WreathProd<T> g1, List<T> x0, List<T> x1) {
        this.g0 = g0;
        this.g1 = g1;
        this.x0 = x0;
        this.x1 = x1;
        checkValid();
    }

    ChainLink(ChainLink<T> old, List<T> newBase) {
        this.g0 = old.g1;
        this.g1 = old.g1;
        this.x0 = old.x1;
        this.x1 = newBase;
        checkValid();
    }

    ChainLink(ChainLink<T> old, int[] permutation) {
        this.g0 = old.g1;
        this.x0 = old.x1;
        this.x1 = this.x0;
        GroupStructure<T> gs = old.g1.getStructure();
        WreathProd<T> d = new WreathProd<T>(old.g1.length(), gs);
        d.setMyPermutation(permutation);
        d.setVector(WreathProd.componentwise_mul(gs, this.x0, WreathProd.componentwise_inv(gs, WreathProd.permute(this.x0, WreathProd.invert(permutation)))));
        this.g1 = new WreathProd<T>(this.g0, d);
        checkValid();
    }

    @Override
    public String toString() {
        return "[" + g0.toString() + ", " + x0.toString() + "; " + g1.toString() + ", " + x1.toString() + "]";
    }

    private void checkValid() {
        if (!isValid())
            System.err.println("Not valid");
    }

    private boolean isValid() {
        return g0.act(x0).equals(g1.act(x0)) && g0.act(x1).equals(g1.act(x1));

    }
}
