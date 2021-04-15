package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.misc.Vector2D;
import simulator.control.exceptions.*;

public class EpsilonEqualStates implements StateComparator {

    private double _eps;
    
    public EpsilonEqualStates (double eps){
        _eps = eps;
    }

    /*Two states s1 and s2 are eps-equal if:
        The value of their “time” key is equal.
        the i-th bodies in the lists of bodies in s1 and s2 must have equal values for key
        “id”, and eps-equal values for “m”, “p”, “v” and “f”.
    This comparator is useful because when performing calculations on data of type double,
    we might get slightly different results depending on the order in which we apply the
    operations (because of the use of floating point arithmetic).
    */
    @Override
    public boolean equal(JSONObject s1, JSONObject s2) throws StatesMismatchException {

        if(s1.getDouble("time") == s2.getDouble("time")){ 

            JSONArray ja1 = s1.getJSONArray("bodies");
            JSONArray ja2 = s2.getJSONArray("bodies");
            if(ja1.length() == ja2.length()){

                for(int i = 0; i < ja1.length(); ++i){
                    String id1 = ja1.getJSONObject(i).getString("id"),  
                           id2 = ja2.getJSONObject(i).getString("id");
                    if(!id1.equals(id2)){
                         throw new StatesMismatchException("Differing IDs: "+ id1 + " , " + id2);
                    }

                    double m1 = ja1.getJSONObject(i).getDouble("m"), 
                           m2 = ja2.getJSONObject(i).getDouble("m");
                    if(!epsEqual(m1, m2)){
                        throw new StatesMismatchException("Differing masses: "+ m1 + " , " + m2);
                    }
                        
                    cmp_using_key(ja1, ja2, "p", i);
                    cmp_using_key(ja1, ja2, "f", i);
                    cmp_using_key(ja1, ja2, "v", i);
                }
            }
        }

        return true;
    }
    

    private void cmp_using_key(JSONArray ja1, JSONArray ja2, String key, int i) throws StatesMismatchException{
        JSONArray coords1 = ja1.getJSONObject(i).getJSONArray(key),
                  coords2 = ja2.getJSONObject(i).getJSONArray(key);
        if(!epsEqual(coords1, coords2)){
            throw new StatesMismatchException("Differing vectors: "+ coords1 + " , " + coords2);
        }
    }

    //Two numbers a and b are eps-equal if “Math.abs(a-b) <= eps”
    private boolean epsEqual(double a, double b){
        return Math.abs(a-b) <= _eps;
    }


    //extracts vector coordinates from a JSONArray and copares two vectors via an eps-equal if “v1.distanceTo(v2) <= eps”
    private boolean epsEqual(JSONArray coords1, JSONArray coords2){
        Vector2D a = new Vector2D(coords1),
                 b = new Vector2D(coords2);
        return a.distanceTo(b) <= _eps;
    }

}
