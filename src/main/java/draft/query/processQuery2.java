package draft.query;

import draft.utils.testTimer;
import net.spy.memcached.MemcachedClient;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static draft.memcached.testMemcached.*;
import static draft.utils.folder.deleteDirectory;
//import static draft.utils.folder.listFileMc;
import static draft.utils.terminal.readTerminal;
import static draft.utils.writeFile.storeValue;
import static draft.warpscript.utils.*;
//import io.restassured.RestAssured;
import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.web.server.LocalServerPort;


public class processQuery2 {
    @LocalServerPort
    static int port;

    static String token = "YevxHKGF6zmOOfTvxnP_76v1n_i3dAPm6DAyWCgKE5q5mfCc6ZHGuT5i40atfkit8YgqRBLBZ3EfPZIdLJMG5ck7sGQYUXy07gDelvmoM6BNKR_SaCrynV";
    static String commandToken = "'"+token+"' 'token' STORE";
    static String host = "http://145.239.64.49:8080/api/v0/exec";
    static String http = "http://localhost";
    //static String folderMC2 = System.getProperty("user.dir") + "/" + "mc2";
    static String folderMC2 = http + ":" + port + System.getProperty("user.dir") + "/" + "mc2";
    static String folderResult = "result";
    static String formatDate = "yyyy_MM_dd_HH_mm_ss";
    static String beginQuery = "Query";
    static String endQuery = "Area";
    static String beginArea = "Area";
    static String endArea = "T1";
    static String beginT1 = "T1";
    static String endT1 = "T2";
    static String beginT2 = "T2";
    static String endT2 = "";
    static String dateSpaceTime = "T";
    static String spaceFormatDate = "_";
    static boolean cleanMemcached = true;
    public static void main (String[] args) throws IOException, java.text.ParseException, InterruptedException, ExecutionException {
        List<String> listQuery = new ArrayList<>();
        String Query1 = "Query 2 Area A T1 '2020-02-01T00:00:00.0Z' T2 '2020-02-05T00:00:00.0Z'";
        String Query2 = "Query 2 Area A T1 '2020-02-07T00:00:00.0Z' T2 '2020-02-10T00:00:00.0Z'";
        String Query3 = "Query 2 Area A T1 '2020-02-12T00:00:00.0Z' T2 '2020-02-15T00:00:00.0Z'";
        String Query4 = "Query 2 Area A T1 '2020-02-20T00:00:00.0Z' T2 '2020-02-29T00:00:00.0Z'";

        String Query5 = "Query 2 Area A T1 '2020-01-25T00:00:00.0Z' T2 '2020-01-29T00:00:00.0Z'";
        String Query6 = "Query 2 Area A T1 '2020-03-02T00:00:00.0Z' T2 '2020-03-04T00:00:00.0Z'";

        listQuery.add(Query1);
        listQuery.add(Query2);
        listQuery.add(Query3);
        listQuery.add(Query4);
        listQuery.add(Query5);
        listQuery.add(Query6);

        String query = "Query 2 Area A T1 '2020-02-21T00:00:00.0Z' T2 '2020-02-28T00:00:00.0Z'";
        listQuery.add(query);
        testTimer timer = new testTimer();
        //draft.utils.testTimer;
        //draft.utils.testTimer();
        MemcachedClient mcc = null;
        mcc = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
        System.out.println("Connection to server sucessfully");
        //for(int i = 0; i < 100; i ++){
        testBoth(listQuery, mcc);
        mcc.shutdown();
        //}
        System.exit(0);

    }

