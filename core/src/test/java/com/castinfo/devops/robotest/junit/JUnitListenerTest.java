package com.castinfo.devops.robotest.junit;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunNotifier;
import org.mockito.Mockito;

import com.castinfo.devops.robotest.RobotestException;

public class JUnitListenerTest {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testListenerMethods() throws Exception {
        JUnitCaseListener ngL = new JUnitCaseListener();

        Description desc = Mockito.mock(Description.class);
        ArrayList<Description> childs = new ArrayList<>();
        childs.add(desc);
        Mockito.when(desc.getChildren())
               .thenReturn(childs);
        Mockito.when(desc.getTestClass())
               .thenReturn((Class) JUnitListenerTest.class);
        Mockito.when(desc.getMethodName())
               .thenReturn("testListenerMethods");
        Result result = Mockito.mock(Result.class);

        ngL.setClazz(this.getClass());
        ngL.getClazz();
        try {
            ngL.testRunStarted(desc);
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("TEST CLASS MUST BE @RobotestSuite ANNOTATED!"));
        }
        try {
            ngL.testRunFinished(result);
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("TEST CLASS MUST BE @RobotestSuite ANNOTATED!"));
        }
        try {
            ngL.testStarted(desc);
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("TEST METHOD NOT EXIST, NOT ACCESIBLE AND NOT @RobotestCase ANNOTATED!"));
        }
        try {
            ngL.testFinished(desc);
        } catch (RobotestException e) {
            Assert.assertTrue(e.getMessage()
                               .contains("TEST METHOD NOT EXIST, NOT ACCESIBLE AND NOT @RobotestCase ANNOTATED!"));
        }

        JUnitCaseRunner listener = Mockito.mock(JUnitCaseRunner.class);
        RunNotifier notifier = Mockito.mock(RunNotifier.class);
        listener.run(notifier);
    }
}
