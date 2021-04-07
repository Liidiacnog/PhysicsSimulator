package simulator.factories;

import org.json.JSONObject;

import simulator.control.MassEqualStates;
import simulator.control.StateComparator;

public class MassEqualStatesBuilder extends Builder<StateComparator>{

    private static final String MassEqualStatesBuilderType = "masseq";
    private static final String MassEqualStatesBuilderDesc = "Compares 2 states in terms of their “time” key, and keys “id” and “mass” of the i-th bodies in their lists";

    public MassEqualStatesBuilder(){
        _type = MassEqualStatesBuilderType;
        _desc = MassEqualStatesBuilderDesc;
    }

    protected StateComparator createNewT(JSONObject info) {
        return new MassEqualStates(); //TODO could sth go wrong?
    }

}
