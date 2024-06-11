package com.DynamicJavaClassesAtRuntime.DynamicJavaClasses.controller;

import com.DynamicJavaClassesAtRuntime.DynamicJavaClasses.service.CompileGeneratedClasses;
import com.DynamicJavaClassesAtRuntime.DynamicJavaClasses.service.DynamicClassLoader;
import com.DynamicJavaClassesAtRuntime.DynamicJavaClasses.service.XJCAtRunTime;
import com.sun.tools.javac.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;

@RequestMapping("/api")
@RestController
public class RuntimeXJCCompiler {

  @Autowired
  XJCAtRunTime xjcAtRunTime;

  @Autowired
  CompileGeneratedClasses compileGeneratedClasses;

  @GetMapping("/hello-world")
  public String helloWorld(){
    return "Hello World";
  }

  @GetMapping("/generate-java-class")
  public String GenerateJavaClass(){
    try {
      // Using XJC to generate java source file for the given XSD
      xjcAtRunTime.generateClassesFromXSD();

      // Using JAVA compiler, compile the generated source file and get .class files
      compileGeneratedClasses.CompileGeneratesClassesUsingCompilerAPI();

      // Load the compiled java classes using custom loader
      URL[] urls = {new File("target/generated-sources/xjc").toURI().toURL()};
      DynamicClassLoader classLoader = new DynamicClassLoader(urls, Main.class.getClassLoader());

      // Load specific class by name
      Class<?> FormClass = classLoader.loadClassFromFile("com.DynamicJavaClassesAtRuntime.DynamicJavaClasses.generated.Form");
      Method[] methods = FormClass.getMethods();
      System.out.println(Arrays.stream(methods).toList() + "methods");

      for (Method method : methods){
        System.out.println("Method name" + method + method.getName());
        System.out.println("Method return type" + method.getReturnType() + method.getParameterCount());
      }

    }catch(Exception e){
      System.out.println(e + " " + "error");
      return e.getMessage();
    }
    return "Success";
  };
}
