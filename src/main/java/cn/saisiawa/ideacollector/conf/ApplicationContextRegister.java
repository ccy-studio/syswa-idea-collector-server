package cn.saisiawa.ideacollector.conf;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.core.io.Resource;

/**
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/11 12:00
 * @Version：1.0
 */
@Component
public class ApplicationContextRegister implements ApplicationContextAware {

    private ApplicationContext applicationContext = null;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 提供一个方法,用于加载sql脚本文件
     *
     * @param url sql文件位置
     * @return
     */
    public Resource getResource(String url) {
        return this.applicationContext.getResource(url);
    }
}
