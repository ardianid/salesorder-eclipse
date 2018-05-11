package slo.fajar.lestari;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Jpnoo2 extends Activity {
	
	private DBAdapter dba=null;
	private SQLiteDatabase db=null;
	private Cursor cursor_tipeoutl;
	private Cursor cursor_rayon;
	private Cursor cursor_rute;
	private Cursor cursor_pasar;
	private Cursor cursor_group;
	private Cursor cursor_area;
	protected ListAdapter  adapter_tipeoutl;
	protected ListAdapter  adapter_rayon;
	protected ListAdapter  adapter_rute;
	protected ListAdapter  adapter_pasar;
	protected ListAdapter  adapter_group;
	protected ListAdapter  adapter_area;
	
	TextView group_outl;
	TextView tipe_outl;
	Spinner jenis_outl;
	TextView rayon;
	TextView rute;
	TextView pasar;
	Spinner lingkungan;
	TextView norek;
	TextView bank;
	TextView atasnama;
	TextView namanoo;
	TextView tarea;
	
	String[] lingk= {"DIDALAM PASAR","DILUAR PASAR"};
	String[] jeniscu={"--","DALAM KOTA","LUAR KOTA"};
	
	Button btnext;
	Button btback;
	Button btclose;
	
	String  kodegroup;
	String kodepasar;
	String nopengajuan;
	String kd_sales;
	String nama_sales;
	String kodearea;
	
	Boolean iinsert;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pnoo2);
        
        Bundle bnd= getIntent().getExtras();
        
        nopengajuan = bnd.getString("nobukti");
        kd_sales = bnd.getString("kd_sales");
        nama_sales = bnd.getString("nama_sales");
        
        namanoo = (TextView)findViewById(R.id.tnama_noo2);
        namanoo.setText(bnd.getString("nama_noo"));
        
        dba = new DBAdapter(this);
        
        tipe_outl = (TextView)findViewById(R.id.ttipe_noo);
        rayon = (TextView)findViewById(R.id.trayon_noo);
        rute = (TextView)findViewById(R.id.trute_noo);
        pasar = (TextView)findViewById(R.id.tpasar_noo);
        norek = (TextView)findViewById(R.id.tnorek_noo);
        bank = (TextView)findViewById(R.id.tnorek_noo);
        atasnama = (TextView)findViewById(R.id.tnamarek_noo);
        tarea = (TextView)findViewById(R.id.tarea_noo);
        
        btnext = (Button)findViewById(R.id.bt_next2_noo);
        btback = (Button)findViewById(R.id.bt_back2_noo);
        btclose = (Button)findViewById(R.id.btcancel_noo2);
        
        jenis_outl = (Spinner)findViewById(R.id.tjenis_noo);
        ArrayAdapter<String> jeniso=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,jeniscu);
        jenis_outl.setAdapter(jeniso);
        
        lingkungan = (Spinner)findViewById(R.id.tlingkungan_noo);
        ArrayAdapter<String> link=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lingk);
        lingkungan.setAdapter(link);
        
        group_outl = (TextView)findViewById(R.id.tgroup_noo);
        
        
        /* group_outl.setOnKeyListener(tgroup_list);
        tipe_outl.setOnKeyListener(tipeOutl_list);
        rayon.setOnKeyListener(rayon_list);
        rute.setOnKeyListener(rute_list);
        pasar.setOnKeyListener(pasar_list);
        norek.setOnKeyListener(norek_list);
        bank.setOnKeyListener(bank_list);
        atasnama.setOnKeyListener(atasnama_list); */
        
        group_outl.setOnFocusChangeListener(tgroup_onfocus_list);
        tipe_outl.setOnFocusChangeListener(tipeOutl_onfocus_list);
        rayon.setOnFocusChangeListener(rayon_onfocus_list);
        rute.setOnFocusChangeListener(rute_onfocus_list);
        pasar.setOnFocusChangeListener(pasar_onfocus_list);
        tarea.setOnFocusChangeListener(area_list);
        
        btnext.setOnClickListener(btnext_list);
        btback.setOnClickListener(btback_list);
        btclose.setOnClickListener(btcancel_list);
        
	} // akhir dari event create
	
