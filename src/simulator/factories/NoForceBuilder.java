package simulator.factories;

import simulator.model.ForceLaws;
import simulator.model.NoForce;

public class NoForceBuilder extends Builder<ForceLaws> {
    
    private static String NoForceBuilderType = "ng";
    protected String _desc = "No force acts upon the bodies"; 

    public NoForceBuilder(){
        _type = NoForceBuilderType;
    }
    
    protected ForceLaws createNewT() {
        return new NoForce();
    }
}
