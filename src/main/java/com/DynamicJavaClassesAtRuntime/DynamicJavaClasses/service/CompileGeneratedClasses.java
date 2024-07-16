package com.DynamicJavaClassesAtRuntime.DynamicJavaClasses.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.janino.SimpleCompiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Using Janino compiler to compile the generated source file and get .class files
@Service
public class CompileGeneratedClasses {

    @Autowired
    SetS4hanaData setS4hanaData;

    public String CompileGeneratesClassesUsingCompilerAPI() throws Exception{
        String sourceDir = "target/generated-sources/com/DynamicJavaClassesAtRuntime/DynamicJavaClasses/generated/";

        File sourceDirectory = new File(sourceDir);
        if (!sourceDirectory.exists() || !sourceDirectory.isDirectory()) {
            throw new Exception("Source directory does not exist or is not a directory: " + sourceDir);
        }

        List<File> files = new ArrayList<>();
        collectJavaFiles(new File(sourceDir), files);

        if (files.isEmpty()){
            throw new Exception("There is no source file exist.");
        }

        // Initialize the janino's SimpleCompiler
        SimpleCompiler compiler = new SimpleCompiler();
        String xmlData = "";

        for (File file : files) {
            System.out.println(file + " CompilingFile");

            Path javaFilePath = file.toPath();
            String javaSource = new String(Files.readAllBytes(javaFilePath));

            if (file.getName().equals("Form.java")) {
                compiler.cook(javaSource);

                // Load the compiled class in runtime container
                Class<?> formClass = compiler.getClassLoader().loadClass("com.DynamicJavaClassesAtRuntime.DynamicJavaClasses.generated.Form");

                // Create xml by feed the S4hana data
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

            }
        }
        return xmlData;
    }

    // Get the files from source directory and push the file path in to files array
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
}