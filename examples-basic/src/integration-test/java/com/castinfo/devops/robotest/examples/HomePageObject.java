package com.castinfo.devops.robotest.examples;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.xpath.XPathExpressionException;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.castinfo.devops.robotest.PageObject;
import com.castinfo.devops.robotest.RobotestException;
import com.castinfo.devops.robotest.annot.RobotestStep;
import com.castinfo.devops.robotest.config.RobotestConfiguration;

/**
 * Test Cases related to Cast-Info Home Page
 *
 * @author Jordi.Artal
 *
 */
public class HomePageObject extends PageObject {

    public static final String PRE_HOME_CFG = "PRE_HOME_CFG";
    private static final Logger LOG = LoggerFactory.getLogger(RobotestConfiguration.class);

    /**
     * Compares current page title with a literal expression
     *
     * @throws RobotestException
     */
    @RobotestStep(tag = "HOME_STEP_001", description = "Check home title", captureScreenShootAtEndStep = true)
    public void checkTitle() throws RobotestException {
        String parameter = "Cast Info | Cast Info s.a > Soluciones y Servicios tecnológicos de Vanguardia";
        if (!this.getDriver().getTitle().equals(parameter)) {
            throw new RobotestException("Screen doesn't contains this Title: " + parameter);
        }
    }

    /**
     * Performs search with a String
     *
     * @throws RobotestException
     */
    @RobotestStep(tag = "HOME_STEP_002", description = "Check search Contacto", captureScreenShootAtEndStep = true)
    public void checkSearchContacto() throws RobotestException {
        String searchParameter = "contacto";
        this.getDriver().manage().window().maximize();
        WebElement search = this.getDriver().findElement(By.id("et_top_search"));
        if (search != null) {
            this.click(search);
            WebElement input = this.getDriver().findElement(By.tagName("input"));
            if (input != null) {
                input.clear();
                input.sendKeys(searchParameter);
                input.submit();
            } else {
                throw new RobotestException("There is no input parameter in Search Form");
            }
        } else {
            throw new RobotestException("There is none Search Form");
        }
    }

    /**
     * Confirms that Contacto page exists
     *
     * @throws RobotestException
     */
    @RobotestStep(tag = "HOME_STEP_003", description = "Check go Contacto Page", captureScreenShootAtEndStep = true)
    public void checkGoContacto() throws RobotestException {
        List<WebElement> results = this.getDriver().findElements(By.id("left-area"));
        if (results != null) {
            List<WebElement> hrefs = results.get(0).findElements(By.tagName("a"));
            if (hrefs != null) {
                StringBuffer sb = new StringBuffer("https://www.cast-info.es/contacto/");
                for (WebElement element : hrefs) {
                    if (element.getAttribute("href").contentEquals(sb)) {
                        this.openURL("https://www.cast-info.es/contacto/");
                        break;
                    }
                }
            } else {
                throw new RobotestException("There is no HTTP 'a' elements finded");
            }
        } else {
            throw new RobotestException("No search results");
        }
    }

    /**
     * Assertion that Contacto Form exists
     *
     * @throws XPathExpressionException
     */
    @RobotestStep(tag = "HOME_STEP_004", description = "Check Contacto form exists", captureScreenShootAtEndStep = true)
    public void checkFormExists() {
        /*
         * RobotestApiTestingClient restClient = new RobotestApiTestingClient(); String templateUrl =
         * "https://www.cast-info.es/contacto/"; String response =
         * XmlResponseValidations.getXPathForHtml(restClient.getForString(templateUrl),
         * "//form[@class='et_pb_contact_form clearfix']"); if (response == null) { throw new
         * RobotestApiTestingException("The is no Search Form"); }
         */
    }

    /**
     * Fills Contacto form boxes
     *
     * @throws RobotestException
     */
    @RobotestStep(tag = "HOME_STEP_005", description = "Check Contacto form works", captureScreenShootAtEndStep = true)
    public void checkUseForm() throws RobotestException {
        WebElement name = this.getDriver().findElement(By.id("et_pb_contact_name_1"));
        name.clear();
        name.sendKeys("test name");
        Assert.assertTrue("insertion error in contact name", name.getAttribute("value").equals("Nombre Prueba"));
        WebElement email = this.getDriver().findElement(By.id("et_pb_contact_email_1"));
        email.clear();
        email.sendKeys("test eMail");
        Assert.assertTrue("insertion error in contact eMail", email.getAttribute("value").equals("email Prueba"));
        WebElement message = this.getDriver().findElement(By.id("et_pb_contact_message_1"));
        message.clear();
        message.sendKeys("test message: Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer mauris erat, vulputate at fermentum ac, lacinia sed sem.");
        Assert.assertTrue("insertion error in contact message",
                          message.getAttribute("value")
                                 .equals("test message: Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer mauris erat, vulputate at fermentum ac, lacinia sed sem."));
    }

    /**
     * Verify that html element with id "top-menu" exists
     *
     * @throws RobotestException
     */
    @RobotestStep(tag = "HOME_STEP_006", description = "Check Top Menu exists", captureScreenShootAtEndStep = true)
    public void checkTopMenu() throws RobotestException {
        String parameter = "top-menu";
        this.getDriver().manage().window().maximize();
        Assert.assertTrue("element not found:" + parameter,
                          this.getDriver().findElement(By.id(parameter)).isDisplayed());
    }

