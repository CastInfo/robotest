package com.castinfo.devops.robotest.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.castinfo.devops.robotest.RobotestException;
import com.castinfo.devops.robotest.StepStatus;
import com.castinfo.devops.robotest.annot.RobotestCase;
import com.castinfo.devops.robotest.annot.RobotestConfig;
import com.castinfo.devops.robotest.annot.RobotestStep;
import com.castinfo.devops.robotest.annot.RobotestSuite;
import com.castinfo.devops.robotest.config.DockerConfig;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReporterTest {

    @Test
    public void testSuiteListReport() throws IOException, RobotestException {
        File suiteListFile = File.createTempFile("test", "json");
        suiteListFile.deleteOnExit();
        SuitesListReport suiteListReport = new SuitesListReport(suiteListFile);
        suiteListReport.appendToSuiteListReport("report-file-1.json");
        suiteListReport.appendToSuiteListReport("report-file-1.json");
        Assert.assertTrue(suiteListReport.getSuiteListReportContent().getSuites().size() == 2);
    }

    @Test
    public void testSuiteReport() throws IOException, RobotestException {
        File reportFile = File.createTempFile("test", "json");
        reportFile.deleteOnExit();
        JsonFactory jfactory = new JsonFactory();
        List<ConfigEntry> cfg = new ArrayList<>();
        cfg.add(new ConfigEntry("TEST_CFG_KEY",
                                "TEST_CFG_VALUE"));
        cfg.get(0).setId("TEST_CFG_KEY");
        cfg.get(0).setValue("TEST_CFG_VALUE");
        RobotestConfig cfgAnnot = Mockito.mock(RobotestConfig.class);
        Mockito.when(cfgAnnot.key()).thenReturn("CFG_KEY");
        Mockito.when(cfgAnnot.type()).thenReturn(String.class);
        Mockito.when(cfgAnnot.resource()).thenReturn("CFG_RESOURCE");
        RobotestSuite suiteAnnot = Mockito.mock(RobotestSuite.class);
        Mockito.when(suiteAnnot.tag()).thenReturn("SUITE_TAG");
        Mockito.when(suiteAnnot.description()).thenReturn("SUITE_DESC");
        Mockito.when(suiteAnnot.configElements()).thenReturn(new RobotestConfig[] { cfgAnnot });
        RobotestCase caseAnnot = Mockito.mock(RobotestCase.class);
        Mockito.when(caseAnnot.tag()).thenReturn("CASE_TAG");
        Mockito.when(caseAnnot.description()).thenReturn("CASE_DESC");
        Mockito.when(caseAnnot.configElements()).thenReturn(new RobotestConfig[] { cfgAnnot });
        RobotestStep stepAnnot = Mockito.mock(RobotestStep.class);
        Mockito.when(stepAnnot.tag()).thenReturn("STEP_TAG");
        Mockito.when(stepAnnot.description()).thenReturn("STEP_DESC");
        Mockito.when(stepAnnot.capturePageSourceAtEndStep()).thenReturn(true);
        Mockito.when(stepAnnot.captureConsoleErrorLogsAtEndStep()).thenReturn(true);
        Mockito.when(stepAnnot.captureScreenShootAtEndStep()).thenReturn(true);
        try (JsonGenerator jGenerator = jfactory.createGenerator(reportFile, JsonEncoding.UTF8)) {
            SuiteReport sr = new SuiteReport(0,
                                             reportFile);
            sr.initSuite(suiteAnnot.tag(), suiteAnnot.description(), 1, cfg);
            ValidationEntry val = ValidationEntry.buildError().withMessage("VALIDATION_ENTRY");
            sr.addSuiteValidationEntry(val);
            sr.initCase(caseAnnot.tag(), caseAnnot.description(), 3, cfg);
            sr.addCaseValidationEntry(caseAnnot.tag(), val);
            sr.initStep(caseAnnot.tag(), stepAnnot.tag(), stepAnnot.description(), cfg, 5);
            sr.addStepValidationEntry(caseAnnot.tag(), stepAnnot.tag(), val);
            sr.endStep(caseAnnot.tag(), stepAnnot.tag(), StepStatus.INFO, 6);
            sr.endCase(caseAnnot.tag(), 4);
            sr.endSuite(2);
        } finally {
            final ObjectMapper mapper = new ObjectMapper();
            JsonNode cfgReaded = mapper.readTree(reportFile);
            Assert.assertTrue(cfgReaded.size() == 8);
            Assert.assertTrue(cfgReaded.isObject());
            Assert.assertTrue(cfgReaded.get("order").asInt() == 0);
            Assert.assertTrue(cfgReaded.get("suite").asText().equals("SUITE_TAG"));
            Assert.assertTrue(cfgReaded.get("description").asText().equals("SUITE_DESC"));
            Assert.assertTrue(cfgReaded.get("initMillis").asInt() == 1);
            Assert.assertTrue(cfgReaded.get("endMillis").asInt() == 2);
            Assert.assertTrue(cfgReaded.get("config").get(0).get("TEST_CFG_KEY").asText().equals("TEST_CFG_VALUE"));
            Assert.assertTrue(cfgReaded.get("suiteOutCaseErrors").get(0).get("status").asText().equals("ERROR"));
            Assert.assertTrue(cfgReaded.get("suiteOutCaseErrors")
                                       .get(0)
                                       .get("resource")
                                       .get(0)
                                       .asText()
                                       .contains("VALIDATION_ENTRY"));
            Assert.assertTrue(cfgReaded.get("cases").get(0).get("case").asText().equals("CASE_TAG"));
            Assert.assertTrue(cfgReaded.get("cases").get(0).get("description").asText().equals("CASE_DESC"));
            Assert.assertTrue(cfgReaded.get("cases").get(0).get("initMillis").asInt() == 3);
            Assert.assertTrue(cfgReaded.get("cases").get(0).get("endMillis").asInt() == 4);
            Assert.assertTrue(cfgReaded.get("cases")
                                       .get(0)
                                       .get("steps")
                                       .get(0)
                                       .get("step")
                                       .asText()
                                       .equals("STEP_TAG"));
            Assert.assertTrue(cfgReaded.get("cases")
                                       .get(0)
                                       .get("steps")
                                       .get(0)
                                       .get("description")
                                       .asText()
                                       .equals("STEP_DESC"));
            Assert.assertTrue(cfgReaded.get("cases").get(0).get("steps").get(0).get("initMillis").asInt() == 5);
            Assert.assertTrue(cfgReaded.get("cases").get(0).get("steps").get(0).get("endMillis").asInt() == 6);
        }
    }

    @Test
    public void testConfigReport() throws IOException, RobotestException {
        File reportFile = File.createTempFile("test", "json");
        reportFile.deleteOnExit();
        JsonFactory jfactory = new JsonFactory();
        List<ConfigEntry> cfg = new ArrayList<>();
        cfg.add(new ConfigEntry("TEST_CFG_KEY",
                                "TEST_CFG_VALUE"));
        cfg.get(0).setId("TEST_CFG_KEY");
        cfg.get(0).setValue("TEST_CFG_VALUE");
        Properties props = new Properties();
        props.setProperty("TEST_PROP_KEY", "TEST_PROP_VALUE");
        cfg.add(new ConfigEntry("TEST_CFG_PROP",
                                props));
        DockerConfig dcfg = new DockerConfig();
        dcfg.setHost("HOST_EXAMPLE");
        cfg.add(new ConfigEntry("TEST_CFG_JSON",
                                dcfg));
        try (JsonGenerator jGenerator = jfactory.createGenerator(reportFile, JsonEncoding.UTF8)) {
            jGenerator.writeStartObject();
            ConfigReport cfgR = new ConfigReport(cfg);
            cfgR.writeConfig(jGenerator, "TEST_CFG");
            jGenerator.writeEndObject();
        } finally {
            final ObjectMapper mapper = new ObjectMapper();
            JsonNode cfgReaded = mapper.readTree(reportFile);
            Assert.assertTrue(cfgReaded.size() == 1);
            Assert.assertTrue(cfgReaded.get("TEST_CFG").isArray());
            Assert.assertTrue(cfgReaded.get("TEST_CFG").size() == 3);
            Assert.assertTrue(cfgReaded.get("TEST_CFG").get(0).get("TEST_CFG_KEY").asText().equals("TEST_CFG_VALUE"));
            Assert.assertTrue(cfgReaded.get("TEST_CFG")
                                       .get(1)
                                       .get("TEST_CFG_PROP")
                                       .get("TEST_PROP_KEY")
                                       .asText()
                                       .equals("TEST_PROP_VALUE"));
            Assert.assertTrue(cfgReaded.get("TEST_CFG")
                                       .get(2)
                                       .get("TEST_CFG_JSON")
                                       .get("host")
                                       .asText()
                                       .equals("HOST_EXAMPLE"));
        }
    }

    @Test
    public void testStepAndValidationsReport() throws IOException, RobotestException {
        File reportFile = File.createTempFile("test", "json");
        reportFile.deleteOnExit();
        JsonFactory jfactory = new JsonFactory();
        List<ConfigEntry> cfg = new ArrayList<>();
        cfg.add(new ConfigEntry("TEST_CFG_KEY",
                                "TEST_CFG_VALUE"));
        RobotestStep stepAnnot = Mockito.mock(RobotestStep.class);
        Mockito.when(stepAnnot.tag()).thenReturn("STEP_TAG");
        Mockito.when(stepAnnot.description()).thenReturn("STEP_DESC");
        Mockito.when(stepAnnot.capturePageSourceAtEndStep()).thenReturn(true);
        Mockito.when(stepAnnot.captureConsoleErrorLogsAtEndStep()).thenReturn(true);
        Mockito.when(stepAnnot.captureScreenShootAtEndStep()).thenReturn(true);
        ValidationEntry val = null;
        try (JsonGenerator jGenerator = jfactory.createGenerator(reportFile, JsonEncoding.UTF8)) {
            StepReport sR = new StepReport(0,
                                           stepAnnot.tag(),
                                           stepAnnot.description(),
                                           cfg,
                                           1);
            val = ValidationEntry.buildDebug();
            val.setStatus(StepStatus.DEBUG);
            val.withType(ValidationType.TEXT);
            val.setValidationOrder(1);
            val.withMessage("VALIDATION_DEBUG_MESSAGE");
            sR.getAdditional().add(val);
            val = ValidationEntry.buildInfo();
            val.withCapture(reportFile);
            sR.getAdditional().add(val);
            val = ValidationEntry.buildWarning();
            val.withConsole("VALIDATION_LOG");
            sR.getAdditional().add(val);
            val = ValidationEntry.buildError();
            val.withException(new Exception());
            sR.getAdditional().add(val);
            val = ValidationEntry.buildCritical();
            val.withHtmlSource(reportFile);
            sR.getAdditional().add(val);
            val = ValidationEntry.buildDefect();
            val.withMessage("DEFECT_MESSAGE");
            sR.getAdditional().add(val);
            val = new ValidationEntry(StepStatus.DEBUG);
            val.withMessage("VALIDATION_DEBUG_MESSAGE");
            sR.getAdditional().add(val);
            sR.endStep(StepStatus.INFO, 2);
            sR.writeStep(jGenerator);
        } finally {
            final ObjectMapper mapper = new ObjectMapper();
            JsonNode cfgReaded = mapper.readTree(reportFile);
            Assert.assertTrue(cfgReaded.size() == 8);
            Assert.assertTrue(cfgReaded.isObject());
            Assert.assertTrue(cfgReaded.get("order").asInt() == 0);
            Assert.assertTrue(cfgReaded.get("step").asText().equals("STEP_TAG"));
            Assert.assertTrue(cfgReaded.get("description").asText().equals("STEP_DESC"));
            Assert.assertTrue(cfgReaded.get("initMillis").asInt() == 1);
            Assert.assertTrue(cfgReaded.get("endMillis").asInt() == 2);
            Assert.assertTrue(cfgReaded.get("status").asText().equals("INFO"));
            Assert.assertTrue(cfgReaded.get("config").get(0).get("TEST_CFG_KEY").asText().equals("TEST_CFG_VALUE"));
            Assert.assertTrue(cfgReaded.get("validations").get(0).get("status").asText().equals("DEBUG"));
            Assert.assertTrue(cfgReaded.get("validations").size() == 7);
            Assert.assertTrue(cfgReaded.get("validations")
                                       .get(0)
                                       .get("type")
                                       .asText()
                                       .equals(ValidationType.TEXT.name()));
            Assert.assertTrue(cfgReaded.get("validations").get(0).get("order").asInt() == 1);
            Assert.assertTrue(cfgReaded.get("validations")
                                       .get(0)
                                       .get("resource")
                                       .get(0)
                                       .asText()
                                       .equals("VALIDATION_DEBUG_MESSAGE"));
            Assert.assertTrue(cfgReaded.get("validations").get(1).get("status").asText().equals("INFO"));
            Assert.assertTrue(cfgReaded.get("validations")
                                       .get(1)
                                       .get("type")
                                       .asText()
                                       .equals(ValidationType.SCREENSHOT.name()));
            Assert.assertTrue(cfgReaded.get("validations").get(1).get("resource").get(0).asText().trim().length() > 0);
            Assert.assertTrue(cfgReaded.get("validations").get(2).get("status").asText().equals("WARNING"));
            Assert.assertTrue(cfgReaded.get("validations")
                                       .get(2)
                                       .get("type")
                                       .asText()
                                       .equals(ValidationType.CONSOLE.name()));
            Assert.assertTrue(cfgReaded.get("validations")
                                       .get(2)
                                       .get("resource")
                                       .get(0)
                                       .asText()
                                       .equals("VALIDATION_LOG"));
            Assert.assertTrue(cfgReaded.get("validations").get(3).get("status").asText().equals("ERROR"));
            Assert.assertTrue(cfgReaded.get("validations")
                                       .get(3)
                                       .get("type")
                                       .asText()
                                       .equals(ValidationType.TEXT.name()));
            Assert.assertTrue(cfgReaded.get("validations")
                                       .get(3)
                                       .get("resource")
                                       .get(0)
                                       .asText()
                                       .contains("No message"));
            Assert.assertTrue(cfgReaded.get("validations").get(4).get("status").asText().equals("CRITICAL"));
            Assert.assertTrue(cfgReaded.get("validations")
                                       .get(4)
                                       .get("type")
                                       .asText()
                                       .equals(ValidationType.HTML.name()));
            Assert.assertTrue(cfgReaded.get("validations").get(4).get("resource").get(0).asText().trim().length() > 0);

        }

    }

    @Test
    public void testValidationsExceptions() throws IOException, RobotestException {
        File reportFile = File.createTempFile("test", "json");
        reportFile.deleteOnExit();
        ValidationEntry val = ValidationEntry.buildDebug();
        val.withException(new Exception("TEST_EXCEPTION"));
        val.getResource().get(0).contains("TEST_EXCEPTION");
        val.withException(new NullPointerException());
        val.getResource().get(1).contains("NullPointerException");
        Exception wt = new Exception("TEST_EXCEPTION");
        wt.setStackTrace(new StackTraceElement[] {});
        val.withException(wt);
        try {
            val.withCapture(reportFile);
            Assert.fail("RobotestException expected, no change of validation type is posible");
        } catch (RobotestException e) {
            // ok
        }
        try {
            val.withHtmlSource(reportFile);
            Assert.fail("RobotestException expected, no change of validation type is posible");
        } catch (RobotestException e) {
            // ok
        }

        val = ValidationEntry.buildDebug();
        val.withCapture(reportFile);
        try {
            val.withMessage("TEST_MESSAGE");
            Assert.fail("RobotestException expected, no change of validation type is posible");
        } catch (RobotestException e) {
            // ok
        }
        try {
            val.withConsole("TEST_MESSAGE");
            Assert.fail("RobotestException expected, no change of validation type is posible");
        } catch (RobotestException e) {
            // ok
        }
    }

    @Test
    public void forzeIOExceptions() throws IOException, RobotestException {
        List<ConfigEntry> cfg = new ArrayList<>();
        cfg.add(new ConfigEntry("TEST_CFG_KEY",
                                "TEST_CFG_VALUE"));
        try (JsonGenerator jGenerator = Mockito.mock(JsonGenerator.class)) {
            Mockito.doThrow(new IOException("CRASH!")).when(jGenerator).writeStartArray();
            ConfigReport cfgR = new ConfigReport(cfg);
            cfgR.writeConfig(jGenerator, "TEST_CFG");
            Assert.fail("IO Must fail!");
        } catch (RobotestException e) {
            // ok
        }
        try (JsonGenerator jGenerator = Mockito.mock(JsonGenerator.class)) {
            Mockito.doThrow(new IOException("CRASH!")).when(jGenerator).writeStartObject();
            ValidationEntry val = ValidationEntry.buildCritical();
            val.writeValidation(jGenerator);
            Assert.fail("IO Must fail!");
        } catch (RobotestException e) {
            // ok
        }
        try (JsonGenerator jGenerator = Mockito.mock(JsonGenerator.class)) {
            Mockito.doThrow(new IOException("CRASH!")).when(jGenerator).writeStartObject();
            StepReport sR = new StepReport(0,
                                           "ID",
                                           "DESC",
                                           cfg,
                                           0);
            sR.writeStep(jGenerator);
            Assert.fail("IO Must fail!");
        } catch (RobotestException e) {
            // ok
        }
        try (JsonGenerator jGenerator = Mockito.mock(JsonGenerator.class)) {
            Mockito.doThrow(new IOException("CRASH!")).when(jGenerator).writeStartObject();
            CaseReport cR = new CaseReport(0,
                                           "ID",
                                           "DESC",
                                           0,
                                           cfg);
            cR.writeCase(jGenerator);
            Assert.fail("IO Must fail!");
        } catch (RobotestException e) {
            // ok
        }
        File reportFile = File.createTempFile("test", "json");
        reportFile.deleteOnExit();
        SuiteReport sR = new SuiteReport(0,
                                         reportFile);
        sR.setjGenerator(Mockito.mock(JsonGenerator.class));
        Mockito.doThrow(new IOException("CRASH!")).when(sR.getjGenerator()).writeStartObject();
        Mockito.doThrow(new IOException("CRASH!")).when(sR.getjGenerator()).writeStartArray();
        try {
            sR.initSuite("ID", "DESC", 0, cfg);
            Assert.fail("IO Must fail!");
        } catch (RobotestException e) {
            // ok
        }
        sR.initCase("ID", "DESC", 0, cfg);
        try {
            sR.endCase("ID", 0);
            Assert.fail("IO Must fail!");
        } catch (RobotestException e) {
            // ok
        }
        try {
            sR.endSuite(0);
            Assert.fail("IO Must fail!");
        } catch (RobotestException e) {
            // ok
        }
    }
}