private void isi_all(){
	
	db = dba.getReadableDatabase();
	
	try {
		
		String sql="SELECT noo._id, noo.no_pengajuan, noo.group_outl, noo.tipe_outl,"+
				  " noo.jenis_outl, noo.rayon, noo.rute, noo.pasar,"+
				  " noo.lingkungan, noo.no_rek, noo.atas_nama_rek, noo.bank,"+
				  " noo.step, ms_groupcust.nama AS namagroup, ms_pasar.nama_pasar,"+
				  " noo.kd_kelurahan, ms_area.nama_kelurahan"+
				" FROM noo LEFT JOIN"+
				  " ms_groupcust ON noo.group_outl = ms_groupcust.kode LEFT JOIN"+
				  " ms_pasar ON noo.pasar = ms_pasar.kd_pasar LEFT JOIN"+
				  " ms_area ON noo.kd_kelurahan = ms_area.kd_kelurahan"+
					" WHERE noo.no_pengajuan='"+ nopengajuan +"'";	
		
		Cursor cursor = dba.SelectData(db,sql);
		cursor.moveToFirst(); 
		
        int jml_baris = cursor.getCount();
        	//jml_baris -= 1;
        if (jml_baris==1){
        	
        	String sinsert=cursor.getString(cursor.getColumnIndex("step"));
        	Integer sinsert2 = Integer.valueOf(sinsert);
        	
        	
        	if (sinsert2 > 2){
        		iinsert = false;
        	}else{
        		iinsert = true;
        	}
        	
        	if (sinsert2 <2){
        		
        		kodegroup="";
            	kodepasar="";
            	kodearea="";
            	
            	group_outl.setText("");
            	tipe_outl.setText("");
            	rayon.setText("");
            	rute.setText("");
            	pasar.setText("");
            	norek.setText("");
            	atasnama.setText("");
            	bank.setText("");
            	tarea.setText("");
        		
        		return;
        	}
        	
        	kodegroup = cursor.getString(cursor.getColumnIndex("group_outl"));
        	kodepasar = cursor.getString(cursor.getColumnIndex("pasar"));
        	kodearea = cursor.getString(cursor.getColumnIndex("kd_kelurahan")); 
        	
        	group_outl.setText(cursor.getString(cursor.getColumnIndex("namagroup")));
        	tipe_outl.setText(cursor.getString(cursor.getColumnIndex("tipe_outl")));
        	
        	String jenisoutl=cursor.getString(cursor.getColumnIndex("jenis_outl"));
        	
        	if (jenisoutl.equals("--")){
        		jenis_outl.setSelection(0);
        	}else if (jenisoutl.equals("DALAM KOTA")){
        		jenis_outl.setSelection(1);
        	}else{
        		jenis_outl.setSelection(2);
        	}
        	
        	rayon.setText(cursor.getString(cursor.getColumnIndex("rayon")));
        	rute.setText(cursor.getString(cursor.getColumnIndex("rute")));
        	pasar.setText(cursor.getString(cursor.getColumnIndex("nama_pasar")));
        	
        	tarea.setText(cursor.getString(cursor.getColumnIndex("nama_kelurahan")));
        	
        	String slingkungan=cursor.getString(cursor.getColumnIndex("lingkungan"));
        	
        	if (slingkungan.equals("DIDALAM PASAR")) {
        		lingkungan.setSelection(0);
        	}else{
        		lingkungan.setSelection(1);
        	}
        	
        	norek.setText(cursor.getString(cursor.getColumnIndex("no_rek")));
        	atasnama.setText(cursor.getString(cursor.getColumnIndex("atas_nama_rek")));
        	bank.setText(cursor.getString(cursor.getColumnIndex("bank")));
        	
        	cursor.close();
        	
        }else {
        	
        	iinsert = true;
        	
        	kodegroup="";
        	kodepasar="";
        	kodearea="";
        	
        	group_outl.setText("");
        	tipe_outl.setText("");
        	rayon.setText("");
        	rute.setText("");
        	pasar.setText("");
        	norek.setText("");
        	atasnama.setText("");
        	bank.setText("");
        	tarea.setText("");
        	
        }
		
	} catch (Exception e) {
		// TODO: handle exception
		Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
	}
	
	
};
	
View.OnKeyListener tgroup_list =new  View.OnKeyListener() {
		
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
				tipe_outl.requestFocus();
				return true;
			}else {
				return false;
			}
		}
	};
	
