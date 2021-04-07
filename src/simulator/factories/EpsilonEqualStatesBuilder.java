package simulator.factories;

import simulator.control.StateComparator;
import simulator.control.EpsilonEqualStates;

import org.json.JSONException;
import org.json.JSONObject;


public class EpsilonEqualStatesBuilder extends Builder<StateComparator> {
    
    private static String EpsilonEqualStatesBuilderType = "epseq";
    private static final String EpsilonEqualStatesBuilderDesc = "Compares 2 states in terms of their “time” and “id” keys," +
                                " and epsilon equality for keys “m”, “p”, “v” and “f” of the i-th bodies in their lists";


    public EpsilonEqualStatesBuilder(){
        _type = EpsilonEqualStatesBuilderType;
        _desc = EpsilonEqualStatesBuilderDesc;
    }

    protected JSONObject getData() { 
        JSONObject data = new JSONObject();
        data.put("eps", "Epsilon value");
        return data;
    }

    protected StateComparator createNewT(JSONObject info) throws IllegalArgumentException {
        try {
            return new EpsilonEqualStates(info.getDouble("eps"));
        } catch (JSONException e) {
            return new EpsilonEqualStates();
            //TODO necessary?: throw new IllegalArgumentException("Epsilon state comparator could not be created with the given epsilon");
            //y si le han dado un valor a eps, pero el valor no es valido (por ejemplo es un string) //TODO should we check sth else or just create it with the default value for eps?
            //debería entonces tirar excepción o coger el valor predeterminado?
            //yo creo que lo primero, el predeterminado sería si no tiene valor
        }
    }

}
