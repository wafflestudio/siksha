package com.wafflestudio.siksha.form;

import java.io.Serializable;
import java.util.List;

public class Menu implements Serializable {
    public String restaurant;

    public boolean empty;

    public List<Food> foods;
}