    public static void testBoth(List<String> listQuery, MemcachedClient mcc) throws java.text.ParseException, IOException, ExecutionException, InterruptedException {
        String[] nameValue = new String[2];
        nameValue[0] = "MemCached";
        nameValue[1] = "Directly";
        double[] time = new double[2];
        time[0] = runWithMemCached(listQuery, mcc);
        time[1] = runWithoutMemCached(listQuery, mcc);
        System.out.println("Compare time running "+ time[0] +" and " + time[1] + "seconds");
        storeValue(listQuery,time,nameValue);
    }
    public static void clearMemcached(List<String> query, MemcachedClient mcc) throws IOException, ExecutionException, InterruptedException, ParseException {

        for(int i = 0; i < query.size(); i++){
            String Query = prepareQuery(query.get(i));
            formQuery formQuery = new formQuery(getQuery(Query), getArea(Query),getDate1(Query), getDate2(Query));
            String key = getNameFile(draft.warpscript.utils.writeNameFile(formQuery));

            System.out.println("The key " + key + " is deleted:" + mcc.delete(key).isDone());
            /*
            // try to add data with existing key
            Future fo = mcc.delete(key);
            // print status of delete method
            System.out.println("delete status:" + fo.get());

             */
        }
    }
    public static double runWithMemCached(List<String> queries, MemcachedClient mcc) throws java.text.ParseException, IOException, ExecutionException, InterruptedException {
        //clearFolder(folderQuery(queries.get(1)));
        clearFolder(queries.get(0));
        String query = queries.get(queries.size()-1);
        clearMemcached(queries, mcc);
        prepareMemcached(queries, mcc);
        double time = runQuery(query, mcc);
        return time;
    }
    public static double runQuery(String query, MemcachedClient mcc) throws java.text.ParseException, IOException {
        long start = System.currentTimeMillis();
        process(prepareQuery(query), commandToken, host, mcc);
        long stop = System.currentTimeMillis();
        double actualTime = (double)(stop-start)/1000.0;
        return actualTime;
    }
    public static double runQueryBasicCache(List<String> queries, MemcachedClient mcc) throws java.text.ParseException, IOException {
        long start = System.currentTimeMillis();
        for (int i = 0; i< queries.size(); i++){
            System.out.println("Running query "+ queries.get(i) + "+++++++++++++");
            processBasicCache(prepareQuery(queries.get(i)), commandToken, host, mcc);
        }

        long stop = System.currentTimeMillis();
        double actualTime = (double)(stop-start)/1000.0;
        return actualTime;
    }
    public static double runQuerySemanticCache(List<String> queries, MemcachedClient mcc) throws IOException, ParseException {
        long start = System.currentTimeMillis();
        String[] listQuery = new String[queries.size()];
        formQuery [] listFormQuery = new formQuery[queries.size()];

        for (int i = 0; i < listQuery.length; i++){
            System.out.println("Running query "+ queries.get(i) + "+++++++++++++");
            listQuery[i] = prepareQuery(queries.get(i));
            listFormQuery[i] = new formQuery(getQuery(listQuery[i]), getArea(listQuery[i]), getDate1(listQuery[i]), getDate2(listQuery[i]));
            process(listQuery[i], commandToken, host, mcc);
        }
        //clearMemcached(queries);
        //prepareMemcached(queries);
        //process(prepareQuery(query), commandToken, host);
        long stop = System.currentTimeMillis();
        double actualTime = (double)(stop-start)/1000.0;
        return actualTime;
    }
    public static double runWithoutMemCached(List<String> queries, MemcachedClient mcc) throws IOException, ParseException {
        long start = System.currentTimeMillis();
        //clearFolder(folderQuery(queries.get(1)));
        //clearFolder(queries.get(0));
        String query = prepareQuery(queries.get(queries.size()-1));
        //clearMemcached(queries);
        //prepareMemcached(queries);
        //process(prepareQuery(query), commandToken, host);
        formQuery formQuery = new formQuery(getQuery(query), getArea(query), getDate1(query), getDate2(query));
        newCached(formQuery,query, commandToken, host, mcc);
        long stop = System.currentTimeMillis();
        double actualTime = (double)(stop-start)/1000.0;
        return actualTime;
    }

    public static String getFolderResult(){
        return folderResult;
    }
    public static void clearFolder(String query) throws IOException {
        String folder = getFolderQuery(query);
        Path folderPath = Paths.get(folder);
        if (Files.exists(folderPath)) {
            //Files.deleteIfExists(folderPath);
            deleteDirectory(new File(folder));
        }
        if (Files.notExists(folderPath)){
            Files.createDirectories(folderPath);
        }
    }
    /*
    public static void deleteMCFile(formQuery formQuery){
        String folder = writeNameFile(formQuery);
        folder = folder.substring(folder.indexOf("Query"), folder.indexOf("T1"));
        String fileName = writeNameFile(formQuery);
        ////////////The exist queries
        List<String> listFile = listFileMc(folderQuery(formQuery));

    }

     */



