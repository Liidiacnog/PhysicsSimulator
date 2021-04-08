package simulator.factories;

import simulator.model.ForceLaws;
import org.json.JSONException;
import org.json.JSONObject;
import simulator.model.NewtonUniversalGravitation;


public class NewtonUniversalGravitationBuilder extends Builder<ForceLaws> {
    
    private static String NewtonUniversalGravitationBuilderType = "nlug";
    private static final String NewtonUniversalGravitationBuilderDesc = "Newton’s law of universal gravitation"; 
    
    public NewtonUniversalGravitationBuilder(){
        _type = NewtonUniversalGravitationBuilderType;
        _desc = NewtonUniversalGravitationBuilderDesc;
    }
    
    protected JSONObject getData() { 
        JSONObject data = new JSONObject();
        data.put("G", "Constant of universal gravitation");
        return data;
    }

    protected ForceLaws createNewT(JSONObject info) {
        try {
            if (!info.isNull("G"))
                return new NewtonUniversalGravitation(info.getDouble("G"));
            else
                return new NewtonUniversalGravitation();
        } catch (JSONException e) {
            throw new IllegalArgumentException("Invalid value for parameter G");
        }
    }
}
