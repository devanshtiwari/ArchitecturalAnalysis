package gitinfo;


import org.eclipse.jgit.api.DiffCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GitLog {

    List<RevCommit> commitList;
    Git git;

    GitLog(File dir) throws GitAPIException {
        try {
            git = Git.open(dir);
            commitList = new ArrayList<>();

//
//            String line = "";
//            BufferedReader bufferedReader = gitLogFunction(dir, "isPrime", "evaluate/prime.c");
//            while( (line = bufferedReader.readLine())!=null) {
//                System.out.println(line);
//            }

            Collection<Ref> allRefs = git.getRepository().getRefDatabase().getRefs();
            RevWalk revWalk = new RevWalk(git.getRepository());

            Iterable<RevCommit> logs = git.log().call();
            for(Ref ref : allRefs){
                revWalk.markStart(revWalk.parseCommit(ref.getObjectId()));
            }
            for(RevCommit commit: revWalk){
                commitList.add(commit);
                System.out.println(commit);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoHeadException e) {
            e.printStackTrace();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }

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
