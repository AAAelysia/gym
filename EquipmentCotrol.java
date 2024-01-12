package Equipment_;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 控制器类，用于管理健身俱乐部设备信息的界面交互和数据处理
 */
public class EquipmentCotrol {
    @FXML
    private TableView<Equipment> tableView;
    @FXML
    private TableColumn<Equipment, Integer> idColumn;
    @FXML
    private TableColumn<Equipment, String> nameColumn;
    @FXML
    private TableColumn<Equipment, String> typeColumn;
    @FXML
    private TableColumn<Equipment, Integer> quantityColumn;
    @FXML
    private TableColumn<Equipment, Double> priceColumn;
    @FXML
    private TableColumn<Equipment, String> manufacturerColumn;
    @FXML
    private TableColumn<Equipment, Date> purchaseDateColumn;
    @FXML
    private TextField idTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField typeTextField;
    @FXML
    private TextField quantityTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private TextField manufacturerTextField;
    @FXML
    private DatePicker purchaseDatePicker;
    @FXML
    private TextField searchTextField;
    @FXML
    private Label pageLabel;
    private ObservableList<Equipment> equipmentList;
    private List<Equipment> filteredEquipmentList;
    private int currentPage = 1;
    private int itemsPerPage = 15;
    private Equipment selectedEquipment;
    @FXML
    private TextField searchByIdTextField;
    @FXML
    private DatePicker searchByDateDatePicker;

