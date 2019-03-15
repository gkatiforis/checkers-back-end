package com.katiforis.top10.DTO.game;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CurrentTimeDTO implements Serializable {

    private Date currentDate;

    public CurrentTimeDTO() {
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }
}
