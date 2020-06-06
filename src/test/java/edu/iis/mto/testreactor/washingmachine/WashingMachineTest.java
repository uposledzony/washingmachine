package edu.iis.mto.testreactor.washingmachine;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.hamcrest.*;

@ExtendWith(MockitoExtension.class)
class WashingMachineTest {

    @Mock
    private DirtDetector dirtDetector;
    @Mock
    private Engine engine;
    @Mock
    private WaterPump waterPump;
    private WashingMachine washingMachine;

    @BeforeEach
    void setUp() throws Exception {
        washingMachine = new WashingMachine(dirtDetector, engine, waterPump);
    }

    @Test
    void tooHeavyLaundryBatchShouldCauseInResultErrorLaundryStatus() {

    }

    @Test
    void normalLaundryBatchOfNormalWeightShouldNotCauseInResultErrorLaundryStatus() {

    }

    @Test
    void woolLaundryBatchOfWeightGreaterThanMaxBatchWeightShouldCauseInResultErrorLaundryStatus() {

    }

    private LaundryStatus success(Program program) {
        return LaundryStatus.builder()
                .withErrorCode(ErrorCode.NO_ERROR)
                .withResult(Result.SUCCESS)
                .withRunnedProgram(program).build();
    }

    private LaundryStatus error(ErrorCode code, Program program) {
        return LaundryStatus.builder()
                .withResult(Result.FAILURE)
                .withRunnedProgram(program)
                .withErrorCode(code)
                .build();
    }
}
