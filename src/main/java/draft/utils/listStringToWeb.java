package draft.utils;

import java.util.List;

public class listStringToWeb {
    public static String main(List<String> listString){
        String temp = "";
        for(int i = 0; i < listString.size() - 1; i++){
            temp = temp + listString.get(i) + "\n";
        }
        temp = temp + listString.get(listString.size()-1);
        return temp;
    }
}
