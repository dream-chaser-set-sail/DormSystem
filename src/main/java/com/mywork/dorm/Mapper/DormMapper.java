package com.mywork.dorm.Mapper;

import com.mywork.dorm.Bean.BuildingName;
import com.mywork.dorm.Bean.Query;
import com.mywork.dorm.Bean.Dorm;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DormMapper {

    @Select("<script>" +
            "SELECT * FROM dormitories " +
            "<where>" +
            "   <if test=\"buildingName != null and buildingName != ''\">" +
            "       AND building_name LIKE CONCAT('%', #{buildingName}, '%')" +
            "   </if>" +
            "   <if test=\"roomNumber != null and roomNumber != ''\">" +
            "       AND room_number LIKE CONCAT('%', #{roomNumber}, '%')" +
            "   </if>" +
            "</where>" +
            "limit #{page},#{limit}"+
            "</script>")
    List<Dorm> list(Query query);

    @Select("<script>" +
            "SELECT COUNT(*) FROM dormitories " +
            "<where>" +
            "   <if test=\"buildingName != null and buildingName != ''\">" +
            "       AND building_name LIKE CONCAT('%', #{buildingName}, '%')" +
            "   </if>" +
            "   <if test=\"roomNumber != null and roomNumber != ''\">" +
            "       AND room_number LIKE CONCAT('%', #{roomNumber}, '%')" +
            "   </if>" +
            "</where>" +
            "</script>")
    Integer listCount(Query query);

    @Delete("delete from dormitories where id = #{id}")
    Integer deleteById(Integer id);

    @Insert("insert into dormitories (building_name, room_number) values (#{buildingName}, #{roomNumber})")
    Integer add(Dorm dormitories);

    @Select("select * from dormitories where id = #{id}")
    Dorm selectById(Integer id);

    @Update("update dormitories set building_name = #{buildingName}, room_number = #{roomNumber} where id = #{id}")
    Integer update(Dorm dormitories);

    @Select("select building_name from dormitories group by building_name")
    List<BuildingName> selectByBuildingName();

    @Update("update dormitories set people = people + 1 where id = #{id}")
    Integer updateByPeopleNumAdd(Integer id);

    @Update("update dormitories set people = people - 1 where id = #{id}")
    Integer updateByPeopleNumsub(Integer id);

    @Update("update dormitories set status = '已住满' where id = #{id}")
    Integer updateByStatus(Integer id);

    @Select("SELECT building_name, SUM(people) AS 'people' FROM `dormitories` GROUP BY building_name")
    List<Dorm> selectByCharts();
}