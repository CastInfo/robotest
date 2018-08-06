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
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Jackson DTO for http connection config.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "generalTimeout", "proxyHost", "proxyPort", "noproxyfor", "proxyUser", "proxySecret" })
public class RobotestHttpConnConfig {

    public static String PROXY_HOST_PORT_SEPARATOR = ":";
    public static final int ONE_SECOND_IN_MILLIS = 1000;

    @JsonProperty("generalTimeout")
    private String generalTimeout = "";

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
     * The generalTimeout.
     *
     * @return generalTimeou
     *
     */
    @JsonProperty("generalTimeout")
    public String getGeneralTimeout() {
        return generalTimeout;
    }

    public long obtainGeneralTimeoutInSeconds() {
        long millis = Long.parseLong(generalTimeout);
        return millis / ONE_SECOND_IN_MILLIS;
    }

    /**
     * The generalTimeout.
     *
     * @param waitTimeout
     *            generalTimeout
     *
     */
    @JsonProperty("generalTimeout")
    public void setGeneralTimeout(final String waitTimeout) {
        generalTimeout = waitTimeout;
    }

    /**
     * have proxy
     *
     * @return true if proxy
     */
    public boolean haveProxyHost() {
        return StringUtils.isNotEmpty(getProxyHost());
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
    public String obtainProxyHostAndPort() {
        return getProxyHost() + PROXY_HOST_PORT_SEPARATOR + getProxyPort();
    }

    /**
     * proxy port int
     *
     * @return int
     */
    public int obtainIntegerProxyPort() {
        return Integer.parseInt(proxyPort);
    }

    /**
     * have non-proxy
     *
     * @return true if nonprxoy
     */
    public boolean haveNoproxyfor() {
        return StringUtils.isNotEmpty(getNoproxyfor());
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
     * have proxy user
     *
     * @return true if proxy user
     */
    public boolean haveProxyUser() {
        return StringUtils.isEmpty(getProxyUser());
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
        return new HashCodeBuilder().append(generalTimeout)
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
        if (!(other instanceof RobotestHttpConnConfig)) {
            return false;
        }
        RobotestHttpConnConfig rhs = (RobotestHttpConnConfig) other;
        return new EqualsBuilder().append(generalTimeout,
                                          rhs.generalTimeout)
                                  .append(proxyHost,
                                          rhs.proxyHost)
                                  .append(proxyPort,
                                          rhs.proxyPort)
                                  .append(noproxyfor,
                                          rhs.noproxyfor)
                                  .append(proxyUser,
                                          rhs.proxyUser)
                                  .append(proxySecret,
                                          rhs.proxySecret)
                                  .isEquals();
    }
}
