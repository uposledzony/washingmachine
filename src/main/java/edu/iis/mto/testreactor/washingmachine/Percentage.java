package edu.iis.mto.testreactor.washingmachine;

import static java.lang.Double.doubleToLongBits;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

public class Percentage {

    private final double value;

    public Percentage(double value) {
        if (value < 0.0d || value > 100.0d) {
            throw new IllegalArgumentException("value must be in range <0,100>");
        }
        this.value = value;
    }

    public boolean isGreaterThan(Percentage other) {
        return requireNonNull(other, "other == null").value < this.value;
    }

    public boolean lowerThan(Percentage other) {
        return requireNonNull(other, "other == null").value > this.value;
    }

    @Override
    public int hashCode() {
        return hash(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Percentage other = (Percentage) obj;
        return doubleToLongBits(value) == doubleToLongBits(other.value);
    }

}
