package fileIO;

import java.io.File;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;

public class ProjectDirectoryReader {

    public File getProjectFile() {
        return projectFile;
    }

    private File projectFile;
    private ArrayList<File> sourceFiles = new ArrayList<>();
    private ArrayList<File> headerFiles = new ArrayList<>();


    public ArrayList<File> getSourceFiles() {
        return sourceFiles;
    }

    public ArrayList<File> getHeaderFiles() {
        return headerFiles;
    }



    public ProjectDirectoryReader(String path) throws NotDirectoryException {
        projectFile = new File(path);
        readDir(projectFile);
    }

    public ProjectDirectoryReader(File path) throws NotDirectoryException {
        projectFile = path;
        readDir(projectFile);
    }

    private void readDir(File dir) throws NotDirectoryException {
        //TODO change it to regex later properly
        if(!dir.isDirectory()) {
//            System.out.println("Input Path is not directory.");
            throw new NotDirectoryException(dir.getPath());
        }
        for(File file: dir.listFiles()){
            if(file.isDirectory())
                readDir(file);
            else {
                if (file.getName().endsWith(".c") || file.getName().endsWith(".h"))
                    sourceFiles.add(file);
                if(file.getName().endsWith(".h"))
                    headerFiles.add(file);
//                    headerFiles.add(projectFile.toURI().relativize(file.toURI()).getPath());
            }

        }
    }



}
