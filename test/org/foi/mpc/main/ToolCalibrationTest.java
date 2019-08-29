package org.foi.mpc.main;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.foi.common.filesystem.file.PropertiesFileUtility;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagJavaAdapter;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneJavaAdapter;
import org.foi.mpc.usecases.toolCalibration.ToolCalibrationOutputBoundary;
import org.foi.mpc.usecases.toolCalibration.ToolCalibratorInputBoundary;
import org.foi.mpc.usecases.toolCalibration.ToolCalibratorUseCase;
import org.foi.mpc.usecases.toolCalibration.models.*;
import org.foi.mpc.usecases.toolCalibration.view.ToolCalibrationPresenterCMD;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Properties;

import static org.foi.mpc.MPCContext.CalibrationConfigProperties.*;
import static org.junit.Assert.*;

@RunWith(HierarchicalContextRunner.class)
public class ToolCalibrationTest {
    private  String[] args = {"testPropertiesFile.properties"};
    private ToolCalibration toolCalibration;
    private File testPropertiesFile = new File("testPropertiesFile.properties");
    private Properties properties;

    @Before
    public void setUp() throws Exception {
        properties = new Properties();
        PropertiesFileUtility.createNewPropertiesFile(properties,testPropertiesFile);

        toolCalibration = new ToolCalibration();
    }

    @After
    public void tearDown() throws Exception {
        testPropertiesFile.delete();
    }

    @Test
    public void createsUseCase(){
        assertNotNull(toolCalibration.getCalibartionUseCase());
        assertTrue(toolCalibration.getCalibartionUseCase() instanceof ToolCalibratorUseCase);
    }

    @Test
    public void createsPresenter(){
        assertNotNull(toolCalibration.getPresenterCMD());
        assertTrue(toolCalibration.getPresenterCMD() instanceof ToolCalibrationPresenterCMD);
    }


    public class withUseCaseSelfShunt implements ToolCalibratorInputBoundary {
        ToolCalibrationOutputBoundary presenter;
        ToolCalibrationRequestModel requestModel;
        ToolCalibrationRequestModel inputRequestModel;
        ToolCalibrationOutputBoundary inputPresenter;
        ToolCalibrationResponseModel responseModel;

        @Before
        public void setUp() throws Exception {
            presenter = new ToolCalibrationOutputBoundary() {
                @Override
                public void presentReport(ToolCalibrationResponseModel responseModel) {

                }
            };
            toolCalibration.setCalibartionUseCase(this);
            toolCalibration.setPresenterCMD(presenter);
            requestModel = createTestRequestModel();
            createProperiesFromRequestModel(requestModel);
        }

        @Test
        public void canReadProperties(){
            toolCalibration.run(args);

            Properties parsedProperties = toolCalibration.getProperties();
            assertEquals(properties,parsedProperties);
        }

        @Test
        public void passesPresenterToUseCase(){
            toolCalibration.run(args);

            assertSame(presenter, inputPresenter);
        }

        @Test
        public void ifWrongParamsAreGivenReturnError(){
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            String[] args = {};
            ToolCalibration toolCalibration = new ToolCalibration();
            toolCalibration.run(args);

            String[] args2 = {"file.notproperties"};
            toolCalibration.run(args2);

            assertEquals("Wrong input parameters!\n" +
                                 "Wrong input parameters!file.notproperties",outContent.toString().trim());
        }

        @Test
        public void passesCorrectRequestModelToUseCase(){
            toolCalibration.run(args);

            assertNotNull(inputRequestModel);
            assertEquals(requestModel,inputRequestModel);
        }

        @Override
        public void runCalibration(ToolCalibrationOutputBoundary presenter, ToolCalibrationRequestModel requestModel) {
            this.inputPresenter = presenter;
            this.inputRequestModel = requestModel;
            presenter.presentReport(responseModel);
        }
    }

    private Properties createProperiesFromRequestModel(ToolCalibrationRequestModel requestModel) {
        properties.clear();
        properties.put(BASE_TOOL_NAME,requestModel.baseToolName);
        properties.put(CALIBRATE_TOOL_NAME,requestModel.toCalibrateToolName);
        properties.put(WORKING_DIR,requestModel.workingDir.getPath());
        properties.put(INPUT_DIR,requestModel.inputDirWithCalibrationCases.getPath());
        properties.put(BASE_PARAM_NAME+"1",requestModel.baseToolParams.get(0).paramName);
        properties.put(BASE_PARAM_VALUE+"1",String.valueOf(requestModel.baseToolParams.get(0).paramValue));


        testPropertiesFile.delete();
        PropertiesFileUtility.createNewPropertiesFile(properties,testPropertiesFile);

        return properties;
    }

    private ToolCalibrationRequestModel createTestRequestModel() {
        ToolCalibrationRequestModel requestModel = new ToolCalibrationRequestModel();
        requestModel.baseToolParams = new ArrayList<>();
        ToolParam param = new ToolParam();
        param.paramName="t";
        param.paramValue=3;
        requestModel.baseToolParams.add(param);
        requestModel.toCalibrateToolName = SimGruneJavaAdapter.NAME;
        requestModel.baseToolName = JPlagJavaAdapter.NAME;
        requestModel.workingDir = new File("testWorkingDir");
        requestModel.inputDirWithCalibrationCases = new File("testInputDir");
        return requestModel;
    }
}