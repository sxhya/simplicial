import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxh on 07.01.17.
 */
public class ClassifyingSpace<T> implements SimplicialStructure<ClassifyingSpace.ClassifyingSpaceElement<T>> {
    @Override
    public int getLevel(ClassifyingSpaceElement<T> simplicialObject) {
        return simplicialObject.myVector.size();
    }

    @Override
    public ClassifyingSpaceElement<T> face(ClassifyingSpaceElement<T> simplicialObject, int k) {
        int n = getLevel(simplicialObject);
        GroupStructure<T> gs = simplicialObject.myGroupStructure;
        if (k < 0 || k > n) throw new IllegalArgumentException();
        ClassifyingSpaceElement<T> result = new ClassifyingSpaceElement<T>(simplicialObject);
        if (k == 0) {
            result.myVector.remove(0);
        } else if (k == n) {
            result.myVector.remove(n-1);
        } else {
            T a = result.myVector.get(k-1);
            T b = result.myVector.get(k);
            result.myVector.remove(k);
            result.myVector.remove(k-1);
            result.myVector.add(k-1, gs.mul(a, b));
        }
        return result;
    }

    @Override
    public ClassifyingSpaceElement<T> degeneracy(ClassifyingSpaceElement<T> simplicialObject, int k) {
        int n = getLevel(simplicialObject);
        GroupStructure<T> gs = simplicialObject.myGroupStructure;
        if (k < 0 || k > n) throw new IllegalArgumentException();
        ClassifyingSpaceElement<T> result = new ClassifyingSpaceElement<T>(simplicialObject);
        result.myVector.add(k, gs.unit());
        return result;
    }

    /**
     * Created by sxh on 07.01.17.
     */
    public static class ClassifyingSpaceElement<T extends  Object> {
        public GroupStructure<T> myGroupStructure;
        public List<T> myVector;

        public ClassifyingSpaceElement(GroupStructure<T> gs, List<T> v) {
            myVector = v;
            myGroupStructure = gs;
        }

        public ClassifyingSpaceElement(ClassifyingSpaceElement<T> cse) {
            myVector = new ArrayList<T>();
            myVector.addAll(cse.myVector);
            myGroupStructure = cse.myGroupStructure;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof ClassifyingSpaceElement) {
                ClassifyingSpaceElement cse = (ClassifyingSpaceElement) o;
                return cse.myVector.equals(myVector);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return myVector.hashCode();
        }

        @Override
        public String toString() {
            return myVector.toString();
        }
    }
}
