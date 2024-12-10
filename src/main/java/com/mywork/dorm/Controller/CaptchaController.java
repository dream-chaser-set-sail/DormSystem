package com.mywork.dorm.Controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.mywork.dorm.Utils.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class CaptchaController {

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/captcha")
    public String captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("CaptchaController.captcha");
//        no-store 表示不允许缓存任何内容，这意味着浏览器或其他缓存设备不应该存储任何关于该请求及其响应的信息。
//        no-cache 表示在使用缓存的情况下，每次请求都需要重新验证服务器上的内容，这样可以确保用户获得最新的数据。
        response.setHeader("Cache-Control", "no-store, no-cache");
//        这行代码设置了响应的内容类型为 JPEG 图像。
        response.setContentType("image/jpeg");
        //生成文字验证码
        String captcha = defaultKaptcha.createText();
        log.info("验证码：{}", captcha);
        HttpSession session = request.getSession();
        session.setAttribute("code", captcha);
        // 存入readis
        redisTemplate.opsForValue().set(CommonUtil.getCaptchaKey(request), captcha, 60 * 1000, TimeUnit.MILLISECONDS);
        // 生成图片验证码
        BufferedImage image = defaultKaptcha.createImage(captcha);
//        ServletOutputStream out = response.getOutputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", out);
        String base64Code = Base64.encodeBase64String(out.toByteArray());
        String base = "data:image/png;base64," + base64Code;
        return base;
    }
}
