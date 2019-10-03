package mainApp;

import fileIO.DirManager;

import fileIO.CSVWriter;
import fileIO.ProjectDirectoryReader;
import gitinfo.GitLog;
import org.eclipse.jgit.revwalk.RevCommit;
import projectReader.ProjectProcessor;

import java.io.File;
import java.util.*;

public class App {

    public static void main(String[] args) throws Exception {

        String projectDir = "/Users/devan/Documents/ArchitecturalAnalysis/ProjectsSources/TestProjects2";
        CSVWriter csvWriter = new CSVWriter("CSV");
        int counter = 0;
        ArrayList<File> projPaths = DirManager.getDirectories(projectDir);

        int total = projPaths.size();

        GitLog gitLog = new GitLog(new File("/Users/devan/Documents/ArchitecturalAnalysis/ProjectsSources/TestProjects/TestCProject"));



        for (File proj : projPaths) {
            long start = System.currentTimeMillis();
            System.out.println(++counter+ "/" + total + ". Project Name: " + proj);

            ProjectDirectoryReader projectDirectoryReader = new ProjectDirectoryReader(proj);
            //TODO can be done in a single go
            ProjectProcessor projectProcessor  =new ProjectProcessor();
            projectProcessor.projectParse(proj);


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

        }


        //Commit analysis
//
//        GitLog gitLog = new GitLog(new File("/Users/devan/Documents/ArchitecturalAnalysis/ProjectsSources/TestProjects/TestCProject"));
//
//        Stack<RevCommit> commitList = gitLog.commitList;
//        int total = commitList.size();
//        int commitCounter = 0;
//
//        while (!commitList.isEmpty()) {
//            RevCommit commit = commitList.pop();
//            System.out.println(commit);
//            gitLog.checkOut(commit);
//
//            long start = System.currentTimeMillis();
//            System.out.println(++commitCounter+ "/" + total + ". Project Name: " + projPaths.get(0));
//
//            ProjectDirectoryReader projectDirectoryReader = new ProjectDirectoryReader(projPaths.get(0));
//            //TODO can be done in a single go
//            ProjectProcessor projectProcessor  =new ProjectProcessor();
//            projectProcessor.projectParse(projPaths.get(0));
//
//
//            List<List<String>> dependencyCSVData = projectProcessor.getFileDependencyCSVFormat(projectDirectoryReader,true);
//            List<List<String>> fileInfoCSV = projectProcessor.getFileInfoCSVFormat(true);
//            List<List<String>> functionInfoCSV = projectProcessor.getFunctionInfoCSVFormat(true);
//            List<List<String>> headerToFileCSV = projectProcessor.getHeaderToFileCSVFormat(true);
//
//
//            csvWriter.setFile("Dependencies",projectDirectoryReader.getProjectFile(), commitCounter);
//            csvWriter.writez(dependencyCSVData);
//            csvWriter.setFile("FileInfo",projectDirectoryReader.getProjectFile(), commitCounter);
//            csvWriter.writez(fileInfoCSV);
//            csvWriter.setFile("FunctionInfo",projectDirectoryReader.getProjectFile(), commitCounter);
//            csvWriter.writez(functionInfoCSV);
//            csvWriter.setFile("HeaderIncludeInfo",projectDirectoryReader.getProjectFile(), commitCounter);
//            csvWriter.writez(headerToFileCSV);
//            System.out.println((System.currentTimeMillis() - start)/1000D + " Seconds");
//
//        }





    }
}