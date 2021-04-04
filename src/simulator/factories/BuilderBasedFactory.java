package simulator.factories;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import simulator.model.*;
import simulator.misc.*;

public class BuilderBasedFactory<T> implements Factory<T> {

    private List<JSONObject> info;
    private List<Builder<T>> _builders;

    public BuilderBasedFactory(List<Builder<T>> builders){
        _builders = new ArrayList<>(builders);

        info = new ArrayList<>();
        for (int i = 0; i < builders.size(); i++) {
            info.add(builders.get(i).getBuilderInfo());
        }
    }


/*tries the builders one by one until it succeeds to create a corresponding instance — throws IllegalArgumentException in
case of failure.*/
    public T createInstance(JSONObject info) throws IllegalArgumentException { //TODO since it inherits from RunTimeExc should we declare that it throws it?
        T inst = null;
        //int i = 0;
        boolean found = false;
        for (int i = 0; i < _builders.size() && !found; ++i){ //TODO ask, change for a while loop?
            inst = _builders.get(i).createInstance(info);
            if (inst != null) {
                found = true;
            }
        }
        /*while(!found && i < _builders.size()) {
            inst = _builders.get(i).createInstance(info);
            if (inst != null) {
                found = true;
            }
        }*/
        
        if (inst == null){
            throw new IllegalArgumentException("No valid type found matching the information provided"); 
        }

        return inst;
    }

    /* Method getInfo() of this class aggregates the JSON structures returned
by getBuilderInfo() of all builders in a list and returns it (create the list and aggregate the
information in the constructor, to avoid creating it every time).*/
    public List<JSONObject> getInfo() {
        return info;
    }

    public static void main(String[] args) {
        test1();
    }

    public static void test1(){ //TODO remove
        /*
        Body b1 = new MassLosingBody("1", new Vector2D(), new Vector2D(), 10, 0.1, 3);
		Body b2 = new MassLosingBody("2", new Vector2D(), new Vector2D(1, 1), 10, 0.1, 3);
        */
        List<Builder<Body>> bodyBuilders = new ArrayList<>();
        bodyBuilders.add(new BasicBodyBuilder());
        bodyBuilders.add(new MassLosingBodyBuilder());
        Factory<Body> bodyFactory = new BuilderBasedFactory<Body>(bodyBuilders);

        JSONObject j1 = new JSONObject();
        JSONObject data1 = new JSONObject();
        data1.put("id", "1");
        data1.put("v", new Vector2D().asJSONArray());
        data1.put("p", new Vector2D().asJSONArray());
        data1.put("m", 10);
        //data1.put("freq", 0.1);
        //data1.put("factor", 3);
        j1.put("type", "basic");
        j1.put("data", data1);

        JSONObject j2 = new JSONObject();
        JSONObject data2 = new JSONObject();
        data2.put("id", "2");
        data2.put("v", new Vector2D().asJSONArray());
        data2.put("p", new Vector2D(1, 1).asJSONArray());
        data2.put("m", 10);
        data2.put("freq", 0.1);
        data2.put("factor", 3);
        j2.put("type", "mlb");
        j2.put("data", data2);

        Body b1 = bodyFactory.createInstance(j1);
        Body b2 = bodyFactory.createInstance(j2);
		double t = 0.5;
		List<Body> bs = new ArrayList<>();
		bs.add(b1);
		bs.add(b2);
		double G = 6.67E-11;
		NewtonUniversalGravitation f = new NewtonUniversalGravitation(G);
		f.apply(bs);
		for (int i = 0; i < 300; i++) {
			System.out.println("f: " + b1.getForce() + ", p: " + b1.getPosition());
			b1.move(t);
			System.out.println("f: " + b2.getForce() + ", p: " + b2.getPosition());
			b2.move(t);
		} 
    }

    public static void test2() {
        List<Builder<Body>> bodyBuilders = new ArrayList<>();
        bodyBuilders.add(new BasicBodyBuilder());
        bodyBuilders.add(new MassLosingBodyBuilder());
        Factory<Body> bodyFactory = new BuilderBasedFactory<Body>(bodyBuilders);
    }
    
}
