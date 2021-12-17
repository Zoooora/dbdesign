package com.database.dbdesign.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Teacher {
    private String Tno;
    private String Tname;
    private String Tsex;
    private Date Tbirthday;
    private String Tdept;
    private String Tspno;
    private String Tpassword;
}
