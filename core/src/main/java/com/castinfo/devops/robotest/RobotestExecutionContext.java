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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.castinfo.devops.robotest.annot.RobotestCase;
import com.castinfo.devops.robotest.annot.RobotestSuite;
import com.castinfo.devops.robotest.config.DockerConfig;
import com.castinfo.devops.robotest.docker.DockerFarmBuilder;
import com.castinfo.devops.robotest.report.ValidationEntry;

/**
 * This class brings acces to execution context of ROBOTEST Suites and Cases, buildin and stoping all that objects
 * needed and make posible to retrive this load objects internally throught Suite - Case - Steps execution.
 *
 * Another responsability is doclet validations of Suite an Case user test implementations.
 *
 * This class is Thread Safe.
 *
 */
public final class RobotestExecutionContext {

    /**
     * Suite, Case and Step doclet tag attribute must acomplish this pattern.
     */
    public static final String TAG_PATTERN = "[a-zA-Z_0-9]+";

    private static final Logger LOG = LoggerFactory.getLogger(RobotestExecutionContext.class);
    private static final Map<RobotestSuite, SuiteContext> SUITES = new HashMap<>();
    private static final Map<String, Class<?>> SUITES_TAG_CLASSES = new HashMap<>();
    private static final Map<String, String> CASE_TAG_METHODS = new HashMap<>();
    private static final Map<RobotestSuite, List<RobotestCase>> SUITE_CASES = new HashMap<>();
    private static final Map<RobotestSuite, Map<RobotestCase, DockerConfig>> BROWSER_DOCKERS = new HashMap<>();
    private static DockerFarmBuilder dockerFarmClient = null;
    private static Object endSuiteBlocker = new Object();
    private static final AtomicInteger SUITE_ORDER = new AtomicInteger(-1);

    /**
     * Utility class avoid object creation.
     */
    private RobotestExecutionContext() {
        super();
    }

    /**
     * Get Suite througt his annotation.
     *
     * @param suiteAnnot
     *            Suite annot.
     * @return The suite ctx.
     */
    public static SuiteContext getSuite(final RobotestSuite suiteAnnot) {
        return SUITES.get(suiteAnnot);
    }

    /**
     * Get map of all under execution suites.
     *
     * @return map of RobotestSuite annots of SuiteContext
     */
    protected static Map<RobotestSuite, SuiteContext> getSuites() {
        return SUITES;
    }

    /**
     * Get map of all browsers under execution suites.
     *
     * @return map of RobotestSuite annots of Map of RobotestCase of DockerConfig
     */
    protected static Map<RobotestSuite, Map<RobotestCase, DockerConfig>> getBrowserDockers() {
        return BROWSER_DOCKERS;
    }

    /**
     * Inits Docker farm.
     *
     * @param dockerConfig
     *            config docker
     * @throws RobotestException
     *             connection docker errors.
     */
    protected static synchronized void initDockerFarmBuilder(final DockerConfig dockerConfig) throws RobotestException {
        if (null == dockerFarmClient) {
            dockerFarmClient = new DockerFarmBuilder(dockerConfig);
            dockerFarmClient.connectDockerClient();
        }
    }

    /**
     * Get Docker farm.
     *
     * @return Docker farm builder
     * @throws RobotestException
     *             if not initied.
     */
    protected static DockerFarmBuilder getDockerFarmBuilder() throws RobotestException {
        if (null == dockerFarmClient) {
            throw new RobotestException("NO DOCKER FARM INITIED, REVISE CONFIG OR CONNECTION TO DOCKER HOST");
        }
        return dockerFarmClient;
    }

