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

import java.util.Map;

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
@JsonPropertyOrder({ "host", "image", "idContainer", "containerName", "networkMode", "hub", "expose_port",
                     "expose_debug_port", "chromeImageTag", "chromeDebugImageTag", "firefoxImageTag",
                     "firefoxDebugImageTag", "dockerRegistryUrl", "dockerRegistryUser", "dockerRegistryEmail" })
public class DockerConfig {

    /**
     * The Host .
     */
    @JsonProperty("host")
    private String host = null;
    /**
     * The chromeImageTag .
     */
    @JsonProperty("chromeImageTag")
    private String chromeImageTag = null;
    /**
     * The chrome_debug_image_tag .
     */
    @JsonProperty("chromeDebugImageTag")
    private String chromeDebugImageTag = null;
    /**
     * The firefoxImageTag .
     */
    @JsonProperty("firefoxImageTag")
    private String firefoxImageTag = null;
    /**
     * The firefoxDebugImageTag .
     */
    @JsonProperty("firefoxDebugImageTag")
    private String firefoxDebugImageTag = null;
    /**
     * The Image.
     */
    @JsonProperty("image")
    private String image = null;
    /**
     * The idContainer .
     */
    @JsonProperty("idContainer")
    private String idContainer = null;
    /**
     * The Hub .
     */
    @JsonProperty("hub")
    private String hub = null;
    /**
     * The Expose_port .
     */
    @JsonProperty("expose_port")
    private String exposePort = null;
    /**
     * The Expose_debug_port .
     */
    @JsonProperty("expose_debug_port")
    private String exposeDebugPort = null;
    /**
     * The certs path.
     */
    @JsonProperty("certsPath")
    private String certsPath = null;
    /**
     * The containerName.
     */
    @JsonProperty("containerName")
    private String containerName = null;

    /**
     * The network mode.
     */
    @JsonProperty("networkMode")
    private String networkMode = null;

    @JsonProperty("dockerRegistryUrl")
    private String dockerRegistryUrl = null;
    @JsonProperty("dockerRegistryUser")
    private String dockerRegistryUser = null;
    private String dockerRegistrySecret = null;
    @JsonProperty("dockerRegistryEmail")
    private String dockerRegistryEmail = null;

    private Map<String, String> labels = null;

    /**
     * The Host .
     *
     * @return host
     */
    @JsonProperty("host")
    public String getHost() {
        return this.host;
    }

    /**
     * The Host .
     *
     * @param host
     *            host
     */
    @JsonProperty("host")
    public void setHost(final String host) {
        this.host = host;
    }

    /**
     * The Image .
     *
     * @return image
     */
    @JsonProperty("image")
    public String getImage() {
        return this.image;
    }

    /**
     * The Image .
     *
     * @param image
     *            image
     *
     */
    @JsonProperty("image")
    public void setImage(final String image) {
        this.image = image;
    }

    /**
     * The idContainer .
     *
     * @return idContainer
     */
    @JsonProperty("idContainer")
    public String getIdContainer() {
        return this.idContainer;
    }

    /**
     * The idContainer .
     *
     * @param idContainer
     *            idContainer
     */
    @JsonProperty("idContainer")
    public void setIdContainer(final String idContainer) {
        this.idContainer = idContainer;
    }

    /**
     * The containerName getter.
     *
     * @return continerName value.
     */
    @JsonProperty("containerName")
    public String getContainerName() {
        return this.containerName;
    }

    /**
     * The containerName setter.
     *
     * @param containerName
     *            value.
     */
    @JsonProperty("containerName")
    public void setContainerName(final String containerName) {
        this.containerName = containerName;
    }

    /**
     * The networkMode getter.
     *
     * @return the networkMode value.
     */
    public String getNetworkMode() {
        return this.networkMode;
    }

    /**
     * The networkMode setter.
     *
     * @param networkMode
     *            the networkMode to set
     */
    public void setNetworkMode(final String networkMode) {
        this.networkMode = networkMode;
    }

    /**
     * Getter method for dockerRegistryUrl.
     *
     * @return the dockerRegistryUrl
     */
    @JsonProperty("dockerRegistryUrl")
    public String getDockerRegistryUrl() {
        return this.dockerRegistryUrl;
    }

