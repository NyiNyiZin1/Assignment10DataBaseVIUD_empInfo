package bc.group2.com.assignment10databaseviud_empinfo;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UpdateActivity extends AppCompatActivity {

    String[] rankdata={"Chief Executive Office(CEO)","Managing Director(MD)","Assistant Manager","Supervisor","Sale Representative"};
    String[] deptdata={"Managing Department","HR Department","Finance Department","Admin Department"};

    TextView eid,ename,nrc,gender,dob,phone;
    EditText email,password,address;
    Button update,cancle,back;
    Spinner rank,dept;

    String eidstr="",emailstr="",pswstr="",rankstr="",deptstr="",addstr="";

    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        final Bundle bundle=savedInstanceState;

        db = SQLiteDatabase.openDatabase("data/data/bc.group2.com.assignment10databaseviud_empinfo/employeeDB", null, SQLiteDatabase.CREATE_IF_NECESSARY);


        ArrayAdapter<String> aa=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,rankdata);
        ArrayAdapter<String> aa1=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,deptdata);

        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        eid=(TextView)findViewById(R.id.eidtxt);
        ename=(TextView)findViewById(R.id.enametxt);
        nrc=(TextView)findViewById(R.id.nrctxt);
        gender=(TextView)findViewById(R.id.gendertxt);
        dob=(TextView)findViewById(R.id.dobtxt);
        phone=(TextView)findViewById(R.id.phtxt);

        email=(EditText)findViewById(R.id.emailedit);
        password=(EditText)findViewById(R.id.pswedit);
        address=(EditText)findViewById(R.id.addressedit);

        final Intent intent=getIntent();
        Bundle b=intent.getExtras();
        ArrayList<String> data=b.getStringArrayList("sdata");
            //Toast.makeText(UpdateActivity.this,"Data:   "+data.get(i),Toast.LENGTH_LONG).show();
        eid.setText(data.get(0));
        ename.setText(data.get(1));
        nrc.setText(data.get(2));
        gender.setText(data.get(7));
        dob.setText(data.get(8));
        phone.setText(data.get(9));

        email.setText(data.get(3));
        password.setText(data.get(4));
        address.setText(data.get(10));


        rank=(Spinner)findViewById(R.id.rankedit);
        rank.setAdapter(aa);

        dept=(Spinner)findViewById(R.id.deptedit);
        dept.setAdapter(aa1);

        update=(Button)findViewById(R.id.updateData);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                eidstr=eid.getText().toString();
                emailstr=email.getText().toString();
                pswstr=password.getText().toString();
                rankstr=rank.getSelectedItem().toString();
                deptstr=dept.getSelectedItem().toString();
                addstr=address.getText().toString();

                try {
                    if (emailstr.equals("")||pswstr.equals("")||addstr.equals("")){
                        Toast.makeText(UpdateActivity.this,"Please Fill All Data!!!",Toast.LENGTH_LONG).show();
                    }
                    else {
                        db.beginTransaction();
                        db.execSQL("update employeeInfo set email='" + emailstr + "',password='" + pswstr + "',rank='" + rankstr + "',dept='" + deptstr + "',address='" + addstr + "' where eId='" + eidstr + "'");
                        db.setTransactionSuccessful();
                        Toast.makeText(UpdateActivity.this, "Update Successful", Toast.LENGTH_LONG).show();
                        Log.e("Update>>>>>>>", "Successful");
                    }

                }catch (SQLiteException e) {
                    Toast.makeText(UpdateActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    db.endTransaction();
//                    onCreate(bundle);
                    Intent i=new Intent(UpdateActivity.this,MainActivity.class);
                    startActivity(i);

                }
            }
        });

        back=(Button)findViewById(R.id.uback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cancle=(Button)findViewById(R.id.ucancel);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setText("");
                password.setText("");
                address.setText("");
            }
        });
    }
}
