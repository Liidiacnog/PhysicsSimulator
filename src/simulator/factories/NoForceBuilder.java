package simulator.factories;

import org.json.JSONObject;
import simulator.model.ForceLaw;
import simulator.model.NoForce;

public class NoForceBuilder extends Builder<ForceLaw> {
    
    private static String NoForceBuilderType = "nf";
    private static final String NoForceBuilderDesc = "No force"; 

    public NoForceBuilder(){
        _type = NoForceBuilderType;
        _desc = NoForceBuilderDesc;
    }
    
    protected ForceLaw createNewT(JSONObject info) {
        return new NoForce();
    }

}
