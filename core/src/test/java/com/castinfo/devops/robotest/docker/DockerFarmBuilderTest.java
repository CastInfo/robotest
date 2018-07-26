package com.castinfo.devops.robotest.docker;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.castinfo.devops.robotest.RobotestException;
import com.castinfo.devops.robotest.config.DockerConfig;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InfoCmd;
import com.github.dockerjava.api.command.InspectContainerCmd;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.NetworkSettings;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.Ports.Binding;
import com.github.dockerjava.api.model.PullResponseItem;

public class DockerFarmBuilderTest {

    @Test
    public void testPullCallback() throws IOException {
        try (DockerBasicImagePullResultCallback callback = new DockerBasicImagePullResultCallback()) {
            callback.onNext(Mockito.mock(PullResponseItem.class));
        }
    }

    @Test
    public void testContainerCallback() throws IOException {
        try (DockerContainerCallback callback = new DockerContainerCallback("Selenium Server is up and running")) {
            Frame frame = Mockito.mock(Frame.class);
            Mockito.when(frame.toString()).thenReturn("Selenium Server is up and running");
            callback.onNext(frame);
            Assert.assertTrue(callback.isServerSeleniumLoaded());
            callback.setServerSeleniumLoaded(false);
            Mockito.when(frame.toString()).thenReturn("TEST");
            callback.onNext(frame);
            Assert.assertTrue(!callback.isServerSeleniumLoaded());
        }
    }

    @Test
    public void testFarmBuilder() throws RobotestException {
        DockerConfig cfgBasic = new DockerConfig();
        cfgBasic.setCertsPath(System.getProperty("user.home") + "/.docker/machine/certs");
        cfgBasic.setExposePort("4444");
        cfgBasic.setChromeImageTag("TEST_CHROME_IMAGE_PATH");
        cfgBasic.setChromeDebugImageTag("TEST_CHROME_IMAGE_PATH");
        cfgBasic.setFirefoxImageTag("TEST_FIREFOX_IMAGE_PATH");
        cfgBasic.setFirefoxDebugImageTag("TEST_FIREFOX_IMAGE_PATH");

        DockerConfig cfgBrowser = new DockerConfig();
        cfgBrowser.setIdContainer("TEST_ID");
        cfgBrowser.setExposePort("4444");

        // create farm
        DockerFarmBuilder farm = new DockerFarmBuilder(cfgBasic);
        DockerClient docker = Mockito.mock(DockerClient.class);
        farm.setDockerClient(docker);

        // test client
        InfoCmd infoCmd = Mockito.mock(InfoCmd.class);
        Mockito.when(docker.infoCmd()).thenReturn(infoCmd);
        Info info = Mockito.mock(Info.class);
        Mockito.when(infoCmd.exec()).thenReturn(info);

        try {
            farm.connectDockerClient();
        } catch (RobotestException e) {
            Assert.assertEquals("DOCKER CONN IS REQUIRED, REVISE BASE CONFIG", e.getMessage());
        }

        InspectContainerCmd inspect = Mockito.mock(InspectContainerCmd.class);
        InspectContainerResponse inspectRes = Mockito.mock(InspectContainerResponse.class);
        Mockito.when(inspect.exec()).thenReturn(inspectRes);
        Mockito.when(docker.inspectContainerCmd(ArgumentMatchers.anyString())).thenReturn(inspect);
        NetworkSettings netsettings = Mockito.mock(NetworkSettings.class);
        Mockito.when(inspectRes.getNetworkSettings()).thenReturn(netsettings);
        Ports ports = Mockito.mock(Ports.class);
        Mockito.when(netsettings.getPorts()).thenReturn(ports);
        Map<ExposedPort, Binding[]> bindings = new HashMap<>();
        Binding[] bind = { new Binding("",
                                       "4444") };
        bindings.put(ExposedPort.tcp(Integer.parseInt(cfgBrowser.getExposePort())), bind);
        Mockito.when(ports.getBindings()).thenReturn(bindings);

        cfgBasic.setHost("tcp://192.168.99.100:2376");
        cfgBasic.setHub("192.168.99.100");
        farm.resolveBrowserHub(cfgBrowser);
        Assert.assertEquals("http://192.168.99.100:4444/wd/hub", cfgBrowser.getHub());

        try {
            farm.resolveBrowserHub(cfgBrowser);
        } catch (RobotestException e) {
            Assert.assertEquals("DOCKER UNIX SOCKET CONN NEED DOCKER PUBLIC HOST CONFIG", e.getMessage());
        }

        cfgBasic.setHost("unix:///var/run/docker.sock");
        cfgBasic.setHub("192.168.99.100");
        farm.resolveBrowserHub(cfgBrowser);
        Assert.assertEquals("http://192.168.99.100:4444/wd/hub", cfgBrowser.getHub());

        try {
            cfgBasic.setHost("https:///var/run/docker.sock");
            cfgBasic.setHub("192.168.99.100");
            farm.resolveBrowserHub(cfgBrowser);
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage().startsWith("DOCKER CONN URI NOT VALID, REVISE CFG"));
        }

        farm.destroyDockerClient();

    }

}
