package slo.fajar.lestari;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.AdapterView.OnItemSelectedListener;

public class jfaktur2 extends Activity {
	
	private DBAdapter dba=null;
	private SQLiteDatabase db=null;
	private Cursor dbcursor;
	private Cursor cursor;
	protected ListAdapter  adapter;
	
	String Qsql;
	String kd_cust="";
	String nama_cust="";
	String kd_sales="";
	String nama_sales="";
	String nobukti="";
	String tgltrans=null;
	String perubahan2;
	String kd_kondisi;
	
	String jenisbayar[]={"Kredit","Tunai"};
	
	TextView tnamacust;
	TextView tsales;
	Spinner sp_crbayar;
	ListView list1;
	
	String longi;
	String loti;
	
	String longi_now;
	String loti_now;
	String jarak;
	
	int nobukti_d;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {                           
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail);
		
        dba = new DBAdapter(this);
        
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date(); 
    	
        tgltrans =dateFormat.format(date);
        
        Bundle bnd= getIntent().getExtras();
        
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
        	
        //	Log.v("isi", perubahan2);
        	
        	
        //	Toast.makeText(getBaseContext(), "(Toko)-> Longitude : "+ longi +" Latitude : "+ loti, Toast.LENGTH_LONG).show();
		//	Toast.makeText(getBaseContext(), "(Saat ini)-> Longitude : "+ longi_now +" Latitude : "+ loti_now, Toast.LENGTH_LONG).show();
		//	Toast.makeText(getBaseContext(), "(Jarak) : "+ jarak , Toast.LENGTH_LONG).show();
			
        	tnamacust = (TextView)findViewById(R.id.namacust_ord);
        	tsales = (TextView)findViewById(R.id.namasal_ord);
        	sp_crbayar = (Spinner)findViewById(R.id.spin_crbay);
        	
        	tnamacust.setText(nama_cust);
        	tsales.setText(nama_sales);
        	
