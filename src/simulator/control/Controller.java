package simulator.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import simulator.control.exceptions.StatesMismatchException;
import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.ForceLaw;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;
import java.util.List;

public class Controller {

    private PhysicsSimulator _sim;
    private Factory<Body> _fB;
    private Factory<ForceLaw> _fFL;

    /*
     * responsible for: (1) reading the bodies from a given InputStream and adding
     * them to the simulator; and (2) executing the simulator a predetermined number
     * of steps and printing the different states to a given OutputStream (and
     * compare the output to the expected one if required).
     */

    public Controller(PhysicsSimulator sim, Factory<Body> fB, Factory<ForceLaw> fFL) {
        _fB = fB;
        _sim = sim;
        _fFL = fFL;
    }

    /*
     * Converts the input JSON into a JSONObject, using and then extracts each bbi
     * from jsonInupt, creates a corresponding body b using the bodies factory, and
     * adds it to the simulator by calling method addBody.
     */
    public void loadBodies(InputStream in) {
        /*
         * we assume that "in" includes a JSON structure of the form { "bodies":
         * [bb1,...,bbn] } where each bbi is a JSON structure defining a corresponding
         * body
         */
        // convert the input JSON into a JSONObject:
        JSONArray bodies = new JSONObject(new JSONTokener(in)).getJSONArray("bodies");
        for (int i = 0; i < bodies.length(); ++i) {
            _sim.addBody(_fB.createInstance(bodies.getJSONObject(i)));
        }
    }

    /*
     * it runs the simulator n steps, and prints the different states out in the
     * following JSON format: { "states": [s0,s1,...,sn] } where s0 is the state of
     * the simulator before executing any step, and each si with i >= 1 is the state
     * of the simulator immediately after executing the i-th simulation step
     */
    public void run(int n, OutputStream out, InputStream expOut, StateComparator cmp) throws StatesMismatchException {
        PrintStream p = new PrintStream(out);
        JSONArray arrayExpStates = null;
        if (expOut != null) {
            arrayExpStates = new JSONObject(new JSONTokener(expOut)).getJSONArray("states");
        }

        p.println("{");
        p.println("\"states\": [");

        try {

            for (int i = 0; i < n; ++i) {
                if (arrayExpStates != null)
                    compareUsingCmp(_sim.getState(), arrayExpStates.getJSONObject(i), cmp, i);
                p.println(_sim.toString() + ',');
                _sim.advance();
            }
            // last one has no final comma
            p.println(_sim.toString());
            if (arrayExpStates != null)
                compareUsingCmp(_sim.getState(), arrayExpStates.getJSONObject(n), cmp, n);

            p.println("]");
            p.println("}");

        } catch (StatesMismatchException ex) {
            p.println(ex.getMessage());
            p.println("Cause: ");
            p.println(ex.getCause().getMessage());
        }

    }

    /*
     * it runs the simulator n steps, without printing any output, and without
     * making a comparison via a StateComparator
     */
    public void run(int n) throws StatesMismatchException {
        run(n, new OutputStream() {
            @Override
            public void write(int b) throws IOException {
            };
        }, null, null); // TODO okay?
    }

    // compares JSONObject's j1 and j2 using StateComparator cmp, and throws
    // StatesMismatchException if they aren't equal,
    /// otherwise it does nothing
    private void compareUsingCmp(JSONObject j1, JSONObject j2, StateComparator cmp, int execStep)
            throws StatesMismatchException {
        try {
            cmp.equal(j1, j2);
        } catch (StatesMismatchException c) {
            throw new StatesMismatchException(String.format("Simulation step number: " + execStep + " . %n States j1: "
                    + j1.toString(5) + "%n%n and %n%n" + "j2: " + j2.toString(5) + "%n%n" + "Differ %n"), c);
        }
    }

    /* calls reset of the simulator. */
    public void reset() {
        _sim.reset();
    }

    // calls setDeltaTime of the simulator
    public void setDeltaTime(double dt) {
        _sim.setDeltaTime(dt);
    }

    // calls addObserver of the simulator.
    public void addObserver(SimulatorObserver o) {
        _sim.addObserver(o);
    }

    /*
     * returns the list returned by calling get- Info() of the force laws factory.
     * This will be used in the GUI to show the available force laws and allow
     * changing them. public void
     */
    public List<JSONObject> getForceLawsInfo() {
        return _fFL.getInfo();
    }

    /*
     * uses the current force laws factory to create a new force laws object as
     * indicated in info, and then changes the simulator’s force laws to the new
     * one.
     */
    public void setForceLaws(JSONObject info) {
        _sim.setForceLaws(_fFL.createInstance(info));
    }

}
