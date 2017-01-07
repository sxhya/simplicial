import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxh on 07.01.17.
 */
public class Horn<T> {
  private SimplicialStructure<T> mySimplicialStructure;
  private List<T> mySimplices = new ArrayList<>();

  public Horn(SimplicialStructure<T> str, List<T> simplices){
    mySimplicialStructure = str;
    mySimplices = simplices;

    if (!isValid()) throw new IllegalArgumentException();
  }

  public boolean isValid() {
    int n = getIndex();
    int k = getHoleNum();

    if (n + 1 != mySimplices.size()) return false;

    for (int i=0; i<=n; i++)
      for (int j=i+1; i<=n; j++)
        if (i != k && j != k) {
          T x = mySimplicialStructure.face(mySimplices.get(j), i);
          T y = mySimplicialStructure.face(mySimplices.get(i), j-1);
          if (!x.equals(y)) return false;
        }
    return true;
  }

  public int getHoleNum(){
    int prev = -1;
    for (int i = 0; i < mySimplices.size(); i++) {
      if (prev == -1 && mySimplices.get(i) == null) prev = i; else
        if (mySimplices.get(i) == null) throw new IllegalStateException();
    }
    if (prev == -1) throw new IllegalStateException();
    return prev;
  }

  public int getIndex(){
    if (mySimplices.isEmpty()) throw new IllegalStateException();
    int index = mySimplicialStructure.getLevel(mySimplices.get(0));
    for (T t : mySimplices) if (mySimplicialStructure.getLevel(t)!=index) throw new IllegalStateException();
    return index+1;
  }

  public SimplicialStructure<T> getSimplicialStructure() {
    return mySimplicialStructure;
  }


}
