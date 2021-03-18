package simulator.factories;

import simulator.model.ForceLaws;
import org.json.JSONObject;
import simulator.model.NewtonUniversalGravitation;


public class NewtonUniversalGravitationBuilder extends Builder<ForceLaws> {
    
    private static String NewtonUniversalGravitationBuilderType = "newtonUG";

    public NewtonUniversalGravitationBuilder(){
        _type = NewtonUniversalGravitationBuilderType;
    }
    
    protected JSONObject getData() { 
        JSONObject data = new JSONObject();
        data.put("G", "Value of the constant G");

        return data;
    }

    protected ForceLaws createNewT(JSONObject info) {
        try {
            return new NewtonUniversalGravitation(info.getDouble("G"));
        } catch (RuntimeException e) {
            throw new IllegalArgumentException();
        }
    }
}
