//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantaci�n de la referencia de enlace (JAXB) XML v2.2.7 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perder�n si se vuelve a compilar el esquema de origen. 
// Generado el: 2014.06.18 a las 09:48:32 AM CEST 
//


package org.pcdm.util.parser.model.wsdl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Clase Java para tBindingOperation complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="tBindingOperation">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.xmlsoap.org/wsdl/}tExtensibleDocumented">
 *       &lt;sequence>
 *         &lt;element name="input" type="{http://schemas.xmlsoap.org/wsdl/}tBindingOperationMessage" minOccurs="0"/>
 *         &lt;element name="output" type="{http://schemas.xmlsoap.org/wsdl/}tBindingOperationMessage" minOccurs="0"/>
 *         &lt;element name="fault" type="{http://schemas.xmlsoap.org/wsdl/}tBindingOperationFault" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tBindingOperation", propOrder = {
    "input",
    "output",
    "fault"
})
public class TBindingOperation
    extends TExtensibleDocumented
{

    protected TBindingOperationMessage input;
    protected TBindingOperationMessage output;
    protected List<TBindingOperationFault> fault;
    @XmlAttribute(name = "name", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String name;

    /**
     * Obtiene el valor de la propiedad input.
     * 
     * @return
     *     possible object is
     *     {@link TBindingOperationMessage }
     *     
     */
    public TBindingOperationMessage getInput() {
        return input;
    }

    /**
     * Define el valor de la propiedad input.
     * 
     * @param value
     *     allowed object is
     *     {@link TBindingOperationMessage }
     *     
     */
    public void setInput(TBindingOperationMessage value) {
        this.input = value;
    }

    /**
     * Obtiene el valor de la propiedad output.
     * 
     * @return
     *     possible object is
     *     {@link TBindingOperationMessage }
     *     
     */
    public TBindingOperationMessage getOutput() {
        return output;
    }

    /**
     * Define el valor de la propiedad output.
     * 
     * @param value
     *     allowed object is
     *     {@link TBindingOperationMessage }
     *     
     */
    public void setOutput(TBindingOperationMessage value) {
        this.output = value;
    }

    /**
     * Gets the value of the fault property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fault property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFault().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TBindingOperationFault }
     * 
     * 
     */
    public List<TBindingOperationFault> getFault() {
        if (fault == null) {
            fault = new ArrayList<TBindingOperationFault>();
        }
        return this.fault;
    }

    /**
     * Obtiene el valor de la propiedad name.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Define el valor de la propiedad name.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
