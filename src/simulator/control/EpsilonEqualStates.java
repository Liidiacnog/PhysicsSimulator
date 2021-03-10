package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;

public class EpsilonEqualStates implements StateComparator {

    private double _eps;

    public EpsilonEqualStates (double eps){
        _eps = eps;
    }

/*Two numbers a and b are eps-equal if “Math.abs(a-b) <= eps”, and two vectors v1 and
    v2 are eps-equal if “v1.distanceTo(v2) <= eps”. Two states s1 and s2 are equal if:
        The value of their “time” key is equal.
        the i-th bodies in the lists of bodies in s1 and s2 must have equal values for key
        “id”, and eps-equal values for “m”, “p”, “v” and “f”.
    This comparator is useful because when performing calculations on data of type double,
    we might get slightly different results depending on the order in which we apply the
    operations (because of the use of floating point arithmetic). When comparing your output
    to the expected output, you can allow the values to be slightly different by changing the
    value of eps.
    */
    @Override
    public boolean equal(JSONObject s1, JSONObject s2) {
        boolean eq = false;
        if(s1.has("time") && s2.has("time") && s1.get("time") == s1.get("time")){ //TODO do we need to check if keys are defined?, or we suppose they will be
            JSONArray ja1 = s1.getJSONArray("bodies");
            JSONArray ja2 = s2.getJSONArray("bodies");
            if(ja1.length() == ja2.length()){
                eq = true;
                /*for(int i = 0; i < ja1.length() && eq; ++i){
                    if(ja1.get(i).getString("id") != ja2.get(i).getString("id") || ja1.get(i).getDouble("mass") != ja2.get(i).getDouble("mass")) //TODO use casting?
                        eq = false;
                }*/ //TODO unfinished
            }
        }

        return eq;
    }
    
    
}