View.OnKeyListener tipeOutl_list =new  View.OnKeyListener() {
		
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
				jenis_outl.requestFocus();
				return true;
			}else if (keyCode== KeyEvent.ACTION_UP){
				group_outl.requestFocus();
				return true;
			}else {
				return false;
			}
		}
	};

View.OnKeyListener rayon_list =new  View.OnKeyListener() {
		
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
				rute.requestFocus();
				return true;
			}else if (keyCode== KeyEvent.ACTION_UP){
				jenis_outl.requestFocus();
				return true;
			}else {
				return false;
			}
		}
	};
View.OnKeyListener rute_list =new  View.OnKeyListener() {
		
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
				pasar.requestFocus();
				return true;
			}else if (keyCode== KeyEvent.ACTION_UP){
				rute.requestFocus();
				return true;
			}else {
				return false;
			}
		}
	};

View.OnKeyListener pasar_list =new  View.OnKeyListener() {
		
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
				lingkungan.requestFocus();
				return true;
			}else if (keyCode== KeyEvent.ACTION_UP){
				rute.requestFocus();
				return true;
			}else {
				return false;
			}
		}
	};

View.OnKeyListener norek_list =new  View.OnKeyListener() {
		
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
				bank.requestFocus();
				return true;
			}else if (keyCode== KeyEvent.ACTION_UP){
				lingkungan.requestFocus();
				return true;
			}else {
				return false;
			}
		}
	};
View.OnKeyListener bank_list =new  View.OnKeyListener() {
		
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
				atasnama.requestFocus();
				return true;
			}else if (keyCode== KeyEvent.ACTION_UP){
				norek.requestFocus();
				return true;
			}else {
				return false;
			}
		}
	};
View.OnKeyListener atasnama_list =new  View.OnKeyListener() {
		
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
				btnext.requestFocus();
				return true;
			}else if (keyCode== KeyEvent.ACTION_UP){
				bank.requestFocus();
				return true;
			}else {
				return false;
			}
		}
	};

View.OnFocusChangeListener tgroup_onfocus_list = new View.OnFocusChangeListener() {
		
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			
			if (!hasFocus){
				
				if (group_outl.getText().toString().trim().length()==0) {
					kodegroup="";
					return;
				};
				
				kodegroup="";
				
				String sql="select * from ms_groupcust where nama like '%"+ group_outl.getText().toString().trim() +"%'";
				
				db = dba.getReadableDatabase(); 
				
				try {
					
					Cursor cursor2 = dba.SelectData(db, sql);
	        		cursor2.moveToFirst();
					
	        		int jml_baris2=cursor2.getCount() ;
	        		
	        		if (jml_baris2 > 1) {
	        			showDialog(5);
	        			cursor2.close();
	        		}
	        		else if (jml_baris2==1){
	        			
	        			kodegroup= cursor2.getString(cursor2.getColumnIndex("kode"));
	        			group_outl.setText(cursor2.getString(cursor2.getColumnIndex("nama")));
	        			
	        			cursor2.close();
	        			
	        		}else{
	        			Toast.makeText(getBaseContext(),"Group Outlet tidak ada", Toast.LENGTH_LONG).show();
	        			kodegroup="";
	        			group_outl.setText("");
	        		}
	        		
				}catch (Exception e) {
					Toast.makeText(Jpnoo2.this, e.toString(), Toast.LENGTH_LONG).show();
				}
				
				
				
			};
			
		}
	}; // akhir dari onfocus tipe outle
	
View.OnFocusChangeListener tipeOutl_onfocus_list = new View.OnFocusChangeListener() {
	
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		
		if (!hasFocus){
			
			if (tipe_outl.getText().toString().trim().length()==0) {
				return;
			};
			
			String sql="select _id,keterangan from ms_other where tipe='Type Customer' and keterangan like '%"+ tipe_outl.getText().toString().trim() +"%'";
			
			db = dba.getReadableDatabase(); 
			
			try {
				
				Cursor cursor2 = dba.SelectData(db, sql);
        		cursor2.moveToFirst();
				
        		int jml_baris2=cursor2.getCount() ;
        		
        		if (jml_baris2 > 1) {
        			showDialog(1);
        			cursor2.close();
        		}
        		else if (jml_baris2==1){
        			
        			tipe_outl.setText(cursor2.getString(cursor2.getColumnIndex("keterangan")));
        			
        			cursor2.close();
        			
        		}else{
        			Toast.makeText(getBaseContext(),"Tipe Outlet tidak ada", Toast.LENGTH_LONG).show();
        			tipe_outl.setText("");
        		}
        		
			}catch (Exception e) {
				Toast.makeText(Jpnoo2.this, e.toString(), Toast.LENGTH_LONG).show();
			}
			
			
			
		};
		
	}
}; // akhir dari onfocus tipe outlet