    public static void prepareMemcached(List<String> listQuery, MemcachedClient mcc) throws java.text.ParseException, IOException {
        for (int i = 0; i < listQuery.size()-1; i++){
            process(prepareQuery(listQuery.get(i)), commandToken, host, mcc);
        }
    }
    public static void prepareMemcachedAll(List<String> listQuery, MemcachedClient mcc) throws java.text.ParseException, IOException {
        String query = "";
        for (int i = 0; i < listQuery.size(); i++){
            //process(prepareQuery(listQuery.get(i)), commandToken, host, mcc);
            query = prepareQuery(listQuery.get(i));
            formQuery formQuery = new formQuery(getQuery(query), getArea(query), getDate1(query), getDate2(query));
            newCached(formQuery,query, commandToken, host, mcc);
        }
    }
    public static void processBasicCache(String query, String tokenCommand, String host, MemcachedClient mcc) throws IOException, java.text.ParseException {
        query = query.replaceAll(" ","");
        formQuery formQuery = new formQuery(getQuery(query), getArea(query),getDate1(query), getDate2(query));
        String warpScript = contentWarpScript(query, tokenCommand);
        runQueryBasicMemcached(formQuery, warpScript, query,tokenCommand,host, mcc);
    }
    /*
    public static void process(String query, String tokenCommand, String host, MemcachedClient mcc) throws IOException, java.text.ParseException {
        //String query = "Query 1 Area A Day 10 Before 2020_03_04_14_49_24";
        query = query.replaceAll(" ","");
        formQuery formQuery = new formQuery(getQuery(query), getArea(query), getDate1(query), getDate2(query));
        //int check = checkQuery(formQuery);
        int check = checkQuery(formQuery,mcc);
        if (check == 0) {
            newCached(formQuery,query, tokenCommand, host, mcc);
        } else {
            if (check == 1) {
                oldCached(formQuery,query, mcc);
            }
            else{
                runProbeQuery(formQuery, mcc);
                runRemainQuery(formQuery, mcc);
            }
        }
    }
     */
    public static List<List<Double>> process(String query, String tokenCommand, String host, MemcachedClient mcc) throws IOException, java.text.ParseException {
        //String query = "Query 1 Area A Day 10 Before 2020_03_04_14_49_24";
        List<List<Double>> result = new ArrayList<>();
        query = query.replaceAll(" ","");
        formQuery formQuery = new formQuery(getQuery(query), getArea(query), getDate1(query), getDate2(query));
        //int check = checkQuery(formQuery);
        int check = checkQuery(formQuery,mcc);
        if (check == 0) {
            List<List<Double>> temp = newCached(formQuery,query, tokenCommand, host, mcc);
            for(int i = 0; i< temp.size(); i++){
                result.add(temp.get(i));
            }
        } else {
            if (check == 1) {
                List<List<Double>> temp = oldCached(formQuery,query, mcc);
                for(int i = 0; i< temp.size(); i++){
                    result.add(temp.get(i));
                }
            }
            else{
                List<List<Double>> tempProbe = runProbeQuery(formQuery, mcc);
                for(int i = 0; i< tempProbe.size(); i++){
                    result.add(tempProbe.get(i));
                }
                List<List<Double>> tempRemain = runRemainQuery(formQuery, mcc);
                for(int i = 0; i< tempRemain.size(); i++){
                    result.add(tempRemain.get(i));
                }


            }
        }
        return result;
    }
    /*
    public static int checkQuery(formQuery query) throws java.text.ParseException {
        //////// The current query
        String fileName = writeNameFile(query);
        ////////////The exist query
        List<String> listFile = listFileMc(folderQuery(query));
        int check = 0;
        int i = 0;
        while (i<listFile.size()){
            int test = checkQuery(query,listFile.get(i));
            if (test!=0){
                i = listFile.size();
                check = test;
            };
            //System.out.println(check);
            i++;
        }
        return check;
    }

     */
    public static int checkQuery(formQuery query, MemcachedClient mcc) throws java.text.ParseException {
        ////////////The exist query
        List<String> listFile = getAllKey(mcc);
        int check = 0;
        int i = 0;
        while (i<listFile.size()){
            int test = checkQuery(query,listFile.get(i));
            if (test!=0){
                i = listFile.size();
                check = test;
            };
            //System.out.println(check);
            i++;
        }
        return check;
    }
    public static int checkQuery(formQuery newQuery, String oldQuery) throws java.text.ParseException {
        int status = 0;
        //////// The current query
        String fileName = writeNameFile(newQuery);
        Date dateEnd = getEndDate(fileName);
        Date dateStart = getStartDate(fileName);
        //System.out.println(dateEnd);
        //System.out.println(dateStart);
        //System.out.println(fileName);
        //////// The old query
        Date endDate = getEndDate(oldQuery);
        Date startDate =getStartDate(oldQuery);
        //System.out.println(endDate);
        //System.out.println(startDate);

        if ((dateEnd.before(startDate))||
                (dateStart.after(endDate))||
                (dateEnd.compareTo(startDate)==0)||
                (dateStart.compareTo(endDate)==0)){ // new outside old
            status = 0; // no overlap, new is left or right of old
        }
        else{
            if ((dateEnd.before(endDate)||dateEnd.compareTo(endDate)==0) && (dateStart.after(startDate)||(dateStart.compareTo(startDate)==0))){
                status = 1;// new inside old
            }
            else {
                if (dateEnd.after(endDate) && dateStart.before(startDate)) {
                    status = 2; // old inside new
                }
                else {
                    if (dateStart.before(startDate) && dateEnd.before(endDate) && dateEnd.after(startDate)) {
                        status = 3; //overlap left
                    }
                    else {
                        status = 4; // overlap right
                    }
                }

            }
        }
        return status;
    }


