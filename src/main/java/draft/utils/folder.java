package draft.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class folder {
    public static boolean deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDirectory(children[i]);
                if (!success) {
                    return false;
                }
            }
        } // either file or an empty directory
        System.out.println("removing file : " + dir.getName());
        return dir.delete();
    }
    public static List<String> listFileMc(String folder){
        List<String> list = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(Paths.get(folder))) {

            List<String> result = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());

            //result.forEach(System.out::println);
            for(int i =0 ; i < result.size(); i ++){
                String[] split = result.get(i).split("/");
                String temp = split[split.length-1];
                if (temp.contains(".mc2")) temp = temp.replaceAll(".mc2","");
                list.add(temp);
            }
            return list;

        } catch (IOException e) {
            e.printStackTrace();
            return list;
        }
    }
}
