package com.database.dbdesign.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.database.dbdesign.model.teacherActionModel;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class teacherMainPageController {

    @Autowired
    private teacherActionModel teacherAction;

    @GetMapping("/teacherInformation")
    public String getInformation(HttpSession session, Model model){
        return teacherAction.mainInformation(session, model);
    }

    @GetMapping("/studentInformation")
    public String getStudentList(HttpSession session, Model model){
        return teacherAction.getAllStudentInSameDepartment(session, model);
    }

    @GetMapping("/alterPassword")
    public String change(){
        return "teacherAlterPassword";
    }

    @PostMapping("/teacherAlterPassword")
    public String changePassword(String originalPassword, String newPassword, String confirmNewPassword, HttpSession session, Model model){
        return teacherAction.alterPassword(originalPassword, newPassword, confirmNewPassword, session, model);
    }
}
