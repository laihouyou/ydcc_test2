package com.movementinsome.caice.vo;

import java.io.Serializable;
import java.util.List;

/**每一个工程的孩子数据集合
 * Created by zzc on 2017/6/5.
 */

public class StatisticsChileVo implements Serializable {
    //每一天的点数据集合
    public List<SavePointVo> savePointVos;
    //每一天的线数据集合
    public List<SavePointVo> saveLineVos;

    public List<SavePointVo> getSavePointVos() {
        return savePointVos;
    }

    public void setSavePointVos(List<SavePointVo> savePointVos) {
        this.savePointVos = savePointVos;
    }

    public List<SavePointVo> getSaveLineVos() {
        return saveLineVos;
    }

    public void setSaveLineVos(List<SavePointVo> saveLineVos) {
        this.saveLineVos = saveLineVos;
    }
}
