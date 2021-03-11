package simulator.factories;

import simulator.control.StateComparator;
import simulator.control.EpsilonEqualStates;
import org.json.JSONObject;


public class EpsilonEqualStatesBuilder extends Builder<StateComparator> {
    
    private static String EpsilonEqualStatesBuilderType = "epsEq";

    private EpsilonEqualStatesBuilder(){
        _type = EpsilonEqualStatesBuilderType;
    }

    protected JSONObject getData() { 
        JSONObject data = new JSONObject();
        data.put("eps", "Epsilon value");

        return data;
    }

    protected StateComparator createNewT(JSONObject info) {
        return new EpsilonEqualStates(info.getDouble("eps")); 
    }

}
