package com.example.fruitdirectselling;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.fruitdirectselling.DB.MyHelper;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecondActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private EditText et_name;
    private EditText et_pass1;
    private EditText et_pass2;
    private CheckBox mimaCheckBox1;
    private CheckBox mimaCheckBox2;
    private Button zhuce;
    private EditText phone;
    private EditText email;
    private TextView information_sex;
    private TextView information_birthday;
    private Button clear;
    boolean tiaozhuan = false;
    String[] sexArry = {"男","保密","女"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        et_name = findViewById(R.id.yonghuming);
        et_pass1 = findViewById(R.id.mima1);
        et_pass2 = findViewById(R.id.mima2);
        mimaCheckBox1 = findViewById(R.id.mimaCheckBox1);
        mimaCheckBox2 = findViewById(R.id.mimaCheckBox2);
        phone = findViewById(R.id.shoujihao);
        email = findViewById(R.id.youxiang);
        information_sex = findViewById(R.id.information_sex);
        information_birthday = findViewById(R.id.information_birthday);
        clear = findViewById(R.id.clear);
        zhuce = findViewById(R.id.zhuce);
/*
        phone.setInputType(InputType.TYPE_CLASS_PHONE);
*/
        //两个密码输入隐藏功能
        mimaCheckBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    //如果选中，显示密码
                    et_pass1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    //否则隐藏密码
                    et_pass1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        mimaCheckBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    //如果选中，显示密码
                    et_pass2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    //否则隐藏密码
                    et_pass2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //注册功能
        zhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chkAndSaveData();
                if(tiaozhuan){
                    String uname = et_name.getText().toString().trim();
                    String upass = et_pass1.getText().toString().trim();
                    String usex = information_sex.getText().toString().trim();
                    String ubirthday = information_birthday.getText().toString().trim();
                    String uemail = email.getText().toString().trim();
                    String uphone = phone.getText().toString().trim();
                    Add(uname, upass, usex, ubirthday, uemail ,uphone);
                    showMsg("注册成功");
                }
            }
        });

        //生日日期
        information_birthday.setOnClickListener(v -> {//生日点击弹出日期选择框
            Calendar calendar = Calendar.getInstance();//获取Calendar实例
            //创建日期选择器
            DatePickerDialog dialog = new DatePickerDialog(this, this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();//窗口弹出
        });


        //清除功能
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout layout = findViewById(R.id.container);
                int childCount = layout.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    if (!(layout.getChildAt(i) instanceof Button)) {
                        LinearLayout layout1 = (LinearLayout) layout.getChildAt(i);
                        for (int j = 0; j < layout1.getChildCount(); j++) {
                            if (layout1.getChildAt(j) instanceof EditText) {
                                EditText editText = ((EditText) layout1.getChildAt(j));
                                editText.setText("");
                            }
                        }
                    }
                }
            }
        });

        //性别
        information_sex.setOnClickListener(v -> {//性别点击后弹出性别选择框
            AlertDialog.Builder builder3 = new AlertDialog.Builder(this);// 自定义对话框
            // checkedItem默认的选中 setSingleChoiceItems设置单选按钮组
            builder3.setSingleChoiceItems(sexArry, 1, (dialog, which) -> {// which是被选中的位置
                // showToast(which+"");
                information_sex.setText(sexArry[which]);
                dialog.dismiss();// 随便点击一个item消失对话框，不用点击确认取消
            });
            builder3.show();// 让弹出框显示
        });

        //邮箱
        email.addTextChangedListener(new EmailWather(email));
        //手机号
        phone.addTextChangedListener(new PhoneWather(phone));
    }


    //实现注册功能中的 chkAndSaveData方法
    private void chkAndSaveData() {
        int i = 0;
        if(TextUtils.isEmpty(et_name.getText().toString().trim())){
            showMsg("请输入用户名");
        }else {
            i = i+1;
        }if(Find(et_name.getText().toString().trim())){
            showMsg("用户已存在");
        }else {
            i = i+1;
        }
        if(TextUtils.isEmpty(et_pass1.getText().toString().trim())){
            showMsg("请输入密码");
        }else {
            i = i+1;
        }
        if(!et_pass1.getText().toString().trim().equals(et_pass2.getText().toString().trim())){
            showMsg("密码不一致");
        }else {
            i = i+1;
        }
        if(TextUtils.isEmpty(information_sex.getText().toString().trim())){
            showMsg("请选择性别");
        }else {
            i = i+1;
        }
        if(information_birthday.getText().toString().trim().equals("点击选择")){
            showMsg("请选择生日");
        }else {
            i = i+1;
        }
        if(TextUtils.isEmpty(email.getText().toString().trim())){
            showMsg("请输入邮箱");
        }else {
            i = i+1;
        }
        if(TextUtils.isEmpty(phone.getText().toString().trim())){
            showMsg("请输入手机号");
        }else {
            i = i+1;
        }

        if(i == 8){
            tiaozhuan = true;
        }else {
            showMsg("注册失败");
        }
    }
    //用toast实现showMsg方法
    private void showMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    //数据库添加数据
    private void Add(String NAME, String PASSWORD, String SEX, String BIRTHDAY, String EMAIL, String PHONE  ){
        //创建MyHelper类
        MyHelper myHelper = new MyHelper(this);
        //获取可写对象
        SQLiteDatabase db = myHelper.getWritableDatabase();
        //创建ContentValues对象存放数据
        ContentValues values = new ContentValues();
        //添加数据
        values.put("NAME",NAME);
        values.put("PASSWORD",PASSWORD);
        values.put("SEX",SEX);
        values.put("BIRTHDAY",BIRTHDAY);
        values.put("EMAIL",EMAIL);
        values.put("PHONE",PHONE);
        //将values数据插入到表(data)中
        db.insert("member",null,values);
        //关闭操作
        db.close();
    }

    //从数据库中查询
    private boolean Find(String NAME){
        MyHelper myHelper = new MyHelper(this);
        //获取可读对象
        SQLiteDatabase db = myHelper.getReadableDatabase();
        //调用query函数查询
        Cursor cursor = db.query("member", null, "NAME=?", new String[] {NAME+""}, null, null ,null);
        //判断是否有数据
        if (cursor.getCount() != 0){
            return true;
        }else{
            return false;
        }
    }


    //生日日期
    @Override
    //日期选择完事件
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String desc = String.format("%d-%d-%d", year, month + 1, dayOfMonth);
        information_birthday.setText(desc);//设置生日
    }


    //邮箱
    private class EmailWather implements TextWatcher {

        //监听改变的文本框
        private EditText editText1;

        public EmailWather(EditText email) {
            this.editText1 = email;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(isMailbox(email.getText().toString()))
                zhuce.setEnabled(true);
            else{
                email.setError("邮箱格式错误！");
                zhuce.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }

        public boolean isMailbox(String mailbox) {
            String str = mailbox;
            String pattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(str);
            return m.matches();
        }
    }


    //手机号
    private class PhoneWather implements TextWatcher {

        //监听改变的文本框
        private EditText editText2;

        public PhoneWather(EditText phone) {
            this.editText2 = phone;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(isMobile(phone.getText().toString()))
                zhuce.setEnabled(true);
            else{
                phone.setError("手机号格式错误！");
                zhuce.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }

        public boolean isMobile(String mobile) {
            String str = mobile;
            String pattern = "^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(str);
            return m.matches();
        }

    }

}