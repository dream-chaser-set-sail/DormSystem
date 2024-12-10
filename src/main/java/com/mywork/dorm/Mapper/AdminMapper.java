package com.mywork.dorm.Mapper;

import com.mywork.dorm.Bean.Login;
import com.mywork.dorm.Bean.Query;
import com.mywork.dorm.Bean.Admin;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminMapper {
    @Select("<script>"+
            "select * from admin \n" +
            "   <where>\n"+
            "       <if test=\"" +
            "            name != null and name != '' \">\n" +
            "            name like '%${name}%'\n" +
            "       </if>\n" +
            "       <if test=\"createTime != null and updateTime != null \">\n" +
            "           and createTime between #{createTime} and #{updateTime} \n" +
            "       </if>\n" +
            "   </where>" +
            "   limit #{page},#{limit}"+
            "</script>")
    List<Admin> list(Query query);

    @Select("<script>"+
            "select count(*) from admin \n" +
            "  <where>\n"+
            "       <if test=\"" +
            "            name != null and name != '' \">\n" +
            "            name like '%${name}%'\n" +
            "       </if>\n" +
            "       <if test=\"createTime != null and updateTime != null \">\n" +
            "           and createTime between #{createTime} and #{updateTime} \n" +
            "       </if>\n" +
            "   </where>" +
            "</script>")
    Integer listCount(Query query);

    @Delete("delete from admin where id = #{id}")
    Integer deleteById(Integer id);

    @Insert("insert into admin (name, gender, account, email, image) values (#{name}, #{gender}, #{account}, #{email}, #{image})")
    Integer add(Admin admin);

    @Select("select * from admin where account = #{id}")
    Admin selectById(String id);

    @Update("update admin set name = #{name}, image = #{image}, email = #{email} where id = #{id}")
    Integer update(Admin admin);

    @Select("select * from admin where account = #{account}")
    Admin selectByPass(String account);

    @Update("update admin set password = #{newPass} where id = #{id}")
    Integer updatePass(String newPass, Integer id);

    @Select("select * from admin where name = #{name} and account = #{account}")
    Admin selectByLogin(Login login);
}
