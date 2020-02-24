package com.huejie.osmdroid.model.basic;


import org.litepal.annotation.Column;
import org.litepal.annotation.Table;
import org.litepal.crud.LitePalSupport;

@Table("system_user")
public class SysUser extends LitePalSupport {
    public long id;
    @Column(columeName = "dept_id")
    public long deptId;
    @Column(columeName = "company_id")
    public long companyId;
    @Column(columeName = "user_name")
    public String userName;
    @Column(columeName = "login_name")
    public String loginName;
    @Column(columeName = "office_phone")
    public String officePhone;
    @Column(columeName = "user_code")
    public String userCode;
    @Column(columeName = "user_type")
    public String userType;

    public String email;
    public String phone;
    public String mobile;
    public String sex;
    public String avatar;
    public String password;
    public String salt;
    public String status;
    @Column(columeName = "del_flag")
    public String delFlag;
    @Column(columeName = "login_ip")
    public String loginIp;
    @Column(columeName = "create_by")
    public String createBy;
    @Column(columeName = "createTime")
    public String createTime;
    @Column(columeName = "update_by")
    public String updateBy;
    @Column(columeName = "updateTime")
    public String updateTime;
    public long remark;
    @Column(columeName = "wx_openid")
    public String wxOpenid;
    @Column(columeName = "last_login_ip")
    public String lastLoginIp;
    @Column(columeName = "last_login_date")
    public String lastLoginDate;
    @Column(columeName = "freeze_date")
    public String freezeDate;
    @Column(columeName = "freeze_cause")
    public String freezeCause;
    @Column(columeName = "user_name_en")
    public String userNameEn;
    @Column(columeName = "mgr_type")
    public long mgrType;
    @Column(columeName = "user_weight")
    public double userWeight;

}
