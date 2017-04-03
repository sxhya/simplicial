import jdk.nashorn.internal.runtime.regexp.joni.ast.ConsAltNode;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sxh on 28.12.16.
 */
public class ConsoleMain {
  public static AlphabetGroupStructure ags = AlphabetGroupStructure.INSTANCE;
  public static ClassifyingSpace<String> bg = new ClassifyingSpace<String>();
  public static FreeSimplicialAbelianGroup<ClassifyingSpace.ClassifyingSpaceElement<String>> sag = new FreeSimplicialAbelianGroup<>(bg);


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

  public static<T> FreeSimplicialAbelianGroup.LinearCombination<ClassifyingSpace.ClassifyingSpaceElement<T>> rotate(GroupStructure<T> ags,
                                                                                                                    FreeSimplicialAbelianGroup.LinearCombination<ClassifyingSpace.ClassifyingSpaceElement<T>> lin) {
    FreeSimplicialAbelianGroup.LinearCombination<ClassifyingSpace.ClassifyingSpaceElement<T>> lin2 = new FreeSimplicialAbelianGroup.LinearCombination<>(2);
    for (ClassifyingSpace.ClassifyingSpaceElement<T> cse : lin.myCoo.keySet()) {
      List<T> lst = cse.myVector;
      List<T> lst2 = new ArrayList<>();
      lst2.add(ags.mul(lst.get(0), lst.get(1)));
      lst2.add(ags.inv(lst.get(1)));
      ClassifyingSpace.ClassifyingSpaceElement<T> cse2 = new ClassifyingSpace.ClassifyingSpaceElement<>(ags, lst2);
      lin2.myCoo.put(cse2, lin.myCoo.get(cse));
    }
    return lin2;
  }

  public static<T> List<ChainLink<T>> wpath(GroupStructure<T> ags, List<T> bP, WreathProd<T> wp, int i, int j, T gen) {
    List<ChainLink<T>> result = new LinkedList<ChainLink<T>>();
    List<T> tP = new ArrayList<>();
    int n = bP.size();
    for (int s=0; s<n; s++) {
      if (s == i) tP.add(ags.mul(gen, bP.get(s)));
         else tP.add(bP.get(s));
    }
    ChainLink<T> last = new ChainLink<>(wp, wp, bP, tP);
    result.add(last);

    int[] sigma = new int[n];
    for (int s=0; s<n; s++)
      if (s == i) sigma[s] = j; else
        if (s == j) sigma[s] = i; else
          sigma[s] = s;
    last = new ChainLink<>(last, sigma);
    result.add(last);

    last = new ChainLink<>(last, bP);
    result.add(last);

    last = new ChainLink<>(last, sigma);
    result.add(last);

    return result;
  }

  //w = s_0(p_0)
  //w = w - s_2 d_3(w) + s_2(p_3)
  //w = w - s_1 d_2(w) + s_1(p_2)

  //p_0 = h_0
  //p_1 = ?
  //p_2 = h_1
  //p_3 = композиция

  //w     =     s_0 p_0 -     s_2 d_3 s_0 p_0  +     s_2 p_3 -     s_1 d_2 s_0 p_0 + s_1 d_2 s_2 d_3 s_0 p_0 - s_1 d_2 s_2 p_3 + s_1 p_2 =
  //      =     s_0 p_0 -     s_2 d_3 s_0 p_0  +     s_2 p_3 -     s_1 d_2 s_0 p_0 +     s_1 d_3 s_0 p_0 -     s_1 p_3 +     s_1 p_2
  //d_1 w = d_1 s_0 p_0 - d_1 s_2 d_3 s_0 p_0  + d_1 s_2 p_3 - d_1 s_1 d_2 s_0 p_0 + d_1 s_1 d_3 s_0 p_0 - d_1 s_1 p_3 + d_1 s_1 p_2 =
  //      =         p_0 -         s_1 d_2 p_0  + s_1 d_1 p_3 -         s_0 d_1 p_0 +         s_0 d_2 p_0 -         p_3 +         p_2 =
  //      = p_0 + p_2 - p_3 - s_1 d_2 p_0  + s_1 d_1 p_3 - s_0 d_1 p_0 + s_0 d_2 p_0 =



