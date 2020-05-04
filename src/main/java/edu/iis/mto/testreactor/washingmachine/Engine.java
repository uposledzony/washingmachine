package edu.iis.mto.testreactor.washingmachine;

public interface Engine {

    void runWashing(int timeInMinutes) throws EngineException;

    void spin() throws EngineException;

}
