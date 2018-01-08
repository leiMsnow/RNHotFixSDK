package com.demo.hotfix;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ray.rn.hotfix.HotFix;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HotFix.init(this);
    }
}
