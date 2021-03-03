package simulator.model;

import java.util.List;
import simulator.misc.Vector2D;


public class MovingTowardsFixedPoint implements ForceLaws {
	
	private double _g;
    private Vector2D _c;

    /*simulates a scenario in which we apply a force in the direction of a fixed point c.
    F = m/g in the direction of (c - pos of Body)*/
	public MovingTowardsFixedPoint(Vector2D c, double g){
		_g = g;
        _c = new Vector2D(c);
	}
	
	

	public void apply(List<Body> bs){
		for(int j =0; j < bs.size(); ++j){ //TODO change by implementing Iterator?
            computeForce(bs.get(j));
        }
	}

	private void computeForce(Body b1) {
		Vector2D dir =  b1.p.minus(_c);
        double distance = dir.magnitude();
		if(distance > 0 && b1.getMass() > 0 && _g > 0){
			Vector2D unityDirV = dir.scale(1/distance);
			double factor = b1.getMass()*distance / (_g);
			
            Vector2D result = unityDirV.scale(factor);

			b1.f = b1.f.plus(result);
        }
	}



}


