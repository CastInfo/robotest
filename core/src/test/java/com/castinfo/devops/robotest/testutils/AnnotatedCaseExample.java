package com.castinfo.devops.robotest.testutils;

import com.castinfo.devops.robotest.RobotestException;
import com.castinfo.devops.robotest.TestCase;
import com.castinfo.devops.robotest.annot.RobotestCase;
import com.castinfo.devops.robotest.annot.RobotestSuite;

@RobotestSuite(tag = "SUITE_001", description = "SUITE DESC")
public class AnnotatedCaseExample extends TestCase {

    private AnnotatedPageObjectExample pageObject = null;

    /**
     * Getter method for pageObject.
     *
     * @return the pageObject
     */
    public AnnotatedPageObjectExample getPageObject() {
        return pageObject;
    }

    /**
     * Setter method for the pageObject.
     *
     * @param pageObject
     *            the pageObject to set
     */
    public void setPageObject(final AnnotatedPageObjectExample pageObject) {
        this.pageObject = pageObject;
    }

    @RobotestCase(tag = "CASE_001", description = "CASE DESC")
    public void caseExample() throws RobotestException {
        pageObject = this.buildPageObject(AnnotatedPageObjectExample.class);
    }

    @RobotestCase(tag = "CASE_005", description = "CASE DESC")
    public void caseExample2() throws RobotestException {
        this.buildPageObject(AnnotatedPageObjectExampleErrors.class);
    }

    @RobotestCase(tag = "CASE_001", description = "CASE DESC")
    public void caseExample3() throws RobotestException {
        this.buildPageObject(AnnotatedPageObjectExampleErrors2.class);
    }

}
