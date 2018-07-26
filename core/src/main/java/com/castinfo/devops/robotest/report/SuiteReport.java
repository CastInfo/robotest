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
package com.castinfo.devops.robotest.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.castinfo.devops.robotest.RobotestException;
import com.castinfo.devops.robotest.StepStatus;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

/**
 * Robotest Suite JSON report writer implementation.
 *
 */
public class SuiteReport {

    private static SuitesListReport suitesListReporter = null;
    private static final Object WRITER_BLOCKER = new Object();

    private int suiteOrder;

    private AtomicInteger caseOrder;
    private AtomicInteger stepOrder;

    private JsonGenerator jGenerator;
    private ConfigReport suiteConfig;

    private List<CaseReport> caseReports = new ArrayList<>();
    private List<ValidationEntry> outCaseErrors = new ArrayList<>();

    /**
     * Creates Suite robotest report, creating the JSON file.
     *
     * @param suiteOrder
     *            Suite execution order.
     * @param reportFile
     *            Report file.
     * @throws RobotestException
     *             Errors, probably IO.
     */
    public SuiteReport(final int suiteOrder, final File reportFile) throws RobotestException {
        try {
            reportFile.getParentFile().mkdirs();
            this.suiteOrder = suiteOrder;
            JsonFactory jfactory = new JsonFactory();
            jGenerator = jfactory.createGenerator(reportFile, JsonEncoding.UTF8);
            synchronized (SuiteReport.WRITER_BLOCKER) {
                if (null == SuiteReport.suitesListReporter) {
                    File suiteReporterFile = new File(reportFile.getParentFile().getAbsolutePath()
                            + "/robotest-suites-list.json");
                    SuiteReport.suitesListReporter = new SuitesListReport(suiteReporterFile);
                }
                SuiteReport.suitesListReporter.appendToSuiteListReport(reportFile.getName());
            }
        } catch (IOException e) {
            throw new RobotestException("ROBOTEST SUITE REPORTER ERROR", e);
        }
    }

    /**
     * Getter method for suiteOrder.
     *
     * @return the suiteOrder
     */
    public int getSuiteOrder() {
        return suiteOrder;
    }

    /**
     * Setter method for the suiteOrder.
     *
     * @param suiteOrder
     *            the suiteOrder to set
     */
    public void setSuiteOrder(final int suiteOrder) {
        this.suiteOrder = suiteOrder;
    }

    /**
     * Getter method for caseOrder.
     *
     * @return the caseOrder
     */
    public AtomicInteger getCaseOrder() {
        return caseOrder;
    }

    /**
     * Setter method for the caseOrder.
     *
     * @param caseOrder
     *            the caseOrder to set
     */
    public void setCaseOrder(final AtomicInteger caseOrder) {
        this.caseOrder = caseOrder;
    }

    /**
     * Getter method for stepOrder.
     *
     * @return the stepOrder
     */
    public AtomicInteger getStepOrder() {
        return stepOrder;
    }

    /**
     * Setter method for the stepOrder.
     *
     * @param stepOrder
     *            the stepOrder to set
     */
    public void setStepOrder(final AtomicInteger stepOrder) {
        this.stepOrder = stepOrder;
    }

    /**
     * Getter method for jGenerator.
     *
     * @return the jGenerator
     */
    public JsonGenerator getjGenerator() {
        return jGenerator;
    }

    /**
     * Setter method for the jGenerator.
     *
     * @param jGenerator
     *            the jGenerator to set
     */
    public void setjGenerator(final JsonGenerator jGenerator) {
        this.jGenerator = jGenerator;
    }

    /**
     * Getter method for suiteConfig.
     *
     * @return the suiteConfig
     */
    public ConfigReport getSuiteConfig() {
        return suiteConfig;
    }

    /**
     * Setter method for the suiteConfig.
     *
     * @param suiteConfig
     *            the suiteConfig to set
     */
    public void setSuiteConfig(final ConfigReport suiteConfig) {
        this.suiteConfig = suiteConfig;
    }

    /**
     * Getter method for caseReports.
     *
     * @return the caseReports
     */
    public List<CaseReport> getCaseReports() {
        return caseReports;
    }

    /**
     * Setter method for the caseReports.
     *
     * @param caseReports
     *            the caseReports to set
     */
    public void setCaseReports(final List<CaseReport> caseReports) {
        this.caseReports = caseReports;
    }

    /**
     * Getter method for outCaseErrors.
     *
     * @return the outCaseErrors
     */
    public List<ValidationEntry> getOutCaseErrors() {
        return outCaseErrors;
    }

    /**
     * Setter method for the outCaseErrors.
     *
     * @param outCaseErrors
     *            the outCaseErrors to set
     */
    public void setOutCaseErrors(final List<ValidationEntry> outCaseErrors) {
        this.outCaseErrors = outCaseErrors;
    }

    /**
     * Init Suite in report.
     *
     * @param suiteId
     *            Suite Annot tag or custom id.
     * @param suiteDescription
     *            Suite custom description.
     * @param initMillis
     *            init execution time.
     * @param config
     *            Config entries.
     * @throws RobotestException
     *             Errors, probably IO.
     */
    public void initSuite(final String suiteId, final String suiteDescription, final long initMillis,
                          final List<ConfigEntry> config) throws RobotestException {
        try {
            caseOrder = new AtomicInteger(0);
            jGenerator.writeStartObject();
            jGenerator.writeNumberField("order", suiteOrder);
            jGenerator.writeStringField("suite", suiteId);
            jGenerator.writeStringField("description", suiteDescription);
            jGenerator.writeNumberField("initMillis", initMillis);

            suiteConfig = new ConfigReport(config);
            suiteConfig.writeConfig(jGenerator, "config");

            jGenerator.writeFieldName("cases");
            jGenerator.writeStartArray();
        } catch (IOException e) {
            throw new RobotestException("ROBOTEST REPORTER ERROR", e);
        }
    }

