package com.database.dbdesign.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class Student {
    private String Sno;
    private String Sname;
    private String Ssex;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date Sbirthday;

    private String Sdept;
    private String Sspno;
    private String Spassword;
}
