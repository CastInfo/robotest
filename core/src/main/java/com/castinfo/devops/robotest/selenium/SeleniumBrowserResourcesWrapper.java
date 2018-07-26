/********************************************************************************
 * ROBOTEST
 * Copyright (C) 2018 CAST-INFO, S.A. www.cast-info.es
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package com.castinfo.devops.robotest.selenium;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.castinfo.devops.robotest.RobotestException;
import com.castinfo.devops.robotest.config.ConfigurationAccess;
import com.castinfo.devops.robotest.report.ValidationEntry;

/**
 * Selenium Wrapper around browser resources management (windows/tabs/alerts, cookies, screenshots and logs).
 *
 */
public abstract class SeleniumBrowserResourcesWrapper extends ConfigurationAccess {

    /**
     * Get the Selenium Driver already loaded and configured.
     *
     * @return the driver
     * @throws RobotestException
     *             Configuration initialition related problems.
     */
    public abstract WebDriver getDriver() throws RobotestException;

    /**
     * Open passed URL, in browser.
     *
     * @param url
     *            url
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public void openURL(final String url) throws RobotestException {
        getDriver().get(url);
    }

    /**
     * Open URL in browser and expect the load event.
     *
     * @param url
     *            url
     * @param timeoutInSeconds
     *            timeout in seconds
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public void openURLAndWaitLoad(final String url, final int timeoutInSeconds) throws RobotestException {
        openURL(url);
        waitForPageLoaded(timeoutInSeconds);
    }

    /**
     * Do wait for page load, this is not valid for AJAX invocations, in this cases expect presence of promise Ajax
     * element page modifications. If timeout, escape in browser page loading is triggered.
     *
     * @param waitSeconds
     *            timout
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public void waitForPageLoaded(final long waitSeconds) throws RobotestException {
        ExpectedCondition<Boolean> expectation = webdriverParam -> {
            Boolean pageLoaded = Boolean.FALSE;
            try {
                JavascriptExecutor jsExe = (JavascriptExecutor) webdriverParam;
                Object noSafari = jsExe.executeScript("return window.performance.timing.domContentLoadedEventEnd");
                Object safari = jsExe.executeScript("return document.readyState");
                if ("complete".equals(safari) && !"0".equals(noSafari.toString())) {
                    pageLoaded = Boolean.TRUE;
                }
            } catch (JavascriptException e) {
                // always false
            }
            return pageLoaded;
        };
        try {
            new WebDriverWait(getDriver(),
                              waitSeconds).until(expectation);
        } catch (TimeoutException e) {
            Actions action = new Actions(getDriver());
            action.sendKeys("Keys.ESCAPE").build().perform();
            throw new RobotestException("LOADING PAGE TIMEOUT",
                                        e);
        }
    }

    /**
     * Open link in a new browser tab.
     *
     * @param by
     *            By search
     * @param timeoutSeconds
     *            timeout
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public void openLinkInNewTab(final By by, final long timeoutSeconds) throws RobotestException {
        Actions a = new Actions(getDriver());
        WebElement e = getDriver().findElement(by);
        a.moveToElement(e).keyDown(Keys.CONTROL).click().build().perform();
        switchToAnotherWindow();
        waitForPageLoaded(timeoutSeconds);
    }

    /**
     * Open URL in a new browser tab with id.
     *
     * @param urlToLoad
     *            url
     * @param tabId
     *            id tab
     * @param timeoutInSeconds
     *            timeout
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     *
     */
    public void loadUrlInTabId(final String urlToLoad,
                               final String tabId,
                               final int timeoutInSeconds) throws RobotestException {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("window.open('" + urlToLoad + "', '" + tabId + "');");
        getDriver().switchTo().window(tabId);
        waitForPageLoaded(timeoutInSeconds);
    }

    /**
     * Close tab with id.
     *
     * @param tabId
     *            tab id.
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public void closeTabById(final String tabId) throws RobotestException {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("window.close('" + tabId + "');");
    }

    /**
     * Gets actual browser opened URL.
     *
     * @return Url actual url.
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public String getCurrentUrl() throws RobotestException {
        return getDriver().getCurrentUrl();
    }

    /**
     * Search in time period, if TEXT exist in current opened URL. If 0 timeout passed, will do not wait.
     *
     * @param toSearchInUrl
     *            the search
     * @param seconds
     *            timeout
     * @return true if exist TEXT in the current url.
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public boolean currentURLContains(final String toSearchInUrl, final long seconds) throws RobotestException {
        ExpectedCondition<Boolean> currentURLContains =
                webdriverApplyParam -> Boolean.valueOf(webdriverApplyParam.getCurrentUrl().contains(toSearchInUrl));
        boolean resultado = true;
        try {
            new WebDriverWait(getDriver(),
                              seconds).until(currentURLContains);
        } catch (TimeoutException e) {
            resultado = false;
        }
        return resultado;
    }

    /**
     * Validates in time period, if browser window title exist. If 0 timeout passed, will do not wait.
     *
     * @param title
     *            title
     * @param seconds
     *            timeout
     * @return true if exist.
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public boolean pageTitleContainsUntil(final String title, final long seconds) throws RobotestException {
        boolean resultado = true;
        try {
            new WebDriverWait(getDriver(),
                              seconds).until(ExpectedConditions.titleContains(title));
        } catch (TimeoutException e) {
            resultado = false;
        }
        return resultado;
    }

    /**
     * Validates if some alert/confirm dialog is open.
     *
     * @return true if exists.
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public boolean isAlertPresent() throws RobotestException {
        boolean resultado = true;
        try {
            getDriver().switchTo().alert();
        } catch (NoAlertPresentException e) {
            resultado = false;
        }
        return resultado;
    }

    /**
     * Returns alert/confirm showed TEXT.
     *
     * @param clickAccept
     *            true if will do an accept action / false if cancel action wil be triggered.
     * @return El alert/confirm TEXT.
     * @throws RobotestException
     *             if Selenium Driver unavailable
     */
    public String closeAlertAndGetItsText(final boolean clickAccept) throws RobotestException {
        Alert alert = getDriver().switchTo().alert();
        String alertText = alert.getText();
        if (clickAccept) {
            alert.accept();
        } else {
            alert.dismiss();
        }

        return alertText;
    }

