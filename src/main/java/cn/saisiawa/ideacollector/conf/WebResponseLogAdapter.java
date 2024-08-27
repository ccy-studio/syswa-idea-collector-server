package cn.saisiawa.ideacollector.conf;

import cn.saisiawa.ideacollector.common.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class WebResponseLogAdapter implements ResponseBodyAdvice<Object> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (!ignoreCheck(request.getURI().getPath()) && !(body instanceof InputStreamResource)) {
            logger.info("RSP: " + JSONUtil.toJSONString(body));
        }
        return body;
    }

    public boolean ignoreCheck(String path) {
        return path.equals("/ok");
    }

}
