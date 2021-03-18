package simulator.factories;

import org.json.JSONObject;

import simulator.control.MassEqualStates;
import simulator.control.StateComparator;

public class MassEqualStatesBuilder extends Builder<StateComparator>{
    

    private static String MassEqualStatesBuilderType = "massEq";

    public MassEqualStatesBuilder(){
        _type = MassEqualStatesBuilderType;
    }

    protected StateComparator createNewT(JSONObject info) {
        try {
            return new MassEqualStates();
        } catch (RuntimeException e) {
            throw new IllegalArgumentException();
        }
    }

}
