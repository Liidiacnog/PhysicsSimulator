package simulator.factories;

import org.json.JSONObject;
import simulator.model.ForceLaws;
import simulator.model.NoForce;

public class NoForceBuilder extends Builder<ForceLaws> {
    
    private static String NoForceBuilderType = "nf";
    private static final String NoForceBuilderDesc = "No force acts upon the bodies"; 

    public NoForceBuilder(){
        _type = NoForceBuilderType;
        _desc = NoForceBuilderDesc;
    }
    
    protected ForceLaws createNewT(JSONObject info) {
        return new NoForce();
    }

}
