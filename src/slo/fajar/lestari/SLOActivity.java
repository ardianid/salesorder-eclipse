package slo.fajar.lestari;

//import java.util.ArrayList;

//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class SLOActivity extends Activity {
  //  protected View view;

	/** Called when the activity is first created. */
	
	// private String url_user="http://10.0.2.2:8080/slo/req_user.php";
	
	private DBAdapter dba=null;
	private SQLiteDatabase db = null; 
	private Cursor dbCursor = null;  
	
	static final String ms_user ="ms_user";
	static final String ms_sales ="ms_sales";
	
	TextView userid=null;
	TextView pwd=null;
	TextView tkonfirm;
	
	Button btmasuk,btkeluar;
	
	String kd_sal="";
	String nama_sal="";
	String response = "";
	
	Integer jmlcoba;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
       /* try {
        	String pathTujuan = "/data/data/" + getPackageName() + "/databases/mydb";
        	File f = new File(pathTujuan);
        	if (!f.exists()) {
        		CopyDB(getBaseContext().getAssets().open("mydb"), new FileOutputStream(pathTujuan));
        	}
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        } */
        
        jmlcoba=0;
        
        
        userid = (TextView)findViewById(R.id.tuser_akt);
        pwd = (TextView)findViewById(R.id.tpwd_akt);
        
        btmasuk = (Button)findViewById(R.id.btmasuk_akt);
        btkeluar = (Button)findViewById(R.id.btkeluar_akt);
        
        tkonfirm = (TextView)findViewById(R.id.tkonfirm_awal);
        tkonfirm.setText("");
        
        dba = new DBAdapter(this);
        
       String sql_cekuser ="select nama_user from ms_user where nama_user='admin'";
       db = dba.getReadableDatabase();
       
       Cursor cursor = dba.SelectData(db,sql_cekuser);
       cursor.moveToFirst(); 
       int jml_baris = cursor.getCount();
       
       if (jml_baris >=1){
    	   
    	 String usertabel = cursor.getString(cursor.getColumnIndex("nama_user"));
    	 
    	 if (usertabel.toString().length() <1) {
    		 insert_useradmin();
    	 }
    	 
       }else{
    	   insert_useradmin();
       }
       
        
      /*  
        userid.setOnKeyListener(new View.OnKeyListener() {
			
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				
				if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
					pwd.requestFocus();
					return true;
				}else {
					return false;
				}
			}
		});
        
        
        pwd.setOnKeyListener(new View.OnKeyListener() {
			
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				
				if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
					btmasuk.requestFocus();
					return true;
				}else if (keyCode== KeyEvent.ACTION_UP){
					userid.requestFocus();
					return true;
				}else {
					return false;
				}
			}
		}); */
        
        btmasuk.setOnClickListener(new Button.OnClickListener() {
        	
			public void onClick(View v) {
				
				if (userid.getText().length()==0){
					Toast.makeText(getApplicationContext(), "User-ID harus diisi",Toast.LENGTH_SHORT).show();
					userid.setFocusable(true);
					userid.requestFocus();
					return;
				}
				
				if (pwd.getText().length()==0){
					Toast.makeText(getApplicationContext(), "Password harus diisi",Toast.LENGTH_SHORT).show();
					pwd.setFocusable(true);
					pwd.requestFocus();
					return;
				}
				
				masuk_ok(userid.getText().toString().trim(), pwd.getText().toString().trim(), v);
				
			}
			
			
        });
        
        btkeluar.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				
				jmlcoba=0;
				tkonfirm.setText("");
				
				finish();
			}
		});
        
      
        
} // akhir dari on create
	
	@Override
    protected void onResume(){
    	super.onResume();
    }
    
    @Override
    protected void onPause(){
    	super.onPause();
    }
    
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    }	
   
	
	//cek ada tidaknya
	private void masuk_ok(String usern,String pwd,View v){
		

		
		db = dba.getReadableDatabase(); 
		
		if (usern.equals("admin")){
			dbCursor = dba.SelectData(db, "select "+ms_user+".kd_sales,"+ms_user+".nama_user from "+ms_user+"  where "+ms_user+".nama_user="+"'"+usern+"'"+" and "+ms_user+".pwd="+"'"+pwd+"'");
			dbCursor.moveToFirst(); 
		}else {
		
		dbCursor = dba.SelectData(db, "select "+ms_user+".kd_sales,"+ms_user+".nama_sales from "+ms_user
				+" where "+ms_user+".nama_user="+"'"+usern+"'"+" and "+ms_user+".pwd="+"'"+pwd+"'");
		dbCursor.moveToFirst(); 
	}
		
        int jml_baris = dbCursor.getCount();
        
        if(jml_baris == 0)  {
        	
        	jmlcoba = jmlcoba +1;
        	
        	dbCursor.close();
			Toast.makeText(getApplicationContext(), "User atau Password salah",Toast.LENGTH_SHORT).show();
			
			if (jmlcoba.equals(3)){
				tkonfirm.setText("Anda telah melakukan kesalahan 3x, sekali lagi anda salah semua data akan diHAPUS..");
			}
			
			if (jmlcoba.equals(4)){
				hapus_alldata();
				Toast.makeText(getApplicationContext(), "Anda telah melebihi batas toleransi kesalahan, SEMUA DATA DIHAPUS.!!!",Toast.LENGTH_SHORT).show();
				tkonfirm.setText("SEMUA DATA TELAH DIHAPUS, HUB ADMINISTRATOR");
				jmlcoba=0;
			}
			
			return;
        }else {
        	
        	kd_sal=  dbCursor.getString(0); //menuitemArray.getJSONObject(0).getString("KD_SALES").toString();
			nama_sal=  dbCursor.getString(1); // menuitemArray.getJSONObject(0).getString("Nama").toString();
			
			Intent myIntent = new Intent(v.getContext(), jlogin.class);
			
			myIntent.putExtra("kd_sales", kd_sal);
			myIntent.putExtra("nama_sales", nama_sal);
			
			dbCursor.close();
			
			startActivity(myIntent);
			
			finish();
			return;
        	
        }
        
	}
	
	private void insert_useradmin(){
		db = dba.getReadableDatabase();
		
		try {
			
			String sql ="insert into ms_user (nama_user,kd_sales,nama_sales,pwd) values('admin','00000','admin','sadmin')";
			db.execSQL(sql);
			
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		
		
	}
	
	private void hapus_alldata(){
		
		db = dba.getWritableDatabase();
		
		try {
			
			String sql_delcust = "delete from ms_customer";
 			String sql_delgroup = "delete from ms_groupcust";
 			String sql_delbarang = "delete from ms_barang";
 			String sql_delarea = "delete from ms_area";
 			String sql_delterm = "delete from ms_term";
 			String sql_delpasar = "delete from ms_pasar";
 			String sql_delother = "delete from ms_other";
 			String sql_delsales = "delete from ms_sales";
 			String sql_delkondisi ="delete from ms_kondisi";
 			
 			String sql_delfaktur_d = "delete from faktur_d";
 			String sql_delfaktur_h = "delete from faktur_h";
 			String sql_delnoo = "delete from noo";
 			String sql_deluser ="delete from ms_user";
 			
 			db.execSQL(sql_delcust);
 			db.execSQL(sql_delgroup);
 			db.execSQL(sql_delbarang);
 			db.execSQL(sql_delarea);
 			db.execSQL(sql_delterm);
 			db.execSQL(sql_delpasar);
 			db.execSQL(sql_delother);
 			db.execSQL(sql_delsales);
 			db.execSQL(sql_delkondisi);
 			
 			db.execSQL(sql_delnoo);
 			db.execSQL(sql_delfaktur_h);
 			db.execSQL(sql_delfaktur_d);
 			db.execSQL(sql_deluser);
 			
			
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		
	}
	
    public void CopyDB(InputStream inputStream, OutputStream outputStream) 
        	throws IOException {
        		//--copy 1K byte pada saat ini---
        		byte[] buffer = new byte[1024];
        		int length;
        		while ((length = inputStream.read(buffer))>0) {
        			outputStream.write(buffer, 0, length);
        		}
        		inputStream.close();
        		outputStream.close();
        	}  
	
	
}



