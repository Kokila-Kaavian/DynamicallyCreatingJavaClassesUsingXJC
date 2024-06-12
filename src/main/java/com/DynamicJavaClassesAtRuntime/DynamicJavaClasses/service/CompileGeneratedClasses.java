package com.DynamicJavaClassesAtRuntime.DynamicJavaClasses.service;

import org.eclipse.jdt.core.compiler.batch.BatchCompiler;
import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;
import org.springframework.stereotype.Service;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

// Using JAVA compiler, compile the generated source file and get .class files
@Service
public class CompileGeneratedClasses {

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
    private void collectJavaFiles(File dir, List<String> files){
        System.out.println(dir.listFiles() + "files");
        for (File file : dir.listFiles()){
            if (file.isDirectory()) {
                collectJavaFiles(file, files);
            } else if (file.getName().endsWith(".java")) {
                files.add(file.getPath());
            }
        }
    }

    public void CompileGeneratesClassesUsingCompilerAPI() throws Exception{
        String sourceDir = "target/generated-sources/com\\DynamicJavaClassesAtRuntime\\DynamicJavaClasses\\generated\\";
        String outputDir = "target/classes";

        List<String> files = new ArrayList<>();
        collectJavaFiles(new File(sourceDir), files);

        if (files.isEmpty()){
            throw new Exception("Files are empty");
        }

        System.out.println(System.getProperty("java.home"));


        for (String file : files) {
//            System.out.println(file + " CompilingFile");

            File ouDir = new File(outputDir);

            String[] compilerOption = new String[]{
                    "-classpath", System.getProperty("java.class.path"),
                    "-d", ouDir.getAbsolutePath(),
                    "-source", "1.8",
                    "-target", "1.8",
                    file
            };

            if (!ouDir.exists()) {
                System.out.println("Need to create");
                ouDir.mkdirs();
            }else {
                System.out.println("Exists");
            }

            StringWriter writer = new StringWriter();
            PrintWriter out = new PrintWriter(writer);

            Boolean result = BatchCompiler.compile(compilerOption, out, out, null);
            System.out.println(result + " " + "result");
            System.out.println(writer.toString());
        }

    }


}