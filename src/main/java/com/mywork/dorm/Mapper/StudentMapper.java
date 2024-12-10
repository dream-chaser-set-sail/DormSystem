package com.mywork.dorm.Mapper;

import com.mywork.dorm.Bean.*;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentMapper {

    @Select("<script>"+
            "select * from student \n" +
            "   <where>\n"+
            "       <if test=\"" +
            "            name != null and name != '' \">\n" +
            "            name like '%${name}%'\n" +
            "       </if>\n" +
            "       <if test=\"college != null \">\n" +
            "           and college = #{college}\n" +
            "       </if>\n" +
            "       <if test=\"major != null \">\n" +
            "           and major = #{major}\n" +
            "       </if>\n" +
            "       <if test=\"createTime != null and updateTime != null \">\n" +
            "           and createTime between #{createTime} and #{updateTime} \n" +
            "       </if>\n" +
            "   </where>" +
            "   limit #{page},#{limit}"+
            "</script>")
    List<Student> list(Query query);

    @Select("<script>"+
            "select count(*) from student \n" +
            "  <where>\n"+
            "       <if test=\"" +
            "            name != null and name != '' \">\n" +
            "            name like '%${name}%'\n" +
            "       </if>\n" +
            "       <if test=\"college != null \">\n" +
            "           and college = #{college}\n" +
            "       </if>\n" +
            "       <if test=\"major != null \">\n" +
            "           and major = #{major}\n" +
            "       </if>\n" +
            "       <if test=\"createTime != null and updateTime != null \">\n" +
            "           and createTime between #{createTime} and #{updateTime} \n" +
            "       </if>\n" +
            "   </where>" +
            "</script>")
    Integer listCount(Query query);

    @Delete("delete from student where id = #{id}")
    Integer deleteById(Integer id);

    @Insert("insert into student (account, name, gender, age, image, email, college, major, dorm_id) values (#{account}, #{name}, #{gender}, #{age}, #{image}, #{email}, #{college}, #{major}, #{dormId})")
    Integer add(Student student);

    @Select("select * from student where id = #{id}")
    Student selectById(Integer id);

    @Update("update student set name = #{name}, gender = #{gender}, age = #{age}, image = #{image}, email = #{email}, college = #{college}, major = #{major}, dorm_id = #{dormId} where id = #{id}")
    Integer update(Student student);

    @Select("select * from college")
    List<College> selectByCollege();

    @Select("<script>" +
            "SELECT * FROM major " +
            "<where>" +
            "   <if test=\"id != null\"> " +
            "       college_id = #{id} " +
            "   </if>" +
            "</where>" +
            "</script>")
    List<Major> selectByMajor(Integer id);

    @Update("update student set dorm_id = #{dormId} where id = #{id}")
    Integer updateByDormId(Integer id, Integer dormId);

    @Select("select * from student where name = #{name}")
    Student selectByName(String name);

    @Select("select name from student")
    List<DormName> selectToName();

    @Select("select * from student where name = #{name} and account = #{account}")
    Student selectByLogin(Login login);

    @Select("select * from student where account = #{account}")
    Student selectByAccount(String account);

    @Select("select * from student where name = #{name}")
    Student selectByNameForBuilding(String name);

    @Select("select * from student where dorm_id = #{dormId}")
    List<Student> selectByDormId(Integer dormId);

    @Select("select name from student where dorm_id is null")
    List<DormName> selectForMove();
}