package fileIO;

import fileIO.exceptions.InvalidMasterSourceCodeDirectory;

import java.io.File;
import java.util.ArrayList;

public class DirManager {

    public static ArrayList<File> getDirectories(String dir) throws InvalidMasterSourceCodeDirectory {
        File file = new File(dir);
        ArrayList<File> absolutePaths = new ArrayList<>();
        if(!file.isDirectory())
            return absolutePaths;
        else{
            for(File subdir:file.listFiles()){
                if(subdir.isDirectory())
                    absolutePaths.add(subdir);
            }
        }
        if (absolutePaths.size()==0)
            throw new InvalidMasterSourceCodeDirectory("Invalid Directory which contains all the project source codes");

        return absolutePaths;
    }
}

