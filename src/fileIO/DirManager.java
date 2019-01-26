package fileIO;

import fileIO.exceptions.InvalidMasterSourceCodeDirectory;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class DirManager {

    public static ArrayList<File> getDirectories(File file) throws InvalidMasterSourceCodeDirectory {
        ArrayList<File> absolutePaths = new ArrayList<>();
        if(!file.isDirectory())
            return absolutePaths;
        else{
            for(File subdir: Objects.requireNonNull(file.listFiles())){
                if(subdir.isDirectory())
                    absolutePaths.add(subdir);
            }
        }
        if (absolutePaths.size()==0)
            throw new InvalidMasterSourceCodeDirectory("Invalid Directory which contains all the project source codes");

        return absolutePaths;
    }

    public static ArrayList<File> getDirectories(String dir) throws InvalidMasterSourceCodeDirectory{
        File file = new File(dir);
        return getDirectories(file);
    }

    public static ArrayList<File> getFiles(File dir){
        ArrayList<File> files = new ArrayList<>();
        for(File f : Objects.requireNonNull(dir.listFiles())){
            if(f.isFile())
                files.add(f);
        }
        return files;
    }
}

