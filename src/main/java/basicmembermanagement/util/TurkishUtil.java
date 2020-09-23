package basicmembermanagement.util;

import com.sun.tools.javac.util.List;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.login.LoginI18n;

public class TurkishUtil {

    public static DatePicker.DatePickerI18n datePickerI18n;
    public static LoginI18n loginI18n;


    static {
        datePickerI18n = turkishDatePicker();
        loginI18n = turkishLoginI18n();
    }

    private static LoginI18n turkishLoginI18n(){
        LoginI18n loginI18n = new LoginI18n();

        LoginI18n.ErrorMessage errorMessage = new LoginI18n.ErrorMessage();
        errorMessage.setMessage("Girdiğiniz kullanıcı adı ya da şifre hatalı");
        errorMessage.setTitle("Yanlış kullanıcı adı ya da şifre");
        loginI18n.setErrorMessage(errorMessage);

        LoginI18n.Header header = new LoginI18n.Header();
        header.setTitle("Giriş");
        loginI18n.setHeader(header);

        LoginI18n.Form form = new LoginI18n.Form();
        form.setForgotPassword("");
        form.setPassword("Şifre");
        form.setUsername("Kullanıcı adı");
        form.setTitle("Giriş");
        form.setSubmit("Giriş Yap");

        loginI18n.setForm(form);

        return loginI18n;
    }

    private static DatePicker.DatePickerI18n turkishDatePicker() {
        DatePicker.DatePickerI18n datePickerI18n = new DatePicker.DatePickerI18n();
        datePickerI18n.setCalendar("Takvim");
        datePickerI18n.setCancel("İptal");
        datePickerI18n.setClear("Temizle");
        datePickerI18n.setFirstDayOfWeek(1);
        datePickerI18n.setMonthNames(List.of("Ocak" , "Şubat", "Mart", "Nisan", "Mayıs", "Haziran", "Eylül", "Ekim", "Kasım", "Aralık"));
        datePickerI18n.setToday("Bugün");
        datePickerI18n.setWeek("Hafta");
        datePickerI18n.setWeekdays(List.of("Pazartesi", "Salı", "Çarşamba", "Perşembe", "Cuma", "Cumartesi", "Pazar"));
        datePickerI18n.setWeekdaysShort(List.of("Pt", "Sl", "Çarş", "Perş", "Cm", "Cmt", "Pz"));
        return datePickerI18n;
    }
}
