package com.wafflestudio.siksha.util;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class ParsingJson {
  private Context context;

  public ParsingJson(Context context) {
    this.context = context;
  }

  public RestaurantCrawlingForm[] getParsedForms() {
    StringBuilder stringBuilder = new StringBuilder();

    try {
      FileInputStream fis = context.openFileInput("restaurants.json");
      BufferedReader br = new BufferedReader(new InputStreamReader(fis, "euc-kr"));

      String line;
      while((line = br.readLine()) != null) {
        stringBuilder.append(line);
      }

      return new Gson().fromJson(stringBuilder.toString(), RestaurantCrawlingForm[].class);
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}