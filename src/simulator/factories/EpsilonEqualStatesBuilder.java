package simulator.factories;

import simulator.control.StateComparator;
import simulator.control.EpsilonEqualStates;
import org.json.JSONObject;


public class EpsilonEqualStatesBuilder extends Builder<StateComparator> {
    
    private static String EpsilonEqualStatesBuilderType = "epseq";

    public EpsilonEqualStatesBuilder(){
        _type = EpsilonEqualStatesBuilderType;
    }

    protected JSONObject getData() { 
        JSONObject data = new JSONObject();
        data.put("eps", "Epsilon value");

        return data;
    }

    protected StateComparator createNewT(JSONObject info) {
        try {
            return new EpsilonEqualStates(info.getDouble("eps")); 
        } catch (RuntimeException e) {
            throw new IllegalArgumentException();
        }
    }

}
