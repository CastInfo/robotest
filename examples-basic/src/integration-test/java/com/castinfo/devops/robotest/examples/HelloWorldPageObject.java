package com.castinfo.devops.robotest.examples;

import com.castinfo.devops.robotest.PageObject;
import com.castinfo.devops.robotest.RobotestException;
import com.castinfo.devops.robotest.annot.RobotestStep;

public class HelloWorldPageObject extends PageObject {

    @RobotestStep(tag = "HELLO_WORLD_STEP_001",
                  description = "Check home title",
                  captureConsoleErrorLogsAtEndStep = true)
    public void checkTitle() throws RobotestException {
        String parameter = "Cast Info | Cast Info s.a > Soluciones y Servicios tecnológicos de Vanguardia";
        if (!parameter.equals(this.getDriver().getTitle())) {
            throw new RobotestException("Screen doesn't contains this Title: " + parameter);
        }
        this.addInfoToReport().withMessage("Hi! you complete your first Hello World ROBOTEST example");
    }

}
