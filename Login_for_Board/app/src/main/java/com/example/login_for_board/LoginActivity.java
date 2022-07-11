package com.example.login_for_board;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText et_id, et_pass;
    private Button btn_login, btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_id = findViewById(R.id.login_id);
        et_pass = findViewById(R.id.login_password);
        btn_login = findViewById(R.id.login_button);
        btn_register = findViewById(R.id.register_button);


        Intent intent = getIntent();
        String ID = intent.getStringExtra("ID");
        String PWD = intent.getStringExtra("PWD");
        int WEIGHT = intent.getIntExtra("WEIGHT",50);
        int userWeight = WEIGHT;



        et_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if(editable.length() > 5)
                {
                    btn_login.setClickable(true);
                    btn_login.setBackgroundColor(Color.BLUE);
                }
                else
                {
                    btn_login.setClickable(false);
                    btn_login.setBackgroundColor(Color.GRAY);

                }

            }
        });


        // 회원가입 버튼을 클릭 시 수행
        btn_register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(LoginActivity.this, register.class);
                startActivity(intent);
            }
        });

        // 로그인 버튼을 클릭시 수행
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EditText에 현재 입력되어있는 값을 get(가져온다)해온다.
                String userID = et_id.getText().toString();
                String userPWD = et_pass.getText().toString();

                if (ID.equals(userID) && PWD.equals(userPWD) ){ // 로그인에 성공한경우
                    Toast.makeText(getApplicationContext(),"로그인에 성공하였습니다.",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this,RunActivity.class);
                    intent.putExtra("ID",userID);
                    intent.putExtra("weight",userWeight);
                    startActivity(intent);
                }
                else
                    Toast.makeText(getApplicationContext(),"입력한 아이디가 없습니다.",Toast.LENGTH_SHORT).show();



            }
        });

    }
}