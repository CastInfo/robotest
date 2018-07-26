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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.castinfo.devops.robotest.annot.RobotestCase;
import com.castinfo.devops.robotest.annot.RobotestSuite;
import com.castinfo.devops.robotest.config.DockerConfig;
import com.castinfo.devops.robotest.config.IRobotestConfiguration;
import com.castinfo.devops.robotest.config.RobotestBasicConfig;
import com.castinfo.devops.robotest.config.RobotestConfiguration;
import com.castinfo.devops.robotest.report.ConfigEntry;
import com.castinfo.devops.robotest.report.SuiteReport;
import com.castinfo.devops.robotest.report.ValidationEntry;
import com.castinfo.devops.robotest.selenium.SeleniumDriverFactory;

/**
 * This class contains context object needed for Robotest Suite execution, for example, Report, Selenium Web Driver and
 * Selenium associated Dockers.
 *
 */
public class SuiteContext {

    private static final Logger LOG = LoggerFactory.getLogger(SuiteContext.class);

    private SuiteReport reporter = null;
    private IRobotestConfiguration cfg = new RobotestConfiguration();
    private RobotestSuite suiteAnnotation = null;

    private Map<RobotestCase, WebDriver> caseDrivers = new HashMap<>();

    /**
     * Init configuration (basic and user), report and docker farm.
     *
     * @param order
     *            Suite execution order.
     *
     * @throws RobotestException
     *             errors in IO or timeout Docker Host.
     */
    public void initSuite(final int order) throws RobotestException {
        RobotestExecutionContext.getBrowserDockers().put(suiteAnnotation, new HashMap<>());
        cfg.loadBasic(suiteAnnotation);
        File reportFile = new File(cfg.getConfigBasic().getReportFilePath() + "/robotest-report-" + order + ".json");
        reporter = new SuiteReport(order,
                                   reportFile);
        try {
            cfg.loadAnnotationScopeConfig(suiteAnnotation.configElements(), suiteAnnotation);
            if (null != cfg.getConfigBasic().getDocker()
                    && StringUtils.isNotEmpty(cfg.getConfigBasic().getDocker().getHost())) {
                RobotestExecutionContext.initDockerFarmBuilder(cfg.getConfigBasic().getDocker());
            }
        } finally {
            List<ConfigEntry> cfgs = cfg.toReportConfigEntries(suiteAnnotation);
            cfgs.add(new ConfigEntry("ROBOTEST_CONFIG_BASIC",
                                     cfg.getConfigBasic()));
            reporter.initSuite(suiteAnnotation.tag(), suiteAnnotation.description(), System.currentTimeMillis(), cfgs);
        }
    }

    /**
     * Finish report and free docker farm.
     *
     * @throws RobotestException
     *             errors ending suite.
     */
    public void endSuite() throws RobotestException {
        try {
            reporter.endSuite(System.currentTimeMillis());
        } catch (RobotestException e) {
            throw new RobotestException("ROBOTEST END OF SUITE ERRORS",
                                        e);
        }
    }

    /**
     * Forze destroy all drivers when end robotest. Only can be called exectuion context to ensure free resources.
     *
     */
    protected void forzeSeleniumDriversDestroy() {
        if (null != caseDrivers) {
            for (WebDriver driver : caseDrivers.values()) {
                try {
                    driver.quit();
                } catch (Exception e) {
                    SuiteContext.LOG.error("ERROR STOPING SELENIUM DRIVER", e);
                }
            }
        }
    }

    /**
     * All cases have loaded Selenium Web Driver accesible with this method.
     *
     * @param caseAnnot
     *            case annotation.
     * @return WebDriver.
     */
    protected WebDriver getDriverByCase(final RobotestCase caseAnnot) {
        return caseDrivers.get(caseAnnot);
    }

    /**
     * Getter method for suite associated annotation.
     *
     * @return the suiteAnnotation
     */
    public RobotestSuite getSuiteAnnotation() {
        return suiteAnnotation;
    }

    /**
     * Setter method for the suite annotation.
     *
     * @param suiteAnnotation
     *            the suiteAnnotation to set
     */
    public void setSuiteAnnotation(final RobotestSuite suiteAnnotation) {
        this.suiteAnnotation = suiteAnnotation;
    }

    /**
     * Getter method for reporter.
     *
     * @return the reporter
     */
    public SuiteReport getReporter() {
        return reporter;
    }

