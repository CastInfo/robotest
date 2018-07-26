//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML
// v2.2.8-b130911.1802
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen.
// Generado el: 2018.01.22 a las 10:22:25 PM CET
//

package com.castinfo.devops.robotest.restassured;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p> Clase Java para JaxbPojoType complex type.
 *
 * <p> El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 *
 * <pre>
 * &lt;complexType name="JaxbPojoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="echo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
@XmlType(name = "JaxbPojoType", propOrder = { "echo" })
public class JaxbPojoType {

    @XmlElement(required = true)
    protected String echo;

    /**
     * Obtiene el valor de la propiedad echo.
     *
     * @return possible object is {@link String }
     *
     */
    public String getEcho() {
        return echo;
    }

    /**
     * Define el valor de la propiedad echo.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setEcho(final String value) {
        echo = value;
    }

}
