package simulator.model;

import java.util.List;

public interface SimulatorObserver {
    /*
     * bodies is the current list of bodies; b is a body, time is the current time
     * of the simulator; dt is the current delta-time, i.e., the real time per step;
     * fLawsDesc is a description of the current force laws (obtained by calling
     * toString of the current force laws)
     */

    default public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc){
        //Default: do nothing
    }

    default public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc){
        //Default: do nothing
    }

    default public void onBodyAdded(List<Body> bodies, Body b){
        //Default: do nothing
    }

    default public void onAdvance(List<Body> bodies, double time){
        //Default: do nothing
    }

    default public void onDeltaTimeChanged(double dt){
        //Default: do nothing
    }

    default public void onForceLawsChanged(String fLawsDesc){
        //Default: do nothing
    }

}
