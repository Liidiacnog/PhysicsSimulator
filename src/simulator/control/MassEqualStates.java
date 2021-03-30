package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;


public class MassEqualStates implements StateComparator {

    /*
    Two states s1 and s2 are equal if:
        The value of their “time” key is equal.
        The i-th bodies in the lists of bodies in s1 and s2 have the same value for keys “id” and “mass”.
    */
    @Override
    public boolean equal(JSONObject s1, JSONObject s2) {
        boolean eq = false;
        if(s1.getDouble("time") == s2.getDouble("time")){
            JSONArray ja1 = s1.getJSONArray("bodies");
            JSONArray ja2 = s2.getJSONArray("bodies");
            if(ja1.length() == ja2.length()){
                eq = true;
                for(int i = 0; i < ja1.length() && eq; ++i){
                    if( ja1.getJSONObject(i).getString("id").equals(ja2.getJSONObject(i).getString("id")) 
                        || ja1.getJSONObject(i).getDouble("m") != (ja2.getJSONObject(i).getDouble("m")) )
                            eq = false;
                }
            }
        }

        return eq;
    }


}
