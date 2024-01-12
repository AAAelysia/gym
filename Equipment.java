package Equipment_;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * 器材类，包含器材的各种属性。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Equipment {
    @ExcelProperty("ID")
    private int id; // 器材 ID

    @ExcelProperty("器材名字")
    private String name; // 器材名称

    @ExcelProperty("样式")
    private String type; // 器材样式

    @ExcelProperty("数量")
    private int quantity; // 器材数量

    @ExcelProperty("价格")
    private double price; // 器材价格

    @ExcelProperty("产商")
    private String manufacturer; // 器材生产商

    @ExcelProperty(value = "购买日期", format = "yyyy-MM-dd")
    @DateTimeFormat("yyyy-MM-dd")
    private LocalDate purchaseDate; // 器材购买日期
}
