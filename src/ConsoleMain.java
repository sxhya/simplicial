import java.security.acl.Group;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sxh on 28.12.16.
 */
public class ConsoleMain {

  public static void main(String[] args) {
    AlphabetGroupStructure ags = new AlphabetGroupStructure();
    List<String> basePoint = new ArrayList<String>(); basePoint.add(""); basePoint.add(""); basePoint.add("");
    List<String> twistedPoint = new ArrayList<String>(); twistedPoint.add("A"); twistedPoint.add(""); twistedPoint.add("");
    List<String> twistedPointB = new ArrayList<String>(); twistedPointB.add("B"); twistedPointB.add(""); twistedPointB.add("");
    WreathProd<String> wp = new WreathProd<String>(3, ags);
    List<ChainLink<String>> path = new LinkedList<ChainLink<String>>();
    ChainLink<String> last = new ChainLink<String>(wp, wp, basePoint, twistedPoint);
    System.out.println(last); path.add(last);

    last = new ChainLink<String>(last, new int[]{1, 0 ,2});
    System.out.println(last); path.add(last);

    last = new ChainLink<String>(last, basePoint);
    System.out.println(last); path.add(last);

    last = new ChainLink<String>(last, new int[]{1, 0 ,2});
    System.out.println(last); path.add(last);

    last = new ChainLink<String>(last, twistedPointB);
    System.out.println(last); path.add(last);

    last = new ChainLink<String>(last, new int[]{2, 1 ,0});
    System.out.println(last); path.add(last);

    last = new ChainLink<String>(last, basePoint);
    System.out.println(last); path.add(last);

    last = new ChainLink<String>(last, new int[]{2, 1 ,0});
    System.out.println(last); path.add(last);

    last = new ChainLink<String>(last, new int[]{1, 0 ,2});
    System.out.println(last); path.add(last);

    last = new ChainLink<String>(last, twistedPoint);
    System.out.println(last); path.add(last);

    last = new ChainLink<String>(last, new int[]{1, 0 ,2});
    System.out.println(last); path.add(last);

    last = new ChainLink<String>(last, basePoint);
    System.out.println(last); path.add(last);

    last = new ChainLink<String>(last, new int[]{2, 1 ,0});
    System.out.println(last); path.add(last);

    last = new ChainLink<String>(last, twistedPointB);
    System.out.println(last); path.add(last);

    last = new ChainLink<String>(last, new int[]{2, 1 ,0});
    System.out.println(last); path.add(last);

    last = new ChainLink<String>(last, basePoint);
    System.out.println(last); path.add(last);

    //Project to S:

    for (int i=0; i<path.size(); i++) {
      System.out.println("i="+ i + " " + new WreathProd<String>(new WreathProd<String>(path.get(i).g0), path.get(i).g1));
    }

    List<String> com1 = new ArrayList<String>(); com1.add(""); com1.add(""); com1.add("");
    List<String> com2 = new ArrayList<String>(); com2.add(""); com2.add(""); com2.add("");

    for (int i=0; i<path.size(); i++) {
      List<String> x0 = path.get(i).x0;
      List<String> y0 = path.get(i).g0.act(x0);
      List<String> x1 = path.get(i).x1;
      List<String> y1 = path.get(i).g1.act(x1);
      List<String> diff1 = WreathProd.componentwise_mul(ags, WreathProd.componentwise_inv(ags, x0), x1);
      List<String> diff2 = WreathProd.componentwise_mul(ags, WreathProd.componentwise_inv(ags, y0), y1);
      //System.out.println("i="+ i + " [" + x0 + ", " + y0 + "; " + x1 + ", " + y1 + "]");
      com1 = WreathProd.componentwise_mul(ags, com1, diff1);
      com2 = WreathProd.componentwise_mul(ags, com2, diff2);
      System.out.println("i="+ i + " [" + diff1 + ", " + diff2 + "]" + " Cumulative = ["+com1+"; "+com2+"]");
    }

    /*WreathProd<String> wreathProd1 = new WreathProd<String>(, new WreathProd<String>(3, ags, 0, 2));
    wreathProd1.setComponent("a", 0);
    wreathProd1.setComponent("b", 1);
    wreathProd1.setComponent("c", 2); */

  }
}
