package com.mywork.dorm.Mapper;

import com.mywork.dorm.Bean.Dorm;
import com.mywork.dorm.Bean.Repair;
import com.mywork.dorm.Bean.Query;
import com.mywork.dorm.Bean.Repair;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairMapper {

    @Select("<script>" +
            "SELECT * FROM repair " +
            "<where>" +
            "   <if test=\"name != null and name != ''\">" +
            "       AND name LIKE CONCAT('%', #{name}, '%')" +
            "   </if>" +
            "   <if test=\"createTime != null and updateTime != null \">\n" +
            "       and createTime between #{createTime} and #{updateTime} \n" +
            "   </if>\n" +
            "</where>" +
            "limit #{page},#{limit}"+
            "</script>")
    List<Repair> list(Query query);

    @Select("<script>" +
            "SELECT COUNT(*) FROM repair " +
            "<where>" +
            "   <if test=\"name != null and name != ''\">" +
            "       AND name LIKE CONCAT('%', #{name}, '%')" +
            "   </if>" +
            "   <if test=\"createTime != null and updateTime != null \">\n" +
            "       and createTime between #{createTime} and #{updateTime} \n" +
            "   </if>\n" +
            "</where>" +
            "</script>")
    Integer listCount(Query query);

    @Delete("delete from repair where id = #{id}")
    Integer deleteById(Integer id);

    @Insert("insert into repair (name, dorm_id, repair) values (#{name}, #{DormId}, #{repair})")
    Integer add(Repair repair);

    @Select("select * from repair where id = #{id}")
    Repair selectById(Integer id);

    @Update("update repair set status = '已维修' where id = #{id}")
    Integer updateByStatus(Integer id);
}