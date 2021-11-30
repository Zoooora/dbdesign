package com.database.dbdesign.controller;
import com.database.dbdesign.model.studentActionModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class studentMainPageController {

    @Autowired
    private studentActionModel studentAction;

    @PostMapping("/information")
    public String getInformation(HttpSession session, Model model){
        studentAction.mainPageForm(session, model);
        return "studentInformation";
    }

    @GetMapping("/change")
    public String change(){
        return "studentChangePassword";
    }

    @PostMapping("/studentChangePassword")
    public String studentChangePassword(String originalPassword, String newPassword, String confirmNewPassword, HttpSession session, Model model){
        return studentAction.changePassword(originalPassword, newPassword, confirmNewPassword, session, model);
    }

    @GetMapping("/score")
    public String getScore(HttpSession session, Model model){
        return studentAction.getScore(session, model);
    }
}
