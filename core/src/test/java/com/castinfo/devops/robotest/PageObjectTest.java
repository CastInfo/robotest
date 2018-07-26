package com.castinfo.devops.robotest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.testng.Assert;

import com.castinfo.devops.robotest.annot.RobotestStep;
import com.castinfo.devops.robotest.selenium.SeleniumBrowser;
import com.castinfo.devops.robotest.testutils.AnnotatedCaseExample;
import com.castinfo.devops.robotest.testutils.AnnotatedPageObjectExample;

public class PageObjectTest {

    @Before
    public void setup() {
        System.setProperty("ROBOTEST_REPORT_BASE", System.getProperty("java.io.tmpdir"));
        System.setProperty("ROBOTEST_ENV", "local");
        System.setProperty("ROBOTEST_BROWSER", SeleniumBrowser.CHROME.name());
        System.setProperty("ROBOTEST_BROWSER_HEADLESS", "true");
        System.setProperty("ROBOTEST_BROWSER_WIDTH", "1024");
        System.setProperty("ROBOTEST_BROWSER_HEIGHT", "768");
        System.setProperty("ROBOTEST_BROWSER_MAXIMIZED", "true");
        System.setProperty("ROBOTEST_BROWSER_CONSOLE_LOG_LEVEL", "OFF");
        System.setProperty("ROBOTEST_GENERAL_TIMEOUT", "10000");
    }

    @After
    public void teardown() {
        System.setProperty("ROBOTEST_REPORT_BASE", "");
        System.setProperty("ROBOTEST_ENV", "");
        System.setProperty("ROBOTEST_BROWSER", "");
        System.setProperty("ROBOTEST_GENERAL_TIMEOUT", "");
    }

