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
 * Jackson DTO for Docker config included in base config.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "platform", "device", "login" })
public class BrowserStackConfig {

    /**
     * The platform .
     */
    @JsonProperty("platform")
    private String platform = "";

    /**
     * The device.
     */
    @JsonProperty("device")
    private String device = "";

    /**
     * The login.
     */
    @JsonProperty("login")
    private String login = "";

    /**
     * The access key.
     */
    private String accessKey = "";

    /**
     * Getter method for platform.
     *
     * @return the platform
     */
    @JsonProperty("platform")
    public String getPlatform() {
        return platform;
    }

    /**
     * Setter method for the platform.
     *
     * @param platform
     *            the platform to set
     */
    @JsonProperty("platform")
    public void setPlatform(final String platform) {
        this.platform = platform;
    }

    /**
     * Getter method for device.
     *
     * @return the device
     */
    @JsonProperty("device")
    public String getDevice() {
        return device;
    }

    /**
     * Setter method for the device.
     *
     * @param device
     *            the device to set
     */
    @JsonProperty("device")
    public void setDevice(final String device) {
        this.device = device;
    }

    /**
     * Getter method for login.
     *
     * @return the login
     */
    @JsonProperty("login")
    public String getLogin() {
        return login;
    }

    /**
     * Setter method for the login.
     *
     * @param login
     *            the login to set
     */
    @JsonProperty("login")
    public void setLogin(final String login) {
        this.login = login;
    }

    /**
     * Getter method for accessKey.
     *
     * @return the accessKey
     */
    public String getAccessKey() {
        return accessKey;
    }

    /**
     * Setter method for the password.
     *
     * @param accessKey
     *            the accessKey to set
     */
    public void setAccessKey(final String accessKey) {
        this.accessKey = accessKey;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(device)
                                    .append(platform)
                                    .append(login)
                                    .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof BrowserStackConfig)) {
            return false;
        }
        BrowserStackConfig rhs = (BrowserStackConfig) other;
        return new EqualsBuilder().append(device,
                                          rhs.device)
                                  .append(platform,
                                          rhs.platform)
                                  .append(login,
                                          rhs.login)
                                  .isEquals();
    }

}
