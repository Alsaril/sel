package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by andrey on 14.08.17.
 */
public class Utils {
    static SimpleDateFormat backendDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    static SimpleDateFormat normalDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public static String formatDate(String date){
        String formatDate;
        try {
            formatDate = normalDateFormat.format(backendDateFormat.parse(date));
        } catch (ParseException e) {
            formatDate = null;
            e.printStackTrace();
        }
        return formatDate;
    }

    public static void print(){

        //Printer.INSTANCE.printCheck(1,"4545454", new Date().toString(),25,"25:25,");
    }
}
