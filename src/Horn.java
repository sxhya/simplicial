import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxh on 07.01.17.
 */
class Horn<T> {
  private SimplicialStructure<T> mySimplicialStructure;
  private List<T> mySimplices = new ArrayList<>();

  public Horn(SimplicialStructure<T> str, List<T> simplices){
    mySimplicialStructure = str;
    mySimplices = simplices;

    if (!isValid())
      throw new IllegalArgumentException();
    else System.out.println("Valid horn");
  }

  Horn(SimplicialStructure<T> str, T t, int k) {
    mySimplicialStructure = str;
    int n = str.getLevel(t);
    for (int i=0; i<=n; i++) mySimplices.add(str.face(t, i));
    mySimplices.set(k, null);

    if (!isValid()) throw new IllegalArgumentException();
    else System.out.println("Valid horn");
  }

  private boolean isValid() {
    int n = getIndex();
    int k = getHoleNum();

    if (n + 1 != mySimplices.size()) return false;

    for (int i=0; i<=n; i++)
      for (int j=i+1; j<=n; j++)
        if (i != k && j != k) {
          T x = mySimplicialStructure.face(mySimplices.get(j), i);
          T y = mySimplicialStructure.face(mySimplices.get(i), j-1);
          if (!x.equals(y)) return false;
        }
    return true;
  }

  int getHoleNum(){
    int prev = -1;
    for (int i = 0; i < mySimplices.size(); i++) {
      if (prev == -1 && mySimplices.get(i) == null) prev = i; else
        if (mySimplices.get(i) == null) throw new IllegalStateException();
    }
    if (prev == -1) throw new IllegalStateException();
    return prev;
  }

  int getIndex(){
    if (mySimplices.isEmpty()) throw new IllegalStateException();
    int index = -1;
    for (T t : mySimplices) if (t != null) {
      if (index == -1) index = mySimplicialStructure.getLevel(t); else
      if (mySimplicialStructure.getLevel(t) != index) throw new IllegalStateException();
    }
    if (index == -1) throw new IllegalStateException();
    return index+1;
  }

  T getSimplex(int i) {
    return mySimplices.get(i);
  }

  SimplicialStructure<T> getSimplicialStructure() {
    return mySimplicialStructure;
  }

  @Override
  public String toString() {
    String result = "";
    boolean needComma = false;
    for (T t : mySimplices) {
      if (needComma) result += ", ";
      needComma = true;
      if (t != null) result += t; else result += "==";
    }
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Horn) {
      return ((Horn) o).mySimplices.equals(this.mySimplices);
    }
    return false;
  }
}