    /**
     * Setter method for the dockerRegistryUrl.
     *
     * @param dockerRegistryUrl
     *            the dockerRegistryUrl to set
     */
    @JsonProperty("dockerRegistryUrl")
    public void setDockerRegistryUrl(final String dockerRegistryUrl) {
        this.dockerRegistryUrl = dockerRegistryUrl;
    }

    /**
     * Getter method for dockerRegistryUser.
     *
     * @return the dockerRegistryUser
     */
    @JsonProperty("dockerRegistryUser")
    public String getDockerRegistryUser() {
        return this.dockerRegistryUser;
    }

    /**
     * Setter method for the dockerRegistryUser.
     *
     * @param dockerRegistryUser
     *            the dockerRegistryUser to set
     */
    @JsonProperty("dockerRegistryUser")
    public void setDockerRegistryUser(final String dockerRegistryUser) {
        this.dockerRegistryUser = dockerRegistryUser;
    }

    /**
     * Getter method for dockerRegistrySecret.
     *
     * @return the dockerRegistrySecret
     */
    public String getDockerRegistrySecret() {
        return this.dockerRegistrySecret;
    }

    /**
     * Setter method for the dockerRegistrySecret.
     *
     * @param dockerRegistrySecret
     *            the dockerRegistrySecret to set
     */
    public void setDockerRegistrySecret(final String dockerRegistrySecret) {
        this.dockerRegistrySecret = dockerRegistrySecret;
    }

    /**
     * Getter method for dockerRegistryEmail.
     *
     * @return the dockerRegistryEmail
     */
    @JsonProperty("dockerRegistryEmail")
    public String getDockerRegistryEmail() {
        return this.dockerRegistryEmail;
    }

    /**
     * Setter method for the dockerRegistryEmail.
     *
     * @param dockerRegistryEmail
     *            the dockerRegistryEmail to set
     */
    @JsonProperty("dockerRegistryEmail")
    public void setDockerRegistryEmail(final String dockerRegistryEmail) {
        this.dockerRegistryEmail = dockerRegistryEmail;
    }

    /**
     * The labels getter.
     *
     * @return the labels value.
     */
    public Map<String, String> getLabels() {
        return this.labels;
    }

    /**
     * The labels setter.
     *
     * @param labels
     *            the labels to set
     */
    public void setLabels(final Map<String, String> labels) {
        this.labels = labels;
    }

    /**
     * The Hub .
     *
     * @return hub
     */
    @JsonProperty("hub")
    public String getHub() {
        return this.hub;
    }

    /**
     * The Hub .
     *
     * @param hub
     *            hub
     *
     */
    @JsonProperty("hub")
    public void setHub(final String hub) {
        this.hub = hub;
    }

    /**
     * The Expose_port .
     *
     * @return exposePort
     */
    @JsonProperty("expose_port")
    public String getExposePort() {
        return this.exposePort;
    }

    /**
     * The Expose_port .
     *
     * @param exposePort
     *            exposePort
     */
    @JsonProperty("expose_port")
    public void setExposePort(final String exposePort) {
        this.exposePort = exposePort;
    }

    /**
     * The Expose_debug_port .
     *
     * @return exposeDebugPort
     */
    @JsonProperty("expose_debug_port")
    public String getExposeDebugPort() {
        return this.exposeDebugPort;
    }

    /**
     * The Expose_debug_port .
     *
     * @param exposeDebugPort
     *            exposeDebugPort
     */
    @JsonProperty("expose_debug_port")
    public void setExposeDebugPort(final String exposeDebugPort) {
        this.exposeDebugPort = exposeDebugPort;
    }

    /**
     * The certsPath .
     *
     * @return certsPath
     */
    @JsonProperty("certsPath")
    public String getCertsPath() {
        return this.certsPath;
    }

    /**
     * The certsPath .
     *
     * @param certsPath
     *            certsPath
     */
    @JsonProperty("certsPath")
    public void setCertsPath(final String certsPath) {
        this.certsPath = certsPath;
    }