  public static class FibreEdge<T> {
    public FreeSimplicialAbelianGroup.LinearCombination<ClassifyingSpace.ClassifyingSpaceElement<T>> h;
    private GroupStructure<T> myGs;
    public List<T> pcomp;
    public List<T> ncomp;

    public FibreEdge(List<T> p, List<T> n, GroupStructure<T> gs) {
      // PRECONDITION: "n" should consist of the same components as "p" (possibly in different order)
      pcomp = new ArrayList<>();
      ncomp = new ArrayList<>();
      pcomp.addAll(p);
      ncomp.addAll(n);
      myGs = gs;
      h = new FreeSimplicialAbelianGroup.LinearCombination<>(2);
    }

    public FibreEdge(FibreEdge<T> fe1, FibreEdge<T> fe2) {
      myGs = fe1.myGs;
      if (!myGs.equals(fe2.myGs)) throw new IllegalArgumentException();

      FreeSimplicialAbelianGroup.LinearCombination<ClassifyingSpace.ClassifyingSpaceElement<T>> lin =
        FreeSimplicialAbelianGroup.LinearCombination.sub(zip(myGs, fe1.pcomp, fe2.pcomp), zip(myGs, fe1.ncomp, fe2.ncomp));

      ClassifyingSpace<T> bg = new ClassifyingSpace<T>();
      FreeSimplicialAbelianGroup<ClassifyingSpace.ClassifyingSpaceElement<T>> sag = new FreeSimplicialAbelianGroup<>(bg);

      List<FreeSimplicialAbelianGroup.LinearCombination<ClassifyingSpace.ClassifyingSpaceElement<T>>> lst = new ArrayList<>();
      lst.add(fe2.h); lst.add(null); lst.add(fe1.h); lst.add(lin);
      Horn<FreeSimplicialAbelianGroup.LinearCombination<ClassifyingSpace.ClassifyingSpaceElement<T>>> horn = new Horn<>(sag, lst);

      //  = p_0 + p_2 - p_3 - s_1 d_2 p_0  + s_1 d_1 p_3 + s_0 d_2 p_0 =
      //     x     x     x         x              x             x

        FreeSimplicialAbelianGroup.LinearCombination<ClassifyingSpace.ClassifyingSpaceElement<T>> elem =
        sag.mul(sag.mul(fe2.h, sag.mul(sag.inv(sag.degeneracy(sag.face(fe2.h, 2), 1)), sag.degeneracy(sag.face(fe2.h, 2), 0))), sag.mul(sag.inv(lin), sag.mul(fe1.h, sag.degeneracy(sag.face(lin, 1), 1))));

      FreeSimplicialAbelianGroup.LinearCombination<ClassifyingSpace.ClassifyingSpaceElement<T>> filler = SimplicialGroupStructure.findFiller(sag, horn);
      h = sag.face(filler, 1);
      pcomp = WreathProd.componentwise_mul(myGs, fe1.pcomp, fe2.pcomp);
      ncomp = WreathProd.componentwise_mul(myGs, fe1.ncomp, fe2.ncomp);
    }

    @Override
    public String toString() {
      return h.toString() + " = " + pcomp + " = " + ncomp;
    }
  }

  public static<T> FibreEdge<T> wrapWithFibreEdge(GroupStructure<T> gs, List<ChainLink<T>> path) {
    FibreEdge<T> result = null;
    for (int i=0; i<path.size(); i++) {
      List<T> x0 = path.get(i).x0;
      List<T> y0 = path.get(i).g0.act(x0);
      List<T> x1 = path.get(i).x1;
      List<T> y1 = path.get(i).g1.act(x1);
      List<T> diff1 = WreathProd.componentwise_mul(gs, WreathProd.componentwise_inv(gs, x0), x1);
      List<T> diff2 = WreathProd.componentwise_mul(gs, WreathProd.componentwise_inv(gs, y0), y1);

      boolean allTrivial = true;

      for (T t : diff1) if (t != gs.unit()) allTrivial = false;
      for (T t : diff2) if (t != gs.unit()) allTrivial = false;

      if (allTrivial) continue;
      FibreEdge<T> fe = new FibreEdge<>(diff1, diff2, gs);
      if (result == null) result = fe; else result = new FibreEdge<T>(result, fe);
    }
    return result;
  }

