package JDBC.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 课程实体类，用于描述一门课程的属性和行为。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    /**
     * 课程号，唯一标识一门课程。
     */
    private String ID;

    /**
     * 课程名字，描述课程的名称。
     */
    private String Name;

    /**
     * 教练ID，指导该课程的教练的唯一标识。
     */
    private String CoachID;

    /**
     * 课程类别，描述该课程所属的课程类别。
     */
    private String Type;

    /**
     * 课程时间，描述该课程的时长。
     */
    private int Duration;

    /**
     * 课程描述，描述该课程的详细信息。
     */
    private String Description;

    /**
     * 预约状态，描述该课程的预约状态。
     */
    private String examine;

    /**
     * 价格，描述该课程的价格。
     */
    private int price;

    /**
     * 班级人数，描述该课程开设的班级人数。
     */
    private int number;
}

