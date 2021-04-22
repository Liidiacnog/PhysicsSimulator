package simulator.factories;

import simulator.model.ForceLaw;
import org.json.JSONException;
import org.json.JSONObject;
import simulator.model.NewtonUniversalGravitation;


public class NewtonUniversalGravitationBuilder extends Builder<ForceLaw> {
    
    private static String NewtonUniversalGravitationBuilderType = "nlug";
    private static final String NewtonUniversalGravitationBuilderDesc = "Newton’s law of universal gravitation"; 
    private final static Double DefaultGravitationalConstant = 6.67E-11;

	
    public NewtonUniversalGravitationBuilder(){
        _type = NewtonUniversalGravitationBuilderType;
        _desc = NewtonUniversalGravitationBuilderDesc;
    }
    
    protected JSONObject getData() { 
        JSONObject data = new JSONObject();
        data.put("G", "the gravitational constant (a number)");
        return data;
    }

    protected ForceLaw createNewT(JSONObject info) {
        try {
            if (info.has("G"))
                return new NewtonUniversalGravitation(info.getDouble("G"));
            else
                return new NewtonUniversalGravitation(DefaultGravitationalConstant);
        } catch (JSONException e) {
            throw new IllegalArgumentException("Invalid value for parameter G");
        }
    }
}