    public static Date getEndDate(String query) throws java.text.ParseException {
        Date dateEnd = new SimpleDateFormat(formatDate)
                .parse(query.substring(query.indexOf(beginT2)).replaceAll(beginT2,""));
        return dateEnd;
    }
    public static Date getStartDate(String query) throws java.text.ParseException {
        Date dateEnd = new SimpleDateFormat(formatDate)
                .parse(query.substring(query.indexOf(beginT1),query.indexOf(endT1)).replaceAll(beginT1,""));
        return dateEnd;
    }
    public static List<List<Double>> newCached (formQuery formQuery, String query, String token, String host, MemcachedClient mcc) throws IOException {
        //createMcFile(query,token);
        //String result = "";
        String warpScript = contentWarpScript(query, token);
        //System.out.println("The system does not have the same query");
        List<List<Double>> result = runQuery(formQuery, warpScript, query, token, host, mcc);
        return result;
    }
    public static void nonCached (formQuery formQuery, String query, String token, String host) throws IOException {
        //createMcFile(query,token);
        String warpScript = contentWarpScript(query, token);
        runQueryNonMemcached(formQuery, warpScript, query, token, host);
    }

    public static void runQuery(formQuery Query, String query, String token, String host, MemcachedClient mcc) throws IOException {
        String key = getNameFile(writeNameFile(Query));
        if(mcc.get(key)==null) {
            System.out.println("There is no query cache as: " + key);
        }
        Object obj = runWarpScript(Query,token,host);
        System.out.println("A query cache of: "+ key + " is already stored in Memcached");
        mcc.set(key, 900, obj);
        //System.out.println("set status:"+mcc.set(key, 900, obj).isDone());
        List<List<Double>> ListScore = listListScore(listString(valueScore(obj)));
        //System.out.println(ListScore);
        System.out.println(ListScore.size());
    }
    public static List<List<Double>> runQuery(formQuery Query, String warpScript, String query, String token, String host, MemcachedClient mcc) throws IOException {
        String key = getNameFile(writeNameFile(Query));
        if(mcc.get(key)==null) {
            System.out.println("There is no query cache as: " + key);
        }
        String result = httpServer(warpScript);
        //Object obj = runWarpScript(Query,token,host);
        Object obj = result;
        System.out.println("A query cache of: "+ key + " is already stored in Memcached");
        mcc.set(key, 900, obj);
        //System.out.println("set status:"+mcc.set(key, 900, obj).isDone());
        List<List<Double>> ListScore = listListScore(listString(valueScore(obj)));
        //System.out.println(ListScore);
        System.out.println(ListScore.size());
        return ListScore;
    }
    public static void runQueryNonMemcached(formQuery Query, String query, String token, String host) {
        Object score = runWarpScript(Query,token,host);
        List<List<Double>> ListScore = listListScore(listString(valueScore(score)));
        //System.out.println(ListScore);
        System.out.println(ListScore.size());
    }
    public static void runQueryNonMemcached(formQuery Query, String warpScript, String query, String token, String host) throws IOException {
        String score = httpServer(warpScript);
        //Object score = runWarpScript(warpScript,token,host);
        List<List<Double>> ListScore = listListScore(listString(valueScore(score)));
        //System.out.println(ListScore);
        System.out.println(ListScore.size());
    }
    public static void runQueryBasicMemcached(formQuery Query, String warpScript, String query, String token, String host, MemcachedClient mcc) throws IOException {
        String key = getNameFile(writeNameFile(Query));
        Object score = mcc.get(key);
        String result = "";
        if(score == null) {
            System.out.println("There is no query cache as: " + key);
            //createMcFile(query,token);
            //String warpScript = contentWarpScript(query, token);
            result = httpServer(warpScript);
            //score = runWarpScript(Query,token,host);
            score = result;
            mcc.set(key, 900, score);
            //System.out.println("set status:"+mcc.set(key, 900, score).isDone());
            /*
            List<List<Double>> ListScore = listListScore(listString(valueScore(obj)));
            System.out.println(ListScore);
            System.out.println(ListScore.size());

             */
        }
        else{
            System.out.println("There is a query cache for: " + key);
            result = score.toString();
            //System.out.println("The key is stored in memcached: "+ key);

        }
        List<List<Double>> ListScore = listListScore(listString(valueScore(score)));
        //System.out.println(ListScore);
        System.out.println(ListScore.size());

    }
    public static void runQueryBasicMemcached(formQuery Query, String query, String token, String host, MemcachedClient mcc) throws IOException {
        String key = getNameFile(writeNameFile(Query));
        Object score = mcc.get(key);
        if(score == null) {
            System.out.println("There is no query cache as: " + key);
            createMcFile(query,token);
            score = runWarpScript(Query,token,host);
            mcc.set(key, 900, score);
            //System.out.println("set status:"+mcc.set(key, 900, score).isDone());
            /*
            List<List<Double>> ListScore = listListScore(listString(valueScore(obj)));
            System.out.println(ListScore);
            System.out.println(ListScore.size());

             */
        }
        else{
            System.out.println("There is a query cache for: " + key);
            //System.out.println("The key is stored in memcached: "+ key);

        }
        List<List<Double>> ListScore = listListScore(listString(valueScore(score)));
        //System.out.println(ListScore);
        System.out.println(ListScore.size());

    }

