package gitinfo;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

public class APICaller {

    public static void main(String[] args) throws IOException, ParseException, InterruptedException, GitAPIException {

        GitLog gitLog = new GitLog(new File("/Users/devan/Documents/ArchitecturalAnalysis/ProjectsSources/TestProjects/TestCProject"));


//        FileReader fileReader = new FileReader("api.txt");
//        fileReader.consoleOut();
//        List<String> repos = fileReader.getFileData();
//        GitAPI gitAPI = new GitAPI();
//        JSONArray jsonArrayCommitStats;
//        JSONObject jsonArraySummary;


//        CSVWriter csvWriter = new CSVWriter(new File("github"),true);
//        for(String repo: repos){
//            jsonArrayCommitStats = gitAPI.callCommitStats(repo);
//            jsonArraySummary = gitAPI.callSummary(repo);
//            List list = processJSON(jsonArrayCommitStats, jsonArraySummary);
//            csvWriter.write(list);
//        }
//        csvWriter.close();

    }

    static List<String> processJSON(JSONArray jsonArrayCommitStats, JSONObject jsonArraySummary) throws ParseException {
        List<String> list = new ArrayList<>();
        int count = 0;
        for(Object jsonObject: jsonArrayCommitStats){
            count += Integer.parseInt(((JSONObject) jsonObject).get("total").toString());
        }
        String full_name = jsonArraySummary.getString("full_name");
        int stargazers_count = jsonArraySummary.getInt("stargazers_count");
        int watchers_count = jsonArraySummary.getInt("watchers_count");
        int forks_count = jsonArraySummary.getInt("forks_count");
        int open_issues_count = jsonArraySummary.getInt("open_issues_count");
        int subscribers_count = jsonArraySummary.getInt("subscribers_count");
        //Adding to list of the
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date created_at = format.parse(jsonArraySummary.getString("created_at"));

        int diffDays = (int)( (Instant.now().getEpochSecond() - created_at.toInstant().getEpochSecond()) / ( 60 * 60 * 24));

        System.out.println(diffDays);
        list.add(full_name);
        list.add(Integer.toString(count));
        list.add(Integer.toString(stargazers_count));
        list.add(Integer.toString(forks_count));
        list.add(Integer.toString(open_issues_count));
        list.add(Integer.toString(subscribers_count));
        list.add(Integer.toString(diffDays));
        System.out.println(list);
        return list;
    }


}
