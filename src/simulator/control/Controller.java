package simulator.control;


import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        step. Note that state si is obtained by calling getState() of the simulator. Note
        also that when calling this method with n < 1, the output should include s0. 
    */
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

            if(arrayExpStates != null){//TODO better way? (shorter)
                //compare s0
                //TODO
                // run the sumulation n steps
                for(int i = 1; i < n - 1; ++i){
                    _sim.advance();
                    p.println(_sim.toString() + ',');
                    if(!cmp.equal(_sim.getState(), arrayExpStates.getJSONObject(i)))
                        throw new StatesMismatchException(); //TODO fill info: different states and the number of the execution step
                }
                if(n > 1){ //last one (sn) has no final comma
                    _sim.advance();
                    p.println(_sim.toString());

                }
            }
            else{
                for(int i = 1; i < n - 1; ++i){
                    _sim.advance();
                    p.println(_sim.toString() + ',');
                }
                if(n > 1){ //last one (sn) has no final comma
                    _sim.advance();
                    p.println(_sim.toString());
                }
            }
            p.println("]");
            p.println("}");


            


            
        }



}
