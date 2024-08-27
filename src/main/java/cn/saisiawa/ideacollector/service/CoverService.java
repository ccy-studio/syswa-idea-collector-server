package cn.saisiawa.ideacollector.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.saisiawa.ideacollector.common.enums.ImageBizType;
import cn.saisiawa.ideacollector.common.exception.BizException;
import cn.saisiawa.ideacollector.common.util.AiGeneratorUtil;
import cn.saisiawa.ideacollector.common.util.ImageUtil;
import cn.saisiawa.ideacollector.domain.entity.AiCoverTask;
import cn.saisiawa.ideacollector.domain.entity.AiCoverTaskResult;
import cn.saisiawa.ideacollector.domain.entity.Article;
import cn.saisiawa.ideacollector.domain.vo.AiTaskInfoVo;
import cn.saisiawa.ideacollector.domain.vo.ArticleInfoVo;
import cn.saisiawa.ideacollector.mapper.AiCoverTaskMapper;
import cn.saisiawa.ideacollector.mapper.AiCoverTaskResultMapper;
import cn.saisiawa.ideacollector.mapper.ArticleMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/30 16:25
 * @Version：1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class CoverService {

    @Resource
    private ArticleService articleService;

    @Resource
    private AiGeneratorUtil aiGeneratorUtil;

    @Resource
    private ImageUtil imageUtil;

    @Resource
    private AiCoverTaskMapper aiCoverTaskMapper;

    @Resource
    private AiCoverTaskResultMapper taskResultMapper;

    @Resource
    private ArticleMapper articleMapper;

    /**
     * 文件上传返回路径KEY
     *
     * @param file
     * @return
     */
    public String userUploadImage(MultipartFile file) {
        return imageUtil.saveImage(file, ImageBizType.USER_UPLOAD);
    }


    /**
     * 创建任务
     *
     * @param id
     * @return
     */
    public AiCoverTask createAiTask(Long id) {
        if (aiCoverTaskMapper.selectCount(Wrappers.lambdaQuery(AiCoverTask.class)
                .eq(AiCoverTask::getArticleId, id)
        ) != 0) {
            throw new BizException("无法重复创建AI生图任务");
        }
        ArticleInfoVo detail = articleService.getDetailById(id);
        Assert.notNull(detail);
        AiCoverTask task = new AiCoverTask();
        task.setArticleId(id);
        task.setWidth(768);
        task.setHeight(512);
        task.setAiModel("1");
        task.setPrompt(detail.getTitle().concat("(").concat(detail.getContent()).concat(")"));
        List<AiCoverTaskResult> generatorTask = aiGeneratorUtil.createGeneratorTask(task, 3);
        if (CollUtil.isEmpty(generatorTask)) {
            throw new BizException("任务创建失败，请手动上传封面");
        }
        aiCoverTaskMapper.insert(task);
        generatorTask.forEach(v -> {
            v.setCoverTaskId(task.getId());
            taskResultMapper.insert(v);
        });
        return task;
    }


    /**
     * 请求获取AI任务
     *
     * @param articleId
     * @return
     */
    public AiTaskInfoVo getTaskInfo(Long articleId) {
        AiCoverTask task = aiCoverTaskMapper.selectOne(Wrappers.lambdaQuery(AiCoverTask.class)
                .eq(AiCoverTask::getArticleId, articleId)
        );
        Assert.notNull(task);
        List<AiCoverTaskResult> resultList = taskResultMapper.selectList(Wrappers.lambdaQuery(AiCoverTaskResult.class)
                .eq(AiCoverTaskResult::getCoverTaskId, task.getId())
        );
        List<AiCoverTaskResult> waitList = resultList.stream().filter(f -> f.getStatus().equals(0)).toList();
        waitList.forEach(v -> aiGeneratorUtil.getAiResult(v));
        int successCount = 0;
        for (AiCoverTaskResult result : resultList) {
            if (result.getStatus().equals(1)) {
                successCount++;
            }
        }
        waitList.stream().filter(f -> !f.getStatus().equals(0))
                .forEach(v -> {
                    if (v.getStatus().equals(1)) {
                        byte[] bytes = aiGeneratorUtil.downloadImage(v);
                        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                        String pathKey = imageUtil.saveImage(inputStream, IdUtil.fastSimpleUUID() + ".jpg", ImageBizType.AI_GENERATOR);
                        v.setGenImgLocal(pathKey);
                    }
                    taskResultMapper.updateById(v);
                });

        AiTaskInfoVo taskInfoVo = new AiTaskInfoVo();
        taskInfoVo.setId(task.getId());
        taskInfoVo.setStatus(task.getStatus());
        taskInfoVo.setItems(resultList.stream().map(v -> BeanUtil.copyProperties(v, AiTaskInfoVo.AiTaskItem.class)).toList());

        if (successCount == resultList.size()) {
            task.setStatus(1);
            task.setUpdateTime(LocalDateTime.now());
            aiCoverTaskMapper.updateById(task);
        }
        return taskInfoVo;
    }


    /**
     * 设置激活此AI图片的ID作为文章的ID
     *
     * @param id
     */
    public void activateAiCover(Long id) {
        AiCoverTaskResult result = taskResultMapper.selectById(id);
        Assert.notNull(result);
        AiCoverTask task = aiCoverTaskMapper.selectById(result.getCoverTaskId());
        Article article = articleMapper.selectById(task.getArticleId());
        Assert.notNull(article);
        article.setCover(result.getGenImgLocal());
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.updateById(article);
    }

    /**
     * 返回图片流数据
     *
     * @param key
     * @return
     */
    public byte[] getImageByte(String key) {
        return imageUtil.getImageByte(key);
    }
}
