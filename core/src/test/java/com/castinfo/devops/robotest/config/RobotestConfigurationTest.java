package com.castinfo.devops.robotest.config;

import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.castinfo.devops.robotest.RobotestException;
import com.castinfo.devops.robotest.annot.RobotestConfig;
import com.castinfo.devops.robotest.annot.RobotestSuite;
import com.castinfo.devops.robotest.report.ConfigEntry;
import com.castinfo.devops.robotest.selenium.SeleniumBrowser;
import com.castinfo.devops.robotest.testutils.Jaxb;

public class RobotestConfigurationTest {

    @Before
    public void setup() {
        System.setProperty(RobotestConfigKeys.ROBOTEST_REPORT_BASE, System.getProperty("java.io.tmpdir"));
        System.setProperty(RobotestConfigKeys.ROBOTEST_ENV, "local");
        System.setProperty(RobotestConfigKeys.ROBOTEST_BROWSER, SeleniumBrowser.CHROME.name());
        System.setProperty(RobotestConfigKeys.ROBOTEST_BROWSER_HEADLESS, "true");
        System.setProperty(RobotestConfigKeys.ROBOTEST_BROWSER_WIDTH, "1024");
        System.setProperty(RobotestConfigKeys.ROBOTEST_BROWSER_HEIGHT, "768");
        System.setProperty(RobotestConfigKeys.ROBOTEST_BROWSER_MAXIMIZED, "true");
        System.setProperty(RobotestConfigKeys.ROBOTEST_GENERAL_TIMEOUT_MILLIS, "1000");
        System.setProperty(RobotestConfigKeys.ROBOTEST_DOCKER_CONN, "tcp://192.168.99.100:2376");
        System.setProperty(RobotestConfigKeys.ROBOTEST_DOCKER_TLS, "true");
        System.setProperty(RobotestConfigKeys.ROBOTEST_DOCKER_CERTS,
                           System.getProperty("user.dir") + "/.docker/machine/certs");
        System.setProperty(RobotestConfigKeys.ROBOTEST_DOCKER_DEBUG, "5900");
        System.setProperty(RobotestConfigKeys.ROBOTEST_BROWSERSTACK_DEVICE, "IPHONE X");
        System.setProperty(RobotestConfigKeys.ROBOTEST_BROWSERSTACK_PLATFORM, "11.0");
        System.setProperty(RobotestConfigKeys.ROBOTEST_BROWSERSTACK_USER_NAME, "bs_user");
        System.setProperty(RobotestConfigKeys.ROBOTEST_BROWSERSTACK_ACCESS_KEY, "secret");
    }

    @After
    public void teardown() {
        System.setProperty(RobotestConfigKeys.ROBOTEST_REPORT_BASE, "");
        System.setProperty(RobotestConfigKeys.ROBOTEST_ENV, "");
        System.setProperty(RobotestConfigKeys.ROBOTEST_BROWSER, "");
        System.setProperty(RobotestConfigKeys.ROBOTEST_BROWSER_HEADLESS, "");
        System.setProperty(RobotestConfigKeys.ROBOTEST_BROWSER_WIDTH, "");
        System.setProperty(RobotestConfigKeys.ROBOTEST_BROWSER_HEIGHT, "");
        System.setProperty(RobotestConfigKeys.ROBOTEST_BROWSER_MAXIMIZED, "");
        System.setProperty(RobotestConfigKeys.ROBOTEST_GENERAL_TIMEOUT_MILLIS, "");
        System.setProperty(RobotestConfigKeys.ROBOTEST_DOCKER_CONN, "");
        System.setProperty(RobotestConfigKeys.ROBOTEST_DOCKER_TLS, "");
        System.setProperty(RobotestConfigKeys.ROBOTEST_DOCKER_CERTS, "");
        System.setProperty(RobotestConfigKeys.ROBOTEST_DOCKER_DEBUG, "");
        System.setProperty(RobotestConfigKeys.ROBOTEST_BROWSERSTACK_DEVICE, "");
        System.setProperty(RobotestConfigKeys.ROBOTEST_BROWSERSTACK_PLATFORM, "");
        System.setProperty(RobotestConfigKeys.ROBOTEST_BROWSERSTACK_USER_NAME, "");
        System.setProperty(RobotestConfigKeys.ROBOTEST_BROWSERSTACK_ACCESS_KEY, "");
        System.setProperty(RobotestConfigKeys.ROBOTEST_DOCKER_NETWORK, "");
        System.setProperty(RobotestConfigKeys.ROBOTEST_DOCKER_LABELS, "");
    }

