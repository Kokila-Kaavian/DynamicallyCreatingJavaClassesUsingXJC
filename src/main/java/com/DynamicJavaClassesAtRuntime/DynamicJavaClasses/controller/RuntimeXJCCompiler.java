package com.DynamicJavaClassesAtRuntime.DynamicJavaClasses.controller;

import com.DynamicJavaClassesAtRuntime.DynamicJavaClasses.service.CompileGeneratedClasses;
import com.DynamicJavaClassesAtRuntime.DynamicJavaClasses.service.XJCAtRunTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

      // Using Janino compiler, compile the generated source file and get .class files
      return  compileGeneratedClasses.CompileGeneratesClassesUsingCompilerAPI();

    }catch(Exception e){
      System.out.println(e + " " + "error");
      return e.getMessage();
    }
  }
}