    /**
     * go through top menu links and verify it
     *
     * @throws RobotestException
     */
    @RobotestStep(tag = "HOME_STEP_007", description = "Check top Menu Links", captureScreenShootAtEndStep = true)
    public void checkTopMenuLinks() throws RobotestException {
        List<WebElement> linkList = this.getDriver().findElements(By.xpath("//nav/ul/li/a[@href]"));
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
            throw new RobotestException("Ther is no elements in this navigation menu");
        }
    }

    /**
     * Overview of all Home page inner links
     *
     * @throws RobotestException
     */
    @RobotestStep(tag = "HOME_STEP_008",
                  description = "Check internal Links Home Page",
                  captureScreenShootAtEndStep = true)
    public void checkAllLinks() throws RobotestException {
        List<WebElement> linkList = this.getDriver().findElements(By.xpath("//a[@href]"));
        if (linkList != null) {
            List<String> hrefList = new ArrayList<>();
            for (WebElement elem : linkList) {
                String href = "https://www.cast-info.es";
                try {
                    href = elem.getAttribute("href");
                } catch (StaleElementReferenceException e) {
                    HomePageObject.LOG.error(e.toString());
                }
                if (href != null) {
                    hrefList.add(href);
                }
            }

            // Pages that exceeds getGeneralWaitTimoutSeconds has been excluded
            int i = 0;
            for (String href : hrefList) {
                if (!href.contains("@") && !href.equals("https://www.cast-info.es/")
                        && href.contains("https://www.cast-info.es/")
                        && !href.equals("https://www.cast-info.es/#contador")
                        && !href.equals("https://www.cast-info.es/productos/outsourcing")) {
                    if (!href.endsWith("/")) {
                        href += "/";
                    }
                    i++;
                    HomePageObject.LOG.info(i + " - Verifying link: " + href);
                    this.getDriver().navigate().to(href);
                    this.waitForPageLoaded(2L);
                    Assert.assertTrue("", this.currentURLContains(href, this.getGeneralWaitTimoutSeconds()));
                }
            }
        } else {
            throw new RobotestException("There is no links in this page");
        }

    }

    /**
     * Go to Home page slider buttons and verify its links
     *
     * @throws RobotestException
     */
    @RobotestStep(tag = "HOME_STEP_009",
                  description = "Check Slider Buttons Home Page",
                  captureScreenShootAtEndStep = true)
    public void checkSliderButtons() throws RobotestException {
        List<WebElement> listElem = this.getDriver().findElements(By.xpath("//div[@class='et-pb-controllers']/a"));
        if (!listElem.isEmpty()) {
            String parentWindow = this.getDriver().getWindowHandle();
            Boolean assertCondition = false;
            int i = 1;
            for (WebElement elem : listElem) {
                // go to the slide
                if (i > 1) {
                    this.getDriver().switchTo().window(parentWindow);
                }
                elem.click();

                // find individual slide and verify that is visible
                String className = "et_pb_slide_" + i;
                WebElement sliderElem = this.getDriver().findElement(By.className(className));
                assertCondition = sliderElem.getCssValue("opacity").equals("1");
                Assert.assertTrue("Slider elemnet num: " + i + " NO visible", assertCondition);

                // open slider link in a new Tab
                this.openLinkInNewTab(By.xpath("//*[@id=\"post-42\"]/div/div[1]/div/div[1]/div[" + i
                        + "]/div/div/div/a"), 1L);
                this.getDriver().getWindowHandle();
                i++;
            }
        }
    }

    /**
     * Make a Cookie list and list in log.info object
     *
     * @throws RobotestException
     */
    @RobotestStep(tag = "HOME_STEP_010", description = "Check Cookies", captureScreenShootAtEndStep = true)
    public void checkCastCookies() throws RobotestException {
        WebElement elem = this.findElementBy(By.xpath("//div[@id='cookie-law-info-bar']/span/a"));
        elem.click();
        Set<Cookie> listCookies = this.listOfCookiesAvailable();
        if (!listCookies.isEmpty()) {
            for (Cookie cookie : listCookies) {
                HomePageObject.LOG.info("cookie found: " + cookie.toString());
            }
        } else {
            HomePageObject.LOG.info("No cookies available");
        }
    }

    /**
     * Create a Cookie
     *
     * @throws RobotestException
     */
    @RobotestStep(tag = "HOME_STEP_011", description = "Check Create Cookie", captureScreenShootAtEndStep = true)
    public void checkCreateCookie() throws RobotestException {
        String name = "testCreateCookie";
        this.addCookie(name, "description=test value cookie");
        HomePageObject.LOG.info("cookie with: " + "testCreateCookie" + " and values: "
                + this.getCookieValueByName("testCreateCookie") + " has been created.");
    }

    /**
     * Do a Cookie list and erase all cookies one by one
     *
     * @throws RobotestException
     */
    @RobotestStep(tag = "HOME_STEP_012", description = "Check Cast Cookie", captureScreenShootAtEndStep = true)
    public void checkDeleteCastCookies() throws RobotestException {
        Set<Cookie> listCookies = this.listOfCookiesAvailable();
        for (Cookie cookie : listCookies) {
            this.getDriver().manage().deleteCookieNamed(cookie.getName());
            HomePageObject.LOG.info("cookie with name: '" + cookie.getName() + "' has been erased");
        }
    }

}
