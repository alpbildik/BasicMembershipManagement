package basicmembermanagement.view.member;

import basicmembermanagement.entity.Member;
import basicmembermanagement.service.MemberService;
import basicmembermanagement.util.ConfirmationDialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class MemberEditView extends FormLayout {

    //Config
    Binder<Member> binder = new Binder<>(Member.class);
    private MemberView memberView;
    private MemberService memberService;

    //UI fields
    private TextField name = new TextField("Adı");
    private TextField surname = new TextField("Soyadı");
    private TextField parcel = new TextField("Parsel");
    private TextField phone = new TextField("Telefon");
    private Button save = new Button("Ekle");
    private Button delete = new Button("Sil");



    public MemberEditView(MemberView memberView) {
        this.memberView = memberView;
        this.memberService = memberView.getMemberService();
        binder.bindInstanceFields(this);
        add(name, surname, parcel, phone, new HorizontalLayout(save,delete));
        configureSaveButton();
        configureDeleteButton();
    }


    public void setMember(Member member) {
        if (member == null) {
            setVisible(false);
        }
        else if (member.equals(binder.getBean())) {
            setVisible(false);
        } else {
            setVisible(true);
            name.focus();
        }
        binder.setBean(member);
    }

    public void save(){
        Member member = binder.getBean();
        memberService.save(member);
        memberView.updateData();
        setMember(null);
    }

    public void showDeleteDialog(){
        ConfirmationDialog confirmationDialog = new ConfirmationDialog("Dikkat", "Kullanıcıyı silmek istediğinize emin misiniz?", e -> delete());
        confirmationDialog.open();
    }

    public void delete() {
        Member member = binder.getBean();
        memberService.delete(member);
        memberView.updateData();
        setMember(null);
    }

    private void configureSaveButton(){
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(e -> save());
    }
    private void configureDeleteButton(){
        delete.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        delete.addClickListener(e -> showDeleteDialog());
    }
}
