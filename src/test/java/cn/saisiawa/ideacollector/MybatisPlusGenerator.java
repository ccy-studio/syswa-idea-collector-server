package cn.saisiawa.ideacollector;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;

import java.util.List;

/**
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/1/31 14:40
 * @Version：1.0
 */
public class MybatisPlusGenerator {

    private static final List<String> GENERATOR_TABLES = List.of(
            "T_AI_COVER_TASK",
            "T_AI_COVER_TASK_RESULT",
            "T_ARTICLE",
            "T_ARTICLE_COMMENT",
            "T_ARTICLE_INFO",
            "T_USER"
    );

    public static void main(String[] args) {
        DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder("jdbc:h2:~/h2_database_java;MODE=MySQL", "root", "root").build();
        AutoGenerator generator = new AutoGenerator(dataSourceConfig);
        generator.strategy(new StrategyConfig.Builder()
                .addTablePrefix("T_")
                .addInclude(GENERATOR_TABLES)
                .entityBuilder()
                .enableFileOverride()
                .disableSerialVersionUID()
                .enableTableFieldAnnotation()
                .idType(IdType.AUTO)
                .enableLombok()
                .build());
        generator.template(new TemplateConfig.Builder()
                .disable(TemplateType.CONTROLLER, TemplateType.SERVICE, TemplateType.SERVICE_IMPL)
                .build());
        generator.global(new GlobalConfig.Builder()
                .outputDir("./generatorOutput")
                .author("Saisaiwa")
                .disableOpenDir()
                .build());
        generator.packageInfo(new PackageConfig.Builder()
                .parent("cn.saisiawa.ideacollector")
                .entity("domain.entity")
                .mapper("mapper")
                .build());
        generator.execute();
    }

}
