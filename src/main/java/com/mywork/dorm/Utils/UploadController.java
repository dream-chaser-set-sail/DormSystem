package com.mywork.dorm.Utils;

import com.mywork.dorm.Constant.ImageConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class UploadController {
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/upload")
    public Map<String, Object> UploadImage(MultipartFile file){
//        生成UUID 把-转为""
        String uuid = UUID.randomUUID().toString().replace("-", "");
//        获取上传文件的原始文件名
        String filename = file.getOriginalFilename();
//        获取文件的尾缀 .png
        String extension = filename.substring(filename.lastIndexOf("."));
//        组成文件名
        String imageName = uuid + extension;

//        阿里云OSS上传
        String url = "";
        try {
            url = AliOSS.uploadFile(imageName, file.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        redisTemplate.opsForSet().add(ImageConstant.UPLOAD_IMAGE, url);

        Map<String, Object> map = new HashMap<>();
        if (filename != null && !"".equals(filename)){
            map.put("result",true);
            map.put("imgName",url);
        }else {
            map.put("result",false);
            map.put("imgName",null);
        }
        return map;
    }
}
