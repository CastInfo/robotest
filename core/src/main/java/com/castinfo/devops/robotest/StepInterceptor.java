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

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

import com.castinfo.devops.robotest.annot.RobotestStep;
import com.castinfo.devops.robotest.report.ValidationEntry;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * This class implement interceptor of PageObject/PageObject to provide proxy for automate reporting purposes.
 *
 */
public class StepInterceptor implements MethodInterceptor {

    /*
     * (non-Javadoc)
     *
     * @see net.sf.cglib.proxy.MethodInterceptor#intercept(java.lang.Object, java.lang.reflect.Method,
     * java.lang.Object[], net.sf.cglib.proxy.MethodProxy)
     */
    @Override
    public Object intercept(final Object obj, final Method method, final Object[] args,
                            final MethodProxy proxy) throws Throwable {
        Object resultado = null;
        Throwable errorTest = null;
        StepStatus resultadoStatusStep = StepStatus.INFO;
        PageObject pageFragment = (PageObject) obj;
        RobotestStep stepAnnot = method.getAnnotation(RobotestStep.class);
        SuiteContext sCtx = atStartAnnotationActions(pageFragment, stepAnnot);
        try {
            resultado = proxy.invokeSuper(obj, args);
        } catch (Exception | AssertionError e) {
            errorTest = e;
        }
        if (null != stepAnnot && null != sCtx) {
            if (null != errorTest) {
                resultadoStatusStep = StepStatus.ERROR;
                sCtx.addAdditionalStepEntry(pageFragment,
                                            new ValidationEntry(resultadoStatusStep).withException(errorTest));
            }
            atEndAnnotationActions(pageFragment, sCtx, stepAnnot, resultadoStatusStep);
            sCtx.endStep(pageFragment, resultadoStatusStep, System.currentTimeMillis());
        } else {
            if (null != errorTest) {
                RobotestExecutionContext.getSuite(pageFragment.getSuiteAnnot())
                                        .putCaseError(pageFragment.getCaseAnnot(),
                                                      ValidationEntry.buildCritical().withException(errorTest));
            }
        }
        if (null != errorTest) {
            throw errorTest;
        }
        return resultado;
    }

    /**
     * Do start annotation actions.
     *
     * @param pageFragment
     *            page Object
     * @param stepAnnot
     *            step annot.
     * @return SuiteContext of execution.
     * @throws RobotestException
     */
    private SuiteContext atStartAnnotationActions(final PageObject pageFragment,
                                                  final RobotestStep stepAnnot) throws RobotestException {
        SuiteContext sCtx = null;
        if (null != stepAnnot) {
            pageFragment.setStepAnnot(stepAnnot);
            sCtx = RobotestExecutionContext.getSuite(pageFragment.getSuiteAnnot());
            sCtx.initStep(pageFragment, System.currentTimeMillis());
            if (stepAnnot.captureScreenShootAtStartStep()) {
                try {
                    File screnShoot = pageFragment.takeScreenShoot(stepAnnot.tag());
                    sCtx.addAdditionalStepEntry(pageFragment, ValidationEntry.buildInfo().withCapture(screnShoot));
                } catch (RobotestException e) {
                    ValidationEntry err = ValidationEntry.buildCritical();
                    err.withException(new RobotestException("IS NOT POSIBLE TO CAPTURE IMAGE SCRENSHOOT", e));
                    sCtx.addAdditionalStepEntry(pageFragment, err);
                }
            }
        }
        return sCtx;
    }

    /**
     * Do pagefragment at end step annotaded jobs: capture screenshots, pagesource and browser CONSOLE logs.
     *
     * @param pageFragment
     *            page fragment
     * @param sCtx
     *            suite context
     * @param stepAnnot
     *            step annotation
     * @param resultadoStatusStep
     *            step results
     * @throws RobotestException
     *             errors in reporting actions.
     */
    private void atEndAnnotationActions(final PageObject pageFragment, final SuiteContext sCtx,
                                        final RobotestStep stepAnnot,
                                        final StepStatus resultadoStatusStep) throws RobotestException {
        if (stepAnnot.capturePageSourceAtEndStep()) {
            try {
                File pageSrc = pageFragment.takePageSource(stepAnnot.tag());
                sCtx.addAdditionalStepEntry(pageFragment,
                                            new ValidationEntry(resultadoStatusStep).withHtmlSource(pageSrc));
            } catch (RobotestException e) {
                ValidationEntry err = ValidationEntry.buildCritical();
                err.withException(new RobotestException("IS NOT POSIBLE TO CAPTURE PAGE SOURCE", e));
                sCtx.addAdditionalStepEntry(pageFragment, err);
            }
        }
        if (stepAnnot.captureScreenShootAtEndStep()) {
            try {
                File screnShoot = pageFragment.takeScreenShoot(stepAnnot.tag());
                sCtx.addAdditionalStepEntry(pageFragment,
                                            new ValidationEntry(resultadoStatusStep).withCapture(screnShoot));
            } catch (RobotestException e) {
                ValidationEntry err = ValidationEntry.buildCritical();
                err.withException(new RobotestException("IS NOT POSIBLE TO CAPTURE IMAGE SCRENSHOOT", e));
                sCtx.addAdditionalStepEntry(pageFragment, err);
            }
        }
        if (stepAnnot.captureConsoleErrorLogsAtEndStep()) {
            try {
                List<ValidationEntry> logs = pageFragment.takeBrowserConsoleLogs();
                for (ValidationEntry log : logs) {
                    sCtx.addAdditionalStepEntry(pageFragment, log);
                }
            } catch (RobotestException e) {
                ValidationEntry err = ValidationEntry.buildCritical();
                err.withException(new RobotestException("IS NOT POSIBLE TO RETRIVE BROWSER CONSOLE LOGS", e));
                sCtx.addAdditionalStepEntry(pageFragment, err);
            }
        }
    }
}
