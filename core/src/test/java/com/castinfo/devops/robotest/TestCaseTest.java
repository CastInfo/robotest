package com.castinfo.devops.robotest;

import org.junit.Assert;
import org.junit.Test;

import com.castinfo.devops.robotest.testutils.AnnotatedCaseExample;
import com.castinfo.devops.robotest.testutils.AnnotatedPageObjectExample;

public class TestCaseTest {

    @Test
    public void testBuidPageObject() {
        AnnotatedCaseExample testCase = new AnnotatedCaseExample();
        try {
            testCase.buildPageObject(TestCaseTest.class);
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("CAN'T BUILD NO PAGEOBJECT CLASS"));
        }
        try {
            testCase.buildPageObject(AnnotatedPageObjectExample.class);
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("CAN'T BUILD PAGE FRAGMENTS OUT OF ROBOTEST ANNOTATED CASE"));
        }
        try {
            testCase.caseExample();
        } catch (RobotestException e) {
            Assert.assertNotNull(testCase.getPageObject());
        }
        try {
            testCase.caseExample2();
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("@RobotestStep TAG MUST BE UNIQUE"));
        }
        try {
            testCase.caseExample3();
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("REGEX PATTERN"));
        }
    }
}
