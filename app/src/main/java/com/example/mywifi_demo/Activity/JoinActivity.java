package com.example.mywifi_demo.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private EditText   mName,mBirth,mphone;
    private Button mjoinjoinBtn;
    private String email;
    private String IdToken;

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

        mTxtId.setText(email);

        mjoinjoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        uploadMember(memberBean);
    }

    public void uploadMember(MemberBean memberBean){
        //Firebase 데이터베이스에 메모를 등록한다.
        DatabaseReference dbRef = mFirebaseDatabase.getReference();
        dbRef.child("Users").child( memberBean.memId ).setValue(memberBean);
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

   /* private static final String TAG = "RegisterActivity";
    private EditText mID, mPassword, mPasswordcheck, mName,mBirth,mphone;
    private Button mjoinjoinBtn;
    public static FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        //액션 바 등록하기
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");

        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기버튼
        actionBar.setDisplayShowHomeEnabled(true); //홈 아이콘

        //파이어베이스 접근 설정
        // user = firebaseAuth.getCurrentUser();
        mAuth =  FirebaseAuth.getInstance();
        //firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        mID = findViewById(R.id.txt_id);
        mPassword = findViewById(R.id.txt_pass);
        mPasswordcheck = findViewById(R.id.txt_passck);
        mName = findViewById(R.id.txt_name);
        mBirth = findViewById(R.id.txt_birth);
        mphone = findViewById(R.id.txt_phone);
        mjoinjoinBtn = findViewById(R.id.btn_join_join);


        //파이어베이스 user 로 접글

        //가입버튼 클릭리스너   -->  firebase에 데이터를 저장한다.
        mjoinjoinBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                //가입 정보 가져오기
                final String id = mID.getText().toString().trim();
                String pwd = mPassword.getText().toString().trim();
                String pwdcheck = mPasswordcheck.getText().toString().trim();

                if(pwd.equals(pwdcheck)) {
                    Log.d(TAG, "등록 버튼 " + id + " , " + pwd);
                    final ProgressDialog mDialog = new ProgressDialog(JoinActivity.this);
                    mDialog.setMessage("가입중입니다...");
                    mDialog.show();

                    //파이어베이스에 신규계정 등록하기
                    mAuth.createUserWithEmailAndPassword(id, pwd).addOnCompleteListener(JoinActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            //가입 성공시
                            if (task.isSuccessful()) {
                                mDialog.dismiss();

                                FirebaseUser user = mAuth.getCurrentUser();
                                String id = mID.getText().toString().trim();
                                String email = user.getEmail();
                               // String uid = user.getUid();
                                String name = mName.getText().toString().trim();
                                String birth = mBirth.getText().toString().trim();
                                String phone = mphone.getText().toString().trim();
                                String password= mPassword.getText().toString().trim();


                                //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장
                                HashMap<Object,String> hashMap = new HashMap<>();

                                //hashMap.put("uid",uid);
                                hashMap.put("email",email);
                                hashMap.put("name",name);
                                hashMap.put("birth",birth);
                                hashMap.put("phone",phone);
                                hashMap.put("id",id);
                                hashMap.put("password",password);

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("Users");
                                reference.child(id).setValue(hashMap);


                                //가입이 이루어져을시 가입 화면을 빠져나감.
                                Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(JoinActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                            } else {
                                mDialog.dismiss();
                                Toast.makeText(JoinActivity.this, "이미 존재하는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                                return;  //해당 메소드 진행을 멈추고 빠져나감.

                            }

                        }
                    });

                    //비밀번호 오류시
                }else{

                    Toast.makeText(JoinActivity.this, "비밀번호가 틀렸습니다. 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }

    public boolean onSupportNavigateUp(){
        onBackPressed();; // 뒤로가기 버튼이 눌렸을시
        return super.onSupportNavigateUp(); // 뒤로가기 버튼
    }
}*/