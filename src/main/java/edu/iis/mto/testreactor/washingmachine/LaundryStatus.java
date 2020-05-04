package edu.iis.mto.testreactor.washingmachine;

import java.util.Objects;

public class LaundryStatus {

    private final Result result;
    private final Program runnedProgram;
    private final ErrorCode errorCode;

    private LaundryStatus(Builder builder) {
        this.result = builder.result;
        this.runnedProgram = builder.runnedProgram;
        this.errorCode = builder.errorCode;
    }

    public Result getResult() {
        return result;
    }

    public Program getRunnedProgram() {
        return runnedProgram;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorCode, result, runnedProgram);
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
        LaundryStatus other = (LaundryStatus) obj;
        return errorCode == other.errorCode && result == other.result && runnedProgram == other.runnedProgram;
    }

    @Override
    public String toString() {
        return "LaundryStatus [result=" + result + ", runnedProgram=" + runnedProgram + ", errorCode=" + errorCode + "]";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Result result;
        private Program runnedProgram;
        private ErrorCode errorCode;

        private Builder() {}

        public Builder withResult(Result result) {
            this.result = result;
            return this;
        }

        public Builder withRunnedProgram(Program runnedProgram) {
            this.runnedProgram = runnedProgram;
            return this;
        }

        public Builder withErrorCode(ErrorCode errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public LaundryStatus build() {
            return new LaundryStatus(this);
        }
    }

}
