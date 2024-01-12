package JDBC.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 课程成员实体类，用于描述课程成员的属性和行为。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class coursemember {
    /**
     * 成员ID，唯一标识一个课程成员。
     */
    private int ID;

    /**
     * 课程ID，标识该成员所属的课程。
     */
    private String CourseID;

    /**
     * 成员ID，标识该课程成员的唯一标识。
     */
    private String memberID;

    /**
     * 预约状态，描述该课程成员的预约状态。
     */
    private String examine;
}

