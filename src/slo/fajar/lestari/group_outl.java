package slo.fajar.lestari;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;


public class group_outl extends Activity {
    /** Called when the activity is first created. */
	
	Spinner s1;
	Spinner type;
	Spinner Spin_jenisCust;
	Spinner rayon;
	Spinner diskrit;
	Spinner pasar;
	Spinner lingkungan;
	
	private String xresult="";
	private String url="http://10.0.2.2:8080/slo/req_group_customer.php";
	private String url_type="http://10.0.2.2:8080/slo/req_type_customer.php";
	private String url_rayon="http://10.0.2.2:8080/slo/req_rayon.php";
	private String url_diskrit="http://10.0.2.2:8080/slo/req_diskrit.php";
	private String url_pasar="http://10.0.2.2:8080/slo/reg_pasar_all.php";
	
	private String url_kirim="http://10.0.2.2:8080/slo/add_noo.php";
	
	String[] kd_group;
	String[] keterangan=null; /* tipe outlet */
	String[] jenis_cust={"--","DALAM KOTA","LUAR KOTA"};
	String[] nama_rayon=null;
	String[] nama_diskrit=null;
	String[] kd_pasar=null;
	String[] kd_lingkungan ={"001","002"};
	String[] nama_lingkungan = {"DIDALAM PASAR","DILUAR PASAR"};
	String[] psr_nama=null;
	
	String kd_group_a;
	String kd_psr_a;
	
	String nama_group= "";  /* {"-","CHANDRA GROUP","RAMAYANA GROUP","ALFA GROUP","INDOMARCO GROUP",
			"MILLENIUM GROUP","SURYA MM GROUP","FITRINOF GROUP","MM GROUP","TRADISIONAL GROUP"}; */
	/* String[] id_group={"-","001","002","003","004","005","006","007","008","009"}; */
	
	TextView nama_outlet,pemilik,tgl_lahir,kontak,email,telp1,telp2,hp1,hp2,
			bank,no_rek,atas_nama_rek;
	private Button kirim;
	
	public LocationManager mlocManager;
	
