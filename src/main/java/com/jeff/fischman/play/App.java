package com.jeff.fischman.play;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class App 
{
    public static final String DefaultInputFile = "noCycles.txt";
    public static void main( String[] args )
    {
        String inputFile = DefaultInputFile;
        if (args.length > 0) {
            inputFile = args[0];
        }

        Stream<String> fileStream = getFileStream(inputFile);
        if (fileStream == null) {
            return;
        }

        System.out.printf("App will be using data from file: %s\n", inputFile);
        DependencyMap dependencyMap = DependencyMap.create(fileStream);
        DepChaser depChaser = new DepChaser(dependencyMap);
        boolean res = depChaser.validateNoDependencyCycles();
        System.out.printf("DepChaser %s find cycles", res ? "did not" : "did");
    }

    public static Stream<String> getFileStream(String fileName) {
        try {
            return  Files.lines(Paths.get(fileName));
        } catch (IOException e) {
            System.err.printf("Unable to create stream from file: %s\n", fileName);
            return null;
        }
    }

}
