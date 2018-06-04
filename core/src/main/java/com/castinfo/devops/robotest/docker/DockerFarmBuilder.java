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
package com.castinfo.devops.robotest.docker;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.castinfo.devops.robotest.RobotestException;
import com.castinfo.devops.robotest.config.DockerConfig;
import com.castinfo.devops.robotest.selenium.SeleniumBrowser;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.Ports.Binding;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DefaultDockerClientConfig.Builder;
import com.github.dockerjava.core.DockerClientBuilder;

/**
 * This class build and run Selenium Docker Images in a Docker machine.
 *
 * @author md.ruiz
 *
 */
public class DockerFarmBuilder {

    private static final String BRIDGE_NETWORK_METHOD = "bridge";
    private static final String ROBOTEST_CONTAINER_PREFIX = "robotest_";
    private static final Logger LOG = LoggerFactory.getLogger(DockerFarmBuilder.class);
    private static final int ONE_SECOND_IN_MILLIS = 1000;
    private static final String SEL_SERVER_END = "Selenium Server is up and running";

    private static Map<String, Boolean> pullableDockerImagesStatus = new HashMap<>();
    private static Object pullingBlocker = new Object();

    private DockerClient docker = null;
    private DockerConfig dockerBaseCfg = null;
    private AtomicInteger dockerInstancesNumber = new AtomicInteger(0);

    /**
     * Constructor.
     *
     * @param dockerBaseCfg
     *            docker Base Cfg
     *
     */
    public DockerFarmBuilder(final DockerConfig dockerBaseCfg) {
        this.setDockerBaseCfg(dockerBaseCfg);
        if (DockerFarmBuilder.getPullableDockerImagesStatus().isEmpty()) {
            DockerFarmBuilder.getPullableDockerImagesStatus().put(this.dockerBaseCfg.getChromeImageTag(),
                                                                  Boolean.FALSE);
            DockerFarmBuilder.getPullableDockerImagesStatus().put(this.dockerBaseCfg.getChromeDebugImageTag(),
                                                                  Boolean.FALSE);
            DockerFarmBuilder.getPullableDockerImagesStatus().put(this.dockerBaseCfg.getFirefoxImageTag(),
                                                                  Boolean.FALSE);
            DockerFarmBuilder.getPullableDockerImagesStatus().put(this.dockerBaseCfg.getFirefoxDebugImageTag(),
                                                                  Boolean.FALSE);
        }
    }

    /**
     * Getter method for dockerBaseCfg.
     *
     * @return the dockerBaseCfg
     */
    public DockerConfig getDockerBaseCfg() {
        return this.dockerBaseCfg;
    }

    /**
     * Setter method for the dockerBaseCfg.
     *
     * @param dockerBaseCfg
     *            the dockerBaseCfg to set
     */
    public void setDockerBaseCfg(final DockerConfig dockerBaseCfg) {
        this.dockerBaseCfg = dockerBaseCfg;
    }

    /**
     * Getter method for docker.
     *
     * @return the docker
     */
    public DockerClient getDockerClient() {
        return this.docker;
    }

    /**
     * Setter method for docker.
     *
     * @param dockerCli
     *            client docker
     *
     */
    public void setDockerClient(final DockerClient dockerCli) {
        this.docker = dockerCli;
    }

    /**
     * Mandatory to provide connetion to docker host.
     *
     * @throws RobotestException
     *             if no connetion to docker provided
     */
    public void connectDockerClient() throws RobotestException {
        if (StringUtils.isEmpty(this.dockerBaseCfg.getHost())) {
            throw new RobotestException("DOCKER CONN IS REQUIRED, REVISE BASE CONFIG");
        }
        Builder builder = DefaultDockerClientConfig.createDefaultConfigBuilder()
                                                   .withDockerHost(this.dockerBaseCfg.getHost());
        if (StringUtils.isNotEmpty(this.dockerBaseCfg.getCertsPath())) {
            builder = builder.withDockerTlsVerify(true).withDockerCertPath(this.dockerBaseCfg.getCertsPath());
        }
        this.setDockerClient(DockerClientBuilder.getInstance(builder).build());
        DockerFarmBuilder.LOG.info("DOCKER CLIENT RUNING: {}", this.getDockerClient().infoCmd().exec());
    }

