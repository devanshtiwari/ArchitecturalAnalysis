package mainApp;

import fileIO.DirManager;
import fileIO.exceptions.InvalidMasterSourceCodeDirectory;
import projectReader.FilePoint;
import projectReader.FunctionPoint;
import projectReader.CommandProcessor;
import fileIO.CSVWriter;
import fileIO.ProjectDirectoryReader;
import projectReader.ProjectProcessor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

public class App {

    public static void main(String[] args) throws Exception {

        String projectDir = "/Users/devan/Documents/ArchitecturalAnalysis/ProjectsSources/Custom";

        ArrayList<File> projPaths = DirManager.getDirectories(projectDir);

        ProjectProcessor projectProcessor  =new ProjectProcessor();
        CSVWriter csvWriter = new CSVWriter("CSV");

        for (File proj : projPaths) {
            System.out.println("Project Name: " + proj);

            HashMap<String, ArrayList> projectFileInfo; //Including headers
            HashMap<String, ArrayList> projectFunctionInfo; //Including Parameters

            ProjectDirectoryReader projectDirectoryReader = new ProjectDirectoryReader(proj);
            //TODO can be done in a single go

            projectProcessor.projectParse(proj);


//            headers.put(proj.getName(),h);
            ArrayList<ArrayList<String>> dependencyCSVData = projectProcessor.getFileDependencyCSVFormat(projectDirectoryReader,true);
            ArrayList<ArrayList<String>> fileInfoCSV = projectProcessor.getFileInfoCSVFormat(true);
            ArrayList<ArrayList<String>> functionInfoCSV = projectProcessor.getFunctionInfoCSVFormat(true);
            ArrayList<ArrayList<String>> headerToFileCSV = projectProcessor.getHeaderToFileCSVFormat(true);


            csvWriter.setFile("Dependencies",projectDirectoryReader.getProjectFile());
            csvWriter.writez(dependencyCSVData);
            csvWriter.setFile("FileInfo",projectDirectoryReader.getProjectFile());
            csvWriter.writez(fileInfoCSV);
            csvWriter.setFile("FunctionInfo",projectDirectoryReader.getProjectFile());
            csvWriter.writez(functionInfoCSV);
            csvWriter.setFile("HeaderIncludeInfo",projectDirectoryReader.getProjectFile());
            csvWriter.writez(headerToFileCSV);
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



