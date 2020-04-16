package de.jonashackt.springbootvuejs.controller;

import draft.query.formQuery;
import draft.query.processQuery2;
import net.spy.memcached.MemcachedClient;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.List;

import static draft.memcached.testMemcached.clearAllKey;
import static draft.memcached.testMemcached.getAllKey;
import static draft.query.processQuery2.getNameFile;
import static draft.warpscript.utils.writeNameFile;

public class query {
    static String token = "YevxHKGF6zmOOfTvxnP_76v1n_i3dAPm6DAyWCgKE5q5mfCc6ZHGuT5i40atfkit8YgqRBLBZ3EfPZIdLJMG5ck7sGQYUXy07gDelvmoM6BNKR_SaCrynV";
    static String commandToken = "'"+token+"' 'token' STORE";
    static String host = "http://145.239.64.49:8080/api/v0/exec";
    public static List<List<Double>> runQuery(String kindQuery, String kindArea, String from, String to) throws IOException, ParseException {
        //String result = "";
        MemcachedClient mcc = null;
        mcc = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
        System.out.println("Connection to server sucessfully");
        formQuery FromQuery = new formQuery(kindQuery,kindArea,from,to);
        String query = getNameFile(writeNameFile(FromQuery));
        List<List<Double>> result = draft.query.processQuery2.process(query, commandToken, host, mcc);
        mcc.shutdown();
        return result;

    }
    public static String clearMemcached () throws InterruptedException, IOException {
        MemcachedClient mcc = null;
        mcc = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
        clearAllKey(mcc);
        mcc.shutdown();
        return "The memcached is cleared already";
    }
    public static List<String> readAllMemcached () throws InterruptedException, IOException {
        MemcachedClient mcc = null;
        mcc = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
        List<String> listAllKey = getAllKey(mcc);
        mcc.shutdown();
        return listAllKey;
    }

    public static String testJson() throws IOException, org.json.simple.parser.ParseException {
        String hostJson = "https://api.coindesk.com/v1/bpi/currentprice.json";
        String result = getUrlContents(hostJson);
        System.out.println(result);
        return result;
    }
    private static String getUrlContents(String theUrl)
    {
        StringBuilder content = new StringBuilder();

        // many of these calls can throw exceptions, so i've just
        // wrapped them all in one try/catch statement.
        try
        {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return content.toString();
    }
}