    /**
     * Is invoked by TestNG and JUnit execution testing lyfe cicle listeners at test start, to build internal resources
     * with configuation, for example, reporting system and Selenium Web Driver in Docker. Applyed validations are: -
     * Case tag must be unique. - Suite tag must be unique. - Tag patter must acomplish TAG_PATTERN.
     *
     * @param suiteClazz
     *            Suite class.
     * @param methodName
     *            Case method name.
     * @throws RobotestException
     *             error in validations and initiatin resources.
     */
    public static void buildSuite(final Class<?> suiteClazz, final String methodName) throws RobotestException {
        RobotestSuite suiteAnnot = getSuiteAnnotation(suiteClazz);
        RobotestCase caseAnnot = getCaseAnnotationByMethod(suiteClazz, methodName);
        SuiteContext suite = null;
        if (CASE_TAG_METHODS.containsKey(caseAnnot.tag())
                && !(suiteClazz.getName() + "." + methodName).equals(CASE_TAG_METHODS.get(caseAnnot.tag()))) {
            throw new RobotestException("@RobotestCase TAG MUST BE UNIQUE: " + suiteAnnot.tag() + " IS DUPLICATED IN: "
                    + suiteClazz.getName() + "." + methodName + " AND " + CASE_TAG_METHODS.get(caseAnnot.tag()));
        }
        if (SUITES.containsKey(suiteAnnot)) {
            if (!suiteClazz.equals(SUITES_TAG_CLASSES.get(suiteAnnot.tag()))) {
                throw new RobotestException("@RobotestSuite TAG MUST BE UNIQUE: " + suiteAnnot.tag()
                        + " IS DUPLICATED IN: " + suiteClazz.getName() + " AND "
                        + SUITES_TAG_CLASSES.get(suiteAnnot.tag()));
            }
            if (SUITE_CASES.get(suiteAnnot).contains(caseAnnot)) {
                throw new RobotestException("SAME @RobotestCase TAG IN METHOD " + methodName + " ALLREADY BUILD: "
                        + suiteAnnot.tag()
                        + " parallel exectution context of same test is not posible in real robotest execution, "
                        + "your unit test do end suite and case?");
            }
            SUITE_CASES.get(suiteAnnot).add(caseAnnot);
            LOG.info("REUSE SUITE: {} FOR {}", suiteAnnot.tag(), caseAnnot.tag());
        } else {
            LOG.info("STARTING SUITE: {} FOR {}", suiteAnnot.tag(), caseAnnot.tag());
            suite = new SuiteContext();
            suite.setSuiteAnnotation(suiteAnnot);
            if (SUITES.containsKey(suiteAnnot)) {
                throw new RobotestException("SAME @RobotestSuite TAG ALLREADY BUILD: " + suiteAnnot.tag()
                        + " parallel exectution context of same test is not posible in real robotest execution, "
                        + "your unit test do end suite and case?");
            }
            SUITES.put(suiteAnnot, suite);
            SUITE_CASES.put(suiteAnnot, new ArrayList<RobotestCase>());
            SUITE_CASES.get(suiteAnnot).add(caseAnnot);
            SUITES_TAG_CLASSES.put(suiteAnnot.tag(), suiteClazz);
            CASE_TAG_METHODS.put(caseAnnot.tag(), suiteClazz.getName() + "." + methodName);
            try {
                suite.initSuite(SUITE_ORDER.incrementAndGet());
            } catch (RobotestException e) {
                suite.putIntialitationSuiteError(ValidationEntry.buildCritical().withException(e));
                throw e;
            }
        }
    }

    /**
     * Stops all the docker containers associated to testCase annotation. Can only be called by JUnit/TestNG Listeners.
     *
     */
    public static void forceStopLostResources() {
        for (Map<RobotestCase, DockerConfig> caseSet : BROWSER_DOCKERS.values()) {
            for (DockerConfig docker : caseSet.values()) {
                try {
                    LOG.info("TRY FORCE STOP LOST DOCKER CONTAINER {}", docker.getIdContainer());
                    dockerFarmClient.stopNode(docker.getIdContainer());
                } catch (Exception e) {
                    LOG.error("ERROR FORCE STOP CONTAINER", e);
                }
            }
        }
        try {
            if (null != dockerFarmClient) {
                dockerFarmClient.destroyDockerClient();
            }
        } catch (RobotestException e) {
            LOG.error("DOCKER CLIENT QUIT ERROR", e);
        }
        for (SuiteContext sCtx : SUITES.values()) {
            sCtx.forzeSeleniumDriversDestroy();
        }

    }

    /**
     * End resources of suite context, invoked by TestNG and JUnit testing lyfe cicle listeners.
     *
     * @param clazz
     *            Suite class.
     * @throws RobotestException
     *             errors unloading resources.
     */
    public static void endSuite(final Class<?> clazz) throws RobotestException {
        RobotestSuite suiteAnnot = getSuiteAnnotation(clazz);
        synchronized (endSuiteBlocker) {
            if (SUITE_CASES.containsKey(suiteAnnot) && SUITE_CASES.get(suiteAnnot).isEmpty()) {
                LOG.info("ENDING SUITE: {}", suiteAnnot.tag());
                try {
                    getSuite(suiteAnnot).endSuite();
                } finally {
                    SUITES.remove(suiteAnnot);
                    SUITE_CASES.remove(suiteAnnot);
                }
            }
        }
    }

    /**
     * Retrieve Suite Class RobotestSuite Doclet. Validate Doclet exists and tag attibute conform TAG_PATTERN.
     *
     * @param klass
     *            the class.
     * @return RobotestSuite doclet.
     * @throws RobotestException
     *             errors in annotation.
     */
    public static RobotestSuite getSuiteAnnotation(final Class<?> klass) throws RobotestException {
        RobotestSuite suiteAnnot = klass.getAnnotation(RobotestSuite.class);
        if (null == suiteAnnot) {
            throw new RobotestException("TEST CLASS MUST BE @RobotestSuite ANNOTATED!");
        }
        if (!Pattern.matches(TAG_PATTERN, suiteAnnot.tag())) {
            throw new RobotestException("TAG OF SUITE: " + klass.getName() + " MUST ACOMPLISH " + TAG_PATTERN
                    + " REGEX PATTERN");
        }
        return suiteAnnot;
    }

