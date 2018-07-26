//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML
// v2.2.8-b130911.1802
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen.
// Generado el: 2018.01.22 a las 10:22:25 PM CET
//

package com.castinfo.devops.robotest.restassured;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java element interface generated in the
 * com.castinfo.devops.robotest.rest package. <p> An ObjectFactory allows you to programmatically construct new
 * instances of the Java representation for XML content. The Java representation of XML content can consist of schema
 * derived interfaces and classes representing the binding of schema type definitions, element declarations and model
 * groups. Factory methods for each of these are provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Response_QNAME = new QName("",
                                                           "response");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package:
     * com.castinfo.devops.robotest.rest
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JaxbPojoType }
     *
     */
    public JaxbPojoType createJaxbPojoType() {
        return new JaxbPojoType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link JaxbPojoType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "response")
    public JAXBElement<JaxbPojoType> createResponse(final JaxbPojoType value) {
        return new JAXBElement<>(_Response_QNAME,
                                 JaxbPojoType.class,
                                 null,
                                 value);
    }

}
