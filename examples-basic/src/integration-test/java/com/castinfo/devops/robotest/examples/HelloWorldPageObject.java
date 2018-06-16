package com.castinfo.devops.robotest.examples;

import org.junit.Assert;
import org.openqa.selenium.By;

import com.castinfo.devops.robotest.PageObject;
import com.castinfo.devops.robotest.RobotestException;
import com.castinfo.devops.robotest.annot.RobotestStep;

public class HelloWorldPageObject extends PageObject {

    @RobotestStep(tag = "HELLO_WORLD_STEP_001",
                  description = "Check home title",
                  captureConsoleErrorLogsAtEndStep = true,
                  capturePageSourceAtEndStep = true,
                  captureScreenShootAtEndStep = false,
                  captureScreenShootAtStartStep = false)
    public void checkTitle() throws RobotestException {
        String parameter = "Cast Info | Cast Info s.a > Soluciones y Servicios tecnológicos de Vanguardia";
        if (!parameter.equals(this.getDriver().getTitle())) {
            throw new RobotestException("Screen doesn't contains this Title: " + parameter);
        }
        this.addInfoToReport().withMessage("Web title content is OK");
    }

    @RobotestStep(tag = "HELLO_WORLD_STEP_002",
                  description = "Check home logo",
                  captureScreenShootAtEndStep = true,
                  captureScreenShootAtStartStep = true)
    public void checkLogo() throws RobotestException {
        Assert.assertTrue(this.isElementPresent(By.id("logo")));
        this.addInfoToReport().withMessage("Web logo presence is OK");
    }

}
