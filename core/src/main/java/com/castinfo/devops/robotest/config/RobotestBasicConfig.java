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
package com.castinfo.devops.robotest.config;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Jackson DTO for basic config.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "env", "reportFilePath", "http", "browser", "docker", "browserStack" })
public class RobotestBasicConfig {

    @JsonProperty("env")
    private String env = "";

    @JsonProperty("browser")
    private RobotestBrowserConfig browser;

    @JsonProperty("http")
    private RobotestHttpConnConfig http;

    @JsonProperty("reportFilePath")
    private String reportFilePath = "";

    @JsonProperty("docker")
    private DockerConfig docker;

    @JsonProperty("browserStack")
    private BrowserStackConfig browserStack;

    /**
     * The Browser.
     *
     * @return browser
     *
     */
    @JsonProperty("browser")
    public RobotestBrowserConfig getBrowser() {
        return browser;
    }

    /**
     * The Browser.
     *
     * @param browser
     *            browser
     *
     */
    @JsonProperty("browser")
    public void setBrowser(final RobotestBrowserConfig browser) {
        this.browser = browser;
    }

    /**
     * The http config.
     *
     * @return http config
     *
     */
    @JsonProperty("http")
    public RobotestHttpConnConfig getHttp() {
        return http;
    }

    /**
     * The http config.
     *
     * @param http
     *            http config
     *
     */
    @JsonProperty("http")
    public void setHttp(final RobotestHttpConnConfig http) {
        this.http = http;
    }

    /**
     * The env.
     *
     * @return env
     *
     */
    @JsonProperty("env")
    public String getEnv() {
        return env;
    }

    /**
     * The env.
     *
     * @param env
     *            env
     *
     */
    @JsonProperty("env")
    public void setEnv(final String env) {
        this.env = env;
    }

    /**
     * The Reportpath.
     *
     * @return reportpath
     */
    @JsonProperty("reportFilePath")
    public String getReportFilePath() {
        return reportFilePath;
    }

    /**
     * The reportFilePath.
     *
     * @param reportFilePath
     *            reportFilePath
     *
     */
    @JsonProperty("reportFilePath")
    public void setReportFilePath(final String reportFilePath) {
        this.reportFilePath = reportFilePath;
    }

    /**
     * Docker cfg.
     *
     * @return docker
     */
    @JsonProperty("docker")
    public DockerConfig getDocker() {
        return docker;
    }

    /**
     * Docker cfg.
     *
     * @param docker
     *            cfg docker
     */
    @JsonProperty("docker")
    public void setDocker(final DockerConfig docker) {
        this.docker = docker;
    }

    /**
     * BrowserStack cfg.
     *
     * @return browserStack cfg
     */
    @JsonProperty("browserStack")
    public BrowserStackConfig getBrowserStack() {
        return browserStack;
    }

    /**
     * BrowserStack cfg.
     *
     * @param browserStack
     *            cfg browserStack
     */
    @JsonProperty("browserStack")
    public void setBrowserStack(final BrowserStackConfig browserStack) {
        this.browserStack = browserStack;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(env)
                                    .append(reportFilePath)
                                    .append(http)
                                    .append(browser)
                                    .append(docker)
                                    .append(browserStack)
                                    .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RobotestBasicConfig)) {
            return false;
        }
        RobotestBasicConfig rhs = (RobotestBasicConfig) other;
        return new EqualsBuilder().append(env, rhs.env)
                                  .append(reportFilePath, rhs.reportFilePath)
                                  .append(http, rhs.http)
                                  .append(browser, rhs.browser)
                                  .append(docker, rhs.docker)
                                  .append(browserStack, rhs.browserStack)
                                  .isEquals();
    }

}
