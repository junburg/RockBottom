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

/**
 * 문자열에서 제목과 본문을 나누는 로직 작성
 * Example: String content = "<제목>본문입니다<제목2>본문입니다2<제목3>본문입니다3"
 * => 제목: 본문입니다 제목2: 본문입니다2 제목3: 본문입니다3 (Map form)
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
                // 제목을 변수에 담고 나면 해당 문자열은 제거한다
                index = content.indexOf("<");
                title = content.substring(index, content.indexOf(">")+1);
                content = content.replace(title, "");
                title = title.replace("<", "");
                title = title.replace(">", "");

                if(content.contains("<")) {
                    // content의 마지막 본문이 아닐 경우 ( <>가 있으므로 )
                    body = content.substring(0, content.indexOf("<"));
                    content = content.replace(body, "");
                } else {
                    // content의 마지막 본문 ( <>가 없으므로 )
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
