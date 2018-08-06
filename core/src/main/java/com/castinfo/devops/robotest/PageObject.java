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
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;

import com.castinfo.devops.robotest.report.SuiteReport;
import com.castinfo.devops.robotest.report.ValidationEntry;
import com.castinfo.devops.robotest.restassured.RestAssuredWrapper;
import com.castinfo.devops.robotest.selenium.SeleniumElementsWrapper;

/**
 * This class is used to PageObject/PageObject test orienting.
 *
 * Classes that extends this object will have available access to the ROBOTEST Test Tooling utilities: Selenium Driver
 * and Reporting.
 *
 * Implements Selenium most used test utilities, to simplify tests, but it is not mandatory, feel free to see source and
 * improve your own utilites. If you you may be wan't enrich this utilities, please pull request ROBOTEST with you
 * proposes.
 *
 */
public class PageObject extends SeleniumElementsWrapper {

    private static final AtomicInteger AVOID_REPORT_FILE_NAME_CONFLICT_COUNTER = new AtomicInteger();

    /**
     * This method is for assure that report fileNames not enter in conflict.
     *
     * @return String
     */
    private static String getNextNonConflictFileName(final String fileName) {
        return fileName + "_" + PageObject.AVOID_REPORT_FILE_NAME_CONFLICT_COUNTER.incrementAndGet();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.castinfo.devops.robotest.config.ConfigurationAccess#getTestCfg(java.lang.String)
     */
    @Override
    public <T> T getTestCfg(final String configKey) throws RobotestException {
        T scopedConfig = this.getTestCfgInScope(getSuiteContext(),
                                                getStepAnnot(),
                                                configKey);
        if (null == scopedConfig) {
            scopedConfig = this.getTestCfgInScope(getSuiteContext(),
                                                  getCaseAnnot(),
                                                  configKey);
            if (null == scopedConfig) {
                scopedConfig = this.getTestCfgInScope(getSuiteContext(),
                                                      getSuiteAnnot(),
                                                      configKey);
            }
        }
        return scopedConfig;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.castinfo.devops.robotest.selenium.SeleniumElementsWrapper#getDriver()
     */
    @Override
    public WebDriver getDriver() throws RobotestException {
        SuiteContext sCtx = getSuiteContext();
        WebDriver wD = sCtx.getDriverByCase(getCaseAnnot());
        if (null == wD) {
            throw new RobotestException("WEBDRIVER NOT FOUND, REVISE CONFIGURATION PARAMS");
        }
        return wD;
    }

    /**
     * Internal getter of reporter.
     *
     * @return SuiteReporter
     * @throws RobotestException
     *             If suite initialitation problems happens.
     */
    protected SuiteReport getReporter() throws RobotestException {
        SuiteContext sCtx = getSuiteContext();
        SuiteReport sR = sCtx.getReporter();
        if (null == sR) {
            throw new RobotestException("REPORTER NOT FOUND, REVISE CONFIGURATION PARAMS");
        }
        return sR;
    }

    /**
     * Add step validation entry to the report.
     *
     * @param validation
     *            validation.
     * @return ValidationEntry
     * @throws RobotestException
     *             if Reporter unavailable
     */
    private ValidationEntry addValidationToReport(final ValidationEntry validation) throws RobotestException {
        ValidationEntry resultado;
        if (null == getStepAnnot()) {
            resultado = getReporter().addCaseValidationEntry(getCaseAnnot().tag(),
                                                             validation);
        } else {
            resultado = getReporter().addStepValidationEntry(getCaseAnnot().tag(),
                                                             getStepAnnot().tag(),
                                                             validation);
        }
        return resultado;
    }

    /**
     * Add Debug entry to report.
     *
     * @return the fluent api object
     * @throws RobotestException
     *             Problems to add to report.
     */
    public ValidationEntry addDebugToReport() throws RobotestException {
        ValidationEntry v = ValidationEntry.buildDebug();
        return addValidationToReport(v);
    }

    /**
     * Add Info entry to report.
     *
     * @return the fluent api object
     * @throws RobotestException
     *             Problems to add to report.
     */
    public ValidationEntry addInfoToReport() throws RobotestException {
        ValidationEntry v = ValidationEntry.buildInfo();
        return addValidationToReport(v);
    }

    /**
     * Add Warning entry to report.
     *
     * @return the fluent api object
     * @throws RobotestException
     *             Problems to add to report.
     */
    public ValidationEntry addWarningToReport() throws RobotestException {
        ValidationEntry v = ValidationEntry.buildWarning();
        return addValidationToReport(v);
    }

    /**
     * Add Error entry to report.
     *
     * @return the fluent api object
     * @throws RobotestException
     *             Problems to add to report.
     */
    public ValidationEntry addErrorToReport() throws RobotestException {
        ValidationEntry v = ValidationEntry.buildError();
        return addValidationToReport(v);
    }

    /**
     * Add Critical entry to report.
     *
     * @return the fluent api object
     * @throws RobotestException
     *             Problems to add to report.
     */
    public ValidationEntry addDefectToReport() throws RobotestException {
        ValidationEntry v = ValidationEntry.buildDefect();
        return addValidationToReport(v);
    }

    /**
     * Add Defect entry to report.
     *
     * @return the fluent api object
     * @throws RobotestException
     *             Problems to add to report.
     */
    public ValidationEntry addCriticalToReport() throws RobotestException {
        ValidationEntry v = ValidationEntry.buildCritical();
        return addValidationToReport(v);
    }

    /**
     * Add a screen shot to report.
     *
     * @param status
     *            Step status.
     * @param customName
     *            Name of capture.
     * @throws RobotestException
     *             IO errors.
     */
    public void addScreenShotToReport(final StepStatus status,
                                      final String customName) throws RobotestException {
        ValidationEntry validation = new ValidationEntry(status);
        validation.withCapture(this.takeScreenShoot(customName));
        if (null == getStepAnnot()) {
            throw new RobotestException("Can't add screen shot to report out of step context");
        }
        getReporter().addStepValidationEntry(getCaseAnnot().tag(),
                                             getStepAnnot().tag(),
                                             validation);
    }

    /**
     * Add a HTML page source to report.
     *
     * @param status
     *            Step status.
     * @param customName
     *            Name of HTML.
     * @throws RobotestException
     *             IO errors.
     */
    public void addPageSourceToReport(final StepStatus status,
                                      final String customName) throws RobotestException {
        ValidationEntry validation = new ValidationEntry(status);
        validation.withHtmlSource(this.takePageSource(customName));
        if (null == getStepAnnot()) {
            throw new RobotestException("Can't add page source to report out of step context");
        }
        getReporter().addStepValidationEntry(getCaseAnnot().tag(),
                                             getStepAnnot().tag(),
                                             validation);
    }

    /**
     * Get millis general wait timeout.
     *
     * @return millis general wait timeout
     * @throws RobotestException
     *             If suite initialization problems happens.
     */
    public long getGeneralWaitTimoutMillis() throws RobotestException {
        long results = -1;
        try {
            results = Long.parseLong(this.getBasicCfg()
                                         .getHttp()
                                         .getGeneralTimeout());
        } catch (NumberFormatException e) {
            throw new RobotestException("INCORRECT GENERAL TIMEOUT FOUND",
                                        e);
        }
        return results;
    }

    /**
     * Get millis general wait timeout.
     *
     * @return millis general wait timeout
     * @throws RobotestException
     *             If suite initialization problems happens.
     */
    public long getGeneralWaitTimoutSeconds() throws RobotestException {
        long results = -1;
        try {
            results = this.getBasicCfg()
                          .getHttp()
                          .obtainGeneralTimeoutInSeconds();
        } catch (NumberFormatException e) {
            throw new RobotestException("INCORRECT GENERAL TIMEOUT FOUND",
                                        e);
        }
        return results;
    }

    /**
     * Builds a integrated wrapper for RestAssured REST API client tester
     *
     * @return The wrapper of RestAssured.
     */
    public RestAssuredWrapper givenRestAssured() {
        RestAssuredWrapper restClient = new RestAssuredWrapper();
        return restClient;
    }

    /**
     * Builds a integrated wrapper for RestAssured REST API client tester.
     *
     * We add proxy and timeout ROBOTEST configured settings.
     *
     * @return The wrapper of RestAssured.
     * @throws RobotestException
     */
    public RestAssuredWrapper givenRestAssuredWithHttpSettings() throws RobotestException {
        return new RestAssuredWrapper(this.getBasicCfg()
                                          .getHttp());
    }

    /**
     *
     * Uses @see com.castinfo.devops.robotest.selenium.SeleniumElementsWrapper#takeScreenShoot() to return File results:
     * - The path of the image will be relative to the System Property ROBOTEST_REPORT_BASE.
     *
     * - Robotest keep you from worrying with repetible names adding a secure counter to this name.
     *
     * @param fileName
     *            name of file without extension
     * @return File file of SCREENSHOT
     * @throws RobotestException
     *             WebDriver or IOExceptions.
     */
    public File takeScreenShoot(final String fileName) throws RobotestException {
        try {
            return this.takeEvidence(getSuiteContext().getConfig()
                                                      .getConfigBasic()
                                                      .getReportFilePath(),
                                     fileName,
                                     ".png",
                                     this.takeScreenShoot());
        } catch (IOException e) {
            throw new RobotestException("TAKE SCREENSHOT PAGE SOURCE ERROR",
                                        e);
        }
    }

    /**
     * Save browser present HTML page source with user passed file name. The path of saved HTML will be relative to the
     * System Property ROBOTEST_REPORT_BASE. ROBOTEST keep you from worrying with repetible names adding a secure
     * counter to this name.
     *
     * @param fileName
     *            name of file without extension.
     * @return HTML file captured
     * @throws RobotestException
     *             Error de captura.
     */
    public File takePageSource(final String fileName) throws RobotestException {
        File resultado = null;
        try {
            resultado = this.takeEvidence(getSuiteContext().getConfig()
                                                           .getConfigBasic()
                                                           .getReportFilePath(),
                                          fileName,
                                          ".html",
                                          this.takePageSource()
                                              .getBytes("UTF-8"));
        } catch (IOException e) {
            throw new RobotestException("TAKE PAGE SOURCE ERROR",
                                        e);
        }
        return resultado;
    }

    /**
     * Utility method for developer own evidence reporting. The path of saved HTML will be relative to the System
     * Property ROBOTEST_REPORT_BASE. ROBOTEST keep you from worrying with repetible names adding a secure counter to
     * this name.
     *
     * @param fileName
     *            Name of file without extension.
     * @param extension
     *            Extension
     * @param content
     *            The content to save in byte array.
     * @return File saved.
     * @throws RobotestException
     *             Problems with file capturing.
     */
    public File takeEvidence(final String fileName,
                             final String extension,
                             final byte[] content) throws RobotestException {
        File resultado = null;
        try {
            resultado = this.takeEvidence(getSuiteContext().getConfig()
                                                           .getConfigBasic()
                                                           .getReportFilePath(),
                                          fileName,
                                          extension,
                                          content);
        } catch (IOException e) {
            throw new RobotestException("TAKE PAGE SOURCE ERROR",
                                        e);
        }
        return resultado;
    }

    /**
     * Utility method for developer own evidence reporting. ROBOTEST keep you from worrying with repetible names adding
     * a secure counter to this name.
     *
     * @param reportPath
     *            Path to save method.
     * @param fileName
     *            Name of file without extension.
     * @param extension
     *            Extension
     * @param content
     *            The content to save in byte array.
     * @return File saved.
     * @throws IOException
     *             Problems with file capturing.
     */
    private File takeEvidence(final String reportPath,
                              final String fileName,
                              final String extension,
                              final byte[] content) throws IOException {
        StringBuilder absoluteFileName = new StringBuilder();
        absoluteFileName.append(reportPath);
        absoluteFileName.append(File.separator);
        absoluteFileName.append(PageObject.getNextNonConflictFileName(fileName));
        absoluteFileName.append(extension);
        File resultado = new File(absoluteFileName.toString());
        FileUtils.writeByteArrayToFile(resultado,
                                       content);
        return resultado;
    }

    /**
     * Returns a limited list of ValidationEntrys of WebDriver visible log from browser CONSOLE of any kind (JavaScript,
     * CSS, network, etc), with the log level defined in basic configuration robotest parameters. The retrived Level
     * status equivalence is WARNING for WARNING, ERROR for &gt; WARNING &amp; INFO for &lt; WARNING.
     *
     * @return The retrived browser CONSOLE log list.
     * @throws RobotestException
     *             Errors in the retriving.
     */
    public List<ValidationEntry> takeBrowserConsoleLogs() throws RobotestException {
        return this.takeBrowserConsoleLogs(Level.parse(this.getBasicCfg()
                                                           .getBrowser()
                                                           .getConsoleLogLevel()));
    }

}
