package com.DynamicJavaClassesAtRuntime.DynamicJavaClasses.service;

import org.codehaus.janino.SimpleCompiler;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

@Service
public class SetS4hanaData {

  public String insertS4hanaData(SimpleCompiler compiler, Class<?> formClass) throws Exception{
    String formClassStr = "FORM_CLASS";
    Object formInstance = getInstance(formClass);

    Result result = getResult(compiler);

//
//          Method[] methods = result.deliveryHdrNodeClass().getMethods();
////      System.out.println(Arrays.stream(methods).toList() + "methods");
//
//      for (Method method : methods){
//        System.out.println("Method name  :" + " " + method + method.getName());
////        System.out.println("Method return type  :" + " " + method.getReturnType() + method.getParameterCount());
//      }





    // Set DeliveryHdrNode instance on Form using reflection
    Method setDeliveryHdrNodeMethod = formClass.getMethod("setDeliveryHdrNode", result.deliveryHdrNodeClass());
    setDeliveryHdrNodeMethod.invoke(formInstance, result.deliveryHdrNodeInstance());

    // Creating instance of HandlingUnit and DeliveryItem
    Object handlingUnitInstance = createInstance(compiler, "HandlingUnit");
    Object deliveryItemInstance = createInstance(compiler, "DeliveryItem");

    Method setHandlingUnitMethod = result.deliveryHdrNodeClass().getMethod("setHandlingUnit", handlingUnitInstance.getClass());
    setHandlingUnitMethod.invoke(result.deliveryHdrNodeInstance(), handlingUnitInstance);

    Method setDeliveryItemMethod = result.deliveryHdrNodeClass().getMethod("setDeliveryItem", deliveryItemInstance.getClass());
    setDeliveryItemMethod.invoke(result.deliveryHdrNodeInstance(), deliveryItemInstance);

    // Set other properties on DeliveryHdrNode
    Method setDeliveryheadergrossweightMethod = result.deliveryHdrNodeClass().getMethod("setDeliveryheadergrossweight", float.class);
    float grossWeight = 11.09F;
    setDeliveryheadergrossweightMethod.invoke(result.deliveryHdrNodeInstance(), grossWeight);

    Method setDeliveryheadergrossweightunitMethod = result.deliveryHdrNodeClass().getMethod("setDeliveryheadergrossweightunit", String.class);
    setDeliveryheadergrossweightunitMethod.invoke(result.deliveryHdrNodeInstance(), "kg");

    Method setDeliveryheadergrossvolumeMethod = result.deliveryHdrNodeClass().getMethod("setDeliveryheadergrossvolume", float.class);
    float grossVolume = 15.17F;
    setDeliveryheadergrossvolumeMethod.invoke(result.deliveryHdrNodeInstance(), grossVolume);

    return convertIntoXML(formInstance, formClass);

  }

  private static Result getResult(SimpleCompiler compiler) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    Class<?> deliveryHdrNodeClass = getCustomClass(compiler);
    // Creating instance of DeliveryHdrNode
    Object deliveryHdrNodeInstance = deliveryHdrNodeClass.getDeclaredConstructor().newInstance();
    Result result = new Result(deliveryHdrNodeClass, deliveryHdrNodeInstance);
    return result;
  }

  private record Result(Class<?> deliveryHdrNodeClass, Object deliveryHdrNodeInstance) {
  }

  private static Class<?> getCustomClass(SimpleCompiler compiler) throws ClassNotFoundException {
    // Load the deliveryHrdNodeClass
    Class<?> deliveryHdrNodeClass = compiler.getClassLoader().loadClass("com.DynamicJavaClassesAtRuntime.DynamicJavaClasses.generated.Form$DeliveryHdrNode");
    return deliveryHdrNodeClass;
  }

  private static Object getInstance(Class<?> formClass) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    Object myInstance;
//    if (formClassStr.equals("FORM_CLASS")) {
      // Creating instance of Form
      myInstance = formClass.getDeclaredConstructor().newInstance();

      return myInstance;
  }

  public Object createInstance(SimpleCompiler compiler, String className) throws Exception{
    // Load the class
    Class<?> loadedClass = compiler.getClassLoader().loadClass("com.DynamicJavaClassesAtRuntime.DynamicJavaClasses.generated.Form$DeliveryHdrNode$" + className);
    // Creating instance of loadedClass

    Method[] methods = loadedClass.getMethods();
//      System.out.println(Arrays.stream(methods).toList() + "methods");

    for (Method method : methods){
      System.out.println("Method name  :" + " " + method + method.getName());
//        System.out.println("Method return type  :" + " " + method.getReturnType() + method.getParameterCount());
    }

    return loadedClass.getDeclaredConstructor().newInstance();
  }

  public String convertIntoXML(Object formInstance, Class<?> formClass) throws Exception{
    StringWriter writer = new StringWriter();

    JAXBContext context = JAXBContext.newInstance(formClass);
    Marshaller marshaller = context.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.marshal(formInstance, writer);

    String xml = writer.toString();
    System.out.println(xml);
    return xml;
  }
}
