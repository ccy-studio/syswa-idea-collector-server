package cn.saisiawa.ideacollector.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/30 16:43
 * @Version：1.0
 */
@Setter
@Getter
public class AiTaskInfoVo {

    /**
     * ID
     */
    private Long id;


    /**
     * 任务状态：0执行中,1已结束
     */
    private Integer status;


    /**
     * 图片任务
     */
    private List<AiTaskItem> items;


    @Setter
    @Getter
    public static class AiTaskItem {
        private Long id;

        /**
         * 任务ID
         */
        private String taskId;


        /**
         * 预计生成时间/秒
         */
        private Integer estimate;

        /**
         * 任务状态：0执行中,1生成成功,2生成失败
         */
        private Integer status;

        /**
         * 保存本机图片路径
         */
        private String genImgLocal;
    }
}
