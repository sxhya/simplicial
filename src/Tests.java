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
    WreathProd<String> wreathProd1 = new WreathProd<String>(new WreathProd<String>(3, ags, 0, 1), new WreathProd<String>(3, ags, 0, 2));
    wreathProd1.setComponent("a", 0);
    wreathProd1.setComponent("b", 1);
    wreathProd1.setComponent("c", 2);
    List<String> vec = new ArrayList<String>();
    vec.add("d"); vec.add("e"); vec.add("f");
    System.out.print(wreathProd1+"\n");
    System.out.println(wreathProd1.act(vec));
    /*
    WreathProd<String> wreathProd2 = new WreathProd<String>(3, ags);
    wreathProd2.setComponent("d", 0);
    wreathProd2.setComponent("e", 1);
    wreathProd2.setComponent("f", 2);
    System.out.println(new WreathProd<String>(wreathProd1, wreathProd2));*/
  }

}