View.OnFocusChangeListener rayon_onfocus_list = new View.OnFocusChangeListener() {
	
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		
		if (!hasFocus){
			
			if (rayon.getText().toString().trim().length()==0) {
				return;
			};
			
			String sql="select _id,keterangan from ms_other where tipe='Rayon' and keterangan like '%"+ rayon.getText().toString().trim() +"%'";
			
			db = dba.getReadableDatabase(); 
			
			try {
				
				Cursor cursor2 = dba.SelectData(db, sql);
        		cursor2.moveToFirst();
				
        		int jml_baris2=cursor2.getCount() ;
        		
        		if (jml_baris2 > 1) {
        			showDialog(2);
        			cursor2.close();
        		}
        		else if (jml_baris2==1){
        			
        			rayon.setText(cursor2.getString(cursor2.getColumnIndex("keterangan")));
        			
        			cursor2.close();
        			
        		}else{
        			Toast.makeText(getBaseContext(),"Rayon tidak ada", Toast.LENGTH_LONG).show();
        		}
        		
			}catch (Exception e) {
				Toast.makeText(Jpnoo2.this, e.toString(), Toast.LENGTH_LONG).show();
				rayon.setText("");
			}
			
			
			
		};
		
	}
}; // akhir dari onfocus rayon

View.OnFocusChangeListener rute_onfocus_list = new View.OnFocusChangeListener() {
	
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		
		if (!hasFocus){
			
			if (rute.getText().toString().trim().length()==0) {
				return;
			};
			
			String sql="select _id,keterangan from ms_other where tipe='Diskrit' and keterangan like '%"+ rute.getText().toString().trim() +"%'";
			
			db = dba.getReadableDatabase(); 
			
			try {
				
				Cursor cursor2 = dba.SelectData(db, sql);
        		cursor2.moveToFirst();
				
        		int jml_baris2=cursor2.getCount() ;
        		
        		if (jml_baris2 > 1) {
        			showDialog(3);
        			cursor2.close();
        		}
        		else if (jml_baris2==1){
        			
        			rute.setText(cursor2.getString(cursor2.getColumnIndex("keterangan")));
        			
        			cursor2.close();
        			
        		}else{
        			Toast.makeText(getBaseContext(),"Rute tidak ada", Toast.LENGTH_LONG).show();
        		}
        		
			}catch (Exception e) {
				Toast.makeText(Jpnoo2.this, e.toString(), Toast.LENGTH_LONG).show();
				rute.setText("");
			}
			
			
			
		};
		
	}
}; // akhir dari onfocus rute
View.OnFocusChangeListener pasar_onfocus_list = new View.OnFocusChangeListener() {
	
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		
		if (!hasFocus){
			
			if (pasar.getText().toString().trim().length()==0) {
				kodepasar="";
				return;
			};
			
			kodepasar="";
			
			String sql="select _id,kd_pasar,nama_pasar from ms_pasar where nama_pasar like '%"+ pasar.getText().toString().trim() +"%'";
			
			db = dba.getReadableDatabase(); 
			
			try {
				
				Cursor cursor2 = dba.SelectData(db, sql);
        		cursor2.moveToFirst();
				
        		int jml_baris2=cursor2.getCount() ;
        		
        		if (jml_baris2 > 1) {
        			showDialog(4);
        			cursor2.close();
        		}
        		else if (jml_baris2==1){
        			
        			kodepasar = cursor2.getString(cursor2.getColumnIndex("kd_pasar"));
        			pasar.setText(cursor2.getString(cursor2.getColumnIndex("nama_pasar")));
        			
        			cursor2.close();
        			
        		}else{
        			Toast.makeText(getBaseContext(),"Pasar tidak ada", Toast.LENGTH_LONG).show();
        			kodepasar="";
        			pasar.setText("");
        		}
        		
			}catch (Exception e) {
				Toast.makeText(Jpnoo2.this, e.toString(), Toast.LENGTH_LONG).show();
			}
			
			
			
		};
		
	}
}; // akhir dari onfocus pasar

