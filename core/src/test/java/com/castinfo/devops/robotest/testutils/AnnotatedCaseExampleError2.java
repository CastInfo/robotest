package com.castinfo.devops.robotest.testutils;

import com.castinfo.devops.robotest.RobotestException;
import com.castinfo.devops.robotest.TestCase;
import com.castinfo.devops.robotest.annot.RobotestCase;
import com.castinfo.devops.robotest.annot.RobotestSuite;

@RobotestSuite(tag = "SUITE 001", description = "SUITE DESC")
public class AnnotatedCaseExampleError2 extends TestCase {

    protected AnnotatedPageObjectExample pageObject = null;

    @RobotestCase(tag = "CASE_001", description = "CASE DESC")
    public void caseExample() throws RobotestException {
        pageObject = this.buildPageObject(AnnotatedPageObjectExample.class);
    }

    @RobotestCase(tag = "CASE 001", description = "CASE DESC")
    public void caseExample2() throws RobotestException {
        pageObject = this.buildPageObject(AnnotatedPageObjectExample.class);
    }

}
