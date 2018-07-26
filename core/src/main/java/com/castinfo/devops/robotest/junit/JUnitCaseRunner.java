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
package com.castinfo.devops.robotest.junit;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * JUnit listener implementation. Build JUnitCaseListener to build contexts.
 *
 */
public class JUnitCaseRunner extends BlockJUnit4ClassRunner {

    /**
     * Constructor.
     *
     * @param klass
     *            The real test class fire this listener.
     * @throws InitializationError
     *             errors.
     */
    public JUnitCaseRunner(final Class<?> klass) throws InitializationError {
        super(klass);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.junit.runners.ParentRunner#run(org.junit.runner.notification.RunNotifier)
     */
    @Override
    public void run(final RunNotifier notifier) {
        JUnitCaseListener jl = new JUnitCaseListener();
        jl.setClazz(getTestClass().getJavaClass());
        notifier.addListener(jl);
        notifier.fireTestRunStarted(getDescription());
        super.run(notifier);
    }
}
