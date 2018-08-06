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
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.castinfo.devops.robotest.RobotestException;

/**
 * Wrapper of most used Selenium operations management (windows/tabs, clicks, pointer move and elements management).
 *
 */
public abstract class SeleniumElementsWrapper extends SeleniumBrowserResourcesWrapper {

    /**
     * Wrapper of Selenium findElement basic ops.
     *
     * @param search
     *            By search
     * @return WebElement The element finded
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public WebElement findElementBy(final By search) throws RobotestException {
        return getDriver().findElement(search);
    }

    /**
     * Wrapper of Selenium findElements basic ops.
     *
     * @param search
     *            By search
     * @return List of elements finded
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public List<WebElement> findElementsBy(final By search) throws RobotestException {
        return getDriver().findElements(search);
    }

    /**
     * Returns true if search element is present in entire present page scope.
     *
     * @param by
     *            By search
     * @return true if present
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public boolean isElementPresent(final By by) throws RobotestException {
        boolean resultado = true;
        try {
            getDriver().findElement(by);
        } catch (NoSuchElementException e) {
            resultado = false;
        }
        return resultado;
    }

    /**
     * Search if element is present inside another element scope.
     *
     * @param element
     *            The parent element to inspect.
     * @param by
     *            By search.
     * @return true if present.
     */
    public boolean isElementPresent(final WebElement element,
                                    final By by) {
        boolean resultado = true;
        try {
            element.findElement(by);
        } catch (NoSuchElementException e) {
            resultado = false;
        }
        return resultado;
    }

    /**
     * Searh if element exists and is visible (not hidden) in entire present page scope.
     *
     * @param by
     *            By search
     * @return true if visible and exists.
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public boolean isElementVisible(final By by) throws RobotestException {
        boolean resultado = false;
        try {
            WebElement element = getDriver().findElement(by);
            resultado = element != null && element.isDisplayed();
        } catch (NoSuchElementException e) {
            // false
        }
        return resultado;
    }

    /**
     * Searh for element inside another element and is visible (not hidden).
     *
     * @param element
     *            Parent element
     * @param by
     *            By search
     * @return true if exists and visible
     */
    public boolean isElementVisible(final WebElement element,
                                    final By by) {
        boolean resultado = false;
        try {
            element.findElement(by);
            resultado = element.findElement(by)
                               .isDisplayed();
        } catch (NoSuchElementException e) {
            resultado = false;
        }
        return resultado;
    }

    /**
     * Search in time period, if element exist. If 0 timeout passed, will do not wait.
     *
     * @param by
     *            By search
     * @param seconds
     *            timeout
     * @return true if exist
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public boolean isElementPresentUntil(final By by,
                                         final long seconds) throws RobotestException {
        boolean resultado = true;
        try {
            new WebDriverWait(getDriver(),
                              seconds).until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (TimeoutException e) {
            resultado = false;
        }
        return resultado;
    }

    /**
     * Search for element visible (not hidden) in wait time period. If 0 timeout passed, will do not wait.
     *
     * @param by
     *            By search
     * @param seconds
     *            timeout
     * @return true if element visible
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public boolean isElementVisibleUntil(final By by,
                                         final long seconds) throws RobotestException {
        boolean resultado = true;
        try {
            new WebDriverWait(getDriver(),
                              seconds).until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (TimeoutException e) {
            resultado = false;
        }
        return resultado;
    }

    /**
     * Search for element hidden (not visible) in wait time period. If 0 timeout passed, will do not wait.
     *
     * @param by
     *            By search
     * @param seconds
     *            timeout
     * @return true if element hidden but exists
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public boolean isElementInvisibleUntil(final By by,
                                           final long seconds) throws RobotestException {
        boolean resultado = true;
        try {
            new WebDriverWait(getDriver(),
                              seconds).until(ExpectedConditions.invisibilityOfElementLocated(by));
        } catch (TimeoutException e) {
            resultado = false;
        }
        return resultado;
    }

    /**
     * Count visible (not hidden) elements of any search.
     *
     * @param by
     *            By search.
     * @return num elements visibles found.
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public int getNumElementsVisible(final By by) throws RobotestException {
        int numberOfElements = 0;
        for (WebElement element : getDriver().findElements(by)) {
            if (element.isDisplayed()) {
                numberOfElements += 1;
            }
        }
        return numberOfElements;
    }

    /**
     * Returns the list of visible elements in a search.
     *
     * @param by
     *            By search
     * @return List of visible elements found.
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public List<WebElement> getElementsVisible(final By by) throws RobotestException {
        List<WebElement> listaReturn = new ArrayList<>();
        for (WebElement element : getDriver().findElements(by)) {
            if (element.isDisplayed()) {
                listaReturn.add(element);
            }
        }
        return listaReturn;
    }

    /**
     * An expectation for checking WebElement with given locator has attribute which contains specific value.
     *
     * @param element
     *            used to check it's parameters
     * @param attribute
     *            used to define css or HTML attribute
     * @param value
     *            used as expected attribute value
     * @return Boolean true when element has css or HTML attribute which contains the value
     */
    public boolean attributeContains(final WebElement element,
                                     final String attribute,
                                     final String value) {
        boolean contains = false;
        String currentValue = null;
        try {
            currentValue = element.getAttribute(attribute);
            if (currentValue == null || currentValue.isEmpty()) {
                currentValue = element.getCssValue(attribute);
            }
            contains = currentValue.contains(value);
        } catch (Exception e) {
            // ignore
        }
        return contains;

    }

