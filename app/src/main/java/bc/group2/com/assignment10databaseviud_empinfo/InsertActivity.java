package bc.group2.com.assignment10databaseviud_empinfo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class InsertActivity extends AppCompatActivity {

    String[] rankdata={"Chief Executive Office(CEO)","Managing Director(MD)","Assistant Manager","Supervisor","Sale Representative"};
    String[] deptdata={"Managing Department","HR Department","Finance Department","Admin Department"};

    EditText eid,ename,nrc,email,password,phone,address;
    Button dob,save,cancle,back;
    Spinner rank,dept;
    RadioGroup group;
    RadioButton radgender;


    String rankstr="",enamestr="",nrcstr="",emailstr="",pswstr="",phstr="",addstr="",eidstr="";
    String deptstr="";
    String gender="";
    String dobstring="";

    String query="";

    ArrayList<String> id=new ArrayList<>();

    Calendar mycalendar=Calendar.getInstance();

    SQLiteDatabase db;
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        eid=(EditText)findViewById(R.id.eid);
        ename=(EditText)findViewById(R.id.name);
        nrc=(EditText)findViewById(R.id.nrc);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.psw);
        phone=(EditText)findViewById(R.id.ph);
        address=(EditText)findViewById(R.id.address);
        group=(RadioGroup)findViewById(R.id.group);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int i=group.getCheckedRadioButtonId();
                radgender=(RadioButton)group.findViewById(i);
                gender=radgender.getText().toString();
            }
        });

        ArrayAdapter<String> aa=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,rankdata);
        ArrayAdapter<String> aa1=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,deptdata);

        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        rank=(Spinner)findViewById(R.id.rank);
        rank.setAdapter(aa);

        dept=(Spinner)findViewById(R.id.dept);
        dept.setAdapter(aa1);
        /*rank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rankstr=aa[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        dob=(Button)findViewById(R.id.dob);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog=new DatePickerDialog(InsertActivity.this,
                        dp,mycalendar.get(Calendar.YEAR),
                        mycalendar.get(Calendar.MONTH),
                        mycalendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        save=(Button)findViewById(R.id.insertData);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eidstr=eid.getText().toString();
                enamestr=ename.getText().toString();
                nrcstr=nrc.getText().toString();
                emailstr=email.getText().toString();
                pswstr=password.getText().toString();
                rankstr=rank.getSelectedItem().toString();
                deptstr=dept.getSelectedItem().toString();
                phstr=phone.getText().toString();
                addstr=address.getText().toString();
                //Toast.makeText(getApplicationContext(),"Rank: "+rankstr+"\n Dept: "+deptstr+"\nname "+enamestr+"\ndob   "+dobstring+"\n gender  "+gender,Toast.LENGTH_LONG).show();
                if (eidstr.equals("")||enamestr.equals("")||nrcstr.equals("")||emailstr.equals("")||pswstr.equals("")||rankstr.equals("")||deptstr.equals("")||phstr.equals("")||addstr.equals("")||gender.equals("")){
                    Toast.makeText(getApplicationContext(),"Please fill all data!!!",Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        db = SQLiteDatabase.openDatabase("data/data/bc.group2.com.assignment10databaseviud_empinfo/employeeDB", null, SQLiteDatabase.CREATE_IF_NECESSARY);
                        db.beginTransaction();
                        c=db.rawQuery("select * from employeeInfo",null);
                        int index=c.getColumnIndex("eId");
                        while (c.moveToNext()){
                            id.add(Integer.toString(c.getInt(index)));
                        }
                        /*for(String i:id){
                            Log.e("EID------------",i);
                            if (i.equals(eidstr)){
                                Toast.makeText(InsertActivity.this,"Employee Id already exists!!",Toast.LENGTH_LONG).show();
                            }
                        }*/
                        query = "insert into employeeInfo(eId,ename,nrc,email,password,rank,dept,gender,dob,ph,address)" +
                                " values('" + eidstr + "','" + enamestr + "','" + nrcstr + "','" + emailstr + "','" + pswstr + "','" + rankstr + "','" + deptstr + "','" + gender + "','" + dobstring + "','" + phstr + "','" + addstr + "');";
                        db.execSQL(query);
                        db.setTransactionSuccessful();
                        Toast.makeText(getBaseContext(),"Insert Succcessful",Toast.LENGTH_LONG).show();
                    }catch (SQLiteException e){
                        Toast.makeText(InsertActivity.this,"Employee Id already exists!!",Toast.LENGTH_LONG).show();
                    }
                    finally {
                        db.endTransaction();
                        finish();
                    }
                }
            }
        });

        back=(Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cancle=(Button)findViewById(R.id.cancel);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eid.setText("");
                ename.setText("");
                nrc.setText("");
                email.setText("");
                password.setText("");
                phone.setText("");
                address.setText("");
            }
        });

    }


    DatePickerDialog.OnDateSetListener dp=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mycalendar.set(Calendar.YEAR,year);
            mycalendar.set(Calendar.MONTH,month);
            mycalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

            Date d=mycalendar.getTime();

            String strdate= DateFormat.getDateInstance().format(d);

            dob.setText(strdate);
            dobstring=strdate;
        }
    };
}
