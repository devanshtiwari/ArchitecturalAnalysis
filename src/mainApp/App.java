package mainApp;

import fileIO.DirManager;

import fileIO.CSVWriter;
import fileIO.ProjectDirectoryReader;
import projectReader.ProjectProcessor;

import java.io.File;
import java.util.*;

public class App {

    public static void main(String[] args) throws Exception {

        String projectDir = "/Users/devan/Documents/ArchitecturalAnalysis/ProjectsSources/Custom";

        ArrayList<File> projPaths = DirManager.getDirectories(projectDir);

        CSVWriter csvWriter = new CSVWriter("CSV");

        int counter = 0;
        int total = projPaths.size();

        for (File proj : projPaths) {
            long start = System.currentTimeMillis();
            System.out.println(++counter+ "/" + total + ". Project Name: " + proj);

            HashMap<String, ArrayList> projectFileInfo; //Including headers
            HashMap<String, ArrayList> projectFunctionInfo; //Including Parameters

            ProjectDirectoryReader projectDirectoryReader = new ProjectDirectoryReader(proj);
            //TODO can be done in a single go
            ProjectProcessor projectProcessor  =new ProjectProcessor();
            projectProcessor.projectParse(proj);


//            headers.put(proj.getName(),h);
            List<List<String>> dependencyCSVData = projectProcessor.getFileDependencyCSVFormat(projectDirectoryReader,true);
            List<List<String>> fileInfoCSV = projectProcessor.getFileInfoCSVFormat(true);
            List<List<String>> functionInfoCSV = projectProcessor.getFunctionInfoCSVFormat(true);
            List<List<String>> headerToFileCSV = projectProcessor.getHeaderToFileCSVFormat(true);


            csvWriter.setFile("Dependencies",projectDirectoryReader.getProjectFile());
            csvWriter.writez(dependencyCSVData);
            csvWriter.setFile("FileInfo",projectDirectoryReader.getProjectFile());
            csvWriter.writez(fileInfoCSV);
            csvWriter.setFile("FunctionInfo",projectDirectoryReader.getProjectFile());
            csvWriter.writez(functionInfoCSV);
            csvWriter.setFile("HeaderIncludeInfo",projectDirectoryReader.getProjectFile());
            csvWriter.writez(headerToFileCSV);
            System.out.println((System.currentTimeMillis() - start)/1000D + " Seconds");
//
////            for (File file : projectDirectoryReader.getSourceFiles()) {
////                projectDependencies = commandProcessor.getDependencies(new File(file.getAbsolutePath()));
////                if (!projectDependencies.keySet().isEmpty()) {
////                    csvWriter.write(projectDependencies, proj);
////                }
////            }
        }

//        FileOutputStream fos = new FileOutputStream("headers");
//        ObjectOutputStream oos = new ObjectOutputStream(fos);
//        oos.writeObject(headers);
//        System.out.println(dirHeaders);
    }
}



