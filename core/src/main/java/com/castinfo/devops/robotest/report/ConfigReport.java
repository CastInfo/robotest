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
import java.util.List;
import java.util.Properties;

import com.castinfo.devops.robotest.RobotestException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

/**
 * Robotest Step JSON report writer implementation.
 */
public class ConfigReport {

    private List<ConfigEntry> configs;
    private ObjectMapper mapper;

    /**
     * Constructor.
     *
     * @param cfg
     *            config list.
     *
     */
    public ConfigReport(final List<ConfigEntry> cfg) {
        configs = cfg;
        mapper = new ObjectMapper();
        mapper.registerModule(new JaxbAnnotationModule());
    }

    /**
     * Getter method for configs.
     *
     * @return the configs
     */
    public List<ConfigEntry> getConfigs() {
        return configs;
    }

    /**
     * Setter method for the configs.
     *
     * @param configs
     *            the configs to set
     */
    public void setConfigs(final List<ConfigEntry> configs) {
        this.configs = configs;
    }

    /**
     * Getter method for mapper.
     *
     * @return the mapper
     */
    public ObjectMapper getMapper() {
        return mapper;
    }

    /**
     * Setter method for the mapper.
     *
     * @param mapper
     *            the mapper to set
     */
    public void setMapper(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Config writer in Suite JSON report generator.
     *
     * @param jGenerator
     *            writer
     * @param cfgName
     *            config name.
     * @throws RobotestException
     *             Errors, provably IO.
     */
    public void writeConfig(final JsonGenerator jGenerator, final String cfgName) throws RobotestException {
        try {
            jGenerator.writeFieldName(cfgName);
            jGenerator.writeStartArray();
            for (ConfigEntry cfg : configs) {
                writeConfigTraverse(jGenerator, cfg);
            }
            jGenerator.writeEndArray();
        } catch (IOException e) {
            throw new RobotestException("ROBOTEST REPORTER CONFIG WRITE ERROR", e);
        }
    }

    /**
     * Traverse in configuration to addapt uniform JSON format.
     *
     * @param jGenerator
     *            writer
     * @param cfg
     *            cfg
     * @throws IOException
     *             IO errors.
     */
    private void writeConfigTraverse(final JsonGenerator jGenerator, final ConfigEntry cfg) throws IOException {
        if (cfg.getValue() instanceof String) {
            jGenerator.writeStartObject();
            jGenerator.writeStringField(cfg.getId(), cfg.getValue().toString());
            jGenerator.writeEndObject();
        } else if (cfg.getValue() instanceof Properties) {
            jGenerator.writeStartObject();
            jGenerator.writeFieldName(cfg.getId());
            jGenerator.writeStartObject();
            for (Object key : ((Properties) cfg.getValue()).keySet()) {
                jGenerator.writeStringField(key.toString(), ((Properties) cfg.getValue()).get(key).toString());
            }
            jGenerator.writeEndObject();
            jGenerator.writeEndObject();
        } else {
            jGenerator.writeStartObject();
            jGenerator.writeFieldName(cfg.getId());
            mapper.writeValue(jGenerator, cfg.getValue());
            jGenerator.writeEndObject();
        }
    }

}
