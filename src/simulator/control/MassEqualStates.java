package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.control.exceptions.*;


public class MassEqualStates implements StateComparator {

    /*
    Two states s1 and s2 are equal if:
        The value of their “time” key is equal.
        The i-th bodies in the lists of bodies in s1 and s2 have the same value for keys “id” and “mass”.
    */
    @Override
    public boolean equal(JSONObject s1, JSONObject s2) throws StatesMismatchException { //TODO notify user on what went wrong
        boolean eq = false;
        if(s1.getDouble("time") == s2.getDouble("time")){
            JSONArray ja1 = s1.getJSONArray("bodies");
            JSONArray ja2 = s2.getJSONArray("bodies");
            if(ja1.length() == ja2.length()){
                eq = true;
                for(int i = 0; i < ja1.length() && eq; ++i){
                    String id1 = ja1.getJSONObject(i).getString("id"),   
                           id2 = ja2.getJSONObject(i).getString("id");
                    if(!id1.equals(id2)){
                         eq = false;
                         throw new IDMismatchException("Differing IDs: "+ id1 + " , " + id2);
                    }

                    double m1 = ja1.getJSONObject(i).getDouble("m"), 
                           m2 = ja2.getJSONObject(i).getDouble("m");
                    if(m1 != m2){
                        eq = false;
                        throw new MassMismatchException("Differing masses: "+ m1 + " , " + m2);
                    }

                }
            }
        }
        return eq;
    }


}
