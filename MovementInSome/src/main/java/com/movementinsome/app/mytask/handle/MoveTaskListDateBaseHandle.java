package com.movementinsome.app.mytask.handle;

import java.util.List;

/**
 * Created by LJQ on 2017/5/19.
 */

public interface MoveTaskListDateBaseHandle<T> {

    //更新任务列表
    List<T> movedateList(String type);
}
