package JDBC.coach;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 教练实体类，用于存储教练的信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class coach {
    /**
     * 教练ID
     */
    private String ID;
    /**
     * 教练姓名
     */
    private String Name;
    /**
     * 教练年龄
     */
    private int Age;
    /**
     * 教练性别
     */
    private String Sex;
    /**
     * 教练电话
     */
    private String Phone;
    /**
     * 教练邮箱
     */
    private String Email;
    /**
     * 教练专业领域
     */
    private String Specialization;
    /**
     * 教练工作经验
     */
    private String Experience;
    /**
     * 是否审核通过
     */
    private String Examine;
}
