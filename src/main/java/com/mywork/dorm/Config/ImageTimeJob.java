package com.mywork.dorm.Config;

import com.mywork.dorm.Constant.ImageConstant;
import com.mywork.dorm.Utils.AliOSS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.ObjectUtils;

import java.util.Set;

/**
 * 删除阿里云上多余没用的照片
 */
@Configuration
public class ImageTimeJob {
    @Autowired
    private RedisTemplate redisTemplate;

    @Scheduled(cron = "0 0/2 * * * ?")
    public void deleteImageforTime(){
        System.out.println("ImageTimeJob.deleteImageforTime");
        // 通过Set的差集把真正上传的图片留下，其余的是要删除无效数据
        Set<String> difference = redisTemplate.opsForSet().difference(ImageConstant.UPLOAD_IMAGE, ImageConstant.UPLOAD_IMAGE_TO_DB);
        if (!ObjectUtils.isEmpty(difference)){
            for (String imageName : difference) {
                // 通过阿里云提供的方法删除信息
                AliOSS.deleteFile(imageName);
                redisTemplate.delete(ImageConstant.UPLOAD_IMAGE);
                redisTemplate.delete(ImageConstant.UPLOAD_IMAGE_TO_DB);
            }
        }
    }
}
