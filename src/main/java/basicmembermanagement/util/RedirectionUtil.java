package basicmembermanagement.util;

import basicmembermanagement.entity.Member;
import com.vaadin.flow.component.button.Button;

import java.util.Objects;

public class RedirectionUtil {

    public static void redirectTo(Button button, String url) {
        button.getUI().ifPresent(ui ->
                ui.navigate(url));
    }

    public static void redirectTo(Button button, String url, Member member) {
        if (Objects.isNull(member)) return;

        button.getUI().ifPresent(ui ->
                ui.navigate(url + member.getId().toString()));
    }

}
