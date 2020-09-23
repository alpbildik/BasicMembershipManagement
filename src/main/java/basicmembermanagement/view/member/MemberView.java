package basicmembermanagement.view.member;


import basicmembermanagement.dto.ExpenseSummation;
import basicmembermanagement.entity.Member;
import basicmembermanagement.service.ExpenseService;
import basicmembermanagement.service.MemberService;
import basicmembermanagement.util.ExpenseUtil;
import basicmembermanagement.util.RedirectionUtil;
import basicmembermanagement.view.LoginRequiredView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "member")
//@PWA(name = "Project Base for Vaadin", shortName = "Project Base")
@Theme(value = Material.class)
public class MemberView extends LoginRequiredView {

    //TODO: refactor filters

    @Getter
    @Autowired
    private MemberService memberService;

    @Autowired
    private ExpenseService expenseService;

    private Member selectedMember = null;
    private List<Member> allMembers;
    private MemberEditView memberForm;
    private HashMap<Member, ExpenseSummation> memberExpenses;

    //UI components
    private Grid<Member> grid = new Grid<>(Member.class);
    private TextField filterText = new TextField();
    private Button newMember = new Button("Yeni Üye Ekle");
    private Button editMember = new Button("Üye Düzenle");
    private Button viewExpenses = new Button("Harcamaları görüntüle");
    private Button addExpense = new Button("Harcama ekle");
    private Button addPayment = new Button("Ödeme ekle");



    public MemberView() {
        configureFilterText();
    }

    @PostConstruct
    public void init() {
        updateData();
        configureGrid();
        configureMemberEditForm();
        configureButtons();

        //Configure Layout
        HorizontalLayout mainContent = new HorizontalLayout(grid, memberForm);
        mainContent.setSizeFull();
        add(filterText, mainContent, new HorizontalLayout(newMember, editMember, viewExpenses, addExpense, addPayment));
        setSizeFull();
    }

    private void configureMemberEditForm() {
        memberForm = new MemberEditView(this);
        memberForm.setMember(null);
    }

    private void configureButtons() {
        newMember.addClickListener(e -> memberForm.setMember(new Member()));
        editMember.addClickListener(e -> memberForm.setMember(selectedMember));
        viewExpenses.addClickListener(e -> RedirectionUtil.redirectTo(viewExpenses, "expense/", selectedMember));
        addExpense.addClickListener(e -> RedirectionUtil.redirectTo(addExpense, "usage"));
        addPayment.addClickListener(e -> RedirectionUtil.redirectTo(addPayment, "payment/", selectedMember));
    }

    public void updateData() {
        allMembers = memberService.findAll();
        grid.setItems(memberService.findAll());

        memberExpenses = new HashMap<>();
        for (Member member: allMembers) {
            ExpenseSummation expenseSummation = ExpenseUtil.calculateTotal(expenseService.findByMember(member));
            memberExpenses.put(member, expenseSummation);
        }

    }

    private void filter() {
        List<Member> filteredList = new ArrayList<>(allMembers);

        if (!StringUtils.isEmpty(filterText.getValue())) {
            filteredList = filteredList.stream()
                    .filter(member -> member.getName().toUpperCase().contains(filterText.getValue().toUpperCase()))
                    .collect(Collectors.toList());
        }
        grid.setItems(filteredList);
    }

    private void configureFilterText() {
        filterText.setPlaceholder("İsim ara: ....");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.EAGER);
        filterText.addValueChangeListener(e -> filter());
    }

    private void configureGrid() {
        grid.setColumns("parcel");

        grid.addColumn(Member::getName)
                .setKey("name")
                .setHeader("İsim")
                .setSortable(true);

        grid.addColumn(Member::getSurname)
                .setKey("surname")
                .setHeader("Soyadı")
                .setSortable(true);

        grid.addColumn(Member::getPhone)
                .setKey("phone")
                .setHeader("Telefon")
                .setSortable(true);

        grid.addColumn(member -> memberExpenses.get(member).getTotal().toString())
                .setKey("budget")
                .setHeader("Bakiye")
                .setSortable(true)
                .setFooter(this.getTotolBugdet().toString());

        grid.asSingleSelect().addValueChangeListener(event -> selectedMember = grid.asSingleSelect().getValue());
        grid.setSizeFull();
    }


    private BigDecimal getTotolBugdet() {
        return memberExpenses.values().stream()
                .map(ExpenseSummation::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }
}

