package com.mywork.dorm.Service;


import com.mywork.dorm.Bean.*;
import com.mywork.dorm.Mapper.DormMapper;
import com.mywork.dorm.Mapper.MoveMapper;
import com.mywork.dorm.Mapper.StudentMapper;
import com.mywork.dorm.Mapper.SupervisorMapper;
import com.mywork.dorm.Utils.AccountUtil;
import com.mywork.dorm.Utils.JwtUtil;
import com.mywork.dorm.Utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class MoveService {

    @Autowired
    private MoveMapper moveMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private DormMapper dormMapper;

    @Autowired
    private SupervisorMapper supervisorMapper;

    public Result list(Query query) {
        int Page = (query.getPage() - 1) * query.getLimit();
        query.setPage(Page);
        List<Dorm> dorms = moveMapper.selectByDorm();
        Integer count = moveMapper.listCount(query);
        List<Move> moves = moveMapper.list(query);

        Integer i = 0;
        while (i < moves.size()) {
            Move move = moves.get(i);
            for (Dorm dorm : dorms) {
                String name = dorm.getBuildingName() + "-" + dorm.getRoomNumber();

                // 检查是否匹配 move 的 MoveDormId
                if (dorm.getId().equals(move.getMoveDormId())) {
                    move.setMoveDormName(name);
                }

                // 检查是否匹配 move 的 BeforeDormId
                if (dorm.getId().equals(move.getBeforeDormId())) {
                    move.setBeforeDormName(name);
                }

                // 如果两个名字都已经设置，可以提前结束这个循环
                if (move.getMoveDormName() != null && move.getBeforeDormName() != null) {
                    break;
                }
            }
            i++;
        }

        for (Move move : moves) {
            System.out.println("MoveDormName: " + move.getMoveDormName() + ", BeforeDormName: " + move.getBeforeDormName());
        }


        return Result.Page(moves, count);
    }

    public Result deleteById(Integer id) {
        Integer num = moveMapper.deleteById(id);
        if (num != 0){
            return Result.ok("删除成功");
        }
        return Result.error("删除失败");
    }

    public Result add(Move move) {
        Integer num = 0;
        // 通过学生姓名查询学生信息
        Student student = studentMapper.selectByName(move.getName());
        // 通过学生信息中的宿舍号获得迁出宿舍(如果是null表示该学生未分配宿舍)
        move.setBeforeDormId(student.getDormId());
        // 根据期望迁宿的id查询宿舍信息
        Dorm dorm = dormMapper.selectById(move.getMoveDormId());
        // 使用线程传递token
        Map<String, Object> tokens = ThreadLocalUtil.get();
        String account = (String) tokens.get("id");

        //判断操作人员身份
        Integer roleNum = AccountUtil.selectByRole(account);
        if (roleNum == 2 || roleNum == 3){
            move.setStatus("已通过");
        }else if (roleNum == 1) {
            move.setStatus("审核中");
        }

        // 修改学生宿舍信息(迁入宿舍)并添加迁出记录到表,并且修改相应宿舍人数
        if (student.getDormId() == null) {
            if (move.getStatus().equals("已通过")){
                if (dorm.getCapacity()-1 == dorm.getPeople()) {
                    studentMapper.updateByDormId(student.getId(), move.getMoveDormId());
                    dormMapper.updateByPeopleNumAdd(move.getMoveDormId());
                    dormMapper.updateByStatus(move.getMoveDormId());
                } else {
                    studentMapper.updateByDormId(student.getId(), move.getMoveDormId());
                    dormMapper.updateByPeopleNumAdd(move.getMoveDormId());
                }
            }
        } else if (roleNum != 1) {
            return Result.error("您无权对已有宿舍的学生再次进行迁宿");
        } else  if (roleNum == 1){
            num = moveMapper.add(move);
        }

        if (num != 0){
            return Result.ok("操作成功");
        }
        return Result.error("操作失败");
    }

    public Result selectById(Integer id) {
        Move move = moveMapper.selectById(id);
        return Result.clazz(move);
    }

    public Result update(Move move) {
        Integer num = 0;
        Student student = studentMapper.selectByName(move.getName());
        Dorm dorm = dormMapper.selectById(move.getMoveDormId());
        Move moveNow = moveMapper.selectById(move.getId());
        if (moveNow.getStatus().equals("已通过") || moveNow.getStatus().equals("已拒绝")){
            return Result.error("已经批复，无法修改");
        } else {
            if (dorm.getCapacity()-1 == dorm.getPeople()) {
                num = studentMapper.updateByDormId(student.getId(), move.getMoveDormId());
                moveMapper.updateByStatus(move.getId());
                dormMapper.updateByStatus(move.getMoveDormId());
            } else {
                num = studentMapper.updateByDormId(student.getId(), move.getMoveDormId());
                moveMapper.updateByStatus(move.getId());
            }
        }

        if (num != 0){
            dormMapper.updateByPeopleNumAdd(move.getMoveDormId());
            dormMapper.updateByPeopleNumsub(move.getBeforeDormId());

            // 发送消息...

            return Result.ok("已批复");
        }
        return Result.error("操作失败");
    }

    public Result selectByDorm() {
        Map<String, Object> tokens = ThreadLocalUtil.get();
        String account = (String) tokens.get("id");
        Integer roleNum = AccountUtil.selectByRole(account);

        List<Dorm> dorms = moveMapper.selectByDormAll();
        List<DormNumber> dormNumbers = new ArrayList<>();
        for (Dorm dorm : dorms) {
            if (dorm.getCapacity() != dorm.getPeople()){
                String name = dorm.getBuildingName() +"-"+ dorm.getRoomNumber();
                dormNumbers.add(new DormNumber(dorm.getId(), name));
            }
        }

        if (roleNum == 1) {
            Student student = studentMapper.selectByAccount(account);

            Iterator<DormNumber> iterator = dormNumbers.iterator();
            while (iterator.hasNext()) {
                DormNumber dormNumber = iterator.next();
                if (dormNumber.getId() == student.getDormId()) {
                    iterator.remove(); // 移除不符合条件的宿舍
                }
            }
        }

        return Result.selList(dormNumbers);
    }

    public Result updateByNo(Move move) {
        Integer num = 0;
        Student student = studentMapper.selectByName(move.getName());
        Move moveNow = moveMapper.selectById(move.getId());
        if (moveNow.getStatus().equals("已通过") || moveNow.getStatus().equals("已拒绝")){
            return Result.error("已经批复，无法修改");
        } else {
            num = studentMapper.updateByDormId(student.getId(), move.getBeforeDormId());
            moveMapper.updateByStatusNo(move.getId());
        }

        if (num != 0){
            return Result.ok("已批复");
        }
        return Result.error("操作失败");
    }

    public Result selectByName() {
        System.out.println("=========================================================================");
        System.out.println("MoveService.selectByName");
        System.out.println("=========================================================================");
        List<DormName> dormNames = studentMapper.selectForMove();
        for (int i = 0; i < dormNames.size(); i++) {
            dormNames.get(i).setId(i+1);
        }

        System.out.println("=========================================================================");
        System.out.println(dormNames);
        System.out.println("=========================================================================");

        return Result.selList(dormNames);
    }
}
