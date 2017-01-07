/**
 * Created by sxh on 28.12.16.
 */
public interface GroupStructure<T> {
  public T mul(T a, T b);

  public T inv(T a);

  public T unit();
}
