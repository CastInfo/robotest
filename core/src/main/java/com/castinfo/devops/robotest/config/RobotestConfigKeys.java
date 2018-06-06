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
package com.castinfo.devops.robotest.config;

/**
 * Config keys contants.
 *
 */
public final class RobotestConfigKeys {

    private RobotestConfigKeys() {
        // no instantiable
    }

    /**
     * Browser stack access key robotest parameter.
     */
    public static final String ROBOTEST_BROWSERSTACK_ACCESS_KEY = "ROBOTEST_BROWSERSTACK_ACCESS_KEY";
    /**
     * Browser stack user name robotest parameter.
     */
    public static final String ROBOTEST_BROWSERSTACK_USER_NAME = "ROBOTEST_BROWSERSTACK_USER_NAME";
    /**
     * Browser stack platform robotest parameter.
     */
    public static final String ROBOTEST_BROWSERSTACK_PLATFORM = "ROBOTEST_BROWSERSTACK_PLATFORM";
    /**
     * Browser stack device robotest parameter.
     */
    public static final String ROBOTEST_BROWSERSTACK_DEVICE = "ROBOTEST_BROWSERSTACK_DEVICE";
    /**
     * Selenium docker FIREFOX debug image robotest parameter.
     */
    public static final String ROBOTEST_FIREFOX_DEBUG_DOCKER_IMG_TAG = "ROBOTEST_FIREFOX_DEBUG_DOCKER_IMG_TAG";
    /**
     * Selenium docker FIREFOX image robotest parameter.
     */
    public static final String ROBOTEST_FIREFOX_DOCKER_IMG_TAG = "ROBOTEST_FIREFOX_DOCKER_IMG_TAG";
    /**
     * Selenium docker CHROME debug image robotest parameter.
     */
    public static final String ROBOTEST_CHROME_DEBUG_DOCKER_IMG_TAG = "ROBOTEST_CHROME_DEBUG_DOCKER_IMG_TAG";
    /**
     * Selenium docker CHROME image robotest parameter.
     */
    public static final String ROBOTEST_CHROME_DOCKER_IMG_TAG = "ROBOTEST_CHROME_DOCKER_IMG_TAG";
    /**
     * Selenium docker debug port robotest parameter.
     */
    public static final String ROBOTEST_DOCKER_DEBUG = "ROBOTEST_DOCKER_DEBUG";
    /**
     * Selenium docker connection robotest parameter.
     */
    public static final String ROBOTEST_DOCKER_CONN = "ROBOTEST_DOCKER_CONN";
    /**
     * Selenium docker certs path robotest parameter.
     */
    public static final String ROBOTEST_DOCKER_CERTS = "ROBOTEST_DOCKER_CERTS";
    /**
     * Selenium docker tls use robotest parameter.
     */
    public static final String ROBOTEST_DOCKER_TLS = "ROBOTEST_DOCKER_TLS";
    /**
     * Selenium docker public host robotest parameter.
     */
    public static final String ROBOTEST_DOCKER_PUBLIC_HOST = "ROBOTEST_DOCKER_PUBLIC_HOST";
    /**
     * Docker Network mode
     */
    public static final String ROBOTEST_DOCKER_NETWORK = "ROBOTEST_DOCKER_NETWORK";
    /**
     * Docker exec tag container identifier
     */
    public static final String ROBOTEST_DOCKER_CONTAINER_EXEC_TAG = "ROBOTEST_DOCKER_CONTAINER_EXEC_TAG";
    /**
     * Docker container labels
     */
    public static final String ROBOTEST_DOCKER_LABELS = "ROBOTEST_DOCKER_LABELS";
    /**
     * Selenium docker browser CONSOLE log level robotest parameter.
     */
    public static final String ROBOTEST_BROWSER_CONSOLE_LOG_LEVEL = "ROBOTEST_BROWSER_CONSOLE_LOG_LEVEL";
    /**
     * General timeout robotest parameter.
     */
    public static final String ROBOTEST_GENERAL_TIMEOUT_MILLIS = "ROBOTEST_GENERAL_TIMEOUT";
    /**
     * Browser name robotest parameter.
     */
    public static final String ROBOTEST_BROWSER = "ROBOTEST_BROWSER";
    /**
     * Browser headless robotest parameter.
     */
    public static final String ROBOTEST_BROWSER_HEADLESS = "ROBOTEST_BROWSER_HEADLESS";
    /**
     * Browser window width robotest parameter.
     */
    public static final String ROBOTEST_BROWSER_WIDTH = "ROBOTEST_BROWSER_WIDTH";
    /**
     * Browser window height robotest parameter.
     */
    public static final String ROBOTEST_BROWSER_HEIGHT = "ROBOTEST_BROWSER_HEIGHT";
    /**
     * Browser window maximized robotest parameter.
     */
    public static final String ROBOTEST_BROWSER_MAXIMIZED = "ROBOTEST_BROWSER_MAXIMIZED";
    /**
     * Environmet name robotest parameter.
     */
    public static final String ROBOTEST_ENV = "ROBOTEST_ENV";
    /**
     * Robotest report base path robotest parameter.
     */
    public static final String ROBOTEST_REPORT_BASE = "ROBOTEST_REPORT_BASE";
    /**
     * Robotest browser proxy
     */
    public static final String ROBOTEST_BROWSER_PROXY = "ROBOTEST_BROWSER_PROXY";

}
