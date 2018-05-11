package slo.fajar.lestari;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class setcoord extends Activity {
	
	private DBAdapter dba=null;
	private SQLiteDatabase db=null;
//	private Cursor dbcursor;
	
	TextView tkode;
	TextView tnama;
	TextView talamat;
	
	String kd_sales;
	String nama_sales;
	Integer addord;
	String perubahan2;
	String kd_kondisi;
	
	TextView tlongi;
	TextView tloti;
	
	Button btset_coord;
	Button btback_coord;
	
	public LocationManager mlocManager;
	public float  longi;
	public float lati;
	
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.set_coord_toko);
	        
	        
	        tkode = (TextView)findViewById(R.id.tkode_coord);
	        tnama = (TextView)findViewById(R.id.tnama_coord);
	        talamat = (TextView)findViewById(R.id.talamat_coord);
	        
	        tlongi = (TextView)findViewById(R.id.tlongi_coord);
	        tloti = (TextView)findViewById(R.id.tloti_coord);
	        
	        btset_coord = (Button)findViewById(R.id.btset_coord);
	        btback_coord = (Button)findViewById(R.id.btback_coord);
	        
	        dba = new DBAdapter(this);
	        
	        Bundle bnd= getIntent().getExtras();
	        
	        tkode.setText(bnd.getString("kd_cust"));
	        tnama.setText(bnd.getString("nama_cust"));
	        talamat.setText(bnd.getString("alamat"));
	        
	        kd_sales = bnd.getString("kd_sales");
	        nama_sales = bnd.getString("nama_sales");
	        
	        addord = Integer.valueOf(bnd.getString("addorder"));
	        perubahan2 = bnd.getString("perubahan");
	        kd_kondisi = bnd.getString("kondisi");
	        
	     //   Log.v("sql", perubahan2.toString());
	        
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
			
			
			// dialog to save
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder
        	.setTitle("Set coordinat.... ")
        	.setMessage("Yakin coordinat outlet benar ?")
        	.setIcon(android.R.drawable.ic_dialog_alert)
        	
        	.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					simpan();
				}
			} )
        	.setNegativeButton("No", null);
        	// end of dialog save
			
			btback_coord.setOnClickListener(list_back);
			btset_coord.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					builder.show();
				}
			});
			
			
	 } // on create sampe sini
	 
	 private void simpan(){
		 
		 db = dba.getWritableDatabase();
			
			try {
				
				if ((longi == 0.0) || (lati == 0.0)){
					Toast.makeText(getBaseContext(), "Coordinat tidak ditemukan,set dahulu coordinat !!!", Toast.LENGTH_LONG).show();
					return;
				}
				
				
				String sql ="update ms_customer set long='"+ longi +"',lat='"+ lati +"',editd='1',kd_sales_edit='"+ kd_sales +"'  where kd_customer='"+ tkode.getText() +"'";
				
				
				db.execSQL(sql);
				
				Toast.makeText(getBaseContext(), "Coordinat outlet disimpan", Toast.LENGTH_LONG).show();
				finish();
				
				if (addord == 1) {
					
					Integer hasilcek_s=cek_kelengkapan_cust(tkode.getText().toString().trim()); 
					
					Intent myIntent;
					
					if (hasilcek_s==1){

						myIntent = new Intent(getBaseContext(), jfaktur2.class);
	    				
    					myIntent.putExtra("kd_cust",tkode.getText().toString());
    					myIntent.putExtra("nama_cust", tnama.getText().toString());
    					myIntent.putExtra("kd_sales", kd_sales);
    					myIntent.putExtra("nama_sales", nama_sales);
    					myIntent.putExtra("longi", String.valueOf(longi));
    					myIntent.putExtra("lati", String.valueOf(lati));
    					myIntent.putExtra("longi_now", String.valueOf(longi));
    					myIntent.putExtra("lati_now", String.valueOf(lati));
    					myIntent.putExtra("jarak", 0);
    					myIntent.putExtra("perubahan", perubahan2);
    					myIntent.putExtra("kondisi", kd_kondisi);
        			
        				startActivity(myIntent);
						
					}else{
						
						myIntent = new Intent(getBaseContext(), Clengkapi_hpktp.class);
	    				
	    				myIntent.putExtra("kd_cust",tkode.getText().toString());
	        			myIntent.putExtra("nama_cust", tnama.getText().toString());
	        			myIntent.putExtra("kd_sales", kd_sales);
	        			myIntent.putExtra("nama_sales", nama_sales);
	        			myIntent.putExtra("longi", String.valueOf(longi));
	        			myIntent.putExtra("lati", String.valueOf(lati));
	        			myIntent.putExtra("longi_now", String.valueOf(longi));
	        			myIntent.putExtra("lati_now", String.valueOf(lati));
	        			myIntent.putExtra("jarak", 0);
	        			myIntent.putExtra("perubahan", perubahan2);
		            	myIntent.putExtra("kondisi", kd_kondisi);
	        			
	        			startActivity(myIntent); 

        			
					}
				}; 
				
			}catch(Exception e){
				Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
			}
		 
	 };
	 
	 // listener to back
	 View.OnClickListener list_back= new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	}; // end of listener back
	
	 private void updatenewlocation(Location location){
			
			if (location != null){

				lati=(float) location.getLatitude();
		        longi=(float) location.getLongitude();  
		        
		       /* Log.v("nilai2", String.valueOf(longi));
		        
		        String Text = "Coordinat Outlet : " +
		                      "Latitude = " + location.getLatitude() +
		                      " Longitude = " + location.getLongitude();
		        
		        Toast.makeText( getApplicationContext(), Text, Toast.LENGTH_SHORT).show(); */
		        
			}else {
				lati=0;
				longi=0;
				Toast.makeText( getApplicationContext(), "Coordinat outlet tidak ditemukan", Toast.LENGTH_SHORT).show();
			}
			
			tlongi.setText(String.valueOf(longi));
	        tloti.setText(String.valueOf(lati));
			
			
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
				Toast.makeText( getApplicationContext(), "Gps Aktif", Toast.LENGTH_SHORT).show();
			}
			
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
				Toast.makeText( getApplicationContext(), "Gps Tidak Aktif,akan diaktifkan kembali", Toast.LENGTH_SHORT ).show();
	            
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
	        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
	        if(provider.contains("gps")){
	            final Intent intent = new Intent();
	            intent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
	            intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
	            intent.setData(Uri.parse("3"));
	            sendBroadcast(intent);
	        }
	    }	*/	
		
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
	    protected void onResume(){
	    	super.onResume();
	    };
	    
	    @Override
	    protected void onDestroy(){
	    	super.onDestroy();
	    	//turnGPSOff();
	    };
	    
	    @Override
	    protected void onPause(){
	    	super.onPause();
	    };
	 
}
