package com.example.mywifi_demo.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mywifi_demo.Bean.MemberBean;
import com.example.mywifi_demo.Database.FileDB;
import com.example.mywifi_demo.Adapter.MemberAdapter;
import com.example.mywifi_demo.R;
import com.example.mywifi_demo.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class LoginActivity extends AppCompatActivity{
    //구글 로그인 클라이언트 제어자
    public static GoogleSignInClient mGoogleSignInClient;
    //FireBase 인증객체
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase mFirebaseDB = FirebaseDatabase.getInstance();
    private List<MemberBean> mMemberList;
    private MemberAdapter mMemberAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.signInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });

        //구글 로그인 객체선언
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

    } //end onCreate()


    protected void onResume() {

        super.onResume();

//        if(mFirebaseAuth.getCurrentUser() != null && mFirebaseAuth.getCurrentUser().getEmail()!= null ){
//            Toast.makeText(this, "로그인 성공 - 메인화면 이동", Toast.LENGTH_LONG).show();
//            goMainActivity();
//        }

    }

    private void googleSignIn(){
        Intent i = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(i, 1004);
    }  //end googleSignIn()

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //구글 로그인 버튼 응답
        if (requestCode == 1004) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //구글 로그인 성공
                final GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(getBaseContext(), "구글 로그인에 성공 하였습니다.", Toast.LENGTH_LONG).show();

                final String loginedEmail = account.getEmail();

                //DB 에서 이메일 조회
                mFirebaseDB.getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        int count = 0;
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            try {
                                MemberBean bean = snapshot.getValue(MemberBean.class);
                                String UUIDEmail = Utils.getUserIdFromUUID(loginedEmail);
                                if (TextUtils.equals(bean.memId, UUIDEmail)) {
                                    //FileDB memberBean 값을 저장
                                    FileDB.setLoginMember(getApplicationContext(), bean);

                                    //Firebase 인증 - true: 관리자, false: 유저
                                    firebaseAuthWithGoogle(account, bean.isAdmin);
                                    count = 1;
                                    break;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if(count == 0) {
                            //회원가입 Activity 로 이동
                            Intent i = new Intent(LoginActivity.this, JoinActivity.class);
                            i.putExtra("email", loginedEmail);
                            i.putExtra("tokenId", account.getIdToken());
                            startActivity(i);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });

            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }  //end onActivityResult()

    private void firebaseAuthWithGoogle(GoogleSignInAccount account, final boolean isAdmin){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //Toast.makeText(getBaseContext(), "Firebase 로그인 성공",Toast.LENGTH_LONG).show();
                    if(isAdmin == false){
                        goRealUserMainActivity(); //유저
                    }else{
                        goRealAdminMainActivity(); //관리자
                    }
                }else{
                    Toast.makeText(getBaseContext(), "Firebase 로그인 실패",
                            Toast.LENGTH_LONG).show();
                    Log.w("Test","인증실패: "+task.getException());
                }
            }
        });
    }  //end firebaseAuthWithGoogle()

    //게시판 메인 화면으로 이동한다
    private void goRealUserMainActivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }  // end goUserMainActivity

    private void goRealAdminMainActivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    } // end goAdminMainActivity

}
