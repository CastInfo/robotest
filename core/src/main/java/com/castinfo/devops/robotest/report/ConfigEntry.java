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

/**
 * Uniform JSON Config Entry Wrapper. All configs are viewed in a Tree of &gt;Key,Value&lt;Map
 */
public class ConfigEntry {

    private String id;
    private Object value;

    /**
     * Constructor.
     *
     * @param id
     *            key.
     * @param value
     *            value.
     *
     */
    public ConfigEntry(final String id,
                       final Object value) {
        this.id = id;
        this.value = value;
    }

    /**
     * Getter method for id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Setter method for the id.
     *
     * @param id
     *            the id to set
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Getter method for value.
     *
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Setter method for the value.
     *
     * @param value
     *            the value to set
     */
    public void setValue(final Object value) {
        this.value = value;
    }

}
