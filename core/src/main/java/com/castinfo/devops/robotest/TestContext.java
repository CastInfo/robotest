/********************************************************************************
 * ROBOTEST
 * Copyright (C) 2018 CAST-INFO, S.A. www.cast-info.es
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package com.castinfo.devops.robotest;

import com.castinfo.devops.robotest.annot.RobotestCase;
import com.castinfo.devops.robotest.annot.RobotestStep;
import com.castinfo.devops.robotest.annot.RobotestSuite;

/**
 * Test context object properties.
 *
 */
public class TestContext {

    private RobotestStep stepAnnot;

    private RobotestCase caseAnnot;

    private RobotestSuite suiteAnnot;

    /**
     * Getter step annotation.
     *
     * WARNING! In TestCase context ALWAYS null. Only have value y pageObject context.
     *
     * @return the stepAnnot
     */
    public RobotestStep getStepAnnot() {
        return stepAnnot;
    }

    /**
     * Setter step annotation.
     *
     * @param stepAnnot
     *            the stepAnnot to set
     */
    public void setStepAnnot(final RobotestStep stepAnnot) {
        this.stepAnnot = stepAnnot;
    }

    /**
     * Getter case annotation.
     *
     * @return the caseAnnot
     */
    public RobotestCase getCaseAnnot() {
        return caseAnnot;
    }

    /**
     * Setter case annotation.
     *
     * @param caseAnnot
     *            the caseAnnot to set
     */
    public void setCaseAnnot(final RobotestCase caseAnnot) {
        this.caseAnnot = caseAnnot;
    }

    /**
     * Getter suite annotation.
     *
     * @return the suiteAnnot
     */
    public RobotestSuite getSuiteAnnot() {
        return suiteAnnot;
    }

    /**
     * Setter suite annotation.
     *
     * @param suiteAnnot
     *            the suiteAnnot to set
     */
    public void setSuiteAnnot(final RobotestSuite suiteAnnot) {
        this.suiteAnnot = suiteAnnot;
    }

    /**
     * Internal suite ctx retrive.
     *
     * @return SuiteContext
     * @throws RobotestException
     *             if not suite initied.
     */
    public SuiteContext getSuiteContext() throws RobotestException {
        SuiteContext sCtx = RobotestExecutionContext.getSuite(getSuiteAnnot());
        if (null == sCtx) {
            throw new RobotestException("SUITE NOT INITIED, REVISE CONFIGURATION PARAMS");
        }
        return sCtx;
    }
}
