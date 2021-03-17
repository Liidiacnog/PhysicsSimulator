package simulator.control;


import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.PhysicsSimulator;

public class Controller {

    private PhysicsSimulator _sim;
    private Factory<Body> _fB;

    /*responsible for:
        (1) reading the bodies from a given InputStream and adding them to the simulator; and
        (2) executing the simulator a predetermined number of steps and printing the different
        states to a given OutputStream (and compare the output to the expected one if required).
    */


    public Controller(PhysicsSimulator sim, Factory<Body> fB){
        _fB = fB;
        _sim = sim;
        //TODO okay?
    }

/*Converts the input JSON into a JSONObject, using and then extracts each bbi from jsonInupt, 
creates a corresponding body b using the bodies factory, and adds it to the simulator by calling method addBody.*/
    public void loadBodies(InputStream in){
        /*we assume that "in" includes a JSON structure of the form
             { "bodies": [bb1,...,bbn] }
        where each bbi is a JSON structure defining a corresponding body*/
        //convert the input JSON into a JSONObject:
        JSONObject jsonInput = new JSONObject(new JSONTokener(in));
        
        JSONArray bodies = jsonInput.getJSONArray("bodies");
        for(int i = 0; i < bodies.length(); ++i){
            _sim.addBody(_fB.createInstance(bodies.getJSONObject(i)));
        }
        
    }



    /*it runs the simulator n steps, and prints to the different states out in the following JSON format:
            { "states": [s0,s1,...,sn] }
        where s0 is the state of the simulator before executing any step, and each si with
        i >= 1 is the state of the simulator immediately after executing the i-th simulation
        step. Note that state si is obtained by calling getState() of the simulator. Note
        also that when calling this method with n < 1, the output should include s0. See
        Section 6.2 for a convenient way to print into a OutputStream.

        The 3rd parameter expOut is an InputStream that corresponds to the expected output
        or null (the same syntax as the output described above). If expOut is not null,
        first convert it into a JSON structure, and then in each simulation step you should
        compare the current state of the simulator to the expected one using the state
        comparator of the 4-th argument, and if the result is not equal throw a corresponding
        exception with a message that includes the different states and the number of the
        execution step (better create your own exception class that encapsulate all this
        information).
    */
        public void run(int n, OutputStream out, InputStream expOut, StateComparator cmp){
            JSONObject jsonOUT = new JSONObject();
            JSONArray jArrayStates = new JSONArray();
            jArrayStates.put(0, _sim.getState());
            for(int i = 1; i < n; ++i){
               _sim.advance();
               jArrayStates.put(i, _sim.getState());
            }
            jsonOUT.put("states", jArrayStates);
            
            //out.write(jsonOUT.toString());

            //TODO voy por aquí, section 6.2.
            
        }



}
