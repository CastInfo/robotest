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
package com.castinfo.devops.robotest.report;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class to map json report suite list object.
 *
 */
public class SuiteListMapper {

    private List<String> suites = new ArrayList<>();

    /**
     * Getter of SUITES list.
     *
     * @return list of SUITES
     */
    @JsonProperty("suites")
    public List<String> getSuites() {
        return suites;
    }

    /**
     * Setter of suite list.
     *
     * @param suitesList
     *            suite list array
     */
    @JsonProperty("suites")
    public void setSuites(final List<String> suitesList) {
        suites = suitesList;
    }
}
