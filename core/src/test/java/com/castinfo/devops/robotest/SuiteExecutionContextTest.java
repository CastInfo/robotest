package com.castinfo.devops.robotest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.castinfo.devops.robotest.annot.RobotestCase;
import com.castinfo.devops.robotest.annot.RobotestSuite;
import com.castinfo.devops.robotest.selenium.SeleniumBrowser;
import com.castinfo.devops.robotest.testutils.AnnotatedCaseExample;
import com.castinfo.devops.robotest.testutils.AnnotatedCaseExampleError;
import com.castinfo.devops.robotest.testutils.AnnotatedCaseExampleError2;

@RobotestSuite(tag = "SuiteExecutionContextTest")
public class SuiteExecutionContextTest {

    @Before
    public void setup() {
        System.setProperty("ROBOTEST_REPORT_BASE",
                           System.getProperty("java.io.tmpdir"));
        System.setProperty("ROBOTEST_ENV",
                           "local");
        System.setProperty("ROBOTEST_BROWSER",
                           SeleniumBrowser.CHROME.name());
        System.setProperty("ROBOTEST_BROWSER_HEADLESS",
                           "true");
        System.setProperty("ROBOTEST_BROWSER_WIDTH",
                           "1024");
        System.setProperty("ROBOTEST_BROWSER_HEIGHT",
                           "768");
        System.setProperty("ROBOTEST_BROWSER_MAXIMIZED",
                           "true");
        System.setProperty("ROBOTEST_GENERAL_TIMEOUT",
                           "10000");
    }

    @After
    public void teardown() {
        System.setProperty("ROBOTEST_REPORT_BASE",
                           "");
        System.setProperty("ROBOTEST_ENV",
                           "");
        System.setProperty("ROBOTEST_BROWSER",
                           "");
        System.setProperty("ROBOTEST_GENERAL_TIMEOUT",
                           "");
    }

    @Test
    @RobotestCase(tag = "testExecCtx")
    public void testExecCtx() throws RobotestException {
        try {
            RobotestExecutionContext.getSuiteAnnotation(SuiteExecutionContextTest.class);
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("TEST CLASS MUST BE @RobotestSuite ANNOTATED"));
        }
        try {
            RobotestExecutionContext.getSuiteAnnotation(AnnotatedCaseExampleError2.class);
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("REGEX PATTERN"));
        }
        try {
            RobotestExecutionContext.getCaseAnnotationByMethod(SuiteExecutionContextTest.class,
                                                               "testExecCtx");
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("TEST METHOD NOT EXIST, NOT ACCESIBLE AND NOT @RobotestCase ANNOTATED"));
        }
        try {
            RobotestExecutionContext.getCaseAnnotationByMethod(SuiteExecutionContextTest.class,
                                                               "forzeNotFoundMethod");
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("TEST METHOD NOT EXIST, NOT ACCESIBLE AND NOT @RobotestCase ANNOTATED"));
        }
        try {
            RobotestExecutionContext.getCaseAnnotationByMethod(AnnotatedCaseExampleError2.class,
                                                               "caseExample2");
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("REGEX PATTERN"));
        }
        RobotestExecutionContext.getCaseAnnotationByAnnotationTag(AnnotatedCaseExample.class,
                                                                  "CASE_001");
        try {
            RobotestExecutionContext.getCaseAnnotationByAnnotationTag(AnnotatedCaseExample.class,
                                                                      "notExistMethod");
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("THERE ARE NO TEST METHOD WITH @RobotestCase TAG"));
        }
        RobotestExecutionContext.buildSuite(AnnotatedCaseExample.class,
                                            "caseExample");
        try {
            RobotestExecutionContext.buildSuite(AnnotatedCaseExampleError.class,
                                                "caseExample");
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("@RobotestCase TAG MUST BE UNIQUE"));
        }
        try {
            RobotestExecutionContext.buildSuite(AnnotatedCaseExampleError.class,
                                                "caseExample2");
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("@RobotestSuite TAG MUST BE UNIQUE"));
        }
        try {
            RobotestExecutionContext.buildCaseByMethod(AnnotatedCaseExample.class,
                                                       "caseExample");
        } catch (Exception e) {
            // ok
        }
        RobotestExecutionContext.endCaseByMethod(AnnotatedCaseExample.class,
                                                 "caseExample");
        RobotestExecutionContext.endSuite(AnnotatedCaseExample.class);
    }

}
