package com.database.dbdesign.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;

@Repository
public class loginModel {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String loginAction(String loginType, String loginId, String loginPassword, HttpSession session, Model model){
        String sql = loginType.equals("student") ? studentLogin() : teacherLogin();
        if(StringUtils.hasText(loginId) && StringUtils.hasText(loginPassword)){
            try{
                String clientId = jdbcTemplate.queryForObject(sql, String.class,
                        loginId, loginPassword);
                session.setAttribute("loginClientId", clientId);
                session.setAttribute("loginClientType", loginType);
                return loginType.equals("student") ?
                            "redirect:/studentMain.html" : "redirect:/teacherMain.html";
            }catch (EmptyResultDataAccessException e){
                model.addAttribute("msg", "账号密码错误");
                return "login";
            }
        }
        else{
            model.addAttribute("msg", "请确认账号密码");
            return "login";
        }
    }

    private String studentLogin(){
        return "select Sno from student where Sno = ? and Spassword = ?";
    }

    private String teacherLogin(){
        return "select Tno from teacher where Tno = ? and Tpassword = ?";
    }


}
