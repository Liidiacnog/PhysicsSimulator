package simulator.control;

import org.json.JSONObject;

import simulator.control.exceptions.StatesMismatchException;

public interface StateComparator {
	boolean equal(JSONObject s1, JSONObject s2) throws StatesMismatchException; 
}
