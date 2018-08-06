package com.castinfo.devops.robotest.selenium;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.castinfo.devops.robotest.RobotestException;
import com.castinfo.devops.robotest.config.BrowserStackConfig;
import com.castinfo.devops.robotest.config.DockerConfig;
import com.castinfo.devops.robotest.config.RobotestBasicConfig;
import com.castinfo.devops.robotest.config.RobotestBrowserConfig;
import com.castinfo.devops.robotest.config.RobotestHttpConnConfig;

public class SeleniumFactoryTest {

    public static RobotestBasicConfig buildBrowserConfig(final SeleniumBrowser browser) {
        RobotestBasicConfig basCfg = new RobotestBasicConfig();
        basCfg.setHttp(new RobotestHttpConnConfig());
        basCfg.getHttp()
              .setGeneralTimeout("1000");
        RobotestBrowserConfig bCfg = new RobotestBrowserConfig();
        bCfg.setBrowserName(browser.name());
        bCfg.setHeadLess("true");
        bCfg.setMaximized("true");
        bCfg.setConsoleLogLevel("INFO");
        bCfg.setWindowWidth("1024");
        bCfg.setWindowHeight("768");
        basCfg.setBrowser(bCfg);
        return basCfg;
    }

    @Test
    public void testSeleniumFactoryCapabilities() {
        WebDriver wd = Mockito.mock(WebDriver.class);
        Options ops = Mockito.mock(Options.class);
        Mockito.doNothing()
               .when(ops)
               .deleteAllCookies();
        Timeouts timeout = Mockito.mock(Timeouts.class);
        Mockito.when(timeout.pageLoadTimeout(ArgumentMatchers.anyInt(),
                                             ArgumentMatchers.any(TimeUnit.class)))
               .thenReturn(timeout);
        Mockito.when(timeout.setScriptTimeout(ArgumentMatchers.anyInt(),
                                              ArgumentMatchers.any(TimeUnit.class)))
               .thenReturn(timeout);
        Mockito.when(timeout.implicitlyWait(ArgumentMatchers.anyInt(),
                                            ArgumentMatchers.any(TimeUnit.class)))
               .thenReturn(timeout);
        Mockito.when(ops.timeouts())
               .thenReturn(timeout);
        Window window = Mockito.mock(Window.class);
        Mockito.doNothing()
               .when(window)
               .maximize();
        Mockito.when(ops.window())
               .thenReturn(window);
        Mockito.when(wd.manage())
               .thenReturn(ops);

        SeleniumDriverFactory selDrv = new SeleniumDriverFactory(buildBrowserConfig(SeleniumBrowser.FIREFOX));
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        selDrv = new SeleniumDriverFactory(buildBrowserConfig(SeleniumBrowser.FIREFOX));
        selDrv.preCreationDriverCapabilities(capabilities);
        Assert.assertTrue(capabilities.getCapability("timeouts") != null);
        Assert.assertTrue(capabilities.getCapability(CapabilityType.LOGGING_PREFS) != null);
        Assert.assertTrue(capabilities.getCapability(FirefoxDriver.PROFILE) != null);
        selDrv.postCreationDriverCapabilities(wd,
                                              capabilities,
                                              true);

        selDrv = new SeleniumDriverFactory(buildBrowserConfig(SeleniumBrowser.CHROME));
        capabilities = DesiredCapabilities.chrome();
        selDrv.preCreationDriverCapabilities(capabilities);
        Assert.assertTrue(capabilities.getCapability("timeouts") != null);
        Assert.assertTrue(capabilities.getCapability(CapabilityType.LOGGING_PREFS) != null);
        Assert.assertTrue(capabilities.getCapability(ChromeOptions.CAPABILITY) != null);
        selDrv.postCreationDriverCapabilities(wd,
                                              capabilities,
                                              true);
    }

