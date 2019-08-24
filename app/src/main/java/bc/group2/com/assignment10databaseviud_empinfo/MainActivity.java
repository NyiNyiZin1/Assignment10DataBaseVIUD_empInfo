package bc.group2.com.assignment10databaseviud_empinfo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {
    Button view,insert,update,delete,viewdata,back,updatedata,deletedata;

    Intent intent;
    Bundle data,begin;

    Cursor c;
    SQLiteDatabase db;

    String query="";

    String searchtxt="",nameOrId="";
    RadioGroup gp;
    RadioButton searchby;
    EditText txt;
    TextView viewtxt;

    AlertDialog dialog=null;

    ArrayList<String> sdata=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        begin=savedInstanceState;
        try{
            db = SQLiteDatabase.openDatabase("data/data/bc.group2.com.assignment10databaseviud_empinfo/employeeDB", null, SQLiteDatabase.CREATE_IF_NECESSARY);

        }catch (SQLiteException e){
            Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
        boolean found=tableExists(db,"employeeInfo");

        //To create Table
        if (found==false){
            createTable();
        }


        view=(Button)findViewById(R.id.view);
        view.setOnClickListener(new ViewData());

        insert=(Button)findViewById(R.id.insert);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setContentView(R.layout.activity_insert);
                intent=new Intent(MainActivity.this,InsertActivity.class);
                startActivity(intent);
            }
        });

        update=(Button)findViewById(R.id.update);
        update.setOnClickListener(new Update());

        delete=(Button)findViewById(R.id.delete);
        delete.setOnClickListener(new Delete());

    }

    public boolean tableExists(SQLiteDatabase db1,String tablename){

        String sqlqry="SELECT name FROM sqlite_master "+
                " WHERE type='table'" +
                " AND name='"+tablename+"'";

 /*Cursor cursor=db1.rawQuery(sqlqry,null);
int result=cursor.getCount();
 */
 int result=db1.rawQuery(sqlqry,null).getCount();

        if(result==1)
        {
            return  true;
        }
        else
        {
            return  false;
        }
    }

    public void createTable(){
        try {
             db.beginTransaction();
            //query="select * from employeeInfo";
            //c=db.rawQuery(query,null);
            Toast.makeText(MainActivity.this,"To create table",Toast.LENGTH_LONG).show();
            //if(c.getCount()<0) {
                db.execSQL("create table employeeInfo(eId integer PRIMARY KEY,ename text,nrc text,email text,password text,rank text,dept text,gender text,dob text,ph text,address text);");
                db.setTransactionSuccessful();
                Toast.makeText(MainActivity.this,"Table Created!!!",Toast.LENGTH_LONG).show();

          // }

        }catch (SQLiteException e)
        {
            Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }finally {
            db.endTransaction();
        }
    }

    //ViewData-----------------------------------------------------------------
    public class ViewData implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            setContentView(R.layout.activity_view);

            txt=(EditText)findViewById(R.id.searchtxt);
            gp=(RadioGroup)findViewById(R.id.viewgp);
            gp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    int id = gp.getCheckedRadioButtonId();
                    searchby = (RadioButton) gp.findViewById(id);
                    Log.e("group ID>>>>>>>>>>", Integer.toString(gp.getCheckedRadioButtonId()));
                    if (searchby.getText().toString().equals("Name")) {
                        nameOrId = "name";
                        Log.e("Id>>>", "equals Name");
                    } else if (searchby.getText().toString().equals("ID")) {
                        nameOrId = "id";
                        Log.e("Id>>>", "equals Name");
                    } else {
                        Log.e("Id>>>", "Not equal");
                    }
                }
            });
            viewdata=(Button)findViewById(R.id.viewbtn);
            viewdata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchtxt=txt.getText().toString();
                    if (nameOrId.equals("name")){
                        query="select * from employeeInfo where ename='"+searchtxt+"'";
                    }
                    else if (nameOrId.equals("id")){
                        query="select * from employeeInfo where eId='"+searchtxt+"'";
                    }
                    Cursor c1;
                    viewtxt=(TextView) findViewById(R.id.viewinfo);

                    c1=db.rawQuery(query,null);
                    int index2=c1.getColumnIndex("ename");
                    int index1=c1.getColumnIndex("eId");
                    int index3=c1.getColumnIndex("nrc");
                    int index4=c1.getColumnIndex("email");
                    int index5=c1.getColumnIndex("password");
                    int index6=c1.getColumnIndex("rank");
                    int index7=c1.getColumnIndex("dept");
                    int index8=c1.getColumnIndex("gender");
                    int index9=c1.getColumnIndex("dob");
                    int index10=c1.getColumnIndex("ph");
                    int index11=c1.getColumnIndex("address");
                    Log.e("Cursor Count>>>>>>",Integer.toString(c1.getCount()));
                    if(c1.getCount()>0){
                        while (c1.moveToNext()) {
                            viewtxt.append("Employee ID:   " + Integer.toString(c1.getInt(index1)) +
                                    "\nEmpoyee Name:   " + c1.getString(index2) + "\nNRC:    " + c1.getString(index3) +
                                    "\nEmail:    " + c1.getString(index4) + "\nPassword:    "+c1.getString(index5)+
                                    "\nRank:    " + c1.getString(index6) + "\nDepartment:    "+c1.getString(index7)+
                                    "\nGender:    "+c1.getString(index8) + "\nDate Of Birth:    "+c1.getString(index9)+
                                    "\nPhone No:    "+c1.getString(index10) + "\nAddress:    "+c1.getString(index11)+
                                    "\n-----------------------------------------------------\n");
                        }
                        c1.close();
                    }
                    else {
                        viewtxt.append("No data found searched by "+searchtxt+
                                "\n-----------------------------------------------------\n");
                    }


                }
            });

            back=(Button)findViewById(R.id.vback);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCreate(begin);
                }
            });

        }
    }
    public class Update implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            setContentView(R.layout.activity_toupdate);

            txt=(EditText)findViewById(R.id.searchupdatetxt);
            gp=(RadioGroup)findViewById(R.id.updategp);
            gp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    int id = gp.getCheckedRadioButtonId();
                    searchby = (RadioButton) gp.findViewById(id);
                    Log.e("group ID>>>>>>>>>>", Integer.toString(gp.getCheckedRadioButtonId()));
                    if (searchby.getText().toString().equals("Name")) {
                        nameOrId = "name";
                        Log.e("Id>>>", "equals Name");
                    } else if (searchby.getText().toString().equals("ID")) {
                        nameOrId = "id";
                        Log.e("Id>>>", "equals Name");
                    } else {
                        Log.e("Id>>>", "Not equal");
                    }
                }
            });
           updatedata=(Button)findViewById(R.id.updatebtn);
           updatedata.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    searchtxt=txt.getText().toString();
                    if (nameOrId.equals("name")){
                        query="select * from employeeInfo where ename='"+searchtxt+"'";
                    }
                    else if (nameOrId.equals("id")){
                        query="select * from employeeInfo where eId='"+searchtxt+"'";
                    }
                    Cursor c1;
                    viewtxt=(TextView) findViewById(R.id.updateinfo);
                    c1=db.rawQuery(query,null);
                    int index2=c1.getColumnIndex("ename");
                    int index1=c1.getColumnIndex("eId");
                    int index3=c1.getColumnIndex("nrc");
                    int index4=c1.getColumnIndex("email");
                    int index5=c1.getColumnIndex("password");
                    int index6=c1.getColumnIndex("rank");
                    int index7=c1.getColumnIndex("dept");
                    int index8=c1.getColumnIndex("gender");
                    int index9=c1.getColumnIndex("dob");
                    int index10=c1.getColumnIndex("ph");
                    int index11=c1.getColumnIndex("address");
                    Log.e("Cursor Count>>>>>>",Integer.toString(c1.getCount()));
                    if(c1.getCount()>0){
                        while (c1.moveToNext()) {
                            /*viewtxt.append("Employee ID:   " + Integer.toString(c1.getInt(index1)) +
                                    "\nEmpoyee Name:   " + c1.getString(index2) + "\nNRC:    " + c1.getString(index3) +
                                    "\nEmail:    " + c1.getString(index4) + "\nPassword:    "+c1.getString(index5)+
                                    "\nRank:    " + c1.getString(index6) + "\nDepartment:    "+c1.getString(index7)+
                                    "\nGender:    "+c1.getString(index8) + "\nDate Of Birth:    "+c1.getString(index9)+
                                    "\nPhone No:    "+c1.getString(index10) + "\nAddress:    "+c1.getString(index11)+
                                    "\n-----------------------------------------------------\n");*/
                            Bundle b=new Bundle();
                            sdata.add(Integer.toString(c1.getInt(index1)));
                            sdata.add(c1.getString(index2));
                            sdata.add(c1.getString(index3));
                            sdata.add(c1.getString(index4));
                            sdata.add(c1.getString(index5));
                            sdata.add(c1.getString(index6));
                            sdata.add(c1.getString(index7));
                            sdata.add(c1.getString(index8));
                            sdata.add(c1.getString(index9));
                            sdata.add(c1.getString(index10));
                            sdata.add(c1.getString(index11));
                            b.putStringArrayList("sdata",sdata);
                            Intent intent=new Intent(MainActivity.this,UpdateActivity.class);
                            intent.putExtras(b);
                            startActivity(intent);

                        }
                        c1.close();
                    }
                    else {

                        //viewtxt.append("No data found searched by "+searchtxt+ "\n-----------------------------------------------------\n");
                        AlertDialog warning=makeAndShowDialogBox();
                        warning.show();
                    }

                }
            });

            back=(Button)findViewById(R.id.uback);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCreate(begin);
                    //finish();
                }
            });

        }


    }

    public class Delete implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            setContentView(R.layout.activity_delete);

            txt = (EditText) findViewById(R.id.searchdeletetxt);
            gp = (RadioGroup) findViewById(R.id.deletegp);
            gp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    int id = gp.getCheckedRadioButtonId();
                        searchby = (RadioButton) gp.findViewById(id);
                        Log.e("group ID>>>>>>>>>>", Integer.toString(gp.getCheckedRadioButtonId()));
                        if (searchby.getText().toString().equals("Name")) {
                            nameOrId = "name";
                            Log.e("Id>>>", "equals Name");
                        } else if (searchby.getText().toString().equals("ID")) {
                            nameOrId = "id";
                            Log.e("Id>>>", "equals Name");
                        } else {
                            Log.e("Id>>>", "Not equal");
                        }
                }
            });
            deletedata=(Button)findViewById(R.id.deletebtn);
            deletedata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this,"Delete Button",Toast.LENGTH_LONG).show();
                    searchtxt=txt.getText().toString();
                    if(searchtxt.equals("")){
                        Toast.makeText(MainActivity.this,"Must Check Radio Buutton!!",Toast.LENGTH_LONG).show();
                    }
                    else {
                        if (nameOrId.equals("name")) {
                            query = "select * from employeeInfo where ename='" + searchtxt + "'";
                            Cursor c1;
                            viewtxt = (TextView) findViewById(R.id.updateinfo);
                            c1 = db.rawQuery(query, null);
                            if (c1.getCount() > 0) {
                                while (c1.moveToNext()) {
                                    db.execSQL("delete from employeeInfo where ename='" + searchtxt + "'");
                                    Toast.makeText(MainActivity.this, "Delete Successful", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                AlertDialog deldialog = makeAndShowDialogBox();
                                deldialog.show();
                            }
                        } else if (nameOrId.equals("id")) {
                            query = "select * from employeeInfo where eId='" + searchtxt + "'";
                            Cursor c1;
                            viewtxt = (TextView) findViewById(R.id.updateinfo);
                            c1 = db.rawQuery(query, null);
                            if (c1.getCount() > 0) {
                                while (c1.moveToNext()) {
                                    db.execSQL("delete from employeeInfo where eId='" + searchtxt + "'");
                                    Toast.makeText(MainActivity.this, "Delete Successful", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                AlertDialog deldialog = makeAndShowDialogBox();
                                deldialog.show();
                            }
                        }
                    }
                }
            });
            back=(Button)findViewById(R.id.dback);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCreate(begin);
                }
            });
        }
    }

   /* @Override
    protected void onRestart() {
        super.onRestart();
        view=(Button)findViewById(R.id.view);
        view.setOnClickListener(new ViewData());
    }*/

    public AlertDialog makeAndShowDialogBox(){

        dialog=new AlertDialog.Builder(MainActivity.this)
                .setTitle("Error")
                .setMessage("This employee is does not in database!")
                .setIcon(R.drawable.ic_menu_close_clear_cancel)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();

        return dialog;
    }
}
