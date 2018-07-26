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
package com.castinfo.devops.robotest.annot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ROBOTEST Annotation. This annotation makes posible to configure a free user config resource for Suites, Cases and
 * Steps.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface RobotestConfig {

    /**
     * Key of config.
     *
     * @return Key.
     */
    String key();

    /**
     * Type of config to bind resource. There are 3 type accepted: Properties.class, or POJO class extending Jackson or
     * JAX-B root element.
     *
     * @return Binding resource class.
     */
    @SuppressWarnings("rawtypes")
    Class type();

    /**
     * Resource URI, may be classpath, file or http/s.
     *
     * @return Resource.
     */
    String resource();

}
