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
package com.castinfo.devops.robotest.junit;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.castinfo.devops.robotest.RobotestExecutionContext;

/**
 * Implements JUnit test execution listener. Creates and destroy context for Suite and Case Robotest test executions.
 *
 */
public class JUnitCaseListener extends RunListener {

    private static final Logger LOG = LoggerFactory.getLogger(JUnitCaseListener.class);

    private Class<?> suiteClazz;

    /**
     * Setter of suiteClazz.
     *
     * @param klass
     *            the class of suite
     */
    public void setClazz(final Class<?> klass) {
        suiteClazz = klass;
    }

    /**
     * Getter of suiteClazz.
     *
     * @return suiteClazz
     */
    public Class<?> getClazz() {
        return suiteClazz;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.junit.runner.notification.RunListener#testRunStarted(org.junit.runner.Description)
     */
    @Override
    public void testRunStarted(final Description description) throws Exception {
        for (Description descriptors : description.getChildren()) {
            if (description.getTestClass().equals(getClazz())) {
                RobotestExecutionContext.buildSuite(getClazz(), descriptors.getMethodName());
            }
        }
        super.testRunStarted(description);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.junit.runner.notification.RunListener#testRunFinished(org.junit.runner.Result)
     */
    @Override
    public void testRunFinished(final Result result) throws Exception {
        try {
            RobotestExecutionContext.endSuite(getClazz());
        } catch (Exception e) {
            LOG.error("ERROR END SUITE", e);
        }
        RobotestExecutionContext.forceStopLostResources();
        super.testRunFinished(result);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.junit.runner.notification.RunListener#testStarted(org.junit.runner.Description)
     */
    @Override
    public void testStarted(final Description description) throws Exception {
        if (description.getTestClass().equals(getClazz())) {
            RobotestExecutionContext.buildCaseByMethod(getClazz(), description.getMethodName());
        }
        super.testStarted(description);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.junit.runner.notification.RunListener#testFinished(org.junit.runner.Description)
     */
    @Override
    public void testFinished(final Description description) throws Exception {
        if (description.getTestClass().equals(getClazz())) {
            RobotestExecutionContext.endCaseByMethod(getClazz(), description.getMethodName());
        }
        super.testFinished(description);
    }

}
