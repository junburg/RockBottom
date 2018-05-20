package com.junburg.moon.rockbottom.util;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by Junburg on 2018. 4. 18..
 */

/**
 * 파일 경로를 읽어서 String으로 반환
 */
public class GetPath {

    private Context context;

    public GetPath(Context context) {
        this.context = context;
    }

    public String getPathUri(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(context, uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(index);
    }

}
