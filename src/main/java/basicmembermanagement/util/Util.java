package basicmembermanagement.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {



    public static String toString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/YYYY");
        return formatter.format(date);
    }


    public static String createDescription(Date date, String type, Double lastUse, Double newUse, Double unitPrice) {
        StringBuilder sb = new StringBuilder();
        sb.append("Tarih: " + toString(date) + "\n");
        sb.append("Tip: "   + type + "\n");
        sb.append("ilk indeks: " + lastUse.toString() + "\n");
        sb.append("Son index: " + newUse + "\n");
        sb.append("Birim fiyat: " + unitPrice + "\n");
        return sb.toString();
    }

    public static String createDescription(Date date, String type,  BigDecimal amount) {
        StringBuilder sb = new StringBuilder();
        sb.append("Tarih: " + toString(date) + "\n");
        sb.append("Tip: "   + type + "\n");
        sb.append("Birim fiyat: " + amount + "\n");
        return sb.toString();
    }


}
