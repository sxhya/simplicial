/**
 * Created by sxh on 28.12.16.
 */
public interface GroupStructure<T> {
  public T mul(T a, T b);

  public T inv(T a);

  public T unit();

  public static<T> T comm(GroupStructure<T> gs, T a, T b) {
    return gs.mul(a, gs.mul(b, gs.mul(gs.inv(a), gs.inv(b))));
  }
}
