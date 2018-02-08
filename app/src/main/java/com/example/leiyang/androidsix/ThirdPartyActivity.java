package com.example.leiyang.androidsix;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by 24436 on 2018/2/8.
 */
@RuntimePermissions
public class ThirdPartyActivity extends AppCompatActivity {

    private Button btn_call;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thirdparty_layout);
        btn_call = findViewById(R.id.btn_call_third);
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThirdPartyActivityPermissionsDispatcher.callWithCheck(ThirdPartyActivity.this);
            }
        });
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    //在需要权限的地方注释
    void call(){
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + "10086");
        intent.setData(data);
        try{
            startActivity(intent);
        } catch (SecurityException e){
            e.printStackTrace();
        }
    }

    @OnShowRationale(Manifest.permission.CALL_PHONE)
    //提示用户为何要开启此权限
    void showWhy(final PermissionRequest request){
        new AlertDialog.Builder(this).setMessage("由于需要拨打电话，请按照提示开启电话相关的权限")
                .setTitle("提示用户为何要开启此权限")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        request.proceed();//再次执行权限请求
                    }
                }).show();
    }

    @OnPermissionDenied(Manifest.permission.CALL_PHONE)
    //用户选择拒绝时的提示
    void showDenied(){
        Toast.makeText(this, "用户选择拒绝时的提示", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.CALL_PHONE)
    //用户选择不再询问时的提示
    void showNotAgain(){
        Toast.makeText(this, "无法获取权限", Toast.LENGTH_SHORT).show();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("你的操作需要电话权限，不开启将无法拨打电话！你可以这样开启权限:\n设置--应用管理--权限--应用股权管理")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
        dialog.show();
    }

}