    /**
     * Getter method for chromeImageTag.
     *
     * @return the chromeImageTag
     */
    @JsonProperty("chromeImageTag")
    public String getChromeImageTag() {
        return this.chromeImageTag;
    }

    /**
     * Setter method for the chromeImageTag.
     *
     * @param chromeImageTag
     *            the chromeImageTag to set
     */
    @JsonProperty("chromeImageTag")
    public void setChromeImageTag(final String chromeImageTag) {
        this.chromeImageTag = chromeImageTag;
    }

    /**
     * Getter method for chrome_debug_image_tag.
     *
     * @return the chrome_debug_image_tag
     */
    @JsonProperty("chromeDebugImageTag")
    public String getChromeDebugImageTag() {
        return this.chromeDebugImageTag;
    }

    /**
     * Setter method for the chrome_debug_image_tag.
     *
     * @param chromeDebugImageTag
     *            the chromeDebugImageTag to set
     */
    @JsonProperty("chromeDebugImageTag")
    public void setChromeDebugImageTag(final String chromeDebugImageTag) {
        this.chromeDebugImageTag = chromeDebugImageTag;
    }

    /**
     * Getter method for firefoxImageTag.
     *
     * @return the firefoxImageTag
     */
    @JsonProperty("firefoxImageTag")
    public String getFirefoxImageTag() {
        return this.firefoxImageTag;
    }

    /**
     * Setter method for the firefoxImageTag.
     *
     * @param firefoxImageTag
     *            the firefoxImageTag to set
     */
    @JsonProperty("firefoxImageTag")
    public void setFirefoxImageTag(final String firefoxImageTag) {
        this.firefoxImageTag = firefoxImageTag;
    }

    /**
     * Getter method for firefoxDebugImageTag.
     *
     * @return the firefoxDebugImageTag
     */
    @JsonProperty("firefoxDebugImageTag")
    public String getFirefoxDebugImageTag() {
        return this.firefoxDebugImageTag;
    }

    /**
     * Setter method for the firefoxDebugImageTag.
     *
     * @param firefoxDebugImageTag
     *            the firefoxDebugImageTag to set
     */
    @JsonProperty("firefoxDebugImageTag")
    public void setFirefoxDebugImageTag(final String firefoxDebugImageTag) {
        this.firefoxDebugImageTag = firefoxDebugImageTag;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.host).append(this.image).append(this.idContainer)
                                    .append(this.containerName).append(this.networkMode).append(this.hub)
                                    .append(this.exposePort).append(this.exposeDebugPort).append(this.certsPath)
                                    .append(this.dockerRegistryUrl).append(this.dockerRegistryUser)
                                    .append(this.dockerRegistrySecret).append(this.dockerRegistryEmail)
                                    .append(this.chromeImageTag).append(this.chromeDebugImageTag)
                                    .append(this.firefoxImageTag).append(this.firefoxDebugImageTag).toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof DockerConfig)) {
            return false;
        }
        DockerConfig rhs = (DockerConfig) other;
        return new EqualsBuilder().append(this.host, rhs.host).append(this.image, rhs.image)
                                  .append(this.idContainer, rhs.idContainer)
                                  .append(this.containerName, rhs.containerName)
                                  .append(this.networkMode, rhs.networkMode).append(this.hub, rhs.hub)
                                  .append(this.exposePort, rhs.exposePort)
                                  .append(this.exposeDebugPort, rhs.exposeDebugPort)
                                  .append(this.certsPath, rhs.certsPath).append(this.chromeImageTag, rhs.chromeImageTag)
                                  .append(this.dockerRegistryUrl, rhs.dockerRegistryUrl)
                                  .append(this.dockerRegistryUser, rhs.dockerRegistryUser)
                                  .append(this.dockerRegistrySecret, rhs.dockerRegistrySecret)
                                  .append(this.dockerRegistryEmail, rhs.dockerRegistryEmail)
                                  .append(this.chromeDebugImageTag, rhs.chromeDebugImageTag)
                                  .append(this.firefoxImageTag, rhs.firefoxImageTag)
                                  .append(this.firefoxDebugImageTag, rhs.firefoxDebugImageTag).isEquals();
    }

}
