package cn.saisiawa.ideacollector.conf;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;

/**
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/11 12:01
 * @Version：1.0
 */
@Slf4j
@Service
@AutoConfigureAfter(DataSource.class)
public class H2DataSourceConfig {

    private static final String SCHEMA = "classpath:db/schema-h2.sql";

    @Value("${spring.h2.local:}")
    private String h2DataBasePath;

    @Resource
    private DataSource dataSource;

    @Resource
    private ApplicationContextRegister applicationContextRegister;

    @PostConstruct
    public void init() throws Exception {
        //初始化本地数据库 //获取系统用户目录
        String userHome = System.getProperty("user.home");
        if (StrUtil.isNotBlank(h2DataBasePath)) {
            FileUtil.mkdir(h2DataBasePath);
            userHome = h2DataBasePath;
        }
        log.info("Save To H2 Local h2DataBasePath:{}", userHome);
        // 创建一个标识文件,只有在第一次初始化数据库时会创建,如果系统用户目录下有这个文件,就不会重新执行sql脚本
        File f = new File(userHome + File.separator + "h2_java.lock");
        if (!f.exists()) {
            log.info("--------------初始化h2数据库----------------------");
            f.createNewFile();
            ScriptUtils.executeSqlScript(dataSource.getConnection(), applicationContextRegister.getResource(SCHEMA));
        }
    }

}
