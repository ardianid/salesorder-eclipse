package slo.fajar.lestari;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

public class Jupload_noo extends Activity {
	
	private DBAdapter dba=null;
	private SQLiteDatabase db=null;
	private Cursor dbcursor;
	protected ListAdapter  adapter;
	
	private static String url= "adnoo.php";
	
	ListView list1;
	Button btupload;
	Button btclose;
	
	String kd_sales;
	String nama_sales;
	String nopengajuan=null;
	
	int jmler=0;
	int jmlok=0;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_noo);
        
        Bundle bnd= getIntent().getExtras();
    	
    	kd_sales = bnd.getString("kd_sales").toString();
    	nama_sales = bnd.getString("nama_sales").toString();
    	
    	TextView tnamasales = (TextView)findViewById(R.id.tsales_upl_noo);
    		tnamasales.setText(nama_sales);
    	
    	dba = new DBAdapter(this);
    	db = dba.getWritableDatabase();
    	
    //	String sql="update noo set upl=0";
	//	db.execSQL(sql);
    	
    	list1 = (ListView)findViewById(R.id.list_uplnoo);
    	btupload = (Button)findViewById(R.id.btkirim_uplnoo);
    	btclose = (Button)findViewById(R.id.btclose_uplnoo);
    	
    	btclose.setOnClickListener(close_list);
    	btupload.setOnClickListener(btupl_list);
    	
        
	} // akhir dari event create
	
	View.OnClickListener btupl_list = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if (dbcursor.getCount() <=0) {
				return;
			}
			
			new upload_sync().execute();
			
		}
	};
	
	private void uploade_data() throws InterruptedException {
	
	 	@SuppressWarnings("unused")
		String kdsales=null;
		String jenisnoo=null;
		String nama=null;
		String alamat=null;
		String noktp=null;
		String pemilik=null;
		String tgl_lahir=null;
		String kontak=null;
		String email=null;
		String telp1=null;
		String telp2=null;
		String hp1=null;
		String hp2=null;
		String group_outl=null;
		String tipe_outl=null;
		String jenis_outl=null;
		String rayon=null;
		String rute=null;
		String area=null;
		String pasar=null;
		String lingkungan=null;
		String norek=null;
		String bank=null;
		String atasnama=null;
		String npwp=null;
		String noseri=null;
		String namanpwp=null;
		String alamatnpwp=null;
		String longi=null;
		String loti=null;
		String tanggal=null;
		
		dbcursor.moveToFirst();
		
			do {
				
				
				
				try {
				
					
					nopengajuan = dbcursor.getString(dbcursor.getColumnIndex("no_pengajuan"));
					
					kdsales = dbcursor.getString(dbcursor.getColumnIndex("kd_sales"));
					
					jenisnoo = dbcursor.getString(dbcursor.getColumnIndex("jenis_noo"));
					
					nama = dbcursor.getString(dbcursor.getColumnIndex("nama_outlet"));
					
					alamat = dbcursor.getString(dbcursor.getColumnIndex("alamat"));
					
					noktp = dbcursor.getString(dbcursor.getColumnIndex("no_ktp"));
					
					pemilik = dbcursor.getString(dbcursor.getColumnIndex("nama_pemilik"));
					
					tgl_lahir = dbcursor.getString(dbcursor.getColumnIndex("tgl_lahir"));
					
					String rtgl_lahir="";
					if (tgl_lahir.length()>=8){
						
						rtgl_lahir = parseTOdate(tgl_lahir);
						
					}
					
					
					kontak= dbcursor.getString(dbcursor.getColumnIndex("nama_kontak"));
					
					email = dbcursor.getString(dbcursor.getColumnIndex("email"));
					
					telp1 = dbcursor.getString(dbcursor.getColumnIndex("telp1"));
					
					telp2 = dbcursor.getString(dbcursor.getColumnIndex("telp2"));
					
					hp1 = dbcursor.getString(dbcursor.getColumnIndex("no_hp1"));
					
					hp2 = dbcursor.getString(dbcursor.getColumnIndex("no_hp2"));
					
					group_outl = dbcursor.getString(dbcursor.getColumnIndex("group_outl"));
					
					tipe_outl = dbcursor.getString(dbcursor.getColumnIndex("tipe_outl"));
					
					jenis_outl = dbcursor.getString(dbcursor.getColumnIndex("jenis_outl"));
					
					rayon = dbcursor.getString(dbcursor.getColumnIndex("rayon"));
					
					rute = dbcursor.getString(dbcursor.getColumnIndex("rute"));
					
					area = dbcursor.getString(dbcursor.getColumnIndex("kd_kelurahan"));
					
					pasar = dbcursor.getString(dbcursor.getColumnIndex("pasar"));
					
					lingkungan = dbcursor.getString(dbcursor.getColumnIndex("lingkungan"));
					
					norek = dbcursor.getString(dbcursor.getColumnIndex("no_rek"));
					
					bank = dbcursor.getString(dbcursor.getColumnIndex("bank"));
					
					atasnama = dbcursor.getString(dbcursor.getColumnIndex("atas_nama_rek"));
					
					npwp = dbcursor.getString(dbcursor.getColumnIndex("npwp"));
					
					noseri = dbcursor.getString(dbcursor.getColumnIndex("no_npwp_seri"));
					
					namanpwp= dbcursor.getString(dbcursor.getColumnIndex("nama_npwp"));
					
					alamatnpwp = dbcursor.getString(dbcursor.getColumnIndex("alamat_npwp"));
					
					longi = dbcursor.getString(dbcursor.getColumnIndex("longitude"));
					
					loti = dbcursor.getString(dbcursor.getColumnIndex("latitude"));
					
					tanggal = dbcursor.getString(dbcursor.getColumnIndex("tanggal"));
					
					String rtanggal="";
					if (tanggal.length()>=8){
						
						rtanggal = parseTOdate(tanggal);
						
					}
					
					ArrayList<NameValuePair> postparam = new ArrayList<NameValuePair>();
					postparam.add(new BasicNameValuePair("nopengajuan", nopengajuan));
					postparam.add(new BasicNameValuePair("kd_sales", kd_sales));
					postparam.add(new BasicNameValuePair("jenisnoo", jenisnoo));
					postparam.add(new BasicNameValuePair("nama", nama));
					postparam.add(new BasicNameValuePair("alamat", alamat));
					postparam.add(new BasicNameValuePair("noktp", noktp));
					postparam.add(new BasicNameValuePair("pemilik", pemilik));
					postparam.add(new BasicNameValuePair("tgl_lahir", rtgl_lahir));
					postparam.add(new BasicNameValuePair("kontak", kontak));
					postparam.add(new BasicNameValuePair("email", email));
					postparam.add(new BasicNameValuePair("telp1", telp1));
					postparam.add(new BasicNameValuePair("telp2", telp2));
					postparam.add(new BasicNameValuePair("hp1", hp1));
					postparam.add(new BasicNameValuePair("hp2", hp2));
					postparam.add(new BasicNameValuePair("group_outl", group_outl));
					postparam.add(new BasicNameValuePair("tipe_outl", tipe_outl));
					postparam.add(new BasicNameValuePair("jenis_outl", jenis_outl));
					postparam.add(new BasicNameValuePair("rayon", rayon));
					postparam.add(new BasicNameValuePair("rute", rute));
					postparam.add(new BasicNameValuePair("area", area));
					postparam.add(new BasicNameValuePair("pasar", pasar));
					postparam.add(new BasicNameValuePair("lingkungan", lingkungan));
					postparam.add(new BasicNameValuePair("norek", norek));
					postparam.add(new BasicNameValuePair("bank", bank));
					postparam.add(new BasicNameValuePair("atasnama", atasnama));
					postparam.add(new BasicNameValuePair("npwp", npwp));
					postparam.add(new BasicNameValuePair("noseri", noseri));
					postparam.add(new BasicNameValuePair("namanpwp", namanpwp));
					postparam.add(new BasicNameValuePair("alamatnpwp", alamatnpwp));
					postparam.add(new BasicNameValuePair("longi", longi));
					postparam.add(new BasicNameValuePair("lati", loti));
					postparam.add(new BasicNameValuePair("tanggal", rtanggal));
					
					String res = CustomHttpClient.executeHttpPost(url, postparam);
					 
					 if (res.toString().trim().equals("1")){
						 
						 update_local(nopengajuan);
						 
						 jmlok++;	// Toast.makeText(getApplicationContext(), "Data berhasil dikirim",Toast.LENGTH_SHORT).show();
					 }else {
						 jmler++; // Toast.makeText(getApplicationContext(), "Data gagal dikirim",Toast.LENGTH_SHORT).show();
					 }
					
				}catch (Exception e) {
					// TODO: handle exception
					
					jmler++;
					
				}
				
			} while (dbcursor.moveToNext());
				
			 
		
	} // akhir upload data
	
	private void update_local(String nopengajuan){
		
		String sql="update noo set upl=1 where no_pengajuan='"+ nopengajuan +"'";
		db.execSQL(sql);
		
	}
	
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
	
	View.OnClickListener close_list = new View.OnClickListener() {
		
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
	};
	
	 private void isi_list(){
		 
		 String sql="SELECT *"+
				 " FROM noo"+
				 " WHERE kd_sales='"+ kd_sales +"' and upl=0 and step=4";
		 
	//	 db = dba.getReadableDatabase(); 
		 
		 try {
			
			 dbcursor = dba.SelectData(db,sql);
	    	
			 dbcursor.moveToFirst();

	        	adapter = new SimpleCursorAdapter(
	                    this, 
	                    R.layout.upload_noo0, 
	                    dbcursor,
	                    new String[] {"no_pengajuan","nama_outlet","alamat"}, 
	                    new int[] {R.id.tv_nopeng_upl,R.id.tv_nama_upl,R.id.tv_alamat_upl});
	            list1.setAdapter(adapter);
			 
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		 
		 
	 }; // akhir dari isi)list
	 
	 
	 private String parseTOdate(String bdate) {
		 
		 DateFormat old_date = new SimpleDateFormat("dd/MM/yyyy");
		 DateFormat ftanggal = new SimpleDateFormat("yyyy/MM/dd");
		 String rdate = "";
			try {
				//rdate = old_date.parse(bdate);
				rdate = ftanggal.format(old_date.parse(bdate)) ;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		 return rdate;
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
	    	dbcursor.close();
	    	db.close();
	    }
	    
	    
	    private class upload_sync extends AsyncTask<Void,Void, String>{
	    	
	    	private ProgressDialog pDialog;
	    	
	    	@Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(Jupload_noo.this);
	            pDialog.setMessage("Mohon sabar menunggu, Proses upload sedang berlangsung...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
	        }
	    	
	    	protected void onPostExecute(String content) {
	    		
	    		pDialog.dismiss();
	    		
	            if (jmler > 0) {
	            	
	            	messageDialog(jmler + 
	                        " Upload data gagal...");
	            	
	            //	Toast.makeText(Jupload_noo.this,jmler + 
	            //            " Upload data gagal...", Toast.LENGTH_SHORT).show(); 
	            	
	            } else {
	            	
	            	messageDialog("Upload data selesai...");
	            	
	            	//Toast.makeText(Jupload_noo.this, 
	                 //       "Upload data selesai...", Toast.LENGTH_SHORT).show(); 
	            }
	            
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
	    
	    
	    
}
