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
import java.util.Properties;

import com.castinfo.devops.robotest.RobotestException;
import com.castinfo.devops.robotest.SuiteContext;
import com.castinfo.devops.robotest.TestContext;

/**
 * Access utility methods to retrieve context config under execution.
 */
public abstract class ConfigurationAccess extends TestContext {

    /**
     * Get basic ROBOTEST execution configuration.
     *
     * @return BasicConfig Object.
     * @throws RobotestException
     *             If suite initialization problems happens.
     */
    public RobotestBasicConfig getBasicCfg() throws RobotestException {
        return this.getBasicCfg(getSuiteContext());
    }

    /**
     * Get basic Robotest execution configuration.
     *
     * @param suiteContext
     *            SuiteContext necessary to retrive config.
     * @return BasicConfig Object.
     */
    public RobotestBasicConfig getBasicCfg(final SuiteContext suiteContext) {
        return suiteContext.getConfig()
                           .getConfigBasic();
    }

    /**
     * Gets config test object identified by key, and cast type.
     *
     * @param suiteContext
     *            SuiteContext necessary to retrive config.
     * @param scope
     *            The suite, case or step annotation scope of config to search.
     * @param key
     *            key cfg
     * @param <T>
     *            generic type to be cast
     * @return T cfg casted object.
     */
    public <T> T getTestCfgInScope(final SuiteContext suiteContext,
                                   final Annotation scope,
                                   final String key) {
        return suiteContext.getConfig()
                           .getAnnotationScopeCfg(scope,
                                                  key);
    }

    /**
     * Gets config test object identified by key, and cast type, recursively, first in step scope, second in case scope,
     * and last in suite.
     *
     * @param configKey
     *            key cfg
     * @param <T>
     *            generic type to be cast
     * @return T cfg object.
     * @throws RobotestException
     *             If suite initialization problems happens.
     */
    public abstract <T> T getTestCfg(String configKey) throws RobotestException;

    /**
     * Gets one user configuration String value in annotation scope context config Properties.
     *
     * @param suiteContext
     *            SuiteContext necessary to retrive config.
     * @param scope
     *            The suite, case, step annotation scope to search config.
     * @param configKey
     *            Config Key
     * @param keyProperty
     *            Object config Key.
     * @return The value of properties config key.
     */
    public String getTestPropertyCfgInScope(final SuiteContext suiteContext,
                                            final Annotation scope,
                                            final String configKey,
                                            final String keyProperty) {
        String resultado = null;
        Properties scopedConfig = this.getTestCfgInScope(suiteContext,
                                                         scope,
                                                         configKey);
        if (null != scopedConfig) {
            resultado = scopedConfig.getProperty(keyProperty);
        }
        return resultado;
    }

    /**
     * GGets one user configuration String value in annotation scopes contexts config Properties, recursively, first in
     * step scope, second in case scope, and last in suite scope.
     *
     * @param configKey
     *            Config Key
     * @param keyProperty
     *            Object config Key.
     * @return The value of properties config key.
     * @throws RobotestException
     *             If suite initialization problems happens.
     */
    public String getTestPropertyCfg(final String configKey,
                                     final String keyProperty) throws RobotestException {
        String result = null;
        Properties scopedConfig = this.getTestCfg(configKey);
        if (null != scopedConfig) {
            result = scopedConfig.getProperty(keyProperty);
        }
        return result;
    }

    /**
     * Returns config step object.
     *
     * @param configKey
     *            Object config Key.
     * @param <T>
     *            generic type to be cast
     * @return The object config.
     * @throws RobotestException
     *             If suite initialization problems happens
     */
    public <T> T getStepTestCfg(final String configKey) throws RobotestException {
        return this.getTestCfgInScope(getSuiteContext(),
                                      getStepAnnot(),
                                      configKey);
    }

    /**
     * Gets one user configuration String value in step scope context config Properties.
     *
     * @param configKey
     *            Config Key
     * @param keyProperty
     *            Object config Key.
     * @return The value of properties config key.
     * @throws RobotestException
     *             If suite initialization problems happens.
     */
    public String getStepTestPropertyCfg(final String configKey,
                                         final String keyProperty) throws RobotestException {
        return getTestPropertyCfgInScope(getSuiteContext(),
                                         getStepAnnot(),
                                         configKey,
                                         keyProperty);
    }

    /**
     * Returns config step object.
     *
     * @param configKey
     *            Object config Key.
     * @param <T>
     *            generic type to be cast
     * @return The object config.
     * @throws RobotestException
     *             If suite initialization problems happens
     */
    public <T> T getCaseTestCfg(final String configKey) throws RobotestException {
        return this.getTestCfgInScope(getSuiteContext(),
                                      getCaseAnnot(),
                                      configKey);
    }

    /**
     * Gets one user configuration String value in suite scope context config Properties.
     *
     * @param configKey
     *            Config Key
     * @param keyProperty
     *            Object config Key.
     * @return The value of properties config key.
     * @throws RobotestException
     *             If suite initialization problems happens.
     */
    public String getCaseTestPropertyCfg(final String configKey,
                                         final String keyProperty) throws RobotestException {
        return getTestPropertyCfgInScope(getSuiteContext(),
                                         getCaseAnnot(),
                                         configKey,
                                         keyProperty);
    }

    /**
     * Returns config step object.
     *
     * @param configKey
     *            Object config Key.
     * @param <T>
     *            generic type to be cast
     * @return The object config.
     * @throws RobotestException
     *             If suite initialization problems happens
     */
    public <T> T getSuiteTestCfg(final String configKey) throws RobotestException {
        return this.getTestCfgInScope(getSuiteContext(),
                                      getSuiteAnnot(),
                                      configKey);
    }

    /**
     * Gets one user configuration String value in suite scope context config Properties.
     *
     * @param configKey
     *            Config Key
     * @param keyProperty
     *            Object config Key.
     * @return The value of properties config key.
     * @throws RobotestException
     *             If suite initialization problems happens.
     */
    public String getSuiteTestPropertyCfg(final String configKey,
                                          final String keyProperty) throws RobotestException {
        return getTestPropertyCfgInScope(getSuiteContext(),
                                         getSuiteAnnot(),
                                         configKey,
                                         keyProperty);
    }

}
