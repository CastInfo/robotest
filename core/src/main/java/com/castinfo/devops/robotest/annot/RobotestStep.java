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
 * ROBOTEST Annotation. This annotation makes posible to configure Steps.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RobotestStep {

    /**
     * Tag of Step, acomplish RobotestExecutionContext.TAG_PATTERN regexp.
     *
     * @return Tag.
     */
    String tag();

    /**
     * Free description of step.
     *
     * @return Description.
     */
    String description() default "";

    /**
     * If true (default), robotest capture a SCREENSHOT at start step automatically.
     *
     * @return true if capture SCREENSHOT.
     */
    boolean captureScreenShootAtStartStep() default false;

    /**
     * If true (default), robotest capture a SCREENSHOT at end step automatically.
     *
     * @return true if capture SCREENSHOT.
     */
    boolean captureScreenShootAtEndStep() default true;

    /**
     * If true, robotest capture browser CONSOLE logs at end step automatically.
     *
     * @return true if capture logs.
     */
    boolean captureConsoleErrorLogsAtEndStep() default false;

    /**
     * If true, robotest capture current HTML page source in browser at end step automatically.
     *
     * @return true if capture HTML source.
     */
    boolean capturePageSourceAtEndStep() default false;

    /**
     * User Config list elements.
     *
     * @return User configs list.
     */
    RobotestConfig[] configElements() default {};

}
