package simulator.factories;

import simulator.control.StateComparator;
import simulator.control.EpsilonEqualStates;

import org.json.JSONException;
import org.json.JSONObject;


public class EpsilonEqualStatesBuilder extends Builder<StateComparator> {
    
    private static String EpsilonEqualStatesBuilderType = "epseq";
    private static final String EpsilonEqualStatesBuilderDesc = "Compares 2 states in terms of their “time” and “id” keys, and" +
                                " an epsilon equality for keys “m”, “p”, “v” and “f” of the i-th bodies in their lists";


    public EpsilonEqualStatesBuilder(){
        _type = EpsilonEqualStatesBuilderType;
        _desc = EpsilonEqualStatesBuilderDesc;
    }

    protected JSONObject getData() { 
        JSONObject data = new JSONObject();
        data.put("eps", "Epsilon value");
        return data;
    }

    protected StateComparator createNewT(JSONObject info) {
        try {
            return new EpsilonEqualStates(info.getDouble("eps")); 
        } catch (JSONException e) {
            return new EpsilonEqualStates(); 
            //TODO necessary?: throw new IllegalArgumentException();
        }
    }

}
