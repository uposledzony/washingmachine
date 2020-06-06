package edu.iis.mto.testreactor.washingmachine;

import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.hamcrest.*;
import static edu.iis.mto.testreactor.washingmachine.WashingMachine.MAX_WEIGHT_KG;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@ExtendWith(MockitoExtension.class)
class WashingMachineTest {

    @Mock
    private DirtDetector dirtDetector;
    @Mock
    private Engine engine;
    @Mock
    private WaterPump waterPump;
    private WashingMachine washingMachine;
    private final double HALF_OF_MAX_WEIGHT = MAX_WEIGHT_KG / 2;
    private final double NORMAL_WEIGHT = HALF_OF_MAX_WEIGHT - 1;
    private final Material STANDARD_MATERIAL = Material.COTTON;

    @BeforeEach
    void setUp() throws Exception {
        washingMachine = new WashingMachine(dirtDetector, engine, waterPump);
    }

    @Test
    void laundryBatchOfWeightGreaterThanMaxWeightShouldCauseInResultErrorLaundryStatus() {
        var batch = LaundryBatch.builder().withMaterialType(STANDARD_MATERIAL).withWeightKg(MAX_WEIGHT_KG + 1).build();
        var configuration = ProgramConfiguration.builder().withProgram(Program.MEDIUM).withSpin(true).build();
        var status = washingMachine.start(batch, configuration);

        assertThat(status, is(error(ErrorCode.TOO_HEAVY, null)));
    }

    @Test
    void normalLaundryBatchOfNormalWeightShouldNotCauseInResultErrorLaundryStatusButWithSuccess() {
        var batch = LaundryBatch.builder().withMaterialType(STANDARD_MATERIAL).withWeightKg(NORMAL_WEIGHT).build();
        var configuration = ProgramConfiguration.builder().withProgram(Program.MEDIUM).withSpin(true).build();
        var status = washingMachine.start(batch, configuration);

        assertThat(status, is(not(error(ErrorCode.TOO_HEAVY, null))));
        assertThat(status, is(success(Program.MEDIUM)));
    }

    @Test
    void woolLaundryBatchOfWeightGreaterThanHalfOfMaxBatchWeightShouldCauseInResultErrorLaundryStatus() {
        var batch = LaundryBatch.builder().withMaterialType(Material.WOOL).withWeightKg(HALF_OF_MAX_WEIGHT + 1).build();
        var configuration = ProgramConfiguration.builder().withProgram(Program.MEDIUM).withSpin(true).build();
        var status = washingMachine.start(batch, configuration);

        assertThat(status, is(error(ErrorCode.TOO_HEAVY, null)));
    }

    @Test
    void jeansLaundryBatchOfWeightGreaterThanHalfOfMaxBatchWeightShouldCauseInResultErrorLaundryStatus() {
        var batch = LaundryBatch.builder().withMaterialType(Material.JEANS).withWeightKg(HALF_OF_MAX_WEIGHT + 1).build();
        var configuration = ProgramConfiguration.builder().withProgram(Program.MEDIUM).withSpin(true).build();
        var status = washingMachine.start(batch, configuration);

        assertThat(status, is(error(ErrorCode.TOO_HEAVY, null)));
    }

    @Test
    void jeansLaundryBatchOfWeightLesserThanHalfOfMaxBatchWeightShouldCauseInResultSuccessLaundryStatus() {
        var batch = LaundryBatch.builder().withMaterialType(Material.JEANS).withWeightKg(NORMAL_WEIGHT).build();
        var configuration = ProgramConfiguration.builder().withProgram(Program.MEDIUM).withSpin(true).build();
        var status = washingMachine.start(batch, configuration);

        assertThat(status, is(not(error(ErrorCode.TOO_HEAVY, null))));
        assertThat(status, is(success(Program.MEDIUM)));
    }

    @Test
    void woolLaundryBatchOfWeightLesserThanHalfOfMaxBatchWeightShouldCauseInResultSuccessLaundryStatus() {
        var batch = LaundryBatch.builder().withMaterialType(Material.WOOL).withWeightKg(NORMAL_WEIGHT).build();
        var configuration = ProgramConfiguration.builder().withProgram(Program.MEDIUM).withSpin(true).build();
        var status = washingMachine.start(batch, configuration);

        assertThat(status, is(not(error(ErrorCode.TOO_HEAVY, null))));
        assertThat(status, is(success(Program.MEDIUM)));
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
