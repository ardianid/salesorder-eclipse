package slo.fajar.lestari;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Cuseraddbykod extends Activity {

	private DBAdapter dba=null;
	private SQLiteDatabase db=null;
	
	private static String url= "getsales.php";
	
	EditText tkodesales;
	EditText tnamasales;
	EditText tnamauser;
	EditText tpassword;
	
	ImageButton btcari;
	Button btsimpan;
	Button btcancel;
	
	String hasilnamasales=null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.luserbyserver);
        
        tkodesales= (EditText)findViewById(R.id.tkode_ub);
        tnamasales= (EditText)findViewById(R.id.tnamasal_ub);
        tnamauser= (EditText)findViewById(R.id.tuserub);
        tpassword= (EditText)findViewById(R.id.tpwd_ub);
        
        
        btcari= (ImageButton)findViewById(R.id.bt_cari_ub);
        btsimpan= (Button)findViewById(R.id.btsimpan_ub);
        btcancel= (Button)findViewById(R.id.btcancel_ub);
        
        dba = new DBAdapter(this);
        
        btcari.setOnClickListener(btceksales);
        btsimpan.setOnClickListener(btsimpanok);
        btcancel.setOnClickListener(btbcack_list);
        
        tkodesales.requestFocus();
        
	}// akhir dari on create
	
	@Override
    protected void onResume(){
    	super.onResume();
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
	
	
	View.OnClickListener btbcack_list = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
	}; // akhir listener btback_list
	
	View.OnClickListener btsimpanok = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			simpan();
		}
	};
	
	View.OnClickListener btceksales = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			if (tkodesales.getText().toString().trim().length()==0){
				Toast.makeText(getBaseContext(), "Isi dulu kode sales", Toast.LENGTH_LONG).show();
				return;
			}
			
			new upload_sync().execute();
		}
	};
	
	private void ceksales() throws InterruptedException{
		
		try {
			
			ArrayList<NameValuePair> postparam = new ArrayList<NameValuePair>();
			postparam.add(new BasicNameValuePair("kodesales", tkodesales.getText().toString().trim() ));
			String res = CustomHttpClient.executeHttpPost(url, postparam);
			
			String respon =res.toString().trim();
			
			 if (!respon.equals("error")){
				 
				 hasilnamasales = respon;
				 
			 }else{
				 hasilnamasales=respon;
			 }
			
		} catch (Exception e) {
			// TODO: handle exception
			hasilnamasales ="error2";
		}
		
		
		
	}// akhir dari cek sales
	
    private class upload_sync extends AsyncTask<Void,Void, String>{
    	
    	private ProgressDialog pDialog;
    	
    	@Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Cuseraddbykod.this);
            pDialog.setMessage("Cek User...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
    	
    	protected void onPostExecute(String content) {
    		
    		pDialog.dismiss();
            
    		if (hasilnamasales.equals("error")){
    			Toast.makeText(getBaseContext(), "Kode tidak ditemukan", Toast.LENGTH_LONG).show();
    		//	tkodesales.requestFocus();
    		}else if (hasilnamasales.equals("error2")){
    			Toast.makeText(getBaseContext(), "Kode tidak ditemukan", Toast.LENGTH_LONG).show();
    		//	tkodesales.requestFocus();
    		}else{
    			tnamasales.setText(hasilnamasales);
    			//tnamauser.requestFocus();
    		}
    		
        }
    	
		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
			try {
				ceksales();
			} catch (Exception e) {
				// TODO: handle exception
				Log.v("error cek sales server", e.toString());
			}
			
			
			return null;
		}
    	
		
    } // akhir dari asyncTask
	
    private void simpan(){
    	try {
			
    		String kdsales= tkodesales.getText().toString().trim();
    		String namasales = tnamasales.getText().toString().trim();
    		String namauser = tnamauser.getText().toString().trim();
    		String pwd = tpassword.getText().toString().trim();
    		
    		if (kdsales.length()==0){
    			Toast.makeText(getBaseContext(), "Kode sales harus diisi..", Toast.LENGTH_LONG).show();
    			tkodesales.requestFocus();
    			return;
    		}
    		
    		if (namasales.length()==0){
    			Toast.makeText(getBaseContext(), "Cek dulu kebenaran sales..", Toast.LENGTH_LONG).show();
    			return;
    		}
    		
    		if (namauser.length()==0){
    			Toast.makeText(getBaseContext(), "User harus diisi..", Toast.LENGTH_LONG).show();
    			tnamauser.requestFocus();
    			return;
    		}
    		
    		if (pwd.length()==0){
    			Toast.makeText(getBaseContext(), "Password harus diisi..", Toast.LENGTH_LONG).show();
    			tpassword.requestFocus();
    			return;
    		}
    		
    		db = dba.getWritableDatabase();
    		String sqlcariuser ="select nama_user from ms_user where nama_user='"+ namauser +"'";
    		Cursor cursor2 = dba.SelectData(db,sqlcariuser);
			cursor2.moveToFirst(); 
			
			int jml_baris = cursor2.getCount();
	        
	        if(jml_baris > 0)  {
	        	
	        	String namaus= cursor2.getString(0);
	        	
	        	if (namaus.length() < 1){
	        		add_user(namauser, pwd, kdsales, namasales);
	        		db.close();
	        		
	        		tkodesales.setText("");
	        		tnamasales.setText("");
	        		tnamauser.setText("");
	        		tpassword.setText("");
	        		
	        		Toast.makeText(getBaseContext(), "User ditambahkan...", Toast.LENGTH_LONG).show();
	        		
	        		tkodesales.requestFocus();
	        		
	        		
	        	}else {
	        		Toast.makeText(getBaseContext(), "User sudah ada silahkan rubah", Toast.LENGTH_LONG).show();
	        		tnamauser.requestFocus();
	        		db.close();
	        		return;
	        	}
	        	
	        	
	        }else{
	        	add_user(namauser, pwd, kdsales, namasales);
	        	db.close();
	        	
	        	tkodesales.setText("");
        		tnamasales.setText("");
        		tnamauser.setText("");
        		tpassword.setText("");
        		
        		Toast.makeText(getBaseContext(), "User ditambahkan...", Toast.LENGTH_LONG).show();
        		
        		tkodesales.requestFocus();
	        	
	        	
	        }
    		
    		
		} catch (Exception e) {
			// TODO: handle exception
			Log.v("error simpan salesbyserv", e.toString());
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
    	
    }// akhir dari simpan
    
    private void add_user(String namauser,String pwd,String kdsales,String namasales){
		
		String sql="insert into ms_user (nama_user,kd_sales,nama_sales,pwd) values('"+ namauser + "','"+ kdsales +"','"+ namasales +"','"+ pwd +"')";
		db.execSQL(sql);
		
		
	}
    
}
