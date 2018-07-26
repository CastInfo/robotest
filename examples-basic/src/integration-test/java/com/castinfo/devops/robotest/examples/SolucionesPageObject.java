package com.castinfo.devops.robotest.examples;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.castinfo.devops.robotest.PageObject;
import com.castinfo.devops.robotest.RobotestException;
import com.castinfo.devops.robotest.annot.RobotestStep;

public class SolucionesPageObject extends PageObject {

    public static final String PRE_HOME_CFG = "PRE_HOME_CFG";

    /*
     * Check Solutions Links
     */
    @RobotestStep(tag = "SOLUTIONS_STEP_001",
                  description = "Check Soluciones Link List",
                  captureScreenShootAtEndStep = true)
    public void checkLinkList() throws RobotestException {
        List<WebElement> elems =
                this.findElementsBy(By.xpath("//*[@id=\"post-285\"]/div/div[4]/div/div/div/div[2]/div/div"));
        if (!elems.isEmpty()) {
            for (int i = 1; i < elems.size(); i++) {
                WebElement url =
                        this.findElementBy(By.xpath("//*[@id=\"post-285\"]/div/div[4]/div/div/div/div[2]/div/div[" + i
                                + "]/h2/a"));
                String urlString = url.getAttribute("href");
                String title = url.getText();
                this.getDriver().navigate().to(urlString);
                String titleResult = this.getDriver().getTitle();
                Assert.assertTrue(titleResult.contains(title));
                this.getDriver().navigate().back();
            }
        }
    }

    /*
     * Check Solutions Contacto
     */
    @RobotestStep(tag = "SOLUTIONS_STEP_002",
                  description = "Check Soluciones Contacto",
                  captureScreenShootAtEndStep = true)
    public void checkContacto(final HomePageObject home) throws RobotestException {
        Assert.assertNotNull(this.findElementBy(By.xpath("//*[@id=\"post-285\"]/div/div[6]/div/div/div/a")));
        home.checkCastCookies();
        this.findElementBy(By.xpath("//*[@id=\"post-285\"]/div/div[6]/div/div/div/a")).click();
        home.checkUseForm();
    }
}