  public static void main(String[] args) {
    List<String> basePoint = new ArrayList<String>(); basePoint.add(""); basePoint.add(""); basePoint.add("");
    List<String> twistedPoint = new ArrayList<String>(); twistedPoint.add("A"); twistedPoint.add(""); twistedPoint.add("");
    List<String> twistedPointB = new ArrayList<String>(); twistedPointB.add("B"); twistedPointB.add(""); twistedPointB.add("");
    List<ChainLink<String>> path = new LinkedList<>();
    List<ChainLink<String>> pathChain = new ArrayList<>();
    FibreEdge<String> fe1 = wrapWithFibreEdge(ags, wpath(ags, basePoint, new WreathProd<>(3, ags), 0, 1, "A"));
    FibreEdge<String> fe2 = wrapWithFibreEdge(ags, wpath(ags, basePoint, new WreathProd<>(3, ags), 0, 1, "B"));
    FibreEdge<String> fe3 = wrapWithFibreEdge(ags, wpath(ags, basePoint, new WreathProd<>(3, ags), 0, 1, "ab"));

    FibreEdge<String> composante = new FibreEdge<String>(new FibreEdge<String>(fe1, fe2), fe3);
    System.out.println(composante);
    //pathChain = wpath(ags, basePoint, new WreathProd<String>(3, ags), 0, 1, "A"); path.addAll(pathChain);
    //pathChain = wpath(ags, pathChain.get(pathChain.size()-1).x1, pathChain.get(pathChain.size()-1).g1, 0, 1, "B"); path.addAll(pathChain);
    //pathChain = wpath(ags, pathChain.get(pathChain.size()-1).x1, pathChain.get(pathChain.size()-1).g1, 0, 1, "ab"); path.addAll(pathChain);
   // pathChain = wpath(ags, pathChain.get(pathChain.size()-1).x1, pathChain.get(pathChain.size()-1).g1, 0, 2, "b"); path.addAll(pathChain);

    /* WreathProd<String> wp = new WreathProd<String>(3, ags);
    ChainLink<String> last = new ChainLink<String>(wp, wp, basePoint, twistedPoint);
    path.add(last);

    last = new ChainLink<>(last, new int[]{1, 0 ,2});
    path.add(last);

    last = new ChainLink<>(last, basePoint);
    path.add(last);

    last = new ChainLink<>(last, new int[]{1, 0 ,2});
    path.add(last);

    last = new ChainLink<>(last, twistedPointB);
    path.add(last);

    last = new ChainLink<>(last, new int[]{2, 1 ,0});
    path.add(last);

    last = new ChainLink<>(last, basePoint);
    path.add(last);

    last = new ChainLink<>(last, new int[]{2, 1 ,0});
    path.add(last);

    last = new ChainLink<>(last, new int[]{1, 0 ,2});
    path.add(last);

    last = new ChainLink<>(last, twistedPoint);
    path.add(last);

    last = new ChainLink<>(last, new int[]{1, 0 ,2});
    path.add(last);

    last = new ChainLink<>(last, basePoint);
    path.add(last);

    last = new ChainLink<>(last, new int[]{2, 1 ,0});
    path.add(last);

    last = new ChainLink<>(last, twistedPointB);
    path.add(last);

    last = new ChainLink<>(last, new int[]{2, 1 ,0});
    path.add(last);

    last = new ChainLink<>(last, basePoint);
    path.add(last); */

    WreathStructure<String> wreathStructure = new WreathStructure<>(ags, 3);

    //Project to S:

    List<String> _last = null;
    WreathProd<String> _elem = null;
    for (int i=0; i<path.size(); i++) {
      if (_elem != null && !_elem.equals(path.get(i).g0) || _last != null && !_last.equals(path.get(i).x0))
         System.out.print("<UNEQUAL>");
      System.out.println("i="+i+" ["+path.get(i).x1+", "+path.get(i).g1+"]");
      //System.out.println("      ["+path.get(i).x1+", "+path.get(i).g1+"]");
      _last = path.get(i).x1;
      _elem = path.get(i).g1;
    }

    System.out.println();

    for (int i=0; i<path.size(); i++) {
      WreathProd<String> wprod = new WreathProd<String>(new WreathProd<String>(path.get(i).g0), path.get(i).g1);
      if (wprod.equals(wreathStructure.unit()))
        continue;
      System.out.println("i="+ i + " " + wprod);
    }

    System.out.println();

    List<String> com1 = new ArrayList<>(); com1.add(""); com1.add(""); com1.add("");
    List<String> com2 = new ArrayList<>(); com2.add(""); com2.add(""); com2.add("");

    FreeSimplicialAbelianGroup.LinearCombination<ClassifyingSpace.ClassifyingSpaceElement<String>> com_simplex =
      new FreeSimplicialAbelianGroup.LinearCombination<>(2);

    FreeSimplicialAbelianGroup.LinearCombination<ClassifyingSpace.ClassifyingSpaceElement<String>> com_simplex2 =
      new FreeSimplicialAbelianGroup.LinearCombination<>(2);

    for (int i=0; i<path.size(); i++) {
      List<String> x0 = path.get(i).x0;
      List<String> y0 = path.get(i).g0.act(x0);
      List<String> x1 = path.get(i).x1;
      List<String> y1 = path.get(i).g1.act(x1);
      List<String> diff1 = WreathProd.componentwise_mul(ags, WreathProd.componentwise_inv(ags, x0), x1);
      List<String> diff2 = WreathProd.componentwise_mul(ags, WreathProd.componentwise_inv(ags, y0), y1);

      if (diff1.equals(wreathStructure.unit().getVector()) && diff2.equals(wreathStructure.unit().getVector())) {
        continue;
      }

      //convert diff1 & diff2 to

      FreeSimplicialAbelianGroup.LinearCombination<ClassifyingSpace.ClassifyingSpaceElement<String>> lin =
        FreeSimplicialAbelianGroup.LinearCombination.sub(zip(ags, com1, diff1), zip(ags, com2, diff2));

      com_simplex2 = rotate(ags, FreeSimplicialAbelianGroup.LinearCombination.sub(lin, com_simplex2));

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
      System.out.println("i="+ i + " [" + diff1 + ", " + diff2 + "]" + " Cumulative = ["+com1+"; "+com2+"];");
      //System.out.println("HORN: "+ horn + ";\n Filler: "+com_simplex+"\n 1-edge: "+sag.face(com_simplex,2)+"\n");
      //System.out.println("ALTERNATIVE COM_SIMPLEX: "+ com_simplex2);
    }

    System.out.println();


    /* FreeSimplicialAbelianGroup.LinearCombination<ClassifyingSpace.ClassifyingSpaceElement<String>> me = Tests.m(true, "AB");

    me = FreeSimplicialAbelianGroup.LinearCombination.sub(me, sag.degeneracy(sag.face(me, 0), 0));
    me = FreeSimplicialAbelianGroup.LinearCombination.sub(me, sag.degeneracy(sag.face(me, 1), 1));

    //System.out.println("Simplex: "+com_simplex);
    System.out.println("Simplex_2: "+com_simplex2);
    System.out.println("Miller : "+me);

    System.out.println("diffM 0 = " + sag.face(me, 0));
    System.out.println("diffM 1 = " + sag.face(me, 1));
    System.out.println("diffM 2 = " + sag.face(me, 2));

    me = FreeSimplicialAbelianGroup.LinearCombination.add(com_simplex2, me);
    System.out.println("Diff: "+me);

    List<String> l = new ArrayList<>(); l.add("A"); l.add(""); l.add("");
    FreeSimplicialAbelianGroup.LinearCombination<ClassifyingSpace.ClassifyingSpaceElement<String>> b
      =  new FreeSimplicialAbelianGroup.LinearCombination<>(3);
    b = FreeSimplicialAbelianGroup.LinearCombination.sub(b, new FreeSimplicialAbelianGroup.LinearCombination<>(new ClassifyingSpace.ClassifyingSpaceElement<>(ags, l), 3));
    b = FreeSimplicialAbelianGroup.LinearCombination.sub(b, new FreeSimplicialAbelianGroup.LinearCombination<>(new ClassifyingSpace.ClassifyingSpaceElement<>(ags, l), 3));

    l = new ArrayList<>(); l.add("A"); l.add(""); l.add("B");   b = FreeSimplicialAbelianGroup.LinearCombination.add(b, new FreeSimplicialAbelianGroup.LinearCombination<>(new ClassifyingSpace.ClassifyingSpaceElement<>(ags, l), 3));
    l = new ArrayList<>(); l.add("AB"); l.add(""); l.add("A");  b = FreeSimplicialAbelianGroup.LinearCombination.sub(b, new FreeSimplicialAbelianGroup.LinearCombination<>(new ClassifyingSpace.ClassifyingSpaceElement<>(ags, l), 3));
    l = new ArrayList<>(); l.add("BA"); l.add(""); l.add("");   b = FreeSimplicialAbelianGroup.LinearCombination.add(b, new FreeSimplicialAbelianGroup.LinearCombination<>(new ClassifyingSpace.ClassifyingSpaceElement<>(ags, l), 3));
    l = new ArrayList<>(); l.add("ABa"); l.add(""); l.add("");  b = FreeSimplicialAbelianGroup.LinearCombination.add(b, new FreeSimplicialAbelianGroup.LinearCombination<>(new ClassifyingSpace.ClassifyingSpaceElement<>(ags, l), 3));
    l = new ArrayList<>(); l.add("AB"); l.add("a"); l.add("b"); b = FreeSimplicialAbelianGroup.LinearCombination.sub(b, new FreeSimplicialAbelianGroup.LinearCombination<>(new ClassifyingSpace.ClassifyingSpaceElement<>(ags, l), 3));
    l = new ArrayList<>(); l.add("BA"); l.add("a"); l.add("b"); b = FreeSimplicialAbelianGroup.LinearCombination.add(b, new FreeSimplicialAbelianGroup.LinearCombination<>(new ClassifyingSpace.ClassifyingSpaceElement<>(ags, l), 3));
    l = new ArrayList<>(); l.add("B"); l.add("A"); l.add("a");  b = FreeSimplicialAbelianGroup.LinearCombination.sub(b, new FreeSimplicialAbelianGroup.LinearCombination<>(new ClassifyingSpace.ClassifyingSpaceElement<>(ags, l), 3));
    l = new ArrayList<>(); l.add("A"); l.add("B"); l.add("b");  b = FreeSimplicialAbelianGroup.LinearCombination.add(b, new FreeSimplicialAbelianGroup.LinearCombination<>(new ClassifyingSpace.ClassifyingSpaceElement<>(ags, l), 3));



    me = FreeSimplicialAbelianGroup.LinearCombination.add(me, FreeSimplicialAbelianGroup.LinearCombination.add(sag.face(b, 0), FreeSimplicialAbelianGroup.LinearCombination.sub(sag.face(b, 2),
      FreeSimplicialAbelianGroup.LinearCombination.add(sag.face(b, 1), sag.face(b, 3)))));
    System.out.println("Diff: "+me);

    System.out.println("diff 0 = " + sag.face(me, 0));
    System.out.println("diff 1 = " + sag.face(me, 1));
    System.out.println("diff 2 = " + sag.face(me, 2)); */


    /*WreathProd<String> wreathProd1 = new WreathProd<String>(, new WreathProd<String>(3, ags, 0, 2));
    wreathProd1.setComponent("a", 0);
    wreathProd1.setComponent("b", 1);
    wreathProd1.setComponent("c", 2); */

  }
}
