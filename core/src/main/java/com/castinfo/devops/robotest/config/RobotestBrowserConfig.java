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
@JsonPropertyOrder({ "browserName", "headLess", "windowWidth", "windowHeight", "maximized", "consoleLogLevel",
                     "proxy" })
public class RobotestBrowserConfig {

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

    @JsonProperty("proxy")
    private String proxy = "";

    /**
     * The Browser.
     *
     * @return browserName
     *
     */
    @JsonProperty("browserName")
    public String getBrowserName() {
        return this.browserName;
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
        return this.headLess;
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
        return this.windowWidth;
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
        return this.windowHeight;
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
        return this.maximized;
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
        return this.consoleLogLevel;
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
        this.consoleLogLevel = level;
    }

    /**
     * Getter method for proxy.
     *
     * @return the proxy
     */
    @JsonProperty("proxy")
    public String getProxy() {
        return this.proxy;
    }

    /**
     * Setter method for the proxy.
     *
     * @param proxy
     *            the proxy to set
     */
    @JsonProperty("proxy")
    public void setProxy(final String proxy) {
        this.proxy = proxy;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.browserName).append(this.headLess).append(this.windowWidth)
                                    .append(this.windowHeight).append(this.maximized).append(this.consoleLogLevel)
                                    .append(this.proxy).toHashCode();
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
        return new EqualsBuilder().append(this.browserName, rhs.browserName).append(this.headLess, rhs.headLess)
                                  .append(this.windowWidth, rhs.windowWidth).append(this.windowHeight, rhs.windowHeight)
                                  .append(this.maximized, rhs.maximized)
                                  .append(this.consoleLogLevel, rhs.consoleLogLevel).append(this.proxy, rhs.proxy)
                                  .isEquals();
    }

}
