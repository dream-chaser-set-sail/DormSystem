package com.mywork.dorm.Mapper;

import com.mywork.dorm.Bean.*;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeMapper {

    @Select("<script>"+
            "select * from notice \n" +
            "   <where>\n"+
            "       <if test=\"" +
            "            title != null and title != '' \">\n" +
            "            name like '%${name}%'\n" +
            "       </if>\n" +
            "       <if test=\"createTime != null and updateTime != null \">\n" +
            "           and createTime between #{createTime} and #{updateTime} \n" +
            "       </if>\n" +
            "   </where>" +
            "   limit #{page},#{limit}"+
            "</script>")
    List<Notice> list(Query query);

    @Select("<script>"+
            "select count(*) from notice \n" +
            "  <where>\n"+
            "       <if test=\"" +
            "            title != null and title != '' \">\n" +
            "            title like '%${title}%'\n" +
            "       </if>\n" +
            "       <if test=\"createTime != null and updateTime != null \">\n" +
            "           and createTime between #{createTime} and #{updateTime} \n" +
            "       </if>\n" +
            "   </where>" +
            "</script>")
    Integer listCount(Query query);

    @Delete("delete from notice where id = #{id}")
    Integer deleteById(Integer id);

    @Insert("insert into notice (title, content, account_num, persistent_time) values (#{title}, #{content}, #{accountNum}, #{persistentTime})")
    Integer add(Notice notice);

    @Select("select * from notice where id = #{id}")
    Notice selectById(Integer id);

    @Update("update notice set title = #{title}, content = #{content}, persistent_time = #{persistentTime}  where id = #{id}")
    Integer update(Notice notice);

    @Update("update notice set deleted = #{deleted} where id = #{id}")
    Integer deleted(Integer id, Integer deleted);

    @Select("select * from notice")
    List<Notice> selectByAllData();
}
