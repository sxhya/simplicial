/**
 * Created by sxh on 07.01.17.
 */
public interface SimplicialGroupStructure<T> extends SimplicialStructure<T>, GroupStructure<T> {

  public static<T> T findFiller(SimplicialGroupStructure<T> str, Horn<T> horn) {
    assert (horn.getSimplicialStructure().equals(str));
    //TODO: Implement this method
    return null;
  }
}
