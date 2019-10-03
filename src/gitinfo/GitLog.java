package gitinfo;


import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GitLog {


//    public static List<RevCommit>  readCommits(File csvFile, File dir) throws IOException {
//        BufferedReader br  = new BufferedReader(new FileReader(csvFile));
//        String line;
//        Git git = Git.open(dir);
//        RevWalk revWalk = new RevWalk(git.getRepository());
//        while((line = br.readLine()) != null)
//        {
//            RevCommit commit = null;
//            String commitHash = line;
//            if(commitHash.length() != 0)
//                commit = revWalk.parseCommit(ObjectId.fromString(commitHash));
//        }
//        return commitList;
//    }

    public Stack<RevCommit> commitList;
    Git git;

    public GitLog(File dir) throws IOException, GitAPIException {
        git = Git.open(dir);
        commitList = new Stack<>();

        Iterable<RevCommit> logs = git.log().add(git.getRepository().resolve("remotes/origin/master")).call();

        for(RevCommit revCommit: logs){
            commitList.push(revCommit);
            System.out.println(revCommit.toString());
        }


//            String line = "";
//            BufferedReader bufferedReader = gitLogFunction(dir, "isPrime", "evaluate/prime.c");
//            while( (line = bufferedReader.readLine())!=null) {
//                System.out.println(line);
//            }


//            Collection<Ref> allRefs = git.getRepository().getRefDatabase().getRefs();
//            RevWalk revWalk = new RevWalk(git.getRepository());
//
//            Iterable<RevCommit> logs = git.log().call();
//            for(Ref ref : allRefs){
//                revWalk.markStart(revWalk.parseCommit(ref.getObjectId()));
//            }
//            for(RevCommit commit: revWalk){
//                commitList.add(commit);
//                System.out.println(commit);
//            }
    }

    public void checkOut(RevCommit commit) throws GitAPIException {
        git.checkout().setStartPoint(commit).setCreateBranch(false).setName(commit.name()).call();
//        git.checkout().setName(commit.toString()).call();
    }

    BufferedReader gitLogFunction(File dir, String function, String file) throws IOException {

            ProcessBuilder pb = new ProcessBuilder("git", "log","-L:" +function + ":" +file);
            pb.directory(dir);
            pb.redirectErrorStream(true); // merge stdout and stderr FIXED ERROR

            System.out.println(pb.command().toString());
            Process proc = pb.start();
            return new BufferedReader(new InputStreamReader(proc.getInputStream()));
        }

}
