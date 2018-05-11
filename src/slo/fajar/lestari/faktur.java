package slo.fajar.lestari;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class faktur extends Activity {
    /** Called when the activity is first created. */
	
	// private String url_user="http://10.0.2.2:8080/slo/req_cust.php";
	
	private DBAdapter dba=null;
	private SQLiteDatabase db=null;
	private Cursor dbcursor;
	protected ListAdapter  adapter;
	
	private Cursor cursmanual;
	protected ListAdapter  adaptermanual;
	
	Spinner jenis_trans;
	String[] isi_jenis = {"KREDIT","CASH"};
	String sales;
	String response = "";
	String Qsql;
	String nama_sales;
	String jorder;
	String nobukti;
	
	TextView namasales;
	EditText namacust;
	ListView lv1data;
	TextView ttgl;
	
	public LocationManager mlocManager;
	float longi;
	float loti;
	float longi_now;
	float loti_now;
	
	private ListView listkonfirm;
	String[] jeniskondisi = {"SESUAI RUTE","MANUAL","TIDAK DIKUNJUNGI","TIDAK TRANSAKSI"};
	
	String kodecust;
	String snamacust;
	String alamat;
	String diluar;
	
	String longitu ;
	String latitu ;
	
	Double hsil_calc_coord;
	
	private ListView listmanual;
	
	String ketkondisi_sebelum;
	String jniskondisi_sebelum;
	String kdkondisi_sebelum;
	String kdjenis_sebelum;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {                           
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order2);
        
        
        Bundle sl = getIntent().getExtras();
        	sales = sl.getString("kd_sales");
        	nama_sales = sl.getString("nama_sales");
        	jorder = sl.getString("order");
        	
        dba = new DBAdapter(this);
        
        lv1data = (ListView)findViewById(R.id.list);
    	lv1data.setSelected(true);
        
        namacust = (EditText)findViewById(R.id.tnama);
        
        ttgl = (TextView)findViewById(R.id.ttgl_f);
        
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = new Date(); 
        
        	ttgl.setText(dateFormat.format(date)+ " ");
        	
        ImageButton btcaricust = (ImageButton)findViewById(R.id.bt_cari_ub);
        btcaricust.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cek_customer(0);
			}});  
    	
    	Button kembali = (Button)findViewById(R.id.bt_back);
    	kembali.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
    	
    	
    	// gps
    	
    	mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
    	if ( !mlocManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ){
    		turnGPSOn();
    	}
    	
		Criteria criteria = new Criteria();
		
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
	    criteria.setSpeedRequired(false);
	    criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		
		String provider = mlocManager.getBestProvider(criteria, true);
		Location location = mlocManager.getLastKnownLocation(provider);
        
		updatenewlocation(location);
		
		mlocManager.requestLocationUpdates(provider,2000, 10, mlocListener);
    	
		// akhir gps
		
    	lv1data.setOnItemClickListener(new OnItemClickListener() {
    		
    		public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
    			
    			dbcursor.moveToPosition(position);
    			
    			hsil_calc_coord =0.0;
    			
    			kodecust= dbcursor.getString(dbcursor.getColumnIndex("kd_customer"));
    			snamacust= dbcursor.getString(dbcursor.getColumnIndex("nama"));
    			alamat= dbcursor.getString(dbcursor.getColumnIndex("alamat"));
    			diluar= dbcursor.getString(dbcursor.getColumnIndex("diluarjalur"));
    			
    			Intent myIntent;
    			
    			if (jorder.equals("1")){
    				
    				
    				cekcoordinat(kodecust);
    				
    				
    				if (longi == 0.0) {
    					longitu="";
    				}else{
    					longitu = String.valueOf(longi);
    				}
    				
    				if (loti == 0.0) {
    					latitu="";
    				}else{
    					latitu = String.valueOf(loti);
    				}
    				
    				// cek apakah ada transaksi sebelumnya
    				cek_transaksi_sebelumnya(kodecust); 
    				
    				if ((jniskondisi_sebelum.equals("SESUAI RUTE")) || (jniskondisi_sebelum.equals("MANUAL"))) {
    					
    					if ((longi == Float.valueOf("0.0")) || (loti == Float.valueOf("0.0")) ){
    	    				hsil_calc_coord=Double.valueOf(0);
    	    			}else{
    	    			
    	    				hsil_calc_coord = hitungJarak(longi, longi_now, loti, loti_now);
    	    				hsil_calc_coord = hsil_calc_coord * 1000;
    	    			
    	    			}
    					
    					//hsil_calc_coord = hitungJarak(longi, longi_now, loti, loti_now);
						//hsil_calc_coord = hsil_calc_coord * 1000;
    					
						myIntent = new Intent(parent.getContext(), jfaktur2.class);
	    				
	    				myIntent.putExtra("kd_cust",kodecust);
	        			myIntent.putExtra("nama_cust", snamacust);
	        			myIntent.putExtra("kd_sales", sales);
	        			myIntent.putExtra("nama_sales", nama_sales);
	        			myIntent.putExtra("longi", String.valueOf(longi));
	        			myIntent.putExtra("lati", String.valueOf(loti));
	        			myIntent.putExtra("longi_now", String.valueOf(longi_now));
	        			myIntent.putExtra("lati_now", String.valueOf(loti_now));
	        			myIntent.putExtra("jarak", String.valueOf(hsil_calc_coord));
	        			myIntent.putExtra("perubahan", "1");
	        			myIntent.putExtra("kondisi", kdjenis_sebelum);
	        			
	        			startActivity(myIntent); 
	        			
						return;
						
    				}else if ((! jniskondisi_sebelum.equals("SESUAI RUTE")) && (! jniskondisi_sebelum.equals("error")) && (! jniskondisi_sebelum.equals("")) ){
    					
    					String messg = "Trans sebelumnya " + jniskondisi_sebelum + " Karna " + ketkondisi_sebelum +", Ingin dirubah ?";
    					
    					final AlertDialog.Builder builder = new AlertDialog.Builder(faktur.this);
    		        	builder
    		        	.setTitle("Status data..")
    		        	.setMessage(messg)
    		        	.setIcon(android.R.drawable.ic_dialog_alert)
    		        	
    		        	.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    						
    						public void onClick(DialogInterface arg0, int arg1) {
    							// TODO Auto-generated method stub
    							
    							try {
									
    								db = dba.getWritableDatabase();
        							
        							String sql="delete from faktur_h where no_bukti='"+ kdkondisi_sebelum +"'";
        							db.execSQL(sql);

        							showDialog(1);
    								
        							
								} catch (Exception e) {
									// TODO: handle exception
									Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
								}
    							
    							
    						}
    					} )
    		        	.setNegativeButton("No", null);
    					
    		        	builder.show();
    		        	
    					return;
    					
    				}else if (jniskondisi_sebelum.equals("error")) {
    					return;}
    				
        			
					showDialog(1);
					
    				
    			}else{
    				myIntent = new Intent(parent.getContext(), setcoord.class);
    				
    				myIntent.putExtra("kd_cust",kodecust);
        			myIntent.putExtra("nama_cust", snamacust);
        			myIntent.putExtra("alamat", alamat);
        			myIntent.putExtra("addorder", "0");
        			myIntent.putExtra("kd_sales", sales);
        			myIntent.putExtra("nama_sales", nama_sales);
        			myIntent.putExtra("perubahan", "0");
        			//myIntent.putExtra("kondisi", "");
        			
        			startActivity(myIntent); 
        			
    			}
    			
    			
    			
    			
    		}
    		
		}); // akhir listview click
    	
    	
        } // akhir dari create
    
    @Override
    protected void onResume(){
    	super.onResume();
    	cek_customer(1);
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
    
    public void cek_customer(int AllV){
    	
    	
    	if (AllV==1) {    		
    		Qsql= "SELECT  '0' as _id,ms_customer.kd_customer,ms_customer.nama, ms_customer.alamat,ms_customer.diluarjalur "+
    				"FROM         ms_term INNER JOIN "+
                    "ms_customer ON ms_term.kd_customer = ms_customer.kd_customer "+
                    "where ms_term.kd_salesman="+"'"+ sales.trim() +"' limit 100";
    	}else {
    		
    		Qsql= "SELECT   '0' as _id,ms_customer.kd_customer,ms_customer.nama, ms_customer.alamat,ms_customer.diluarjalur "+
    				"FROM         ms_term INNER JOIN "+
                    "ms_customer ON ms_term.kd_customer = ms_customer.kd_customer "+
                    "where ms_term.kd_salesman="+"'"+ sales.trim() +"' "+
                    "and ms_customer.nama like "+"'%"+ namacust.getText().toString().trim()+"%' limit 100" ;
    	
    	
    		
    	}
    	
    	db = dba.getReadableDatabase(); 
    	
    	
    	try {
    	
    		//Log.v("sql", Qsql);
    		
    		dbcursor = dba.SelectData(db,Qsql);
    		
        	adapter = new SimpleCursorAdapter(
                    this, 
                    R.layout.v_cust, 
                    dbcursor,
                    new String[] {"kd_customer","nama","alamat"}, 
                    new int[] {R.id.tkode_cust,R.id.tnama_cust,R.id.talamat_cust});
            lv1data.setAdapter(adapter); 
            
    	//	ArrayList<String> records = dba.getRecords(Qsql);
        //	lv1data.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,  records)); 
    		
    	}catch (Exception e) {

    		//Log.e("error list cust", e.toString());
    		Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
    	}
    	

     //   setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, records));
    	
    	
    }

    private double hitungJarak(double lon1, double lon2, double lat1, double lat2){   
        return  
            (  
                    (  
                        (Math.acos(Math.sin(lat1 * Math.PI / 180) *   
                        Math.sin(lat2 * Math.PI / 180) +   
                        Math.cos(lat1 * Math.PI / 180) *   
                        Math.cos(lat2 * Math.PI / 180) *   
                        Math.cos((lon1 - lon2) * Math.PI / 180)) *   
                        180 / Math.PI) * 60 * 1.1515  
                    ) *  1.609344  
            );  
    }  
    
    private void cekcoordinat(String kdcust){
		
		longi=Float.valueOf(0);
		loti=Float.valueOf(0);
		
		String long_s;
		String lat_s;
		
		Double slati;
		Double slongi;
		
		db = dba.getReadableDatabase(); 
		
		try {
		
			String sql="select long,lat from ms_customer where kd_customer='" + kdcust +"'";
			
			Cursor	cursor = dba.SelectData(db, sql);
			cursor.moveToFirst(); 
			
			 int jml_baris = cursor.getCount();
				
		        if  (jml_baris > 0)  {
		        	
		        	if (cursor.getString(0) == null) {
		        		long_s ="";
		        	}else if (cursor.getString(0)== "0"){
		        		long_s="";
		        	}else{
		        		long_s = cursor.getString(0);
		        	}
		        	
		        	if (cursor.getString(1)== null){
		        		lat_s="";
		        	}else if (cursor.getString(1)== "0"){
		        		lat_s="";
		        	}else{
		        		lat_s = cursor.getString(1);
		        	}
		        	
		        	
		        	if (long_s.trim().length() ==0) {
		        		longi=Float.valueOf(0);
		        	
		        	}else {
		        		slongi = Double.valueOf(long_s.replace(",", "."));
		        		longi = Float.valueOf(slongi.toString());
		        	};
		        	
		        	
		        	if (lat_s.trim().length() == 0) {
		        		loti=Float.valueOf(0);
		        	
		        	}else {
		        		slati = Double.valueOf(lat_s.replace(",", "."));
		        		loti = Float.valueOf(slati.toString());
		        	};
		        	
		        	
		        }else {
		        	Toast.makeText(getBaseContext(), "Coordinat(Customer) tidak ditemukan...", Toast.LENGTH_LONG).show();
		        	
		        }
		        	
			
			
		}catch(Exception e){
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
			Log.v("cek koordinat error", e.toString());
		}
		
	};
	
	 private void updatenewlocation(Location location){
			
			if (location != null){

				loti_now=(float) location.getLatitude();
		        longi_now=(float) location.getLongitude();  
		        
		       /* Log.v("nilai2", String.valueOf(longi));
		        
		        String Text = "Coordinat Outlet : " +
		                      "Latitude = " + location.getLatitude() +
		                      " Longitude = " + location.getLongitude();
		        
		        Toast.makeText( getApplicationContext(), Text, Toast.LENGTH_SHORT).show(); */
		        
			}else {
				loti_now= Float.valueOf(0);
				longi_now= Float.valueOf(0);
				Toast.makeText( getApplicationContext(), "Coordinat outlet tidak ditemukan", Toast.LENGTH_SHORT).show();
			}
			
			//tlongi.setText(String.valueOf(longi));
	        //tloti.setText(String.valueOf(lati));
			
			
		} // end of update ne location
	 
	 private final LocationListener mlocListener = new LocationListener() {

			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
				/* if(status == 
	       	            LocationProvider.TEMPORARILY_UNAVAILABLE) { 
	       	                                    Toast.makeText(getApplicationContext(), 
	       	            "LocationProvider.TEMPORARILY_UNAVAILABLE", 
	       	            Toast.LENGTH_SHORT).show(); 
	       	                        } 
	       	                        else if(status== LocationProvider.OUT_OF_SERVICE) { 
	       	                                    Toast.makeText(getApplicationContext(), 
	       	            "LocationProvider.OUT_OF_SERVICE", Toast.LENGTH_SHORT).show(); 
	       	                        } */
				
			}
			
		public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				//Toast.makeText( getApplicationContext(), "Gps Aktif", Toast.LENGTH_SHORT).show();
			}
			
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
				//Toast.makeText( getApplicationContext(), "Gps Tidak Aktif,akan diaktifkan kembali", Toast.LENGTH_SHORT ).show();
	            
				turnGPSOn();
				
	            updatenewlocation(null);
				
			}
			
			public void onLocationChanged(Location location) {
				
				// TODO Auto-generated method stub
				updatenewlocation(location);
				
			}
		}; // end of mloclistener
	
