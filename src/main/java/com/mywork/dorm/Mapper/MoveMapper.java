package com.mywork.dorm.Mapper;

import com.mywork.dorm.Bean.BuildingName;
import com.mywork.dorm.Bean.Dorm;
import com.mywork.dorm.Bean.Move;
import com.mywork.dorm.Bean.Query;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoveMapper {

    @Select("<script>" +
            "SELECT * FROM move " +
            "<where>" +
            "   <if test=\"buildingName != null and buildingName != ''\">" +
            "       AND name LIKE CONCAT('%', #{name}, '%')" +
            "   </if>" +
            "   <if test=\"createTime != null and updateTime != null \">\n" +
            "       and createTime between #{createTime} and #{updateTime} \n" +
            "   </if>\n" +
            "</where>" +
            "limit #{page},#{limit}"+
            "</script>")
    List<Move> list(Query query);

    @Select("<script>" +
            "SELECT COUNT(*) FROM move " +
            "<where>" +
            "   <if test=\"buildingName != null and buildingName != ''\">" +
            "       AND name LIKE CONCAT('%', #{name}, '%')" +
            "   </if>" +
            "   <if test=\"createTime != null and updateTime != null \">\n" +
            "       and createTime between #{createTime} and #{updateTime} \n" +
            "   </if>\n" +
            "</where>" +
            "</script>")
    Integer listCount(Query query);

    @Delete("delete from move where id = #{id}")
    Integer deleteById(Integer id);

    @Insert("insert into move (name, before_dormId, move_dormId, status) values (#{name}, #{beforeDormId}, #{moveDormId}, #{status})")
    Integer add(Move move);

    @Select("select * from move where id = #{id}")
    Move selectById(Integer id);

    @Select("select id, building_name, room_number from dormitories")
    List<Dorm> selectByDorm();

    @Update("update move set status = '已通过' where id = #{id}")
    Integer updateByStatus(Integer id);

    @Update("update move set status = '已拒绝' where id = #{id}")
    Integer updateByStatusNo(Integer id);

    @Select("select * from dormitories")
    List<Dorm> selectByDormAll();
}