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

  @Test
  public void moore(){
    ClassifyingSpace<String> bg = new ClassifyingSpace<String>();
    AlphabetGroupStructure ags = new AlphabetGroupStructure();
    List<String> l = new ArrayList<>(); l.add("xoXo"); l.add("jojojo"); l.add("lslsl");
    ClassifyingSpace.ClassifyingSpaceElement<String> cse1 = new ClassifyingSpace.ClassifyingSpaceElement<String>(ags, l);
    l = new ArrayList<>(); l.add("A"); l.add("sh"); l.add("aha");
    ClassifyingSpace.ClassifyingSpaceElement<String> cse2 = new ClassifyingSpace.ClassifyingSpaceElement<String>(ags, l);
    l = new ArrayList<>(); l.add("A"); l.add("sh"); l.add("ch");
    ClassifyingSpace.ClassifyingSpaceElement<String> cse3 = new ClassifyingSpace.ClassifyingSpaceElement<String>(ags, l);

    FreeSimplicialAbelianGroup<ClassifyingSpace.ClassifyingSpaceElement<String>> sag = new FreeSimplicialAbelianGroup<>(bg);
    FreeSimplicialAbelianGroup.LinearCombination<ClassifyingSpace.ClassifyingSpaceElement<String>> lin =
      new FreeSimplicialAbelianGroup.LinearCombination<>(cse1, 3);
    lin = FreeSimplicialAbelianGroup.LinearCombination.sub(lin, new FreeSimplicialAbelianGroup.LinearCombination<>(cse2, 3));
    lin = FreeSimplicialAbelianGroup.LinearCombination.add(lin, new FreeSimplicialAbelianGroup.LinearCombination<>(cse3, 3));
    for (int k = 0; k <= 3; k++) {
      System.out.println("k=" + k);
      Horn<FreeSimplicialAbelianGroup.LinearCombination<ClassifyingSpace.ClassifyingSpaceElement<String>>> horn = new Horn<>(sag, lin, k);
      System.out.println("Original combination: " + lin);
      System.out.println("Corresponding horn: " + horn);
      FreeSimplicialAbelianGroup.LinearCombination<ClassifyingSpace.ClassifyingSpaceElement<String>> filler = SimplicialGroupStructure.findFiller(sag, horn);
      System.out.println("Filler: " + filler);
      Horn<FreeSimplicialAbelianGroup.LinearCombination<ClassifyingSpace.ClassifyingSpaceElement<String>>> horn2 = new Horn<>(sag, filler, k);
      System.out.println("Filler horn:        " + horn2);
      assert(horn.equals(horn2));
    }
  }

}
