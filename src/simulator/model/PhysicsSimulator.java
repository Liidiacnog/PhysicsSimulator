package simulator.model;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class PhysicsSimulator implements Observable<SimulatorObserver>{

    private double _dt; // actual time (in seconds) that corresponds to a simulation step
    // it will be passed to method move of the bodies

    private double _current_t;
    private ForceLaw _force;
    private List<Body> _bodies;
    private List<SimulatorObserver> observers; 
    
    public PhysicsSimulator(double delta_t, ForceLaw forces) throws IllegalArgumentException {
        try {
            observers = new ArrayList<>();
            setDeltaTime(delta_t);
            setForceLaws(forces);
            reset(); //TODO okay here or better explicit call to only necessary methods?
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid parameters for Physics Simulator", e);
        }
    }

    /*
     * applies one simulation step: (1) it calls resetForce() of each body; (2)
     * calls apply() of the force laws; (3) it calls move(dt) of each body where dt
     * is the real time per step; and (4) increments the current time by dt seconds.
     */
    public void advance() {

        // 1
        for (Body b : _bodies) {
            b.resetForce();
        }
        // 2
        _force.apply(_bodies);
        // 3
        for (int i = 0; i < _bodies.size(); ++i) {
            _bodies.get(i).move(_dt);
        }
        // 4
        _current_t += _dt;

        for(SimulatorObserver o: observers)
            o.onAdvance(_bodies, _current_t); 
    }

    /*
     * adds body b to the simulator, and throws IllegalArgumentException if it
     * already exists
     */
    public void addBody(Body b) throws IllegalArgumentException {
        if (_bodies.contains(b))
            throw new IllegalArgumentException();

        _bodies.add(b);
        for(SimulatorObserver o: observers)
            o.onBodyAdded(_bodies, b); 
    }

    /*
     * returns the JSON structure that includes the simulator’s state: { "time": t,
     * "bodies": [json1, json2, . . .] } where t is the current time and jsoni is
     * the JSONObject returned by getState() of the i-th body in the list of bodies.
     */
    public JSONObject getState() {
        JSONObject jo = new JSONObject();

        jo.put("time", _current_t);

        JSONArray ja = new JSONArray();
        for (Body b : _bodies)
            ja.put(b.getState());

        jo.put("bodies", ja);

        return jo;
    }

    public String toString() {
        return getState().toString();
    }

    /* clears the list of bodies and sets the current time to 0.0 */
    public void reset() {
        _current_t = 0.0;
        _bodies = new ArrayList<Body>();
        for(SimulatorObserver o: observers)
            o.onReset(_bodies, _current_t, _dt, "" + _force);
    }

    /*
     * changes the current value of the delta-time (i.e. the real time per step) to
     * dt. It should throw an IllegalArgumentException exception exception if the
     * value is not valid.
     */
    public void setDeltaTime(double dt) throws IllegalArgumentException{
        if (dt > 0)
            _dt = dt;
        else
            throw new IllegalArgumentException("Invalid delta time");

        for(SimulatorObserver o: observers)
            o.onDeltaTimeChanged(_dt); 
    }

    /*
     * changes the force laws of the simulator to forceLaws. It should throw an
     * IllegalArgumentException if the value is not valid (i.e., null).
     */
    public void setForceLaws(ForceLaw forceLaws) {
        if (forceLaws != null) {
            _force = forceLaws;
        } else
            throw new IllegalArgumentException("Invalid force laws");

        for(SimulatorObserver o: observers)
            o.onForceLawsChanged("" + _force); 
    }


    /* add o to the list of observers, if it is not there already */
    public void addObserver(SimulatorObserver o){
        if(!observers.contains(o)){
            o.onRegister(_bodies, _current_t, _dt, "" + _force); 
            observers.add(o);
        }
    }


}
