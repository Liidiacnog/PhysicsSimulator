package simulator.factories;

import simulator.control.StateComparator;
import simulator.control.EpsilonEqualStates;

import org.json.JSONException;
import org.json.JSONObject;


public class EpsilonEqualStatesBuilder extends Builder<StateComparator> {
    
    private static String EpsilonEqualStatesBuilderType = "epseq";
    private static final String EpsilonEqualStatesBuilderDesc = "Compares 2 states in terms of their “time” and “id” keys," +
                                " and epsilon equality for keys “m”, “p”, “v” and “f” of the i-th bodies in their lists";


    public EpsilonEqualStatesBuilder(){
        _type = EpsilonEqualStatesBuilderType;
        _desc = EpsilonEqualStatesBuilderDesc;
    }

    protected JSONObject getData() { 
        JSONObject data = new JSONObject();
        data.put("eps", "Epsilon value");
        return data;
    }

    protected StateComparator createNewT(JSONObject info) throws IllegalArgumentException {
        try {
            if (info.has("eps")) {
                return new EpsilonEqualStates(info.getDouble("eps"));
            } else {
                return new EpsilonEqualStates();
            }
        } catch (JSONException e) {
            throw new IllegalArgumentException("Invalid value for eps");
        }
    }

}
