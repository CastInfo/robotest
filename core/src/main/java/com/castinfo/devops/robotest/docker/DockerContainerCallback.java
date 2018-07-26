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
package com.castinfo.devops.robotest.docker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.command.LogContainerResultCallback;

/**
 * Utility to log Docker container logs to CONSOLE.
 *
 */
public class DockerContainerCallback extends LogContainerResultCallback {

    private boolean serverSeleniumLoaded = false;
    private String loadedServerText;
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerContainerCallback.class);

    /**
     * Constructor.
     *
     * @param loadedServerText
     *            end passphrase.
     */
    public DockerContainerCallback(final String loadedServerText) {
        this.loadedServerText = loadedServerText;
    }

    /**
     * Getter method for serverSeleniumLoaded.
     *
     * @return the serverSeleniumLoaded
     */
    public boolean isServerSeleniumLoaded() {
        return serverSeleniumLoaded;
    }

    /**
     * Setter method for the serverSeleniumLoaded.
     *
     * @param serverSeleniumLoaded
     *            the serverSeleniumLoaded to set
     */
    public void setServerSeleniumLoaded(final boolean serverSeleniumLoaded) {
        this.serverSeleniumLoaded = serverSeleniumLoaded;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.github.dockerjava.core.command.LogContainerResultCallback#onNext(com.github.dockerjava.api.model.Frame)
     */
    @Override
    public void onNext(final Frame item) {
        DockerContainerCallback.LOGGER.info("DOCKER LOGGER: {}", item);
        if (item.toString().indexOf(loadedServerText) != -1) {
            serverSeleniumLoaded = true;
        }

        super.onNext(item);
    }

}