    /**
     * Getter method for pullableDockerImagesStatus.
     *
     * @return the pullableDockerImagesStatus
     */
    protected static Map<String, Boolean> getPullableDockerImagesStatus() {
        return DockerFarmBuilder.pullableDockerImagesStatus;
    }

    /**
     * Create Selenium Docker node.
     *
     * @param browser
     *            the browser type
     *
     * @return The Docker object.
     * @throws RobotestException
     *             errors in creation.
     */
    public DockerConfig createBrowser(final String browser) throws RobotestException {
        DockerConfig dockerBrowser = new DockerConfig();
        this.generateContainerName(dockerBrowser);
        try {
            this.resolveImageAndCreateBrowserContainer(browser, dockerBrowser);
            this.resolveBrowserHub(dockerBrowser);
        } catch (Exception e) {
            try {
                this.stopNode(dockerBrowser.getContainerName());
            } catch (Exception e2) {
                LOG.error("ERROR STOPPING CONTAINER", e2);
            }
            if (e instanceof RobotestException) {
                throw e;
            }
            throw new RobotestException("UNEXEPTED ERROR CREATING CONTAINER", e);
        }
        return dockerBrowser;
    }

    /**
     * Resolve Docker image tag and create de associated browser.
     *
     * @param browser
     *            browser type FIREFOX or CHROME.
     * @param dockerBrowser
     *            config Docker instance created in Docker.
     * @throws RobotestException
     *             errors creating image browser container.
     */
    public void resolveImageAndCreateBrowserContainer(final String browser,
                                                      final DockerConfig dockerBrowser) throws RobotestException {
        dockerBrowser.setExposePort("4444");
        if ("true".equalsIgnoreCase(this.dockerBaseCfg.getExposeDebugPort())) {
            dockerBrowser.setExposeDebugPort("5900");
        }
        if (null == this.dockerBaseCfg.getLabels() || this.dockerBaseCfg.getLabels().isEmpty()) {
            dockerBrowser.setLabels(new HashMap<>());
        } else {
            dockerBrowser.setLabels(this.dockerBaseCfg.getLabels());
        }
        if (StringUtils.isEmpty(this.dockerBaseCfg.getNetworkMode())) {
            dockerBrowser.setNetworkMode(BRIDGE_NETWORK_METHOD);
        } else {
            dockerBrowser.setNetworkMode(this.dockerBaseCfg.getNetworkMode());
        }
        if (SeleniumBrowser.CHROME.name().equalsIgnoreCase(browser)) {
            dockerBrowser.setImage(this.dockerBaseCfg.getChromeImageTag());
        } else if (SeleniumBrowser.FIREFOX.name().equalsIgnoreCase(browser)) {
            dockerBrowser.setImage(this.dockerBaseCfg.getFirefoxImageTag());
        } else {
            throw new RobotestException("BROWSER DON'T HAVE DOCKER IMAGE TAG:" + browser);
        }
        this.pullImage(dockerBrowser.getImage());
        dockerBrowser.setIdContainer(this.createBrowserContainer(dockerBrowser));

    }

    /**
     * Generate name of robotest with format:
     * ${ROBOTEST_CONTAINER_PREFIX}${dockerBaseCfg.getContainerName()}${lastRobotestContainerNumber++}.
     *
     * @param dockerBrowser
     *            browser config.
     */
    public void generateContainerName(final DockerConfig dockerBrowser) {
        StringBuilder containerName = new StringBuilder(ROBOTEST_CONTAINER_PREFIX);
        if (StringUtils.isNotEmpty(this.dockerBaseCfg.getContainerName())) {
            containerName.append(this.dockerBaseCfg.getContainerName());
        }
        containerName.append(this.dockerInstancesNumber.incrementAndGet());
        dockerBrowser.setContainerName(containerName.toString());
    }

