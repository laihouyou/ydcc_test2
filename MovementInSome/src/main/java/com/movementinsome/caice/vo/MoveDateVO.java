package com.movementinsome.caice.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by LJQ on 2017/3/15.
 */

@DatabaseTable(tableName = "MoveDateVO")
public class MoveDateVO implements Serializable {

    private static final long serialVersionUID = 6178156405114184270L;

    @DatabaseField
    private String MoveDate;

    @DatabaseField
    private Long time;

    public String getMoveDate() {
        return MoveDate;
    }

    public void setMoveDate(String moveDate) {
        MoveDate = moveDate;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
