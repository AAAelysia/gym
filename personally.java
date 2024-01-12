package JDBC.personal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 个人课程信息类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class personally {
    private String ID;              // 个人课程ID
    private String CoachID;         // 教练ID
    private String MemberID;        // 会员ID
    private LocalDate begin_;       // 开始日期
    private LocalDate end_;         // 结束日期
    private Double price;           // 价格
    private String time_;           // 时间
    private String examine;         // 审核状态
}
