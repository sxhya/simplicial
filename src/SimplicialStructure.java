/**
 * Created by sxh on 07.01.17.
 */
public interface SimplicialStructure<T> {
    int getLevel(T simplicialObject);
    T face (T simplicialObject, int k);
    T degeneracy (T simplicialObject, int k);

    static<T> boolean isValid(SimplicialStructure<T> struc, T simplicialObject){
        int n = struc.getLevel(simplicialObject);
        Object[][] dd = new Object[n+1][n];
        Object[][] ds = new Object[n+1][n];
        Object[][] sd = new Object[n+1][n+2];
        Object[][] ss = new Object[n+1][n+2];

        for (int i=0; i<=n; i++) {
            T di = struc.face(simplicialObject, i);
            T si = struc.degeneracy(simplicialObject, i);
            for (int j=0; j<n; j++) {
                dd[i][j] = struc.face(di, j);
                ds[i][j] = struc.degeneracy(di, j);
            }
            for (int j=0; j<=n+1; j++) {
                sd[i][j] = struc.face(si, j);
                ss[i][j] = struc.degeneracy(si, j);
            }
        }

        for (int i=0; i<=n; i++) {
            if (!simplicialObject.equals(sd[i][i])) return false;
            if (!simplicialObject.equals(sd[i][i+1])) return false;

            for (int j=i+1; j<=n; j++) {
                if (!dd[i][j-1].equals(dd[j][i])) return false;
                if (!sd[j][i].equals(ds[i][j-1])) return false;
            }

            for (int j=i+2; j<=n+1; j++) if (!sd[i][j].equals(ds[j-1][i])) return false;

            for (int j=i; j<=n; j++) if (!ss[i][j+1].equals(ss[j][i])) return false;
        }

        return true;
    }
}
