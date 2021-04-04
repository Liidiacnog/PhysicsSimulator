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
            if (info.get("G") != null)
                return new NewtonUniversalGravitation(info.getDouble("G"));
            else
                return new NewtonUniversalGravitation();
        } catch (JSONException e) {
             //TODO okay or should we check that they have introduced either a valid G or nothing, and no other parameter?
             throw new IllegalArgumentException("Invalid value for G");
        }
    }
}
