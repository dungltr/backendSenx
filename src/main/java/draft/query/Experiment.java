package draft.query;

import draft.utils.testTimer;
import net.spy.memcached.MemcachedClient;

import de.jonashackt.springbootvuejs.controller.BackendController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static draft.memcached.testMemcached.*;
import static draft.query.processQuery2.*;
import static draft.utils.writeFile.storeValue;
import static draft.warpscript.utils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Experiment {
    private static final Logger LOG = LoggerFactory.getLogger(Experiment.class);
    public static void main (String[] args) throws InterruptedException, ExecutionException, ParseException, IOException {
        List<String> listCache = prepareCache();
        List<String> listQuery = prepareQueries();
        /*
        String cache1 = "Query 2 Area A T1 '2020-02-01T00:00:00.0Z' T2 '2020-02-05T00:00:00.0Z'";
        String cache2 = "Query 2 Area A T1 '2020-02-07T00:00:00.0Z' T2 '2020-02-10T00:00:00.0Z'";
        String cache3 = "Query 2 Area A T1 '2020-02-12T00:00:00.0Z' T2 '2020-02-15T00:00:00.0Z'";
        String cache4 = "Query 2 Area A T1 '2020-02-20T00:00:00.0Z' T2 '2020-02-29T00:00:00.0Z'";

        String cache5 = "Query 2 Area A T1 '2020-01-25T00:00:00.0Z' T2 '2020-01-29T00:00:00.0Z'";
        String cache6 = "Query 2 Area A T1 '2020-03-02T00:00:00.0Z' T2 '2020-03-04T00:00:00.0Z'";

        listCache.add(cache1);
        listCache.add(cache2);
        listCache.add(cache3);
        listCache.add(cache4);
        listCache.add(cache5);
        listCache.add(cache6);

        String query1 = cache2;
        String query2 = "Query 2 Area A T1 '2020-02-21T00:00:00.0Z' T2 '2020-02-28T00:00:00.0Z'";
        String query3 = "Query 2 Area A T1 '2020-02-03T00:00:00.0Z' T2 '2020-02-13T00:00:00.0Z'";
        listQuery.add(query1);
        listQuery.add(query2);
        listQuery.add(query3);
         */
        testTimer timer = new testTimer();
        testCache(listCache,listQuery);
        //System.exit(0);
    }

    public static String main() throws InterruptedException, ParseException{
        List<String> listCache = new ArrayList<>();
        List<String> listQuery = new ArrayList<>();

        String cache1 = "Query 2 Area A T1 '2020-02-01T00:00:00.0Z' T2 '2020-02-05T00:00:00.0Z'";
        String cache2 = "Query 2 Area A T1 '2020-02-07T00:00:00.0Z' T2 '2020-02-10T00:00:00.0Z'";
        String cache3 = "Query 2 Area A T1 '2020-02-12T00:00:00.0Z' T2 '2020-02-15T00:00:00.0Z'";
        String cache4 = "Query 2 Area A T1 '2020-02-20T00:00:00.0Z' T2 '2020-02-29T00:00:00.0Z'";

        String cache5 = "Query 2 Area A T1 '2020-01-25T00:00:00.0Z' T2 '2020-01-29T00:00:00.0Z'";
        String cache6 = "Query 2 Area A T1 '2020-03-02T00:00:00.0Z' T2 '2020-03-04T00:00:00.0Z'";

        listCache.add(cache1);
        listCache.add(cache2);
        listCache.add(cache3);
        listCache.add(cache4);
        listCache.add(cache5);
        listCache.add(cache6);

        String query1 = cache2;
        String query2 = "Query 2 Area A T1 '2020-02-21T00:00:00.0Z' T2 '2020-02-28T00:00:00.0Z'";
        String query3 = "Query 2 Area A T1 '2020-02-03T00:00:00.0Z' T2 '2020-02-13T00:00:00.0Z'";
        listQuery.add(query1);
        listQuery.add(query2);
        listQuery.add(query3);

        testTimer timer = new testTimer();

        //System.exit(0);
        try {
            return testCache(listCache,listQuery);
        } catch (ExecutionException | IOException e) {
            e.printStackTrace();
            return "Running memcached test is not finish";
        }
    }

    public static List<String> prepareCache (){
        String cache1 = "Query 2 Area A T1 '2020-02-01T00:00:00.0Z' T2 '2020-02-05T00:00:00.0Z'";
        String cache2 = "Query 2 Area A T1 '2020-02-07T00:00:00.0Z' T2 '2020-02-10T00:00:00.0Z'";
        String cache3 = "Query 2 Area A T1 '2020-02-12T00:00:00.0Z' T2 '2020-02-15T00:00:00.0Z'";
        String cache4 = "Query 2 Area A T1 '2020-02-20T00:00:00.0Z' T2 '2020-02-29T00:00:00.0Z'";

        String cache5 = "Query 2 Area A T1 '2020-01-25T00:00:00.0Z' T2 '2020-01-29T00:00:00.0Z'";
        String cache6 = "Query 2 Area A T1 '2020-03-02T00:00:00.0Z' T2 '2020-03-04T00:00:00.0Z'";
        List<String> listCache = new ArrayList<>();
        listCache.add(cache1);
        listCache.add(cache2);
        listCache.add(cache3);
        listCache.add(cache4);
        listCache.add(cache5);
        listCache.add(cache6);
        return listCache;
    }
    public static List<String> prepareQueries (){
        List<String> listQuery = new ArrayList<>();
        String cache2 = "Query 2 Area A T1 '2020-02-07T00:00:00.0Z' T2 '2020-02-10T00:00:00.0Z'";
        String query1 = cache2;
        String query2 = "Query 2 Area A T1 '2020-02-21T00:00:00.0Z' T2 '2020-02-28T00:00:00.0Z'";
        String query3 = "Query 2 Area A T1 '2020-02-03T00:00:00.0Z' T2 '2020-02-13T00:00:00.0Z'";
        listQuery.add(query1);
        listQuery.add(query2);
        listQuery.add(query3);
        return listQuery;
    }

    public static String compareSingleQuery(String query) throws InterruptedException, IOException, ParseException, ExecutionException {
        MemcachedClient mcc = null;
        mcc = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
        System.out.println("Connection to server sucessfully");
        int sizeOfTest = 3;
        String[] nameValue = new String[sizeOfTest];
        nameValue[0] = "NonCache";
        nameValue[1] = "BasicCache";
        nameValue[2] = "SemanticCache";
        double[] time = new double[sizeOfTest];
        List<String> queries = new ArrayList<>();
        queries.add(query);
        time[0] = experimentNonCache(queries, mcc);
        Thread.sleep(1000);
        List<String> allKey = getAllKey(mcc);
        time[1] = runSingleQueryBasicCache(queries, mcc);
        List<String> allKeyNew = getAllKey(mcc);
        List<String> subKey = subListKey(allKey,allKeyNew);
        removeAllKey(subKey,mcc);
        Thread.sleep(1000);
        List<String> oldKey = getAllKey(mcc);
        time[2] = runQuerySemanticCache(queries, mcc);
        List<String> newKey = getAllKey(mcc);
        List<String> suKey = subListKey(oldKey,newKey);
        removeAllKey(suKey,mcc);
        String pushOut = "Time running in seconds\n"
                + "Directly: " + time[0] + "\n"
                + "Basic Caching: " + time[1] + "\n"
                + "Semantic Caching " + time[2] + "\n" ;
        System.out.println(pushOut);
        //storeValue(queries,time,nameValue);
        mcc.shutdown();
        return pushOut;
    }

    public static String testCache(List<String> prepareCache, List<String> queries) throws IOException, ParseException, ExecutionException, InterruptedException {
        MemcachedClient mcc = null;
        mcc = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
        System.out.println("Connection to server sucessfully");
        int sizeOfTest = 3;
        String[] nameValue = new String[sizeOfTest];
        nameValue[0] = "NonCache";
        nameValue[1] = "BasicCache";
        nameValue[2] = "SemanticCache";
        double[] time = new double[sizeOfTest];
        time[0] = experimentNonCache(queries, mcc);
        Thread.sleep(1000);
        time[1] = experimentBasicCache(prepareCache, queries, mcc);
        Thread.sleep(1000);
        time[2] = experimentSemanticCache(prepareCache,queries, mcc);
        String pushOut = "Time running in seconds\n"
                + "Directly: " + time[0] + "\n"
                + "Basic Caching: " + time[1] + "\n"
                + "Semantic Caching " + time[2] + "\n" ;
        System.out.println(pushOut);
        //storeValue(queries,time,nameValue);
        mcc.shutdown();
        return pushOut;
    }

    public static double experimentNonCache(List<String> queries, MemcachedClient mcc) throws IOException, ParseException {
        System.out.println("There is experiment of Non Cache --------------------------------------------------------");
        long start = System.currentTimeMillis();
        String[] listQuery = new String[queries.size()];
        formQuery [] listFormQuery = new formQuery[queries.size()];

        for (int i = 0; i < listQuery.length; i++){
            System.out.println("Running query "+ queries.get(i) + "+++++++++++++");
            listQuery[i] = prepareQuery(queries.get(i));
            listFormQuery[i] = new formQuery(getQuery(listQuery[i]), getArea(listQuery[i]), getDate1(listQuery[i]), getDate2(listQuery[i]));
            //nonCached(listFormQuery[i],listQuery[i], commandToken, host);
            nonCached(listFormQuery[i],listQuery[i], commandToken, host);
        }
        long stop = System.currentTimeMillis();
        double actualTime = (double)(stop-start)/1000.0;
        return actualTime;
    }

    public static double experimentBasicCache(List<String> prepareCache, List<String> queries, MemcachedClient mcc) throws IOException, ParseException, ExecutionException, InterruptedException {
        System.out.println("There is experiment of Basic Cache ------------------------------------------------------");
        //System.out.println("01. Clear folder ------------------------------------------------------");
        //clearFolder(queries.get(0));
        System.out.println("02. Clear Memcached ------------------------------------------------------");
        clearAllKey(mcc);
        //clearMemcached(prepareCache, mcc);
        //clearMemcached(queries, mcc);
        System.out.println("03. Prepare Memcached ------------------------------------------------------");
        prepareMemcachedAll(prepareCache, mcc);
        System.out.println("04. Run a series query ------------------------------------------------------");
        double time = runQueryBasicCache(queries, mcc);
        return time;
    }
    public static double experimentSemanticCache(List<String> prepareCache, List<String> queries, MemcachedClient mcc) throws IOException, ExecutionException, InterruptedException, ParseException {
        System.out.println("There is experiment of Semantic Cache ---------------------------------------------------");
        //System.out.println("01. Clear folder ------------------------------------------------------");
        //clearFolder(queries.get(0));
        System.out.println("02. Clear Memcached ------------------------------------------------------");
        clearAllKey(mcc);
        //clearMemcached(prepareCache, mcc);
        //clearMemcached(queries, mcc);
        System.out.println("03. Prepare Memcached ------------------------------------------------------");
        prepareMemcachedAll(prepareCache, mcc);
        System.out.println("04. Run a series query ------------------------------------------------------");
        double time = runQuerySemanticCache(queries, mcc);
        return time;
    }
    public static double runSingleQueryBasicCache(List<String> queries, MemcachedClient mcc) throws java.text.ParseException, IOException {
        long start = System.currentTimeMillis();
        for (int i = 0; i< queries.size(); i++){
            System.out.println("Running query "+ queries.get(i) + "+++++++++++++");
            processBasicCache(prepareQuery(queries.get(i)), commandToken, host, mcc);
        }
        long stop = System.currentTimeMillis();
        double actualTime = (double)(stop-start)/1000.0;
        return actualTime;
    }
    public static List<String> subListKey (List<String> oldKey, List<String> newKey){
        List<String> sub = new ArrayList<>();
        for (int i = 0; i < newKey.size(); i++){
            sub.add(newKey.get(i));
        }
        for(int i = 0; i < newKey.size(); i++){
            for(int j = 0; j < oldKey.size(); j++){
                if(newKey.get(i).contains(oldKey.get(j))){
                    sub.remove(newKey.get(i));
                }
            }
        }
        return sub;
    }

}