    /**
     * Is invoked by TestNG and JUnit execution testing lyfe cicle listeners at test start, to build internal resources
     * with configuation.
     *
     * @param clazz
     *            Suite Class.
     * @param methodName
     *            Case method name.
     * @throws RobotestException
     *             error in inicialization.
     */
    public static void buildCaseByMethod(final Class<?> clazz, final String methodName) throws RobotestException {
        RobotestCase caseAnnot = getCaseAnnotationByMethod(clazz, methodName);
        RobotestSuite suiteAnnot = getSuiteAnnotation(clazz);
        LOG.info("STARTING CASE: {} OF {} FOR {}",
                 caseAnnot.tag(),
                 suiteAnnot.tag(),
                 clazz.getName() + "." + methodName);
        SuiteContext suite = getSuite(suiteAnnot);
        try {
            suite.initTestCase(suiteAnnot, caseAnnot);
        } catch (RobotestException e) {
            suite.putCaseError(caseAnnot, ValidationEntry.buildCritical().withException(e));
            throw e;
        }
    }

    /**
     * Free case execution resources.
     *
     * @param clazz
     *            Suite class.
     * @param methodName
     *            Case method name.
     * @throws RobotestException
     *             free case resource errors.
     */
    public static void endCaseByMethod(final Class<?> clazz, final String methodName) throws RobotestException {
        RobotestCase caseAnnot = getCaseAnnotationByMethod(clazz, methodName);
        RobotestSuite suiteAnnot = getSuiteAnnotation(clazz);
        LOG.info("ENDING CASE: {} OF {} FOR {}", caseAnnot.tag(), suiteAnnot.tag(), clazz.getName() + "." + methodName);
        SuiteContext suite = getSuite(suiteAnnot);
        try {
            suite.endTestCase(caseAnnot);
        } finally {
            SUITE_CASES.get(suiteAnnot).remove(caseAnnot);
        }
    }

    /**
     * Retrive case annotation information by case method name, validate doclet tag attribute acomplish TAG_PATTERN.
     *
     * @param klass
     *            Suite class.
     * @param methodName
     *            case method name.
     * @return RobotestCase annot.
     * @throws RobotestException
     *             errors in case retrive and validations.
     */
    public static RobotestCase getCaseAnnotationByMethod(final Class<?> klass,
                                                         final String methodName) throws RobotestException {
        RobotestCase annotCase = null;
        try {
            for (Method method : klass.getMethods()) {
                if (method.getName().equals(methodName)) {
                    if (null != annotCase) {
                        throw new RobotestException("TEST METHOD NAME MUST BE UNIQUE! " + methodName);
                    }
                    annotCase = method.getAnnotation(RobotestCase.class);
                }
            }
            if (null == annotCase) {
                throw new RobotestException("TEST METHOD NOT EXIST, NOT ACCESIBLE AND NOT @RobotestCase ANNOTATED!");
            }
            validateTag(klass, methodName, annotCase);
        } catch (SecurityException e) {
            throw new RobotestException("TEST METHOD NOT EXIST OR NOT ACCESIBLE: " + methodName,
                                        e);
        }
        return annotCase;
    }

    /**
     * Tag must acommplish [a-zA-Z_0-9]+ pattern.
     *
     * @param klass
     *            suite.
     * @param annotCase
     *            case
     * @throws RobotestException
     *             errors in pattern.
     */
    private static void validateTag(final Class<?> klass,
                                    final String methodName,
                                    final RobotestCase annotCase) throws RobotestException {
        if (!Pattern.matches(TAG_PATTERN, annotCase.tag())) {
            throw new RobotestException("TAG OF CASE: " + methodName + " OF CLASS " + klass.getName()
                    + " MUST ACOMPLISH " + TAG_PATTERN + " REGEX PATTERN");
        }
    }

    /**
     * Retrive case annotation information by tag Name, validate doclet tag attribute acomplish TAG_PATTERN.
     *
     * @param klass
     *            Suite class.
     * @param tagName
     *            case method name.
     * @return RobotestCase annot.
     * @throws RobotestException
     *             errors in case retrive and validations.
     */
    public static RobotestCase getCaseAnnotationByAnnotationTag(final Class<?> klass,
                                                                final String tagName) throws RobotestException {
        RobotestCase annotCase = null;
        try {
            RobotestCase annotCaseTmp = null;
            for (Method method : klass.getMethods()) {
                if (method.isAnnotationPresent(RobotestCase.class)) {
                    annotCaseTmp = method.getAnnotation(RobotestCase.class);
                    if (annotCaseTmp.tag().equals(tagName)) {
                        annotCase = annotCaseTmp;
                        validateTag(klass, method.getName(), annotCase);
                    }
                }
            }
            if (null == annotCase) {
                throw new RobotestException("THERE ARE NO TEST METHOD WITH @RobotestCase TAG: " + tagName);
            }
        } catch (SecurityException e) {
            throw new RobotestException("TEST METHOD MUST BE DECLARED PUBLIC!",
                                        e);
        }
        return annotCase;
    }

}
