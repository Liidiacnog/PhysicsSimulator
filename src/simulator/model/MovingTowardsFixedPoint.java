package simulator.model;

import java.util.List;
import simulator.misc.Vector2D;


public class MovingTowardsFixedPoint implements ForceLaw {
	
	private double _g;
    private Vector2D _c;

    /*simulates a scenario in which we apply a force in the direction of a fixed point c.
    F = m/g in the direction of (c - pos of Body)*/
	public MovingTowardsFixedPoint(Vector2D c, double g){
		_g = g;
        _c = new Vector2D(c);
	}

	public void apply(List<Body> bs){
		for(Body b: bs){
            computeForce(b);
        }
	}

	//computes the corresponding Fixed Point force, and adds it to b1's force vector
	private void computeForce(Body b1) {
		Vector2D dir =  _c.minus(b1.getPosition());
        double distance = dir.magnitude();
		if(distance > 0 && b1.getMass() > 0 && _g > 0){
			Vector2D dirUnityV = dir.direction();
			double factor = b1.getMass() / _g;
			
            Vector2D result = dirUnityV.scale(factor);

			b1.addForce(b1.getForce().plus(result));
        }
	}
	
	public String toString(){
		return "Moving towards " + _c + " with constant acceleration " +_g;
	}

}


