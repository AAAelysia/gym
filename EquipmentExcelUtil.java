package Equipment_;

import Equipment_.Equipment;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 工具类，用于管理健身俱乐部设备信息的数据处理
 */
public class EquipmentExcelUtil {
    private static final String FILE_PATH = "D:\\learn\\Javacxu\\Fitness_Club_Management_System\\src\\Equipment.xlsx";

    /**
     * 读取 Excel 文件，返回 Equipment 对象列表
     * @return Equipment 对象列表
     * @throws IOException IO 异常
     */
    public static List<Equipment> readExcel() throws IOException {
        List<Equipment> equipmentList = new ArrayList<>();

        FileInputStream fis = new FileInputStream(FILE_PATH);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue; // 跳过标题行
            }

            int id = (int) row.getCell(0).getNumericCellValue();
            String name = row.getCell(1).getStringCellValue();
            String type = row.getCell(2).getStringCellValue();
            int quantity = (int) row.getCell(3).getNumericCellValue();
            double price = row.getCell(4).getNumericCellValue();
            String manufacturer = row.getCell(5).getStringCellValue();
            LocalDate purchaseDate = row.getCell(6).getLocalDateTimeCellValue().toLocalDate();

            Equipment equipment = new Equipment(id, name, type, quantity, price, manufacturer, purchaseDate);
            equipmentList.add(equipment);
        }
        workbook.close();
        fis.close();

        return equipmentList;
    }

    /**
     * 将 Equipment 对象列表写入 Excel 文件
     *
     * @param equipmentList Equipment 对象列表
     * @throws IOException IO 异常
     */
    public static void writeExcel(List<Equipment> equipmentList) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        // 写入标题行
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("器材名字");
        headerRow.createCell(2).setCellValue("样式");
        headerRow.createCell(3).setCellValue("数量");
        headerRow.createCell(4).setCellValue("价格");
        headerRow.createCell(5).setCellValue("产商");
        headerRow.createCell(6).setCellValue("购买日期");

        // 写入数据行
        int rowNum = 1;
        for (Equipment equipment : equipmentList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(equipment.getId());
            row.createCell(1).setCellValue(equipment.getName());
            row.createCell(2).setCellValue(equipment.getType());
            row.createCell(3).setCellValue(equipment.getQuantity());
            row.createCell(4).setCellValue(equipment.getPrice());
            row.createCell(5).setCellValue(equipment.getManufacturer());

            Cell purchaseDateCell = row.createCell(6);
            purchaseDateCell.setCellValue(equipment.getPurchaseDate());
            DataFormat dataFormat = workbook.createDataFormat();
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setDataFormat(dataFormat.getFormat("yyyy-MM-dd"));
            purchaseDateCell.setCellStyle(cellStyle);
        }

        FileOutputStream fos = new FileOutputStream(FILE_PATH);
        workbook.write(fos);

        workbook.close();
        fos.close();
    }

    /**
     * 根据 ID 查询 Equipment 对象
     *
     * @param id ID
     * @return Equipment 对象，如果找不到则返回 null
     * @throws IOException IO 异常
     */
    public static Equipment getEquipmentById(int id) throws IOException {
        List<Equipment> equipmentList = readExcel();

        for (Equipment equipment : equipmentList) {
            if (equipment.getId() == id) {
                return equipment;
            }
        }

        return null;
    }

    /**
     * 添加 Equipment 对象到 Excel 文件
     *
     * @param equipment Equipment 对象
     * @throws IOException IO 异常
     */
    public static void addEquipment(Equipment equipment) throws IOException {
        List<Equipment> equipmentList = readExcel();
        equipmentList.add(equipment);
        writeExcel(equipmentList);
    }

    /**
     * 根据 ID 修改 Equipment 对象在 Excel 文件中的值
     *
     * @param id              ID
     * @param updatedEquipment 更新后的 Equipment 对象
     * @throws IOException IO 异常
     */
    public static void updateEquipment(int id, Equipment updatedEquipment) throws IOException {
        List<Equipment> equipmentList = readExcel();

        for (int i = 0; i < equipmentList.size(); i++) {
            Equipment equipment = equipmentList.get(i);
            if (equipment.getId() == id) {
                equipmentList.set(i, updatedEquipment);
                break;
            }
        }

        writeExcel(equipmentList);
    }

    /**
     * 根据 ID 删除 Equipment 对象在 Excel 文件中的行
     *
     * @param id ID
     * @throws IOException IO 异常
     */
    public static void deleteEquipment(int id) throws IOException {
        List<Equipment> equipmentList = readExcel();

        for (int i = 0; i < equipmentList.size(); i++) {
            Equipment equipment = equipmentList.get(i);
            if (equipment.getId() == id) {
                equipmentList.remove(i);
                break;
            }
        }
        writeExcel(equipmentList);

    }
    /**
     * 根据条件查询 Equipment 对象列表
     *
     * @param condition 查询条件
     * @return 符合条件的 Equipment 对象列表
     * @throws IOException IO 异常
     */
    public static List<Equipment> searchEquipment(String condition) throws IOException {
        List<Equipment> equipmentList = readExcel();
        List<Equipment> result = new ArrayList<>();

        for (Equipment equipment : equipmentList) {
            if (equipment.getName().contains(condition) ||
                    equipment.getType().contains(condition) ||
                    equipment.getManufacturer().contains(condition)) {
                result.add(equipment);
            }
        }

        return result;
    }

    /**
     * 根据条件删除 Equipment 对象在 Excel 文件中的行
     *
     * @param condition 删除条件
     * @throws IOException IO 异常
     */
    public static void deleteEquipment(String condition) throws IOException {
        List<Equipment> equipmentList = readExcel();

        for (int i = equipmentList.size() - 1; i >= 0; i--) {
            Equipment equipment = equipmentList.get(i);
            if (equipment.getName().contains(condition) ||
                    equipment.getType().contains(condition) ||
                    equipment.getManufacturer().contains(condition)) {
                equipmentList.remove(i);
            }
        }

        writeExcel(equipmentList);
    }
    /**
     * 根据日期查询 Equipment 对象列表
     *
     * @param date 查询日期
     * @return 符合条件的 Equipment 对象列表
     * @throws IOException IO 异常
     */
    public static List<Equipment> searchEquipmentByDate(Date date) throws IOException {
        List<Equipment> equipmentList = readExcel();
        List<Equipment> result = new ArrayList<>();

        for (Equipment equipment : equipmentList) {
            if (equipment.getPurchaseDate().equals(date)) {
                result.add(equipment);
            }
        }

        return result;
    }

}
