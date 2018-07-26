package com.castinfo.devops.robotest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.castinfo.devops.robotest.annot.RobotestCase;
import com.castinfo.devops.robotest.annot.RobotestConfig;
import com.castinfo.devops.robotest.annot.RobotestStep;
import com.castinfo.devops.robotest.annot.RobotestSuite;
import com.castinfo.devops.robotest.config.DockerConfig;
import com.castinfo.devops.robotest.selenium.SeleniumBrowser;
import com.castinfo.devops.robotest.selenium.SeleniumFactoryTest;
import com.castinfo.devops.robotest.testutils.AnnotatedPageObjectExample;

public class SuiteContextTest {

    @Before
    public void setup() {
        System.setProperty("ROBOTEST_REPORT_BASE", System.getProperty("java.io.tmpdir"));
        System.setProperty("ROBOTEST_ENV", "local");
        System.setProperty("ROBOTEST_BROWSER", SeleniumBrowser.CHROME.name());
        System.setProperty("ROBOTEST_BROWSER_HEADLESS", "true");
        System.setProperty("ROBOTEST_BROWSER_WIDTH", "1024");
        System.setProperty("ROBOTEST_BROWSER_HEIGHT", "768");
        System.setProperty("ROBOTEST_BROWSER_MAXIMIZED", "true");
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
    public void testSuiteContext() throws RobotestException {
        SuiteContext ctx = new SuiteContext();
        RobotestSuite suiteAnnot = Mockito.mock(RobotestSuite.class);
        Mockito.when(suiteAnnot.tag()).thenReturn("TEST_SUITE_001");
        Mockito.when(suiteAnnot.description()).thenReturn("TEST_SUITE_DESC");
        Mockito.when(suiteAnnot.configElements()).thenReturn(new RobotestConfig[] {});
        ctx.setSuiteAnnotation(suiteAnnot);
        Assert.assertNotNull(ctx.getSuiteAnnotation());
        ctx.initSuite(0);
        ctx.getConfig()
           .getConfigBasic()
           .setBrowser(SeleniumFactoryTest.buildBrowserConfig(SeleniumBrowser.CHROME).getBrowser());
        ctx.getConfig().getConfigBasic().setDocker(new DockerConfig());
        RobotestCase caseAnnot = Mockito.mock(RobotestCase.class);
        Mockito.when(caseAnnot.tag()).thenReturn("TEST_CASE_001");
        Mockito.when(caseAnnot.description()).thenReturn("TEST_CASE_DESC");
        Mockito.when(caseAnnot.configElements()).thenReturn(new RobotestConfig[] {});
        ctx.initTestCase(suiteAnnot, caseAnnot);
        RobotestStep stepAnnot = Mockito.mock(RobotestStep.class);
        Mockito.when(stepAnnot.tag()).thenReturn("TEST_STEP_001");
        Mockito.when(stepAnnot.description()).thenReturn("TEST_STEP_DESC");
        AnnotatedPageObjectExample ae = new AnnotatedPageObjectExample();
        ae.setSuiteAnnot(suiteAnnot);
        ae.setCaseAnnot(caseAnnot);
        ae.setStepAnnot(stepAnnot);
        RobotestExecutionContext.getSuites().put(suiteAnnot, ctx);
        Mockito.when(stepAnnot.configElements()).thenReturn(new RobotestConfig[] {});
        ctx.initStep(ae, System.currentTimeMillis());
        ctx.endStep(ae, StepStatus.INFO, System.currentTimeMillis());
        ctx.endTestCase(caseAnnot);
        ctx.endSuite();
    }

}
