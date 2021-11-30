package com.database.dbdesign.model;

import com.database.dbdesign.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String Sno = (String)session.getAttribute("loginClientId");
        try{
            String sql1 = "select Spassword from student where Sno = ?";
            String formerPassword = jdbcTemplate.queryForObject(sql1, String.class, Sno);
            if(formerPassword.equals(originalPassword)){
                if(newPassword.equals(confirmNewPassword)){
                    String sql2 = "update student set Spassword = ? where Sno = ?";
                    Object[] args = {newPassword, Sno};
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

    public String getScore(HttpSession session, Model model){
        float GPA = 0;

        /*
        * 获取成绩列表
        * */
        String Sno = (String)session.getAttribute("loginClientId");
        String sql1 = "select score from student_course where Sno = ?";
        String sql2 = "select Cname from course where Cno in " +
                "(select Cno from student_course where Sno = ?)";
        List<Integer> scoreList = jdbcTemplate.queryForList(sql1, Integer.class, Sno);
        List<String> courseList = jdbcTemplate.queryForList(sql2, String.class, Sno);
        Map<String, Integer> mapScore = new HashMap<>();
        for(int i = 0; i < courseList.size(); i++){
            mapScore.put(courseList.get(i), scoreList.get(i));
        }
        model.addAttribute("scoreMap", mapScore);

        /*
        * 获取GPA
        * */
        String sql3 = "select sum(Ccredit) from course where Cno in " +
                "(select Cno from student_course where Sno = ?)";
        String sql4 = "select score from student_course inner join course " +
                "where Sno = ? and student_course.Cno = course.Cno";
        String sql5 = "select Ccredit from student_course inner join course " +
                "where Sno = ? and student_course.Cno = course.Cno";
        float creditCount = jdbcTemplate.queryForObject(sql3, float.class, Sno);
        List<Integer> GPA_score = jdbcTemplate.queryForList(sql4, Integer.class, Sno);
        List<Float> GPA_credit = jdbcTemplate.queryForList(sql5, Float.class, Sno);
        for(int i = 0; i < GPA_score.size(); i++){
            GPA += (((float) GPA_score.get(i) - 50) / 10 * GPA_credit.get(i));
        }
        GPA /= creditCount;
        model.addAttribute("GPA", GPA);

        return "studentScore";
    }




}
