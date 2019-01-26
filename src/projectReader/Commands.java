package projectReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Commands {



    static BufferedReader ctagsFunctionList(File f) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("ctags", "-x","--c-kinds=fpd",f.getAbsolutePath());
        System.out.println(pb.command().toString());
        Process proc = pb.start();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        return  bufferedReader;
    }

    static BufferedReader ctagsHeaderInfo(File f) throws IOException {
//        ProcessBuilder pb = new ProcessBuilder("gcc", "-MM",f.getAbsolutePath());
        ProcessBuilder pb = new ProcessBuilder("ctags", "-x","--extras=r","--kinds-c=h","--language-force=c",f.getAbsolutePath());
        System.out.println(pb.command().toString());
        Process proc = pb.start();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//        String line = "" ;
//        while((line = reader.readLine()) != null) {
//            String[] splits = line.split("\\s+");
////            (splits[0].substring(splits[0].lastIndexOf("/")+1));
//        }
        return new BufferedReader(new InputStreamReader(proc.getInputStream()));
    }

    static BufferedReader cflowCommand(File f) throws IOException {
        //TODO Better use of --cpp=gcc -E

        ProcessBuilder pb = new ProcessBuilder("cflow","-l", "--cpp",f.getAbsolutePath());
        pb.redirectErrorStream(true); // merge stdout and stderr FIXED ERROR

        System.out.println(pb.command().toString());
        Process proc = pb.start();
        return new BufferedReader(new InputStreamReader(proc.getInputStream()));

    }

}
