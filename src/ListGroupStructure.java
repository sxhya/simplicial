import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by user on 1/9/17.
 */
public class ListGroupStructure implements GroupStructure<List> {
  private List<GroupStructure<Object>> myStructures;

  public ListGroupStructure(List<GroupStructure<Object>> gss) {
    myStructures = gss;
  }

  @Override
  public List mul(List a, List b) {
    int n = myStructures.size();
    if (n != a.size() || n != b.size()) throw new IllegalArgumentException();

    List<Object> result = new ArrayList<>();
    for (int i = 0; i < n ; i++)
      result.add(myStructures.get(i).mul(a.get(i), b.get(i)));
    return result;
  }

  @Override
  public List inv(List a) {
    int n = myStructures.size();
    if (n != a.size()) throw new IllegalArgumentException();
    List<Object> result = new ArrayList<>();
    for (int i = 0; i < n ; i++)
      result.add(myStructures.get(i).inv(a.get(i)));
    return result;
  }

  @Override
  public List unit() {
    int n = myStructures.size();
    return myStructures.stream().map(GroupStructure::unit).collect(Collectors.toList());
  }
}
