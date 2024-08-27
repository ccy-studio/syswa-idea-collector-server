package cn.saisiawa.ideacollector.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/30 9:36
 * @Version：1.0
 */
@AllArgsConstructor
@Getter
public enum ImageBizType {

    /**
     * AI生图
     */
    AI_GENERATOR("AI", "ai"),

    /**
     * 用户上传
     */
    USER_UPLOAD("USER", "user");


    private final String typeCode;

    private final String filePrefix;


}
