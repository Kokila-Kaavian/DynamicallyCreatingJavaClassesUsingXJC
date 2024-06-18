package com.DynamicJavaClassesAtRuntime.DynamicJavaClasses.service;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.springframework.stereotype.Service;

@Service
public class ConvertXML{

     // Generate the XML
  public String convertIntoXML(Object formInstance, Class<?> formClass) throws Exception{
    StringWriter writer = new StringWriter();

    // Using JAXB create the XML
    JAXBContext context = JAXBContext.newInstance(formClass);
    Marshaller marshaller = context.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.marshal(formInstance, writer);

    String xml = writer.toString();
    System.out.println(xml);
    return xml;
  }

}