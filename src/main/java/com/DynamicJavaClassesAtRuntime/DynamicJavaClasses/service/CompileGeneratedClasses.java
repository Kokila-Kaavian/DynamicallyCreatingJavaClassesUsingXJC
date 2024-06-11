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
        String sourceDir = "src/main/java/com\\DynamicJavaClassesAtRuntime\\DynamicJavaClasses\\generated\\";
        String outputDir = "target/generated-sources/xjc";

        List<String> files = new ArrayList<>();
        collectJavaFiles(new File(sourceDir), files);

        if (files.isEmpty()){
            throw new Exception("Files are empty");
        }

        for (String file : files) {
            System.out.println(file + " CompilingFile");

            String[] compilerOption = new String[]{
                    "-d", outputDir,
                    file
            };

            StringWriter writer = new StringWriter();
            PrintWriter out = new PrintWriter(writer);

            BatchCompiler.compile(compilerOption, out, out, null);
        }

    }


}