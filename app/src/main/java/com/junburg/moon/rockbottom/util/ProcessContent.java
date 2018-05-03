package com.junburg.moon.rockbottom.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by Junburg on 2018. 4. 23..
 */

public class ProcessContent {

    private String title;
    private String body;
    private int index;
    private Map<String, String> contentMap;

    public  Map<String, String> divideContent(String content) {
        contentMap = new HashMap<>();

        for (int i=0; i<content.length() ; i++) {
            if (content!="") {
                index = content.indexOf("<");
                title = content.substring(index, content.indexOf(">")+1);
                content = content.replace(title, "");
                title = title.replace("<", "");
                title = title.replace(">", "");

                if(content.contains("<")) {
                    body = content.substring(0, content.indexOf("<"));
                    content = content.replace(body, "");
                } else {
                    body = content.substring(0, content.length());
                    content = content.replace(body, "");
                }
                contentMap.put(title, body);
            } else {
                break;
            }
        }

        return contentMap;
    }
}
