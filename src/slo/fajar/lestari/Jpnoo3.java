package slo.fajar.lestari;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Jpnoo3 extends Activity {
	
	private DBAdapter dba=null;
	private SQLiteDatabase db=null;
	
	EditText npwp;
	EditText npwpseri;
	EditText namanpwp;
	EditText alamatnpwp;
	TextView namanoo;
	
	String nopengajuan;
	String kd_sales;
	String nama_sales;
	
	Button btnext;
	Button btback;
	Button btclose;
	
	Boolean iinsert;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pnoo3);
        
        Bundle bnd= getIntent().getExtras();
        
        nopengajuan = bnd.getString("nobukti");
        kd_sales = bnd.getString("kd_sales");
        nama_sales = bnd.getString("nama_sales");
        
        namanoo = (TextView)findViewById(R.id.tnama_noo3);
        namanoo.setText(bnd.getString("nama_noo"));
        
        dba = new DBAdapter(this);
        
        npwp = (EditText)findViewById(R.id.tnpwp_noo);
        npwpseri = (EditText)findViewById(R.id.tnpwpseri_noo);
        namanpwp = (EditText)findViewById(R.id.tnamanpwp_noo);
        alamatnpwp = (EditText)findViewById(R.id.talamatnpwp_noo);
        
        btnext = (Button)findViewById(R.id.bt_next3_noo);
        btback = (Button)findViewById(R.id.bt_back3_noo);
        btclose = (Button)findViewById(R.id.btcancel_noo3);
        
        npwp.setOnKeyListener(npwp_list);
        npwpseri.setOnKeyListener(npwpseri_list);
        namanpwp.setOnKeyListener(namanpwp_list);
        alamatnpwp.setOnKeyListener(alamatnpwp_list);
        
        btnext.setOnClickListener(btnext_list);
        btback.setOnClickListener(btback_list);
        btclose.setOnClickListener(btcancel_list);
        
	} // akhir dari oncreate
	
	private void isi_all(){
		db = dba.getReadableDatabase();
		
		try {
			
			String sql="SELECT npwp, no_npwp_seri,"+
						"nama_npwp, alamat_npwp,step"+
						" FROM noo"+
					" WHERE no_pengajuan='"+ nopengajuan +"'";
			
			Cursor cursor = dba.SelectData(db,sql);
			cursor.moveToFirst(); 
			
	        int jml_baris = cursor.getCount();
	        if (jml_baris==1){
	        	
	        	npwp.setText(cursor.getString(cursor.getColumnIndex("npwp")));
	        	npwpseri.setText(cursor.getString(cursor.getColumnIndex("no_npwp_seri")));
	        	
	        	alamatnpwp.setText(cursor.getString(cursor.getColumnIndex("alamat_npwp")));
	        	namanpwp.setText(cursor.getString(cursor.getColumnIndex("nama_npwp")));
	        	
	        	String sinsert=cursor.getString(cursor.getColumnIndex("step"));
	        	Integer sinsert2 = Integer.valueOf(sinsert);
	        	
	        	
	        	if (sinsert2 > 3){
	        		iinsert = false;
	        	}else{
	        		iinsert = true;
	        	}
	        	
	        	cursor.close();
	        	
	        }else{
	        	npwp.setText("");
	        	npwpseri.setText("");
	        	namanpwp.setText("");
	        	alamatnpwp.setText("");
	        	iinsert=true;
	        }
			
			
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		
	};
	
View.OnKeyListener npwp_list =new  View.OnKeyListener() {
		
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
				npwpseri.requestFocus();
				return true;
			}else {
				return false;
			}
		}
	};
	
View.OnKeyListener npwpseri_list =new  View.OnKeyListener() {
		
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
				namanpwp.requestFocus();
				return true;
			}else if (keyCode== KeyEvent.ACTION_UP){
				npwp.requestFocus();
				return true;
			}else {
				return false;
			}
		}
	};
	
View.OnKeyListener namanpwp_list =new  View.OnKeyListener() {
		
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
				alamatnpwp.requestFocus();
				return true;
			}else if (keyCode== KeyEvent.ACTION_UP){
				namanpwp.requestFocus();
				return true;
			}else {
				return false;
			}
		}
	};
	
View.OnKeyListener alamatnpwp_list =new  View.OnKeyListener() {
		
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
				btnext.requestFocus();
				return true;
			}else if (keyCode== KeyEvent.ACTION_UP){
				alamatnpwp.requestFocus();
				return true;
			}else {
				return false;
			}
		}
	};
	
View.OnClickListener btnext_list =new View.OnClickListener() {
	
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		db = dba.getWritableDatabase();
		
		try {
			
			String sql="";
			
				sql="update noo set npwp='"+ npwp.getText().toString().trim().toUpperCase() +"',"+
						"no_npwp_seri='"+ npwpseri.getText().toString().trim().toUpperCase() +"',"+
						"nama_npwp='"+ namanpwp.getText().toString().trim().toUpperCase() +"',"+
						"alamat_npwp='"+ alamatnpwp.getText().toString().trim().toUpperCase() +"'";
			
			if (iinsert.equals(true)) {
				sql=sql + ",step=3 where no_pengajuan='"+ nopengajuan +"'";
			}else{
				sql=sql + " where no_pengajuan='"+ nopengajuan +"'";
			}
			
			db.execSQL(sql);
			
			Toast.makeText(Jpnoo3.this, "Step 3 - Disimpan", Toast.LENGTH_LONG).show();
			
			if (iinsert.equals(true)){
			}else{
				showDialog(1);
				return;
			}
			
			Intent myintent=new Intent(getBaseContext(), Jpnoo4.class);
			
			myintent.putExtra("nobukti", nopengajuan);
			myintent.putExtra("nama_noo", namanoo.getText().toString().trim().toUpperCase() );
			myintent.putExtra("kd_sales", kd_sales);
			myintent.putExtra("nama_sales", nama_sales);
			
			startActivity(myintent);
			finish();
			
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		
		
	}
}; // akhir dari next listener

View.OnClickListener btback_list = new View.OnClickListener() {
	
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		Intent myintent=new Intent(getBaseContext(), Jpnoo2.class);
		
		myintent.putExtra("nobukti", nopengajuan);
		myintent.putExtra("nama_noo", namanoo.getText().toString().trim().toUpperCase() );
		myintent.putExtra("kd_sales", kd_sales);
		myintent.putExtra("nama_sales", nama_sales);
		
		startActivity(myintent);
		finish();
		
	}
}; // akhir dari back list

View.OnClickListener btcancel_list = new View.OnClickListener() {
	
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		finish();
	}
}; // akhir dari cancel

@Override
protected void onResume(){
	super.onResume();
	isi_all();
}

@Override
protected void onPause(){
	super.onPause();
}

@Override
protected void onDestroy(){
	super.onDestroy();
	//db.close();
}	


@Override
protected Dialog onCreateDialog(int id) {
    AlertDialog.Builder builder = new AlertDialog.Builder(Jpnoo3.this);
    builder.setTitle("Konfirmasi");
    builder.setMessage("Saat ini anda dalam status Rubah Data, apakah anda akan merubah juga Coordinat ?")
    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            // FIRE ZE MISSILES!
        	Intent myintent=new Intent(getBaseContext(), Jpnoo4.class);
			
			myintent.putExtra("nobukti", nopengajuan);
			myintent.putExtra("nama_noo", namanoo.getText().toString().trim().toUpperCase() );
		
			startActivity(myintent);
			finish();
        	}
    	})
    
    	.setNegativeButton("No", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                	   finish();
                   }
               });
    
    return builder.create();
}

} // akhir dari class activity