    /**
     * Internal Docker image pull, build and run Selenium Image.
     *
     * @param dockerBrowser
     *            config Docker instance created
     * @return Container ID
     * @throws RobotestException
     */
    private String createBrowserContainer(final DockerConfig dockerBrowser) throws RobotestException {
        CreateContainerResponse containerResp = null;
        try (CreateContainerCmd containerCmd = this.getDockerClient().createContainerCmd(dockerBrowser.getImage());
             DockerContainerCallback createCallback = new DockerContainerCallback(DockerFarmBuilder.SEL_SERVER_END)) {
            DockerFarmBuilder.LOG.info("DOCKER CREATE CONTAINER FROM: {}", dockerBrowser);
            containerCmd.withName(dockerBrowser.getContainerName());
            containerCmd.withLabels(dockerBrowser.getLabels());
            containerCmd.withNetworkMode(dockerBrowser.getNetworkMode());
            Ports portBindings = new Ports();
            int exposedPort = Integer.parseInt(dockerBrowser.getExposePort());
            ExposedPort exposePort = ExposedPort.tcp(exposedPort);
            containerCmd.withExposedPorts(exposePort);
            portBindings.bind(exposePort, Binding.bindPort(exposedPort));
            if (StringUtils.isNotEmpty(dockerBrowser.getExposeDebugPort())) {
                exposedPort = Integer.parseInt(dockerBrowser.getExposeDebugPort());
                exposePort = ExposedPort.tcp(exposedPort);
                containerCmd.withExposedPorts(exposePort);
                portBindings.bind(exposePort, Binding.bindPort(exposedPort));
            }
            containerCmd.withPortBindings(portBindings);
            Volume sharedMemory = new Volume("/dev/shm");
            containerCmd.withVolumes(sharedMemory).withBinds(new Bind("/dev/shm", sharedMemory));
            containerResp = containerCmd.exec();
            this.getDockerClient().startContainerCmd(containerResp.getId()).exec();
            this.getDockerClient().logContainerCmd(containerResp.getId()).withStdOut(true).withStdErr(true)
                .withTailAll().withFollowStream(true).exec(createCallback);
            do {
                Thread.sleep(DockerFarmBuilder.ONE_SECOND_IN_MILLIS);
            } while (!createCallback.isServerSeleniumLoaded());
            DockerFarmBuilder.LOG.info("DOCKER NODE UP&RUNING: {}", containerResp.getId());
        } catch (InterruptedException | IOException e) {
            if (null != containerResp) {
                this.stopNode(containerResp.getId());
            }
            throw new RobotestException("ERROR DOCKER ROBOTEST", e);
        }
        return containerResp.getId();
    }

    /**
     * Pull image in docker host only if not already pulled.
     *
     * @param imageName
     *            name of image.
     * @throws RobotestException
     *             Error pulling Docker image.
     */
    private void pullImage(final String imageName) throws RobotestException {
        synchronized (DockerFarmBuilder.pullingBlocker) {
            if (Boolean.FALSE.equals(DockerFarmBuilder.getPullableDockerImagesStatus().get(imageName))) {
                try (DockerBasicImagePullResultCallback pullCallback = new DockerBasicImagePullResultCallback()) {
                    DockerFarmBuilder.LOG.info("DOCKER PULL INIT: {}", imageName);
                    this.getDockerClient().pullImageCmd(imageName).exec(pullCallback).awaitCompletion();
                    DockerFarmBuilder.getPullableDockerImagesStatus().put(imageName, Boolean.TRUE);
                } catch (InterruptedException | IOException e) {
                    throw new RobotestException("ERROR DOCKER PULL " + imageName, e);
                }
            } else {
                DockerFarmBuilder.LOG.info("DOCKER PULL END: {}", imageName);
            }
        }
    }