/*	private void disablegps(){
			Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
		    intent.putExtra("enabled", false);
		    sendBroadcast(intent);
		} */
	
	private void turnGPSOn(){
		
		Intent intent2=new Intent("android.location.GPS_ENABLED_CHANGE");
		intent2.putExtra("enabled", true);
		sendBroadcast(intent2);
		
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.contains("gps")){
            final Intent intent = new Intent();
            intent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
            intent.setData(Uri.parse("3"));
            sendBroadcast(intent);
        }
    }
	
	
/*	private void turnGPSOff(){
  
  		Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
		intent.putExtra("enabled", false);
		sendBroadcast(intent);

        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(provider.contains("gps")){
            final Intent intent = new Intent();
            intent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
            intent.setData(Uri.parse("3"));
            sendBroadcast(intent);
        }
    } */
	
	private void cek_transaksi_sebelumnya(String kd_customer){
		
		db = dba.getReadableDatabase();
		
		try {
			
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	        Date date = new Date(); 
	    	
	        String tgltrans =dateFormat.format(date);
			
			String sql="SELECT faktur_h.no_bukti,ms_kondisi.keterangan,ms_kondisi.jenis,ms_kondisi.kode FROM " +
					"faktur_h INNER JOIN ms_kondisi ON faktur_h.kd_kondisi = ms_kondisi.kode where faktur_h.tanggal='"+ tgltrans +"' and faktur_h.kd_salesman='"+ sales +"' and faktur_h.kd_cust='"+ kd_customer +"'";
			Cursor cursor = dba.SelectData(db,sql);
			cursor.moveToFirst(); 
			
	        int jml_baris = cursor.getCount();
	        
	        if (jml_baris >=1){
	        	kdkondisi_sebelum= cursor.getString(0);
	        	ketkondisi_sebelum = cursor.getString(1);
	        	jniskondisi_sebelum = cursor.getString(2);
	        	kdjenis_sebelum = cursor.getString(3);
	        	
	        }else{
	        	kdkondisi_sebelum="";
	        	ketkondisi_sebelum ="";
	        	jniskondisi_sebelum="";
	        	kdjenis_sebelum="";
	        }
	        
			
		} catch (Exception e) {
			// TODO: handle exception
			
			kdkondisi_sebelum="error";
			ketkondisi_sebelum ="error";
        	jniskondisi_sebelum="error";
			kdjenis_sebelum="error";
			
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
			//Log.v("salah cek sebelumnya", e.toString());
			
		}} // akhir cek transaksi sebelumnya
	
	
	private Integer cek_kelengkapan_cust(String kdcust){
		
		db = dba.getReadableDatabase();
		
		try {
			
		//	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    //    Date date = new Date(); 
	    	
	      //  String tgltrans =dateFormat.format(date);
			
			String sql="select telp1,telp2,hp1,hp2,noktp from ms_customer where kd_customer='"+ kdcust +"'";
			Cursor cursor = dba.SelectData(db,sql);
			cursor.moveToFirst(); 
			
	        int jml_baris = cursor.getCount();
	        
	        if (jml_baris >=1){
	        	
	        	String telp1 = cursor.getString(0);
	        	String telp2 = cursor.getString(1);
	        	String hp1 = cursor.getString(2);
	        	String hp2 = cursor.getString(3);
	        	String noktp = cursor.getString(4);
	        	
	        	if (telp1.trim().length()==0 && telp2.trim().length()==0 && hp1.trim().length()==0 && hp2.trim().length() ==0 ){
	        		return 0;
	        	}
	        	
	        	if (noktp.trim().length()==0){
	        		return 0;
	        	}

	        	return 1;
	        	
	        }else{
	        	return 0;
	        }
	        
			
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
			//Log.v("salah cek sebelumnya", e.toString());
			return 2;
		
		}}// akhir cek kelengkapan customer
	
	@Override
	 protected Dialog onCreateDialog(int id) {
	 
	  AlertDialog dialogDetails = null;
	 
	  switch (id) {
	  case 1:
	   LayoutInflater inflater = LayoutInflater.from(this);
	   View dialogview = inflater.inflate(R.layout.view_lorderrute, null);
	 
	   AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
	  // dialogbuilder.setTitle("Jenis Order");	
	   dialogbuilder.setView(dialogview);
	   dialogDetails = dialogbuilder.create();
	 
	   break;
		
	  case 2:
		  LayoutInflater inflater2 = LayoutInflater.from(this);
		   View dialogview2 = inflater2.inflate(R.layout.view_lordermanual, null);
		 
		   AlertDialog.Builder dialogbuilder2 = new AlertDialog.Builder(this);
		   dialogbuilder2.setTitle("Alasan...");	
		   dialogbuilder2.setView(dialogview2);
		   dialogDetails = dialogbuilder2.create();
		 
		   break;  
	   
	  case 3:
		  LayoutInflater inflater3 = LayoutInflater.from(this);
		   View dialogview3 = inflater3.inflate(R.layout.view_lordermanual, null);
		 
		   AlertDialog.Builder dialogbuilder3 = new AlertDialog.Builder(this);
		   dialogbuilder3.setTitle("Alasan...");	
		   dialogbuilder3.setView(dialogview3);
		   dialogDetails = dialogbuilder3.create();
		 
		   break;  
		   
	  case 4:
		  LayoutInflater inflater4 = LayoutInflater.from(this);
		   View dialogview4 = inflater4.inflate(R.layout.view_lordermanual, null);
		 
		   AlertDialog.Builder dialogbuilder4 = new AlertDialog.Builder(this);
		   dialogbuilder4.setTitle("Alasan...");	
		   dialogbuilder4.setView(dialogview4);
		   dialogDetails = dialogbuilder4.create();
		 
		   break;  
	
	   
	  }
	 
	  return dialogDetails;
	 } // akhir dari on-create
	
	
	@Override
	 protected void onPrepareDialog(int id, final Dialog dialog) {
	 
	  switch (id) {
	  case 1:
		  final AlertDialog alertDialog = (AlertDialog) dialog;
		   Button cancelbutton = (Button) alertDialog
				     .findViewById(R.id.btback_loadrute); 
		   
		   listkonfirm = (ListView)alertDialog.findViewById(R.id.vlist_ordermanual);
		   
		   listkonfirm.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,  jeniskondisi)); 
	       listkonfirm.setTextFilterEnabled(true);
		   
		   
		   listkonfirm.setOnItemClickListener(new OnItemClickListener() {
	    		
	    		public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
	    			
	    			Intent myIntent;
	    			
	    			String pilihan = jeniskondisi[position];
					
			        if(pilihan.equalsIgnoreCase("Cancel")) { 
			           alertDialog.dismiss();
			        } else {  
			            if(pilihan.equalsIgnoreCase("SESUAI RUTE")){
			            	
			    			if ((longitu.length() ==0) || (latitu.length()==0)) {
								
						    	AlertDialog.Builder builder = new AlertDialog.Builder(faktur.this);
						    	builder
						    	.setTitle("Konfirmasi..")
						    	.setMessage("Coordinat outlet tidak ditemukan,anda ingin set coordinat ?")
						    	.setIcon(android.R.drawable.ic_dialog_alert)
						    	
						    	.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									
									public void onClick(DialogInterface arg0, int arg1) {
										// TODO Auto-generated method stub
										
										Intent miinte;
										
										miinte = new Intent(getBaseContext(), setcoord.class);
					    				
										miinte.putExtra("kd_cust",kodecust);
										miinte.putExtra("nama_cust", snamacust);
										miinte.putExtra("alamat", alamat);
										miinte.putExtra("kd_sales", sales);
										miinte.putExtra("nama_sales", nama_sales);
										miinte.putExtra("addorder", "1");
										miinte.putExtra("perubahan", "0");
										miinte.putExtra("kondisi", "SSR");
										
										alertDialog.dismiss();
										
										startActivity(miinte); 
										
									}
								} )
						    	.setNegativeButton("No", new DialogInterface.OnClickListener() {
									
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										return;
									}
								});
								
						    	builder.show();
								return;
							} 
							
							if (longi_now==Float.valueOf(0) || loti_now==Float.valueOf(0)) {
								Toast.makeText( getApplicationContext(), "Coordinat outlet tidak ditemukan", Toast.LENGTH_SHORT).show();
								return;
							}else{
								
								hsil_calc_coord = hitungJarak(longi, longi_now, loti, loti_now);
								hsil_calc_coord = hsil_calc_coord * 1000;
								
							
								
								if (Integer.valueOf(diluar) ==1 ){
									
								}else{
									// jika jarak lebih dari 1,5km maka jangan
									if (hsil_calc_coord > 1500){
										Toast.makeText( getApplicationContext(), "Pastikan anda berada di outlet...!!!", Toast.LENGTH_SHORT).show();
										return;
									} 
								} 
								
							}
			            	
			            	
							alertDialog.dismiss();
							
			            	Integer hsilkelengkapan = cek_kelengkapan_cust(kodecust.toString());
			            	
			            	if (hsilkelengkapan == 1){
			            	
			            	myIntent = new Intent(parent.getContext(), jfaktur2.class);
		    				
		    				myIntent.putExtra("kd_cust",kodecust);
		        			myIntent.putExtra("nama_cust", snamacust);
		        			myIntent.putExtra("kd_sales", sales);
		        			myIntent.putExtra("nama_sales", nama_sales);
		        			myIntent.putExtra("longi", String.valueOf(longi));
		        			myIntent.putExtra("lati", String.valueOf(loti));
		        			myIntent.putExtra("longi_now", String.valueOf(longi_now));
		        			myIntent.putExtra("lati_now", String.valueOf(loti_now));
		        			myIntent.putExtra("jarak", String.valueOf(hsil_calc_coord));
		        			myIntent.putExtra("perubahan", "0");
			            	myIntent.putExtra("kondisi", "SSR");
		        			
		        			startActivity(myIntent); 
		        			
			            	}else if (hsilkelengkapan == 0){
			            		
			            		myIntent = new Intent(parent.getContext(), Clengkapi_hpktp.class);
			    				
			    				myIntent.putExtra("kd_cust",kodecust);
			        			myIntent.putExtra("nama_cust", snamacust);
			        			myIntent.putExtra("kd_sales", sales);
			        			myIntent.putExtra("nama_sales", nama_sales);
			        			myIntent.putExtra("longi", String.valueOf(longi));
			        			myIntent.putExtra("lati", String.valueOf(loti));
			        			myIntent.putExtra("longi_now", String.valueOf(longi_now));
			        			myIntent.putExtra("lati_now", String.valueOf(loti_now));
			        			myIntent.putExtra("jarak", String.valueOf(hsil_calc_coord));
			        			myIntent.putExtra("perubahan", "0");
				            	myIntent.putExtra("kondisi", "SSR");
			        			
			        			startActivity(myIntent); 
			            		
			            	}
		        			
		        			
			            }
			            else if(pilihan.equalsIgnoreCase("TIDAK DIKUNJUNGI")){
			            	alertDialog.dismiss();
			            	showDialog(2);
			            }
			            else if(pilihan.equalsIgnoreCase("TIDAK TRANSAKSI")){
			            	
			            	alertDialog.dismiss();
			            	showDialog(3);
			            	
			            }
			            else if(pilihan.equalsIgnoreCase("MANUAL")){
			            	alertDialog.dismiss();
			            	showDialog(4);
			            }
			            
	    					
	    		}}}); // akhir dari on item click
		   
		   cancelbutton.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					alertDialog.dismiss();
				}}); 
		   
			   break;
		   
	  case 2:
		  
		  final AlertDialog alertDialog2 = (AlertDialog) dialog;  
		  	Button cancelbutton2 = (Button) alertDialog2
		  			.findViewById(R.id.btback_loadrute);
		  
		  	listmanual = (ListView)alertDialog2.findViewById(R.id.vlist_ordermanual);
		  	
		  	load_alasan_manual("TIDAK DIKUNJUNGI");
		  	
		  	listmanual.setOnItemClickListener(new OnItemClickListener() {
	    		
	    		public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
	    			
	    			cursmanual.moveToPosition(position);
	    			
	    			try {
						
	    				simpan_toDbase(cursmanual.getString(cursmanual.getColumnIndex("kode")));
	    				
	    				Toast.makeText(getBaseContext(), "Data disimpan...", Toast.LENGTH_LONG).show();
	    				
	    				alertDialog2.dismiss();
	    				
	    				
					} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
					}
	    			
	    			
	    		}});
	    		
		  	
		  	cancelbutton2.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					alertDialog2.dismiss();
				}});
		  	
		  	break;
		  	
	  case 3:
		  
		  final AlertDialog alertDialog3 = (AlertDialog) dialog;  
		  	Button cancelbutton3 = (Button) alertDialog3
		  			.findViewById(R.id.btback_loadrute);
		  
		  	listmanual = (ListView)alertDialog3.findViewById(R.id.vlist_ordermanual);
		  	
		  	load_alasan_manual("TIDAK TRANSAKSI");
		  	
		  	listmanual.setOnItemClickListener(new OnItemClickListener() {
	    		
	    		public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
	    			
	    			cursmanual.moveToPosition(position);
	    			
	    			try {
						
	    				simpan_toDbase(cursmanual.getString(cursmanual.getColumnIndex("kode")));
	    				
	    				Toast.makeText(getBaseContext(), "Data disimpan...", Toast.LENGTH_LONG).show();
	    				
	    				alertDialog3.dismiss();
	    				
	    				
					} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
					}
	    			
	    		}});
	    		
		  	
		  	cancelbutton3.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					alertDialog3.dismiss();
				}});
		  	
		  	break;
		  	
	  case 4:
		  
		  final AlertDialog alertDialog4 = (AlertDialog) dialog;  
		  	Button cancelbutton4 = (Button) alertDialog4
		  			.findViewById(R.id.btback_loadrute);
		  
		  	listmanual = (ListView)alertDialog4.findViewById(R.id.vlist_ordermanual);
		  	
		  	load_alasan_manual("MANUAL");
		  	
		  	listmanual.setOnItemClickListener(new OnItemClickListener() {
	    		
	    		public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
	    			
	    			cursmanual.moveToPosition(position);
	    			
	    			if ((longi == Float.valueOf("0.0")) || (loti == Float.valueOf("0.0")) ){
	    				hsil_calc_coord=Double.valueOf(0);
	    			}else{
	    			
	    				hsil_calc_coord = hitungJarak(longi, longi_now, loti, loti_now);
	    				hsil_calc_coord = hsil_calc_coord * 1000;
	    			
	    			}
					
					alertDialog4.dismiss();
					
	            	Integer hsilkelengkapan = cek_kelengkapan_cust(kodecust.toString());
	            	
	            	String kdkondisik= cursmanual.getString(cursmanual.getColumnIndex("kode"));
	            	
	            	Intent myIntent;
	            	
	            	if (hsilkelengkapan == 1){
	            	
	            	myIntent = new Intent(parent.getContext(), jfaktur2.class);
    				
    				myIntent.putExtra("kd_cust",kodecust);
        			myIntent.putExtra("nama_cust", snamacust);
        			myIntent.putExtra("kd_sales", sales);
        			myIntent.putExtra("nama_sales", nama_sales);
        			myIntent.putExtra("longi", String.valueOf(longi));
        			myIntent.putExtra("lati", String.valueOf(loti));
        			myIntent.putExtra("longi_now", String.valueOf(longi_now));
        			myIntent.putExtra("lati_now", String.valueOf(loti_now));
        			myIntent.putExtra("jarak", String.valueOf(hsil_calc_coord));
        			myIntent.putExtra("perubahan", "0");
	            	myIntent.putExtra("kondisi", kdkondisik);
        			
        			startActivity(myIntent); 
        			
	            	}else if (hsilkelengkapan == 0){
	            		
	            		myIntent = new Intent(parent.getContext(), Clengkapi_hpktp.class);
	    				
	    				myIntent.putExtra("kd_cust",kodecust);
	        			myIntent.putExtra("nama_cust", snamacust);
	        			myIntent.putExtra("kd_sales", sales);
	        			myIntent.putExtra("nama_sales", nama_sales);
	        			myIntent.putExtra("longi", String.valueOf(longi));
	        			myIntent.putExtra("lati", String.valueOf(loti));
	        			myIntent.putExtra("longi_now", String.valueOf(longi_now));
	        			myIntent.putExtra("lati_now", String.valueOf(loti_now));
	        			myIntent.putExtra("jarak", String.valueOf(hsil_calc_coord));
	        			myIntent.putExtra("perubahan", "0");
		            	myIntent.putExtra("kondisi",kdkondisik);
	        			
	        			startActivity(myIntent); 
	            		
	            	}
					
	    			
	    		}});
		  	
		  	cancelbutton4.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					alertDialog4.dismiss();
				}});
		  	
		  break;
		  	
		  	
	  }} // akhir dari on-prepare
	
	 
	 private void simpan_toDbase(String kdkondisi) throws InterruptedException{
		 
		 isi_bukti();
			
			Date date= new Date();
			DateFormat jam=new SimpleDateFormat("HH:mm:ss");
			DateFormat tanggalsekarang= new SimpleDateFormat("dd/MM/yyyy");
			
			
			String jambukti = jam.format(date);
			String tglsekarang = tanggalsekarang.format(date);
			
			Double JarakSebenarnya = 0.0;
			
			hsil_calc_coord = hitungJarak(longi, longi_now, loti, loti_now);
			hsil_calc_coord = hsil_calc_coord * 1000;
			
			String jarak = hsil_calc_coord.toString();
			
			if (jarak == null){
				JarakSebenarnya=0.0;
			}else if (jarak.length()==0){
				JarakSebenarnya=0.0;
			}else{
				JarakSebenarnya = Double.valueOf(jarak);
			}
			
			Float longinow= null;
			Float latinow= null;
			
			String longi_now2= String.valueOf(longi_now);
			String loti_now2= String.valueOf(loti_now);
			
			
			if (longi_now2 == null) {
				longinow = Float.valueOf(0);
			}else if (longi_now2.length() ==0){
				longinow = Float.valueOf(0);
			}else{
				longinow = Float.valueOf(longi_now2);
			}
			
			if (loti_now2 == null) {
				latinow = Float.valueOf(0);
			}else if (loti_now2.length() ==0){
				latinow = Float.valueOf(0);
			}else{
				latinow = Float.valueOf(loti_now2);
			}
			
			if (longi == 0.0){
				JarakSebenarnya=0.0;
			}
			
			db = dba.getWritableDatabase();
			
			String sql1="insert into faktur_h (trans,lat,long,jam,no_bukti,kd_cust,tanggal,kd_salesman,carabayar,jarak,jamperubahan,jamperubahan2,jarakperubahan,lat2,long2,kd_kondisi) values(0,'"+ latinow +"','"+ longinow +"','"+ jambukti +"','"+ nobukti +"','"+ kodecust +"','"+ tglsekarang +"','"+ sales +"','',"+ JarakSebenarnya +",'00:00:00','00:00:00',0,'0','0','"+ kdkondisi +"')";
			db.execSQL(sql1);
		 
	 }
	
	 private void isi_bukti(){
		 
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
		 
		 
		 nobukti = sales +"." +tgl + bln + thn +"."+ jam + menit + detik  ;
		 
		 Toast.makeText( getApplicationContext(), "No Bukti : " + nobukti, Toast.LENGTH_SHORT).show();
		 
		 
	 };
	
	private void load_alasan_manual(String jenis){
		
		db = dba.getReadableDatabase(); 
		
		try {
			
			String sql="select _id,kode,keterangan from ms_kondisi where jenis='"+ jenis +"'";
			
			cursmanual = dba.SelectData(db,sql);
    		
        	adaptermanual = new SimpleCursorAdapter(
                    this, 
                    R.layout.view_text1, 
                    cursmanual,
                    new String[] {"keterangan"}, 
                    new int[] {R.id.vtext1});
            listmanual.setAdapter(adaptermanual); 
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	
}