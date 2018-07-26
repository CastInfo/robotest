package com.castinfo.devops.robotest.testutils;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement
public class Jaxb {

    String value;

    public String getValue() {
        return value;
    }

    @XmlValue
    public void setValue(final String codigo) {
        value = codigo;
    }

}
