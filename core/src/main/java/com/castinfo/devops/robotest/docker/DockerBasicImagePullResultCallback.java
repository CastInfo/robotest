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

import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.core.command.PullImageResultCallback;

/**
 * Utility to log Docker basic image pulling logs to CONSOLE.
 *
 */
public class DockerBasicImagePullResultCallback extends PullImageResultCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerBasicImagePullResultCallback.class);

    @Override
    public void onNext(final PullResponseItem item) {
        DockerBasicImagePullResultCallback.LOGGER.info("{}",
                                                       item);
        super.onNext(item);
    }
}