	public float  longi;
	public float lati;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noo);
        
        /*
        nama_outlet = (TextView)findViewById(R.id.nama_outl);
    	pemilik = (TextView)findViewById(R.id.pemilik); 
    	tgl_lahir = (TextView)findViewById(R.id.tgl_lhr);
    	kontak = (TextView)findViewById(R.id.kontak);
    	email = (TextView)findViewById(R.id.email); */
    	telp1 = (TextView)findViewById(R.id.telp1);
    	telp2 = (TextView)findViewById(R.id.telp2);
    	hp1 = (TextView)findViewById(R.id.hp1);
    	hp2 = (TextView)findViewById(R.id.hp2);
    	bank = (TextView)findViewById(R.id.bank);
    	no_rek = (TextView)findViewById(R.id.norek);
    	atas_nama_rek = (TextView)findViewById(R.id.atas_nama);
    	
         xresult = getRequest(url);
         
         try {
        	 
        	 parse(1);
        	 
         } catch (Exception e) {
             e.printStackTrace();
         }
        
         xresult ="";
         xresult = getRequest(url_type);
         
         try {
        	 
        	 parse(2);
        	 
         } catch (Exception e) {
             e.printStackTrace();
         }
         
         	Spin_jenisCust = (Spinner)findViewById(R.id.jenis);
	        
	        ArrayAdapter<String> aa=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,jenis_cust);
	        Spin_jenisCust.setAdapter(aa);
	      
	       
	        xresult ="";
	        xresult = getRequest(url_rayon);
	         
	         try {
	        	 
	        	 parse(3);
	        	 
	         } catch (Exception e) {
	             e.printStackTrace();
	         } 
	         
	         xresult ="";
		        xresult = getRequest(url_diskrit);
		         
		         try {
		        	 
		        	 parse(4);
		        	 
		         } catch (Exception e) {
		             e.printStackTrace();
		         }   
	         
		         xresult ="";
			        xresult = getRequest(url_pasar);
			         
			         try {
			        	 
			        	 parse(5);
			        	 
			         } catch (Exception e) {
			             e.printStackTrace();
			         }   
		         
			         	lingkungan = (Spinner)findViewById(R.id.lingkungan);
				        
				        ArrayAdapter<String> ab=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,nama_lingkungan);
				        lingkungan.setAdapter(ab);
				        lingkungan.setOnItemSelectedListener(new my_spinner_lingkungan());
			
				        
			 kirim = (Button)findViewById(R.id.kirim);
			
			kirim.setOnClickListener(new Button.OnClickListener() {

				public void onClick(View v) {
					
					kirim();}

	private void kirim() {
		
		if (Double.valueOf(lati) ==0.0 | Double.valueOf(longi) ==0.0){
			Toast.makeText(getApplicationContext(), "Lokasi toko tidak ditemukan..." , Toast.LENGTH_SHORT).show();
		}else {
			
			Toast.makeText(getApplicationContext(), "Upload Data...." , Toast.LENGTH_SHORT).show();
			
			String s_nama = nama_outlet.getText().toString();
			String s_pemilik = pemilik.getText().toString();
			String s_tgllahir = tgl_lahir.getText().toString();
			String s_kontak = kontak.getText().toString();
			String s_email = email.getText().toString();
			String s_telp1 = telp1.getText().toString();
			String s_telp2 = telp2.getText().toString();
			String s_hp1 = hp1.getText().toString();
			String s_hp2 = hp2.getText().toString();
			String kd_sales = "a1";
					kd_group_a = kd_group_a.toString();
			String s_tipe = (String)type.getSelectedItem();
			String s_jenis_out= (String) Spin_jenisCust.getSelectedItem();
			String s_rayon= (String) rayon.getSelectedItem();
			String s_diskrit = (String) diskrit.getSelectedItem();
				kd_psr_a = kd_psr_a.toString();
			String s_lingkungan= (String) lingkungan.getSelectedItem();
			String s_bank = bank.getText().toString();
			String s_norek = no_rek.getText().toString();
			String s_atasnama  = atas_nama_rek.getText().toString();
			
			String s_status_ins ="1";
			
			String res = null;
			
			try {
				
				java.text.DateFormat newdate,olddate;
				newdate = new SimpleDateFormat("yyyy/MM/dd");
				olddate = new SimpleDateFormat("dd/MM/yyyy");
				
				if (s_tgllahir.length() ==8 ){
					olddate = new SimpleDateFormat("dd/MM/yy");
				}
				
				
				if (s_tgllahir.length() > 0) {
						s_tgllahir = newdate.format(olddate.parse(s_tgllahir));
				}else {
					s_tgllahir = newdate.format(olddate.parse("01/01/1970"));
				}
				
						
						ArrayList<NameValuePair> postparam = new ArrayList<NameValuePair>();
						postparam.add(new BasicNameValuePair("kd_sales", kd_sales));
						postparam.add(new BasicNameValuePair("nama_outlet", s_nama));
						postparam.add(new BasicNameValuePair("nama_pemilik", s_pemilik));
						postparam.add(new BasicNameValuePair("nama_kontak", s_kontak));
						postparam.add(new BasicNameValuePair("tgl_lahir", s_tgllahir));
						postparam.add(new BasicNameValuePair("email", s_email));
						postparam.add(new BasicNameValuePair("telp1", s_telp1));
						postparam.add(new BasicNameValuePair("telp2", s_telp2));
						postparam.add(new BasicNameValuePair("no_hp1", s_hp1));
						postparam.add(new BasicNameValuePair("no_hp2", s_hp2));
						postparam.add(new BasicNameValuePair("longitud", String.valueOf(longi)));
						postparam.add(new BasicNameValuePair("latitud",String.valueOf(lati)));
						postparam.add(new BasicNameValuePair("group_outlet", kd_group_a));
						postparam.add(new BasicNameValuePair("type_outlet", s_tipe));
						postparam.add(new BasicNameValuePair("jenis_outlet", s_jenis_out));
						postparam.add(new BasicNameValuePair("rayon", s_rayon));
						postparam.add(new BasicNameValuePair("rute", s_diskrit));
						postparam.add(new BasicNameValuePair("pasar", kd_psr_a));
						postparam.add(new BasicNameValuePair("lingkungan", s_lingkungan));
						postparam.add(new BasicNameValuePair("bank1", s_bank));
						postparam.add(new BasicNameValuePair("no_rek_cc1", s_norek));
						postparam.add(new BasicNameValuePair("atas_nama_rek1", s_atasnama));
						postparam.add(new BasicNameValuePair("status", s_status_ins));
						
						
						 
						 res = CustomHttpClient.executeHttpPost(url_kirim, postparam);
						 
						 if (res.toString().trim().equals("1")){
							 Toast.makeText(getApplicationContext(), "Data berhasil dikirim",Toast.LENGTH_SHORT).show();
						 }else {
							 Toast.makeText(getApplicationContext(), "Data gagal dikirim",Toast.LENGTH_SHORT).show();
						 }
						 
						 
		    } catch (Exception e) {
		    	Toast.makeText(getApplicationContext(), e.toString(),Toast.LENGTH_SHORT).show();
			}
		}
	}
	
});
		
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
			
			/*
			if (!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){  
			      enablegps();
			      Log.v("active gps", "0");
			} */
			
			updatenewlocation(location);
			
			mlocManager.requestLocationUpdates(provider,2000, 10, mlocListener);  
			
			
			Button closeme = (Button) findViewById(R.id.batal);
			closeme.setOnClickListener(new View.OnClickListener() {
	   			
	   			public void onClick(View v) {
	   				// TODO Auto-generated method stub
	   				disablegps();
	   				finish();
	   			}
	   		}); 
			
			
}
 

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
	};
    