    /**
     * Setter method for the reporter.
     *
     * @param reporter
     *            the reporter to set
     */
    public void setReporter(final SuiteReport reporter) {
        this.reporter = reporter;
    }

    /**
     * Get suite RobotestConfiguration objet. Returned object brings access to basic and custom user configurations.
     *
     * @return RobotestConfiguration.
     */
    public IRobotestConfiguration getConfig() {
        return cfg;
    }

    /**
     * Inits Case test with annotation. Load new Selenium Docker if and connect WebDriver or direct creates local driver
     * without Docker.
     *
     * @param testCaseAnnot
     *            Case annot.
     * @param suiteAnnot
     *            Suite annot.
     * @throws RobotestException
     *             Errors probably IO or creating Docker browser.
     */
    public void initTestCase(final RobotestSuite suiteAnnot,
                             final RobotestCase testCaseAnnot) throws RobotestException {
        getConfig().loadAnnotationScopeConfig(testCaseAnnot.configElements(), testCaseAnnot);
        List<ConfigEntry> lCfgs = cfg.toReportConfigEntries(testCaseAnnot);
        WebDriver wd = null;
        RobotestBasicConfig basicCfg = cfg.getConfigBasic();
        try {
            SeleniumDriverFactory selFactory = new SeleniumDriverFactory(basicCfg);

            if (null != basicCfg.getDocker() && StringUtils.isNotEmpty(basicCfg.getDocker().getHost())) {
                DockerConfig docker = RobotestExecutionContext.getDockerFarmBuilder()
                                                              .createBrowser(basicCfg.getBrowser().getBrowserName());
                RobotestExecutionContext.getBrowserDockers().get(suiteAnnotation).put(testCaseAnnot, docker);
                ConfigEntry cEntry = new ConfigEntry("DOCKER_BROWSER_INSTANCE",
                                                     docker);
                lCfgs.add(cEntry);
                wd = selFactory.buildDockerHubWebDriver(docker);
            } else if (null != basicCfg.getBrowserStack()) {
                wd = selFactory.buildBrowserStackRealDeviceWebDriver(suiteAnnot.tag(),
                                                                     testCaseAnnot.tag(),
                                                                     basicCfg.getBrowserStack());
            } else {
                wd = selFactory.buildLocalNativeWebDriver();
            }
            caseDrivers.put(testCaseAnnot, wd);
        } finally {
            reporter.initCase(testCaseAnnot.tag(), testCaseAnnot.description(), System.currentTimeMillis(), lCfgs);
        }
    }

    /**
     * Ends case report and stop Selenium Docker node if exist and loaded WebDriver.
     *
     * @param testCaseAnnot
     *            Case annot.
     * @throws RobotestException
     *             Errors probably IO or ending Docker browser.
     */
    public void endTestCase(final RobotestCase testCaseAnnot) throws RobotestException {
        StringBuilder errorResult = new StringBuilder();
        try {
            reporter.endCase(testCaseAnnot.tag(), System.currentTimeMillis());
        } catch (RobotestException e) {
            errorResult.append("IMPOSIBLE END CASE REPORT: ");
            errorResult.append(ValidationEntry.throwableToString(e));
            errorResult.append(" ");
        }
        WebDriver driver = caseDrivers.remove(testCaseAnnot);
        if (null != driver) {
            try {
                driver.quit();
            } catch (Exception e) {
                errorResult.append("ERROR CLOSING SELENIUM BROWSER: ");
                errorResult.append(ValidationEntry.throwableToString(e));
                errorResult.append(" ");
            }
        }
        DockerConfig docker = RobotestExecutionContext.getBrowserDockers().get(suiteAnnotation).remove(testCaseAnnot);
        if (null != docker) {
            try {
                RobotestExecutionContext.getDockerFarmBuilder().stopNode(docker.getIdContainer());
            } catch (Exception e) {
                errorResult.append(docker.getIdContainer());
                errorResult.append(": ");
                errorResult.append(ValidationEntry.throwableToString(e));
                errorResult.append(" ");
                SuiteContext.LOG.error("ERROR STOPING DOCKER SELENIUM NODE CONTAINER ID={}", errorResult);
            }
        }
        if (errorResult.length() > 0) {
            throw new RobotestException("ERROR ENDING CASE: " + errorResult.toString());
        }
    }

