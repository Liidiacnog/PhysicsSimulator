package simulator.model;

import java.util.List;

public interface SimulatorObserver {
    /*
     * bodies is the current list of bodies; b is a body, time is the current time
     * of the simulator; dt is the current delta-time, i.e., the real time per step;
     * fLawsDesc is a description of the current force laws (obtained by calling
     * toString of the current force laws)
     */

    public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc);

    public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc);

    public void onBodyAdded(List<Body> bodies, Body b);

    public void onAdvance(List<Body> bodies, double time);

    public void onDeltaTimeChanged(double dt);

    public void onForceLawsChanged(String fLawsDesc);

}
