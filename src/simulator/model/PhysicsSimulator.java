package simulator.model;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class PhysicsSimulator {
   
    private double _dt; //actual time (in seconds) that corresponds to a simulation step
    //it will be passed to method move of the bodies

    private double _current_t;
    private ForceLaws _forces;
    private List<Body> _l;

    public PhysicsSimulator(double delta_t, ForceLaws forces) throws IllegalArgumentException{
        if(delta_t > 0 && forces != null){
            _dt = delta_t;
            _forces = forces;
        }
        else
            throw new IllegalArgumentException("Invalid parameters for Physics Simulator"); 
        
        _current_t = 0.0;
        _l = new ArrayList<Body>();
    }

    
/*applies one simulation step: (1) it calls resetForce() of each body; (2) calls apply() of the force laws; (3) it calls
move(dt) of each body where dt is the real time per step; and (4) increments the current time by dt seconds.*/
public void advance(){

    //1
    for(Body b: _l){
        b.resetForce();
    }
    //2
    _forces.apply(_l);
    //3
    for(int i = 0; i < _l.size(); ++i){
        _l.get(i).move(_dt);
    }
    //4
    _current_t += _dt;

}

 /* adds body b to the simulator, and throws IllegalArgumentException if it already exists */
public void addBody(Body b) throws IllegalArgumentException{
    if(_l.contains(b))
        throw new IllegalArgumentException();
    
    _l.add(b);
}


/*returns the JSON structure that includes the simulator’s state:
    { "time": t, "bodies": [json1, json2, . . .] }
where t is the current time and jsoni is the JSONObject returned by getState() of the i-th body in the list of bodies. */
public JSONObject getState(){
    JSONObject jo = new JSONObject();

    jo.put("time", _current_t);

    JSONArray ja = new JSONArray();
    for(int i = 0; i <_l.size(); ++i)
        ja.put(_l.get(i).getState());
                
    jo.put("bodies", ja);
	
    return jo;
}

public String toString(){
    return getState().toString();
}


}