    @Test
    public void testPageObject() throws RobotestException {
        System.setProperty("ROBOTEST_BROWSER", SeleniumBrowser.CHROME.name());
        System.setProperty("ROBOTEST_GENERAL_TIMEOUT", "10000");
        RobotestExecutionContext.buildSuite(AnnotatedCaseExample.class, "caseExample");
        SuiteContext ctx =
                RobotestExecutionContext.getSuite(RobotestExecutionContext.getSuiteAnnotation(AnnotatedCaseExample.class));
        AnnotatedPageObjectExample po = new AnnotatedPageObjectExample();
        po.setSuiteAnnot(RobotestExecutionContext.getSuiteAnnotation(AnnotatedCaseExample.class));
        po.setCaseAnnot(RobotestExecutionContext.getCaseAnnotationByMethod(AnnotatedCaseExample.class, "caseExample"));
        RobotestStep stepAnnot = Mockito.mock(RobotestStep.class);
        Mockito.when(stepAnnot.tag()).thenReturn("TEST_STEP_001");
        Mockito.when(stepAnnot.description()).thenReturn("TEST_DESC");
        po.setStepAnnot(stepAnnot);
        ctx.initTestCase(po.getSuiteAnnot(), po.getCaseAnnot());
        ctx.initStep(po, System.currentTimeMillis());
        po.addDebugToReport().withMessage("TEST");
        po.addInfoToReport().withMessage("TEST");
        po.addWarningToReport().withMessage("TEST");
        po.addErrorToReport().withMessage("TEST");
        po.addDefectToReport().withMessage("TEST");
        po.addCriticalToReport().withMessage("TEST");
        po.addPageSourceToReport(StepStatus.DEBUG, "TEST");
        try {
            po.addScreenShotToReport(StepStatus.DEBUG, "TEST");
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        try {
            Assert.assertTrue(po.takeBrowserConsoleLogs().isEmpty());
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(po.getBasicCfg().getBrowser().getBrowserName().equals(SeleniumBrowser.CHROME.name()));
        Assert.assertTrue(po.getGeneralWaitTimoutMillis() == 10000);
        Assert.assertTrue(po.getGeneralWaitTimoutSeconds() == 10);

        po.openURL("https://www.google.es");
        po.addCookie("TEST_COOKIE", "TEST_COOKIE_VALUE");
        po.listOfCookiesAvailable();
        po.getCookieValueByName("TEST_COOKIE");
        Assert.assertEquals(po.getCurrentUrl(), "https://www.google.es/");
        Assert.assertTrue(po.currentURLContains("google", 1));
        Assert.assertFalse(po.currentURLContains("error", 1));
        Assert.assertTrue(po.pageTitleContainsUntil("Google", 1));
        Assert.assertFalse(po.pageTitleContainsUntil("error", 1));
        Assert.assertFalse(po.isAlertPresent());
        Assert.assertNotNull(po.findElementBy(By.name("btnK")));
        Assert.assertTrue(po.isElementPresent(By.name("btnK")));
        Assert.assertFalse(po.isElementPresent(By.name("NOTEXIST")));
        Assert.assertTrue(po.isElementVisible(By.name("btnK")));
        Assert.assertFalse(po.isElementVisible(By.name("NOTEXIST")));
        Assert.assertTrue(po.isElementPresent(po.findElementBy(By.className("jsb")), By.name("btnK")));
        Assert.assertFalse(po.isElementPresent(po.findElementBy(By.className("jsb")), By.name("NOTEXIST")));
        Assert.assertTrue(po.isElementVisible(po.findElementBy(By.className("jsb")), By.name("btnK")));
        Assert.assertFalse(po.isElementVisible(po.findElementBy(By.className("jsb")), By.name("NOTEXIST")));
        Assert.assertTrue(po.isElementPresentUntil(By.name("btnK"), 1));
        Assert.assertFalse(po.isElementPresentUntil(By.name("NOTEXIST"), 1));
        Assert.assertTrue(po.isElementVisibleUntil(By.name("btnK"), 1));
        Assert.assertFalse(po.isElementVisibleUntil(By.name("NOTEXIST"), 1));
        Assert.assertFalse(po.isElementInvisibleUntil(By.name("btnK"), 1));
        Assert.assertTrue(po.getNumElementsVisible(By.name("btnK")) == 1);
        Assert.assertTrue(po.getElementsVisible(By.name("btnK")).size() == 1);
        Assert.assertTrue(po.attributeContains(po.findElementBy(By.name("btnK")), "value", "Buscar con Google"));
        Assert.assertTrue(po.isElementClickableUntil(By.name("btnK"), 1));
        Assert.assertFalse(po.isElementClickableUntil(By.name("NOTEXIST"), 1));
        Assert.assertFalse(po.isElementNoClickableUntil(By.name("btnK"), 1));
        Assert.assertFalse(po.isElementNoClickableUntil(By.name("NOTEXIST"), 1));
        try {
            po.moveToElement(po.findElementBy(By.name("btnK")));
            po.click(po.findElementBy(By.name("btnK")));
            po.waitForPageLoaded(5);
        } catch (WebDriverException e) {
            e.printStackTrace();
        }
        try {
            po.openURL("https://www.google.es");
            po.moveToLinkAndClick(po.findElementBy(By.name("btnK")));
        } catch (WebDriverException e) {
            e.printStackTrace();
        }
        try {
            po.openURL("https://www.google.es");
            po.clickUntilLoadPage(po.findElementBy(By.name("btnK")), 5);
        } catch (WebDriverException e) {
            e.printStackTrace();
        }
        try {
            po.openURL("https://www.google.es");
            po.clickUntilLoadPage(1, By.name("btnK"), 5);
            po.moveToElement(po.findElementBy(By.id("lst-ib")));
            po.sendKeysWithRetry(1, By.id("lst-ib"), "castinfo");
            po.click(po.findElementBy(By.name("btnK")));
        } catch (WebDriverException e) {
            e.printStackTrace();
        }
        ctx.endStep(po, StepStatus.INFO, System.currentTimeMillis());
        RobotestExecutionContext.endCaseByMethod(AnnotatedCaseExample.class, "caseExample");
        RobotestExecutionContext.endSuite(AnnotatedCaseExample.class);
    }
}
