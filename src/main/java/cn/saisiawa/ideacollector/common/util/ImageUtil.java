package cn.saisiawa.ideacollector.common.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.net.URLEncodeUtil;
import cn.saisiawa.ideacollector.common.enums.ImageBizType;
import cn.saisiawa.ideacollector.conf.ApplicationContextRegister;
import cn.saisiawa.ideacollector.service.SessionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/30 9:34
 * @Version：1.0
 */
@Component
@Slf4j
public class ImageUtil {


    /**
     * 本地保存的根路径
     */
    @Value("${save-image-path}")
    private String saveImageRootPath;

    @Resource
    private ApplicationContextRegister applicationContextRegister;

    private String generatorFolderName() {
        return new DateTime().toString("yyyyMMdd");
    }


    /**
     * 保存上传的文件到本地，返回相对路径
     *
     * @param ins      文件流
     * @param fileName 文件名称
     * @param type     类型
     * @return 相对路径字符串
     */
    public String saveImage(InputStream ins, String fileName, ImageBizType type) {
        File savePath = new File(saveImageRootPath);
        fileName = String.format("%d-%s", SessionService.getSession().getId(), fileName);
        File root = new File(savePath, type.getFilePrefix().concat(File.separator).concat(generatorFolderName()));
        File file = new File(root, fileName);
        FileUtil.writeFromStream(ins, file, true);
        return URLEncodeUtil.encode(file.getAbsolutePath().substring(savePath.getAbsolutePath().length()),
                StandardCharsets.UTF_8);
    }


    /**
     * 保存文件
     *
     * @param file
     * @param type
     * @return
     */
    public String saveImage(MultipartFile file, ImageBizType type) {
        String filename = file.getOriginalFilename();
        try {
            return saveImage(file.getInputStream(), filename, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public byte[] getImageByte(String key) {
        File file = new File(saveImageRootPath, URLDecoder.decode(key, StandardCharsets.UTF_8));
        if (!file.exists()) {
            org.springframework.core.io.Resource resource = applicationContextRegister.getResource("classpath:img_fail.png");
            try {
                return IoUtil.readBytes(resource.getInputStream(), true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return FileUtil.readBytes(file);
    }

}
