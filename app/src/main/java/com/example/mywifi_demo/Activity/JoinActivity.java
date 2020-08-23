package com.example.mywifi_demo.Activity;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mywifi_demo.Bean.MemberBean;
import com.example.mywifi_demo.R;
import com.example.mywifi_demo.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class JoinActivity extends AppCompatActivity {
    private static FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private static FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private TextView mTxtId;
    private EditText mName, mBirth, mphone;
    private Button mjoinjoinBtn;
    private RadioGroup radioGroup;
    private String email;
    private String IdToken;
    private String mgender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        Intent intent = getIntent();
        IdToken = intent.getStringExtra("tokenId");
        email = intent.getStringExtra("email");
        mTxtId = findViewById(R.id.txtId);
        mName = findViewById(R.id.txt_name);
        mBirth = findViewById(R.id.txt_birth);
        mphone = findViewById(R.id.txt_phone);
        mjoinjoinBtn = findViewById(R.id.btn_join_join);
        radioGroup  = (RadioGroup) findViewById(R.id.rg);

        mTxtId.setText(email);

        mjoinjoinBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //성별 선택
                int id=radioGroup.getCheckedRadioButtonId();
                //getcheckedRadioButton리턴값은 선택된 RadioButton
                RadioButton rb=(RadioButton)findViewById(id);
                mgender=rb.getText().toString();
                joinProcess();

            }
        });


    } //end onCreate()





    //회원가입 처리
    private void joinProcess() {
        MemberBean memberBean = new MemberBean();
        //데이터베이스에 저장한다.

        memberBean.memId = Utils.getUserIdFromUUID(email);
        memberBean.isAdmin = false;
        memberBean.memName = mName.getText().toString();
        memberBean.membirth = mBirth.getText().toString();
        memberBean.memphone = mphone.getText().toString();
        memberBean.memgender= mgender;
        uploadMember(memberBean);
    }

    public void uploadMember(MemberBean memberBean){
        //Firebase 데이터베이스에 메모를 등록한다.
        DatabaseReference dbRef = mFirebaseDatabase.getReference();
        dbRef.child("Users").child( memberBean.memName ).setValue(memberBean);
        //파이어 베이스 인증
        firebaseAuthWithGoogle(IdToken);
    }

    private void firebaseAuthWithGoogle(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //Toast.makeText(getBaseContext(), "Firebase 로그인 성공",Toast.LENGTH_LONG).show();

                    goRealUserMainActivity();
                }else{
                    Toast.makeText(getBaseContext(), "Firebase 로그인 실패",
                            Toast.LENGTH_LONG).show();
                    Log.w("Test","인증실패: "+task.getException());
                }
            }
        });
    }  //end firebaseAuthWithGoogle()


    private void goRealUserMainActivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }  // end goMainActivity (User activity)

}

