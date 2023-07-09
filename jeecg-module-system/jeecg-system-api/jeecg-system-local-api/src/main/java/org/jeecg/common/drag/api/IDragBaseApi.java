package org.jeecg.common.drag.api;

/**
 * 仪表盘API接口
 *
 * @author lsq
 * @date 2023/1/9
 */
public interface IDragBaseApi {


    /**
     * 通过id删除仪表盘
     *
     * @param id
     * @return
     */
    void deleteDragPage(String id);

    /**
     * 通过id复制仪表盘
     *
     * @param id
     * @return id
     */
    String copyDragPage(String id);
}
