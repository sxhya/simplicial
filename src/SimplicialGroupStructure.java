/**
 * Created by sxh on 07.01.17.
 */
public interface SimplicialGroupStructure<T> extends SimplicialStructure<T>, GroupStructure<T> {

  public static<T> T findFiller(SimplicialGroupStructure<T> str, Horn<T> horn) {
    assert (horn.getSimplicialStructure().equals(str));

    int k = horn.getHoleNum();
    int n = horn.getIndex();

    if (k==0) {
      T w = str.degeneracy(horn.getSimplex(n), n-1);
      for (int i = n-1; i > 0; i--)
        w = str.mul(str.mul(w, str.inv(str.degeneracy(str.face(w, i), i-1))), str.degeneracy(horn.getSimplex(i), i-1));
      return w;
    }

    if (k==n) {
      T w = str.degeneracy(horn.getSimplex(0), 0);
      for (int i = 1; i < n; i++)
        w = str.mul(str.mul(w, str.inv(str.degeneracy(str.face(w, i), i))), str.degeneracy(horn.getSimplex(i), i));
      return w;
    }

    if (0 < k && k < n) {
      T w = str.degeneracy(horn.getSimplex(0), 0);
      for (int i = 1; i < k; i++)
        w = str.mul(str.mul(w, str.inv(str.degeneracy(str.face(w, i), i))), str.degeneracy(horn.getSimplex(i), i));
      w = str.mul(str.mul(w, str.inv(str.degeneracy(str.face(w, n), n-1))), str.degeneracy(horn.getSimplex(n), n-1));
      for (int i = n-1; i > k; i--)
        w = str.mul(str.mul(w, str.inv(str.degeneracy(str.face(w, i), i-1))), str.degeneracy(horn.getSimplex(i), i-1));
      return w;
    }

    throw new IllegalStateException();
  }
}
