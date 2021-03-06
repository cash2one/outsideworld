//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-661 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.12 at 10:00:00 下午 CST 
//


package com.processor.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProcessorsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProcessorsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="processor" type="{http://www.example.org/demo/}ProcessorType" maxOccurs="unbounded"/>
 *         &lt;element name="processorFactory" type="{http://www.example.org/demo/}ProcessorFactoryType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProcessorsType", propOrder = {
    "processor",
    "processorFactory"
})
public class ProcessorsType {

    @XmlElement(required = true)
    protected List<ProcessorType> processor;
    protected List<ProcessorFactoryType> processorFactory;

    /**
     * Gets the value of the processor property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the processor property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProcessor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProcessorType }
     * 
     * 
     */
    public List<ProcessorType> getProcessor() {
        if (processor == null) {
            processor = new ArrayList<ProcessorType>();
        }
        return this.processor;
    }

    /**
     * Gets the value of the processorFactory property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the processorFactory property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProcessorFactory().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProcessorFactoryType }
     * 
     * 
     */
    public List<ProcessorFactoryType> getProcessorFactory() {
        if (processorFactory == null) {
            processorFactory = new ArrayList<ProcessorFactoryType>();
        }
        return this.processorFactory;
    }

}
