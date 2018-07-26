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
 * Test Cases related to Clientes section page
 *
 * @author Jordi.Artal
 *
 */
public class ClientesPageObject extends PageObject {

    public static final String PRE_HOME_CFG = "PRE_HOME_CFG";

    /*
     * Check Clientes Contact form
     */
    @RobotestStep(tag = "CLIENTES_STEP_001",
                  description = "Check Clientes Contact form",
                  captureScreenShootAtEndStep = true)
    public void checkContacto(final HomePageObject home) throws RobotestException {
        Assert.assertNotNull("element doesn't exists",
                             this.findElementBy(By.xpath("//*[@id=\"post-503\"]/div/div[3]/div/div/div/a")));
        home.checkCastCookies();
        this.findElementBy(By.xpath("//*[@id=\"post-503\"]/div/div[3]/div/div/div/a")).click();
        home.checkUseForm();
    }

    /*
     * Check Clientes Links
     */
    @RobotestStep(tag = "CLIENTES_STEP_002", description = "Check Clientes Links", captureScreenShootAtEndStep = true)
    public void checkClientesLinks() throws RobotestException {
        String handler = this.getDriver().getWindowHandle();
        List<WebElement> listElem =
                this.findElementsBy(By.xpath("//*[@id=\"post-503\"]/div/div[2]/div/div/div/div[2]/div/div"));
        Assert.assertNotNull("No elements found", listElem);
        JavascriptExecutor js = (JavascriptExecutor) this.getDriver();
        for (WebElement elem : listElem) {
            String link = elem.findElement(By.tagName("a")).getAttribute("href");
            js.executeScript("window.open('" + link + "','_blank');");
            this.switchToAnotherWindow();
            Boolean okPage = this.getCurrentUrl().equals(link);
            Assert.assertTrue("target URL and origin URL are not the same", okPage);
            this.getDriver().close();
            this.getDriver().switchTo().window(handler);
        }
    }
}