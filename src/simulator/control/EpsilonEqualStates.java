package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.misc.Vector2D;

public class EpsilonEqualStates implements StateComparator {

    private double _eps;

    public EpsilonEqualStates (double eps){
        _eps = eps;
    }

    /*Two states s1 and s2 are equal if:
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
        if(s1.get("time") == s2.get("time")){ 
            JSONArray ja1 = s1.getJSONArray("bodies");
            JSONArray ja2 = s2.getJSONArray("bodies");
            if(ja1.length() == ja2.length()){
                eq = true;
                for(int i = 0; i < ja1.length() && eq; ++i){
                    if(ja1.get(i).getId() != ja2.get(i).getId() || !epsEqual(ja1.get(i).getMass(), ja2.get(i).getMass())
                        || !epsEqual(ja1.get(i).getPosition(), ja2.get(i).getPosition()) 
                        || !epsEqual(ja1.get(i).getForce(), ja2.get(i).getForce())
                        || !epsEqual(ja1.get(i).getVelocity(), ja2.get(i).getVelocity()) ) //TODO use casting?
                        eq = false;
                } //TODO casting required?
            }
        }

        return eq;
    }
    
    //Two numbers a and b are eps-equal if “Math.abs(a-b) <= eps”
    private boolean epsEqual(double a, double b){
        return Math.abs(a-b) <= _eps;
    }


    //two vectors v1 and v2 are eps-equal if “v1.distanceTo(v2) <= eps”
    private boolean epsEqual(Vector2D a, Vector2D b){
        return a.distanceTo(b) <= _eps;
    }

}
