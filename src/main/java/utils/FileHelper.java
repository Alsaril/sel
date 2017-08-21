package utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.ProductsData;

import java.io.*;

/**
 * Created by andrey on 24.07.17.
 */
public class FileHelper {

    public static void saveToken(String token){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        File file = new File("token.txt");

        try {
            if(!file.exists()){
                file.createNewFile();
            }
            PrintWriter out = new PrintWriter(file.getAbsoluteFile());
            try {
                out.print(token);
            } finally {
                out.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readToken() throws FileNotFoundException {
        String fileName = "token.txt";
        //Этот спец. объект для построения строки
        StringBuilder sb = new StringBuilder();
        File file = new File(fileName);

        exists(fileName);

        try {
            //Объект для чтения файла в буфер
            BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            try {
                //В цикле построчно считываем файл
                String s;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                }
            } finally {
                //Также не забываем закрыть файл
                in.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        //Возвращаем полученный текст с файла
        return sb.toString();
    }

    private static void exists(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        if (!file.exists()){
            throw new FileNotFoundException(file.getName());
        }
    }
}
