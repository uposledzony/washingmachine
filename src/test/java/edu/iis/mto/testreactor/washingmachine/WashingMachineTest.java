package edu.iis.mto.testreactor.washingmachine;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WashingMachineTest {

    @Mock
    private DirtDetector dirtDetector;
    @Mock
    private Engine engine;
    @Mock
    private WaterPump waterPump;
    private WashingMachine washingMashine;

    @BeforeEach
    void setUp() throws Exception {
        washingMashine = new WashingMachine(dirtDetector, engine, waterPump);
    }

    @Test
    void tooHeavyLaundryBatchShouldCauseInResultErrorLaundryStatus() {
        fail("Not yet implemented");
    }

    @Test
    void normalLaundryBatchOfNormalWeightShouldNotCauseInResultErrorLaundryStatus() {

    }

    @Test
    void woolLaundryBatchOfWeightGreaterThanMaxBatchWeightShouldCauseInResultErrorLaundryStatus() {
        
    }
}
