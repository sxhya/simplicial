import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxh on 28.12.16.
 */

public class Tests {

  @Test
  public void stringGroupStructure() {
    assert (AlphabetGroupStructure.simplify("XOoKKkkoIixJLljO").equals("XoxO"));
  }

  @Test
  public void printPermutation() {
    AlphabetGroupStructure ags = new AlphabetGroupStructure();
    WreathProd<String> wreathProd1 = new WreathProd<String>(new WreathProd<>(3, ags, 0, 1), new WreathProd<>(3, ags, 0, 2));
    wreathProd1.setComponent("a", 0);
    wreathProd1.setComponent("b", 1);
    wreathProd1.setComponent("c", 2);
    List<String> vec = new ArrayList<String>();
    vec.add("d"); vec.add("e"); vec.add("f");
    System.out.print(wreathProd1+"\n");
    System.out.println(wreathProd1.act(vec));
  }

  @Test
  public void bg() {
    AlphabetGroupStructure ags = new AlphabetGroupStructure();
    ClassifyingSpace<String> cs = new ClassifyingSpace<>();
    ArrayList<String> l = new ArrayList<>();
    l.add("a"); l.add("b"); l.add("c");
    ClassifyingSpace.ClassifyingSpaceElement<String> cse = new ClassifyingSpace.ClassifyingSpaceElement<>(ags, l);
    assert SimplicialStructure.isValid(cs, cse);
  }

}
