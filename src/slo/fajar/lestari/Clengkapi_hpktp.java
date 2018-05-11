package slo.fajar.lestari;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Clengkapi_hpktp extends Activity {
	
	private DBAdapter dba=null;
	private SQLiteDatabase db=null;
	
	String kd_sales;
	String nama_sales;
	String kd_cust;
	String nama_cust;
	
	String longi;
	String loti;
	
	String longi_now;
	String loti_now;
	String jarak;
	String perubahan2;
	String kd_kondisi;
	
	TextView jdulnamacust;
	EditText ttelp1;
	EditText ttelp2;
	EditText thp1;
	EditText thp2;
	EditText noktp;
	Button btsimpan;
	Button btback;
	
	String nobukti;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {                           
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.view_lengkapioutl);
	        
	        dba = new DBAdapter(this);
	        
	        Bundle bnd = getIntent().getExtras();
	        kd_cust = bnd.getString("kd_cust").toString();
        	
        	nama_cust = bnd.getString("nama_cust");
        	kd_sales = bnd.getString("kd_sales");
        	nama_sales =  bnd.getString("nama_sales");
        		
        	longi = bnd.getString("longi");
        	loti = bnd.getString("lati");
        	
        	longi_now = bnd.getString("longi_now");
        	loti_now = bnd.getString("lati_now");
        	
        	jarak = bnd.getString("jarak");
        	perubahan2 = bnd.getString("perubahan");
        	
        	kd_kondisi = bnd.getString("kondisi");
	        	
	        jdulnamacust = (TextView)findViewById(R.id.tnama_lengkp2);
	        jdulnamacust.setText(nama_cust);
	        
	        ttelp1 = (EditText)findViewById(R.id.ttelp1_lengkp);
	        ttelp2 = (EditText)findViewById(R.id.ttelp2_lengkp);
	        thp1 = (EditText)findViewById(R.id.thp1_lengkp);
	        thp2 = (EditText)findViewById(R.id.thp2_lengkp);
	        noktp = (EditText)findViewById(R.id.tnoktp_lengkp);
	        
	        btsimpan = (Button)findViewById(R.id.btsimpan_lengkp);
	        btback = (Button)findViewById(R.id.btback_lengkp);
	        
	        btback.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
	        
	        btsimpan.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				if (ttelp1.getText().toString().trim().length()==0 && ttelp2.getText().toString().trim().length()==0 && +
						thp1.getText().toString().trim().length()==0 && thp2.getText().toString().trim().length()==0){
					Toast.makeText(getBaseContext(), "Isi dahulu no telp", Toast.LENGTH_LONG).show();
					return;
					}
				
				if (noktp.getText().toString().trim().length()==0){
					Toast.makeText(getBaseContext(), "Isi dahulu no ktp", Toast.LENGTH_LONG).show();
					return;
				}
						
					simpan();	
				
				}
			});
	        
	        
	        
	 } // akhir dari onCreate
	 
	 @Override
	    protected void onResume(){
	    	super.onResume();
	    	cek_data_sudahada();
	    }
	    
	    @Override
	    protected void onPause(){
	    	super.onPause();
	    }
	    
	    @Override
	    protected void onDestroy(){
	    	super.onDestroy();
	    	//turnGPSOff();
	    	db.close();
	    }
	 
	 private void cek_data_sudahada(){
		 
		 db = dba.getReadableDatabase();
		 try {
			
			 String sql="select telp1,telp2,hp1,hp2,noktp from ms_customer where kd_customer='"+ kd_cust +"'";
			 Cursor cursor = dba.SelectData(db,sql);
				cursor.moveToFirst(); 
				
		        int jml_baris = cursor.getCount();
		        
		        if (jml_baris >=1){
		        	
		        	ttelp1.setText(cursor.getString(0));
		        	ttelp2.setText(cursor.getString(1));
		        	thp1.setText(cursor.getString(2));
		        	thp2.setText(cursor.getString(3));
		        	noktp.setText(cursor.getString(4));
		        	
		        }else{
		        	
		        	ttelp1.setText("");
		        	ttelp2.setText("");
		        	thp1.setText("");
		        	thp2.setText("");
		        	noktp.setText("");
		        	
		        }
			 
			 
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		 
	 }
	 
	 private void simpan(){
		 
		 db = dba.getWritableDatabase(); 
		 try {
			
			 String sql="update ms_customer set telp1='"+ ttelp1.getText().toString() +"',telp2='"+ ttelp2.getText().toString() +"'" +
			 		",hp1='"+ thp1.getText().toString() +"',hp2='"+ thp2.getText().toString() +"',noktp='"+ noktp.getText().toString() +"'" +
					 ",kd_sales_edit='"+ kd_sales +"',editd='1' where kd_customer='"+ kd_cust +"'";
			 
			 db.execSQL(sql);
			
			 finish();
			 
			//if (kd_kondisi.equals("SSR")){
				
				Intent myIntent;
				
					myIntent = new Intent(getBaseContext(), jfaktur2.class);
					
					myIntent.putExtra("kd_cust",kd_cust);
					myIntent.putExtra("nama_cust", nama_cust);
					myIntent.putExtra("kd_sales", kd_sales);
					myIntent.putExtra("nama_sales", nama_sales);
					myIntent.putExtra("longi", longi);
					myIntent.putExtra("lati", loti);
					myIntent.putExtra("longi_now", longi_now);
					myIntent.putExtra("lati_now",loti_now);
					myIntent.putExtra("jarak", jarak);
					myIntent.putExtra("perubahan", "0");
					myIntent.putExtra("kondisi", kd_kondisi);
					
					startActivity(myIntent);
					
		//	} 
			
			Toast.makeText(getBaseContext(), "Data disimpan...", Toast.LENGTH_LONG).show();
			
			/* else{
				
				finish();
				
				isi_bukti();
				
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
				
				if (longi_now == null) {
					longinow = Float.valueOf(0);
				}else if (longi_now.length() ==0){
					longinow = Float.valueOf(0);
				}else{
					longinow = Float.valueOf(longi_now);
				}
				
				if (loti_now == null) {
					latinow = Float.valueOf(0);
				}else if (loti_now.length() ==0){
					latinow = Float.valueOf(0);
				}else{
					latinow = Float.valueOf(loti_now);
				}
				
				String sql1="insert into faktur_h (trans,lat,long,jam,no_bukti,kd_cust,tanggal,kd_salesman,carabayar,jarak,jamperubahan,jamperubahan2,jarakperubahan,lat2,long2,kd_kondisi) values(0,'"+ latinow +"','"+ longinow +"','"+ jambukti +"','"+ nobukti +"','"+ kd_cust +"','"+ tglsekarang +"','"+ kd_sales +"','',"+ JarakSebenarnya +",'00:00:00','00:00:00',0,'0','0','"+ kd_kondisi +"')";
				db.execSQL(sql1);
				
				
				
			} */
 			
			 
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		 
		 
	 }
	 
	/* private void isi_bukti(){
		 
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
		 
		 
		 nobukti = kd_sales +"." +tgl + bln + thn +"."+ jam + menit + detik  ;
		 
		 Toast.makeText( getApplicationContext(), "No Bukti : " + nobukti, Toast.LENGTH_SHORT).show();
		 
		 
	 }; */
	 
	 
	
}
