package com.gedoor.wakeful;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    @BindView(R.id.vw_zfb_hb_kl)
    CardView vwZfbHbKl;
    @BindView(R.id.llDefaultWakefulTime)
    LinearLayout llDefaultWakefulTime;
    @BindView(R.id.tvDefaultWakefulTime)
    TextView tvDefaultWakefulTime;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
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
        vwZfbHb.setOnClickListener(view -> openIntent(Intent.ACTION_VIEW, "https://gedoor.github.io/MyBookshelf/zfbhbrwm.png"));
        vwZfbRwm.setOnClickListener(view -> openIntent(Intent.ACTION_VIEW, "https://gedoor.github.io/MyBookshelf/zfbskrwm.jpg"));
        vwWxRwm.setOnClickListener(view -> openIntent(Intent.ACTION_VIEW, "https://gedoor.github.io/MyBookshelf/wxskrwm.jpg"));
        vwQqRwm.setOnClickListener(view -> openIntent(Intent.ACTION_VIEW, "https://gedoor.github.io/MyBookshelf/qqskrwm.jpg"));
        vwZfbHbKl.setOnClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText(null, "支付宝发红包啦！即日起还有机会额外获得余额宝消费红包！长按复制此消息，打开最新版支付宝就能领取！dlwvHh22lu");
            if (clipboard != null) {
                clipboard.setPrimaryClip(clipData);
                Toast.makeText(this, R.string.copy_complete, Toast.LENGTH_SHORT).show();
            }
            try {
                PackageManager packageManager = this.getApplicationContext().getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage("com.eg.android.AlipayGphone");
                startActivity(intent);
            }catch (Exception e) {
                Toast.makeText(this, "打开支付宝失败,请手动打开支付宝", Toast.LENGTH_SHORT).show();
            }
        });
        setTimeView(preferences.getInt("defaultWakefulTime", 1));
        llDefaultWakefulTime.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("选择默认亮屏时间");
            builder.setItems(getResources().getStringArray(R.array.timeShow), (dialogInterface, i) -> {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("defaultWakefulTime", i);
                editor.apply();
                setTimeView(i);
            });
            builder.show();
        });
    }

    private void setTimeView(int defaultWakefulTime) {
        tvDefaultWakefulTime.setText(getResources().getStringArray(R.array.timeShow)[defaultWakefulTime]);
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

        boolean hasIgnored = false;
        if (powerManager != null) {
            hasIgnored = powerManager.isIgnoringBatteryOptimizations(getPackageName());
        }
        //  判断当前APP是否有加入电池优化的白名单，如果没有，弹出加入电池优化的白名单的设置对话框。
        if(!hasIgnored) {
            try {
                @SuppressLint("BatteryLife") Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
