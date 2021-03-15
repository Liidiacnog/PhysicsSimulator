package simulator.factories;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T> {

    private ArrayList<Builder<T>> bList = new ArrayList<Builder<T>>(); //TODO better with LinkedList? and copy using pointers?

    public BuilderBasedFactory(List<Builder<T>> builders){
        for( int i = 0; i < builders.size(); i++)
            bList.add(builders.get(i));
    }



    /*The following is an example of how we can create a factory of bodies using the classes
that we have developed:
ArrayList<Builder<Body>> bodyBuilders = new ArrayList<>();
bodyBuilders.add(new BasicBodyBuilder());
bodyBuilders.add(new MassLosingBodyBuilder());
Factory<Body> bodyFactory = new BuilderBasedFactory<Body>(bodyBuilders);*/

/*tries the builders one by one until it succeeds to create a corresponding instance — throws IllegalArgumentException in
case of failure.*/
    @Override
    public T createInstance(JSONObject info) throws IllegalArgumentException {
        T inst = null;
        boolean found = false;
        for(int i = 0; i < bList.size() && !found; ++i){
            try{
                bList.get(i).createInstance(info);
                found = true;
            }catch(IllegalArgumentException ex){
                if(bList.get(i).matchType(info.getString("type")))
                    throw new IllegalArgumentException(); //TODO fill cause mssg (Incorrect data for a certain existing type)
                    //else check new builder's type
            }
        }
            
        return inst;
    }

    /* Method getInfo() of this class aggregates the JSON structures returned
by getBuilderInfo() of all builders in a list and returns it (create the list and aggregate the
information in the constructor, to avoid creating it every time).*/
    @Override
    public List<JSONObject> getInfo() {
        
        List<JSONObject> list = new ArrayList<JSONObject>(); //TODO what does it mean when it says "to avoid creating it every time"?
        //TODO not finished

        return null;
    }
    
}
