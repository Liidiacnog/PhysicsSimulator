package simulator.model;

import java.util.List;

public interface ForceLaw {
	default public void apply(List<Body> bs){ /*do nothing*/ }
}
