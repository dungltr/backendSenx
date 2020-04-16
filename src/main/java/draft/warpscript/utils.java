package draft.warpscript;

import draft.query.formQuery;
import draft.query.formQuery2;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static draft.utils.writeFile.createfile;

public class utils {
    //@LocalServerPort
    static int port = 8098;
    static String token = "YevxHKGF6zmOOfTvxnP_76v1n_i3dAPm6DAyWCgKE5q5mfCc6ZHGuT5i40atfkit8YgqRBLBZ3EfPZIdLJMG5ck7sGQYUXy07gDelvmoM6BNKR_SaCrynV";
    static String commandToken = "'"+token+"' 'token' STORE";
    static String host = "http://145.239.64.49:8080/api/v0/exec";
    //static String folderMC2 = System.getProperty("user.dir") + "/" + "mc2";
    static String http = "http:"+"/"+"/localhost";
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
    public static String generateWarpScriptServer(formQuery2 Query, String token){
        String warpScript = token + "\n" +
                "<'\n" +
                generateArea(Query.get_Area()) + "\n" +
                "'>  \n" +
                "'geoJson' STORE \n" +
                "// get from date "+ Query.get_Date1() + "to date " + Query.get_Date2() + " in this area \n" +
                "$geoJson " + scriptDate(Query.get_Date1()) + " " + scriptDate(Query.get_Date2()) +" @org.thinkcities/getFromArea 'gts' STORE \n" +
                "// extract status\n" +
                "$gts 0 @org.thinkcities/extractValue 'gts' STORE \n" +
                "// compute histogram and score\n" +
                "$gts @org.thinkcities/histogram2 'gts' STORE\n" +
                "// compute score \n" +
                "[ $gts <%\n" +
                "  'data' STORE\n" +
                "  $data 7 GET 0 GET 'v' STORE\n" +
                "  <% $v 90 >= %> <% 5 %>\n" +
                "  <% [ $v 70 >= $v 90 < ] &&  %> <% 4 %>\n" +
                "  <% [ $v 50 >= $v 70 < ] &&  %> <% 3 %>\n" +
                "  <% [ $v 0 >= $v 50 < ] &&  %> <% 2 %>\n" +
                "  <% [ $v -20 >= $v 0 < ] &&  %> <% 1 %>\n" +
                "  <% 0 %> 5 SWITCH 'score' STORE\n" +
                "  [ $data 0 GET NaN NaN NaN $score ]\n" +
                "%> MACROMAPPER 0 0 0 ] MAP 0 GET 'gtsScore' STORE\n" +
                "$gtsScore";
        return warpScript;
    }
    public static String generateArea(String Area){
        String temp = "";
        String area = Area;
        switch(area) {
            case "A": {
                temp = "{\n" +
                        "    \"type\": \"Polygon\",\n" +
                        "    \"coordinates\": [\n" +
                        "        [\n" +
                        "            [-1.6423702239990234,48.08266633622704],\n" +
                        "            [-1.6295814514160156,48.089833318436966],\n" +
                        "            [-1.6256332397460938,48.09006264537752],\n" +
                        "            [-1.6239166259765625,48.09183989449307],\n" +
                        "            [-1.6243457794189453,48.09258517422977],\n" +
                        "            [-1.6112995147705078,48.0999227364189],\n" +
                        "            [-1.6060638427734375,48.10255941705171],\n" +
                        "            [-1.6077804565429688,48.1041069709598],\n" +
                        "            [-1.6282081604003906,48.092814488898725],\n" +
                        "            [-1.6424560546875,48.084845204471684],\n" +
                        "            [-1.6462326049804688,48.083985135927],\n" +
                        "            [-1.6477775573730469,48.081232819942365],\n" +
                        "            [-1.6421127319335938,48.08134750271524],\n" +
                        "            [-1.6423702239990234,48.08266633622704]\n" +
                        "        ]\n" +
                        "    ]\n" +
                        "}";
            }
            break;
            default:
                temp = "";
                break;
        }
        return temp;
    }
    public static String scriptDate(String date){
        String begin = "'";
        String end = ".0Z'";
        String[] time = date.split(spaceFormatDate);
        String middle = "T";
        String prepare = begin + time[0] + "-" + time[1] + "-" + time[2] + middle + time[3] + ":" + time[4] + ":" + time[5] + end;
        return prepare;
    }
    public static void generateFileMc(formQuery2 formQuery, String token){
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));
        String warpScript = generateWarpScriptServer(formQuery, token);
        String nameFile = writeNameFile(formQuery);
        System.out.println("Here is file name "+nameFile);
        createfile(folderQuery(formQuery),nameFile, warpScript);
    }
    public static String generateContent(formQuery2 formQuery, String token){
        String warpScript = generateWarpScriptServer(formQuery, token);
        //String nameFile = writeNameFile(formQuery);
        //System.out.println("The content of warpScript is \n :" + warpScript);
        return warpScript;
        //createfile(folderQuery(formQuery),nameFile, warpScript);
    }
    public static String writeNameFile(formQuery2 query){
        return beginQuery + query.get_Query() + beginArea + query.get_Area() + beginT1 + query.get_Date1() + beginT2 + query.get_Date2() + ".mc2";
    }
    public static String writeNameFile(formQuery query){
        String nameFile = "";
        switch (query.get_Query()){
            case "1":
                nameFile = "Query" + query.get_Query() + "Area" + query.get_Area() + "Day" + query.get_Day() + "Before" + query.get_Date2() + ".mc2";
                break;
            case "2":
                nameFile =  beginQuery + query.get_Query() + beginArea + query.get_Area() + beginT1 + query.get_Date1() + beginT2 + query.get_Date2() + ".mc2";
                break;
            //default:
            //    nameFile = beginQuery + query.get_Query() + beginArea + query.get_Area() + beginT1 + query.get_Date1() + beginT2 + query.get_Date2() + ".mc2";
        }
        return nameFile;
    }
    public static String folderQuery(formQuery2 formQuery) {
        String folder = folderMC2 + "/" + beginQuery + formQuery.get_Query() + beginArea + formQuery.get_Area();
        Path folderPath = Paths.get(folder);
        if (!Files.exists(folderPath)) {
            try {
                Files.createDirectory(folderPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return folder;
    }
    public static String folderQuery(formQuery formQuery) {
        String folder = folderMC2 + "/" + beginQuery + formQuery.get_Query() + beginArea + formQuery.get_Area();
        Path folderPath = Paths.get(folder);
        if (!Files.exists(folderPath)) {
            try {
                Files.createDirectory(folderPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return folder;
    }
    public static void createMcFile(String query, String token){
        String Query = getQuery(query);
        String Area = getArea(query);
        String Date1 = getDate1(query);
        String Date2 = getDate2(query);
        formQuery2 formQuery = new formQuery2(Query, Area, Date1, Date2);
        generateFileMc(formQuery, token);
    }

    public static String contentWarpScript(String query, String token){
        String content = "";
        String Query = getQuery(query);
        String Area = getArea(query);
        String Date1 = getDate1(query);
        String Date2 = getDate2(query);
        formQuery2 formQuery = new formQuery2(Query, Area, Date1, Date2);
        content = generateContent(formQuery, token);
        return content;
    }

    public static String getQuery(String query){
        String temp = query.substring(query.indexOf(beginQuery),query.indexOf(endQuery));
        temp = temp.replaceAll(beginQuery,"");
        return temp;
    }
    public static String getArea(String query){
        String temp = query.substring(query.indexOf(beginArea),query.indexOf(endArea));
        temp = temp.replaceAll(beginArea,"");
        return temp;
    }
    public static String getDate1(String query){
        String temp = query.substring(query.indexOf(beginT1),query.indexOf(endT1));
        temp = temp.replaceAll(beginT1,"").replaceAll(dateSpaceTime,spaceFormatDate);
        return temp;
    }
    public static String getDate2(String query){
        String temp = query.substring(query.indexOf(beginT2));
        temp = temp.replaceAll(beginT2,"").replaceAll(dateSpaceTime,spaceFormatDate);
        return temp;
    }
    public static String getFolderQuery(String Query){
        String query = prepareQuery(Query);
        query = query.substring(query.indexOf("Query"), query.indexOf("T1"));
        return folderMC2 + "/" + query;
    }
    public static String prepareQuery(String query){
        return query.replaceAll("'","").replaceAll(".0Z","").replaceAll(":","_").replaceAll("-","_").replaceAll(" ",""); // Query2AreaAT12020_01_01T00_00_00T22020_01_02T00_00_00
    }
}
