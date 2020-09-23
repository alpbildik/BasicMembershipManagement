package basicmembermanagement.view.payment;

import basicmembermanagement.entity.Expense;
import basicmembermanagement.entity.Member;
import basicmembermanagement.service.ExpenseService;
import basicmembermanagement.service.MemberService;
import basicmembermanagement.util.ConfirmationDialog;
import basicmembermanagement.util.ExpenseUtil;
import basicmembermanagement.util.RedirectionUtil;
import basicmembermanagement.view.LoginRequiredView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Objects;


@Route(value = "payment")
//@PWA(name = "Project Base for Vaadin", shortName = "Project Base")
@Theme(value = Material.class)
public class PaymentView extends LoginRequiredView implements HasUrlParameter<String> {

    private String memberId;
    private Member member;
    private Label headerLabel = new Label("Ödeme ekleme ekranı");
    private Label memberName = new Label("Kullanıcı: ");
    private NumberField amount = new NumberField("Miktar");
    private TextField description = new TextField("Açıklama");
    private Button savePayment = new Button("Kaydet");


    @Autowired
    private MemberService memberService;

    @Autowired
    private ExpenseService expenseService;

    public PaymentView(){
    }

    @PostConstruct
    public void init(){
        add(headerLabel);
        configurePaymentForm();
        setSizeFull();
    }


    private void configurePaymentForm() {
        HorizontalLayout paymentInput = new HorizontalLayout(amount, description);
        add(memberName, paymentInput, savePayment);
        savePayment.addClickListener(e -> validate());
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        this.memberId = s;
        this.member = memberService.findById(Long.valueOf(memberId)).get();
        memberName.setText("Kullanıcı: " + member.getInfo());
    }

    private void validate() {
        ConfirmationDialog confirmDialog;
        if (Objects.isNull(member)) {
            confirmDialog = new ConfirmationDialog("Dikkat!", "Müşteri seçili değil. Lütfen ana ekrana dönünüz", e -> {});
        } else if (StringUtils.isEmpty(description.getValue())) {
            confirmDialog = new ConfirmationDialog("Dikkat!", "Açıklama alanı boş olamaz", e -> {});
        } else if (Objects.isNull(amount.getValue()) || amount.getValue().equals(0.0)) {
            confirmDialog = new ConfirmationDialog("Dikkat!", "Miktar alanı boş olamaz", e -> {});
        } else {
            confirmDialog = new ConfirmationDialog("Dikkat!", "Kayıt işlemini onaylıyor musunuz?", e -> savePayment());
        }
        confirmDialog.open();
    }

    private void savePayment(){
        Expense payment = ExpenseUtil.createPayment(member, BigDecimal.valueOf(amount.getValue()), description.getValue());
        ConfirmationDialog confirmDialog;
        if (expenseService.save(payment)) {
            confirmDialog = new ConfirmationDialog("", "İşlem başarılı", e -> RedirectionUtil.redirectTo(savePayment, "expense/", member));
        } else {
            confirmDialog = new ConfirmationDialog("", "İşlem sırasında bir hata oluştu", e -> {});
        }
        confirmDialog.open();
    }

}
