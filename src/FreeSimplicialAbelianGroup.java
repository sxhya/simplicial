import java.util.HashMap;

/**
 * Created by sxh on 07.01.17.
 */
public class FreeSimplicialAbelianGroup<T> implements SimplicialStructure<FreeSimplicialAbelianGroup.LinearCombination<T>> {
    private SimplicialStructure<T> mySimplicialStructure;

    public FreeSimplicialAbelianGroup(SimplicialStructure<T> ss) {
        mySimplicialStructure = ss;
    }

    @Override
    public int getLevel(LinearCombination<T> simplicialObject) {
        if (!simplicialObject.myCoo.isEmpty())
            for (T t : simplicialObject.myCoo.keySet())
                assert (mySimplicialStructure.getLevel(t) == simplicialObject.myLevel);

        return simplicialObject.myLevel;
    }

    @Override
    public LinearCombination<T> face(LinearCombination<T> simplicialObject, int k) {
        int n = simplicialObject.myLevel;
        LinearCombination<T> result = new LinearCombination<T>(n);

        for (T t : simplicialObject.myCoo.keySet())
            result = LinearCombination.add(result,
                    new LinearCombination<T>(mySimplicialStructure.face(t, k), n-1));

        return result;
    }

    @Override
    public LinearCombination<T> degeneracy(LinearCombination<T> simplicialObject, int k) {
        int n = simplicialObject.myLevel;
        LinearCombination<T> result = new LinearCombination<T>(n);

        for (T t : simplicialObject.myCoo.keySet())
            result = LinearCombination.add(result,
                    new LinearCombination<T>(mySimplicialStructure.degeneracy(t, k), n+1));

        return result;
    }

    static class LinearCombination<S> {
        private HashMap<S, Integer> myCoo = new HashMap<S, Integer>();
        private int myLevel;

        public LinearCombination(int level) {
            myLevel = level;
        }

        public LinearCombination(S s, int level) {
            myLevel = level;
            myCoo.put(s, 1);
        }

        static<S> LinearCombination<S> add(LinearCombination<S> a, LinearCombination<S> b) {
            return lc(a, b, 1, 1);
        }

        public static<S> LinearCombination<S> sub(LinearCombination<S> a, LinearCombination<S> b) {
            return lc(a, b, 1, -1);
        }

        public static<S> LinearCombination<S> inv(LinearCombination<S> a) {
            return sub(new LinearCombination<S>(a.myLevel), a);
        }


        private static<S> LinearCombination<S> lc(LinearCombination<S> a, LinearCombination<S> b, int ca, int cb) {
            assert (a.myLevel == b.myLevel);
            LinearCombination<S> result = new LinearCombination<S>(a.myLevel);
            for (S s : a.myCoo.keySet()) {
                int i = ca * a.myCoo.get(s);
                if (b.myCoo.containsKey(s)) i += cb * b.myCoo.get(s);
                result.put(s, i);
            }

            for (S s : b.myCoo.keySet())
                if (!a.myCoo.containsKey(s)) result.put(s, cb * b.myCoo.get(s));

            return result;
        }

        private void put(S key, int value) {
            if (value != 0) myCoo.put(key, value);
        }
    }
}