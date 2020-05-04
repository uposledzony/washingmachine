package edu.iis.mto.testreactor.washingmachine;

public interface WaterPump {

    void pour(double weigth) throws WaterPumpException;

    void release() throws WaterPumpException;

}
