package com.database.dbdesign.controller;

import com.database.dbdesign.model.loginModel;
import com.database.dbdesign.model.studentActionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class indexController {

    @Autowired
    private loginModel login;/*不能用new*/

    @Autowired
    private studentActionModel studentAction;

    @GetMapping(value = {"/", "/login"})
    public String loginPage(){
        return "login";
    }

    @PostMapping("/login")
    public String main(String loginType, String loginId, String loginPassword, HttpSession session, Model model){
        return login.loginAction(loginType, loginId, loginPassword, session, model);
    }

    @GetMapping("/studentMain.html")
    public String studentMainPage(HttpSession session, Model model){
        Object loginStudent = session.getAttribute("loginClientId");
        if(loginStudent != null) {
            studentAction.mainPageForm(session, model);
            return "studentMain";
        }
        else{
            model.addAttribute("msg", "请重新登录");
            return "login";
        }
    }

    @GetMapping("/teacherMain.html")
    public String teacherMainPage(HttpSession session, Model model){
        Object loginStudent = session.getAttribute("loginClientId");
        if(loginStudent != null) return "teacherMain";
        else{
            model.addAttribute("msg", "请重新登录");
            return "login";
        }
    }
}
