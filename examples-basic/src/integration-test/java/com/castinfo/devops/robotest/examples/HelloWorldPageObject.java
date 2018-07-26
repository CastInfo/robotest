package com.castinfo.devops.robotest.examples;

import org.junit.Assert;
import org.openqa.selenium.By;

import com.castinfo.devops.robotest.PageObject;
import com.castinfo.devops.robotest.RobotestException;
import com.castinfo.devops.robotest.annot.RobotestStep;

import io.restassured.http.Method;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.*;

public class HelloWorldPageObject extends PageObject {

    @RobotestStep(tag = "HELLO_WORLD_STEP_000",
                  description = "Config view",
                  captureConsoleErrorLogsAtEndStep = false,
                  captureScreenShootAtEndStep = true)
    public void noOpCode() {
    }

    @RobotestStep(tag = "HELLO_WORLD_STEP_001",
                  description = "Check home title",
                  captureConsoleErrorLogsAtEndStep = true,
                  capturePageSourceAtEndStep = true,
                  captureScreenShootAtEndStep = false,
                  captureScreenShootAtStartStep = true)
    public void checkTitle() throws RobotestException {
        String parameter = "Cast Info | Cast Info s.a > Soluciones y Servicios tecnológicos de Vanguardia";
        if (!parameter.equals(getDriver().getTitle())) {
            throw new RobotestException("Screen doesn't contains this Title: " + parameter);
        }
        addInfoToReport().withMessage("Web title content is OK");
    }

    @RobotestStep(tag = "HELLO_WORLD_STEP_002",
                  description = "Check home logo",
                  captureScreenShootAtEndStep = true,
                  captureScreenShootAtStartStep = false)
    public void checkLogo() throws RobotestException {
        Assert.assertTrue(this.isElementPresent(By.id("logo")));
        addInfoToReport().withMessage("Web logo presence is OK");
    }

    @RobotestStep(tag = "HELLO_WORLD_STEP_003",
                  description = "Check rest service",
                  captureScreenShootAtEndStep = false,
                  captureScreenShootAtStartStep = false)
    public void checkRest() throws RobotestException {
        Response response = getRestAssuredWrapper().doCall("https://reqres.in/api/users/2", Method.GET)
                                                   .buildRequest()
                                                   .getResponse();
        response.then().statusCode(200).body("data.id", equalTo(2));
        addInfoToReport().withMessage("JSON Response: " + response.body().asString());
    }

}
