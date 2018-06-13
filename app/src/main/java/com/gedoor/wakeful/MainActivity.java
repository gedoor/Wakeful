package com.gedoor.wakeful;

import android.content.Intent;
import android.net.Uri;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.vw_zfb_tz)
    CardView vwZfbTz;
    @BindView(R.id.vw_zfb_hb)
    CardView vwZfbHb;
    @BindView(R.id.vw_zfb_rwm)
    CardView vwZfbRwm;
    @BindView(R.id.vw_wx_rwm)
    CardView vwWxRwm;
    @BindView(R.id.vw_qq_rwm)
    CardView vwQqRwm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        ignoreBatteryOptimization();
    }

    // 添加菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //菜单
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        vwZfbTz.setOnClickListener(view -> Donate.aliDonate(this));
        vwZfbHb.setOnClickListener(view -> openIntent(Intent.ACTION_VIEW,
                "https://render.alipay.com/p/f/fd-j6lzqrgm/guiderofmklvtvw.html?shareId=2088002567472225&campStr=p1j%2BdzkZl018zOczaHT4Z5CLdPVCgrEXq89JsWOx1gdt05SIDMPg3PTxZbdPw9dL&sign=OqN%2FEToAWmYOBperSICGSOtBs36JILq1%2BshguP6l51U%3D&scene=offlinePaymentNewSns"));
        vwZfbRwm.setOnClickListener(view -> openIntent(Intent.ACTION_VIEW, "https://gedoor.github.io/MyBookshelf/zfbskrwm.jpg"));
        vwWxRwm.setOnClickListener(view -> openIntent(Intent.ACTION_VIEW, "https://gedoor.github.io/MyBookshelf/wxskrwm.jpg"));
        vwQqRwm.setOnClickListener(view -> openIntent(Intent.ACTION_VIEW, "https://gedoor.github.io/MyBookshelf/qqskrwm.jpg"));
    }

    private void openIntent(String intentName, String address) {
        try {
            Intent intent = new Intent(intentName);
            intent.setData(Uri.parse(address));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.can_not_open, Toast.LENGTH_SHORT).show();
        }
    }

    private void ignoreBatteryOptimization() {

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);

        boolean hasIgnored = powerManager.isIgnoringBatteryOptimizations(getPackageName());
        //  判断当前APP是否有加入电池优化的白名单，如果没有，弹出加入电池优化的白名单的设置对话框。
        if(!hasIgnored) {
            try {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
