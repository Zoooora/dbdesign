package com.database.dbdesign.model;

import com.database.dbdesign.entity.Student;
import com.database.dbdesign.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class teacherActionModel {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String mainInformation(HttpSession session, Model model){
        String sql = "select * from teacher where Tno = ?";
        String getBirthday = "select DATE(Tbirthday) from teacher where Tno = ?";
        String getDept = "select Dname from department where Dno = ?";
        String getSpeciality = "select SPname from speciality where SPno = ?";
        Teacher teacher = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Teacher.class), session.getAttribute("loginClientId"));
        Date birthday = jdbcTemplate.queryForObject(getBirthday, Date.class, session.getAttribute("loginClientId"));
        String dept = jdbcTemplate.queryForObject(getDept, String.class, teacher.getTdept());
        String speciality = jdbcTemplate.queryForObject(getSpeciality, String.class, teacher.getTspno());
        String s = String.valueOf(birthday);
        model.addAttribute("Tno", session.getAttribute("loginClientId"));
        model.addAttribute("Tname", teacher.getTname());
        model.addAttribute("Tsex", teacher.getTsex());
        model.addAttribute("Tbirthday", s.substring(0, 11));
        model.addAttribute("Tdept", dept);
        model.addAttribute("Tspeciality", speciality);
        return "teacherInformation";
    }

    public String getAllStudentInSameSpeciality(HttpSession session, Model model){
        String Tno = (String)session.getAttribute("loginClientId");
        String sql1 = "select * from student where Sspno in " +
                "(select Tspno from teacher where Tno = ?)";
        List<Student> students = jdbcTemplate.query(sql1, new BeanPropertyRowMapper<>(Student.class), Tno);
        model.addAttribute("list", students);
        return "teacherGetAllStudent";
    }

    public String getScore(String Sno, Model model){
        float GPA = 0;
        /*
         * 获取成绩列表
         * */
        String sql1 = "select score from student_course where Sno = ?";
        String sql2 = "select Cname from course where Cno in " +
                "(select Cno from student_course where Sno = ?)";
        List<Integer> scoreList = jdbcTemplate.queryForList(sql1, Integer.class, Sno);
        List<String> courseList = jdbcTemplate.queryForList(sql2, String.class, Sno);
        Map<String, Integer> mapScore = new HashMap<>();
        for(int i = 0; i < courseList.size(); i++){
            mapScore.put(courseList.get(i), scoreList.get(i));
        }
        model.addAttribute("specificScoreMap", mapScore);
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
        model.addAttribute("specificGPA", GPA);

        return "teacherGetSpecificStudent";
    }

    public String addStudent(Student student, Model model){
        if(!StringUtils.hasText(student.getSno()) || !StringUtils.hasText(student.getSname()) ||
                !StringUtils.hasText(student.getSsex()) || !StringUtils.hasText(student.getSbirthday().toString()) ||
                !StringUtils.hasText(student.getSdept()) || !StringUtils.hasText(student.getSspno()) ||
                !StringUtils.hasText(student.getSpassword())){
            model.addAttribute("addErrorMsg", "请正确的输入全部信息");
            return "teacherAddStudent";
        }
        String sql = "insert into student (Sno, Sname, Ssex, Sbirthday, Sdept, Sspno, Spassword) " +
                "values (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, student.getSno(), student.getSname(), student.getSsex(), student.getSbirthday(), student.getSdept(), student.getSspno(), student.getSpassword());
        return "teacherMain";
    }

    public String alterPassword(String originalPassword, String newPassword, String confirmNewPassword, HttpSession session, Model model){
        String Tno = (String)session.getAttribute("loginClientId");
        try{
            String sql1 = "select Tpassword from teacher where Tno = ?";
            String formerPassword = jdbcTemplate.queryForObject(sql1, String.class, Tno);
            if(formerPassword.equals(originalPassword)){
                if(newPassword.equals(confirmNewPassword)){
                    String sql2 = "update teacher set Tpassword = ? where Tno = ?";
                    Object[] args = {newPassword, Tno};
                    jdbcTemplate.update(sql2, args);
                    return "redirect:/teacherMain.html";
                }
                else {
                    model.addAttribute("differentPasswordMsg", "两次输入密码不一致");
                    return "teacherAlterPassword";
                }
            }
            else{
                model.addAttribute("differentPasswordMsg", "原密码错误");
                return "teacherAlterPassword";
            }
        }catch (Exception e){
            model.addAttribute("differentPasswordMsg", "原密码错误");
            return "teacherAlterPassword";
        }



    }
}