    public static String runWarpScript(formQuery query, String token, String host){
        /*
        curl -v  --data-binary "[ 'hMeJHyhK5wiHur_dsO8kccnzWLAVQO7eOfvOVhXAlmftexalPBy6cENIHwv8JDx10BSBlW0Baz.oOj1uONYYY3H5zdD_WTGePMC_cb2lRr1m_IfUgt.sZYtTdlvU0zbB' '~.*' {} NOW -1 ] FETCH" 'http://127.0.0.1:8080/api/v0/exec'
         */
        String fileName = writeNameFile(query);
        System.out.println(fileName);
        Path path = FileSystems.getDefault().getPath(".").toAbsolutePath();
        String currentPath = path.toString();
        System.out.println("The current Path is" + currentPath);
        System.out.println("The folder Query is" + folderQuery(query));
        //String link = currentPath.substring(0,currentPath.indexOf(".")) + folderQuery(query) + "/";
        String link = folderQuery(query) + "/";
        String localLink ="@"+link;
        String command = "curl --data-binary "+ localLink + fileName +host;

        System.out.println("The command is: "+command);
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", command);

        //System.out.println("Run the mc2 file with: " + command);
        //String contentFile = readFile.readMc2(link+fileName);
        //String outputName = link + fileName.replace(".mc2", ".json");
        //readFile.readNormal (processBuilder, fileName, contentFile, outputName);
        String read = readTerminal(processBuilder);
        //System.out.println(read);
        return read;
    }
    public static String httpServer(String warpScript) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(host);
        StringEntity params = new StringEntity(warpScript);
        request.addHeader("content-type", " application/x-www-form-urlencoded");
        request.setEntity(params);
        //httpClient.execute(request);
        //httpClient.close();
        HttpEntity entity = httpClient.execute(request).getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
        //System.out.println(responseString);
        return responseString;
    }
    public static String runWarpScript(String warpScript, String token, String host){
        /*
        curl -v  --data-binary "[ 'hMeJHyhK5wiHur_dsO8kccnzWLAVQO7eOfvOVhXAlmftexalPBy6cENIHwv8JDx10BSBlW0Baz.oOj1uONYYY3H5zdD_WTGePMC_cb2lRr1m_IfUgt.sZYtTdlvU0zbB' '~.*' {} NOW -1 ] FETCH" 'http://127.0.0.1:8080/api/v0/exec'
         */
        //String link = currentPath.substring(0,currentPath.indexOf(".")) + folderQuery(query) + "/";
        //String link = folderQuery(query) + "/";
        //String localLink ="@"+link;
        String command = "curl -d \""+ warpScript + "\" -X POST "+host;

        System.out.println("The command is: "+command);
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", command);

        //System.out.println("Run the mc2 file with: " + command);
        //String contentFile = readFile.readMc2(link+fileName);
        //String outputName = link + fileName.replace(".mc2", ".json");
        //readFile.readNormal (processBuilder, fileName, contentFile, outputName);
        String read = readTerminal(processBuilder);
        //System.out.println(read);
        return read;
    }

    public static List<List<Double>> oldCached(formQuery formQuery, String query, MemcachedClient mcc) throws java.text.ParseException, IOException {
        formQuery oldQuery = checkProbeQuery(formQuery,mcc);
        System.out.println("The old query is: " + writeNameFile(oldQuery));
        System.out.println("The new query is: " + writeNameFile(formQuery));
        Date startNew = getStartDate(writeNameFile(formQuery));
        Date startOld = getStartDate(writeNameFile(oldQuery));
        Date endNew = getEndDate(writeNameFile(formQuery));
        Date endOld = getEndDate(writeNameFile(oldQuery));
        long startDiff = getDateDiff(startOld,startNew, TimeUnit.MILLISECONDS);
        long endDiff = getDateDiff(endNew,endOld, TimeUnit.MILLISECONDS);
        long oldDistance = getDateDiff(startOld,endOld, TimeUnit.MILLISECONDS);
        long newDistance = getDateDiff(startNew,endNew, TimeUnit.MILLISECONDS);

        // Get value from cache
        String key = getNameFile(writeNameFile(oldQuery));
        System.out.println("The key is read: " + key);
        Object score = mcc.get(key);
        List<List<Double>> ListScore = listListScore(listString(valueScore(score)));
        List<List<Double>> newListScore = newListScore(startDiff,endDiff,oldDistance, ListScore);
        //System.out.println("The list of old score " + ListScore);
        System.out.println("The size of old score is " + ListScore.size());
        //System.out.println("The list of new score " +newListScore);
        System.out.println("The size of new score is " + newListScore.size());
        return newListScore;
    }
    public static formQuery checkProbeQuery(formQuery query, MemcachedClient mcc) throws java.text.ParseException {
        formQuery probeQuery = new formQuery("","","","");// = new ArrayList<>();
        List<String> listFile = getAllKey(mcc);// listFileMc(folderQuery(query));
        int i = 0;
        while (i<listFile.size()){
            if (checkQuery(query,listFile.get(i))==1){
                String temp = getNameFile(listFile.get(i));
                probeQuery.set_Query(getQuery(temp));
                probeQuery.set_Area(getArea(temp));
                probeQuery.set_Date1(getDate1(temp));
                probeQuery.set_Date2(getDate2(temp));
                i = listFile.size();
            };
            i++;
        }
        return probeQuery;
    }
    public static String getNameFile(String file){
        String arrayFile = "";
        if (file.contains(".")) {
            arrayFile = file.substring(file.indexOf("Query"),file.indexOf("."));
        }
        else arrayFile = file.substring(file.indexOf("Query"));
        return arrayFile;
    }
    public static List<List<Double>> newListScore(long startDiff, long endDiff, long oldDistance, List<List<Double>> ListScore){
        int begin = (int) (startDiff*ListScore.size()/oldDistance);
        int end = (int) (ListScore.size() - endDiff*ListScore.size()/oldDistance);
        List<List<Double>> newListScore = new ArrayList<>();
        for (int i = begin; i < end ; i ++)
            newListScore.add(ListScore.get(i));
        return newListScore;
    }
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
    public static List<List<Double>> runProbeQuery(formQuery query, MemcachedClient mcc) throws java.text.ParseException, IOException {
        List<List<Double>> result = new ArrayList<>();
        List<formQuery> probeQuery = getProbeQuery(query, mcc);
        System.out.println("\nRunning Probe Query -----------------------------");
        for (int i = 0; i < probeQuery.size(); i++){
            String stringQuery = writeNameFile(probeQuery.get(i)).replaceAll(".mc2","");
            System.out.println("Get the result from "+ stringQuery);
            List<List<Double>> temp = oldCached(probeQuery.get(i),stringQuery, mcc);
            for(int j = 0; j < temp.size(); j++){
                result.add(temp.get(j));
            }
        }
        return result;
    }
    public static List<formQuery> getProbeQuery(formQuery query, MemcachedClient mcc) throws java.text.ParseException {
        List<formQuery> listProbeQuery = new ArrayList<>();

        List<String> listFile = getAllKey(mcc);// listFileMc(folderQuery(query));
        int i = 0;
        while (i<listFile.size()){
            int check = checkQuery(query,listFile.get(i));
            if (check==3){
                formQuery probeQuery = new formQuery("","","","");// = new ArrayList<>();
                String temp = listFile.get(i);
                probeQuery.set_Query(query.get_Query());
                probeQuery.set_Area(query.get_Area());
                getDate1(temp);
                //getStartDate(temp);
                //getEndDate(writeNameFile(query));
                //int day = (int) (getDateDiff(getStartDate(temp),getEndDate(writeNameFile(query)), TimeUnit.DAYS));
                //System.out.println("The number of days:"+ day);
                //String Day = Integer.toString(day);
                probeQuery.set_Date1(getDate1(temp));
                probeQuery.set_Date2(query.get_Date2());
                listProbeQuery.add(probeQuery);
                System.out.println("Add probe Query:" + writeNameFile(probeQuery));
            };
            if (check==4){
                formQuery probeQuery = new formQuery("","","","");// = new ArrayList<>();
                String temp = listFile.get(i);
                probeQuery.set_Query(query.get_Query());
                probeQuery.set_Area(query.get_Area());
                getDate2(temp);
                //getStartDate(temp);
                //getEndDate(writeNameFile(query));
                //int day = (int) (getDateDiff(getStartDate(writeNameFile(query)),getEndDate(temp), TimeUnit.DAYS));
                //System.out.println("The number of days:"+ day);
                //String Day = Integer.toString(day);
                probeQuery.set_Date1(query.get_Date1());
                probeQuery.set_Date2(getDate2(temp));
                listProbeQuery.add(probeQuery);
                System.out.println("Add probe Query:" + writeNameFile(probeQuery));
            };
            if (check==2){
                formQuery probeQuery = new formQuery("","","","");// = new ArrayList<>();
                String temp = listFile.get(i);
                probeQuery.set_Query(query.get_Query());
                probeQuery.set_Area(query.get_Area());
                probeQuery.set_Date1(getDate1(listFile.get(i)));
                probeQuery.set_Date2(getDate2(listFile.get(i)));
                listProbeQuery.add(probeQuery);
                System.out.println("Add probe Query:" + writeNameFile(probeQuery));
            };
            i++;
        }
        return listProbeQuery;
    }

    public static List<List<Double>> runRemainQuery(formQuery query, MemcachedClient mcc) throws java.text.ParseException, IOException {
        List<List<Double>> result = new ArrayList<>();
        List<String> listFile = soft(getAllKey(mcc));//listFileMc(folderQuery(query)));
        List<formQuery> remainQuery = getRemainQuery(listFile,query);
        System.out.println("\nRunning a list of remainder query: ");
        for (int i = 0; i < remainQuery.size(); i++){
            System.out.println(writeNameFile(remainQuery.get(i)));
            //createMcFile(writeNameFile(remainQuery.get(i)),token);
            //newCached(remainQuery.get(i),getNameFile(writeNameFile(remainQuery.get(i))), commandToken, host, mcc);
            List<List<Double>> temp = newCached(remainQuery.get(i),getNameFile(writeNameFile(remainQuery.get(i))), commandToken, host, mcc);
            for(int j = 0; j < temp.size(); j++){
                result.add(temp.get(j));
            }
        }
        return result;
    }
    public static List<formQuery> getRemainQuery(List<String> listQuery, formQuery query) throws java.text.ParseException {
        List<formQuery> remainQuery = new ArrayList<>();
        remainQuery.add(query);
        while (listQuery.size()>0){
            int i = 0;
            while(i < listQuery.size()){
                int j = 0;
                boolean non = false;
                while (j < remainQuery.size()){
                    List<formQuery> tempQuery = repairRemainQuery(remainQuery.get(j),listQuery.get(i));
                    if(tempQuery.size()>0){
                        remainQuery.remove(remainQuery.get(j));
                        for (int k = 0; k < tempQuery.size(); k++){
                            remainQuery.add(tempQuery.get(k));
                        }
                        int temp = remainQuery.size();
                        j = temp;
                        temp = listQuery.size();
                        listQuery.remove(listQuery.get(i));
                        i = temp;
                        non = true;
                    }
                    j++;
                }
                if (!non){
                    int temp = listQuery.size();
                    listQuery.remove(listQuery.get(i));
                    i = temp;
                }
                i++;
            }
        }

        return remainQuery;
    }
    public static List<formQuery> repairRemainQuery (formQuery formQuery, String query) throws java.text.ParseException {
        List<formQuery> listQuery = new ArrayList<>();
        if (compare(formQuery,query)==2) {
            formQuery left = fixRemainQuery(formQuery,getStartDate(writeNameFile(formQuery)),getStartDate(query));
            formQuery right = fixRemainQuery(formQuery,getEndDate(query),getEndDate(writeNameFile(formQuery)));
            listQuery.add(left);
            listQuery.add(right);
        }
        if (compare(formQuery,query)==3) {
            formQuery tempQuery = fixRemainQuery(formQuery,getEndDate(query),getEndDate(writeNameFile(formQuery)));
            listQuery.add(tempQuery);
        }
        if (compare(formQuery,query)==4) {
            formQuery tempQuery = fixRemainQuery(formQuery,getStartDate(writeNameFile(formQuery)),getStartDate(query));
            listQuery.add(tempQuery);
        }
        return listQuery;
    }
    public static formQuery fixRemainQuery(formQuery formQuery, Date start, Date end) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(formatDate);//Dung edit, old is HH:mm:ss
        String startDate = dateFormat.format(start);
        String endDate = dateFormat.format(end);
        formQuery query = new formQuery(formQuery.get_Query(),formQuery.get_Area(),startDate,endDate);
        //query.set_Date1(startDate);
        //query.set_Date2(endDate);
        return query;
    }

    public static int compare (formQuery formQuery, String query) throws java.text.ParseException {
        int check = 0;
        Date dateStartOld = getStartDate(query);
        Date dateEndOld = getEndDate(query);
        Date dateStartNew = getStartDate(writeNameFile(formQuery));
        Date dateEndNew = getEndDate(writeNameFile(formQuery));

        if(dateStartOld.before(dateStartNew) && dateEndOld.after(dateStartNew) && dateEndOld.before(dateEndNew)){
            check = 3;
        }
        if(dateStartOld.after(dateStartNew) && dateEndOld.before(dateEndNew)){
            check = 2;
        }
        if(dateStartOld.after(dateStartNew) && dateStartOld.before(dateEndNew) && dateEndOld.after(dateEndNew)){
            check = 4;
        }
        return check;
    }
    public static List<String> soft(List<String> list) throws java.text.ParseException {
        List<String> temp = list;
        List<String> result = new ArrayList<>();
        while (temp.size()>0){
            String min = temp.get(0);
            for (int i = 1; i < temp.size(); i++){
                Date before = getEndDate(min);
                Date after = getEndDate(temp.get(i));
                if (before.after(after)){
                    min = temp.get(i);
                }
            }
            //System.out.println("There is min " + min);
            temp.remove(min);
            result.add(min.substring(min.indexOf("Query")));
        }
        return result;
    }
}
