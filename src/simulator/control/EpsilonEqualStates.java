package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.misc.Vector2D;

public class EpsilonEqualStates implements StateComparator {

    private double _eps;
    private static final double DefaultEps = 0.0;

    public EpsilonEqualStates (){
        _eps = DefaultEps;
    }

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
    public boolean equal(JSONObject s1, JSONObject s2) {//TODO modify so that user gets noticed on which parameter caused inequality?
        boolean eq = false;
        if(s1.getDouble("time") == s2.getDouble("time")){ 
            JSONArray ja1 = s1.getJSONArray("bodies");
            JSONArray ja2 = s2.getJSONArray("bodies");
            if(ja1.length() == ja2.length()){
                eq = true;
                for(int i = 0; i < ja1.length() && eq; ++i){
                    //create vector instances with the values extracted from objects i for velocity, position and force of each
                    // JSONOBject, to pass them as parameters to method epsEqual and compare them
                    JSONArray coords1 = ja1.getJSONObject(i).getJSONArray("p"),
                              coords2 = ja2.getJSONObject(i).getJSONArray("p");
                    Vector2D vPos1 = new Vector2D(coords1),
                             vPos2 = new Vector2D(coords2);
                            
                    coords1 = ja1.getJSONObject(i).getJSONArray("v");
                    coords2 = ja2.getJSONObject(i).getJSONArray("v");
                    Vector2D vVel1 = new Vector2D(coords1),
                             vVel2 = new Vector2D(coords2);
                    
                    coords1 = ja1.getJSONObject(i).getJSONArray("f");
                    coords2 = ja2.getJSONObject(i).getJSONArray("f");
                    Vector2D vForce1 = new Vector2D(coords1),
                             vForce2 = new Vector2D(coords2);

                    if(!ja1.getJSONObject(i).getString("id").equals(ja2.getJSONObject(i).getString("id"))){
                         eq = false;
                         throw new IDMismatchException("Differing IDs: ", ja1.getJSONObject(i).getString("id"), ja2.getJSONObject(i).getString("id"));
                    }//TODO in progress
                       
                    if(!epsEqual(ja1.getJSONObject(i).getDouble("m"), ja2.getJSONObject(i).getDouble("m"))){
                        eq = false;
                    }
                        
                    if(!epsEqual(vPos1, vPos2) || !epsEqual(vForce1, vForce2) || !epsEqual(vVel1, vVel2)){
                        eq = false;
                    } 
                }
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
