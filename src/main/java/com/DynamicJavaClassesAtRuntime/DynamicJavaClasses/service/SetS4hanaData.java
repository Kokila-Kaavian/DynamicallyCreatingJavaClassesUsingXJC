package com.DynamicJavaClassesAtRuntime.DynamicJavaClasses.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.codehaus.janino.SimpleCompiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SetS4hanaData {

  @Autowired
  ConvertXML convertXML;

  public String insertS4hanaData(SimpleCompiler compiler, Class<?> formClass) throws Exception{

    // Creating the instance for form class
    Object formInstance = getInstance(formClass);

    Result result = getResult(compiler);

         Method[] methods = result.deliveryHdrNodeClass().getMethods();

     for (Method method : methods){
       System.out.println("Method name  :" + " " + method.getName());
       System.out.println("Method return type  :" + " " + method.getReturnType());
     }


    // Set DeliveryHdrNode instance on Form using reflection
    Method setDeliveryHdrNodeMethod = formClass.getMethod("setDeliveryHdrNode", result.deliveryHdrNodeClass());
    setDeliveryHdrNodeMethod.invoke(formInstance, result.deliveryHdrNodeInstance());

    // Creating instance of HandlingUnit and DeliveryItem
    Object handlingUnitInstance = createInstance(compiler, "HandlingUnit");
    Object deliveryItemInstance = createInstance(compiler, "DeliveryItem");

    Method setHandlingUnitMethod = result.deliveryHdrNodeClass().getMethod("setHandlingUnit", handlingUnitInstance.getClass());
    setHandlingUnitMethod.invoke(result.deliveryHdrNodeInstance, handlingUnitInstance);

    Method setDeliveryItemMethod = result.deliveryHdrNodeClass().getMethod("setDeliveryItem", deliveryItemInstance.getClass());
    setDeliveryItemMethod.invoke(result.deliveryHdrNodeInstance, deliveryItemInstance);

    // Set other properties on DeliveryHdrNode
    Method setDeliveryheadergrossweightMethod = result.deliveryHdrNodeClass().getMethod("setDeliveryheadergrossweight", float.class);
    float grossWeight = 11.09F;
    setDeliveryheadergrossweightMethod.invoke(result.deliveryHdrNodeInstance(), grossWeight);

    Method setDeliveryheadergrossweightunitMethod = result.deliveryHdrNodeClass().getMethod("setDeliveryheadergrossweightunit", String.class);
    setDeliveryheadergrossweightunitMethod.invoke(result.deliveryHdrNodeInstance(), "kg");

    Method setDeliveryheadergrossvolumeMethod = result.deliveryHdrNodeClass().getMethod("setDeliveryheadergrossvolume", float.class);
    float grossVolume = 15.17F;
    setDeliveryheadergrossvolumeMethod.invoke(result.deliveryHdrNodeInstance(), grossVolume);

    // Generate the XML
    return convertXML.convertIntoXML(formInstance, formClass);

  }

  private static Result getResult(SimpleCompiler compiler) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    Class<?> deliveryHdrNodeClass = getCustomClass(compiler);
    // Creating instance of DeliveryHdrNode
    Object deliveryHdrNodeInstance = deliveryHdrNodeClass.getDeclaredConstructor().newInstance();
    return new Result(deliveryHdrNodeClass, deliveryHdrNodeInstance);
  }

  private record Result(Class<?> deliveryHdrNodeClass, Object deliveryHdrNodeInstance) {
  }

  private static Class<?> getCustomClass(SimpleCompiler compiler) throws ClassNotFoundException {
    // Load the deliveryHrdNodeClass
    Class<?> deliveryHdrNodeClass = compiler.getClassLoader().loadClass("com.DynamicJavaClassesAtRuntime.DynamicJavaClasses.generated.Form$DeliveryHdrNode");
    return deliveryHdrNodeClass;
  }

  private static Object getInstance(Class<?> formClass) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      return formClass.getDeclaredConstructor().newInstance();
  }

  // Create instance for child node
  public Object createInstance(SimpleCompiler compiler, String className) throws Exception{
    // Load the class
    Class<?> loadedClass = compiler.getClassLoader().loadClass("com.DynamicJavaClassesAtRuntime.DynamicJavaClasses.generated.Form$DeliveryHdrNode$" + className);
    // Creating instance of loadedClass
    return loadedClass.getDeclaredConstructor().newInstance();
  }

}
