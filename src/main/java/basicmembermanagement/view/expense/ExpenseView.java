package basicmembermanagement.view.expense;

import basicmembermanagement.dto.ExpenseSummation;
import basicmembermanagement.entity.Expense;
import basicmembermanagement.entity.Member;
import basicmembermanagement.enums.ExpenseType;
import basicmembermanagement.service.ExpenseService;
import basicmembermanagement.service.MemberService;
import basicmembermanagement.util.ExpenseUtil;
import basicmembermanagement.util.TurkishUtil;
import basicmembermanagement.view.LoginRequiredView;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Route(value = "expense")
//@PWA(name = "Project Base for Vaadin", shortName = "Project Base")
@Theme(value = Material.class)
public class ExpenseView extends LoginRequiredView implements HasUrlParameter<String> {

    private Grid<Expense> grid = new Grid<>(Expense.class);
    private List<Expense> allExpenses;
    private List<Expense> filteredExpense;
    private String memberId;
    private Label totalAmount = new Label();
    private Label memberInfo = new Label();
    private Member member;
    //filter
    private DatePicker startDateFilter = new DatePicker("Başlangıç Tarihi");
    private DatePicker endDateFilter = new DatePicker("Bitiş Tarihi");
    private ComboBox<ExpenseType> expenseTypeComboBox = new ComboBox<>("Harcama tipi");



    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private MemberService memberService;


    public ExpenseView(){
    }

    @PostConstruct
    public void init(){
        configureGrid();
        add(memberInfo);
        add(configureFilters());
        add(grid);
        add(totalAmount);
        setSizeFull();
    }

    private HorizontalLayout configureFilters() {
        expenseTypeComboBox.setItems(ExpenseType.values());
        expenseTypeComboBox.setItemLabelGenerator(ExpenseType::getTr);

        startDateFilter.addValueChangeListener(e -> filterExpenses());
        startDateFilter.setI18n(TurkishUtil.datePickerI18n);
        endDateFilter.addValueChangeListener(e -> filterExpenses());
        endDateFilter.setI18n(TurkishUtil.datePickerI18n);

        expenseTypeComboBox.addValueChangeListener(e -> filterExpenses());

        return new HorizontalLayout(expenseTypeComboBox, startDateFilter, endDateFilter);
    }

    private void configureGrid() {
        grid.setColumns("createDate");

        grid.addColumn(expense -> expense.getMember().getParcel()).setHeader("Parcel");

        grid.getColumnByKey("createDate")
                .setHeader("Tarih")
                .setSortable(true);


        grid.addColumn(ExpenseUtil::getAmount)
                .setKey("amount")
                .setSortable(true)
                .setHeader("Miktar");

        grid.addColumn(expense -> expense.getType().tr)
                .setSortable(true)
                .setHeader("Tip");

        grid.setHeightByRows(true);
        grid.addColumn(TemplateRenderer
                .<Expense>of("[[item.description]]")
                .withProperty("description", Expense::getDescription)
        ).setHeader("Açıklama").setFlexGrow(6);

    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        this.memberId = s;
        this.member = memberService.findById(Long.valueOf(memberId)).get();

        allExpenses = expenseService.findByMember(member);
        grid.setItems(allExpenses);
        ExpenseSummation expenseSummation = ExpenseUtil.calculateTotal(allExpenses);
        filteredExpense = new ArrayList<>(allExpenses);
        totalAmount.setText(expenseSummation.getText());
        memberInfo.setText("Üye: " + member.getInfo());
    }


    private void filterExpenses() {
        filteredExpense.clear();
        filteredExpense.addAll(allExpenses);

        if (Objects.nonNull(expenseTypeComboBox.getValue())) {
            filteredExpense = filteredExpense.stream()
                    .filter(expense -> expenseTypeComboBox.getValue().equals(expense.getType()))
                    .collect(Collectors.toList());
        }

        if (Objects.nonNull(startDateFilter.getValue())) {
            filteredExpense = filteredExpense.stream()
                    .filter(expense -> expense.getCreateDate().after(Date.valueOf(startDateFilter.getValue())))
                    .collect(Collectors.toList());
        }

        if (Objects.nonNull(endDateFilter.getValue())) {
            filteredExpense = filteredExpense.stream()
                    .filter(expense -> expense.getCreateDate().before(Date.valueOf(endDateFilter.getValue())))
                    .collect(Collectors.toList());
        }

        grid.setItems(filteredExpense);
        grid.getColumnByKey("amount").setFooter(ExpenseUtil.getTotal(this.filteredExpense));

    }

}
