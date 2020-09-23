package basicmembermanagement.view.usage;

import basicmembermanagement.dto.UsageDTO;
import basicmembermanagement.entity.Member;
import basicmembermanagement.enums.ExpenseType;
import basicmembermanagement.enums.UsageType;
import basicmembermanagement.exception.ValidationException;
import basicmembermanagement.service.ExpenseService;
import basicmembermanagement.service.UsageService;
import basicmembermanagement.util.ConfirmationDialog;
import basicmembermanagement.util.RedirectionUtil;
import basicmembermanagement.util.UsageValidationUtil;
import basicmembermanagement.util.Util;
import basicmembermanagement.view.LoginRequiredView;
import basicmembermanagement.view.member.MemberView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Route(value = "usage")
@Theme(value = Material.class)
public class UsageView  extends LoginRequiredView {

    @Autowired
    private UsageService usageService;

    @Autowired
    private ExpenseService expenseService;

    Map<UsageType, List<UsageDTO>> usages = new HashMap<>();
    Binder<UsageDTO> binder = new Binder<>(UsageDTO.class);
    List<UsageDTO> usageByType;
    //UI components
    ComboBox<UsageType> usageTypeComboBox = new ComboBox<>("Kullanım Tipi:");
    Grid<UsageDTO> grid = new Grid<>(UsageDTO.class);
    boolean isFirstInput = false;
    NumberField unitAmount = new NumberField("Birim kullanım ücreti");
    TextField descriptionField = new TextField("Açıklama");


    public UsageView() {
    }

    @PostConstruct
    public void init(){
        for (UsageType usageType: UsageType.values()) {
            usages.put(usageType, usageService.findLastUsages(usageType));
        }

        Label welcome = new Label("Fatura girme ekranı");

        configureGrid();
        HorizontalLayout header = configureHeader();
        HorizontalLayout saveButtons = configureButtons();
        add(welcome, header, grid, saveButtons);
        setSizeFull();
    }

    private HorizontalLayout configureHeader(){
        usageTypeComboBox.setItems(UsageType.values());
        usageTypeComboBox.addValueChangeListener(e -> updateGrid());
        usageTypeComboBox.setItemLabelGenerator(UsageType::getTr);

        return new HorizontalLayout(usageTypeComboBox, unitAmount, descriptionField);

    }

    private HorizontalLayout configureButtons(){
        Button save = new Button("Kaydet");
        save.addClickListener(e -> validateAndSave());
        return new HorizontalLayout(save);
    }

    private void validateAndSave(){
        try {
            List<UsageDTO> validatedUsages = validate();
            ConfirmationDialog confirmDialog;
            if (usageTypeComboBox.getValue().isIndividual && usageTypeComboBox.getValue().isAllNecessary) {
                confirmDialog = new ConfirmationDialog(
                        "Dikkat!",
                        "Kayıt işlemini onaylıyor musunuz? \n toplamTutar: " + getTotalAmount(validatedUsages),
                        e -> saveUsages(validatedUsages)
                );
            } else {
                confirmDialog = new ConfirmationDialog(
                        "Dikkat!",
                        "Kayıt işlemini onaylıyor musunuz? \n toplamTutar: " + getTotalAmount(validatedUsages),
                        e -> createExpense()
                );
            }
            confirmDialog.open();
        } catch (ValidationException ex) {
            ConfirmationDialog confirmDialog = new ConfirmationDialog("Dikkat!", ex.getMessage(), e -> {});
            confirmDialog.open();
        } catch (Exception ex) {
            ConfirmationDialog confirmDialog = new ConfirmationDialog("Hata!", ex.getMessage(), e -> {});
            confirmDialog.open();
        }
    }


