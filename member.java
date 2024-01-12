package JDBC.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * member类是一个用于表示会员信息的实体类。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class member {
    /**
     * 会员ID
     */
    private String ID;
    /**
     * 会员姓名
     */
    private String Name;
    /**
     * 会员年龄
     */
    private int Age;
    /**
     * 会员性别
     */
    private String Sex;
    /**
     * 会员电话号码
     */
    private String Phone;
    /**
     * 会员邮箱
     */
    private String Email;
    /**
     * 会员地址
     */
    private String Address;
    /**
     * 会员密码
     */
    private String password;
}
