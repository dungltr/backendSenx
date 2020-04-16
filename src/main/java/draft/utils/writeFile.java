package draft.utils;

import draft.query.formQuery2;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static draft.query.processQuery2.getFolderResult;
import static draft.warpscript.utils.*;

public class writeFile {
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
	public static void writeOut (String link, StringBuilder output){
    	String Text = output.toString();
    	Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(link), "utf-8"));
            writer.write(Text);
        }
        catch (IOException ex) {
            // report
        }
        finally {
            try {writer.close();}
            catch (Exception ex) {/*ignore*/}
        }
    }
	public static void writeString (String link, String content){
    	String Text = content;
    	Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(link), "utf-8"));
            writer.write(Text);
        }
        catch (IOException ex) {
            // report
        }
        finally {
            try {writer.close();}
            catch (Exception ex) {/*ignore*/}
        }
    }

    public static void createfile(String OperatorFolder, String filename, String content){
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(OperatorFolder+"/"+filename), "utf-8"));
            writer.write(content);
        }
        catch (IOException ex) {
            // report
        }
        finally {
            try {writer.close();}
            catch (Exception ex) {/*ignore*/}
        }
    }

    public static void storeValue(List<String> listQuery, double[] Value, String[] Name) throws IOException {
        String query = prepareQuery(listQuery.get(listQuery.size()-1));
        formQuery2 formQuery = new formQuery2(getQuery(query), getArea(query), getDate1(query), getDate2(query));
        String folder = getFolderResult();//getFolderQuery(query);
        Path folderPath = Paths.get(folder);
        if (Files.notExists(folderPath)){
            Files.createDirectories(folderPath);
        }
        String nameFile = writeNameFile(formQuery);
        if (nameFile.contains("mc2")) {
            nameFile = nameFile.replaceAll("mc2","csv");
        }
        String fileName = folder + "/" + nameFile;
        double[][] a = new double[1][Value.length];
        for (int i = 0; i < Value.length; i++)
            a[0][i] = Value[i];
        Path filePath = Paths.get(fileName);
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
            String FILE_HEADER = "";
            for (int i= 0; i < Value.length-1; i++)
                FILE_HEADER = FILE_HEADER + Name[i] + COMMA_DELIMITER;
            int last = Value.length-1;
            FILE_HEADER = FILE_HEADER + Name[last];
            Writematrix2CSV(a, fileName, FILE_HEADER);
        }
        else {
            String add = "";
            int i = 0;
            for (i = 0; a[0].length - 1 > i; i++)
                add = add + a[0][i] + COMMA_DELIMITER;
            if (a[0].length - 1 == i)
                add = add + a[0][i] + NEW_LINE_SEPARATOR;
            Files.write(filePath, add.getBytes(), StandardOpenOption.APPEND);
        }
    }
    public static void Writematrix2CSV(double[][] a, String fileName, String FILE_HEADER){
        int m = a.length;
        int n = a[0].length;
        int i = 0;
        String tmp = "";
        double[] b = null;
        List arrays = new ArrayList();
        for (i = 0; m >i; i++)
            arrays.add(a[i]);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);
            //Write the CSV file header
            fileWriter.append(FILE_HEADER.toString());
            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
            for (Iterator it = arrays.iterator(); it.hasNext();) {
                double[] array = (double[]) it.next();
                tmp = Arrays.toString(array);
                tmp = tmp.replace("[","");
                tmp = tmp.replace("]","");
                tmp = tmp.replace(" ","");
                fileWriter.append(tmp);
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }
    }
}
