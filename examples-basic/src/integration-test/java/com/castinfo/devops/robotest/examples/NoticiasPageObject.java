package com.castinfo.devops.robotest.examples;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.castinfo.devops.robotest.PageObject;
import com.castinfo.devops.robotest.RobotestException;
import com.castinfo.devops.robotest.annot.RobotestStep;

/**
 *
 * @author Jordi.Artal
 *
 */
public class NoticiasPageObject extends PageObject {

    public static final String PRE_HOME_CFG = "PRE_HOME_CFG";

    /*
     * Check Noticias Links
     */
    @RobotestStep(tag = "NOTICIAS_STEP_001", description = "Check Noticias Post", captureScreenShootAtEndStep = true)
    public void checkNoticiasPost() throws RobotestException {
        List<WebElement> listElems =
                this.findElementsBy(By.xpath("//*[@id=\"post-326\"]/div/div[2]/div/div[1]/div/div/div/div[1]//a"));
        Assert.assertNotNull("There ara no active Posts", listElems);
        String handler = this.getDriver().getWindowHandle();

        JavascriptExecutor js = (JavascriptExecutor) this.getDriver();
        for (WebElement elem : listElems) {
            String link = elem.getAttribute("href");
            js.executeScript("window.open('" + link + "','_blank');");
            this.switchToAnotherWindow();
            Boolean okPage = this.getCurrentUrl().equals(link);
            Assert.assertTrue("final page is not the same as sended target page", okPage);
            this.getDriver().close();
            this.getDriver().switchTo().window(handler);
        }
    }

    /*
     * Check Search in Noticias
     */
    @RobotestStep(tag = "NOTICIAS_STEP_002", description = "Check Noticias Search", captureScreenShootAtEndStep = true)
    public void checkNoticiasSearch() throws RobotestException {
        WebElement elem = this.findElementBy(By.xpath("//*[@id=\"s\"]"));
        elem.clear();
        elem.sendKeys("doctorado");
        elem.submit();
        this.waitForPageLoaded(2L);
        Assert.assertTrue("incorrect result page",
                          this.getCurrentUrl().equals("https://www.cast-info.es/?s=doctorado"));
    }

}