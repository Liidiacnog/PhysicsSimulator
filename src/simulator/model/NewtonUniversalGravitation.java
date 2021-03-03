package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class NewtonUniversalGravitation {
	
	static int _G;

	public NewtonUniversalGravitation(final int G){
		_G = G;
	}
	
	public void apply(List<Body> bs){
		for(int i = 0; i < bs.size(); ++i)
			for(int j = i + 1; j < bs.size(); ++j){ //TODO change by implementing Iterator?
				 computeForce(bs.get(i), bs.get(j));
			}
	}

	private void computeForce(Body b1, Body b2) {
		Vector2D result = new Vector2D();
		double distance = b1.p.distanceTo(b2.p);
		if(distance > 0 && b1.getMass() > 0 && b2.getMass() > 0){
			Vector2D dirB1ToB2 =  b2.p.minus(b1.p);
			Vector2D dirB2ToB1 =  b1.p.minus(b2.p);
			Vector2D unityDirV_1 = dirB1ToB2.scale(1/dirB1ToB2.magnitude());
			Vector2D unityDirV_2 = dirB2ToB1.scale(1/dirB2ToB1.magnitude());
			//TODO check if unityDirVector is unity vector indeed
			double factor = _G*b1.getMass()*b2.getMass() / (distance*distance);
			result = unityDirVector.scale(factor);

			bs.get(i).f = result;
			bs.get(j).f = result.scale();
		}

		return result;
	}



}
