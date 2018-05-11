package slo.fajar.lestari;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Jpnoo4 extends Activity {
	private DBAdapter dba=null;
	private SQLiteDatabase db=null;
	
	TextView namanoo;
	TextView tlongi;
	TextView tlati;
	
	String nopengajuan;
	String kd_sales;
	String nama_sales;
	
	Button btsave;
	Button btback;
	Button btclose;
	
	public LocationManager mlocManager;
	public float  longi;
	public float lati;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pnoo4);
        
        namanoo = (TextView)findViewById(R.id.tnama_noo4);
        
        tlongi = (TextView)findViewById(R.id.tlongi_noo);
        tlati = (TextView)findViewById(R.id.tlati_noo);
        
        btsave = (Button)findViewById(R.id.btsetsave_noo);
        btback = (Button)findViewById(R.id.bt_back4_noo);
        btclose = (Button)findViewById(R.id.btcancel_noo4);
        
        Bundle bnd= getIntent().getExtras();
        
        nopengajuan = bnd.getString("nobukti");
        namanoo.setText(bnd.getString("nama_noo"));
        
        kd_sales = bnd.getString("kd_sales");
        nama_sales = bnd.getString("nama_sales");
        
        dba = new DBAdapter(this);
        
        mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
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
		
		btsave.setOnClickListener(btsave_list);
		btback.setOnClickListener(btback_list);
        btclose.setOnClickListener(btclose_listener); 
		
	} // akhir dari event create
	
	View.OnClickListener btsave_list = new View.OnClickListener() {
		
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			// dialog to save
						AlertDialog.Builder builder = new AlertDialog.Builder(Jpnoo4.this);
			        	builder
			        	.setTitle("Set coordinat.... ")
			        	.setMessage("Yakin coordinat & semua semua data benar ?")
			        	.setIcon(android.R.drawable.ic_dialog_alert)
			        	
			        	.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								simpan();
							}
						} )
			        	.setNegativeButton("No", null);
			        	// end of dialog save
			
			        	builder.show();
			        	
		}
	};
	
	View.OnClickListener btback_list = new View.OnClickListener() {
		
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			Intent myintent=new Intent(getBaseContext(), Jpnoo3.class);
			
			myintent.putExtra("nobukti", nopengajuan);
			myintent.putExtra("nama_noo", namanoo.getText().toString().trim().toUpperCase() );
			myintent.putExtra("kd_sales", kd_sales);
			myintent.putExtra("nama_sales", nama_sales);
			
			startActivity(myintent);
			finish();
			
		}
	}; // akhir dari back list
	
	View.OnClickListener btclose_listener = new View.OnClickListener() {
		
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
	};
	
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
	        tlati.setText(String.valueOf(lati));
			
			
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
	    
		private void disablegps(){
			Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
		    intent.putExtra("enabled", false);
		    sendBroadcast(intent);
		}
		
		
	/*	private void enablegps(){
			Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
		    intent.putExtra("enabled", true);
		    sendBroadcast(intent);
		} */ 
		
		private void simpan(){
			
			db = dba.getWritableDatabase();
			
			try {
				
				if ((longi == 0.0) || (lati == 0.0)){
					Toast.makeText(getBaseContext(), "Coordinat tidak ditemukan,set dahulu coordinat !!!", Toast.LENGTH_LONG).show();
					return;
				}
				
				String sql ="update noo set longitude='"+ longi +"',latitude='"+ lati +"',step=4 where no_pengajuan='"+ nopengajuan +"'";
				
				db.execSQL(sql);
				
				Toast.makeText(getBaseContext(), "Data Noo Selesai, DISIMPAN", Toast.LENGTH_LONG).show();
				finish();
				
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
			}
			
		}// akhir dari simpan
		
		 @Override
		    protected void onResume(){
		    	super.onResume();
		    	//enablegps();
		    };
		    
		    @Override
		    protected void onDestroy(){
		    	super.onDestroy();
		    	disablegps();
		    };
		    
		    @Override
		    protected void onPause(){
		    	super.onPause();
		    };
		
} // akhir dari activity
