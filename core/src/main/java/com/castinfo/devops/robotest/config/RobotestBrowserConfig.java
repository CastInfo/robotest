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
@JsonPropertyOrder({ "browserName",
                     "headLess",
                     "windowWidth",
                     "windowHeight",
                     "maximized",
                     "consoleLogLevel",
                     "proxyHost",
                     "proxyPort",
                     "noproxyfor",
                     "proxyUser",
                     "proxySecret" })
public class RobotestBrowserConfig {

    public static String PROXY_HOST_PORT_SEPARATOR = ":";

    @JsonProperty("browserName")
    private String browserName = "";

    @JsonProperty("headLess")
    private String headLess = "";

    @JsonProperty("windowWidth")
    private String windowWidth = "";

    @JsonProperty("windowHeight")
    private String windowHeight = "";

    @JsonProperty("maximized")
    private String maximized = "";

    @JsonProperty("consoleLogLevel")
    private String consoleLogLevel = "";

    @JsonProperty("proxyHost")
    private String proxyHost = "";

    @JsonProperty("proxyPort")
    private String proxyPort = "";

    @JsonProperty("noproxyfor")
    private String noproxyfor = "";

    @JsonProperty("proxyUser")
    private String proxyUser = "";

    @JsonProperty("proxySecret")
    private String proxySecret = "";

    /**
     * The Browser.
     *
     * @return browserName
     *
     */
    @JsonProperty("browserName")
    public String getBrowserName() {
        return browserName;
    }

    /**
     * The Browser.
     *
     * @param browserName
     *            browserName
     *
     */
    @JsonProperty("browserName")
    public void setBrowserName(final String browserName) {
        this.browserName = browserName;
    }

    /**
     * Getter method for headLess.
     *
     * @return the headLess
     */
    @JsonProperty("headLess")
    public String getHeadLess() {
        return headLess;
    }

    /**
     * Setter method for the headLess.
     *
     * @param headLess
     *            the headLess to set
     */
    @JsonProperty("headLess")
    public void setHeadLess(final String headLess) {
        this.headLess = headLess;
    }

    /**
     * Getter method for windowWidth.
     *
     * @return the windowWidth
     */
    @JsonProperty("windowWidth")
    public String getWindowWidth() {
        return windowWidth;
    }

    /**
     * Setter method for the windowWidth.
     *
     * @param windowWidth
     *            the windowWidth to set
     */
    @JsonProperty("windowWidth")
    public void setWindowWidth(final String windowWidth) {
        this.windowWidth = windowWidth;
    }

    /**
     * Getter method for windowHeight.
     *
     * @return the windowHeight
     */
    @JsonProperty("windowHeight")
    public String getWindowHeight() {
        return windowHeight;
    }

    /**
     * Setter method for the windowHeight.
     *
     * @param windowHeight
     *            the windowHeight to set
     */
    @JsonProperty("windowHeight")
    public void setWindowHeight(final String windowHeight) {
        this.windowHeight = windowHeight;
    }

    /**
     * The Browser maximized.
     *
     * @return maximized
     *
     */
    @JsonProperty("maximized")
    public String getMaximized() {
        return maximized;
    }

    /**
     * The Browser maximized.
     *
     * @param maximized
     *            maximized
     *
     */
    @JsonProperty("maximized")
    public void setMaximized(final String maximized) {
        this.maximized = maximized;
    }

    /**
     * The Browser Console Log Level.
     *
     * @return CONSOLE browserName log level
     *
     */
    @JsonProperty("consoleLogLevel")
    public String getConsoleLogLevel() {
        return consoleLogLevel;
    }

    /**
     * The Browser Console Log Level.
     *
     * @param level
     *            CONSOLE browserName log level
     *
     */
    @JsonProperty("consoleLogLevel")
    public void setConsoleLogLevel(final String level) {
        consoleLogLevel = level;
    }

    /**
     * Getter method for proxy host.
     *
     * @return the proxy host
     */
    @JsonProperty("proxyHost")
    public String getProxyHost() {
        return proxyHost;
    }

    /**
     * Setter method for the proxyHost.
     *
     * @param proxyHost
     *            the proxy Host to set
     */
    @JsonProperty("proxyHost")
    public void setProxyHost(final String proxyHost) {
        this.proxyHost = proxyHost;
    }

    /**
     * Getter of proxyPort
     *
     * @return the proxyPort
     */
    @JsonProperty("proxyPort")
    public String getProxyPort() {
        return proxyPort;
    }

    /**
     * Setter of proxyPort
     *
     * @param proxyPort
     *            the proxyPort to set
     */
    @JsonProperty("proxyPort")
    public void setProxyPort(final String proxyPort) {
        this.proxyPort = proxyPort;
    }

    /**
     * Obtain proxyHost:proxyPort uri
     *
     * @return uri
     */
    public String getProxyHostAndPort() {
        return getProxyHost() + PROXY_HOST_PORT_SEPARATOR + getProxyPort();
    }

    /**
     * Getter method for noproxyfor.
     *
     * @return the noproxyfor
     */
    @JsonProperty("noproxyfor")
    public String getNoproxyfor() {
        return noproxyfor;
    }

    /**
     * Setter method for the noproxyfor.
     *
     * @param noproxyfor
     *            the noproxyfor to set
     */
    @JsonProperty("noproxyfor")
    public void setNoproxyfor(final String noproxyfor) {
        this.noproxyfor = noproxyfor;
    }

    /**
     * Getter of proxyUser
     *
     * @return the proxyUser
     */
    @JsonProperty("proxyUser")
    public String getProxyUser() {
        return proxyUser;
    }

    /**
     * Setter of proxyUser
     *
     * @param proxyUser
     *            the proxyUser to set
     */
    @JsonProperty("proxyUser")
    public void setProxyUser(final String proxyUser) {
        this.proxyUser = proxyUser;
    }

    /**
     * Getter of proxySecret
     *
     * @return the proxySecret
     */
    @JsonProperty("proxySecret")
    public String getProxySecret() {
        return proxySecret;
    }

    /**
     * Setter of proxySecret
     *
     * @param proxySecret
     *            the proxySecret to set
     */
    @JsonProperty("proxySecret")
    public void setProxySecret(final String proxySecret) {
        this.proxySecret = proxySecret;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(browserName)
                                    .append(headLess)
                                    .append(windowWidth)
                                    .append(windowHeight)
                                    .append(maximized)
                                    .append(consoleLogLevel)
                                    .append(proxyHost)
                                    .append(proxyPort)
                                    .append(noproxyfor)
                                    .append(proxyUser)
                                    .append(proxySecret)
                                    .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RobotestBrowserConfig)) {
            return false;
        }
        RobotestBrowserConfig rhs = (RobotestBrowserConfig) other;
        return new EqualsBuilder().append(browserName, rhs.browserName)
                                  .append(headLess, rhs.headLess)
                                  .append(windowWidth, rhs.windowWidth)
                                  .append(windowHeight, rhs.windowHeight)
                                  .append(maximized, rhs.maximized)
                                  .append(consoleLogLevel, rhs.consoleLogLevel)
                                  .append(proxyHost, rhs.proxyHost)
                                  .append(proxyPort, rhs.proxyPort)
                                  .append(noproxyfor, rhs.noproxyfor)
                                  .append(proxyUser, rhs.proxyUser)
                                  .append(proxySecret, rhs.proxySecret)
                                  .isEquals();
    }

}
