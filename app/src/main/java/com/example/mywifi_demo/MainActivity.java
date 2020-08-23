package com.example.mywifi_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button sendbt;
    //    private Button getdbt;
    private EditText editdt;
    public String msg;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private ChildEventListener mChild;
    private final int GET_GALLERY_IMAGE = 200;
    private ImageView imageview;
    private DatabaseReference mPostReference;
//    private ListView listView;
//    private ArrayAdapter<String> adapter;

    Button btn_Update;
    Button btn_Insert;
    EditText edit_id;
    EditText edit_name;
    EditText edit_gender;
    EditText edit_birth;
    EditText edit_cmkg;


   private String id;
   private String name;
   private Long birth;
   private String gender = "";
   private String cmkg;



//    String sort = "id";

    ArrayAdapter<String> arrayAdapter;

    static ArrayList<String> arrayIndex = new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        getdbt = (Button) findViewById(R.id.output);
//        editdt = (EditText) findViewById(R.id.edit_id);
        imageview = (ImageView) findViewById(R.id.imageView);
        btn_Insert = (Button) findViewById(R.id.btn_insert);
        btn_Insert.setOnClickListener(this);
        btn_Update = (Button) findViewById(R.id.btn_update);
        btn_Update.setOnClickListener(this);
        edit_id = (EditText) findViewById(R.id.edit_id);
        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_birth = (EditText) findViewById(R.id.edit_birth);
        edit_gender = (EditText) findViewById(R.id.edit_gender);
        edit_cmkg = (EditText) findViewById(R.id.edit_cmkg);

        btn_Insert.setEnabled(true);
        btn_Update.setEnabled(false);

//        initDatabase();

//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
//        listView.setAdapter(adapter);

        //       List<Object> Array = new ArrayList<Object>();

/////////////////기존 단일 데이터 입력시 코드(~200807)//////////////////////////////////

//////////////////이미지를 누르면 사진을 등록하는 코드/////////////////////////////
        imageview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });
    }
    public void setInsertMode() {

        edit_id.setText("ID");
        edit_name.setText("NAME");
        edit_birth.setText("BIRTH");
        edit_gender.setText("FEMALE/MALE");
        edit_cmkg.setText("cm/kg");

       edit_id.setText("");
        edit_name.setText("");
        edit_birth.setText("");
        edit_gender.setText("");
        edit_cmkg.setText("");

        btn_Insert.setEnabled(true);
        btn_Update.setEnabled(false);
    }

    public boolean IsExistID(){
        boolean IsExist = arrayIndex.contains(id);
        return IsExist;
    }

    public void postFirebaseDatabase(boolean add){
        mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            FirebasePost post = new FirebasePost(id, name, birth, gender, cmkg);
            postValues = post.toMap();
        }
        childUpdates.put("/id_list/" + id, postValues);
        mPostReference.updateChildren(childUpdates);
    }

///////////////////////////////////갤러리 접근/////////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
            imageview.setImageURI(selectedImageUri);

        }

    }
///////////////////////////////////////////////////////////////////////////////
public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_insert:
                id=edit_id.getText().toString();
                name=edit_name.getText().toString();
                birth=Long.parseLong(edit_birth.getText().toString());
                gender=edit_gender.getText().toString();
                cmkg=edit_cmkg.getText().toString();
                if(!IsExistID()){
                    postFirebaseDatabase(true);

                  //  getFirebaseDatabase();

                    setInsertMode();
                }else{
                    Toast.makeText(MainActivity.this, "이미 존재하는 ID, 사용불가", Toast.LENGTH_SHORT).show();
                }
                edit_id.requestFocus();
                edit_id.setCursorVisible(true);
                break;

            case R.id.btn_update:
                id=edit_id.getText().toString();
                name=edit_name.getText().toString();
                birth=Long.parseLong(edit_birth.getText().toString());
                gender=edit_gender.getText().toString();
                cmkg=edit_cmkg.getText().toString();
                postFirebaseDatabase(true);
//                getFirebaseDatabase();
                setInsertMode();
                edit_id.setEnabled(true);
                edit_id.requestFocus();
                edit_id.setCursorVisible(true);
                break;


        }
}
}

//    private void initDatabase() {
//        mDatabase = FirebaseDatabase.getInstance();
//
//        mReference = mDatabase.getReference("log");
//        mReference.child("log").setValue("check");
 //   }


