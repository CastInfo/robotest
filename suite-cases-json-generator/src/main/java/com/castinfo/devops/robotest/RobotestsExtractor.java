package com.castinfo.devops.robotest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.castinfo.devops.robotest.annot.RobotestCase;
import com.castinfo.devops.robotest.annot.RobotestSuite;

/**
 *
 * This plugin utility extract JSON of robotest annotation to select Suite and Cases in Jenkins 1 and 2. You can include
 * this plugin in your test project pom build plugin definition, or exectute manually doing:
 * com.castinfo.devops:com.castinfo.devops.robotest.extractor:0.0.1-SNAPSHOT:robotestsextractor
 * -Dtest.classes.subdir=target/test-classes/ -Dresources.dir=src/robotest/
 * -Djenkins1.file=robotest_test_choice_jenkins1.json -Drobotest_test_choice_jenkins2.json This fileNames and dir
 * especified in example, are provided by default. This plugin is associated to TEST_COMPILE fase.
 */
@Mojo(name = "robotestsextractor", defaultPhase = LifecyclePhase.TEST_COMPILE)
public class RobotestsExtractor extends AbstractMojo {

    @Parameter(property = "project.basedir")
    private File basedir;

    @Parameter(property = "test.classes.subdir", defaultValue = "target/test-classes/")
    private String testSubDir;

    @Parameter(property = "resources.dir", defaultValue = "src/robotest/")
    private String resourcesDir;

    @Parameter(property = "jenkins1.file", defaultValue = "robotest_test_choice_jenkins1.json")
    private String jk1File;

    @Parameter(property = "jenkins2.file", defaultValue = "robotest_test_choice_jenkins2.json")
    private String jk2File;

    @Parameter(property = "project.testClasspathElements", required = true, readonly = true)
    private List<String> testClasspath;

    @Override
    public void execute() throws MojoExecutionException {
        if (null == this.testSubDir || this.testSubDir.trim().length() == 0) {
            this.testSubDir = "target/test-classes/";
        }
        if (null == this.resourcesDir || this.resourcesDir.trim().length() == 0) {
            this.resourcesDir = "src/robotest/";
        }
        if (null == this.jk1File || this.jk1File.trim().length() == 0) {
            this.jk1File = "jenkins1.file";
        }
        if (null == this.jk2File || this.jk2File.trim().length() == 0) {
            this.jk2File = "jenkins2.file";
        }
        File testClassesDir = new File(this.basedir.getAbsolutePath() + File.separator + this.testSubDir);
        if (!testClassesDir.exists()) {
            throw new MojoExecutionException("TEST DIR NOT EXISTS!");
        }
        File resourcesJson = new File(this.basedir.getAbsolutePath() + File.separator + this.resourcesDir);
        if (!resourcesJson.exists()) {
            resourcesJson.mkdirs();
        }
        try (FileWriter testSuitesJenkins2 =
                new FileWriter(resourcesJson.getAbsolutePath() + File.separator + this.jk2File);
             FileWriter testSuitesJenkins1 =
                     new FileWriter(resourcesJson.getAbsolutePath() + File.separator + this.jk1File)) {
            this.getLog().info("RETRIVE ROBOTEST SUITES AND CASES: " + testClassesDir.getAbsolutePath());
            testSuitesJenkins2.write("{ \"tests\":[");
            testSuitesJenkins2.write("{\"robotestSuite\":\"ALL\",\"robotestCase\":\"ALL\",\"robotestClass\":\"*\",\"robotestMethod\":\"*\"}");
            testSuitesJenkins1.write("tests=[");
            URL[] urls = new URL[] { testClassesDir.toURI().toURL() };
            ClassLoader testClassLoader = new URLClassLoader(urls, this.getClass().getClassLoader());
            this.findClasses(testClassesDir, testClassesDir, testSuitesJenkins1, testSuitesJenkins2, testClassLoader);
            testSuitesJenkins2.write("]}");
            testSuitesJenkins1.write("{\"robotestSuite\":\"ALL\",\"robotestCase\":\"ALL\",\"robotestClass\":\"*\",\"robotestMethod\":\"*\",\"enabled\":true}]\n");
            testSuitesJenkins1.write("enableField=enabled\n");
            testSuitesJenkins1.write("groupBy=robotestSuite\n");
            testSuitesJenkins1.write("fieldSeparator=.\n");
            testSuitesJenkins1.write("showFields=robotestCase\n");
        } catch (IOException e) {
            this.getLog().error(e);
            throw new MojoExecutionException("ERROR IO", e);
        }

    }

    private void findClasses(final File rootFile,
                             final File file,
                             final FileWriter testSuitesJk1,
                             final FileWriter testSuitesJk2,
                             final ClassLoader clazzLoader) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                for (File child : file.listFiles()) {
                    this.findClasses(rootFile, child, testSuitesJk1, testSuitesJk2, clazzLoader);
                }
            } else {
                if (file.getName().toLowerCase().endsWith(".class")) {
                    try {
                        Class klass = clazzLoader.loadClass(this.clazzFilePathToJavaPackage(rootFile, file));
                        this.getLog()
                            .info("SEARCH ROBOTEST ANNOTATIONS IN: " + klass.getPackage().getName() + File.separator
                                    + klass.getName());
                        if (klass.isAnnotationPresent(RobotestSuite.class)) {
                            RobotestSuite suiteAnnot = (RobotestSuite) klass.getAnnotation(RobotestSuite.class);
                            for (int i = 0; i < klass.getMethods().length; i++) {
                                Method method = klass.getMethods()[i];
                                if (method.isAnnotationPresent(RobotestCase.class)) {
                                    RobotestCase annotCase = method.getAnnotation(RobotestCase.class);
                                    testSuitesJk1.write("{\"robotestSuite\":\"" + suiteAnnot.tag()
                                            + "\",\"robotestCase\":\"" + annotCase.tag() + "\",\"robotestClass\":\""
                                            + this.clazzFilePathToJavaPackage(rootFile, file)
                                            + "\",\"robotestMethod\":\"" + method.getName() + "\",\"enabled\":true},");
                                    testSuitesJk2.write(",{\"robotestSuite\":\"" + suiteAnnot.tag()
                                            + "\",\"robotestCase\":\"" + annotCase.tag() + "\",\"robotestClass\":\""
                                            + this.clazzFilePathToJavaPackage(rootFile, file)
                                            + "\",\"robotestMethod\":\"" + method.getName() + "\"}");
                                    this.getLog()
                                        .info("{\"robotestSuite\":\"" + suiteAnnot.tag() + "\",\"robotestCase\":\""
                                                + annotCase.tag() + "\",\"robotestClass\":\""
                                                + this.clazzFilePathToJavaPackage(rootFile, file)
                                                + "\",\"robotestMethod\":\"" + method.getName() + "\"}");
                                }
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        this.getLog().error(e);
                    }

                }
            }
        }
    }

    private String clazzFilePathToJavaPackage(final File rootFile, final File file) {
        return file.getAbsolutePath()
                   .replace(".class", "")
                   .replace(rootFile.getAbsolutePath() + File.separator, "")
                   .replaceAll("\\" + File.separator, ".");
    }
}
