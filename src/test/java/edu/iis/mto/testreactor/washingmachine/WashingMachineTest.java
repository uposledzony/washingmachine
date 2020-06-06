package edu.iis.mto.testreactor.washingmachine;

import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Assertions.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.mockito.internal.matchers.Null;
import org.mockito.junit.jupiter.MockitoExtension;
import static edu.iis.mto.testreactor.washingmachine.WashingMachine.MAX_WEIGHT_KG;
import static edu.iis.mto.testreactor.washingmachine.WashingMachine.AVERAGE_DEGREE;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

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
    private final Percentage PERCENTAGE_GREATER_THEN_AVERAGE_PERCENTAGE = new Percentage(50 + 1);

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

    @Test
    void checkIfProgramIsSetToLongProgramIfStartProgramIsAutoDetectAndDirtDetectorReturnAnInformationAboutTooMuchDirtPercentage() {
        when(dirtDetector.detectDirtDegree(Mockito.any(LaundryBatch.class)))
                .thenReturn(PERCENTAGE_GREATER_THEN_AVERAGE_PERCENTAGE);

        var batch = LaundryBatch.builder().withMaterialType(STANDARD_MATERIAL).withWeightKg(NORMAL_WEIGHT).build();
        var configuration = ProgramConfiguration.builder().withProgram(Program.AUTODETECT).withSpin(true).build();
        var status = washingMachine.start(batch, configuration);

        assertThat(status, is(not(error(ErrorCode.TOO_HEAVY, null))));
        assertThat(status, is(success(Program.LONG)));
    }

    @Test
    void checkIfProgramIsSetToMediumProgramIfStartProgramIsAutoDetectAndDirtDetectorReturnAnInformationAboutAverageDirtPercentage() {
        when(dirtDetector.detectDirtDegree(Mockito.any(LaundryBatch.class)))
                .thenReturn(AVERAGE_DEGREE);

        var batch = LaundryBatch.builder().withMaterialType(STANDARD_MATERIAL).withWeightKg(NORMAL_WEIGHT).build();
        var configuration = ProgramConfiguration.builder().withProgram(Program.AUTODETECT).withSpin(true).build();
        var status = washingMachine.start(batch, configuration);

        assertThat(status, is(not(error(ErrorCode.TOO_HEAVY, null))));
        assertThat(status, is(success(Program.MEDIUM)));
    }

    @Test
    void checkIfWashingProcessIsExecutedInProperOrder() throws WaterPumpException, EngineException {
        when(dirtDetector.detectDirtDegree(Mockito.any(LaundryBatch.class)))
                .thenReturn(AVERAGE_DEGREE);

        var batch = LaundryBatch.builder().withMaterialType(STANDARD_MATERIAL).withWeightKg(NORMAL_WEIGHT).build();
        var configuration = ProgramConfiguration.builder().withProgram(Program.AUTODETECT).withSpin(true).build();
        washingMachine.start(batch, configuration);
        var callOrder = inOrder(waterPump, engine, dirtDetector);
        var expectedProgram = Program.MEDIUM;

        callOrder.verify(dirtDetector).detectDirtDegree(batch);
        callOrder.verify(waterPump).pour(NORMAL_WEIGHT);
        callOrder.verify(engine).runWashing(expectedProgram.getTimeInMinutes());
        callOrder.verify(waterPump).release();
        callOrder.verify(engine).spin();
    }

    @Test
    void checkIfRunningWashingProgramWithPassedNullParametersCausesNPE() {
        Assertions.assertThrows(NullPointerException.class, () -> washingMachine.start(null, null));
    }

    @Test
    void checkIfRunningWashingProgramWithPassedNullBatchCausesNPE() {
        var configuration = ProgramConfiguration.builder().withProgram(Program.AUTODETECT).withSpin(true).build();

        Assertions.assertThrows(NullPointerException.class, () -> washingMachine.start(null, configuration));
    }

    @Test
    void checkIfRunningWashingProgramWithPassedNullConfigurationCausesInResultUnknownStatusError() {
        var batch = LaundryBatch.builder().withMaterialType(STANDARD_MATERIAL).withWeightKg(NORMAL_WEIGHT).build();
        var status = washingMachine.start(batch, null);

        assertThat(status, is(equalTo(error(ErrorCode.UNKNOWN_ERROR, null))));
    }

    @Test
    void checkIfDirtDetectorCausedExceptionProgramShouldEndUpWithUnknownErrorStatus() {
        when(dirtDetector.detectDirtDegree(Mockito.any())).thenThrow(RuntimeException.class);

        var configuration = ProgramConfiguration.builder().withProgram(Program.AUTODETECT).withSpin(true).build();
        var batch = LaundryBatch.builder().withMaterialType(STANDARD_MATERIAL).withWeightKg(NORMAL_WEIGHT).build();
        var status = washingMachine.start(batch, configuration);

        assertThat(status, is(equalTo(error(ErrorCode.UNKNOWN_ERROR, null))));
    }

    @Test
    void checkIfEngineCausedEngineExceptionProgramShouldEndUpWithEngineFailureStatus() throws EngineException {
        when(dirtDetector.detectDirtDegree(Mockito.any(LaundryBatch.class)))
                .thenReturn(AVERAGE_DEGREE);
        var expectedProgram = Program.MEDIUM;
        doThrow(EngineException.class).when(engine).runWashing(expectedProgram.getTimeInMinutes());
        var configuration = ProgramConfiguration.builder().withProgram(Program.AUTODETECT).withSpin(true).build();
        var batch = LaundryBatch.builder().withMaterialType(STANDARD_MATERIAL).withWeightKg(NORMAL_WEIGHT).build();
        var status = washingMachine.start(batch, configuration);

        assertThat(status, is(equalTo(error(ErrorCode.ENGINE_FAILURE, expectedProgram))));
    }

    @Test
    void checkIfWaterPumpCausedWaterPumpExceptionProgramShouldEndUpWithWaterPumpFailureStatus() throws WaterPumpException {
        var expectedProgram = Program.MEDIUM;
        doThrow(WaterPumpException.class).when(waterPump).release();
        var configuration = ProgramConfiguration.builder().withProgram(expectedProgram).withSpin(true).build();
        var batch = LaundryBatch.builder().withMaterialType(STANDARD_MATERIAL).withWeightKg(NORMAL_WEIGHT).build();
        var status = washingMachine.start(batch, configuration);

        assertThat(status, is(equalTo(error(ErrorCode.WATER_PUMP_FAILURE, expectedProgram))));
    }

    @Test
    void programConfigurationWithoutSpinProvidedToWashingMachineShouldNotMakeEngineToSpin() throws EngineException {
        var expectedProgram = Program.MEDIUM;
        var configuration = ProgramConfiguration.builder().withProgram(expectedProgram).withSpin(false).build();
        var batch = LaundryBatch.builder().withMaterialType(STANDARD_MATERIAL).withWeightKg(NORMAL_WEIGHT).build();
        var status = washingMachine.start(batch, configuration);
        verify(engine, times(0)).spin();
    }

    @Test
    void programConfigurationWithSpinProvidedToWashingMachineShouldMakeEngineToSpinExactlyOneTime() throws EngineException {
        var expectedProgram = Program.MEDIUM;
        var configuration = ProgramConfiguration.builder().withProgram(expectedProgram).withSpin(true).build();
        var batch = LaundryBatch.builder().withMaterialType(STANDARD_MATERIAL).withWeightKg(NORMAL_WEIGHT).build();
        var status = washingMachine.start(batch, configuration);
        verify(engine, times(1)).spin();
    }

    @Test
    void programWithoutAutoDetectionOfDirtShouldNotCallDirtDetector() {
        var expectedProgram = Program.MEDIUM;
        var configuration = ProgramConfiguration.builder().withProgram(expectedProgram).withSpin(true).build();
        var batch = LaundryBatch.builder().withMaterialType(STANDARD_MATERIAL).withWeightKg(NORMAL_WEIGHT).build();
        var status = washingMachine.start(batch, configuration);
        verify(dirtDetector, times(0)).detectDirtDegree(batch);
    }

    @Test
    void programWithAutoDetectionOfDirtShouldCallDirtDetector() {
        var expectedProgram = Program.AUTODETECT;
        var configuration = ProgramConfiguration.builder().withProgram(expectedProgram).withSpin(true).build();
        var batch = LaundryBatch.builder().withMaterialType(STANDARD_MATERIAL).withWeightKg(NORMAL_WEIGHT).build();
        var status = washingMachine.start(batch, configuration);
        verify(dirtDetector, times(1)).detectDirtDegree(batch);
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
