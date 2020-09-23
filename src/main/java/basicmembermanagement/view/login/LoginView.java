package basicmembermanagement.view.login;

import basicmembermanagement.entity.User;
import basicmembermanagement.repository.UserRepo;
import basicmembermanagement.util.TurkishUtil;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@Route(value = "")
@PWA(name = "Üye yönetim uygulamas", shortName = "Üye Yönetim")
public class LoginView extends VerticalLayout {

    @Autowired
    private UserRepo userRepo;

    LoginForm loginForm = new LoginForm();

    public LoginView() {
        loginForm.setI18n(TurkishUtil.loginI18n);

        loginForm.addLoginListener(e -> {
            User user = authenticate(e);
            if (Objects.nonNull(user)) {
                VaadinSession.getCurrent().setAttribute(User.class, user);
                loginForm.getUI().ifPresent(ui -> ui.navigate("member"));
            } else {
                loginForm.setError(true);
            }

        });

        add(loginForm);
        setSizeFull();
    }

    private User authenticate(AbstractLogin.LoginEvent loginEvent) {
        User user = userRepo.findByUsername(loginEvent.getUsername());
        return Objects.nonNull(user) && user.getPassword().equals(loginEvent.getPassword()) ? user : null;

    }

}
