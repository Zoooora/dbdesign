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

    @GetMapping("/change")
    public String change(){
        return "studentChangePassword";
    }

    @PostMapping("/studentChangePassword")
    public String studentChangePassword(String originalPassword, String newPassword, String confirmNewPassword, HttpSession session, Model model){
        return studentAction.changePassword(originalPassword, newPassword, confirmNewPassword, session, model);
    }
}