    private void configureGrid(){
        //grid.setItems(usages.get(UsageType.WATER));

        grid.setColumns("memberName");

        grid.getColumnByKey("memberName").setHeader("Üye Adı");

        grid.addColumn(usageDTO -> usageDTO.getMember().getParcel()).setHeader("Parsel");

        grid.addColumn(usageDTO -> usageDTO.getUsageType().getTr())
                .setHeader("Kullanım tipi");

        Grid.Column<UsageDTO> lastUsageColumn = grid.addColumn(UsageDTO::getLastUsageRecord)
                .setKey("lastUsageRecord")
                .setHeader("İlk endeks");

        Grid.Column<UsageDTO> newUsageColumn = grid.addColumn(UsageDTO::getNewUsageRecord)
                .setKey("newUsageRecord")
                .setHeader("Son endeks");

        grid.setSizeFull();


        Editor<UsageDTO> editor = grid.getEditor();
        editor.setBinder(binder);
        //editor.setBuffered(true);

        //new Usage editor
        NumberField newUsageField = new NumberField();
        newUsageField.getElement()
                .addEventListener("keydown", event -> grid.getEditor().cancel())
                .setFilter("event.key === 'Tab' && event.shiftKey");

        binder.bind(newUsageField, "newUsageRecord");
        newUsageColumn.setEditorComponent(newUsageField);

        //last Usage editor
        NumberField lastUsageField = new NumberField();
        lastUsageField.getElement()
                .addEventListener("keydown", event -> grid.getEditor().cancel())
                .setFilter("event.key === 'Tab' && event.shiftKey");


        binder.bind(lastUsageField, "lastUsageRecord");
        lastUsageColumn.setEditorComponent(lastUsageField);


        grid.addItemDoubleClickListener(event -> {
            grid.getEditor().editItem(event.getItem());
            newUsageField.focus();
        });

        binder.addValueChangeListener(event -> {
            grid.getEditor().refresh();
        });

        grid.getEditor().addCloseListener(event -> {
            grid.getEditor().save();
        });
    }


    private void updateGrid(){
        usageByType = usages.get(usageTypeComboBox.getValue());
        grid.setItems(usageByType);
        isFirstInput = usageByType.stream()
                .anyMatch(usageDTO -> Objects.isNull(usageDTO.getLastUsageRecord()));

        if (binder.getBinding("lastUsageRecord").isPresent()) {
            binder.getBinding("lastUsageRecord").get().setReadOnly(!isFirstInput);
        }

        if (UsageType.OTHER.equals(usageTypeComboBox.getValue())) {
            usageByType.forEach(
                    usageDTO -> usageDTO.setLastUsageRecord(0.0)
            );

            unitAmount.setValue(1.0);
        } else {
            unitAmount.clear();
        }

    }


    private List<UsageDTO> validate() throws ValidationException {
        return UsageValidationUtil.validate(usageTypeComboBox.getValue(),
                usages.get(usageTypeComboBox.getValue()),
                unitAmount.getValue(),
                descriptionField.getValue(),
                isFirstInput);
    }

    private void createExpense() {
        if (usageTypeComboBox.getValue().isAllNecessary) {
            expenseService.createCommonExpense(BigDecimal.valueOf(unitAmount.getValue()), ExpenseType.findFromUsageType(usageTypeComboBox.getValue()), descriptionField.getValue());
        } else {
            expenseService.createExpense(usages.get(usageTypeComboBox.getValue()), BigDecimal.valueOf(unitAmount.getValue()), ExpenseType.findFromUsageType(usageTypeComboBox.getValue()), descriptionField.getValue());
        }

        ConfirmationDialog confirmDialog = new ConfirmationDialog("", "İşlem başarılı", e -> {
            UI.getCurrent().navigate(MemberView.class);
        });
        confirmDialog.open();
    }

    private void saveUsages(List<UsageDTO> usages) {
        usageService.saveUsages(usages, unitAmount.getValue(), isFirstInput);
        ConfirmationDialog confirmDialog = new ConfirmationDialog("", "İşlem başarılı", e -> {
            UI.getCurrent().navigate(MemberView.class);
        });
        confirmDialog.open();
    }

    private Double getTotalAmount(List<UsageDTO> usageDTOS){
        if (!usageTypeComboBox.getValue().isIndividual) {
            return unitAmount.getValue() * usageByType.size();
        } else {
            return usageDTOS.stream()
                    .filter(usageDTO -> Objects.nonNull(usageDTO.getNewUsageRecord()))
                    .mapToDouble(usageDTO -> unitAmount.getValue() * (usageDTO.getLastUsageRecord() - usageDTO.getNewUsageRecord()))
                    .reduce(0.0, Double::sum);
        }
    }
}
