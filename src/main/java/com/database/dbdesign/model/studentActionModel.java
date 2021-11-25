package com.database.dbdesign.model;

import com.database.dbdesign.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Repository
public class studentActionModel {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void mainPageForm(HttpSession session, Model model){
        String sql = "select * from student where Sno = ?";
        String getBirthday = "select DATE(Sbirthday) from student where Sno = ?";
        String getDept = "select Dname from department where Dno = ?";
        String getSpeciality = "select SPname from speciality where SPno = ?";
        Student stu = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Student.class), session.getAttribute("loginClientId"));
        Date birthday = jdbcTemplate.queryForObject(getBirthday, Date.class, session.getAttribute("loginClientId"));
        String dept = jdbcTemplate.queryForObject(getDept, String.class, stu.getSdept());
        String speciality = jdbcTemplate.queryForObject(getSpeciality, String.class, stu.getSspno());
        String s = String.valueOf(birthday);
        model.addAttribute("Sno", session.getAttribute("loginClientId"));
        model.addAttribute("Sname", stu.getSname());
        model.addAttribute("Ssex", stu.getSsex());
        model.addAttribute("Sbirthday", s.substring(0, 11));
        model.addAttribute("Sdept", dept);
        model.addAttribute("Sspno", speciality);
    }

    public String changePassword(String originalPassword, String newPassword, String confirmNewPassword, HttpSession session, Model model){
        try{
            String sql1 = "select Spassword from student where Sno = ?";
            String formerPassword = jdbcTemplate.queryForObject(sql1, String.class, session.getAttribute("loginClientId"));
            if(formerPassword.equals(originalPassword)){
                if(newPassword.equals(confirmNewPassword)){
                    String sql2 = "update student set Spassword = ? where Sno = ?";
                    Object[] args = {newPassword, session.getAttribute("loginClientId")};
                    jdbcTemplate.update(sql2, args);
                    return "redirect:/studentMain.html";
                }
                else {
                    model.addAttribute("differentPasswordMsg", "两次输入密码不一致");
                    return "studentChangePassword";
                }
            }
            else{
                model.addAttribute("differentPasswordMsg", "原密码错误");
                return "studentChangePassword";
            }
        }catch (Exception e){
            model.addAttribute("differentPasswordMsg", "原密码错误");
            return "studentChangePassword";
        }

        
    }
}
