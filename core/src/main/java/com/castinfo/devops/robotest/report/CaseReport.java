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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.castinfo.devops.robotest.RobotestException;
import com.castinfo.devops.robotest.StepStatus;
import com.fasterxml.jackson.core.JsonGenerator;

/**
 * Robotest Case JSON report writer implementation.
 */
public class CaseReport {

    private String id;
    private String description;
    private ConfigReport caseConfig;
    private long initMillis;
    private long endMillis;
    private int caseOrder;
    private List<StepReport> stepReports = new ArrayList<>();
    private List<ValidationEntry> outStepErrors = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param caseOrder
     *            case exec. order.
     * @param id
     *            case annot tag or custom id.
     * @param description
     *            description
     * @param initMillis
     *            init case timemillis.
     * @param caseConfig
     *            list of case config.
     */
    public CaseReport(final int caseOrder,
                      final String id,
                      final String description,
                      final long initMillis,
                      final List<ConfigEntry> caseConfig) {
        this.caseOrder = caseOrder;
        this.id = id;
        this.description = description;
        this.initMillis = initMillis;
        this.caseConfig = new ConfigReport(caseConfig);
    }

    /**
     * Getter method for id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Setter method for the id.
     *
     * @param id
     *            the id to set
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Getter method for description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter method for the description.
     *
     * @param description
     *            the description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Getter method for caseConfig.
     *
     * @return the caseConfig
     */
    public ConfigReport getCaseConfig() {
        return caseConfig;
    }

    /**
     * Setter method for the caseConfig.
     *
     * @param caseConfig
     *            the caseConfig to set
     */
    public void setCaseConfig(final ConfigReport caseConfig) {
        this.caseConfig = caseConfig;
    }

    /**
     * Getter method for initMillis.
     *
     * @return the initMillis
     */
    public long getInitMillis() {
        return initMillis;
    }

    /**
     * Setter method for the initMillis.
     *
     * @param initMillis
     *            the initMillis to set
     */
    public void setInitMillis(final long initMillis) {
        this.initMillis = initMillis;
    }

    /**
     * Getter method for endMillis.
     *
     * @return the endMillis
     */
    public long getEndMillis() {
        return endMillis;
    }

    /**
     * Setter method for the endMillis.
     *
     * @param endMillis
     *            the endMillis to set
     */
    public void setEndMillis(final long endMillis) {
        this.endMillis = endMillis;
    }

    /**
     * Getter method for caseOrder.
     *
     * @return the caseOrder
     */
    public int getCaseOrder() {
        return caseOrder;
    }

    /**
     * Setter method for the caseOrder.
     *
     * @param caseOrder
     *            the caseOrder to set
     */
    public void setCaseOrder(final int caseOrder) {
        this.caseOrder = caseOrder;
    }

    /**
     * Getter method for stepReports.
     *
     * @return the stepReports
     */
    public List<StepReport> getStepReports() {
        return stepReports;
    }

    /**
     * Setter method for the stepReports.
     *
     * @param stepReports
     *            the stepReports to set
     */
    public void setStepReports(final List<StepReport> stepReports) {
        this.stepReports = stepReports;
    }

    /**
     * Getter method for outStepErrors.
     *
     * @return the outStepErrors
     */
    public List<ValidationEntry> getOutStepErrors() {
        return outStepErrors;
    }

    /**
     * Setter method for the outStepErrors.
     *
     * @param outStepErrors
     *            the outStepErrors to set
     */
    public void setOutStepErrors(final List<ValidationEntry> outStepErrors) {
        this.outStepErrors = outStepErrors;
    }

    /**
     * End case.
     *
     * @param millis
     *            end case timemillis.
     */
    public void endCase(final long millis) {
        endMillis = millis;
    }

    /**
     * Add step to case.
     *
     * @param stepReport
     *            step report.
     */
    public void addStep(final StepReport stepReport) {
        stepReports.add(stepReport);
    }

    /**
     * Write the case in the suite JSON writer.
     *
     * @param jGenerator
     *            json writer.
     * @throws RobotestException
     *             errors, probably IO.
     */
    public void writeCase(final JsonGenerator jGenerator) throws RobotestException {
        try {
            jGenerator.writeStartObject();
            jGenerator.writeNumberField("order",
                                        caseOrder);
            jGenerator.writeStringField("case",
                                        id);
            jGenerator.writeStringField("description",
                                        description);
            jGenerator.writeNumberField("initMillis",
                                        initMillis);

            caseConfig.writeConfig(jGenerator,
                                   "config");

            jGenerator.writeFieldName("caseOutStepErrors");
            jGenerator.writeStartArray();
            for (ValidationEntry toadd : outStepErrors) {
                jGenerator.writeStartObject();
                jGenerator.writeStringField("status",
                                            toadd.getStatus()
                                                 .name());
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

            jGenerator.writeFieldName("steps");
            jGenerator.writeStartArray();
            for (StepReport step : stepReports) {
                step.writeStep(jGenerator);
            }
            jGenerator.writeEndArray();
            jGenerator.writeNumberField("endMillis",
                                        endMillis);
            jGenerator.writeEndObject();
        } catch (IOException e) {
            throw new RobotestException("ROBOTEST REPORTER ERROR CASE",
                                        e);
        }
    }

    /**
     * Put informatin status and endMillis to step object.
     *
     * @param caseId
     *            annotation step or custom id
     * @param endMs
     *            end millisecons
     * @param status
     *            final status of step.
     */
    public void endStep(final String caseId,
                        final long endMs,
                        final StepStatus status) {
        for (StepReport stepReport : stepReports) {
            if (stepReport.getId()
                          .equals(caseId)) {
                stepReport.endStep(status,
                                   endMs);
            }
        }

    }

}
