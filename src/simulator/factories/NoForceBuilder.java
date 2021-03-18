package simulator.factories;

import simulator.model.ForceLaws;
import simulator.model.NoForce;

public class NoForceBuilder extends Builder<ForceLaws> {
    
    private static String NoForceBuilderType = "noForce";

    public NoForceBuilder(){
        _type = NoForceBuilderType;
    }
    
    protected ForceLaws createNewT() {
        try {
            return new NoForce();
        } catch (RuntimeException e) {
            throw new IllegalArgumentException();
        }
    }
}
