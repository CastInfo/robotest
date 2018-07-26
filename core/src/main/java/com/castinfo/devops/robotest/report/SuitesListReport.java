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

import com.castinfo.devops.robotest.RobotestException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * Generates a index file with al suite report files.
 *
 */
public class SuitesListReport {

    private File suitesListFile;
    private ObjectMapper mapper = new ObjectMapper();
    private SuiteListMapper suiteListMapper = new SuiteListMapper();
    private DefaultPrettyPrinter printer = new DefaultPrettyPrinter();

    /**
     * Creates Suite List of robotest reports, creating the JSON file.
     *
     * @param reportFile
     *            Report file.
     * @throws RobotestException
     *             Errors, probably IO.
     */
    public SuitesListReport(final File reportFile) throws RobotestException {
        try {
            suitesListFile = reportFile;
            mapper.writeValue(suitesListFile, suiteListMapper);
        } catch (IOException e) {
            throw new RobotestException("ERROR CREATING SUITE LIST REPORT", e);
        }
    }

    /**
     * Getter method for suitesListFile.
     *
     * @return the suitesListFile
     */
    public File getSuitesListFile() {
        return suitesListFile;
    }

    /**
     * Setter method for the suitesListFile.
     *
     * @param suitesListFile
     *            the suitesListFile to set
     */
    public void setSuitesListFile(final File suitesListFile) {
        this.suitesListFile = suitesListFile;
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
     * Getter method for suiteListMapper.
     *
     * @return the suiteListMapper
     */
    public SuiteListMapper getSuiteListMapper() {
        return suiteListMapper;
    }

    /**
     * Setter method for the suiteListMapper.
     *
     * @param suiteListMapper
     *            the suiteListMapper to set
     */
    public void setSuiteListMapper(final SuiteListMapper suiteListMapper) {
        this.suiteListMapper = suiteListMapper;
    }

    /**
     * Getter method for printer.
     *
     * @return the printer
     */
    public DefaultPrettyPrinter getPrinter() {
        return printer;
    }

    /**
     * Setter method for the printer.
     *
     * @param printer
     *            the printer to set
     */
    public void setPrinter(final DefaultPrettyPrinter printer) {
        this.printer = printer;
    }

    /**
     * Append a suite file in the list.
     *
     * @param suiteReportFileName
     *            name of suite report
     * @throws RobotestException
     *             io errors.
     */
    public void appendToSuiteListReport(final String suiteReportFileName) throws RobotestException {
        try {
            final SuiteListMapper suitesList = getSuiteListReportContent();
            suitesList.getSuites().add(suiteReportFileName);
            ObjectWriter writer = mapper.writer(printer);
            writer.writeValue(suitesListFile, suitesList);
        } catch (IOException e) {
            throw new RobotestException("ERROR APPEND SUITE LIST REPORT", e);
        }
    }

    /**
     * Load a POJO representation of suite list file content.
     *
     * @return SuiteListMapper
     * @throws RobotestException
     *             io errors.
     */
    public SuiteListMapper getSuiteListReportContent() throws RobotestException {
        try {
            return mapper.readValue(suitesListFile, SuiteListMapper.class);
        } catch (IOException e) {
            throw new RobotestException("ERROR GET SUITE LIST REPORT CONTENT", e);
        }
    }

}
