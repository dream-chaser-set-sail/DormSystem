package com.mywork.dorm.Mapper;

import cn.hutool.log.Log;
import com.mywork.dorm.Bean.*;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupervisorMapper {

    @Select("<script>" +
            "select * from supervisor \n" +
            "   <where>\n" +
            "       <if test=\"" +
            "            name != null and name != '' \">\n" +
            "            name like '%${name}%'\n" +
            "       </if>\n" +
            "       <if test=\"buildingName != null \">\n" +
            "           and building_name = #{buildingName}\n" +
            "       </if>\n" +
            "       <if test=\"createTime != null and updateTime != null \">\n" +
            "           and createTime between #{createTime} and #{updateTime} \n" +
            "       </if>\n" +
            "   </where>" +
            "   limit #{page},#{limit}" +
            "</script>")
    List<Supervisor> list(Query query);

    @Select("<script>" +
            "select count(*) from supervisor \n" +
            "  <where>\n" +
            "       <if test=\"" +
            "            name != null and name != '' \">\n" +
            "            name like '%${name}%'\n" +
            "       </if>\n" +
            "       <if test=\"buildingName != null \">\n" +
            "           and building_name = #{buildingName}\n" +
            "       </if>\n" +
            "       <if test=\"createTime != null and updateTime != null \">\n" +
            "           and createTime between #{createTime} and #{updateTime} \n" +
            "       </if>\n" +
            "   </where>" +
            "   limit #{page},#{limit}"+
            "</script>")
    Integer listCount(Query query);

    @Delete("delete from supervisor where id = #{id}")
    Integer deleteById(Integer id);

    @Insert("insert into supervisor (account, name, gender, age, image, building_name) values (#{account}, #{name}, #{gender}, #{age}, #{image}, #{buildingName})")
    Integer add(Supervisor supervisor);

    @Select("select * from supervisor where id = #{id}")
    Supervisor selectById(Integer id);

    @Update("update supervisor set name = #{name}, gender = #{gender}, age = #{age}, image = #{image}, building_name = #{buildingName} where id = #{id}")
    Integer update(Supervisor supervisor);

    @Select("select * from supervisor where name = #{name} and account = #{account}")
    Supervisor selectByLogin(Login login);

    @Select("select * from supervisor where account = #{account}")
    Supervisor selectByAccount(String account);
}