    /**
     * 初始化方法，设置表格视图的列属性和监听器
     */
    @FXML
    private void initialize() {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setItems(getEquipmentList());

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        manufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        purchaseDateColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));

        updatePageLabel();

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedEquipment = newSelection;
                showSelectedEquipment();
            }
        });
    }

    /**
     * 展示选中设备的信息，并将其填充到输入框中
     */
    private void showSelectedEquipment() {
        idTextField.setText(String.valueOf(selectedEquipment.getId()));
        nameTextField.setText(selectedEquipment.getName());
        typeTextField.setText(selectedEquipment.getType());
        quantityTextField.setText(String.valueOf(selectedEquipment.getQuantity()));
        priceTextField.setText(String.valueOf(selectedEquipment.getPrice()));
        manufacturerTextField.setText(selectedEquipment.getManufacturer());

        purchaseDatePicker.setValue(selectedEquipment.getPurchaseDate());
        idTextField.setDisable(true);
    }

    /**
     * 获取设备列表数据
     *
     * @return 设备列表的可观察对象
     */
    private ObservableList<Equipment> getEquipmentList() {
        if (equipmentList == null) {
            try {
                List<Equipment> list = EquipmentExcelUtil.readExcel();
                equipmentList = FXCollections.observableArrayList(list);
                filteredEquipmentList = new ArrayList<>(equipmentList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return FXCollections.observableArrayList(filteredEquipmentList.subList(getStartIndex(), getEndIndex()));
    }

    /**
     * 添加设备信息
     */
    @FXML
    private void addEquipment() {
        int id = Integer.parseInt(idTextField.getText());
        String name = nameTextField.getText();
        String type = typeTextField.getText();
        int quantity = Integer.parseInt(quantityTextField.getText());
        double price = Double.parseDouble(priceTextField.getText());
        String manufacturer = manufacturerTextField.getText();
        LocalDate purchaseDate = purchaseDatePicker.getValue();
        Equipment equipment = new Equipment(id, name, type, quantity, price, manufacturer, purchaseDate);
        try {
            EquipmentExcelUtil.addEquipment(equipment);
            equipmentList.add(equipment);
            filteredEquipmentList = new ArrayList<>(equipmentList);
            clearInputFields();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存设备信息
     */
    @FXML
    private void saveEquipment() {
        int id = selectedEquipment.getId();
        String name = nameTextField.getText();
        String type = typeTextField.getText();
        int quantity = Integer.parseInt(quantityTextField.getText());
        double price = Double.parseDouble(priceTextField.getText());
        String manufacturer = manufacturerTextField.getText();
        LocalDate purchaseDate = purchaseDatePicker.getValue();

        Equipment updatedEquipment = new Equipment(id, name, type, quantity, price, manufacturer, purchaseDate);

        try {
            EquipmentExcelUtil.updateEquipment(id, updatedEquipment);
            for (Equipment equipment : equipmentList) {
                if (equipment.getId() == id) {
                    equipment.setName(name);
                    equipment.setType(type);
                    equipment.setQuantity(quantity);
                    equipment.setPrice(price);
                    equipment.setManufacturer(manufacturer);
                    equipment.setPurchaseDate(purchaseDate);
                    break;
                }
            }
            clearInputFields();
        } catch (IOException e) {
            e.printStackTrace();
        }
        idTextField.setDisable(false);
    }

    /**
     * 删除设备信息
     */
    @FXML
    private void deleteEquipment() {
        int id = selectedEquipment.getId();

        try {
            EquipmentExcelUtil.deleteEquipment(id);
            equipmentList.removeIf(equipment -> equipment.getId() == id);
            filteredEquipmentList = new ArrayList<>(equipmentList);
            clearInputFields();
        } catch (IOException e) {
            e.printStackTrace();
        }
        idTextField.setDisable(false);
    }

    /**
     * 搜索设备信息
     */
    @FXML
    private void searchEquipment() {
        String condition = searchTextField.getText().trim().toLowerCase();

        try {
            filteredEquipmentList = EquipmentExcelUtil.searchEquipment(condition);
        } catch (IOException e) {
            e.printStackTrace();
        }

        currentPage = 1;
        updatePageLabel();
        tableView.setItems(getEquipmentList());
    }

    /**
     * 根据ID搜索设备信息
     */
    @FXML
    private void searchById() {
        int id = Integer.parseInt(searchByIdTextField.getText());
        try {
            Equipment equipment = EquipmentExcelUtil.getEquipmentById(id);
            List<Equipment> list = new ArrayList<>();
            list.add(equipment);

            currentPage = 1;
            updatePageLabel();
            tableView.setItems(FXCollections.observableArrayList(list));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据购买日期搜索设备信息
     */
    @FXML
    private void searchByDate() {
        LocalDate localDate = searchByDateDatePicker.getValue();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        try {
            List<Equipment> equipmentList = EquipmentExcelUtil.searchEquipmentByDate(date);

            currentPage = 1;
            updatePageLabel();
            tableView.setItems(FXCollections.observableArrayList(equipmentList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示上一页设备信息
     */
    @FXML
    private void previousPage() {
        if (currentPage > 1) {
            currentPage--;
            updatePageLabel();
            tableView.setItems(getEquipmentList());
        }
    }

    /**
     * 显示下一页设备信息
     */
    @FXML
    private void nextPage() {
        if (currentPage < getTotalPages()) {
            currentPage++;
            updatePageLabel();
            tableView.setItems(getEquipmentList());
        }
    }

    /**
     * 获取当前页数据的起始索引
     *
     * @return 当前页数据的起始索引
     */
    private int getStartIndex() {
        return (currentPage - 1) * itemsPerPage;
    }

    /**
     * 获取当前页数据的结束索引
     *
     * @return 当前页数据的结束索引
     */
    private int getEndIndex() {
        int endIndex = currentPage * itemsPerPage;
        return endIndex > filteredEquipmentList.size() ? filteredEquipmentList.size() : endIndex;
    }

    /**
     * 获取总页数
     *
     * @return 总页数
     */
    private int getTotalPages() {
        return (int) Math.ceil((double) filteredEquipmentList.size() / itemsPerPage);
    }

    /**
     * 更新页面标签显示
     */
    private void updatePageLabel() {
        pageLabel.setText(String.format("第 %d 页（共 %d 页）", currentPage, getTotalPages()));
    }

    /**
     * 清空输入框中的内容
     */
    private void clearInputFields() {
        idTextField.clear();
        nameTextField.clear();
        typeTextField.clear();
        quantityTextField.clear();
        priceTextField.clear();
        manufacturerTextField.clear();
        purchaseDatePicker.setValue(null);
    }
}
