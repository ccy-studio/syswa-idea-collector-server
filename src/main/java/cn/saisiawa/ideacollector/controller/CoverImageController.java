package cn.saisiawa.ideacollector.controller;

import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.StrUtil;
import cn.saisiawa.ideacollector.common.anno.SessionCheck;
import cn.saisiawa.ideacollector.common.bean.BaseResponse;
import cn.saisiawa.ideacollector.domain.vo.AiTaskInfoVo;
import cn.saisiawa.ideacollector.service.CoverService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

/**
 * 图片管理
 *
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/30 17:11
 * @Version：1.0
 */
@RestController
@RequestMapping("/img")
@Validated
public class CoverImageController {

    @Resource
    private CoverService coverService;


    /**
     * 文件上传返回路径KEY
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public BaseResponse<String> userUploadImage(@NotNull MultipartFile file) {
        return BaseResponse.ok(coverService.userUploadImage(file));
    }

    /**
     * 创建任务
     *
     * @param id
     * @return
     */
    @RequestMapping("/generator-ai/{articleId}")
    public BaseResponse<Void> createAiTask(@NotNull @PathVariable Long articleId) {
        coverService.createAiTask(articleId);
        return BaseResponse.ok();
    }

    /**
     * 请求获取AI任务
     *
     * @param articleId
     * @return
     */
    @RequestMapping("/get-ai-info/{articleId}")
    public BaseResponse<AiTaskInfoVo> getTaskInfo(@NotNull @PathVariable Long articleId) {
        return BaseResponse.ok(coverService.getTaskInfo(articleId));
//        return BaseResponse.ok(coverService.getTaskInfo(141L));
    }

    /**
     * 设置激活此AI图片的ID作为文章的ID
     *
     * @param id
     */
    @RequestMapping("/activity-image/{id}")
    public BaseResponse<Void> activateAiCover(@NotNull @PathVariable Long id) {
        coverService.activateAiCover(id);
        return BaseResponse.ok();
    }

    /**
     * 返回图片流数据
     *
     * @param key
     * @return
     */
    @RequestMapping
    @SessionCheck(ignore = true)
    public ResponseEntity<InputStreamResource> getImageByte(@NotBlank String key) {
        byte[] imageByte = coverService.getImageByte(key);
        HttpStatusCode statusCode = HttpStatus.OK;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("inline")
                .filename(URLEncodeUtil.encode(StrUtil.subAfter(key, "/", true)))
                .build());
        headers.setContentType(MediaType.parseMediaType("image/jpeg"));
        headers.setContentLength(imageByte.length);
        return ResponseEntity.status(statusCode)
                .headers(headers)
                .body(new InputStreamResource(new ByteArrayInputStream(imageByte)));
    }

}