    /**
     * Do focus in first no-principal.
     *
     * @return Window identificator.
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public String switchToAnotherWindow() throws RobotestException {
        String popupHandle = "";
        Set<String> s = getDriver().getWindowHandles();
        Iterator<String> ite = s.iterator();
        while (ite.hasNext()) {
            popupHandle = ite.next();
            if (!popupHandle.contains(getDriver().getWindowHandle())) {
                getDriver().switchTo().window(popupHandle);
                break;
            }
        }
        return popupHandle;
    }

    /**
     * Do focus in firt non-principal window that have searched window title.
     *
     * @param title
     *            window title to search
     * @return true if window with this title exist.
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public boolean switchToWindowThatContainsTitle(final String title) throws RobotestException {
        String windowParent = getDriver().getWindowHandle();
        Iterator<String> availableWindows = getDriver().getWindowHandles().iterator();
        boolean resultado = false;
        while (!resultado && availableWindows.hasNext()) {
            String window = availableWindows.next();
            if (!windowParent.equals(window)) {
                getDriver().switchTo().window(window);
                if (getDriver().getTitle().toLowerCase().contains(title)) {
                    resultado = true;
                } else {
                    getDriver().switchTo().window(windowParent);
                }
            }
        }
        return resultado;
    }

    /**
     * Add cookie to browser.
     *
     * @param cookieName
     *            name of cookie
     * @param cookieValue
     *            desired value.
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public void addCookie(final String cookieName, final String cookieValue) throws RobotestException {
        Cookie ck = new Cookie(cookieName,
                               cookieValue);
        getDriver().manage().addCookie(ck);
    }

    /**
     * Get cookie value of a cookie if exists.
     *
     * @param cookieName
     *            name of cookie
     * @return String value of cookie, null if not exist
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public String getCookieValueByName(final String cookieName) throws RobotestException {
        String resultado = null;
        Cookie c = getDriver().manage().getCookieNamed(cookieName);
        if (null != c) {
            resultado = c.getValue();
        }
        return resultado;
    }

    /**
     * List of cookies.
     *
     * @return list of cookies
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public Set<Cookie> listOfCookiesAvailable() throws RobotestException {
        return getDriver().manage().getCookies();
    }

    /**
     * Makes browser screenshoot and return in bytes.
     *
     * @return bytes screeshoot captured
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public byte[] takeScreenShoot() throws RobotestException {
        try {
            WebDriver captureDriver = getDriver();
            if (captureDriver instanceof RemoteWebDriver) {
                new Augmenter().augment(captureDriver);
            }
            return ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
        } catch (WebDriverException e) {
            throw new RobotestException("CAPTURE SCREEN SHOOT ERROR",
                                        e);
        }

    }

    /**
     * Makes browser present HTML page source capture and return in String.
     *
     * @return HTML file captured in a String
     * @throws RobotestException
     *             if Selenium Driver unavailable or error
     */
    public String takePageSource() throws RobotestException {
        try {
            return getDriver().getPageSource();
        } catch (WebDriverException e) {
            throw new RobotestException("CAPTURE PAGE SOURCE ERROR",
                                        e);
        }
    }

    /**
     * Returns a limited list of ValidationEntrys of WebDriver visible log from browser CONSOLE with desired level of
     * any kind (JavaScript, CSS, network, etc). The retrived Level status equivalence is WARNING for WARNING, ERROR for
     * &gt; WARNING &amp; INFO for &lt; WARNING.
     *
     * @param desiredLogLevel
     *            log level desired
     * @return The retrived browser CONSOLE log list
     * @throws RobotestException
     *             Errors in the log retrive with web driver.
     */
    public List<ValidationEntry> takeBrowserConsoleLogs(final Level desiredLogLevel) throws RobotestException {
        List<ValidationEntry> resultado = new ArrayList<>();
        ValidationEntry tmpValidationEntry = null;
        try {
            Logs logs = getDriver().manage().logs();
            Iterator<String> itTiposLog = logs.getAvailableLogTypes().iterator();
            while (itTiposLog.hasNext()) {
                String tipoLog = itTiposLog.next();
                LogEntries logEntries = logs.get(tipoLog);
                for (LogEntry logEntry : logEntries) {
                    if (logEntry.getLevel().intValue() >= desiredLogLevel.intValue()) {
                        if (Level.WARNING.intValue() < logEntry.getLevel().intValue()) {
                            tmpValidationEntry = ValidationEntry.buildError().withConsole(logEntry.getMessage());
                        } else if (Level.WARNING.intValue() > logEntry.getLevel().intValue()) {
                            tmpValidationEntry = ValidationEntry.buildInfo().withConsole(logEntry.getMessage());
                        } else {
                            tmpValidationEntry = ValidationEntry.buildWarning().withConsole(logEntry.getMessage());
                        }
                        resultado.add(tmpValidationEntry);
                    }
                }
            }
        } catch (WebDriverException e) {
            throw new RobotestException("ERROR RETRIVING LOGS",
                                        e);
        }
        return resultado;
    }

}
