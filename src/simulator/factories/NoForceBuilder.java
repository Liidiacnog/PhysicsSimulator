package simulator.factories;

import simulator.model.ForceLaws;
import simulator.model.NoForce;

public class NoForceBuilder extends Builder<ForceLaws> {
    
    private static String NoForceBuilderType = "noForce";

    private NoForceBuilder(){
        _type = NoForceBuilderType;
    }
    
    protected ForceLaws createNewT() {
        return new NoForce();
    }
}
