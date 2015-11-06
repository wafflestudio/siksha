package com.wafflestudio.siksha.form;

import java.io.Serializable;
import java.util.List;

public class Restaurant implements Serializable {
    public String name;

    public boolean empty;

    public List<Food> foods;
}