View.OnFocusChangeListener area_list = new View.OnFocusChangeListener() {
	
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		
		if (!hasFocus){
			
			if (tarea.getText().toString().trim().length()==0) {
				kodearea="";
				return;
			};
			
			kodearea="";
			
			String sql="SELECT _id, kd_kelurahan, nama_kelurahan"+
					" FROM ms_area where nama_kelurahan like '%"+ tarea.getText().toString().trim() +"%'";
			
			db = dba.getReadableDatabase(); 
			
			try {
				
				Cursor cursor2 = dba.SelectData(db, sql);
        		cursor2.moveToFirst();
				
        		int jml_baris2=cursor2.getCount() ;
        		
        		if (jml_baris2 > 1) {
        			showDialog(6);
        			cursor2.close();
        		}
        		else if (jml_baris2==1){
        			
        			kodearea = cursor2.getString(cursor2.getColumnIndex("kd_kelurahan"));
        			tarea.setText(cursor2.getString(cursor2.getColumnIndex("nama_kelurahan")));
        			
        			cursor2.close();
        			
        		}else{
        			Toast.makeText(getBaseContext(),"Area customer tidak ada", Toast.LENGTH_LONG).show();
        			kodearea="";
        			tarea.setText("");
        		}
        		
			}catch (Exception e) {
				Toast.makeText(Jpnoo2.this, e.toString(), Toast.LENGTH_LONG).show();
			}
			
			
			
		};
		
	}
};

