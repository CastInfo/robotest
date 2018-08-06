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

import static io.restassured.RestAssured.when;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.castinfo.devops.robotest.RobotestException;
import com.castinfo.devops.robotest.annot.RobotestConfig;
import com.castinfo.devops.robotest.annot.RobotestSuite;
import com.castinfo.devops.robotest.report.ConfigEntry;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class load configuration resources for Suites, Cases and Steps. The resource protocol must be, classpath, file
 * or valid URL. Resource path may have word substituion with ${SYSTEM_PROPERTY} wildcards. The type of resource, must
 * be properties, xml or json. For readable test code purposes, ROBOTEST separates Basic configuration (to internal tool
 * configuration) to custom configurations (user needs), with two different doclets (RobotestBaseConfig and
 * RobotestConfig)
 */
public class RobotestConfiguration implements IRobotestConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(RobotestConfiguration.class);
    private RobotestBasicConfig basicCfg;
    private Map<Annotation, Map<String, ConfigurationEntry<?>>> testConfigEntries = new HashMap<>();

    /*
     * (non-Javadoc)
     *
     * @see com.castinfo.devops.robotest.config.IRobotestConfiguration#loadBasic(com.castinfo.devops.robotest.annot.
     * RobotestSuite)
     */
    @Override
    public void loadBasic(final RobotestSuite suiteAnnot) {
        basicCfg = new RobotestBasicConfig();
        System.setProperty(RobotestConfigKeys.ROBOTEST_REPORT_BASE,
                           getProperty(RobotestConfigKeys.ROBOTEST_REPORT_BASE,
                                       System.getProperty("java.io.tmpdir")));
        basicCfg.setReportFilePath(System.getProperty(RobotestConfigKeys.ROBOTEST_REPORT_BASE));
        RobotestConfiguration.LOG.info("ROBOTEST_REPORT_BASE: {}",
                                       basicCfg.getReportFilePath());

        System.setProperty(RobotestConfigKeys.ROBOTEST_ENV,
                           getProperty(RobotestConfigKeys.ROBOTEST_ENV,
                                       "local"));
        basicCfg.setEnv(System.getProperty(RobotestConfigKeys.ROBOTEST_ENV));
        RobotestConfiguration.LOG.info("ROBOTEST ENV: {}",
                                       basicCfg.getEnv());

        configHttp();
        configBrowser();
        configDocker();
        configBrowserStack();
    }

    /**
     * Utility to assure no null values in configuration system properties.
     *
     * @param key
     *            key of system property
     * @param defaultValue
     *            default value
     * @return the final value.
     */
    private String getProperty(final String key,
                               final String defaultValue) {
        String value = System.getProperty(key);
        if (StringUtils.isEmpty(value)) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * Do http configuration.
     */
    public void configHttp() {
        RobotestHttpConnConfig httpConfig = new RobotestHttpConnConfig();
        basicCfg.setHttp(httpConfig);

        System.setProperty(RobotestConfigKeys.ROBOTEST_GENERAL_TIMEOUT_MILLIS,
                           getProperty(RobotestConfigKeys.ROBOTEST_GENERAL_TIMEOUT_MILLIS,
                                       "10000"));
        httpConfig.setGeneralTimeout(System.getProperty(RobotestConfigKeys.ROBOTEST_GENERAL_TIMEOUT_MILLIS));
        RobotestConfiguration.LOG.info("ROBOTEST_GENERAL_TIMEOUT: {}",
                                       httpConfig.getGeneralTimeout());

        httpConfig.setProxyHost(System.getProperty(RobotestConfigKeys.ROBOTEST_BROWSER_PROXY_HOST,
                                                   ""));
        RobotestConfiguration.LOG.info("ROBOTEST_BROWSER_PROXY_HOST: {}",
                                       httpConfig.getProxyHost());

        httpConfig.setProxyPort(System.getProperty(RobotestConfigKeys.ROBOTEST_BROWSER_PROXY_PORT,
                                                   ""));
        RobotestConfiguration.LOG.info("ROBOTEST_BROWSER_PROXY_PORT: {}",
                                       httpConfig.getProxyPort());

        httpConfig.setNoproxyfor(System.getProperty(RobotestConfigKeys.ROBOTEST_BROWSER_NOPROXYFOR,
                                                    ""));
        RobotestConfiguration.LOG.info("ROBOTEST_BROWSER_NOPROXYFOR: {}",
                                       httpConfig.getNoproxyfor());

        httpConfig.setProxyUser(System.getProperty(RobotestConfigKeys.ROBOTEST_BROWSER_PROXY_AUTH_USER,
                                                   ""));
        RobotestConfiguration.LOG.info("ROBOTEST_BROWSER_PROXY_AUTH_USER: {}",
                                       httpConfig.getProxyUser());

        httpConfig.setProxySecret(System.getProperty(RobotestConfigKeys.ROBOTEST_BROWSER_PROXY_AUTH_SECRET,
                                                     ""));
        RobotestConfiguration.LOG.info("ROBOTEST_BROWSER_PROXY_AUTH_SECRET: {}",
                                       httpConfig.getProxySecret());
    }

    /**
     * Do browser configuration.
     */
    public void configBrowser() {
        RobotestBrowserConfig browserConfig = new RobotestBrowserConfig();
        basicCfg.setBrowser(browserConfig);

        System.setProperty(RobotestConfigKeys.ROBOTEST_BROWSER_CONSOLE_LOG_LEVEL,
                           getProperty(RobotestConfigKeys.ROBOTEST_BROWSER_CONSOLE_LOG_LEVEL,
                                       "WARNING"));
        browserConfig.setConsoleLogLevel(System.getProperty(RobotestConfigKeys.ROBOTEST_BROWSER_CONSOLE_LOG_LEVEL));
        RobotestConfiguration.LOG.info("ROBOTEST_BROWSER_CONSOLE_LOG_LEVEL: {}",
                                       browserConfig.getConsoleLogLevel());

        System.setProperty(RobotestConfigKeys.ROBOTEST_BROWSER,
                           getProperty(RobotestConfigKeys.ROBOTEST_BROWSER,
                                       "CHROME"));
        browserConfig.setBrowserName(System.getProperty(RobotestConfigKeys.ROBOTEST_BROWSER));
        RobotestConfiguration.LOG.info("ROBOTEST_BROWSER: {}",
                                       basicCfg.getBrowser()
                                               .getBrowserName());

        System.setProperty(RobotestConfigKeys.ROBOTEST_BROWSER_HEADLESS,
                           getProperty(RobotestConfigKeys.ROBOTEST_BROWSER_HEADLESS,
                                       "false"));
        browserConfig.setHeadLess(System.getProperty(RobotestConfigKeys.ROBOTEST_BROWSER_HEADLESS));
        RobotestConfiguration.LOG.info("ROBOTEST_BROWSER_HEADLESS: {}",
                                       basicCfg.getBrowser()
                                               .getHeadLess());

        System.setProperty(RobotestConfigKeys.ROBOTEST_BROWSER_WIDTH,
                           getProperty(RobotestConfigKeys.ROBOTEST_BROWSER_WIDTH,
                                       "1024"));
        browserConfig.setWindowWidth(System.getProperty(RobotestConfigKeys.ROBOTEST_BROWSER_WIDTH));
        RobotestConfiguration.LOG.info("ROBOTEST_BROWSER_WIDTH: {}",
                                       basicCfg.getBrowser()
                                               .getWindowWidth());

        System.setProperty(RobotestConfigKeys.ROBOTEST_BROWSER_HEIGHT,
                           getProperty(RobotestConfigKeys.ROBOTEST_BROWSER_HEIGHT,
                                       "768"));
        browserConfig.setWindowHeight(System.getProperty(RobotestConfigKeys.ROBOTEST_BROWSER_HEIGHT));
        RobotestConfiguration.LOG.info("ROBOTEST_BROWSER_HEIGHT: {}",
                                       basicCfg.getBrowser()
                                               .getWindowHeight());

        System.setProperty(RobotestConfigKeys.ROBOTEST_BROWSER_MAXIMIZED,
                           getProperty(RobotestConfigKeys.ROBOTEST_BROWSER_MAXIMIZED,
                                       "false"));
        browserConfig.setMaximized(System.getProperty(RobotestConfigKeys.ROBOTEST_BROWSER_MAXIMIZED));
        RobotestConfiguration.LOG.info("ROBOTEST_BROWSER_MAXIMIZED: {}",
                                       basicCfg.getBrowser()
                                               .getMaximized());

    }

    /**
     * Do Docker load config.
     */
    public void configDocker() {
        if (StringUtils.isNotEmpty(System.getProperty(RobotestConfigKeys.ROBOTEST_DOCKER_CONN))) {
            DockerConfig dockerConfig = new DockerConfig();

            dockerConfig.setHost(System.getProperty(RobotestConfigKeys.ROBOTEST_DOCKER_CONN));
            RobotestConfiguration.LOG.info("ROBOTEST_DOCKER_HOST: {}",
                                           dockerConfig.getHost());

            if ("true".equalsIgnoreCase(System.getProperty(RobotestConfigKeys.ROBOTEST_DOCKER_TLS))) {
                System.setProperty(RobotestConfigKeys.ROBOTEST_DOCKER_CERTS,
                                   getProperty(RobotestConfigKeys.ROBOTEST_DOCKER_CERTS,
                                               System.getProperty("user.home") + "/.docker/machine/certs"));
                dockerConfig.setCertsPath(System.getProperty(RobotestConfigKeys.ROBOTEST_DOCKER_CERTS));
                RobotestConfiguration.LOG.info("ROBOTEST_DOCKER_CERTS: {}",
                                               dockerConfig.getCertsPath());
            }

            dockerConfig.setHub(System.getProperty(RobotestConfigKeys.ROBOTEST_DOCKER_PUBLIC_HOST));
            RobotestConfiguration.LOG.info("ROBOTEST_DOCKER_PUBLIC_HOST: {}",
                                           dockerConfig.getHub());

            dockerConfig.setExposeDebugPort(System.getProperty(RobotestConfigKeys.ROBOTEST_DOCKER_DEBUG));
            RobotestConfiguration.LOG.info("ROBOTEST_DOCKER_DEBUG: {}",
                                           dockerConfig.getExposeDebugPort());

            dockerConfig.setNetworkMode(System.getProperty(RobotestConfigKeys.ROBOTEST_DOCKER_NETWORK));
            RobotestConfiguration.LOG.info("ROBOTEST_DOCKER_NETWORK: {}",
                                           dockerConfig.getNetworkMode());

            dockerConfig.setInspectRealPort(System.getProperty(RobotestConfigKeys.ROBOTEST_DOCKER_INSPECTREALPORT,
                                                               "false"));
            RobotestConfiguration.LOG.info("ROBOTEST_DOCKER_INSPECTREALPORT: {}",
                                           dockerConfig.getInspectRealPort());

            dockerConfig.setContainerName(System.getProperty(RobotestConfigKeys.ROBOTEST_DOCKER_CONTAINER_EXEC_TAG));
            RobotestConfiguration.LOG.info("ROBOTEST_DOCKER_CONTAINER_EXEC_TAG: {}",
                                           dockerConfig.getContainerName());

            Map<String, String> labels = new HashMap<>();
            String labelsText = System.getProperty(RobotestConfigKeys.ROBOTEST_DOCKER_LABELS);
            if (StringUtils.isNotEmpty(labelsText)) {
                String[] label = null;
                String key = null;
                String value = null;
                for (String labelText : labelsText.trim()
                                                  .split(",")) {
                    label = labelText.trim()
                                     .split(":");
                    key = label[0];
                    if (label.length == 1) {
                        value = "";
                    } else {
                        value = label[1];
                    }
                    labels.put(key,
                               value);
                }
            }
            dockerConfig.setLabels(labels);
            RobotestConfiguration.LOG.info("ROBOTEST_DOCKER_LABELS: {}",
                                           labelsText);

            dockerConfig.setDockerRegistryUrl(System.getProperty(RobotestConfigKeys.ROBOTEST_DOCKER_REGISTRY_URL));
            RobotestConfiguration.LOG.info("ROBOTEST_DOCKER_REGISTRY: {}",
                                           dockerConfig.getDockerRegistryUrl());

            dockerConfig.setDockerRegistryUser(System.getProperty(RobotestConfigKeys.ROBOTEST_DOCKER_REGISTRY_USER));
            RobotestConfiguration.LOG.info("ROBOTEST_DOCKER_REGISTRY: {}",
                                           dockerConfig.getDockerRegistryUser());

            dockerConfig.setDockerRegistrySecret(System.getProperty(RobotestConfigKeys.ROBOTEST_DOCKER_REGISTRY_SECRET));
            RobotestConfiguration.LOG.info("ROBOTEST_DOCKER_REGISTRY_SECRET *****");

            dockerConfig.setDockerRegistryEmail(System.getProperty(RobotestConfigKeys.ROBOTEST_DOCKER_REGISTRY_EMAIL));
            RobotestConfiguration.LOG.info("ROBOTEST_DOCKER_REGISTRY_EMAIL: {}",
                                           dockerConfig.getDockerRegistryEmail());

            System.setProperty(RobotestConfigKeys.ROBOTEST_CHROME_DOCKER_IMG_TAG,
                               getProperty(RobotestConfigKeys.ROBOTEST_CHROME_DOCKER_IMG_TAG,
                                           "selenium/standalone-chrome:3.12.0-boron"));
            dockerConfig.setChromeImageTag(System.getProperty(RobotestConfigKeys.ROBOTEST_CHROME_DOCKER_IMG_TAG));
            RobotestConfiguration.LOG.info("ROBOTEST_CHROME_DOCKER_IMG_TAG: {}",
                                           dockerConfig.getChromeImageTag());

            System.setProperty(RobotestConfigKeys.ROBOTEST_CHROME_DEBUG_DOCKER_IMG_TAG,
                               getProperty(RobotestConfigKeys.ROBOTEST_CHROME_DEBUG_DOCKER_IMG_TAG,
                                           "selenium/standalone-chrome-debug:3.12.0-boron"));
            dockerConfig.setChromeDebugImageTag(System.getProperty(RobotestConfigKeys.ROBOTEST_CHROME_DEBUG_DOCKER_IMG_TAG));
            RobotestConfiguration.LOG.info("ROBOTEST_CHROME_DEBUG_DOCKER_IMG_TAG: {}",
                                           dockerConfig.getChromeDebugImageTag());

            System.setProperty(RobotestConfigKeys.ROBOTEST_FIREFOX_DOCKER_IMG_TAG,
                               getProperty(RobotestConfigKeys.ROBOTEST_FIREFOX_DOCKER_IMG_TAG,
                                           "selenium/standalone-firefox:3.12.0-boron"));
            dockerConfig.setFirefoxImageTag(System.getProperty(RobotestConfigKeys.ROBOTEST_FIREFOX_DOCKER_IMG_TAG));
            RobotestConfiguration.LOG.info("ROBOTEST_FIREFOX_DOCKER_IMG_TAG: {}",
                                           dockerConfig.getFirefoxImageTag());

            System.setProperty(RobotestConfigKeys.ROBOTEST_FIREFOX_DEBUG_DOCKER_IMG_TAG,
                               getProperty(RobotestConfigKeys.ROBOTEST_FIREFOX_DEBUG_DOCKER_IMG_TAG,
                                           "selenium/standalone-firefox-debug:3.12.0-boron"));
            dockerConfig.setFirefoxDebugImageTag(System.getProperty(RobotestConfigKeys.ROBOTEST_FIREFOX_DEBUG_DOCKER_IMG_TAG));
            RobotestConfiguration.LOG.info("ROBOTEST_FIREFOX_DEBUG_DOCKER_IMG_TAG: {}",
                                           dockerConfig.getFirefoxDebugImageTag());

            basicCfg.setDocker(dockerConfig);
        }
    }

    /**
     * Do browser stack config loading.
     */
    public void configBrowserStack() {
        if (StringUtils.isNotEmpty(System.getProperty(RobotestConfigKeys.ROBOTEST_BROWSERSTACK_DEVICE))) {
            BrowserStackConfig bsCfg = new BrowserStackConfig();
            bsCfg.setDevice(System.getProperty(RobotestConfigKeys.ROBOTEST_BROWSERSTACK_DEVICE));
            RobotestConfiguration.LOG.info("ROBOTEST_BROWSERSTACK_DEVICE: {}",
                                           bsCfg.getDevice());
            bsCfg.setPlatform(System.getProperty(RobotestConfigKeys.ROBOTEST_BROWSERSTACK_PLATFORM));
            RobotestConfiguration.LOG.info("ROBOTEST_BROWSERSTACK_PLATFORM: {}",
                                           bsCfg.getPlatform());
            bsCfg.setLogin(System.getProperty(RobotestConfigKeys.ROBOTEST_BROWSERSTACK_USER_NAME));
            RobotestConfiguration.LOG.info("ROBOTEST_BROWSERSTACK_USER_NAME: {}",
                                           bsCfg.getLogin());
            bsCfg.setAccessKey(System.getProperty(RobotestConfigKeys.ROBOTEST_BROWSERSTACK_ACCESS_KEY));
            RobotestConfiguration.LOG.info("ROBOTEST_BROWSERSTACK_ACCESS_KEY *****");
            basicCfg.setBrowserStack(bsCfg);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.castinfo.devops.robotest.config.IRobotestConfiguration#loadAnnotationScopeConfig(com.castinfo.devops.robotest
     * .annot.RobotestConfig[], java.lang.annotation.Annotation)
     */
    @Override
    @SuppressWarnings("unchecked")
    public void loadAnnotationScopeConfig(final RobotestConfig[] cfgAnnot,
                                          final Annotation scope) throws RobotestException {
        if (null != cfgAnnot) {
            for (RobotestConfig cfg : cfgAnnot) {
                @SuppressWarnings("rawtypes")
                ConfigurationEntry configEntry = new ConfigurationEntry();
                if (cfg.resource()
                       .startsWith("system://")) {
                    String[] envKeys = cfg.resource()
                                          .substring("system://".length())
                                          .split(",");
                    Properties envValues = new Properties();
                    for (String key : envKeys) {
                        envValues.setProperty(key,
                                              System.getProperty(key));
                    }
                    configEntry.setValue(envValues);
                } else {
                    String resourceUri = expandUri(cfg.resource());
                    try (InputStream resource = loadResourceStream(resourceUri)) {
                        configEntry.setValue(this.mapObjectResources(resource,
                                                                     cfg.type()));
                    } catch (IOException | NullPointerException e) {
                        throw new RobotestException("ERROR READING USER CONFIG: " + resourceUri,
                                                    e);
                    }
                }
                testConfigEntries.computeIfAbsent(scope,
                                                  k -> new HashMap<>())
                                 .put(cfg.key(),
                                      configEntry);
            }
        }
    }

    /**
     * Makes the configuration loading and bean mapping (Properties, Jaxb, Jackson).
     *
     * @param resource
     *            iostream
     * @param clazzType
     *            Binding type.
     * @throws RobotestException
     *             Marshal exceptions.
     */
    @SuppressWarnings("unchecked")
    private <T> T mapObjectResources(final InputStream resource,
                                     final Class<T> clazzType) throws RobotestException {
        Object mapped = null;
        if (clazzType.equals(Properties.class)) {
            Properties props = new Properties();
            try {
                props.load(resource);
                mapped = props;
            } catch (IOException e) {
                throw new RobotestException("CONFIG RESOURCE PROPERTIES READ ERROR",
                                            e);
            }
        } else if (isJackson(clazzType)) {
            try {
                mapped = new ObjectMapper().readValue(resource,
                                                      clazzType);
            } catch (IOException e) {
                throw new RobotestException("CONFIG RESOURCE JSON READ ERROR",
                                            e);
            }
        } else if (isJaxb(clazzType)) {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(clazzType);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                mapped = unmarshaller.unmarshal(resource);
            } catch (JAXBException e) {
                throw new RobotestException("CONFIG RESOURCE JAXB READ ERROR",
                                            e);
            }
        } else {
            throw new RobotestException("CONFIG RESOURCE TYPE NOT SUPPORTED: " + clazzType.getName());
        }
        return (T) mapped;
    }

    /**
     * Load the input stream of resource.
     *
     * @param resourceUri
     *            puede ser classpath, file o http/s
     * @return el input stream del resourceUri
     * @throws RobotestException
     *             errores en la carga del iostream
     */
    private InputStream loadResourceStream(final String resourceUri) throws RobotestException {
        InputStream resource = null;
        if (resourceUri.startsWith("classpath://")) {
            resource = this.getClass()
                           .getClassLoader()
                           .getResourceAsStream(resourceUri.replaceFirst("classpath://",
                                                                         ""));
        } else if (resourceUri.startsWith("file://")) {
            try {
                resource = new FileInputStream(resourceUri);
            } catch (FileNotFoundException e) {
                throw new RobotestException("CONFIG RESOURCE FILE NOT EXIST",
                                            e);
            }
        } else if (resourceUri.startsWith("http://") || resourceUri.startsWith("https://")) {
            resource = new ByteArrayInputStream(when().get(resourceUri)
                                                      .getBody()
                                                      .asByteArray());
        } else {
            throw new RobotestException("CONFIG RESOURCE NOT SUPORTED");
        }
        return resource;
    }

    /**
     * Provides system properties wildcard substitiution.
     *
     * @param cfg
     *            cfg annotation
     * @return Expanded URI.
     */
    private String expandUri(final String resource) {
        Map<String, String> systemMap = new HashMap<>();
        for (String key : System.getProperties()
                                .stringPropertyNames()) {
            systemMap.put(key,
                          System.getProperties()
                                .getProperty(key));
        }
        StrSubstitutor sub = new StrSubstitutor(systemMap);
        return sub.replace(resource);
    }

    /**
     * Identifies Jackson mapping classes.
     *
     * @param type
     *            object
     * @return true if jackson
     */
    private static boolean isJackson(final Class<?> type) {
        boolean resultado = false;
        for (Annotation annot : type.getAnnotations()) {
            if (annot.annotationType()
                     .getName()
                     .contains("com.fasterxml.jackson.annotation")) {
                resultado = true;
            }
        }
        return resultado;
    }

    /**
     * Identifies Jaxb mapping classes.
     *
     * @param type
     *            object
     * @return true if jaxb
     */
    private static boolean isJaxb(final Class<?> type) {
        boolean resultado = false;
        for (Annotation annot : type.getAnnotations()) {
            if (annot.annotationType()
                     .getName()
                     .contains("javax.xml.bind.annotation")) {
                resultado = true;
            }
        }
        if (!resultado && type.getSuperclass()
                              .equals(JAXBElement.class)) {
            resultado = true;
        }
        return resultado;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.castinfo.devops.robotest.config.IRobotestConfiguration#getConfigBasic()
     */
    @Override
    public RobotestBasicConfig getConfigBasic() {
        return basicCfg;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.castinfo.devops.robotest.config.IRobotestConfiguration#getAnnotationScopeCfg(java.lang.annotation.Annotation,
     * java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getAnnotationScopeCfg(final Annotation scope,
                                       final String key) {
        T resultado = null;
        Map<String, ConfigurationEntry<?>> configEntries = testConfigEntries.get(scope);
        if (null != configEntries) {
            ConfigurationEntry<T> config = (ConfigurationEntry<T>) configEntries.get(key);
            if (null != config) {
                resultado = config.getValue();
            }
        }
        return resultado;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.castinfo.devops.robotest.config.IRobotestConfiguration#toReportConfigEntries(java.lang.annotation.Annotation)
     */
    @Override
    public List<ConfigEntry> toReportConfigEntries(final Annotation scope) {
        List<ConfigEntry> entries = new ArrayList<>();
        if (testConfigEntries.containsKey(scope)) {
            for (String key : testConfigEntries.get(scope)
                                               .keySet()) {
                entries.add(new ConfigEntry(key,
                                            testConfigEntries.get(scope)
                                                             .get(key)
                                                             .getValue()));
            }
        }
        return entries;
    }

}
