package slo.fajar.lestari;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Jupload_cust extends Activity {

	private DBAdapter dba=null;
	private SQLiteDatabase db=null;
	private Cursor dbcursor;
	protected ListAdapter  adapter;
	
	private static String url= "updatecust.php";
	
	ListView list1;
	Button btupload;
	Button btclose;
	
	String kd_sales;
	String nama_sales;
	
	int jmler=0;
	int jmlok=0;
	int jmlada=0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_upcust);
        
        Bundle bnd= getIntent().getExtras();
    	
    	kd_sales = bnd.getString("kd_sales").toString();
    	nama_sales = bnd.getString("nama_sales").toString();
    	
    	TextView tnamasales = (TextView)findViewById(R.id.tsales_upl_cust);
		tnamasales.setText(nama_sales);
	
		dba = new DBAdapter(this);
		db = dba.getWritableDatabase();
    	
		/*String sql="update ms_customer set upl=0";
		db.execSQL(sql);*/
		
		list1 = (ListView)findViewById(R.id.list_uplcust);
    	btupload = (Button)findViewById(R.id.btkirim_uplcust);
    	btclose = (Button)findViewById(R.id.btclose_uplcust);
		
    	btclose.setOnClickListener(close_list);
    	btupload.setOnClickListener(btupl_list);
    	
	} // akhir dari on create
	
	View.OnClickListener close_list = new View.OnClickListener() {
		
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
	}; // akhir dari event close_list
	
	View.OnClickListener btupl_list = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if (dbcursor.getCount() <=0) {
				return;
			}
			
			new upload_sync().execute();
			
		}
	};
	
	private void isi_list(){
		 
		 String sql="SELECT _id,kd_customer,nama,alamat,lat,long,kd_sales_edit,telp1,telp2,hp1,hp2,noktp"+
				 " FROM ms_customer"+
				 " WHERE editd=1 and upl=0";
		 
		 
		 try {
			
			 dbcursor = dba.SelectData(db,sql);
	    	
			 dbcursor.moveToFirst();

	        	adapter = new SimpleCursorAdapter(
	                    this, 
	                    R.layout.upload_upcust0, 
	                    dbcursor,
	                    new String[] {"kd_customer","nama","alamat"}, 
	                    new int[] {R.id.tv_nocust_upl,R.id.tv_namacust_upl,R.id.tv_alamatcust_upl});
	            list1.setAdapter(adapter);
			 
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		 
		 
	 }; // akhir dari isi)list
	
	private void uploade_data() throws InterruptedException {
			
			String kdcust=null;
			String lati=null;
			String longi=null;
			String kdsales=null;
			String telp1=null;
			String telp2=null;
			String hp1=null;
			String hp2=null;
			String noktp=null;
			
			dbcursor.moveToFirst();
			
				do {
					
					try {
					
						
						kdcust = dbcursor.getString(dbcursor.getColumnIndex("kd_customer"));
						
						lati = dbcursor.getString(dbcursor.getColumnIndex("lat"));
						
						longi = dbcursor.getString(dbcursor.getColumnIndex("long"));
						
						kdsales = dbcursor.getString(dbcursor.getColumnIndex("kd_sales_edit"));
						
						telp1 = dbcursor.getString(dbcursor.getColumnIndex("telp1"));
						
						telp2 = dbcursor.getString(dbcursor.getColumnIndex("telp2"));
						
						hp1 = dbcursor.getString(dbcursor.getColumnIndex("hp1"));
						
						hp2 = dbcursor.getString(dbcursor.getColumnIndex("hp2"));
						
						noktp = dbcursor.getString(dbcursor.getColumnIndex("noktp"));
						
						ArrayList<NameValuePair> postparam = new ArrayList<NameValuePair>();
						postparam.add(new BasicNameValuePair("kd_cust", kdcust));
						postparam.add(new BasicNameValuePair("lat", lati));
						postparam.add(new BasicNameValuePair("long", longi));
						postparam.add(new BasicNameValuePair("kd_sales", kdsales));
						postparam.add(new BasicNameValuePair("telp1", telp1));
						postparam.add(new BasicNameValuePair("telp2", telp2));
						postparam.add(new BasicNameValuePair("hp1", hp1));
						postparam.add(new BasicNameValuePair("hp2", hp2));
						postparam.add(new BasicNameValuePair("noktp", noktp));
						
						String res = CustomHttpClient.executeHttpPost(url, postparam);
						
						Log.v("hasilupl", kdcust+","+lati+","+longi+","+kdsales+","+telp1+","+telp2+","+hp1+","+hp2+","+noktp);
						
						 if (res.toString().trim().equals("1")){
							 
							 update_local(kdcust);
							 
							 jmlok++;	// Toast.makeText(getApplicationContext(), "Data berhasil dikirim",Toast.LENGTH_SHORT).show();
						 }else if (res.toString().trim().equals("0")) {
							 jmler++; // Toast.makeText(getApplicationContext(), "Data gagal dikirim",Toast.LENGTH_SHORT).show();
						 }else{
							 
						//	 Toast.makeText(getApplicationContext(),dbcursor.getString(dbcursor.getColumnIndex("nama")) + "\n"+ "Sudah diupdate oleh sales lain",Toast.LENGTH_SHORT).show();
							 
							 jmlada++;
							 update_local(kdcust);
							 
						 }
						
					}catch (Exception e) {
						// TODO: handle exception
						
						jmler++;
						
					}
					
				} while (dbcursor.moveToNext());
					
				 
			
		} // akhir upload data
	 
	private void messageDialog(String message){
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Konfirm Dialog");
		alertDialog.setMessage(message);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		        }
		});
		alertDialog.show();
	} // akhir dari messagedialog  
	
		private void update_local(String nopengajuan){
			
			String sql="update ms_customer set upl=1 where kd_customer='"+ nopengajuan +"'";
			db.execSQL(sql);
			
		}

		private class upload_sync extends AsyncTask<Void,Void, String>{
	    	
	    	private ProgressDialog pDialog;
	    	
	    	@Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(Jupload_cust.this);
	            pDialog.setMessage("Mohon sabar menunggu, Proses upload sedang berlangsung...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
	        }
	    	
	    	protected void onPostExecute(String content) {
	    		
	    		pDialog.dismiss();
	            
	    		String msg;
	    		
	    		if (jmler > 0){
	    			
	    			if (jmlada > 0) {
		    			msg = jmlada + " Data Telah diupdate sales lain (OK)" + "\n"+
		    					jmler + 
		                        " Upload data gagal";
		    		}else{
		    			msg = 	jmler + 
		                        " Upload data gagal";
		    		}
	    			
	            	messageDialog(msg);
	            	
	            //	Toast.makeText(Jupload_noo.this,jmler + 
	            //            " Upload data gagal...", Toast.LENGTH_SHORT).show(); 
	            	
	            } else {
	            	
	            	if (jmlada > 0) {
		    			msg = jmlada + " Data Telah diupdate sales lain (OK)" + "\n"+
		    					"Data lain Berhasil Upload (Ok)";
		    		}else{
		    			msg = 	"Upload data selesai...";
		    		}
	            	
	            	messageDialog(msg);
	            	
	            	//Toast.makeText(Jupload_noo.this, 
	                 //       "Upload data selesai...", Toast.LENGTH_SHORT).show(); 
	            }
	            
	    		jmlada=0;
	    		jmler=0;
	    		jmlok=0;
	    		
	            isi_list();
	            
	        }
	    	
			@Override
			protected String doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				
				try {
					uploade_data();
				} catch (Exception e) {
					// TODO: handle exception
					Log.v("error upl noo", e.toString());
				}
				
				
				return null;
			}
	    	
			
	    } // akhir dari asyncTask
		
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
	    	dbcursor.close();
	    	db.close();
	    }
}
