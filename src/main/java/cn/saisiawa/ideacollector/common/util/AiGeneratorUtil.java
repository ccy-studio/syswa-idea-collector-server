package cn.saisiawa.ideacollector.common.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.saisiawa.ideacollector.domain.entity.AiCoverTask;
import cn.saisiawa.ideacollector.domain.entity.AiCoverTaskResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/30 15:37
 * @Version：1.0
 */
@Service
@Slf4j
public class AiGeneratorUtil {

    @Value("${ai-api-key}")
    private String apiKey;

    private static final PoolingHttpClientConnectionManager CM;
    private static final CloseableHttpClient HTTP_CLIENT;

    static {
        CM = allTrust();
        HTTP_CLIENT = HttpClients.custom()
                .setConnectionManager(CM)
                .build();
    }


    /**
     * 创建AI生图任务
     *
     * @param task
     * @param count 生成的数量
     * @return
     */
    public List<AiCoverTaskResult> createGeneratorTask(AiCoverTask task, int count) {
        HttpPost httpPost = new HttpPost(URI.create("https://ston.6pen.art/release/open-task"));
        httpPost.addHeader("content-type", "application/json");
        httpPost.addHeader("ys-api-key", apiKey);
        Map<String, Object> data = new HashMap<>();
        data.put("prompt", task.getPrompt());
        data.put("fill_prompt", 0);
        data.put("width", task.getWidth());
        data.put("height", task.getHeight());
        data.put("model_type", "preset");
        data.put("model_id", task.getAiModel());
        data.put("multiply", count);
        HttpEntity entity = new StringEntity(JSONUtil.toJSONString(data), StandardCharsets.UTF_8);
        httpPost.setEntity(entity);
        try {
            CloseableHttpResponse response = HTTP_CLIENT.execute(httpPost);
            String content = IoUtil.read(response.getEntity().getContent(), true).toString("utf-8");
            HttpClientUtils.closeQuietly(response);
            Map<String, Object> map = JSONUtil.parseObject(content, Map.class);
            if (MapUtil.getInt(map, "code") != 200) {
                log.error("请求失败:{}", content);
                return null;
            }
            Map<String, Object> dat = (Map<String, Object>) map.get("data");
            List<String> ids = (List<String>) dat.get("ids");
            return ids.stream().map(id -> {
                AiCoverTaskResult result = new AiCoverTaskResult();
                result.setCoverTaskId(task.getId());
                result.setTaskId(id);
                result.setStatus(0);
                return result;
            }).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 请求获取结果
     *
     * @param result
     */
    public void getAiResult(AiCoverTaskResult result) {
        HttpGet httpGet = new HttpGet(URI.create("https://ston.6pen.art/release/open-task?id=".concat(result.getTaskId())));
        httpGet.addHeader("ys-api-key", apiKey);
        try {
            CloseableHttpResponse response = HTTP_CLIENT.execute(httpGet);
            String content = IoUtil.read(response.getEntity().getContent(), true).toString("utf-8");
            HttpClientUtils.closeQuietly(response);
            Map<String, Object> map = JSONUtil.parseObject(content, Map.class);
            if (MapUtil.getInt(map, "code") != 200) {
                log.error("请求失败:{}", content);
                return;
            }
            Map<String, Object> dat = (Map<String, Object>) map.get("data");
            result.setEstimate(MapUtil.getInt(dat, "estimate"));
            String state = MapUtil.getStr(dat, "state");
            switch (state) {
                case "success":
                    result.setStatus(1);
                    result.setGenImgRemote(MapUtil.getStr(dat, "gen_img"));
                    result.setUpdateTime(LocalDateTime.now());
                    break;
                case "fail":
                case "cancel":
                    result.setStatus(2);
                    log.error("生图失败: {}", content);
                    result.setUpdateTime(LocalDateTime.now());
                    break;
                default:
                    result.setStatus(0);
                    break;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public byte[] downloadImage(AiCoverTaskResult result) {
        Assert.notBlank(result.getGenImgRemote());
        HttpGet httpGet = new HttpGet(URI.create(result.getGenImgRemote()));
        try {
            CloseableHttpResponse response = HTTP_CLIENT.execute(httpGet);
            byte[] bytes = IoUtil.readBytes(response.getEntity().getContent(), true);
            HttpClientUtils.closeQuietly(response);
            return bytes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 绕过验证
     *
     * @author xz
     */
    private static PoolingHttpClientConnectionManager allTrust() {
        SSLContext sslContext = null;

        PoolingHttpClientConnectionManager connectionManager = null;
        try {
            sslContext = SSLContext.getInstance("TLSv1.2");
            X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
            sslContext.init(null, new TrustManager[]{trustManager}, null);

            //设置http和https对应处理socket链接工厂的对象
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE))
                    .build();
            connectionManager = new PoolingHttpClientConnectionManager(registry);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            log.info("errorLogs->:{}", e);
        }

        return connectionManager;
    }
}
