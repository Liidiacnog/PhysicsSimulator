package simulator.factories;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;


public class BuilderBasedFactory<T> implements Factory<T> {

    private List<JSONObject> info;
    private List<Builder<T>> _builders;

    public BuilderBasedFactory(List<Builder<T>> builders){
        _builders = new ArrayList<Builder<T>>(builders);

        info = new ArrayList<JSONObject>();
        for (int i = 0; i < builders.size(); i++) {
            info.add(builders.get(i).getBuilderInfo());
        }
    }


    /*tries the builders one by one until it succeeds to create a corresponding instance — throws IllegalArgumentException in
    case of failure.*/
    @Override
    public T createInstance(JSONObject info) throws IllegalArgumentException { 
        T inst = null;
        boolean found = false;
        for (int i = 0; i < _builders.size() && !found; ++i){
            inst = _builders.get(i).createInstance(info);//throws IllegalArgumentException if type is valid, but data is incorrect
            if (inst != null) {
                found = true;
            }
        }
        
        if (inst == null){
            throw new IllegalArgumentException("No valid type found matching the information provided"); 
        }

        return inst;
    }

    /* Method getInfo() of this class aggregates the JSON structures returned
by getBuilderInfo() of all builders in a list and returns it (create the list and aggregate the
information in the constructor, to avoid creating it every time).*/
    @Override
    public List<JSONObject> getInfo() {
        return info;
    }
}
