package draft.memcached;

import draft.utils.number;
import net.spy.memcached.MemcachedClient;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class testMemcached {
    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
        //test();
        //test2();
        test3();
    }
    public static MemcachedClient connectMemcached() throws IOException {
        MemcachedClient mcc = new MemcachedClient(new
                InetSocketAddress("127.0.0.1", 11211));
        System.out.println("Connection to server sucessfully");
        return mcc;
    }
    public static void test() throws IOException, ParseException {
        // Connecting to Memcached server on localhost
        MemcachedClient mcc = new MemcachedClient(new
                InetSocketAddress("127.0.0.1", 11211));
        System.out.println("Connection to server sucessfully");

        //MemcachedClient mcc = connectMemcached();
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("employees.json");
        //Read JSON file
        Object obj = jsonParser.parse(reader);

        System.out.println("set status:"+mcc.set("tutorialspoint", 900, obj).isDone());
        // Get value from cache
        System.out.println("Get from Cache:"+mcc.get("tutorialspoint"));
    }
    public static void test3() throws IOException, InterruptedException {
        MemcachedClient mcc = new MemcachedClient(new
                InetSocketAddress("127.0.0.1", 11211));
        System.out.println("Connection to server sucessfully");
        System.out.println("There are a list of key in Memcached" + mcc.getStats("items"));
        Object items = mcc.getStats("items");
        removeAllKey(getAllKey(mcc),mcc);
        mcc.shutdown();
    }

    public static List<String> getAllKey(MemcachedClient mcc){
        Object items = mcc.getStats("items");
        String itemsString = items.toString();
        String[] listItems = itemsString.split(",");
        List<Integer> codeItems = new ArrayList<>();
        List<Object> listObject = new ArrayList<>();
        for (int i = 0; i < listItems.length; i++){
            String[] list = listItems[i].split(":");
            for (int j = 0; j < list.length; j++) {
                if (number.isInteger(list[j])) {
                    if(!codeItems.contains(Integer.parseInt(list[j]))){
                        codeItems.add(Integer.parseInt(list[j]));
                        listObject.add(mcc.getStats("cachedump " + Integer.parseInt(list[j]) + " 100"));
                    }
                }
            }
        }
        List<String> key = new ArrayList<>();
        for (int i = 0 ; i < listObject.size(); i++){
            //System.out.println(listObject.get(i));
            String object = listObject.get(i).toString();
            String[] keyString = object.substring(object.indexOf("={"), object.indexOf("}}")).split(",");
            for (int j = 0; j < keyString.length; j++){
                String temp = keyString[j];
                if(temp.contains(" ")) temp = temp.replaceAll(" ", "");
                if(temp.contains("={")) temp = temp.substring(temp.indexOf("={")+2, temp.indexOf("=["));
                if(temp.contains("=[")) temp = temp.substring(0, temp.indexOf("=["));
                key.add(temp);
            }
        }
        return key;
    }
    public static void removeAllKey(List<String> key, MemcachedClient mcc) throws InterruptedException {
        for (int i = 0; i < key.size(); i++){
            mcc.delete(key.get(i));
            //while(!mcc.delete(key.get(i)).isDone()){
            //    Thread.sleep(1000);
            //}
            //System.out.println("The key " + key.get(i) + " is deleted:" + mcc.delete(key.get(i)).isDone());
        }
        List<String> testKey = getAllKey(mcc);
        System.out.println("The number of keys are still on Memcached " + testKey.size());
    }
    public static void clearAllKey( MemcachedClient mcc) throws InterruptedException {
        removeAllKey(getAllKey(mcc),mcc);
    }
    public static String valueScore(Object score){
        String scoreString = score.toString();
        scoreString = scoreString.substring(scoreString.indexOf("[["),scoreString.indexOf("]]"));
        return scoreString;
    }
    public static String valueScore(String score){
        String scoreString = score;
        scoreString = scoreString.substring(scoreString.indexOf("[["),scoreString.indexOf("]]"));
        return scoreString;
    }
    public static List<String> listString(String valueScore){
        String[] arrayScore = valueScore.split(",");
        int lengthScore = valueScore.split(",").length;
        List<String> listScore =  new ArrayList<>();
        for(int i = 0; i < lengthScore; i++){
            listScore.add(arrayScore[i].replace("[","").replace("]","").replace(" ",""));
        }
        return listScore;
    }
    public static List<List<Double>> listListScore (List<String> listScore){
        List<List<Double>> list = new ArrayList<>();
        for(int i = 0; i < listScore.size()/2; i++) {
            List<Double> smallList = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                smallList.add(Double.parseDouble(listScore.get(i*2+j)));
            }
        list.add(smallList);
        }
        return list;
    }
    public static void test2() throws IOException, ParseException{
        MemcachedClient mcc = connectMemcached();
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("score.json");
        //Read JSON file
        Object obj = jsonParser.parse(reader);

        System.out.println("set status:"+mcc.set("score", 900, obj).isDone());
        // Get value from cache
        System.out.println("Get from Cache:"+mcc.get("score"));
        Object score = mcc.get("score");

        //System.out.println(valueScore(score));
        List<String> listScore = listString(valueScore(score));
        List<List<Double>> ListScore = listListScore(listScore);
        System.out.println(ListScore);
    }

}
