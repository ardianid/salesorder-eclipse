package slo.fajar.lestari;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Jupload extends Activity {
	
	//private OperasiInternet oi;
	private DBAdapter dba=null;
	private SQLiteDatabase db=null;
	
	ListView list1;
	
	Integer jmlorder;
	Integer jmleditcust;
	Integer jmlnoo;
	
	String kd_Sales;
	String nama_sales;
	
	 String menu[];
	
	@Override
    public void onCreate(Bundle savedInstanceState) {                           
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploaddata);
        
        list1 = (ListView)findViewById(R.id.vlist_ordermanual);
        
        dba = new DBAdapter(this);
        
        Bundle bnd= getIntent().getExtras();
        
        kd_Sales = bnd.getString("kd_sales");
        nama_sales = bnd.getString("nama_sales");     
        
        
        list1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    // When clicked, show a toast with the TextView text
				String pilihan = menu[position];
				
		        if(pilihan.equalsIgnoreCase("4. Kembali"))  
		            finish();  
		        else {  
		            if(pilihan.startsWith("1. Order")){  
		                
		            	try {
							SetLoad(1);
						} catch (InterruptedException e) {
							// TODO: handle exception
							Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
						}
							
		                
		            }  else if(pilihan.startsWith("2. Noo")){
		            	
		            	try {
							SetLoad(2);
						} catch (InterruptedException e) {
							// TODO: handle exception
							Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
						}
						
		            }else if(pilihan.startsWith("3. Update Customer")){
		            	
		            	try {
							SetLoad(3);
						} catch (InterruptedException e) {
							// TODO: handle exception
							Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
						}
		            	
		            } 
		        
		        }  
				
			}
		});
        
	} // akhir dari event create
	
	private void SetLoad(int setload) throws InterruptedException {
		
		Intent myIntent = null; 
		
	//	oi = new OperasiInternet(this);
	//	if(oi.CheckInternetConnection()){
		
			switch (setload) {
			case 1:
				
				myIntent = new Intent(getBaseContext(), Jupload_fakt.class);
				
				myIntent.putExtra("kd_sales", kd_Sales);
				myIntent.putExtra("nama_sales", nama_sales);
				
				break;
				
			case 2:
				
				myIntent = new Intent(getBaseContext(), Jupload_noo.class);
				
				myIntent.putExtra("kd_sales", kd_Sales);
				myIntent.putExtra("nama_sales", nama_sales);
				
				break;
				
			case 3:
				
				myIntent = new Intent(getBaseContext(), Jupload_cust.class);
				
				myIntent.putExtra("kd_sales", kd_Sales);
				myIntent.putExtra("nama_sales", nama_sales);
				
				break;
				
				
			}
			
			startActivity(myIntent); 
			
	//	}else{
	//		messageDialog("Gagal melakukan Koneksi internet \n" +
	//				"Proses Upload Data tidak bisa dilanjutkan");
	//	}
		
	}
	
	private void isi_list(){
		
		cekjml_order();
		cekjml_editcust();
		cekjml_noo();
		
		String menu1 = "1. Order"+" ("+ jmlorder + ")"; 
		String menu2 = "2. Noo"+" ("+ jmlnoo + ")";
		String menu3 = "3. Update Customer"+" ("+ jmleditcust + ")";
		
		menu = new String[] {menu1 ,menu2,menu3, "4. Kembali"};
			
			list1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,  menu)); 
	        list1.setTextFilterEnabled(true);
		
	        
	} // akhir isi list
	
	private void cekjml_order(){
		
		db = dba.getReadableDatabase();
		
		try {
		
			String sql="select count(no_bukti) as jml from faktur_h where trans=0 and kd_salesman='"+ kd_Sales +"'";
			
			Cursor cursor = dba.SelectData(db,sql);
			cursor.moveToFirst(); 
			
	        int jml_baris = cursor.getCount();
			if (jml_baris != 0) {
				
				jmlorder = Integer.valueOf(cursor.getString(cursor.getColumnIndex("jml")));
				
			}else{
				jmlorder=0;
			}
	        
			cursor.close();
					
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		
	}
	
	private void cekjml_editcust(){
		
		db = dba.getReadableDatabase();
		
		try {
		
			String sql="select count(kd_customer) as jml from ms_customer where editd=1 and upl=0 and kd_sales_edit='"+ kd_Sales +"'";
			
			Cursor cursor = dba.SelectData(db,sql);
			cursor.moveToFirst(); 
			
	        int jml_baris = cursor.getCount();
			if (jml_baris != 0) {
				
				jmleditcust = Integer.valueOf(cursor.getString(cursor.getColumnIndex("jml")));
				
			}else{
				jmleditcust=0;
			}
	        
			cursor.close();
					
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		
	}
	
	private void cekjml_noo(){
		
		db = dba.getReadableDatabase();
		
		try {
		
			String sql="select count(no_pengajuan) as jml from noo where upl=0 and step=4 and kd_sales='"+ kd_Sales +"'";
			
			Cursor cursor = dba.SelectData(db,sql);
			cursor.moveToFirst(); 
			
	        int jml_baris = cursor.getCount();
			if (jml_baris != 0) {
				
				jmlnoo = Integer.valueOf(cursor.getString(cursor.getColumnIndex("jml")));
				
			}else{
				jmlnoo=0;
			}
	        
			cursor.close();
					
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		
	}

	
	@Override
	protected void onResume(){
		super.onResume();
		isi_list();
	}
	
	@Override
	protected void onPause(){
		super.onPause();
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		db.close();
	}
	
	/*
	private void messageDialog(String message){
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Alert Dialog");
		alertDialog.setMessage(message);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		        }
		});
		alertDialog.show();
	} // akhir dari messagedialog */
	
	
}
