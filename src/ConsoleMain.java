import java.security.acl.Group;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sxh on 28.12.16.
 */
public class ConsoleMain {

  public static<T> FreeSimplicialAbelianGroup.LinearCombination<ClassifyingSpace.ClassifyingSpaceElement<T>> zip(GroupStructure<T> ags, List<T> l1, List<T> l2) {
    List<T> l;
    if (l1.size() != l2.size()) throw new IllegalArgumentException();

    FreeSimplicialAbelianGroup.LinearCombination<ClassifyingSpace.ClassifyingSpaceElement<T>> lin =
      new FreeSimplicialAbelianGroup.LinearCombination<>(2);

    for (int i=0; i<l1.size(); i++) {
      l = new ArrayList<>();
      l.add(l1.get(i)); l.add(l2.get(i));
      ClassifyingSpace.ClassifyingSpaceElement<T> cse = new ClassifyingSpace.ClassifyingSpaceElement<>(ags, l);
      lin = FreeSimplicialAbelianGroup.LinearCombination.add(lin, new FreeSimplicialAbelianGroup.LinearCombination<>(cse, 2));
    }
    return lin;
  }

  public static void main(String[] args) {
    AlphabetGroupStructure ags = new AlphabetGroupStructure();
    ClassifyingSpace<String> bg = new ClassifyingSpace<String>();
    FreeSimplicialAbelianGroup<ClassifyingSpace.ClassifyingSpaceElement<String>> sag = new FreeSimplicialAbelianGroup<>(bg);

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

    List<String> com1 = new ArrayList<>(); com1.add(""); com1.add(""); com1.add("");
    List<String> com2 = new ArrayList<>(); com2.add(""); com2.add(""); com2.add("");

    FreeSimplicialAbelianGroup.LinearCombination<ClassifyingSpace.ClassifyingSpaceElement<String>> com_simplex =
      new FreeSimplicialAbelianGroup.LinearCombination<>(2);

    for (int i=0; i<path.size(); i++) {
      List<String> x0 = path.get(i).x0;
      List<String> y0 = path.get(i).g0.act(x0);
      List<String> x1 = path.get(i).x1;
      List<String> y1 = path.get(i).g1.act(x1);
      List<String> diff1 = WreathProd.componentwise_mul(ags, WreathProd.componentwise_inv(ags, x0), x1);
      List<String> diff2 = WreathProd.componentwise_mul(ags, WreathProd.componentwise_inv(ags, y0), y1);
      //convert diff1 & diff2 to

      FreeSimplicialAbelianGroup.LinearCombination<ClassifyingSpace.ClassifyingSpaceElement<String>> lin =
        FreeSimplicialAbelianGroup.LinearCombination.sub(zip(ags, com1, diff1), zip(ags, com2, diff2));

      FreeSimplicialAbelianGroup.LinearCombination<ClassifyingSpace.ClassifyingSpaceElement<String>> zero_simplex =
        new FreeSimplicialAbelianGroup.LinearCombination<>(2);


      List<FreeSimplicialAbelianGroup.LinearCombination<ClassifyingSpace.ClassifyingSpaceElement<String>>> lst = new ArrayList<>();
      lst.add(zero_simplex); lst.add(null); lst.add(com_simplex); lst.add(lin);
      Horn<FreeSimplicialAbelianGroup.LinearCombination<ClassifyingSpace.ClassifyingSpaceElement<String>>> horn = new Horn<>(sag, lst);

      FreeSimplicialAbelianGroup.LinearCombination<ClassifyingSpace.ClassifyingSpaceElement<String>> filler = SimplicialGroupStructure.findFiller(sag, horn);
      com_simplex = sag.face(filler, 1);

      //System.out.println("i="+ i + " [" + x0 + ", " + y0 + "; " + x1 + ", " + y1 + "]");
      com1 = WreathProd.componentwise_mul(ags, com1, diff1);
      com2 = WreathProd.componentwise_mul(ags, com2, diff2);
      System.out.print("i="+ i + " [" + diff1 + ", " + diff2 + "]" + " Cumulative = ["+com1+"; "+com2+"]; ");
      System.out.println("HORN: "+ horn + ";\n Filler: "+com_simplex+"\n 1-edge: "+sag.face(com_simplex,2)+"\n");
    }

    /*WreathProd<String> wreathProd1 = new WreathProd<String>(, new WreathProd<String>(3, ags, 0, 2));
    wreathProd1.setComponent("a", 0);
    wreathProd1.setComponent("b", 1);
    wreathProd1.setComponent("c", 2); */

  }
}