    /**
     * Validate if search element is clicable in a time period. If 0 timeout passed, will do not wait.
     *
     * @param by
     *            By search
     * @param seconds
     *            timeout
     * @return true if clicable
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public boolean isElementClickableUntil(final By by,
                                           final long seconds) throws RobotestException {
        boolean resultado = true;
        try {
            new WebDriverWait(getDriver(),
                              seconds).until(ExpectedConditions.elementToBeClickable(by));
        } catch (TimeoutException e) {
            resultado = false;
        }
        return resultado;
    }

    /**
     * Validate if search element is NOT clicable in a time period. If 0 timeout passed, will do not wait.
     *
     * @param by
     *            By search
     * @param seconds
     *            timeout
     * @return true if not clicable
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public boolean isElementNoClickableUntil(final By by,
                                             final long seconds) throws RobotestException {
        boolean resultado = true;
        try {
            new WebDriverWait(getDriver(),
                              seconds).until(ExpectedConditions.not(ExpectedConditions.elementToBeClickable(by)));
        } catch (TimeoutException e) {
            resultado = false;
        }
        return resultado;
    }

    /**
     * Click and expect to load page event.
     *
     * @param link
     *            element to click
     * @param pageLoadingWaitSeconds
     *            time
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public void clickUntilLoadPage(final WebElement link,
                                   final long pageLoadingWaitSeconds) throws RobotestException {
        click(link);
        waitForPageLoaded(pageLoadingWaitSeconds);
    }

    /**
     * Forzes click of search element. First do wait for element clicable and after, do the click, expecting page
     * loading event. If 0 timeout passed, will do not wait.
     *
     * @param waitForLinkToClick
     *            timeout element clicable
     * @param by
     *            By search
     * @param pageLoadingWaitSeconds
     *            timeout page loading
     * @return true si se ha cargado el link clicado.
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public boolean clickUntilLoadPage(final long waitForLinkToClick,
                                      final By by,
                                      final long pageLoadingWaitSeconds) throws RobotestException {
        boolean timeout = false;
        if (waitForLinkToClick > 0) {
            try {
                new WebDriverWait(getDriver(),
                                  waitForLinkToClick).until(ExpectedConditions.elementToBeClickable(by));
            } catch (TimeoutException e) {
                timeout = true;
            }
        }
        if (!timeout) {
            WebElement link = getDriver().findElement(by);
            click(link);
            waitForPageLoaded(pageLoadingWaitSeconds);
        }
        return timeout;
    }

    /**
     * Do a click without wait.
     *
     * @param link
     *            clicable element.
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public void click(final WebElement link) throws RobotestException {
        try {
            link.click();
        } catch (WebDriverException e) {
            try {
                JavascriptExecutor executor = (JavascriptExecutor) getDriver();
                executor.executeScript("arguments[0].click();",
                                       link);
            } catch (JavascriptException e2) {
                throw new RobotestException("NOT CLICABLE ELEMENT",
                                            e2);
            }
        }
    }

    /**
     * Move to element.
     *
     * @param search
     *            finded element
     * @throws RobotestException
     *             if Selenium Driver unavailable
     */
    public void moveToElement(final WebElement search) throws RobotestException {
        try {
            Actions mover = new Actions(getDriver());
            mover.moveToElement(search)
                 .build()
                 .perform();
        } catch (MoveTargetOutOfBoundsException e) {
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);",
                                                             search);
        }
    }

    /**
     * Move to link and perform click.
     *
     * @param link
     *            link element
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public void moveToLinkAndClick(final WebElement link) throws RobotestException {
        moveToElement(link);
        click(link);
    }

    /**
     * Retries TEXT field input for n times, expecting introducing TEXT is OK (prevent Selenium API bugs).
     *
     * @param numRetry
     *            max retries
     * @param by
     *            Text target
     * @param keys
     *            The TEXT.
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public void sendKeysWithRetry(final int numRetry,
                                  final By by,
                                  final String keys) throws RobotestException {
        int i = 0;
        do {
            getDriver().findElement(by)
                       .clear();
            getDriver().findElement(by)
                       .sendKeys(keys);
            i += 1;
        } while (getDriver().findElement(by)
                            .getAttribute("value")
                            .compareTo(keys) != 0
                && i < numRetry);
    }

    /**
     * Select a option value in a selectable form.
     *
     * @param selectElement
     *            Select element
     * @param value
     *            Value to search
     * @throws RobotestException
     *             Selenium Driver unavailable or error
     */
    public void selectOption(final WebElement selectElement,
                             final String value) throws RobotestException {
        if (selectElement.getAttribute("value")
                         .compareTo(value) != 0) {
            selectElement.sendKeys(value);
            if (selectElement.getAttribute("value")
                             .compareTo(value) != 0) {
                JavascriptExecutor executor = (JavascriptExecutor) getDriver();
                executor.executeScript("const textToFind = '" + value + "';" + "const dd = arguments[0];"
                        + "dd.selectedIndex = [...dd.options].findIndex (option => option.text === textToFind);",
                                       selectElement);
            }
        }
    }

}
