package slo.fajar.lestari;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class jlogin extends Activity {

	String kd_sal ="";
	String nama_sal ="";
	String menu[]={"Order","Set Coordinat Outlet","Pengajuan NOO","Upload/Kirim Data","Utility","Exit"};
	
	private ListView list1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        
        LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        
        if ( !mlocManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ){
    		turnGPSOn();
    	}
        
        Bundle kd_sales= getIntent().getExtras();
        kd_sal = kd_sales.getString("kd_sales");
        
        nama_sal = kd_sales.getString("nama_sales");
        
        list1 = (ListView)findViewById(R.id.listutility);
        
    //    TextView namasales = (TextView) findViewById(R.id.userlog);
    //    	namasales.setText(kd_sal+ " " +nama_sal);
        
     //   	ArrayList<String> your_array_list = new ArrayList<String>();
       //     your_array_list.add("Order");
        //    your_array_list.add("Pengajuan NOO");
         //   your_array_list.add("Exit");
            
          //  ArrayAdapter<String> arrayAdapter =      
           //         new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, your_array_list);
            //        list1.setAdapter(arrayAdapter); 
            
      // ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu);
      //  list1.setAdapter(arrayAdapter);	
        
       list1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,  menu)); 
        list1.setTextFilterEnabled(true);
        
        list1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    // When clicked, show a toast with the TextView text
				String pilihan = menu[position];
				
				Intent myIntent = null;  
		        if(pilihan.equalsIgnoreCase("Exit")) { 
		        	//turnGPSOff();
		            finish();  
		        } else {  
		            if(pilihan.equalsIgnoreCase("Order")){  
		                
		                myIntent = new Intent(getBaseContext(), faktur.class);
						
						myIntent.putExtra("kd_sales", kd_sal);
						myIntent.putExtra("nama_sales", nama_sal);
						myIntent.putExtra("order", "1");
						
						//startActivity(myIntent);
		                
		            }  else if(pilihan.equalsIgnoreCase("Set Coordinat Outlet")){
		            	
		            	myIntent = new Intent(getBaseContext(), faktur.class);
						
						myIntent.putExtra("kd_sales", kd_sal);
						myIntent.putExtra("nama_sales", nama_sal);
						myIntent.putExtra("order", "0");
						myIntent.putExtra("perubahan", "0");
						
		            }else if(pilihan.equalsIgnoreCase("Pengajuan NOO")) { 
		            	
		            	myIntent = new Intent(getBaseContext(), Jpnoo0.class);
					
		            	myIntent.putExtra("kd_sales", kd_sal);
		            	myIntent.putExtra("nama_sales", nama_sal);
		            	
					//startActivity(myIntent);  
		            }else if (pilihan.equalsIgnoreCase("Upload/Kirim Data")){
		            	myIntent = new Intent(getBaseContext(), Jupload.class);
						
		            	myIntent.putExtra("kd_sales", kd_sal);
		            	myIntent.putExtra("nama_sales", nama_sal);
		            }else if (pilihan.equalsIgnoreCase("Utility")){
		            	myIntent = new Intent(getBaseContext(), Cutility.class);
						
		            	myIntent.putExtra("kd_sales", kd_sal);
		            	myIntent.putExtra("nama_sales", nama_sal);
		            }else if (pilihan.equalsIgnoreCase("View Hasil Order")){
		            	
		            }
		            startActivity(myIntent);  
		        
		        }  
				
			}
		});
        
        
        
    }
    
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
	
	/*
	private void turnGPSOff(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(provider.contains("gps")){
            final Intent intent = new Intent();
            intent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
            intent.setData(Uri.parse("3"));
            sendBroadcast(intent);
        }
    } */		
    
    @Override
    protected void onResume(){
    	super.onResume();
    };
    
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    //	turnGPSOff();
    };
    
    @Override
    protected void onPause(){
    	super.onPause();
    };
    
}
