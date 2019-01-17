package gitinfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class GitAPI {
    String BASE;
    String ACCESS_TOKEN = "0b964cc12e7915ee3687b495fc21cec1bfaeea57";
    String stats = "/stats/contributors";

    GitAPI(){
        BASE = "https://api.github.com/repos/";
    }

    JSONArray callCommitStats(String project) throws IOException, InterruptedException {
        String commitStats;
        commitStats = BASE + project + stats;
        commitStats += "?access_token=" + ACCESS_TOKEN;
        System.out.println(commitStats);
        return new JSONArray(call(commitStats));
    }

    JSONObject callSummary(String project) throws IOException, InterruptedException {
        String summary;
        summary = BASE + project;
        summary += "?access_token=" + ACCESS_TOKEN;

        System.out.println(summary);
        return new JSONObject(call(summary));
    }

    private String call(String URL) throws IOException, InterruptedException {

        URL obj = new URL(URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        int responseCode = 0;
        //add request header
//        con.setRequestProperty("User-Agent", USER_AGENT);

        while(responseCode != 200) {

//        TimeUnit.SECONDS.sleep(1);
            responseCode = con.getResponseCode();
            if(responseCode==202) {
                TimeUnit.SECONDS.sleep(1);
                con = (HttpURLConnection) obj.openConnection();
            }
            System.out.println("\nSending 'GET' request to URL : " + URL);
            System.out.println("Response Code : " + responseCode);
        }
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());
//        System.out.println(jsonArray.toString());
        return response.toString();

    }
}
