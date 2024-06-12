package com.DynamicJavaClassesAtRuntime.DynamicJavaClasses.service;

import com.sun.codemodel.JCodeModel;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.api.ErrorListener;
import com.sun.tools.xjc.api.S2JJAXBModel;
import com.sun.tools.xjc.api.SchemaCompiler;
import com.sun.tools.xjc.api.XJC;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

// Using XJC to generate java source file for the given XSD
@Service
public class XJCAtRunTime {

  public void generateClassesFromXSD() throws Exception{
    String outputPath = "target/generated-sources/";
    String packageName = "com.DynamicJavaClassesAtRuntime.DynamicJavaClasses.generated";

    // Get the source Schema File Path (XSD)
    File file = ResourceUtils.getFile("classpath:XSD/Loading-List.xsd");

    // Initialize XJC schema-compilers
    SchemaCompiler sc = XJC.createSchemaCompiler();

    // Set the output packageName
    sc.forcePackageName(packageName);

    // Set the source schema which going to parse and change to class
    InputSource inputSource = new InputSource(file.toURI().toString());
    sc.parseSchema(inputSource);

    // Bind all the inputs and generate model(java class)
    S2JJAXBModel model = sc.bind();

    // Check model is created
    if (model == null){
      System.out.println("Model is null");
    }else{
      System.out.println("Model has value");
    }

    // Checks the output directory is exist
    File outputDir = new File(outputPath);
    if (!outputDir.exists()){
      System.out.println("There is no directory 1");
      if (!outputDir.mkdirs()){
        System.out.println("There is no directory");
      }
    }

    // Set the output directory path
    Options options = new Options();
    options.targetDir = outputDir;

    JCodeModel codeModel = model.generateCode(new com.sun.tools.xjc.Plugin[0], new ErrorListener() {
      @Override
      public void error(SAXParseException e) {
        System.out.println(e.getMessage() + "exception1");
      }

      @Override
      public void fatalError(SAXParseException e) {
        System.out.println(e.getMessage() + "exception2");

      }

      @Override
      public void warning(SAXParseException e) {
        System.out.println(e.getMessage() + "exception3");

      }

      @Override
      public void info(SAXParseException e) {
        System.out.println(e.getMessage() + "exception4");

      }
    });

    // Write the generated class in a specific output directory
    codeModel.build(outputDir);

  }

}
