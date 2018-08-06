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

import java.lang.annotation.Annotation;
import java.util.List;

import com.castinfo.devops.robotest.RobotestException;
import com.castinfo.devops.robotest.annot.RobotestConfig;
import com.castinfo.devops.robotest.annot.RobotestSuite;
import com.castinfo.devops.robotest.report.ConfigEntry;

/**
 * Interface of robotest configuration, contract to provide diferent implementations of configurations if needed.
 *
 */
public interface IRobotestConfiguration {

    /**
     * Load basic configuration for Suite marshalled in RobotestBasicConfig object. There are some customized by user
     * System properties added to this config object defined in RobotestConfigKeys:
     *
     * <pre>
     *
     * ROBOTEST_REPORT_BASE: path of the ROBOTEST generated report, defaults to "java.io.tmpdir" Java System Property.
     * ROBOTEST_ENV: environment name, feel free, if no value robotest defaults to local.
     * ROBOTEST_BROWSER: browser FIREFOX, CHROME (default), IPAD, IPHONE and ANDROID are supported
     * ROBOTEST_BROWSER_HEADLESS: true if you wan't headleass execution, only CHROME &gt; 58 and FIREFOX &lt; 59
     * ROBOTEST_BROWSER_WIDTH: Browser windows width size, default 1024
     * ROBOTEST_BROWSER_HEIGHT: Browser windows height size, default 768
     * ROBOTEST_BROWSER_MAXIMIZED: True if browser open in maximized mode
     * ROBOTEST_BROWSER_CONSOLE_LOG_LEVEL: Warning by default, values accepted are java.util.logging.Level values.
     * ROBOTEST_GENERAL_TIMEOUT: timeout general for Selenium Driver Api utilities, defaults to 1000ms.
     * ROBOTEST_DOCKER_CONN: Docker host for example for local typical Docker installation is tcp://192.168.99.100:2376
     * or unix:///var/run/docker.sock
     * ROBOTEST_DOCKER_PUBLIC_HOST: Docker public ip or dns host for selenium hub docker based connection
     * ROBOTEST_DOCKER_CERTS: Docker certs path of docker remote host, defaults to "user.dir" Java System Property +
     * .docker/machine/certs.
     * ROBOTEST_DOCKER_DEBUG_PORT: Docker port for remote debug purposes with VNC, for example 5900.
     * ROBOTEST_CHROME_DOCKER_IMG_TAG: This is managed by default by robotest, but you can forze especific tag.
     * ROBOTEST_CHROME_DEBUG_DOCKER_IMG_TAG: This is managed by default by robotest, but you can forze especific tag.
     * ROBOTEST_FIREFOX_DOCKER_IMG_TAG: This is managed by default by robotest, but you can forze especific tag.
     * ROBOTEST_FIREFOX_DEBUG_DOCKER_IMG_TAG: This is managed by default by robotest, but you can forze especific tag.
     * ROBOTEST_BROWSERSTACK_USER_NAME: Browser Stack username
     * ROBOTEST_BROWSERSTACK_ACCESS_KEY: Browser Stack accessKey
     * ROBOTEST_BROWSERSTACK_DEVICE: See device list
     * https://www.browserstack.com/list-of-browsers-and-platforms?product=automate.
     * ROBOTEST_BROWSERSTACK_PLATFORM: See platform list
     * https://www.browserstack.com/list-of-browsers-and-platforms?product=automate.
     *
     * Related with Web Driver Manager integration (https://github.com/bonigarcia/webdrivermanager)
     *
     * wdm.geckoDriverVersion Forze version of native FIREFOX driver manager download for specific user installed
     * FIREFOX versions
     * wdm.chromeDriverVersion Forze version of native CHROME driver manager download for specific user installed CHROME
     * versions
     * wdm.targetPath Native driver manager download directory
     *
     * </pre>
     *
     * Docker takes precendence of browserstack, if two configurations set, Docker WebDriver will be selected to avoid
     * problems in CI *nix environments.
     *
     * @param suiteAnnot
     *            annotSuite
     */
    void loadBasic(RobotestSuite suiteAnnot);

    /**
     * Load user annotation configurations for Suite, Cases and Steps.
     *
     * @param cfgAnnot
     *            Annotation config.
     * @param scope
     *            Type anotation suite,case,step
     * @throws RobotestException
     *             errores de lectura o carga de configuraciones
     */
    void loadAnnotationScopeConfig(RobotestConfig[] cfgAnnot,
                                   Annotation scope) throws RobotestException;

    /**
     * Gets de basic/system configuration.
     *
     * @return basic cfg
     */
    RobotestBasicConfig getConfigBasic();

    /**
     * Gets one user configuration object.
     *
     * @param scope
     *            test scope, annotation suite, case or step
     *
     * @param key
     *            key confing
     * @param <T>
     *            generic type to be cast
     * @return Config Object
     */
    <T> T getAnnotationScopeCfg(Annotation scope,
                                String key);

    /**
     * Reporting purposes adapter.
     *
     * @param scope
     *            Robotest Anotation: suite, case and step.
     * @return Value list of this scope.
     */
    List<ConfigEntry> toReportConfigEntries(Annotation scope);

}