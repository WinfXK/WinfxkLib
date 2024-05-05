package com.winfxk.winfxklia;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.winfxk.winfxklia.dialog.MyBuilder;

/**
 * author: Winfx
 * date: 2024/5/3 22:54
 */
public class WinfxkliaActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_activity);
        MyBuilder dialog = new MyBuilder(this);
        dialog.setTitle("提示");
        dialog.setMessage("这是一条提示信息");
        dialog.addButton("确定");
        dialog.addButton("取消");
        dialog.show();
    }
}
