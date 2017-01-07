/**
 * Created by sxh on 28.12.16.
 */
public class WreathStructure<T> implements GroupStructure<WreathProd<T>> {
  private GroupStructure<T> myStructure;
  private int myLen;

  public WreathStructure(GroupStructure<T> structure, int n) {
    myStructure = structure;
    myLen = n;
  }

  @Override
  public WreathProd<T> unit() {
    return new WreathProd<T>(myLen, myStructure);
  }

  @Override
  public WreathProd<T> inv(WreathProd<T> a) {
    return new WreathProd<T>(a);
  }

  @Override
  public WreathProd<T> mul(WreathProd<T> a, WreathProd<T> b) {
    return new WreathProd<T>(a, b);
  }
}
