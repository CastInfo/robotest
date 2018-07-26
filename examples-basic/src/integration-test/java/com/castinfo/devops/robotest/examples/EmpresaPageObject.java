package com.castinfo.devops.robotest.examples;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.castinfo.devops.robotest.PageObject;
import com.castinfo.devops.robotest.RobotestException;
import com.castinfo.devops.robotest.annot.RobotestStep;

/**
 *
 * @author Jordi.Artal
 *
 */
public class EmpresaPageObject extends PageObject {

    public static final String PRE_HOME_CFG = "PRE_HOME_CFG";

    /*
     * Check Page Title
     */
    @RobotestStep(tag = "EMPRESA_STEP_001", description = "Check empresa title", captureScreenShootAtEndStep = true)
    public void checkTitle() throws RobotestException {
        String parameter = "Empresa | Cast Info";
        if (!this.getDriver().getTitle().equals(parameter)) {
            throw new RobotestException("this page does not contains this title: " + parameter);
        }
    }

    /*
     * Check Check empresa Sections
     */
    @RobotestStep(tag = "EMPRESA_STEP_002", description = "Check Empresa Sections", captureScreenShootAtEndStep = true)
    public void checkSections() throws RobotestException {
        Assert.assertNotNull("This element doesn't exist",
                             this.findElementBy(By.xpath("//*[@id=\"post-659\"]/div/div[3]/div/div[2]")));
        this.moveToElement(this.findElementBy(By.xpath("//*[@id=\"post-659\"]/div/div[2]")));
        this.click(this.findElementBy(By.xpath("//*[@id=\"post-659\"]/div/div[3]/div/div[2]")));

        Assert.assertNotNull("This element doesn't exist",
                             this.findElementBy(By.xpath("//*[@id=\"post-659\"]/div/div[3]/div/div[3]")));
        this.moveToElement(this.findElementBy(By.xpath("//*[@id=\"post-659\"]/div/div[2]")));
        this.click(this.findElementBy(By.xpath("//*[@id=\"post-659\"]/div/div[3]/div/div[3]")));

        Assert.assertNotNull("This element doesn't exist",
                             this.findElementBy(By.xpath("//*[@id=\"post-659\"]/div/div[3]/div/div[4]")));
        this.moveToElement(this.findElementBy(By.xpath("//*[@id=\"post-659\"]/div/div[2]")));
        this.click(this.findElementBy(By.xpath("//*[@id=\"post-659\"]/div/div[3]/div/div[4]")));
    }

    /*
     * Check Empresa certifications links
     */
    @RobotestStep(tag = "EMPRESA_STEP_003",
                  description = "Check Empresa certifications links",
                  captureScreenShootAtEndStep = true)
    public void checkCalidadLinks() throws RobotestException {
        List<WebElement> listElem =
                this.getDriver().findElements(By.xpath("//*[@id=\"imatges_certificats\"]/div/div/a"));
        for (WebElement elem : listElem) {
            // Open elements in a new Tab
            String selectLinkOpeninNewTab = Keys.chord(Keys.CONTROL, Keys.RETURN);
            elem.sendKeys(selectLinkOpeninNewTab);
        }
    }

    /*
     * Check las flechas en Clientes
     */
    @RobotestStep(tag = "EMPRESA_STEP_004",
                  description = "Check Empresa clientes arrows",
                  captureScreenShootAtEndStep = true)
    public void checkClientesArrows() throws RobotestException {
        this.moveToElement(this.findElementBy(By.xpath("//*[@id=\"post-659\"]/div/div[5]")));

        Assert.assertNotNull("This element doesn't exist",
                             this.findElementBy(By.xpath("//*[@id=\"post-659\"]/div/div[5]/div/div/div[1]/a[1]")));
        this.click(this.findElementBy(By.xpath("//*[@id=\"post-659\"]/div/div[5]/div/div/div[1]/a[1]")));

        Assert.assertNotNull("This element doesn't exist",
                             this.findElementBy(By.xpath("//*[@id=\"post-659\"]/div/div[5]/div/div/div[1]/a[2]")));
        this.click(this.findElementBy(By.xpath("//*[@id=\"post-659\"]/div/div[5]/div/div/div[1]/a[2]")));
    }

    /*
     * Check enlaces lista de Clientes
     */
    @RobotestStep(tag = "EMPRESA_STEP_005",
                  description = "Check Empresa clientes links",
                  captureScreenShootAtEndStep = true)
    public void checkClientesLinks() throws RobotestException {
        List<WebElement> linkList =
                this.getDriver().findElements(By.xpath("//*[@id=\"post-659\"]/div/div[5]/div/div//a"));
        if (!linkList.isEmpty()) {
            List<String> hrefList = new ArrayList<>();
            for (WebElement elem : linkList) {
                hrefList.add(elem.getAttribute("href"));
            }
            for (String href : hrefList) {
                this.waitForPageLoaded(1L);
                this.getDriver().navigate().to(href);
                Assert.assertTrue(href.equals(this.getCurrentUrl()));
            }
        } else {
            throw new RobotestException("There is no elements in this section");
        }
    }

    /*
     * Check Popups Google Maps
     */
    @RobotestStep(tag = "EMPRESA_STEP_006",
                  description = "Check Empresa Google Maps Popups",
                  captureScreenShootAtEndStep = true)
    public void checkMaps() throws RobotestException, InterruptedException {
        this.findElementBy(By.xpath("//div[@id='cookie-law-info-bar']/span/a")).click();
        this.moveToElement(this.getDriver().findElement(By.xpath("//*[@id=\"post-659\"]/div/div[7]/div")));
        List<WebElement> elems =
                this.getDriver()
                    .findElements(By.xpath("//*[@id=\"post-659\"]/div/div[7]/div/div[1]/div/div/div[1]/div[3]/div[2]/div[3]/div"));
        if (!elems.isEmpty()) {
            for (WebElement elem : elems) {
                this.moveToElement(elem);
                elem.click();
                if (this.getDriver()
                        .findElement(By.xpath("//*[@id=\"post-659\"]/div/div[7]/div/div[1]/div/div/div[1]/div[3]/div[2]/div[4]/div"))
                        .isDisplayed()) {
                    Thread.sleep(1000L);
                    this.getDriver()
                        .findElement(By.xpath("//*[@id=\"post-659\"]/div/div[7]/div/div[1]/div/div/div[1]/div[3]/div[2]/div[4]/div/div[3]"))
                        .click();
                    Assert.assertTrue(true);
                } else {
                    Assert.assertTrue("This element can not be displayed", false);
                }
            }
        }
    }
}
