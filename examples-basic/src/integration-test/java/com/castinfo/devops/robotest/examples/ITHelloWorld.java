package com.castinfo.devops.robotest.examples;

import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.castinfo.devops.robotest.RobotestException;
import com.castinfo.devops.robotest.TestCase;
import com.castinfo.devops.robotest.annot.RobotestCase;
import com.castinfo.devops.robotest.annot.RobotestConfig;
import com.castinfo.devops.robotest.annot.RobotestSuite;
import com.castinfo.devops.robotest.junit.JUnitCaseRunner;

import io.restassured.http.Method;
import io.restassured.response.Response;

@RobotestSuite(tag = "HELLO_WORLD_ROBOTEST",
               description = "Navegación estándar Mango Shop",
               configElements = { @RobotestConfig(key = "WEB_TO_TEST",
                                                  type = Properties.class,
                                                  resource = "system://CAST-INFO-WEB") })
@RunWith(JUnitCaseRunner.class)
public class ITHelloWorld extends TestCase {

    @Test
    @RobotestCase(tag = "HELLO_WORLD_ROBOTEST_CASE_001", description = "The ROBOTEST HelloWorld example by Selenium")
    public void helloWorldSelenium() throws RobotestException {
        HelloWorldPageObject helloWorldPO = this.buildPageObject(HelloWorldPageObject.class);
        String urlToTest = getSuiteTestPropertyCfg("WEB_TO_TEST", "CAST-INFO-WEB");
        helloWorldPO.openURL(urlToTest);
        helloWorldPO.checkTitle();
        helloWorldPO.checkLogo();
        helloWorldPO.addInfoToReport().withMessage("Congratulations, you have finish your first ROBOTEST example!");
    }

    @Test
    @RobotestCase(tag = "HELLO_WORLD_ROBOTEST_CASE_002", description = "The ROBOTEST HelloWorld example by RestAssured")
    public void helloWorldRestAssured() throws RobotestException {
        HelloWorldPageObject helloWorldPO = this.buildPageObject(HelloWorldPageObject.class);
        helloWorldPO.checkRest();
        helloWorldPO.addInfoToReport().withMessage("Congratulations, you have finish your second ROBOTEST example!");
    }

}