    /**
     * Flush Suite finish data and out case errors.
     *
     * @param time
     *            end time millis.
     * @throws RobotestException
     *             errors, probably IO.
     */
    public void endSuite(final long time) throws RobotestException {
        try {
            jGenerator.writeEndArray();

            jGenerator.writeFieldName("suiteOutCaseErrors");
            jGenerator.writeStartArray();
            for (ValidationEntry toadd : outCaseErrors) {
                jGenerator.writeStartObject();
                jGenerator.writeStringField("status", toadd.getStatus().name());
                if (null != toadd.getResource()) {
                    jGenerator.writeArrayFieldStart("resource");
                    for (String res : toadd.getResource()) {
                        jGenerator.writeObject(res);
                    }
                    jGenerator.writeEndArray();
                }
                jGenerator.writeEndObject();
            }
            jGenerator.writeEndArray();

            jGenerator.writeNumberField("endMillis", time);
            jGenerator.writeEndObject();
            jGenerator.close();
        } catch (IOException e) {
            throw new RobotestException("ROBOTEST ERROR REPORT", e);
        }
    }

    /**
     * Init CaseReport under Suite in report.
     *
     * @param caseId
     *            case annot tag or custom id.
     * @param caseDescription
     *            case custom description
     * @param initMillis
     *            execution init millis
     * @param caseConfig
     *            case configs.
     */
    public void initCase(final String caseId, final String caseDescription, final long initMillis,
                         final List<ConfigEntry> caseConfig) {
        stepOrder = new AtomicInteger(0);
        caseReports.add(new CaseReport(caseOrder.getAndIncrement(), caseId, caseDescription, initMillis, caseConfig));
    }

    /**
     * End case to suite report.
     *
     * @param caseId
     *            case annot tag or custom Id.
     * @param endMillis
     *            final time millis.
     * @throws RobotestException
     *             Errors, probably IO.
     */
    public void endCase(final String caseId, final long endMillis) throws RobotestException {
        for (CaseReport caseReport : caseReports) {
            if (caseReport.getId().equals(caseId)) {
                caseReport.endCase(endMillis);
                synchronized (SuiteReport.WRITER_BLOCKER) {
                    caseReport.writeCase(jGenerator);
                }
            }
        }

    }

    /**
     * Initialize step report inside Case of Suite report.
     *
     * @param caseId
     *            case annot tag or custom id.
     * @param stepId
     *            step annot tag or custom id.
     * @param stepDescription
     *            step custom description.
     * @param stepConfig
     *            step config.
     * @param initMillis
     *            initial exec. millis
     */
    public void initStep(final String caseId, final String stepId, final String stepDescription,
                         final List<ConfigEntry> stepConfig, final long initMillis) {
        for (CaseReport caseReport : caseReports) {
            if (caseReport.getId().equals(caseId)) {
                caseReport.addStep(new StepReport(stepOrder.getAndIncrement(), stepId, stepDescription, stepConfig,
                                                  initMillis));
            }
        }
    }

    /**
     * Add step validation entry to step.
     *
     * @param caseId
     *            case annot tag or custom id.
     * @param stepId
     *            secondary step annot tag or custom id.
     * @param validationEntry
     *            validation entry.
     * @return The validation entry to fluent api purposes.
     */
    public ValidationEntry addStepValidationEntry(final String caseId, final String stepId,
                                                  final ValidationEntry validationEntry) {
        for (CaseReport caseReport : caseReports) {
            if (caseReport.getId().equals(caseId)) {
                for (StepReport stepReport : caseReport.getStepReports()) {
                    if (null != stepReport.getId() && stepReport.getId().equals(stepId)) {
                        validationEntry.setValidationOrder(stepReport.getValidationOrder().getAndIncrement());
                        stepReport.getAdditional().add(validationEntry);
                    }
                }
            }
        }
        return validationEntry;
    }

    /**
     * Add out step case validation entrys.
     *
     * @param caseId
     *            case annot tag or custom id.
     * @param validationEntry
     *            validation entry.
     * @return The validation entry to fluent api purposes.
     */
    public ValidationEntry addCaseValidationEntry(final String caseId, final ValidationEntry validationEntry) {
        for (CaseReport caseReport : caseReports) {
            if (caseReport.getId().equals(caseId)) {
                caseReport.getOutStepErrors().add(validationEntry);
            }
        }
        return validationEntry;
    }

    /**
     * Add out suite validation entry.
     *
     * @param validationEntry
     *            entry.
     */
    public void addSuiteValidationEntry(final ValidationEntry validationEntry) {
        outCaseErrors.add(validationEntry);
    }

    /**
     * Flush step to case suite report, including validation entrys.
     *
     * @param caseId
     *            case annot tag or custom id.
     * @param stepId
     *            step annot tag or custom id.
     * @param resultadoStep
     *            Step final result status.
     * @param endMillis
     *            end time millis.
     */
    public void endStep(final String caseId, final String stepId, final StepStatus resultadoStep,
                        final long endMillis) {
        for (CaseReport caseReport : caseReports) {
            if (caseReport.getId().equals(caseId)) {
                for (StepReport stepReport : caseReport.getStepReports()) {
                    if (stepReport.getId().equals(stepId)) {
                        stepReport.setEndMillis(endMillis);
                        stepReport.setStepStatus(resultadoStep);
                    }
                }
            }
        }
    }

}
