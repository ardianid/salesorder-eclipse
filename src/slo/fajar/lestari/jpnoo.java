package slo.fajar.lestari;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class jpnoo extends Activity {
	
	private DBAdapter dba=null;
	private SQLiteDatabase db=null;
	
	Spinner tjenisnoo;
	TextView tnama;
	TextView talamat;
	TextView tktp;
	TextView tpemilik;
	TextView ttgl_lahir;
	TextView tkontak;
	TextView temail;
	TextView ttelp1;
	TextView ttelp2;
	TextView thp1;
	TextView thp2;
	
	Button btnext;
	Button btcancel;
	
	String[] jenis_noo={"Kredit","Cash"};
	String kd_sales;
	String nama_sales;
	
	String nopengajuan;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pnoo1);
        
        Bundle bnd= getIntent().getExtras();
        	
        	kd_sales = bnd.getString("kd_sales").toString();
        	nama_sales = bnd.getString("nama_sales").toString();
        	nopengajuan = bnd.getString("nopengajuan").toString();
        	
        tjenisnoo = (Spinner)findViewById(R.id.spjenis_noo);
        
        tnama = (TextView)findViewById(R.id.tnama_noo);
        talamat = (TextView)findViewById(R.id.talamat_noo);
        tktp = (TextView)findViewById(R.id.tktp_noo);
        tpemilik = (TextView)findViewById(R.id.tpemilik_noo);
        ttgl_lahir = (TextView)findViewById(R.id.ttgl_lhr_noo);
        tkontak = (TextView)findViewById(R.id.tkontak_noo);
        temail = (TextView)findViewById(R.id.temail_noo);
        ttelp1 = (TextView)findViewById(R.id.ttelp1_noo);
        ttelp2 = (TextView)findViewById(R.id.ttelp2_noo);
        thp1 = (TextView)findViewById(R.id.thp1_noo);
        thp2 = (TextView)findViewById(R.id.thp2_noo);
        
        btnext = (Button)findViewById(R.id.bt_next1_noo);
        btcancel = (Button)findViewById(R.id.btcancel_noo1);
        
        dba = new DBAdapter(this);
        
        ArrayAdapter<String> aa=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,jenis_noo);
        tjenisnoo.setAdapter(aa);
        
        
     /*   tnama.setOnKeyListener(tnama_list);
        talamat.setOnKeyListener(talamat_list);
        tktp.setOnKeyListener(tktp_list);
        tpemilik.setOnKeyListener(tpemilik_list);
        ttgl_lahir.setOnKeyListener(ttgllhr_list);
        tkontak.setOnKeyListener(tkontak_list);
        temail.setOnKeyListener(temail_list);
        ttelp1.setOnKeyListener(ttelp1_list);
        ttelp2.setOnKeyListener(ttelp2_list);
        thp1.setOnKeyListener(thp1_list);
        thp2.setOnKeyListener(thp2_list); */
        
        
        btcancel.setOnClickListener(cancel_list);
        btnext.setOnClickListener(next_step_list);
        
	} // akhir dari class oncreate
	
	
	View.OnKeyListener tnama_list =new  View.OnKeyListener() {
		
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
				talamat.requestFocus();
				return true;
			}else if (keyCode== KeyEvent.ACTION_UP){
				tjenisnoo.requestFocus();
				return true;
			}else {
				return false;
			}
		}
	};
	
	
	
	View.OnKeyListener talamat_list =new  View.OnKeyListener() {
		
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			
			if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
				tktp.requestFocus();
				return true;
			}else if (keyCode== KeyEvent.ACTION_UP){
				tnama.requestFocus();
				return true;
			}else {
				return false;
			}
		}
	};
	
	View.OnKeyListener tktp_list =new  View.OnKeyListener() {
		
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
				tpemilik.requestFocus();
				return true;
			}else if (keyCode== KeyEvent.ACTION_UP){
				tktp.requestFocus();
				return true;
			}else {
				return false;
			}
		}
	};
	
	View.OnKeyListener tpemilik_list =new  View.OnKeyListener() {
		
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
				ttgl_lahir.requestFocus();
				return true;
			}else if (keyCode== KeyEvent.ACTION_UP){
				tpemilik.requestFocus();
				return true;
			}else {
				return false;
			}
		}
	};
	
	View.OnKeyListener ttgllhr_list =new  View.OnKeyListener() {
		
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
				tkontak.requestFocus();
				return true;
			}else if (keyCode== KeyEvent.ACTION_UP){
				tpemilik.requestFocus();
				return true;
			}else {
				return false;
			}
		}
	};
	
	View.OnKeyListener tkontak_list =new  View.OnKeyListener() {
		
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
				temail.requestFocus();
				return true;
			}else if (keyCode== KeyEvent.ACTION_UP){
				ttgl_lahir.requestFocus();
				return true;
			}else {
				return false;
			}
		}
	};
	
	View.OnKeyListener temail_list =new  View.OnKeyListener() {
		
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
				ttelp1.requestFocus();
				return true;
			}else if (keyCode== KeyEvent.ACTION_UP){
				tkontak.requestFocus();
				return true;
			}else {
				return false;
			}
		}
	};
	
	View.OnKeyListener ttelp1_list =new  View.OnKeyListener() {
		
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
				ttelp2.requestFocus();
				return true;
			}else if (keyCode== KeyEvent.ACTION_UP){
				ttelp1.requestFocus();
				return true;
			}else {
				return false;
			}
		}
	};
	
	View.OnKeyListener ttelp2_list =new  View.OnKeyListener() {
		
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
				thp1.requestFocus();
				return true;
			}else if (keyCode== KeyEvent.ACTION_UP){
				ttelp1.requestFocus();
				return true;
			}else {
				return false;
			}
		}
	};
	
	View.OnKeyListener thp1_list =new  View.OnKeyListener() {
		
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
				thp2.requestFocus();
				return true;
			}else if (keyCode== KeyEvent.ACTION_UP){
				ttelp2.requestFocus();
				return true;
			}else {
				return false;
			}
		}
	};

	View.OnKeyListener thp2_list =new  View.OnKeyListener() {
		
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
				btnext.requestFocus();
				return true;
			}else if (keyCode== KeyEvent.ACTION_UP){
				thp1.requestFocus();
				return true;
			}else {
				return false;
			}
		}
	};
	
	View.OnClickListener cancel_list= new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};
	
	View.OnClickListener next_step_list = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if (tnama.getText().toString().length()==0) {
				Toast.makeText(jpnoo.this,"Nama Outlet tidak boleh kosong", Toast.LENGTH_LONG).show();
				tnama.requestFocus();
				return;
			}
			
			if (talamat.getText().toString().length()==0) {
				Toast.makeText(jpnoo.this,"Alamat Outlet tidak boleh kosong", Toast.LENGTH_LONG).show();
				talamat.requestFocus();
				return;
			}
			
			if (tktp.getText().toString().length()==0) {
				Toast.makeText(jpnoo.this,"KTP Pemilik Outlet tidak boleh kosong", Toast.LENGTH_LONG).show();
				tktp.requestFocus();
				return;
			}
			
			if ( (ttelp1.getText().toString().length()==0) && (ttelp2.getText().toString().length()==0) &&
					(thp1.getText().toString().length()==0) && (thp2.getText().toString().length()==0)) {
				Toast.makeText(jpnoo.this,"Telp/Hp Outlet tidak boleh kosong", Toast.LENGTH_LONG).show();
				ttelp1.requestFocus();
				return;
			}
			
			
			db = dba.getWritableDatabase();
			
			try {
				
				String nobukti;
				
				if (nopengajuan.trim().length() !=0) {
					
					nobukti = nopengajuan;
					
					String sql="update noo set nama_outlet='"+ tnama.getText().toString().toUpperCase().trim() +"',"+
							"alamat='"+ talamat.getText().toString().toUpperCase().trim() +"', no_ktp='"+ tktp.getText().toString().toUpperCase().trim() +"',"+
							"nama_pemilik='"+ tpemilik.getText().toString().toUpperCase().trim() +"', nama_kontak='"+ tkontak.getText().toString().toUpperCase().trim() +"',"+
							"tgl_lahir='"+ ttgl_lahir.getText().toString().trim() +"', email='"+ temail.getText().toString().toUpperCase().trim() +"',"+
							"telp1='"+ ttelp1.getText().toString().toUpperCase().trim() +"', telp2='"+ ttelp2.getText().toString().toUpperCase().trim() +"',"+
							"no_hp1='"+ thp1.getText().toString().toUpperCase().trim() +"', no_hp2='"+ thp2.getText().toString().toUpperCase().trim() +"',"+
							"jenis_noo='"+ (String) tjenisnoo.getSelectedItem() +"'"+  
							" where no_pengajuan='"+ nopengajuan +"'";
					
						db.execSQL(sql);
					
				}else {
				
				
					nobukti=isi_bukti();
				
					Date date= new Date();
					DateFormat tanggalsekarang= new SimpleDateFormat("dd/MM/yyyy");
				
					String tglsekarang = tanggalsekarang.format(date);
				
					String sql= "INSERT INTO noo (no_pengajuan, kd_sales, tanggal,"+
						"nama_outlet, alamat, no_ktp, nama_pemilik,"+
						"nama_kontak, tgl_lahir, email, telp1,"+
						"telp2, no_hp1, no_hp2,jenis_noo, step,upl) VALUES("+
						"'"+ nobukti +"','"+ kd_sales + "','"+ tglsekarang +"',"+
						"'"+ tnama.getText().toString().toUpperCase().trim() +"','"+ talamat.getText().toString().toUpperCase().trim() +"',"+
						"'"+ tktp.getText().toString().toUpperCase().trim() +"','"+ tpemilik.getText().toString().toUpperCase().trim() +"',"+
						"'"+ tkontak.getText().toString().toUpperCase().trim() +"','"+ ttgl_lahir.getText().toString().trim() +"','"+ temail.getText().toString().toUpperCase().trim() +"',"+
						"'"+ ttelp1.getText().toString().toUpperCase().trim() +"','"+ ttelp2.getText().toString().toUpperCase().trim() +"',"+
						"'"+ thp1.getText().toString().toUpperCase().trim() +"','"+ thp2.getText().toString().toUpperCase().trim() +"','"+ (String) tjenisnoo.getSelectedItem() +"',1,0)";
						
					db.execSQL(sql);
				
				}
				
				Toast.makeText(jpnoo.this, "Step 1 - Disimpan", Toast.LENGTH_LONG).show();
				
				Intent myintent=new Intent(getBaseContext(), Jpnoo2.class);
				
					myintent.putExtra("nobukti", nobukti);
					myintent.putExtra("nama_noo", tnama.getText().toString().toUpperCase().trim());
					myintent.putExtra("kd_sales", kd_sales);
					myintent.putExtra("nama_sales", nama_sales);
					
					startActivity(myintent);
					finish();
				
			}catch (Exception e){
			//	Log.v("error noo step 1", e.toString());
				Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
			}
			
			
		}
	};
	
	private String isi_bukti(){
		 
		 String nobukti;
		 
		 DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		 Date date = new Date(); 
		 
		 String tanggals = dateFormat.format(date);
		 
		// Calendar cal = Calendar.getInstance();
		 String tgl= tanggals.substring(0, 2) ;  //cal.get(Calendar.DATE) ;
		 String bln= tanggals.substring(3,5); // cal.get(Calendar.MONTH);
		 String thn =tanggals.substring(6, 8) ; // cal.get(Calendar.YEAR);

		 String jam = tanggals.substring(9, 11); //cal.get(Calendar.HOUR);
		 String menit = tanggals.substring(12, 14);  //cal.get(Calendar.MINUTE);
		 String detik = tanggals.substring(15, 17);  //cal.get(Calendar.SECOND);
		 
		 
		 nobukti = "N-"+ tgl + bln + thn + jam + menit + detik + kd_sales;
		 
		 Toast.makeText( getApplicationContext(), "No Bukti : " + nobukti, Toast.LENGTH_SHORT).show();
		 return nobukti;
		 
	 };
	
	private void isi_all() {
		
		if (nopengajuan.trim().length()!=0){
			
			db = dba.getReadableDatabase();
			
			try {
				
				String sql ="SELECT _id,nama_outlet, alamat, no_ktp,"+
								"nama_pemilik, nama_kontak, tgl_lahir, email,"+
									"telp1, telp2, no_hp1, no_hp2,jenis_noo"+
										" FROM noo WHERE no_pengajuan='"+ nopengajuan +"'";
				Cursor cursor = dba.SelectData(db,sql);
				cursor.moveToFirst(); 
				
		        int jml_baris = cursor.getCount();
		        if (jml_baris==1){
		        
		        	tnama.setText(cursor.getString(cursor.getColumnIndex("nama_outlet")));
		        	talamat.setText(cursor.getString(cursor.getColumnIndex("alamat")));
		        	tktp.setText(cursor.getString(cursor.getColumnIndex("no_ktp")));
		        	tpemilik.setText(cursor.getString(cursor.getColumnIndex("nama_pemilik")));
		        	tkontak.setText(cursor.getString(cursor.getColumnIndex("nama_kontak")));
		        	ttgl_lahir.setText(cursor.getString(cursor.getColumnIndex("tgl_lahir")));
		        	temail.setText(cursor.getString(cursor.getColumnIndex("email")));
		        	ttelp1.setText(cursor.getString(cursor.getColumnIndex("telp1")));
		        	ttelp2.setText(cursor.getString(cursor.getColumnIndex("telp2")));
		        	thp1.setText(cursor.getString(cursor.getColumnIndex("no_hp1")));
		        	thp2.setText(cursor.getString(cursor.getColumnIndex("no_hp2")));
		        	
		        	String jenisnoo = cursor.getString(cursor.getColumnIndex("jenis_noo"));
		        	
		        	if (jenisnoo.toUpperCase().equals("KREDIT")){
		        		tjenisnoo.setSelection(0);
		        	}else{
		        		tjenisnoo.setSelection(1);
		        	}
		        	
		        	cursor.close();
		        	
		        }else {
		        	
		        	tnama.setText("");
		        	talamat.setText("");
		        	tktp.setText("");
		        	tpemilik.setText("");
		        	tkontak.setText("");
		        	ttgl_lahir.setText("");
		        	temail.setText("");
		        	ttelp1.setText("");
		        	ttelp2.setText("");
		        	thp1.setText("");
		        	thp2.setText("");
		        	
		        }
		        
				
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
			}
			
		}
		
	};
	 
	 @Override
	    protected void onResume(){
	    	super.onResume();
	    	
	    	if (nopengajuan.trim().length() !=0){
	    		isi_all();
	    	}
	    	
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
	
}