View.OnClickListener btnext_list = new View.OnClickListener() {
	
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		if (tipe_outl.getText().toString().length()==0) {
			Toast.makeText(Jpnoo2.this,"Tipe Outlet tidak boleh kosong", Toast.LENGTH_LONG).show();
			tipe_outl.requestFocus();
			return;
		}
		
		if (rayon.getText().toString().length()==0) {
			Toast.makeText(Jpnoo2.this,"Rayon Outlet tidak boleh kosong", Toast.LENGTH_LONG).show();
			rayon.requestFocus();
			return;
		}
		
		if (tarea.getText().toString().length()==0) {
			Toast.makeText(Jpnoo2.this,"Area Outlet tidak boleh kosong", Toast.LENGTH_LONG).show();
			tarea.requestFocus();
			return;
		}
		
		if (rute.getText().toString().length()==0) {
			Toast.makeText(Jpnoo2.this,"Rute Outlet tidak boleh kosong", Toast.LENGTH_LONG).show();
			rute.requestFocus();
			return;
		}
		
		if (String.valueOf(jenis_outl.getSelectedItem()).toString().length() ==0) {
			Toast.makeText(Jpnoo2.this,"Jenis Outlet tidak boleh kosong", Toast.LENGTH_LONG).show();
			jenis_outl.requestFocus();
			return;
		}
		
		db = dba.getWritableDatabase();
		
		try {
			
			String sql="";
			
			sql="update noo set group_outl='"+ kodegroup +"', tipe_outl='"+ tipe_outl.getText().toString().trim().toUpperCase() +"', jenis_outl='"+ (String)jenis_outl.getSelectedItem() +"', rayon='"+ rayon.getText().toString().trim().toUpperCase() +"',"+
					"rute='"+ rute.getText().toString().trim().toUpperCase() +"', pasar='"+ kodepasar +"', lingkungan='"+ (String)lingkungan.getSelectedItem() +"', no_rek='"+ norek.getText().toString().trim().toUpperCase() +"',"+
					"atas_nama_rek='"+ atasnama.getText().toString().trim().toUpperCase() +"', bank='"+ bank.getText().toString().trim().toUpperCase() +"',kd_kelurahan='"+ kodearea +"'";	
			
			if (iinsert.equals(true)){
				
				sql= sql +  ",step=2 where no_pengajuan='"+ nopengajuan +"'";
				
			}else{
				
				sql= sql +  " where no_pengajuan='"+ nopengajuan +"'";
				
			}
			
			db.execSQL(sql);
			
			Toast.makeText(Jpnoo2.this, "Step 2 - Disimpan", Toast.LENGTH_LONG).show();
			
			Intent myintent=new Intent(getBaseContext(), Jpnoo3.class);
			
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
}; // akhir dari next list

View.OnClickListener btback_list = new View.OnClickListener() {
	
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		Intent myintent=new Intent(getBaseContext(), jpnoo.class);
		
		myintent.putExtra("kd_sales", kd_sales);
		myintent.putExtra("nama_sales", nama_sales);
	//	myintent.putExtra("nama_noo", namanoo.getText().toString().trim().toUpperCase() );
		myintent.putExtra("nopengajuan", nopengajuan);
		
		
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
	 protected Dialog onCreateDialog(int id) {
	 
	  AlertDialog dialogDetails = null;
	 
	  switch (id) {
	  case 1: // tipe outlet
	   LayoutInflater inflater = LayoutInflater.from(this);
	   View dialogview = inflater.inflate(R.layout.view_tipeoutl, null);
	 
	   AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
	   dialogbuilder.setTitle("View Barang");	
	   dialogbuilder.setView(dialogview);
	   dialogDetails = dialogbuilder.create();
	 
	   break;
	   
	  case 2 : // rayon
		  LayoutInflater inflater2 = LayoutInflater.from(this);
		   View dialogview2 = inflater2.inflate(R.layout.view_rayon, null);
		 
		   AlertDialog.Builder dialogbuilder2 = new AlertDialog.Builder(this);
		   dialogbuilder2.setTitle("View Rayon");	
		   dialogbuilder2.setView(dialogview2);
		   dialogDetails = dialogbuilder2.create();
		 
		   break;
	  
	  case 3: // rute
		  LayoutInflater inflater3 = LayoutInflater.from(this);
		   View dialogview3 = inflater3.inflate(R.layout.view_rute, null);
		 
		   AlertDialog.Builder dialogbuilder3 = new AlertDialog.Builder(this);
		   dialogbuilder3.setTitle("View Rute");	
		   dialogbuilder3.setView(dialogview3);
		   dialogDetails = dialogbuilder3.create();
		 
		   break;
		   
	  case 4: // pasar
		  LayoutInflater inflater4 = LayoutInflater.from(this);
		   View dialogview4 = inflater4.inflate(R.layout.view_pasar, null);
		 
		   AlertDialog.Builder dialogbuilder4 = new AlertDialog.Builder(this);
		   dialogbuilder4.setTitle("View Pasar");	
		   dialogbuilder4.setView(dialogview4);
		   dialogDetails = dialogbuilder4.create();
		 
		   break;
		  
	  case 5: // group outl
		  LayoutInflater inflater5 = LayoutInflater.from(this);
		   View dialogview5 = inflater5.inflate(R.layout.view_group_outl, null);
		 
		   AlertDialog.Builder dialogbuilder5 = new AlertDialog.Builder(this);
		   dialogbuilder5.setTitle("View Group Outlet");	
		   dialogbuilder5.setView(dialogview5);
		   dialogDetails = dialogbuilder5.create();
		 
		   break;
		   
	  case 6: // area outlet
		  LayoutInflater inflater6 = LayoutInflater.from(this);
		   View dialogview6 = inflater6.inflate(R.layout.view_area, null);
		 
		   AlertDialog.Builder dialogbuilder6 = new AlertDialog.Builder(this);
		   dialogbuilder6.setTitle("View Area Outlet");	
		   dialogbuilder6.setView(dialogview6);
		   dialogDetails = dialogbuilder6.create();
		 
		   break;
		   
	  }
	 
	  return dialogDetails;
	 } // akhir dialog create
	 
@Override
	 protected void onPrepareDialog(int id, Dialog dialog) {
	 
	  switch (id) {
	  case 1: // tipe outlet
		  final AlertDialog alertDialog = (AlertDialog) dialog;
		  Button cancelbutton = (Button) alertDialog
				  .findViewById(R.id.btv_tipeoutl);

	   ListView list1 = (ListView) alertDialog
	     .findViewById(R.id.lv_tipeoutl);
	   
	   String sql="select _id,keterangan from ms_other where tipe='Type Customer' and keterangan like '%"+ tipe_outl.getText().toString().trim() +"%' order by keterangan";
	   
	   try {
	    
   		cursor_tipeoutl = dba.SelectData(db,sql);
   		
       	adapter_tipeoutl = new SimpleCursorAdapter(
                   this, 
                   R.layout.view_tipeoutl0, 
                   cursor_tipeoutl,
                   new String[] {"keterangan"}, 
                   new int[] {R.id.tv_tipeoutl});
           list1.setAdapter(adapter_tipeoutl);  
           
           
           
   		}catch (Exception e) {
   			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
   		}
	   
	   
	   list1.setOnItemClickListener(new OnItemClickListener() {
		   
		   public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			
			   tipe_outl.setText(cursor_tipeoutl.getString(cursor_tipeoutl.getColumnIndex("keterangan")));
			   
			   jenis_outl.requestFocus(); 
			   alertDialog.dismiss();
			   
		   }}); 
  
	   cancelbutton.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			alertDialog.dismiss();
		}});
	   
	   break;
	   
	  case 2: // rayon
		  
		  final AlertDialog alertDialog2 = (AlertDialog) dialog;
		  Button cancelbutton2 = (Button) alertDialog2
				  .findViewById(R.id.btv_rayon);

	   ListView list2 = (ListView) alertDialog2
	     .findViewById(R.id.lv_rayon);
	   
	   String sql2="select _id,keterangan from ms_other where tipe='Rayon' and keterangan like '%"+ rayon.getText().toString().trim() +"%' order by keterangan";
	   
	   try {
	    
   		cursor_rayon = dba.SelectData(db,sql2);
   		
       	adapter_rayon = new SimpleCursorAdapter(
                   this, 
                   R.layout.view_rayon0, 
                   cursor_rayon,
                   new String[] {"keterangan"}, 
                   new int[] {R.id.tv_rayon});
           list2.setAdapter(adapter_rayon);  
           
           
           
   		}catch (Exception e) {
   			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
   		}
	   
	   
	   list2.setOnItemClickListener(new OnItemClickListener() {
		   
		   public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			
			   rayon.setText(cursor_rayon.getString(cursor_rayon.getColumnIndex("keterangan")));
			   
			   rute.requestFocus(); 
			   alertDialog2.dismiss();
			   
		   }}); 
  
	   cancelbutton2.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			alertDialog2.dismiss();
		}});
	   
	   break;
		  
	  case 3 : // rute
		  
		  final AlertDialog alertDialog3 = (AlertDialog) dialog;
		  Button cancelbutton3 = (Button) alertDialog3
				  .findViewById(R.id.btv_rute);

	   ListView list3 = (ListView) alertDialog3
	     .findViewById(R.id.lv_rute);
	   
	   String sql3="select _id,keterangan from ms_other where tipe='Diskrit' and keterangan like '%"+ rute.getText().toString().trim() +"%' order by keterangan";
	   
	   try {
	    
   		cursor_rute = dba.SelectData(db,sql3);
   		
       	adapter_rute = new SimpleCursorAdapter(
                   this, 
                   R.layout.view_rute0, 
                   cursor_rute,
                   new String[] {"keterangan"}, 
                   new int[] {R.id.tv_rute});
           list3.setAdapter(adapter_rute);  
           
           
           
   		}catch (Exception e) {
   			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
   		}
	   
	   
	   list3.setOnItemClickListener(new OnItemClickListener() {
		   
		   public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			
			   rute.setText(cursor_rute.getString(cursor_rute.getColumnIndex("keterangan")));
			   
			   tarea.requestFocus(); 
			   alertDialog3.dismiss();
			   
		   }}); 
  
	   cancelbutton3.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			alertDialog3.dismiss();
		}});
	   
	   break;		  
		  
	  case 4 : // pasar
		  
		  final AlertDialog alertDialog4 = (AlertDialog) dialog;
		  Button cancelbutton4 = (Button) alertDialog4
				  .findViewById(R.id.btv_pasar);

	   ListView list4 = (ListView) alertDialog4
	     .findViewById(R.id.lv_pasar);
	   
	   String sql4="select _id,kd_pasar,nama_pasar from ms_pasar where nama_pasar like '%"+ pasar.getText().toString().trim() +"%'";
	   
	   try {
	    
   		cursor_pasar = dba.SelectData(db,sql4);
   		
       	adapter_pasar = new SimpleCursorAdapter(
                   this, 
                   R.layout.view_pasar01, 
                   cursor_pasar,
                   new String[] {"kd_pasar","nama_pasar"}, 
                   new int[] {R.id.tv_kdpasar,R.id.tv_namapasar});
           list4.setAdapter(adapter_pasar);  
           
           
           
   		}catch (Exception e) {
   			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
   		}
	   
	   
	   list4.setOnItemClickListener(new OnItemClickListener() {
		   
		   public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			   
			   kodepasar = cursor_pasar.getString(cursor_pasar.getColumnIndex("kd_pasar"));
			   pasar.setText(cursor_pasar.getString(cursor_pasar.getColumnIndex("nama_pasar")));
			   
			   lingkungan.requestFocus(); 
			   alertDialog4.dismiss();
			   
		   }}); 
  
	   cancelbutton4.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			alertDialog4.dismiss();
		}});
	   
	   break;
		
	  case 5 : // group outl
		  
		  final AlertDialog alertDialog5 = (AlertDialog) dialog;
		  Button cancelbutton5 = (Button) alertDialog5
				  .findViewById(R.id.btv_group_outl);

	   ListView list5 = (ListView) alertDialog5
	     .findViewById(R.id.lv_group_outl);
	   
	   String sql5="select * from ms_groupcust where nama like '%"+ group_outl.getText().toString().trim() +"%'";
	   
	   try {
	    
   		cursor_group = dba.SelectData(db,sql5);
   		
       	adapter_group = new SimpleCursorAdapter(
                   this, 
                   R.layout.view_group_outl0, 
                   cursor_group,
                   new String[] {"kode","nama"}, 
                   new int[] {R.id.tv_kdgroup_outl,R.id.tv_namagroup_outl});
           list5.setAdapter(adapter_group);  
           
           
           
   		}catch (Exception e) {
   			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
   		}
	   
	   
	   list5.setOnItemClickListener(new OnItemClickListener() {
		   
		   public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			   
			   kodegroup= cursor_group.getString(cursor_group.getColumnIndex("kode"));
			   group_outl.setText(cursor_group.getString(cursor_group.getColumnIndex("nama")));
			   
			   tipe_outl.requestFocus(); 
			   alertDialog5.dismiss();
			   
		   }}); 
  
	   cancelbutton5.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			alertDialog5.dismiss();
		}});
	   
	   break;
	   
	  case 6 : // Area outlet
		  
		  final AlertDialog alertDialog6 = (AlertDialog) dialog;
		  Button cancelbutton6 = (Button) alertDialog6
				  .findViewById(R.id.btv_area);

	   ListView list6 = (ListView) alertDialog6
	     .findViewById(R.id.lv_area);
	   
	   String sql6="SELECT _id, kd_kelurahan, nama_kelurahan,nama_kec,nama_kab"+
				" FROM ms_area where nama_kelurahan like '%"+ tarea.getText().toString().trim() +"%' limit 150";
	   
	   try {
	    
   		cursor_area = dba.SelectData(db,sql6);
   		
       	adapter_area = new SimpleCursorAdapter(
                   this, 
                   R.layout.view_area0, 
                   cursor_area,
                   new String[] {"kd_kelurahan","nama_kelurahan","nama_kec","nama_kab"}, 
                   new int[] {R.id.tv_kdgroup_area,R.id.tv_namakel_area,R.id.tv_namakec_area,R.id.tv_namakab_area});
           list6.setAdapter(adapter_area);  
           
           
   		}catch (Exception e) {
   			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
   		}
	   
	   
	   list6.setOnItemClickListener(new OnItemClickListener() {
		   
		   public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			   
			   kodearea= cursor_area.getString(cursor_area.getColumnIndex("kd_kelurahan"));
			   tarea.setText(cursor_area.getString(cursor_area.getColumnIndex("nama_kelurahan")));
			   
			   pasar.requestFocus(); 
			   alertDialog6.dismiss();
			   
		   }}); 
  
	   cancelbutton6.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			alertDialog6.dismiss();
		}});
	   
	   break;
	   
	  }
	  
	 } // akhir dari on prepare dialog
	 
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
	 
	 
} // akhir dari activity
