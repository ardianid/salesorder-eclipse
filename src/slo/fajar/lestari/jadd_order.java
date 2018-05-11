package slo.fajar.lestari;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class jadd_order extends Activity {

	private DBAdapter dba=null;
	private SQLiteDatabase db=null;
	private Cursor dbcursor;
	protected ListAdapter  adapter;
	
	EditText tkode;
	EditText tqty1;
	EditText tqty2;
	EditText tqty3;
	EditText tnama;
	
	TextView tsat1;
	TextView tsat2;
	TextView tsat3;
	
	
	ImageButton btcari;
	Button btok;
	
	String sqlview=null;
	String nobukti;
	
	String kd_sales;
	String nama_sales;
	String kd_cust;
	String nama_cust;
	String longi;
	String loti;
	String carabayar;
	
	String longitrans;
	String lotitrans;
	String jarak;
	String perubahan2;
	
	String kdkondisi;
	
	Boolean kodebarangok;
	
	int addbukti;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {                           
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_order);
        
        Bundle bnd= getIntent().getExtras();
        
        kd_sales = bnd.getString("kd_sales");
        nama_sales = bnd.getString("nama_sales");
        nobukti = bnd.getString("nobukti");
        kd_cust = bnd.getString("kd_cust");
        nama_cust = bnd.getString("nama_cust");
        longi = bnd.getString("longi");
        loti = bnd.getString("loti");
        carabayar = bnd.getString("carabayar");
        
        longitrans = bnd.getString("longi_now");
        lotitrans = bnd.getString("loti_now");
        jarak = bnd.getString("jarak");
        perubahan2 = bnd.getString("perubahan");
        
        kdkondisi = bnd.getString("kondisi");
        
        kodebarangok =false;
        
       // Log.v("akhirnya", bnd.getString("perubahan"));
        
       // 	Toast.makeText(getBaseContext(), "(Toko)-> Longitude : "+ longi +" Latitude : "+ loti, Toast.LENGTH_LONG).show();
	//		Toast.makeText(getBaseContext(), "(Saat ini)-> Longitude : "+ longitrans +" Latitude : "+ lotitrans, Toast.LENGTH_LONG).show();
		//	Toast.makeText(getBaseContext(), "(Jarak) : "+ jarak , Toast.LENGTH_LONG).show();
        
        dba = new DBAdapter(this);
        
        if (nobukti.length()==0){
        	isi_bukti();
        	//addbukti=0;
        }else{
        	addbukti=1;
        };
        
        
        tkode = (EditText)findViewById(R.id.tkode_add);
        
        tqty1 = (EditText)findViewById(R.id.tqty1_add);
        tqty2 = (EditText)findViewById(R.id.tqty2_add);
        tqty3 = (EditText)findViewById(R.id.tqty3_add);
        
        tnama = (EditText)findViewById(R.id.tnama_add2);
        tsat1 = (TextView)findViewById(R.id.tsat1_add);
        tsat2 = (TextView)findViewById(R.id.tsat2_add);
        tsat3 = (TextView)findViewById(R.id.tsat3_add);
        
     //   btcari = (ImageButton)findViewById(R.id.btcari_add);
        btok = (Button)findViewById(R.id.btback_add);
        final Button smp= (Button)findViewById(R.id.btok_add);
        
        tkode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus){
					
					if (tkode.getText().toString().trim().length()==0) {
						
						kodebarangok=false;
						tnama.setText("");
						
						return;
					}else{
						kodebarangok=false;
						cekjml_kode(tkode.getText().toString().toUpperCase().trim(),1);
					}
					
					
				};
			}
		});
		
        tnama.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus){
					
					if (tnama.getText().toString().trim().length()==0) {
						
						kodebarangok=false;
						tkode.setText("");
						
						return;
					}else{
						kodebarangok=false;
						cekjml_kode(tnama.getText().toString().toUpperCase().trim(),0);
					}
					
					
				};
			}
		});
        
        btok.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
        
        
        smp.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (tkode.getText().toString().trim().length()==0) {
					Toast.makeText( getApplicationContext(), "Kode barang tidak boleh kosong", Toast.LENGTH_SHORT).show();
					tkode.requestFocus();
					return;
				};
				
				if (kodebarangok.equals(false)) {
					Toast.makeText( getApplicationContext(), "Kode barang tidak ditemukan,periksa kembali", Toast.LENGTH_SHORT).show();
					tkode.requestFocus();
					return;
				};
				
				Double qty1;
				Double qty2;
				Double qty3;
				
				if (tqty1.getText().toString().trim().length()==0){
					qty1 = Double.parseDouble("0");
				}else{
					qty1 = Double.parseDouble(tqty1.getText().toString());
				}
				
				if (tqty2.getText().toString().trim().length()==0){
					qty2 = Double.parseDouble("0");
				}else{
					qty2 = Double.parseDouble(tqty2.getText().toString());
				}
				
				if (tqty3.getText().toString().trim().length()==0){
					qty3 = Double.parseDouble("0");
				}else{
					qty3 = Double.parseDouble(tqty3.getText().toString());
				}
				
				
				if (qty1== Double.parseDouble("0")  && qty2==Double.parseDouble("0") && qty3==Double.parseDouble("0")) {
					Toast.makeText( getApplicationContext(), "Barang yang akan diorder tidak boleh 0", Toast.LENGTH_SHORT).show();
					tqty1.requestFocus();
					return;
				};
				
				simpan();
				
				
			}
		});
        
        tkode.setOnKeyListener(new View.OnKeyListener() {
			
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				
				if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
					tqty1.requestFocus();
					return true;
				}else {
					return false;
				}
			}
		});
        /*
        tqty1.setOnKeyListener(new View.OnKeyListener() {
			
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				
				if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
					tqty2.requestFocus();
					return true;
				}else if (keyCode== KeyEvent.ACTION_UP){
					tkode.requestFocus();
					return true;
				}else {
					return false;
				}
			}
		});
       
        tqty2.setOnKeyListener(new View.OnKeyListener() {
			
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				
				if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
					tqty3.requestFocus();
					return true;
				}else if (keyCode== KeyEvent.ACTION_UP){
					tqty2.requestFocus();
					return true;
				}else {
					return false;
				}
			}
		});
        
        tqty3.setOnKeyListener(new View.OnKeyListener() {
			
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				
				if ((keyCode== KeyEvent.KEYCODE_ENTER ) ||(keyCode== KeyEvent.ACTION_DOWN )) {
					smp.requestFocus();
					return true;
				}else if (keyCode== KeyEvent.ACTION_UP){
					tqty2.requestFocus();
					return true;
				}else {
					return false;
				}
			}
		}); */
            
	}
	
	protected void simpan(){
		
		db = dba.getWritableDatabase();
		
		try {
			
			Date date= new Date();
			DateFormat jam=new SimpleDateFormat("HH:mm:ss");
			DateFormat tanggalsekarang= new SimpleDateFormat("dd/MM/yyyy");
			
			
			String jambukti = jam.format(date);
			String tglsekarang = tanggalsekarang.format(date);
			
			Double JarakSebenarnya = 0.0;
			
			if (jarak == null){
				JarakSebenarnya=0.0;
			}else if (jarak.length()==0){
				JarakSebenarnya=0.0;
			}else{
				JarakSebenarnya = Double.valueOf(jarak);
			}
			
			Float longinow= null;
			Float latinow= null;
			
			if (longitrans == null) {
				longinow = Float.valueOf(0);
			}else if (longitrans.length() ==0){
				longinow = Float.valueOf(0);
			}else{
				longinow = Float.valueOf(longitrans);
			}
			
			if (lotitrans == null) {
				latinow = Float.valueOf(0);
			}else if (lotitrans.length() ==0){
				latinow = Float.valueOf(0);
			}else{
				latinow = Float.valueOf(lotitrans);
			}
			
			if (addbukti==0){
				String sql1="";
				sql1="insert into faktur_h (trans,lat,long,jam,no_bukti,kd_cust,tanggal,kd_salesman,carabayar,jarak,jamperubahan,jamperubahan2,jarakperubahan,lat2,long2,kd_kondisi) values(0,'"+ latinow +"','"+ longinow +"','"+ jambukti +"','"+ nobukti +"','"+ kd_cust +"','"+ tglsekarang +"','"+ kd_sales +"','"+ carabayar +"',"+ JarakSebenarnya +",'00:00:00','00:00:00',0,'0','0','"+ kdkondisi +"')";
				db.execSQL(sql1);
				
				addbukti=1;
				
			}else{
				
				if (Integer.valueOf(perubahan2)==0){
				}else {
				
					String sql1="";
					sql1="update faktur_h set jamperubahan='"+ jambukti +"',jarakperubahan="+ JarakSebenarnya +",lat2='"+ latinow +"',long2='"+ longinow +"' where no_bukti='"+ nobukti +"'";
					db.execSQL(sql1);
				
				}
				
			};
			
			String kode = tkode.getText().toString().trim();
			
			int qty1;
			int qty2;
			int qty3;
			
			if (tqty1.getText().toString().trim().length()==0){
				qty1=0;
			}else{
				qty1 = Integer.parseInt(tqty1.getText().toString());;
			}
			
			if (tqty2.getText().toString().trim().length()==0){
				qty2=0;
			}else{
				qty2 = Integer.parseInt(tqty2.getText().toString());;
			}
			
			if (tqty3.getText().toString().trim().length()==0){
				qty3=0;
			}else{
				qty3 = Integer.parseInt(tqty3.getText().toString());;
			}
			
			//DateFormat jam=new SimpleDateFormat("HH:mm:ss"); 
			
			String jamsekarang = jam.format(date);
			
			String sql="insert into faktur_d (trans,no_bukti_h,kd_barang,qty1,qty2,qty3,jam) values(0,'"+ nobukti +"','"+ kode +"',"+ qty1 +","+ qty2 +","+ qty3 +",'"+ jamsekarang +"')";
			db.execSQL(sql);
			
			if (Integer.valueOf(perubahan2)==0){
				String sql2="update faktur_h set jamselesai='"+ jamsekarang +"',trans=0 where no_bukti='"+  nobukti +"'";
				db.execSQL(sql2);
			}else{
				String sql2="update faktur_h set jamperubahan2='"+ jamsekarang +"',trans=0 where no_bukti='"+  nobukti +"'";
				db.execSQL(sql2);
			}
			
			
			Toast.makeText(getBaseContext(), "Order barang disimpan", Toast.LENGTH_LONG).show();
			
			tkode.setText("");
			tnama.setText("");
			
			tqty1.setText("");
			tqty2.setText("");
			tqty3.setText("");
			
			tsat1.setText("");
			tsat2.setText("");
			tsat3.setText("");
			
			tkode.requestFocus();
			
			kodebarangok=false;
			
					
		}catch (Exception e){
			//Log.v("error insert faktur detail", e.toString());
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		
	}
	
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
	//	db.close();
	}
	
	private void cekjml_kode(String kodebarang,Integer kode){
		
		db = dba.getReadableDatabase(); 
		
		kodebarangok=false;
		
		try {
			
			
			String sql="";
			
			if (kode==1){
				sql="select kd_barang,nama_barang,satuan1,satuan2,satuan3 from ms_barang where kd_barang='"+ kodebarang.trim() +"'";
			}else{
				sql="select kd_barang,nama_barang,satuan1,satuan2,satuan3 from ms_barang where nama_barang='"+ kodebarang.trim() +"'";
			}
			
			
			Cursor cursor = dba.SelectData(db,sql);
			cursor.moveToFirst(); 
			
	        int jml_baris = cursor.getCount();
			
	        if (jml_baris==1){
	        	
	        	kodebarangok=true;
	        	
	        	tkode.setText(cursor.getString(cursor.getColumnIndex("kd_barang")));
	        	tnama.setText(cursor.getString(cursor.getColumnIndex("nama_barang")));
	        	
	        	tsat1.setText(cursor.getString(cursor.getColumnIndex("satuan1")));
	        	tsat2.setText(cursor.getString(cursor.getColumnIndex("satuan2")));
	        	tsat3.setText(cursor.getString(cursor.getColumnIndex("satuan3")));
	        	
	        	cursor.close();
	        	
	        	//tqty1.requestFocus();
	        	
	        }else if (jml_baris==0){
	        	
	        	cursor.close();
	        	
	        	if (kode==1){
	        		sqlview="select _id,kd_barang,nama_barang,satuan1,satuan2,satuan3 from ms_barang where kd_barang like '%"+ kodebarang +"%'";
	        	}else{
	        		sqlview="select _id,kd_barang,nama_barang,satuan1,satuan2,satuan3 from ms_barang where nama_barang like '%"+ kodebarang +"%'";
	        	}
	        		
	        	Cursor cursor2 = dba.SelectData(db, sqlview);
	        		cursor2.moveToFirst();
	        	
	        		int jml_baris2=cursor2.getCount() ;
	        		
	        		cursor2.close();
	        		
	        		if (jml_baris2 >= 1) {
	        			
	        			
	        			showDialog(1);
	        		}else{
	        			kodebarangok=false;
	        			Toast.makeText(getBaseContext(),"Barang tidak ada", Toast.LENGTH_LONG).show();
	        		}
	        }  
			
		}catch(Exception e){
			kodebarangok=false;
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		
	}
	
	
	 @Override
	 protected Dialog onCreateDialog(int id) {
	 
	  AlertDialog dialogDetails = null;
	 
	  switch (id) {
	  case 1:
	   LayoutInflater inflater = LayoutInflater.from(this);
	   View dialogview = inflater.inflate(R.layout.view_kodebar0, null);
	 
	   AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
	   dialogbuilder.setTitle("View Barang");	
	   dialogbuilder.setView(dialogview);
	   dialogDetails = dialogbuilder.create();
	 
	   break;
	  }
	 
	  return dialogDetails;
	 }
	
	 
	 @Override
	 protected void onPrepareDialog(int id, Dialog dialog) {
	 
	  switch (id) {
	  case 1:
	   final AlertDialog alertDialog = (AlertDialog) dialog;
	   Button cancelbutton = (Button) alertDialog
	     .findViewById(R.id.btback_view0);

	   ListView list1 = (ListView) alertDialog
	     .findViewById(R.id.list_view0);
	   
	   
	   try {
	    
		   
		 if (sqlview == null){
			 return;
		 }
		   
   		dbcursor = dba.SelectData(db,sqlview);
   		
       	adapter = new SimpleCursorAdapter(
                   this, 
                   R.layout.view_kodebar, 
                   dbcursor,
                   new String[] {"kd_barang","nama_barang"}, 
                   new int[] {R.id.tkode_view,R.id.tnama_view});
           list1.setAdapter(adapter);  
           
           
           
   		}catch (Exception e) {
   			Log.v("error list", e.toString());
   			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
   		}
	   
	   
	   list1.setOnItemClickListener(new OnItemClickListener() {
		   
		   public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			   
			   kodebarangok=true;
			   
			   tkode.setText(dbcursor.getString(dbcursor.getColumnIndex("kd_barang")));
			   tnama.setText(dbcursor.getString(dbcursor.getColumnIndex("nama_barang")));
  	
			   tsat1.setText(dbcursor.getString(dbcursor.getColumnIndex("satuan1")));
			   tsat2.setText(dbcursor.getString(dbcursor.getColumnIndex("satuan2")));
			   tsat3.setText(dbcursor.getString(dbcursor.getColumnIndex("satuan3")));
			   
			   tqty1.requestFocus(); 
			   alertDialog.dismiss();
			   
		   }}); 
  
	   cancelbutton.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			kodebarangok=false;
			alertDialog.dismiss();
		}});
	   
	   break;
	  }}
	 
	 private void isi_bukti(){
		 
		 DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		 Date date = new Date(); 
		 
		 DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
	      Date date2 = new Date(); 
	    	
	      String tgltrans =dateFormat2.format(date2);
		 
		String sql="select no_bukti from faktur_h where tanggal='"+ tgltrans +"' and kd_salesman='"+ kd_sales +"' and kd_cust='"+ kd_cust +"' and carabayar='"+ carabayar +"'";
		
		db = dba.getReadableDatabase();
		
		try {
			
			Cursor cursor = dba.SelectData(db, sql);
			cursor.moveToFirst(); 
			
			int jml_baris = cursor.getCount();
			
	        
	        if(jml_baris == 0)  {
		
	        
	        	String tanggals = dateFormat.format(date);
		 
	        	// Calendar cal = Calendar.getInstance();
	        	String tgl= tanggals.substring(0, 2) ;  //cal.get(Calendar.DATE) ;
	        	String bln= tanggals.substring(3,5); // cal.get(Calendar.MONTH);
	        	String thn =tanggals.substring(6, 8) ; // cal.get(Calendar.YEAR);

	        	String jam = tanggals.substring(9, 11); //cal.get(Calendar.HOUR);
	        	String menit = tanggals.substring(12, 14);  //cal.get(Calendar.MINUTE);
	        	String detik = tanggals.substring(15, 17);  //cal.get(Calendar.SECOND);
		 
		 
	        	nobukti = kd_sales +"." +tgl + bln + thn +"."+ jam + menit + detik  ;
	        	
	        	addbukti=0;
	        	
	        	Toast.makeText( getApplicationContext(), "No Bukti : " + nobukti, Toast.LENGTH_SHORT).show();
		 
	    }else{
	    	
	    	nobukti = cursor.getString(cursor.getColumnIndex("no_bukti"));
	    	
	    	addbukti=1;
	    }
		}catch (Exception e){
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		 
		 
	 };
	
}
