package projectReader;

import fileIO.ProjectDirectoryReader;

import java.io.File;
import java.nio.file.NotDirectoryException;
import java.util.*;

public class ProjectProcessor {

    private CommandProcessor commandProcessor = new CommandProcessor();
//    private HashMap<String, HashSet<String>> fileHeaders = new HashMap<>();

    ArrayList<ArrayList<String>> fileHeaders = new ArrayList<>();
    ProjectDirectoryReader projectDirectoryReader;

    public void projectParse(File proj) throws NotDirectoryException {

        projectDirectoryReader = new ProjectDirectoryReader(proj);
        ArrayList<File> dirHeaders = projectDirectoryReader.getHeaderFiles();
        commandProcessor.setAllFunctions(projectDirectoryReader);
        this.fileHeaders = commandProcessor.setHeaders(projectDirectoryReader);
    }

    public ArrayList<ArrayList<String>> getFileDependencyCSVFormat(ProjectDirectoryReader projectDirectoryReader, Boolean headers){

        ArrayList<ArrayList<String>> csv = new ArrayList<>();
        if(headers) {
            ArrayList<String> header = new ArrayList<>(Arrays.asList("File","Function","File", "Function"));
            csv.add(header);
        }
        long count = 0;
        for(File file:projectDirectoryReader.getSourceFiles()){
            HashMap<FilePoint, FilePoint> projectDependencies = commandProcessor.getDependencies(file);
            if (!projectDependencies.keySet().isEmpty()) {
                for (FilePoint fp : projectDependencies.keySet()) {
                    ArrayList<String> line = new ArrayList<>();
                    if(projectDependencies.get(fp).nullCheck() || fp.nullCheck())
                        continue;
                    //Not mapping header files.
                    if(projectDependencies.get(fp).getFile().getName().endsWith(".h"))
                        continue;

                    line.add(projectDirectoryReader.getProjectFile().toURI().relativize(fp.getFile().toURI()).getPath());
                    line.add(fp.getFunction());
                    if(projectDependencies.get(fp).getFile() == null)
                        line.add(" ");
                    else
                        line.add(projectDirectoryReader.getProjectFile().toURI().relativize(projectDependencies.get(fp).getFile().toURI()).getPath());
                    line.add(projectDependencies.get(fp).getFunction());
                    csv.add(line);
                }
            }
        }

        count = commandProcessor.timeTaken;

        return csv;
    }

    public ArrayList<ArrayList<String>> getFileInfoCSVFormat(Boolean headers) {
        if(headers) {
            ArrayList<String> header = new ArrayList<>(Arrays.asList("File","Headers"));
            fileHeaders.add(0,header);
        }
        return fileHeaders;
    }

    public ArrayList<ArrayList<String>> getFunctionInfoCSVFormat(Boolean headers){
        ArrayList<ArrayList<String>> csv = new ArrayList<>();
        if(headers) {
            ArrayList<String> header = new ArrayList<>(Arrays.asList("File Name","Function Name","Parameters","Count"));
            csv.add(header);
        }
        HashSet<FunctionPoint> functionPoints = commandProcessor.getProjectFunctionsList();
        for(FunctionPoint functionPoint : functionPoints){
            ArrayList<String> csvLine = new ArrayList<>();
            if(functionPoint.getType().equals("function")) {
                csvLine.add(projectDirectoryReader.getProjectFile().toURI().relativize(functionPoint.getLocation().toURI()).getPath());
                csvLine.add(functionPoint.getName());
                String params = String.join(";",functionPoint.getParameters());
                csvLine.add(String.join(";",functionPoint.getParameters()));
                if(params.isEmpty())
                    csvLine.add("0");
                else
                    csvLine.add(Integer.toString(params.split(";").length));
                csv.add(csvLine);
            }
        }
        return csv;
    }

    public ArrayList<ArrayList<String>> getHeaderToFileCSVFormat(Boolean headers) {
        HashMap<File, HashSet<File>> headerToFile = commandProcessor.getHeaderToFile();
        ArrayList<ArrayList<String>> csv = new ArrayList<>();
        if(headers) {
            ArrayList<String> header = new ArrayList<>(Arrays.asList("Header File Location","Header Name","Included By","Count"));
            csv.add(header);
        }
        for(File header:headerToFile.keySet()){
            HashSet<File> headerFileUsed = headerToFile.get(header);
            ArrayList<String> csvLine = new ArrayList<>();
            StringBuilder filesUsed = new StringBuilder();
            for(File f:headerFileUsed){
                filesUsed.append(projectDirectoryReader.getProjectFile().toURI().relativize(f.toURI()).getPath()).append(";");
            }
            csvLine.add(projectDirectoryReader.getProjectFile().toURI().relativize(header.toURI()).getPath());
            csvLine.add(header.getName());
            csvLine.add(filesUsed.toString());
            if(filesUsed.length() == 0)
                csvLine.add("0");
            else
                csvLine.add(Integer.toString(filesUsed.toString().split(";").length));
            csv.add(csvLine);
        }
        return csv;
    }
}