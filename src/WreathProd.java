import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxh on 28.12.16.
 */
public class WreathProd<T> {
  private List<T> myVector;
  private int[] myPermutation;
  private GroupStructure<T> structure;

  public void setComponent(T c, int i) {
    myVector.set(i, c);
  }

  public void setVector(List<T> vector) {
    this.myVector = vector;
  }

  public void setMyPermutation(int[] p) {
    myPermutation = p;
  }

  @Override
  public String toString() {
    return "("+ myVector.toString()+"; "+printPermutation(myPermutation)+")";
  }

  public static String printPermutation(int[] permutation) {
    boolean[] alreadyPrinted = new boolean[permutation.length];
    for (int i=0;i<alreadyPrinted.length;i++) alreadyPrinted[i] = false;
    boolean b;
    String result = "";
    int fNP;
    do {
      fNP = -1;
      for (int i=0; i<alreadyPrinted.length; i++) if(!alreadyPrinted[i]) {
        fNP = i;
        break;
      }
      if (fNP!=-1){
        if (permutation[fNP] == fNP) {
          alreadyPrinted[fNP] = true;
          continue;
        }
        result += "(";
        boolean beginFlag = true;
        while (!alreadyPrinted[fNP]) {
          alreadyPrinted[fNP] = true;
          if (!beginFlag) result += ", ";
          result += (fNP+1);
          fNP = permutation[fNP];
          beginFlag = false;
        }
        result +=")";
      }
    } while (fNP != -1);
    return result;
  }

  private void doInitialize(int len, GroupStructure<T> structure) {
    myVector = new ArrayList<T>();
    myPermutation = unitPerm(len);
    this.structure = structure;
    for (int i = 0; i < len; i++) {
      myVector.add(structure.unit());
    }
  }

  public WreathProd(int len, GroupStructure<T> structure) {
    doInitialize(len, structure);
  }

  public WreathProd(int len, GroupStructure<T> structure, int a, int b) {
    doInitialize(len, structure);
    assert (a < length() && b < length() && a >= 0 && b >= 0);
    myPermutation[a] = b;
    myPermutation[b] = a;
  }

  public WreathProd(GroupStructure<T> structure, List<T> vector, int[] permutation) {
    myPermutation = permutation;
    myVector = vector;
    this.structure = structure;
  }

  public WreathProd(WreathProd<T> a) {
    this.structure = a.structure;
    this.myPermutation = invert(a.myPermutation);
    this.myVector = componentwise_inv(structure, permute(a.myVector, invert(a.myPermutation)));
  }

  public WreathProd(WreathProd<T> a, WreathProd<T> b) {
    assert (a.length() == b.length());
    assert (a.structure.equals(b.structure));
    this.structure = a.structure;
    int[] pinv = invert(a.myPermutation);
    this.myVector = componentwise_mul(structure, a.myVector, permute(b.myVector, pinv));
    this.myPermutation = mul(a.myPermutation, b.myPermutation);
  }

  public int length() {
    assert (myVector.size() == myPermutation.length);
    return myVector.size();
  }

  public GroupStructure<T> getStructure() {
    return structure;
  }

  public List<T> act(List<T> vec) {
    WreathProd<T> wp = new WreathProd<T>(length(), structure);
    wp.myVector = vec;
    return new WreathProd<T>(this, wp).myVector;
  }

  public static int[] unitPerm(int len) {
    int[] result = new int[len];
    for (int i=0; i<len; i++) result[i]=i;
    return result;
  }

  public static int[] invert(int[] p) {
    int[] result = new int[p.length];
    for (int i = 0; i < result.length; i++) result[p[i]] = i;
    return result;
  }

  public static int[] mul(int[] a, int[] b) {
    assert (a.length == b.length);
    int[] result = new int[a.length];
    for (int i = 0; i < a.length; i++) {
      assert (a[i] >= 0 && a[i] < a.length);
      result[i] = b[a[i]];
    }
    return result;
  }

  public static <TS> List<TS> permute(List<TS> source, int[] p) {
    List<TS> result = new ArrayList<TS>();
    assert (source.size() == p.length);
    for (int aP : p) result.add(source.get(aP));
    return result;
  }

  public static <TS> List<TS> componentwise_mul(GroupStructure<TS> groupStructure, List<TS> l1, List<TS> l2) {
    assert (l1.size() == l2.size());
    List<TS> result = new ArrayList<TS>();
    for (int i = 0; i < l1.size(); i++) result.add(groupStructure.mul(l1.get(i), l2.get(i)));
    return result;
  }

  public static <TS> List<TS> componentwise_inv(GroupStructure<TS> groupStructure, List<TS> l1) {
    List<TS> result = new ArrayList<TS>();
    for (int i = 0; i < l1.size(); i++) result.add(groupStructure.inv(l1.get(i)));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof WreathProd) {
      WreathProd wp = (WreathProd) obj;
      if (wp.myPermutation.length != myPermutation.length) return false;
      for (int i=0; i<myPermutation.length; i++) if (wp.myPermutation[i] != myPermutation[i]) return false;
      return wp.myVector.equals(myVector);
    }
    return false;
  }

  public List<T> getVector(){
    return myVector;
  }

  public int[] getPermutation(){
    return myPermutation;
  }
}