private void updatenewlocation(Location location){
	
	if (location != null){

		lati= (float) location.getLatitude();
        longi= (float) location.getLongitude();  
        
        Log.v("nilai2", String.valueOf(longi));
        
        String Text = "Coordinat Outlet : " +
                      "Latitude = " + location.getLatitude() +
                      " Longitude = " + location.getLongitude();
        
        Toast.makeText( getApplicationContext(), Text, Toast.LENGTH_SHORT).show();

	}else {
		lati=0;
		longi=0;
		Toast.makeText( getApplicationContext(), "Coordinat outlet tidak ditemukan", Toast.LENGTH_SHORT).show();
	}
	
}

/*
private void enablegps(){
	Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
    intent.putExtra("enabled", true);
    sendBroadcast(intent);
} */

private void disablegps(){
	Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
    intent.putExtra("enabled", false);
    sendBroadcast(intent);
} 

public void getRequest_toadd(String Url) {
   /* Toast.makeText(this, "Kirim Data " + Url + " ", Toast.LENGTH_SHORT)
            .show(); */
    HttpClient client = new DefaultHttpClient();
    HttpGet request = new HttpGet(url);
    try {
        HttpResponse response = client.execute(request);
        Toast.makeText(this, "Kirim Data " + request(response) + " ",
                Toast.LENGTH_SHORT).show();
    } catch (Exception ex) {
        Toast.makeText(this, "Pengiriman data gagal !", Toast.LENGTH_SHORT)
                .show();
    }
		
	}
				
    
    private void parse(Integer x) throws Exception {
    		       /*  jObject = new JSONObject(xresult); */
    	
    				
    				String[] sret=null;
    	 			JSONArray menuitemArray=null;
    		        
    		        
    		        if (x==1) {
    		        
    		        menuitemArray = new JSONArray(xresult); 
    		        	
    		        sret= new String [menuitemArray.length()];
    		        kd_group = new String [menuitemArray.length()];
    		        
    		        }else if (x==2) {
    		        	
    		        	menuitemArray = new JSONArray(xresult); 
    		        	
    		        	keterangan= new String [menuitemArray.length()];
    		        }else if (x==3){
    		        	
    		        	menuitemArray = new JSONArray(xresult);
    		        	
    		        	nama_rayon= new String [menuitemArray.length()];
    		        }else if (x==4){
    		        	menuitemArray = new JSONArray(xresult);
    		        	
    		        	nama_diskrit= new String [menuitemArray.length()];
    		        }else if (x==5){
    		        	menuitemArray = new JSONArray(xresult);
    		        	
    		        	kd_pasar= new String [menuitemArray.length()];
    		        	psr_nama= new String [menuitemArray.length()];
    		        }
    		        
    		        
    		        
    		        for (int i = 0; i < menuitemArray.length(); i++) {
    		        	
    		        	if (x==1) {
    		        	sret[i]= menuitemArray.getJSONObject(i).getString("Nama_GroupCustomer");
    		        	kd_group[i]= menuitemArray.getJSONObject(i).getString("KD_groupCustomer");
    		        	
    		        	}else if (x==2) {
    		        		keterangan[i]= menuitemArray.getJSONObject(i).getString("Keterangan");
    		        	}else if (x==3){
    		        		nama_rayon[i]= menuitemArray.getJSONObject(i).getString("rayon");
    		        	}else if (x==4){
    		        		nama_diskrit[i]= menuitemArray.getJSONObject(i).getString("diskrit");
    		        	}else if (x==5){
    		        		kd_pasar[i]=menuitemArray.getJSONObject(i).getString("KD_Pasar");
    		        		psr_nama[i]=menuitemArray.getJSONObject(i).getString("Nama_pasar");
    		        	}
    		        	
    		           /* sret +=menuitemArray.getJSONObject(i)
    		            .getString("Nama_GroupCustomer").toString()+" : ";
    		            System.out.println(menuitemArray.getJSONObject(i)
    		                    .getString("Nama_GroupCustomer").toString());
    		            System.out.println(menuitemArray.getJSONObject(i).getString(
    		                    "harga").toString());
    		            sret +=menuitemArray.getJSONObject(i).getString(
    		            "harga").toString()+"\n"; */
    		        }
    		        
    		        if (x==1){
    		        
    		        s1 = (Spinner)findViewById(R.id.group);
    		        
    		        ArrayAdapter<String> aa=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,sret);
    		        s1.setAdapter(aa);
    		        s1.setOnItemSelectedListener(new my_spinner_group());
    		        
    		        }else if (x==2) {
    		        	
    		        	type = (Spinner)findViewById(R.id.tipe);
    			        
    			        ArrayAdapter<String> aa=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,keterangan);
    			        type.setAdapter(aa);
    			       
    		        	
    		        }else if (x==3) {
    		        	
    		        	rayon = (Spinner)findViewById(R.id.rayon);
    			        
    			        ArrayAdapter<String> aa=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,nama_rayon);
    			        rayon.setAdapter(aa);
    			        
    		        	
    		        }else if (x==4) {
    		        	diskrit = (Spinner)findViewById(R.id.rute);
    			        
    			        ArrayAdapter<String> aa=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,nama_diskrit);
    			        diskrit.setAdapter(aa);
    		        }else if (x==5) {
    		        	pasar = (Spinner)findViewById(R.id.pasar);
    		        	
    		        	ArrayAdapter<String> aa=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,psr_nama);
    			        pasar.setAdapter(aa);
    			        pasar.setOnItemSelectedListener(new my_spinner_pasar());
    		        }
    		        
    		    }
    
    
    public String getRequest(String Url){
    	
    		       String sret="";
    		        HttpClient client = new DefaultHttpClient();
    		        HttpGet request = new HttpGet(Url);
    		        try{
    		          HttpResponse response = client.execute(request);
    		          sret =request(response);
    		          
    		          
    		        }catch(Exception ex){
    		        	
    		            Toast.makeText(this,"Gagal "+sret, Toast.LENGTH_SHORT).show();
    		        }
    		        return sret;
    		 
    		    }
    
    public static String request(HttpResponse response){
    		        String result = "";
    		        try{
    		            InputStream in = response.getEntity().getContent();
    		            BufferedReader reader = new BufferedReader(new InputStreamReader(in,"iso-8859-1"), 8);
    		            
    		            StringBuilder str = new StringBuilder();
    		            String line = null;
    		            while((line = reader.readLine()) != null){
    		                str.append(line + "\n");
    		            }
    		            in.close();
    		            result = str.toString();
    		        }catch(Exception ex){
    		        	
    		            result = "Error";
    		        }
    		        return result;
    		    }

    
    
public class my_spinner_lingkungan implements OnItemSelectedListener {
    	
    	public void onItemSelected(AdapterView<?> parent, View v,int position,long id) {
    		Toast.makeText(getApplicationContext(), kd_lingkungan[position],Toast.LENGTH_SHORT).show();
    	}

    	public void onNothingSelected(AdapterView<?> parent) {}
    	
    }
    
public class my_spinner_pasar implements OnItemSelectedListener {
    	
    	public void onItemSelected(AdapterView<?> parent, View v,int position,long id) {
    		Toast.makeText(getApplicationContext(), kd_pasar[position],Toast.LENGTH_SHORT).show();
    		kd_psr_a = kd_pasar[position].toString();
    	}

    	public void onNothingSelected(AdapterView<?> parent) {}
    	
    }
    
 public class my_spinner_group implements OnItemSelectedListener {
	
	public void onItemSelected(AdapterView<?> parent, View v,int position,long id) {
		Toast.makeText(getApplicationContext(), kd_group[position],Toast.LENGTH_SHORT).show();
		kd_group_a =kd_group[position];
	}

	public void onNothingSelected(AdapterView<?> parent) {}
	
 }
 
}