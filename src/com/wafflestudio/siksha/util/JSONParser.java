package com.wafflestudio.siksha.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.wafflestudio.siksha.form.InformationJSON;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class JSONParser {
    public static <T> T parseJSONFile(Context context, Class<T> type) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader br;

            if (type == InformationJSON.class) {
                File file = new File(context.getFilesDir(), "informations.json");

                if (file.exists()) {
                    FileInputStream fis = context.openFileInput("informations.json");
                    br = new BufferedReader(new InputStreamReader(fis, "euc-kr"));
                }
                else {
                    InputStream is = context.getAssets().open("informations.json");
                    br = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                }
            }
            else {
                FileInputStream fis = context.openFileInput("menus.json");
                br = new BufferedReader(new InputStreamReader(fis, "euc-kr"));
            }

            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Gson().fromJson(stringBuilder.toString(), type);
    }
}