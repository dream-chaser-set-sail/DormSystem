package com.mywork.dorm.Service;


import com.mywork.dorm.Bean.*;
import com.mywork.dorm.Mapper.AdminMapper;
import com.mywork.dorm.Mapper.MoveMapper;
import com.mywork.dorm.Mapper.NoticeMapper;
import com.mywork.dorm.Mapper.NoticeMapper;
import com.mywork.dorm.Utils.AccountUtil;
import com.mywork.dorm.Utils.JwtUtil;
import com.mywork.dorm.Utils.SetDefaultProfileImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private AdminMapper adminMapper;

    public Result list(Query query) {
        int Page = (query.getPage() - 1) * query.getLimit();
        query.setPage(Page);
        Integer count = noticeMapper.listCount(query);
        List<Notice> users = noticeMapper.list(query);

        for (Notice user : users) {
            if (user.getPersistentTime() != null) {
                user.setPersistentTime(user.getPersistentTime().replace(",", " 至 "));
            }

            if (user.getAccountNum() != null) {
                Admin admin = adminMapper.selectById(user.getAccountNum());
                user.setName(admin.getName());
            }
        }

        return Result.Page(users, count);
    }

    public Result deleteById(Integer id) {
        Integer num = noticeMapper.deleteById(id);
        if (num != 0){
            return Result.ok("删除成功");
        }
        return Result.error("删除失败");
    }

    public Result add(Notice notice) {
        String[] persistentTimeArray = notice.getPersistentTimeArray();
        StringBuffer persistentTime = new StringBuffer();
        for (int i = 0; i < persistentTimeArray.length; i++) {
            if (i != persistentTimeArray.length-1) {
                persistentTime.append(persistentTimeArray[i] + ",");
            }else {
                persistentTime.append(persistentTimeArray[i]);
            }
        }
        notice.setPersistentTime(persistentTime.toString());

        Integer num = noticeMapper.add(notice);
        if (num != 0){
            return Result.ok("添加成功");
        }
        return Result.error("添加失败");
    }

    public Result selectById(Integer id) {
        Notice notice = noticeMapper.selectById(id);
        return Result.clazz(notice);
    }

    public Result update(Notice notice) {
        Integer num = noticeMapper.update(notice);
        if (num != 0){
            return Result.ok("修改成功");
        }
        return Result.error("修改失败");
    }

    public Result deleted(Integer id, Integer deleted) {
        Integer num = noticeMapper.deleted(id, deleted);
        if (num != 0){
            return Result.ok("执行成功");
        }
        return Result.error("执行失败");
    }

    public Result selectByAllData() {
        List<Notice> noticeList = noticeMapper.selectByAllData();
        for (Notice notice : noticeList) {
            Admin admin = adminMapper.selectById(notice.getAccountNum());
            notice.setName(admin.getName());
        }

        Iterator<Notice> iterator = noticeList.iterator();
        while (iterator.hasNext()) {
            Notice notice = iterator.next();
            if (notice.getDeleted() == 0) {
                iterator.remove();
            }
        }

        return Result.selList(noticeList);
    }
}
