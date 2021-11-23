package com.database.dbdesign.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Student {
    private String Sno;
    private String Sname;
    private String Ssex;
    private Date Sbirthday;
    private String Sdept;
    private String Sspno;
    private String Spassword;
}