    @Test
    public void testLoadBasicConfig() {
        IRobotestConfiguration rCfg = new RobotestConfiguration();
        RobotestSuite suiteAnnot = Mockito.mock(RobotestSuite.class);
        rCfg.loadBasic(suiteAnnot);
        List<ConfigEntry> lCfgEntries = rCfg.toReportConfigEntries(suiteAnnot);
        Assert.assertTrue(lCfgEntries.size() == 0);
        RobotestBasicConfig basicConfig = rCfg.getConfigBasic();
        Assert.assertTrue(basicConfig.getReportFilePath().equals(System.getProperty("java.io.tmpdir")));
        Assert.assertTrue(basicConfig.getEnv().equals("local"));
        Assert.assertTrue(basicConfig.getBrowser().getBrowserName().equals(SeleniumBrowser.CHROME.name()));
        Assert.assertTrue(basicConfig.getBrowser().getConsoleLogLevel().equals("WARNING"));
        Assert.assertTrue(!basicConfig.getGeneralTimeout().isEmpty());
        Assert.assertTrue(basicConfig.getDocker().getHost().equals("tcp://192.168.99.100:2376"));
        Assert.assertTrue(basicConfig.getDocker()
                                     .getCertsPath()
                                     .equals(System.getProperty("user.dir") + "/.docker/machine/certs"));
        Assert.assertTrue(basicConfig.getDocker().getExposeDebugPort().equals("5900"));
        Assert.assertTrue(basicConfig.getBrowserStack().getDevice().equals("IPHONE X"));
        Assert.assertTrue(basicConfig.getBrowserStack().getPlatform().equals("11.0"));
        Assert.assertTrue(basicConfig.getBrowserStack().getLogin().equals("bs_user"));
        Assert.assertTrue(basicConfig.getBrowserStack().getAccessKey().equals("secret"));

        Assert.assertTrue(basicConfig.toString().length() > 0);
        Assert.assertTrue(basicConfig.hashCode() != 0);
        Assert.assertTrue(basicConfig.equals(basicConfig));
        Assert.assertTrue(!basicConfig.equals(new Object()));
        Assert.assertTrue(!basicConfig.equals(new RobotestBasicConfig()));

    }

    @Test
    public void testLoadUserConfigProperties() throws RobotestException {
        IRobotestConfiguration rCfg = new RobotestConfiguration();
        RobotestConfig cfgAnnot = Mockito.mock(RobotestConfig.class);
        Mockito.when(cfgAnnot.resource()).thenReturn("classpath://testeable-local.properties");
        Mockito.when(cfgAnnot.type()).thenReturn(Properties.class);
        RobotestConfig[] cfgAnnotArray = { cfgAnnot };
        RobotestSuite suiteAnnot = Mockito.mock(RobotestSuite.class);
        Mockito.when(suiteAnnot.configElements()).thenReturn(cfgAnnotArray);
        rCfg.loadAnnotationScopeConfig(suiteAnnot.configElements(), suiteAnnot);
        List<ConfigEntry> lCfgEntries = rCfg.toReportConfigEntries(suiteAnnot);
        Assert.assertTrue(lCfgEntries.size() == 1);
        Assert.assertTrue(lCfgEntries.get(0).getValue() instanceof Properties);
        Assert.assertTrue(((Properties) lCfgEntries.get(0).getValue()).getProperty("TESTEABLE_KEY")
                                                                      .equals("TESTEABLE_VALUE"));
    }

    @Test
    public void testLoadUserConfigJackson() throws RobotestException {
        IRobotestConfiguration rCfg = new RobotestConfiguration();
        RobotestConfig cfgAnnot = Mockito.mock(RobotestConfig.class);
        Mockito.when(cfgAnnot.resource()).thenReturn("classpath://testeable-docker.json");
        Mockito.when(cfgAnnot.type()).thenReturn(DockerConfig.class);
        RobotestConfig[] cfgAnnotArray = { cfgAnnot };
        RobotestSuite suiteAnnot = Mockito.mock(RobotestSuite.class);
        Mockito.when(suiteAnnot.configElements()).thenReturn(cfgAnnotArray);
        rCfg.loadAnnotationScopeConfig(suiteAnnot.configElements(), suiteAnnot);
        List<ConfigEntry> lCfgEntries = rCfg.toReportConfigEntries(suiteAnnot);
        Assert.assertTrue(lCfgEntries.size() == 1);
        Assert.assertTrue(lCfgEntries.get(0).getValue() instanceof DockerConfig);
        DockerConfig docker = (DockerConfig) lCfgEntries.get(0).getValue();
        Assert.assertTrue(docker.getHost().equals("TEST_HOST"));
        Assert.assertTrue(docker.toString().length() > 0);
        Assert.assertTrue(docker.hashCode() != 0);
        Assert.assertTrue(docker.equals(docker));
        Assert.assertTrue(!docker.equals(new Object()));
        Assert.assertTrue(!docker.equals(new DockerConfig()));
    }

    @Test
    public void testLoadUserConfigJaxb() throws RobotestException {
        IRobotestConfiguration rCfg = new RobotestConfiguration();
        RobotestConfig cfgAnnot = Mockito.mock(RobotestConfig.class);
        Mockito.when(cfgAnnot.resource()).thenReturn("classpath://testeable-jaxb.xml");
        Mockito.when(cfgAnnot.type()).thenReturn(Jaxb.class);
        RobotestConfig[] cfgAnnotArray = { cfgAnnot };
        RobotestSuite suiteAnnot = Mockito.mock(RobotestSuite.class);
        Mockito.when(suiteAnnot.configElements()).thenReturn(cfgAnnotArray);
        rCfg.loadAnnotationScopeConfig(suiteAnnot.configElements(), suiteAnnot);
        List<ConfigEntry> lCfgEntries = rCfg.toReportConfigEntries(suiteAnnot);
        Assert.assertTrue(lCfgEntries.size() == 1);
        Assert.assertTrue(lCfgEntries.get(0).getValue() instanceof Jaxb);
        Jaxb xml = (Jaxb) lCfgEntries.get(0).getValue();
        Assert.assertTrue(xml.getValue().equals("TEST"));
    }

}
