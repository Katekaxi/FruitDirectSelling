package com.example.fruitdirectselling;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fruitdirectselling.DB.MyHelper;

public class MainActivity extends AppCompatActivity {

    private EditText member_name;
    private EditText member_password;
    private CheckBox passwordcheckBox;
    private Button member_login;
    private Button member_register;
    private Button login;
    boolean tiaozhuan = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        member_name = (EditText) findViewById(R.id.yonghuming);
        member_password = (EditText) findViewById(R.id.mima);
        passwordcheckBox = (CheckBox) findViewById(R.id.mimaCheckBox);
        member_login = (Button) findViewById(R.id.member_login);
        member_register = (Button) findViewById(R.id.member_register);
        login = (Button) findViewById(R.id.login);

        //会员跳转水果界面
        member_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Find(member_name.getText().toString().trim());
                if (tiaozhuan) {
                    Intent intent = new Intent(MainActivity.this, FruitActivity.class);
                    intent.putExtra("Ismember",true);
                    startActivity(intent);
                    tiaozhuan = false;
                }
            }
        });

        //游客跳转水果界面
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //给next添加响应事件
                Intent intent=new Intent(MainActivity.this,FruitActivity.class);
                //启动
                startActivity(intent);
            }
        });


        //跳转注册界面
        member_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //给next添加响应事件
                Intent intent=new Intent(MainActivity.this,SecondActivity.class);
                startActivity(intent);
            }
        });

        //密码输入隐藏功能
        passwordcheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    //如果选中，显示密码
                    member_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    //否则隐藏密码
                    member_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    //从数据库中查询
    private void Find(String NAME){
        MyHelper myHelper = new MyHelper(this);
        //获取可读对象
        SQLiteDatabase db = myHelper.getReadableDatabase();
        //调用query函数查询
        Cursor cursor = db.query("member", null, "NAME=?", new String[] {NAME+""}, null, null ,null);
        //判断是否有数据
        member_password = (EditText) findViewById(R.id.mima);
        if (cursor.getCount() != 0){
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex("PASSWORD"));
                if (password.equals(member_password.getText().toString().trim())) {
                    tiaozhuan = true;
                } else {
                    showMsg("密码不正确");
                }
            }
        }else{
            showMsg("用户不存在");
        }
    }

    //用toast实现showMsg方法
    private void showMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}