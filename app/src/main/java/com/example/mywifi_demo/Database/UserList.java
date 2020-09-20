package com.example.mywifi_demo.Database;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mywifi_demo.Activity.MainActivity;
import com.example.mywifi_demo.FirebasePost;
import com.example.mywifi_demo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserList extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference mPostReference;
    CheckBox check_userid;
    CheckBox check_name;
    CheckBox check_age;
    String sort = "id";
    long age;
    String gender = "";

    ArrayAdapter<String> arrayAdapter;

    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userlist);
        check_userid = (CheckBox) findViewById(R.id.check_userid);
        check_userid.setOnClickListener(this);
        check_name = (CheckBox) findViewById(R.id.check_name);
        check_name.setOnClickListener(this);
        check_age = (CheckBox) findViewById(R.id.check_age);
        check_age.setOnClickListener(this);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        ListView listView = (ListView) findViewById(R.id.db_list_view);
        listView.setAdapter(arrayAdapter);
//        listView.setOnItemClickListener(onClickListener);
        listView.setOnItemLongClickListener(longClickListener);

        check_userid.setChecked(true);
        getFirebaseDatabase();

    }
    private AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("Long Click", "position = " + position);
            final String[] nowData = arrayData.get(position).split("\\s+");
 //           ID = nowData[0];
            String viewData = nowData[0] + ", " + nowData[1] + ", " + nowData[2] + ", " + nowData[3];
            AlertDialog.Builder dialog = new AlertDialog.Builder(UserList.this);
            dialog.setTitle("데이터 삭제")
                    .setMessage("해당 데이터를 삭제 하시겠습니까?" + "\n" + viewData)
                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
               //             postFirebaseDatabase(false);
                            getFirebaseDatabase();
               //             setInsertMode();
               //             edit_ID.setEnabled(true);
                            Toast.makeText(UserList.this, "데이터를 삭제했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(UserList.this, "삭제를 취소했습니다.", Toast.LENGTH_SHORT).show();
                  //          setInsertMode();
                 //          edit_ID.setEnabled(true);
                        }
                    })
                    .create()
                    .show();
            return false;
        }
    };
    public void getFirebaseDatabase(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("getFirebaseDatabase", "key: " + dataSnapshot.getChildrenCount());
                arrayData.clear();
                arrayIndex.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    FirebasePost get = postSnapshot.getValue(FirebasePost.class);
                    String[] info = {get.id, get.name, String.valueOf(get.birth), get.gender};
                    String Result = setTextLength(info[0],10) + setTextLength(info[1],10) + setTextLength(info[2],10) + setTextLength(info[3],10);
                    arrayData.add(Result);
                    arrayIndex.add(key);
                    Log.d("getFirebaseDatabase", "key: " + key);
                    Log.d("getFirebaseDatabase", "info: " + info[0] + info[1] + info[2] + info[3]);
                }
                arrayAdapter.clear();
                arrayAdapter.addAll(arrayData);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("getFirebaseDatabase","loadPost:onCancelled", databaseError.toException());
            }
        };
        Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("id_list").orderByChild(sort);
        sortbyAge.addListenerForSingleValueEvent(postListener);
    }

    private String setTextLength(String s, int i) {
        if(s.length()<i){
            int gap = i -s.length();
            for (int k=0; k<gap; k++){
                s = s + " ";
            }
        }
        return s;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.check_userid:
                check_name.setChecked(false);
                check_age.setChecked(false);
                sort = "id";
                break;

            case R.id.check_name:
                check_userid.setChecked(false);
                check_age.setChecked(false);
                sort = "name";
                break;

            case R.id.check_age:
                check_userid.setChecked(false);
                check_name.setChecked(false);
                sort = "age";
                break;
        }
        }
    }