    /**
     * Selenium HUB port will not be exposed to avoid port conflicts, real ports and host used will be returned by this
     * method.
     *
     * @param dockerBrowser
     *            docker resolved config
     * @throws RobotestException
     *             Docker unix socket conn needs public host configuration
     */
    public void resolveBrowserHub(final DockerConfig dockerBrowser) throws RobotestException {
        DockerFarmBuilder.LOG.info("RESOLVING HUB FOR CONTAINER: {}", dockerBrowser.getIdContainer());
        if (!BRIDGE_NETWORK_METHOD.equalsIgnoreCase(dockerBrowser.getNetworkMode())) {
            InspectContainerResponse contenedor = this.getDockerClient()
                                                      .inspectContainerCmd(dockerBrowser.getIdContainer()).exec();
            ExposedPort expPort = ExposedPort.tcp(Integer.parseInt(dockerBrowser.getExposePort()));
            dockerBrowser.setExposePort(contenedor.getNetworkSettings().getPorts().getBindings()
                                                  .get(expPort)[0].getHostPortSpec());
        } else {
            dockerBrowser.setExposePort("4444");
        }
        if (StringUtils.isEmpty(this.dockerBaseCfg.getHub())) {
            // dockerBrowser.setHub(dockerBrowser.getIdContainer());
            dockerBrowser.setHub(dockerBrowser.getContainerName());
        } else {
            dockerBrowser.setHub(this.dockerBaseCfg.getHub());
        }
        dockerBrowser.setHub(String.format("http://%1$s:%2$s/wd/hub", dockerBrowser.getHub(),
                                           dockerBrowser.getExposePort()));
        if (this.uriValidator(dockerBrowser.getHub(), "http")) {
            DockerFarmBuilder.LOG.info("DOCKER RESOLVED HUB : {}", dockerBrowser.getHub());
        } else {
            throw new RobotestException("DOCKER CONN URI NOT VALID, REVISE CFG: " + this.dockerBaseCfg.getHost());
        }

    }

    /**
     * Utility method to valite URIs
     *
     * @param uri
     *            the string contain the uri
     * @param validSchemes
     *            valid schema tipes
     * @return
     */
    private boolean uriValidator(final String uri, final String... validSchemes) {
        boolean isValid;
        final URI uriObj;
        try {
            uriObj = new URI(uri);
            isValid = Arrays.asList(validSchemes).contains(uriObj.getScheme());
        } catch (Exception e) {
            isValid = false;
        }
        return isValid;
    }

    /**
     * Stop docker node.
     *
     * @param idContainer
     *            Docker container id.
     */
    public void stopNode(final String idContainer) {
        DockerFarmBuilder.LOG.info("TRY TO STOP DOCKER CONTAINER: {}", idContainer);
        this.getDockerClient().stopContainerCmd(idContainer).exec();
        DockerFarmBuilder.LOG.info("TRY TO REMOVE DOCKER CONTAINER: {}", idContainer);
        this.getDockerClient().removeContainerCmd(idContainer).exec();
        DockerFarmBuilder.LOG.info("STOPED AND REMOVED DOCKER CONTAINER: {}", idContainer);
    }

    /**
     * Destroy docker client.
     *
     * @throws RobotestException
     *             Errors IO.
     */
    public void destroyDockerClient() throws RobotestException {
        DockerFarmBuilder.LOG.info("TRY TO STOP DOCKER CLIENT {}", this.dockerBaseCfg.getHost());
        if (null != this.docker) {
            try {
                this.docker.close();
            } catch (IOException e) {
                throw new RobotestException("DOCKER ERROR CLOSING CLIENT", e);
            }
            DockerFarmBuilder.LOG.info("STOP DOCKER CLIENT {}", this.dockerBaseCfg.getHost());
        }
    }

}
