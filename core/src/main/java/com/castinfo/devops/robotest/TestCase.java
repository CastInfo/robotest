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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.castinfo.devops.robotest.annot.RobotestCase;
import com.castinfo.devops.robotest.annot.RobotestStep;
import com.castinfo.devops.robotest.config.ConfigurationAccess;

import net.sf.cglib.proxy.Enhancer;

/**
 * Suite classes extends this class to build context of Suite, Case and Step PageObjects/PageFragments context.
 */
public class TestCase extends ConfigurationAccess {

    private static final Logger LOG = LoggerFactory.getLogger(TestCase.class);
    private static Map<String, String> pageFragmentTagClasses = new HashMap<>();

    /**
     * Default no params constructor.
     *
     */
    public TestCase() {
        try {
            setSuiteAnnot(RobotestExecutionContext.getSuiteAnnotation(this.getClass()));
        } catch (RobotestException e) {
            LOG.error("NO SUITE ANNOTATION FOUND, EXECUTION MAY FAIL", e);
        }
    }

    /**
     * Search the RobotestCase annotation tag invoker.
     *
     * @return Test tag of robotest case finded.
     * @throws RobotestException
     *             if no RobotestCase finded.
     */
    private RobotestCase searchInvokerCaseTag() throws RobotestException {
        RobotestCase testTagAnnot = null;
        StackTraceElement[] stes = new Throwable().getStackTrace();
        for (int is = 1; is < stes.length; is++) {
            StackTraceElement ste = stes[is];
            try {
                Method[] caseMethods = Class.forName(ste.getClassName()).getMethods();
                for (Method m : caseMethods) {
                    if (m.getName().equals(ste.getMethodName()) && m.isAnnotationPresent(RobotestCase.class)) {
                        testTagAnnot = m.getAnnotation(RobotestCase.class);
                        break;
                    }
                }
            } catch (NoClassDefFoundError | ClassNotFoundException | SecurityException e) {
                // ignore
            }
        }
        if (null == testTagAnnot) {
            throw new RobotestException("CAN'T BUILD PAGE FRAGMENTS OUT OF ROBOTEST ANNOTATED CASE");
        }
        return testTagAnnot;
    }

    /**
     * Search if class implements PageObject class or implementation of this.
     *
     * @param pageObject
     *            po.
     * @param <T>
     *            page object to be cast
     * @throws RobotestException
     *             if no PageObject class finded.
     */
    private <T> void isPageObject(final Class<T> pageObject) throws RobotestException {
        boolean isPageObject = false;
        Class<?> superClass = pageObject.getSuperclass();
        while (!isPageObject && null != superClass) {
            if (superClass.equals(PageObject.class)) {
                isPageObject = true;
            } else {
                superClass = superClass.getSuperclass();
            }
        }
        if (!isPageObject) {
            throw new RobotestException("CAN'T BUILD NO PAGEOBJECT CLASS: " + pageObject.getName());
        }
    }

    /**
     * Do validations of page fragment annotations
     *
     * @param pageObject
     *            page object
     * @param testTagAnnot
     *            case annotation
     * @param pfTagAnnot
     * @param m
     * @throws RobotestException
     */
    private void pageFragmentAnnotationValidations(final Class<?> pageObject,
                                                   final String testTagAnnot,
                                                   final String pfTagAnnot,
                                                   final Method m) throws RobotestException {
        if (!Pattern.matches(RobotestExecutionContext.TAG_PATTERN, pfTagAnnot)) {
            throw new RobotestException("TAG OF FRAGMENT: " + pageObject.getName() + "." + m.getName()
                    + " MUST ACOMPLISH " + RobotestExecutionContext.TAG_PATTERN + " REGEX PATTERN");
        }
        if (TestCase.pageFragmentTagClasses.containsKey(pfTagAnnot) && !(pageObject.getName() + "."
                + m.getName()).equals(TestCase.pageFragmentTagClasses.get(pfTagAnnot))) {
            throw new RobotestException("@RobotestStep TAG MUST BE UNIQUE: " + testTagAnnot + " IS DUPLICATED IN: "
                    + pageObject.getName() + "." + m.getName() + " AND "
                    + TestCase.pageFragmentTagClasses.get(pfTagAnnot));
        }
    }

    /**
     * You will use to Build WebFragment/PageObject context to execute. Validations applied are: - RobotestStep
     * annotation tag attribute will acomplish TAG_PATTERN. - RobotestStep tag must be unique in PageObject/PageObject.
     *
     * @param pageObject
     *            WebFragment
     * @param <T>
     *            page object to be cast
     * @return PageObject with complete context tooling.
     * @throws RobotestException
     *             errors and validations
     */
    public <T> T buildPageObject(final Class<T> pageObject) throws RobotestException {
        this.isPageObject(pageObject);
        setCaseAnnot(searchInvokerCaseTag());
        Method[] pageObjectMethods = pageObject.getMethods();
        String pfTagAnnot = null;
        for (Method m : pageObjectMethods) {
            if (m.isAnnotationPresent(RobotestStep.class)) {
                pfTagAnnot = m.getAnnotation(RobotestStep.class).tag();
                pageFragmentAnnotationValidations(pageObject, getCaseAnnot().tag(), pfTagAnnot, m);
                TestCase.pageFragmentTagClasses.put(pfTagAnnot, pageObject.getName() + "." + m.getName());
            }
        }
        return this.buildEnhacedPageObject(pageObject);
    }

    /**
     * Build WebFragment execution context interceptor, enhacing tooling and automate execution of reporting utilities.
     *
     * @param pageObject
     *            WebFragment class
     * @param <T>
     *            page object to be cast
     * @return PageObject the builded implemented interceptor enhaced.
     */
    @SuppressWarnings("unchecked")
    private <T> T buildEnhacedPageObject(final Class<T> pageObject) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(pageObject);
        enhancer.setCallback(new StepInterceptor());
        PageObject enhaced = (PageObject) enhancer.create();
        enhaced.setSuiteAnnot(getSuiteAnnot());
        enhaced.setCaseAnnot(getCaseAnnot());
        return (T) enhaced;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.castinfo.devops.robotest.config.ConfigurationAccess#getTestCfg(java.lang.String)
     */
    @Override
    public <T> T getTestCfg(final String configKey) throws RobotestException {
        SuiteContext sContext = getSuiteContext();
        T scopedConfig = this.getTestCfgInScope(sContext, getCaseAnnot(), configKey);
        if (null == scopedConfig) {
            scopedConfig = this.getTestCfgInScope(sContext, getSuiteAnnot(), configKey);
        }
        return scopedConfig;
    }

}
