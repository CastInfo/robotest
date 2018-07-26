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
import java.util.concurrent.atomic.AtomicInteger;

import com.castinfo.devops.robotest.RobotestException;
import com.castinfo.devops.robotest.StepStatus;
import com.fasterxml.jackson.core.JsonGenerator;

/**
 * Robotest Step JSON report writer implementation.
 */
public class StepReport {

    private List<ValidationEntry> additional = new ArrayList<>();
    private String id;
    private String description;
    private StepStatus stepStatus;
    private ConfigReport stepConfig;
    private long initMillis;
    private long endMillis;
    private int stepOrder;
    private AtomicInteger validationOrder;

    /**
     * Constructor.
     *
     * @param stepOrder
     *            step order.
     * @param id
     *            step annot tag or custom id
     * @param description
     *            step annot description or custom message
     * @param stepConfig
     *            step config.
     * @param initMillis
     *            initial exec millis.
     */
    public StepReport(final int stepOrder,
                      final String id,
                      final String description,
                      final List<ConfigEntry> stepConfig,
                      final long initMillis) {
        this.stepOrder = stepOrder;
        validationOrder = new AtomicInteger(0);
        this.id = id;
        this.description = description;
        this.stepConfig = new ConfigReport(stepConfig);
        this.initMillis = initMillis;
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
     * Getter method for additional.
     *
     * @return the additional
     */
    public List<ValidationEntry> getAdditional() {
        return additional;
    }

    /**
     * Setter method for the additional.
     *
     * @param additional
     *            the additional to set
     */
    public void setAdditional(final List<ValidationEntry> additional) {
        this.additional = additional;
    }

    /**
     * Getter method for stepStatus.
     *
     * @return the stepStatus
     */
    public StepStatus getStepStatus() {
        return stepStatus;
    }

    /**
     * Setter method for the stepStatus.
     *
     * @param stepStatus
     *            the stepStatus to set
     */
    public void setStepStatus(final StepStatus stepStatus) {
        this.stepStatus = stepStatus;
    }

    /**
     * Getter method for stepConfig.
     *
     * @return the stepConfig
     */
    public ConfigReport getStepConfig() {
        return stepConfig;
    }

    /**
     * Setter method for the stepConfig.
     *
     * @param stepConfig
     *            the stepConfig to set
     */
    public void setStepConfig(final ConfigReport stepConfig) {
        this.stepConfig = stepConfig;
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
     * Getter method for stepOrder.
     *
     * @return the stepOrder
     */
    public int getStepOrder() {
        return stepOrder;
    }

    /**
     * Setter method for the stepOrder.
     *
     * @param stepOrder
     *            the stepOrder to set
     */
    public void setStepOrder(final int stepOrder) {
        this.stepOrder = stepOrder;
    }

    /**
     * Getter method for validationOrder.
     *
     * @return the validationOrder
     */
    public AtomicInteger getValidationOrder() {
        return validationOrder;
    }

    /**
     * Setter method for the validationOrder.
     *
     * @param validationOrder
     *            the validationOrder to set
     */
    public void setValidationOrder(final AtomicInteger validationOrder) {
        this.validationOrder = validationOrder;
    }

    /**
     * End step.
     *
     * @param status
     *            status level.
     * @param millis
     *            end step timemillis.
     */
    public void endStep(final StepStatus status, final long millis) {
        stepStatus = status;
        endMillis = millis;
    }

    /**
     * Write step in Suite JSON generator writer.
     *
     * @param jGenerator
     *            writer.
     * @throws RobotestException
     *             Errors, probably IO.
     */
    public void writeStep(final JsonGenerator jGenerator) throws RobotestException {
        try {
            jGenerator.writeStartObject();
            jGenerator.writeNumberField("order", stepOrder);
            jGenerator.writeStringField("step", id);
            jGenerator.writeStringField("description", description);
            jGenerator.writeNumberField("initMillis", initMillis);
            jGenerator.writeNumberField("endMillis", endMillis);
            jGenerator.writeStringField("status", stepStatus.name());

            stepConfig.writeConfig(jGenerator, "config");

            jGenerator.writeFieldName("validations");
            jGenerator.writeStartArray();
            for (ValidationEntry toadd : additional) {
                toadd.writeValidation(jGenerator);
            }
            jGenerator.writeEndArray();
            jGenerator.writeEndObject();
        } catch (IOException e) {
            throw new RobotestException("ROBOTEST STEP REPORTER ERROR",
                                        e);
        }
    }

}
