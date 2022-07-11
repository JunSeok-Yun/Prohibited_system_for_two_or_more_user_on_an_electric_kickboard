package com.example.login_for_board;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class register extends AppCompatActivity {
    private EditText et_id, et_pass, et_name, et_age, et_weight;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // 액티비티 시작시 처음으로 실행되는 생명주기
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ArrayList<String> ID = new ArrayList<>();
        ArrayList<String> PWD = new ArrayList<>();
        ArrayList<String> NAME = new ArrayList<>();
        ArrayList<Integer> AGE = new ArrayList<>();
        ArrayList<Integer> WEIGHT = new ArrayList<>();

        // 아이디 값 찾아주기
        et_id = findViewById(R.id.register_id);
        et_pass = findViewById(R.id.register_pass);
        et_name = findViewById(R.id.register_name);
        et_age = findViewById(R.id.register_age);
        et_weight = findViewById(R.id.register_weight);

        // 회원가입 버튼 클릭 시 수행
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EditText에 현재 입력되어있는 값을 get(가져온다)해온다.
                String userID = et_id.getText().toString();
                String userPass = et_pass.getText().toString();
                String userName = et_name.getText().toString();
                int userAge = Integer.parseInt(et_age.getText().toString());
                int userWeight = Integer.parseInt(et_weight.getText().toString());

                ID.add(userID);
                PWD.add(userPass);
                NAME.add(userName);
                AGE.add(userAge);
                WEIGHT.add(userWeight);

                Toast.makeText(getApplicationContext(), "회원 등록에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(register.this, LoginActivity.class);
                intent.putExtra("ID",userID);
                intent.putExtra("PWD",userPass);
                intent.putExtra("WEIGHT",userWeight);
                startActivity(intent);


                //System.out.println("id :" + ID.get(1));

            }
        });
    }
}