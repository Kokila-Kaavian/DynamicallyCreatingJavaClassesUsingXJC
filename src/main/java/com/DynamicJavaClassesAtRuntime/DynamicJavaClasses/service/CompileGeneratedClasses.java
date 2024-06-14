package com.DynamicJavaClassesAtRuntime.DynamicJavaClasses.service;

import org.codehaus.janino.SimpleCompiler;
import org.eclipse.jdt.core.compiler.batch.BatchCompiler;
import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Using JAVA compiler, compile the generated source file and get .class files
@Service
public class CompileGeneratedClasses {

    @Autowired
    SetS4hanaData setS4hanaData;

//    public void CompileGeneratesClassesUsingCompilerAPI() throws Exception{
//        String sourceDir = "target/generated-sources/xjc";
////        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
//            JavaCompiler compiler = new EclipseCompiler();
//        // Checks the compiler is available
//        if (compiler == null){
//            throw new Exception("Cannot find Java Compiler");
//        }
//
//        List<String> files = new ArrayList<>();
//        collectJavaFiles(new File(sourceDir), files);
//
//        System.out.println(files + "Files need to compile");
//
//        List<String> compileOptions = new ArrayList<>();
//        compileOptions.add("-d");
//        compileOptions.add(sourceDir);
////        compileOptions.add("-proc:none");
//        compileOptions.addAll(files);
//
//        compiler.run(null, null, null, compileOptions.toArray(new String[0]));
//
//    }

    // Get the files from source directory and append the file path in to files array
    private void collectJavaFiles(File dir, List<File> files){
        System.out.println(dir.listFiles() + " " + "files");
        for (File file : dir.listFiles()){
            if (file.isDirectory()) {
                collectJavaFiles(file, files);
            } else if (file.getName().endsWith(".java")) {
                files.add(file);
            }
        }
    }

    public String CompileGeneratesClassesUsingCompilerAPI() throws Exception{
        String sourceDir = "target/generated-sources/com/DynamicJavaClassesAtRuntime/DynamicJavaClasses/generated/";

        File sourceDirectory = new File(sourceDir);
        if (!sourceDirectory.exists() || !sourceDirectory.isDirectory()) {
            throw new Exception("Source directory does not exist or is not a directory: " + sourceDir);
        }

        List<File> files = new ArrayList<>();
        collectJavaFiles(new File(sourceDir), files);

        if (files.isEmpty()){
            throw new Exception("Files are empty");
        }


        SimpleCompiler compiler = new SimpleCompiler();
        String xmlData = "";

        for (File file : files) {
            System.out.println(file + " CompilingFile");


            /** Using Janino package*/
            Path javaFilePath = file.toPath();
            String javaSource = new String(Files.readAllBytes(javaFilePath));

            if (file.getName().equals("Form.java")) {
                compiler.cook(javaSource);
                Class<?> formClass = compiler.getClassLoader().loadClass("com.DynamicJavaClassesAtRuntime.DynamicJavaClasses.generated.Form");

                 xmlData = setS4hanaData.insertS4hanaData(compiler, formClass);
            }

        }
        for (File file : files) {
            if (!file.getName().equals("Form.java")){
                Path javaFilePath = file.toPath();
                System.out.println(javaFilePath);
                String javaSource = new String(Files.readAllBytes(javaFilePath));

                SimpleCompiler objCompiler = new SimpleCompiler();
                objCompiler.setParentClassLoader(compiler.getClassLoader());
                objCompiler.cook(javaSource);
                Class<?> objFacClass = objCompiler.getClassLoader().loadClass("com.DynamicJavaClassesAtRuntime.DynamicJavaClasses.generated.ObjectFactory");

//                Object objFacInstance = objFacClass.getDeclaredConstructor().newInstance();
//                Method createFormMethod = objFacClass.getMethod("createForm");
//                Object formInstance = createFormMethod.invoke(objFacInstance);

            }
        }
        return xmlData;
    }
}