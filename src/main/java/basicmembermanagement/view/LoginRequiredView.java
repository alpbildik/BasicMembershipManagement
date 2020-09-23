package basicmembermanagement.view;

import basicmembermanagement.entity.User;
import basicmembermanagement.view.login.LoginView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.server.VaadinSession;

import java.util.Objects;

public class LoginRequiredView extends VerticalLayout implements BeforeEnterObserver {

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (Objects.isNull(VaadinSession.getCurrent().getAttribute(User.class))) {
            beforeEnterEvent.rerouteTo(LoginView.class);
        }
    }
}
