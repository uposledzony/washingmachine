package edu.iis.mto.testreactor.washingmachine;

public enum Program {
    AUTODETECT(0),
    SHORT(20),
    MEDIUM(50),
    LONG(120);

    private int timeInMinutes;

    private Program(int time) {
        this.timeInMinutes = time;
    }

    public int getTimeInMinutes() {
        return timeInMinutes;
    }

}
