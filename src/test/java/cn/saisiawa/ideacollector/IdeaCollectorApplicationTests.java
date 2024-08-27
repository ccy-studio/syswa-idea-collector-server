package cn.saisiawa.ideacollector;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.saisiawa.ideacollector.common.bean.SessionInfo;
import cn.saisiawa.ideacollector.common.enums.ImageBizType;
import cn.saisiawa.ideacollector.common.util.ImageUtil;
import cn.saisiawa.ideacollector.domain.entity.Article;
import cn.saisiawa.ideacollector.domain.entity.ArticleComment;
import cn.saisiawa.ideacollector.domain.entity.User;
import cn.saisiawa.ideacollector.domain.req.ArticleCreateReq;
import cn.saisiawa.ideacollector.mapper.ArticleCommentMapper;
import cn.saisiawa.ideacollector.mapper.ArticleMapper;
import cn.saisiawa.ideacollector.mapper.UserMapper;
import cn.saisiawa.ideacollector.service.ArticleService;
import cn.saisiawa.ideacollector.service.SessionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledThreadPoolExecutor;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = "spring.profiles.active=dev")
@Slf4j
class IdeaCollectorApplicationTests {

    @Resource
    private ArticleCommentMapper articleCommentMapper;

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private ArticleService articleService;

    @Resource
    private ImageUtil imageUtil;

    @Resource
    private UserMapper userMapper;


    private boolean execArticle = true;

    private boolean execArticleComment = true;

    private boolean execArticleGive = true;


    @Test
    void contextLoads() throws InterruptedException {
        User user = userMapper.selectList(null).get(0);
        Assert.notNull(user);
        SessionInfo sessionInfo = new SessionInfo();
        sessionInfo.setUser(user);
        sessionInfo.setId(user.getId());
        sessionInfo.setOpenId(user.getOpenId());
        SessionService.setSession(sessionInfo);

        if (execArticle) {
            String jsonContent = HttpUtil.get("https://oshwhub.com/api/fp/idea/list", MapBuilder.<String, Object>create()
                    .put("type", "new")
                    .put("page", "1")
                    .put("pageSize", "100")
                    .build());
            JSONObject json = JSONUtil.parseObj(jsonContent);
            JSONArray list = json.getJSONObject("result").getJSONArray("lists");
            CountDownLatch countDownLatch = ThreadUtil.newCountDownLatch(list.size());
            for (int i = 0; i < list.size(); i++) {
                final JSONObject object = list.getJSONObject(i);
                ThreadUtil.execute(() -> {
                    try {
                        String content = object.getStr("description");
                        String title = object.getStr("title");
                        ArticleCreateReq req = new ArticleCreateReq();
                        req.setComment("init article");
                        req.setTitle(title);
                        req.setContent(content);
                        req.setCover(downloadImage2());
                        articleService.publishArticle(req);
                        log.info("文章入库完成");
                    } catch (Exception e) {
                        log.error("插入文章错误", e);
                    } finally {
                        countDownLatch.countDown();
                    }
                });
            }
            log.info("等待文章插入执行完成......");
            countDownLatch.await();
            log.info("文章批量插入完成<<<<<<<<<");
            ThreadUtil.safeSleep(2000);
        }


        if (execArticleComment) {
            log.info("开始执行评论插入");
            //添加评论
            articleCommentMapper.delete(null);
            ScheduledThreadPoolExecutor scheduledExecutor = ThreadUtil.createScheduledExecutor(10);
            List<Article> articles = articleMapper.selectList(null);
            CountDownLatch commentDownLatch = new CountDownLatch(articles.size());
            for (Article article : articles) {
                scheduledExecutor.execute(() -> {
                    try {
                        Long articleId = article.getId();
                        int rootCount = RandomUtil.randomInt(5, 20);
                        int secondCount = RandomUtil.randomInt(1, 5);
                        int levelCount = RandomUtil.randomInt(1, 3);
                        for (int i = 0; i < rootCount; i++) {
                            ArticleComment comment = new ArticleComment();
                            comment.setArticleId(articleId);
                            comment.setParentId(null);
                            comment.setUserId(user.getId());
                            comment.setContent(generatorComment());
                            articleCommentMapper.insert(comment);
                            for (int i1 = 0; i1 < levelCount; i1++) {
                                for (int j = 0; j < secondCount; j++) {
                                    comment.setParentId(comment.getId());
                                    comment.setId(null);
                                    comment.setContent(generatorComment());
                                    articleCommentMapper.insert(comment);
                                }
                            }
                        }
                        log.info("评论插入完成");
                    } finally {
                        commentDownLatch.countDown();
                    }
                });
            }

            commentDownLatch.await();
            log.info("===>>>>>评论全部插入完成");
        }

        if (execArticleGive) {
            List<Article> articles = articleMapper.selectList(null);
            for (Article article : articles) {
                article.setGive(RandomUtil.randomLong(1, 10000));
                article.setSee(RandomUtil.randomLong(1, 10000));
                articleMapper.updateById(article);
            }
        }

        log.info("程序运行结束");
    }


    @Test
    void updateImage() throws InterruptedException {
        User user = userMapper.selectList(null).get(0);
        Assert.notNull(user);
        SessionInfo sessionInfo = new SessionInfo();
        sessionInfo.setUser(user);
        sessionInfo.setId(user.getId());
        sessionInfo.setOpenId(user.getOpenId());
        SessionService.setSession(sessionInfo);

        List<Article> articles = articleMapper.selectList(null);
        CountDownLatch downLatch = new CountDownLatch(articles.size());
        ScheduledThreadPoolExecutor scheduledExecutor = ThreadUtil.createScheduledExecutor(10);
        for (Article article : articles) {
            scheduledExecutor.execute(() -> {
                try {
                    article.setCover(downloadImage2());
                    articleMapper.updateById(article);
                    log.info("下载完成");
                } catch (Exception e) {
                    log.error("下载失败：", e);
                } finally {
                    downLatch.countDown();
                }
            });
        }
        downLatch.await();
        log.info("================程序执行完成");
    }

    String downloadImage() {
        HttpResponse httpResponse = HttpUtil.createRequest(Method.GET, "https://picsum.photos/512/512?random=" + System.currentTimeMillis())
                .execute();
        String imgUrl = httpResponse.header("location");
        httpResponse.close();
        httpResponse = HttpUtil.createRequest(Method.GET, imgUrl).execute();
        if (httpResponse.contentLength() == 0) {
            httpResponse.close();
            return downloadImage();
        }
        try {
            return imageUtil.saveImage(httpResponse.bodyStream(), IdUtil.fastSimpleUUID() + ".jpg", ImageBizType.USER_UPLOAD);
        } finally {
            httpResponse.close();
        }
    }

    String downloadImage2() {
        HttpResponse httpResponse = HttpUtil.createRequest(Method.GET, "http://imgapi.xl0408.top/index.php")
                .execute();
        String imgUrl = httpResponse.header("location");
        httpResponse.close();
        httpResponse = HttpUtil.createRequest(Method.GET, imgUrl).execute();
        if (httpResponse.contentLength() == 0) {
            httpResponse.close();
            return downloadImage2();
        }
        try {
            return imageUtil.saveImage(httpResponse.bodyStream(), IdUtil.fastSimpleUUID() + ".jpg", ImageBizType.USER_UPLOAD);
        } finally {
            httpResponse.close();
        }
    }

    String generatorComment() {
        String jsonContent = HttpUtil.get("https://api.uomg.com/api/comments.163?format=json");
        JSONObject object = JSONUtil.parseObj(jsonContent);
        return object.getJSONObject("data").getStr("content");
    }

}
