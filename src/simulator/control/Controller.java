package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

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
    }

/*Converts the input JSON into a JSONObject, using and then extracts each bbi from jsonInupt, 
creates a corresponding body b using the bodies factory, and adds it to the simulator by calling method addBody.*/
    public void loadBodies(InputStream in){
        /*we assume that "in" includes a JSON structure of the form
             { "bodies": [bb1,...,bbn] }
        where each bbi is a JSON structure defining a corresponding body*/
        //convert the input JSON into a JSONObject:
        JSONArray bodies = new JSONObject(new JSONTokener(in)).getJSONArray("bodies");
        for(int i = 0; i < bodies.length(); ++i){
            _sim.addBody(_fB.createInstance(bodies.getJSONObject(i)));
        }
    }



    /*it runs the simulator n steps, and prints to the different states out in the following JSON format:
            { "states": [s0,s1,...,sn] }
        where s0 is the state of the simulator before executing any step, and each si with
        i >= 1 is the state of the simulator immediately after executing the i-th simulation
        step */
    public void run(int n, OutputStream out, InputStream expOut, StateComparator cmp) throws StatesMismatchException{
        PrintStream p = new PrintStream(out);
        JSONArray arrayExpStates = null;
        if(expOut != null){
            arrayExpStates = new JSONObject(new JSONTokener(expOut)).getJSONArray("states");
        }
    
        p.println("{");
        p.println("\"states\": [");
        //print s0:
        p.println(_sim.toString() + ',');

        //try{
            if(arrayExpStates != null){//TODO better way? (shorter)
                //compare s0
                compareUsingCmp(_sim.getState(), arrayExpStates.getJSONObject(0), cmp, 0);
                // run the simulation n steps
                for(int i = 1; i < n; ++i){
                    _sim.advance();
                    p.println(_sim.toString() + ',');
                    compareUsingCmp(_sim.getState(), arrayExpStates.getJSONObject(i), cmp, i);
                }
                if(n > 1){ //last one (sn), it has no final comma
                    _sim.advance();
                    p.println(_sim.toString());
                    compareUsingCmp(_sim.getState(), arrayExpStates.getJSONObject(n), cmp, n);
                }
            }else{
                for(int i = 1; i < n; ++i){
                    _sim.advance();
                    p.println(_sim.toString() + ',');
                }
                if(n > 1){ //last one (sn), has no final comma
                    _sim.advance();
                    p.println(_sim.toString());
                }
            }
        //} catch (StatesMismatchException ex){
            //TODO inform user or what is it for?
        //}
        p.println("]");
        p.println("}");
    }

    //compares JSONObject's j1 and j2 using StateComparator cmp, and throws StatesMismatchException if they aren't equal, 
    ///otherwise it does nothing
    private void compareUsingCmp(JSONObject j1, JSONObject j2, StateComparator cmp, int execStep) throws StatesMismatchException {
        if(!cmp.equal(j1, j2))
                throw new StatesMismatchException("Simulation step number: " + execStep + "%n" + "States j1: " + j1.toString(5) + "and j2: " + j2.toString(5) + "differ", j1, j2, execStep); //TODO leave like this or string mssg inside constructor itself?
    }



}