        	ArrayAdapter<String> aa=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,jenisbayar);
        	sp_crbayar.setAdapter(aa);
        	
        	 
            
            list1 = (ListView)findViewById(R.id.listorder);
            
            Button kembali = (Button)findViewById(R.id.btkembali_sinc);
        	kembali.setOnClickListener(new View.OnClickListener() {
    			
    			public void onClick(View v) {
    				// TODO Auto-generated method stub
    				finish();
    			}});
            
        	
        	sp_crbayar.setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					cekbukti();
				}

				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
			});
     
        	
        	final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder
        	.setTitle("Hapus data..")
        	.setMessage("Yakin akan dihapus ?")
        	.setIcon(android.R.drawable.ic_dialog_alert)
        	
        	.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					delete_item_Onlist(nobukti_d);
				}
			} )
        	.setNegativeButton("No", null);
        
        	
        	list1.setOnItemClickListener(new OnItemClickListener() {
        		
        		public void onItemClick(AdapterView<?> parent, View view,
    					int position, long id) {
        			
        				
        					nobukti_d=Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndex("no_bukti")));
        					
        					
        				if (nobukti_d >0 ){
        					
        					String transd=dbcursor.getString(dbcursor.getColumnIndex("trans"));
        					
        					if (transd.equals("TERKIRIM")){
        						Toast.makeText(getBaseContext(), "Data sudah dikirim/diuploade tidak dapat dirubah..", Toast.LENGTH_LONG).show();
        						return;
        					}
        					
        					builder.show();

        				}		
        		}});
        	
        	
        	Button tambah = (Button)findViewById(R.id.btadd_ord);
        	tambah.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				//	cekcoordinat();
					
					/*
					if (longi.length() <=0 || loti.length() <=0 ) {
						Toast.makeText(getBaseContext(), "Coordinat Customer belum ada....", Toast.LENGTH_LONG).show();
						return;
					} */
					
					String	carabayar= (String) sp_crbayar.getSelectedItem();
					
					Intent myIntent = new Intent(getBaseContext(), jadd_order.class);
					
					
					myIntent.putExtra("kd_sales",kd_sales);
					myIntent.putExtra("nama_sales",nama_sales);
					myIntent.putExtra("kd_cust",kd_cust);
					myIntent.putExtra("nama_cust",nama_cust);
					myIntent.putExtra("longi", longi);
					myIntent.putExtra("loti", loti);
					myIntent.putExtra("nobukti", nobukti);
					myIntent.putExtra("carabayar", carabayar);
					myIntent.putExtra("longi_now", longi_now);
					myIntent.putExtra("loti_now", loti_now);
					myIntent.putExtra("jarak", jarak);
					myIntent.putExtra("perubahan", perubahan2);
					myIntent.putExtra("kondisi", kd_kondisi);
					
					startActivity(myIntent); 
					
					
				}
			});  
        	
	}
	
	@Override
    protected void onResume(){
    	super.onResume();
    	 cekbukti();
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

 
    
    
	private void cekbukti(){
		
		String	carabayar= (String) sp_crbayar.getSelectedItem();
		
		
		String sql="select no_bukti from faktur_h where tanggal='"+ tgltrans +"' and kd_salesman='"+ kd_sales +"' and kd_cust='"+ kd_cust +"' and carabayar='"+ carabayar.trim() +"'";
		
		db = dba.getReadableDatabase();
		
		try {
		
			cursor = dba.SelectData(db, sql);
			cursor.moveToFirst(); 
			
			int jml_baris = cursor.getCount();
			
	        
	        if(jml_baris == 0)  {
	        	
	        	cursor.close();
	        	
	        	nobukti="";
	        	
	        	perubahan2="0";
	        	
	        	isi_listview("xx");
	        	
				return;
	        	
	        }else{
	        	
	        	perubahan2="1";
	        	nobukti =cursor.getString(0);
	        	
	        	isi_listview(nobukti);
	        	
	        	cursor.close();
	        	
				return;
	        	
	        }
			
		}catch (Exception e){
		//	Log.v("bug cekbukti", e.toString());
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
    }; 
	 
	private void delete_item_Onlist(int nobuktidetail){
		
		db = dba.getWritableDatabase(); 
		
		try{
			
			String sql="delete from faktur_d where no_bukti="+nobuktidetail;
			
			db.execSQL(sql);
			
			dbcursor.requery();
			
			
		}catch(Exception e){
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		
	};
	
	private void isi_listview(String nobukti){
		
		Qsql ="SELECT     faktur_h._id,faktur_d.no_bukti,faktur_d.kd_barang, ms_barang.nama_barang, faktur_d.qty1, faktur_d.qty2, faktur_d.qty3, ms_barang.satuan1, ms_barang.satuan2,ms_barang.satuan3,CASE WHEN faktur_d.trans=1 THEN 'TERKIRIM' ELSE '' END as trans "+
					"FROM faktur_d INNER JOIN "+
                      "faktur_h ON faktur_d.no_bukti_h = faktur_h.no_bukti INNER JOIN "+
                      "ms_barang ON faktur_d.kd_barang = ms_barang.kd_barang "+
                      "where faktur_h.no_bukti='"+ nobukti +"'";	
			
		//db = dba.getReadableDatabase(); 
    	
    	
    	try {
    	
    		dbcursor = dba.SelectData(db,Qsql);
    		
    	//	Log.v("cek", "sampe 2");
    		
        	adapter = new SimpleCursorAdapter(
                    this, 
                    R.layout.det_grid_order, 
                    dbcursor,
                    new String[] {"kd_barang","nama_barang","qty1","qty2","qty3","satuan1","satuan2","satuan3","trans"}, 
                    new int[] {R.id.tkode_ord,R.id.tnama_ord,R.id.tjml1_ord,R.id.tjml_ord2,R.id.tjml_ord3,R.id.tsat_ord1,R.id.tsat_ord2,R.id.tsat_ord3,R.id.tstat_ord});
            list1.setAdapter(adapter); 
		
		
    }catch (Exception e) {

		Log.e("error list cust", e.toString());
		Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
	}};

	
	
}
