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
package com.castinfo.devops.robotest.testng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.TestNGException;

import com.castinfo.devops.robotest.RobotestException;
import com.castinfo.devops.robotest.RobotestExecutionContext;

/**
 * Implements TestNG test execution listener. Creates and destroy context for Suite and Case Robotest test executions.
 *
 */
public class TestNGCaseListener implements IInvokedMethodListener, ITestListener {

    private static final Logger LOG = LoggerFactory.getLogger(TestNGCaseListener.class);

    /*
     * (non-Javadoc)
     *
     * @see org.testng.IInvokedMethodListener#afterInvocation(org.testng.IInvokedMethod, org.testng.ITestResult)
     */
    @Override
    public void afterInvocation(final IInvokedMethod ctx, final ITestResult arg1) {
        try {
            RobotestExecutionContext.endCaseByMethod(ctx.getTestMethod().getInstance().getClass(),
                                                     ctx.getTestMethod().getMethodName());
        } catch (RobotestException e) {
            throw new TestNGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.testng.IInvokedMethodListener#beforeInvocation(org.testng.IInvokedMethod, org.testng.ITestResult)
     */
    @Override
    public void beforeInvocation(final IInvokedMethod ctx, final ITestResult arg1) {
        try {
            RobotestExecutionContext.buildCaseByMethod(ctx.getTestMethod().getInstance().getClass(),
                                                       ctx.getTestMethod().getMethodName());
        } catch (RobotestException e) {
            throw new TestNGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.testng.ITestListener#onFinish(org.testng.ITestContext)
     */
    @Override
    public void onFinish(final ITestContext ctx) {
        try {
            if (null != ctx.getAllTestMethods()) {
                Class<?> testClazz = null;
                for (int i = 0; i < ctx.getAllTestMethods().length; i++) {
                    testClazz = ctx.getAllTestMethods()[i].getTestClass().getRealClass();
                    try {
                        RobotestExecutionContext.endSuite(testClazz);
                    } catch (Exception e) {
                        LOG.error("ERROR END SUITE", e);
                    }
                }
            }
        } finally {
            RobotestExecutionContext.forceStopLostResources();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.testng.ITestListener#onStart(org.testng.ITestContext)
     */
    @Override
    public void onStart(final ITestContext ctx) {
        if (null != ctx.getAllTestMethods()) {
            for (int i = 0; i < ctx.getAllTestMethods().length; i++) {
                try {
                    RobotestExecutionContext.buildSuite(ctx.getAllTestMethods()[i].getTestClass().getRealClass(),
                                                        ctx.getAllTestMethods()[i].getMethodName());
                } catch (RobotestException e) {
                    throw new TestNGException(e);
                }
            }
        }

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(final ITestResult arg0) {
        // not need to implement
    }

    @Override
    public void onTestFailure(final ITestResult arg0) {
        // not need to implement

    }

    @Override
    public void onTestSkipped(final ITestResult arg0) {
        // not need to implement

    }

    @Override
    public void onTestStart(final ITestResult arg0) {
        // not need to implement

    }

    @Override
    public void onTestSuccess(final ITestResult arg0) {
        // not need to implement
    }

}