    @Test
    public void testBuildBrowserStack() throws RobotestException {
        SeleniumDriverFactory selDrv = new SeleniumDriverFactory(buildBrowserConfig(SeleniumBrowser.FIREFOX));
        BrowserStackConfig bsCfg = new BrowserStackConfig();
        try {
            selDrv.browserStackCfgValidations("TEST_SUITE",
                                              "TEST_CASE",
                                              bsCfg);
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("BROWSERSTACK BROWSER NOT SUPORTED"));
        }
        selDrv = new SeleniumDriverFactory(buildBrowserConfig(SeleniumBrowser.ANDROID));
        try {
            selDrv.browserStackCfgValidations("TEST_SUITE",
                                              "TEST_CASE",
                                              bsCfg);
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("BROWSERSTACK DEVICE IS MANDATORY"));
        }
        bsCfg.setDevice("TEST_DEVICE");
        try {
            selDrv.browserStackCfgValidations("TEST_SUITE",
                                              "TEST_CASE",
                                              bsCfg);
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("BROWSERSTACK PLATFORM IS MANDATORY"));
        }
        bsCfg.setPlatform("TEST_PLATFORM");
        try {
            selDrv.browserStackCfgValidations("TEST_SUITE",
                                              "TEST_CASE",
                                              bsCfg);
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("BROWSERSTACK USERNAME IS MANDATORY"));
        }
        bsCfg.setLogin("TEST_LOGIN");
        try {
            selDrv.browserStackCfgValidations("TEST_SUITE",
                                              "TEST_CASE",
                                              bsCfg);
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("BROWSERSTACK ACCESSKEY IS MANDATORY"));
        }
        bsCfg.setAccessKey("TEST_PASSWORD");
        try {
            selDrv.browserStackCfgValidations(null,
                                              "TEST_CASE",
                                              bsCfg);
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("BROWSERSTACK SUITE NAME IS MANDATORY"));
        }
        try {
            selDrv.browserStackCfgValidations("TEST_SUITE",
                                              null,
                                              bsCfg);
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("BROWSERSTACK CASE NAME IS MANDATORY"));
        }
        selDrv.browserStackCfgValidations("TEST_SUITE",
                                          "TEST_CASE",
                                          bsCfg);
        selDrv = new SeleniumDriverFactory(buildBrowserConfig(SeleniumBrowser.IPAD));
        selDrv.browserStackCfgValidations("TEST_SUITE",
                                          "TEST_CASE",
                                          bsCfg);
        selDrv = new SeleniumDriverFactory(buildBrowserConfig(SeleniumBrowser.IPHONE));
        selDrv.browserStackCfgValidations("TEST_SUITE",
                                          "TEST_CASE",
                                          bsCfg);
        DesiredCapabilities capabilities = selDrv.browserStackCapabilities("TEST_SUITE",
                                                                           "TEST_CASE",
                                                                           bsCfg);
        Assert.assertTrue(capabilities.getCapability("browserName")
                                      .toString()
                                      .equalsIgnoreCase(SeleniumBrowser.IPHONE.name()));
        Assert.assertTrue(capabilities.getCapability("device")
                                      .equals(bsCfg.getDevice()));
        Assert.assertTrue(capabilities.getCapability("os_version")
                                      .equals(bsCfg.getPlatform()));
        Assert.assertTrue(capabilities.getCapability("build")
                                      .equals("TEST_SUITE"));
        Assert.assertTrue(capabilities.getCapability("project")
                                      .equals("TEST_CASE"));
        try {
            selDrv.buildBrowserStackRealDeviceWebDriver("TEST_SUITE",
                                                        "TEST_CASE",
                                                        null);
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("BROWSERSTACK CONFIG IS MANDATORY"));
        }
        try {
            bsCfg.setLogin("MALFORMED URL");
            selDrv.buildBrowserStackRealDeviceWebDriver("TEST_SUITE",
                                                        "TEST_CASE",
                                                        bsCfg);
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("BROWSER STACK SELENIUM DRIVER CRETION ERROR"));
        }
        Assert.assertTrue(bsCfg.equals(bsCfg));
        Assert.assertTrue(!bsCfg.equals(new Object()));
        Assert.assertTrue(!bsCfg.equals(new BrowserStackConfig()));
    }

    @Test
    public void testBuildDockerHub() {
        SeleniumDriverFactory selDrv = new SeleniumDriverFactory(buildBrowserConfig(SeleniumBrowser.ANDROID));
        DockerConfig dCfg = new DockerConfig();
        WebDriver drv = null;
        try {
            drv = selDrv.buildDockerHubWebDriver(dCfg);
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("NOT DOCKER AVAILABLE FOR THIS BROWSER"));
        } finally {
            if (null != drv) {
                drv.quit();
            }
        }
        selDrv = new SeleniumDriverFactory(buildBrowserConfig(SeleniumBrowser.FIREFOX));
        try {
            drv = selDrv.buildDockerHubWebDriver(dCfg);
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("SELENIUM DRIVER DOCKER HUB CONFIG NOT FOUND"));
        } finally {
            if (null != drv) {
                drv.quit();
            }
        }
        dCfg.setHub("NO VALID HUB");
        try {
            drv = selDrv.buildDockerHubWebDriver(dCfg);
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("DOCKER HUB SELENIUM DRIVER CREATION ERROR"));
        } finally {
            if (null != drv) {
                drv.quit();
            }
        }
        selDrv = new SeleniumDriverFactory(buildBrowserConfig(SeleniumBrowser.CHROME));
        try {
            drv = selDrv.buildDockerHubWebDriver(dCfg);
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("DOCKER HUB SELENIUM DRIVER CREATION ERROR"));
        } finally {
            if (null != drv) {
                drv.quit();
            }
        }
    }

    @Test
    public void buildNativeChromeBrowser() throws Exception {
        SeleniumDriverFactory selDrv = new SeleniumDriverFactory(buildBrowserConfig(SeleniumBrowser.CHROME));
        WebDriver drv = null;
        try {
            drv = selDrv.buildLocalNativeWebDriver();
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("NATIVE DRIVER CREATION ERROR"));
        } finally {
            if (null != drv) {
                drv.quit();
            }
        }
    }

    @Test
    public void buildNativeFirefoxBrowser() throws Exception {
        SeleniumDriverFactory selDrv = new SeleniumDriverFactory(buildBrowserConfig(SeleniumBrowser.FIREFOX));
        WebDriver drv = null;
        try {
            drv = selDrv.buildLocalNativeWebDriver();
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("NATIVE DRIVER CREATION ERROR"));
        } finally {
            if (null != drv) {
                drv.quit();
            }
        }

    }

}