    /**
     * Init step config and report.
     *
     * @param pf
     *            PageObject object.
     * @param initMillis
     *            initial step timemillis.
     * @throws RobotestException
     *             errors loading step
     */
    public void initStep(final PageObject pf, final long initMillis) throws RobotestException {
        IRobotestConfiguration suiteCfg = RobotestExecutionContext.getSuite(pf.getSuiteAnnot()).getConfig();
        getConfig().loadAnnotationScopeConfig(pf.getStepAnnot().configElements(), pf.getStepAnnot());
        getReporter().initStep(pf.getCaseAnnot().tag(),
                               pf.getStepAnnot().tag(),
                               pf.getStepAnnot().description(),
                               suiteCfg.toReportConfigEntries(pf.getStepAnnot()),
                               initMillis);
        SuiteContext.LOG.info("STARTING STEP: {} OF {} OF {}",
                              pf.getStepAnnot().tag(),
                              pf.getSuiteAnnot().tag(),
                              pf.getCaseAnnot().tag());
    }

    /**
     * Adapter for legacy compatibilities.
     *
     * @param pf
     *            page object
     * @param idStep
     *            custom step id
     * @param stepDescription
     *            custom step description.
     * @param startMillis
     *            step start milliseconds
     * @param endMillis
     *            step end milliseconds
     * @param status
     *            Final step status.
     * @param listValidationEntries
     *            Validation entries list
     * @throws RobotestException
     *             errors loading step
     */
    public void addCustomStepToReport(final PageObject pf,
                                      final String idStep,
                                      final String stepDescription,
                                      final long startMillis,
                                      final long endMillis,
                                      final StepStatus status,
                                      final List<ValidationEntry> listValidationEntries) throws RobotestException {
        getReporter().initStep(pf.getCaseAnnot().tag(),
                               idStep,
                               stepDescription,
                               pf.getSuiteContext().getConfig().toReportConfigEntries(pf.getStepAnnot()),
                               startMillis);
        for (ValidationEntry validationEntry : listValidationEntries) {
            getReporter().addStepValidationEntry(pf.getCaseAnnot().tag(), idStep, validationEntry);
        }
        getReporter().endStep(pf.getCaseAnnot().tag(), idStep, status, endMillis);
        SuiteContext.LOG.info("ADD CUSTOM STEP: {} OF {} OF {}",
                              idStep,
                              pf.getCaseAnnot().tag(),
                              pf.getSuiteAnnot().tag());
    }

    /**
     * Ends step report.
     *
     * @param pf
     *            PageObject
     * @param resultadoStatusStep
     *            Step result status.
     * @param endMillis
     *            end time.
     */
    public void endStep(final PageObject pf, final StepStatus resultadoStatusStep, final long endMillis) {
        getReporter().endStep(pf.getCaseAnnot().tag(), pf.getStepAnnot().tag(), resultadoStatusStep, endMillis);
        SuiteContext.LOG.info("ENDING STEP: {} OF {} OF {}",
                              pf.getStepAnnot().tag(),
                              pf.getCaseAnnot().tag(),
                              pf.getSuiteAnnot().tag());
    }

    /**
     * Add aditional step validation entry.
     *
     * @param pf
     *            PageObject
     * @param additionalEntry
     *            additional entry.
     */
    public void addAdditionalStepEntry(final PageObject pf, final ValidationEntry additionalEntry) {
        getReporter().addStepValidationEntry(pf.getCaseAnnot().tag(), pf.getStepAnnot().tag(), additionalEntry);
    }

    /**
     * Put Report Suite out case initialitation errors. If no reporter available, validation entry is redirected to
     * CONSOLE.
     *
     * @param validationEntry
     *            validation entry.
     */
    protected void putIntialitationSuiteError(final ValidationEntry validationEntry) {
        try {
            getReporter().addSuiteValidationEntry(validationEntry);
        } catch (Exception e) {
            SuiteContext.LOG.info("ERROR WRITING SUITE OUT CASE ERROR: {} CAUSE: {}",
                                  validationEntry.getResource(),
                                  e.getMessage());
        }
    }

    /**
     * Put Report Case out step initialitation errors.
     *
     * @param caseAnnot
     *            case annotation.
     * @param validationEntry
     *            validation entry.
     */
    protected void putCaseError(final RobotestCase caseAnnot, final ValidationEntry validationEntry) {
        getReporter().addCaseValidationEntry(caseAnnot.tag(), validationEntry);

    }

}
