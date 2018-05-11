package slo.fajar.lestari;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class Cutility extends Activity {

	String menu[]={"Backup Data","Update Master","Hapus All Transaksi","Tambah User","Rubah Password", "Kembali"};
	ListView list1;
	
	String kd_sal;
	String nama_sal;
	
	private DBAdapter dba=null;
	private SQLiteDatabase db=null;
	private Cursor dbcursor;
	
	String statbackup;
	String stathapusall;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.lutility);
        
        
        dba = new DBAdapter(this);
        
        Bundle kd_sales= getIntent().getExtras();
        kd_sal = kd_sales.getString("kd_sales");
        
        nama_sal = kd_sales.getString("nama_sales");
        
        
        list1 = (ListView)findViewById(R.id.listutility);
        list1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,  menu)); 
        list1.setTextFilterEnabled(true);
        
        list1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    // When clicked, show a toast with the TextView text
				String pilihan = menu[position];
				
				Intent myIntent = null;  
		        if(pilihan.equalsIgnoreCase("Kembali"))  
		            finish();  
		        else {
		        	if(pilihan.equalsIgnoreCase("Update Master")){ 
		        		myIntent = new Intent(getBaseContext(), CupdateData.class);
						
		            	myIntent.putExtra("kd_sales", kd_sal);
		            	myIntent.putExtra("nama_sales", nama_sal);
		            	
		            	startActivity(myIntent);
		            	
		        	}else if(pilihan.equalsIgnoreCase("Tambah User")){
		        		showDialog(1);
		        	}else if(pilihan.equalsIgnoreCase("Rubah Password")){
		        		showDialog(2);
		        	}else if(pilihan.equalsIgnoreCase("Backup Data")){
		        		new backup_sync().execute();
		        	}else if(pilihan.equalsIgnoreCase("Hapus All Transaksi")){
		        		new hapusalltrans_sync().execute();
		        	}
		        	
		        }
		        
			}
		});
        
        
        
	}// akhir dari on create
	
	private void hapus_all_trans(){
		try {
			
						
			db = dba.getWritableDatabase();
			
			String sql_delfaktur_d = "delete from faktur_d";
 			String sql_delfaktur_h = "delete from faktur_h";
 			String sql_delnoo = "delete from noo";
			
 			db.execSQL(sql_delnoo);
 			db.execSQL(sql_delfaktur_h);
 			db.execSQL(sql_delfaktur_d);
			
			stathapusall="benar";
			
		} catch (Exception e) {
			// TODO: handle exception
			stathapusall="salah";
			e.printStackTrace();
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
	}
	
	private class hapusalltrans_sync extends AsyncTask<Void,Void, String>{
    	
    	private ProgressDialog pDialog;
    	
    	@Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Cutility.this);
            pDialog.setMessage("Mohon sabar menunggu, Proses sedang berlangsung...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
    	
    	protected void onPostExecute(String content) {
    		
    		pDialog.dismiss();
    		String msg;
    		
    		if (stathapusall.equals("benar")){
    			msg ="Data berhasil dihapus...";
    		}else{
    			msg ="Data gagal dihapus...";
    		}
            	messageDialog(msg);
            	
            	stathapusall="";
            	
            
        }
    	
		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
			try {
				hapus_all_trans();
			} catch (Exception e) {
				// TODO: handle exception
				Log.v("error upl noo", e.toString());
			}
			
			
			return null;
		}
    	
		
    } // akhir dari asyncTask
	
	
	private void backupdata(){
		
		// InputStream myInput;
		 
			try {
				
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
				 
				//Context contsext = getApplicationContext();
				String dir = getBaseContext().getDatabasePath("mydb").getPath();
			//	String letakdbase =dir;
				
			//	myInput = new FileInputStream(letakdbase);//this is
				// the path for all apps
				//insert your package instead packagename,ex:com.mybusiness.myapp
	 
	 
			    // Set the output folder on the SDcard
			    File directory = new File( Environment.getExternalStorageDirectory().getPath()+ "/backup");
			    // Create the folder if it doesn't exist:
			    if (!directory.exists()) 
			    {
			        directory.mkdirs();
			    } 
			    // Set the output file stream up:
			    
			    File pathtujuan =new File(Environment.getExternalStorageDirectory()+ "/backup");
			    File pathawal =new File(dir.substring(0, dir.length() -4));
			    
			    String namadbase = kd_sal +"."+tgl+bln+thn+"."+jam+menit+detik;
			    
			    File currentDB = new File(pathawal, "/mydb");
	            File backupDB = new File(pathtujuan, "/"+namadbase);
			    
	            if (currentDB.exists()) {
	                FileChannel src = new FileInputStream(currentDB).getChannel();
	                FileChannel dst = new FileOutputStream(backupDB).getChannel();
	                dst.transferFrom(src, 0, src.size());
	                src.close();
	                dst.close();
	            }
			    
	            statbackup="benar";
	            
			  //  Log.v("letak database", directory + 
			    //		"/"+namadbase);
			    
			   // OutputStream myOutput = new FileOutputStream(letakdbase);
			    
			  //  InputStream myInputs = new FileInputStream(directory + 
			  //  		"/"+namadbase);
			    
			  //  CopyDB(getBaseContext().getAssets().open("mydb"), new FileOutputStream(directory + 
			   // 		"/"+namadbase));
			    
			}catch (Exception e){
				statbackup="salah";
				e.printStackTrace();
				Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
			}
			
			
			
		
	}
	
	 public void CopyDB(InputStream inputStream, OutputStream outputStream) 
	        	throws IOException {
	        		//--copy 1K byte pada saat ini---
	        		byte[] buffer = new byte[1024];
	        		int length;
	        		while ((length = inputStream.read(buffer))>0) {
	        			outputStream.write(buffer, 0, length);
	        		}
	        		inputStream.close();
	        		outputStream.close();
	        	}  
	
	private class backup_sync extends AsyncTask<Void,Void, String>{
    	
    	private ProgressDialog pDialog;
    	
    	@Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Cutility.this);
            pDialog.setMessage("Mohon sabar menunggu, Proses backup sedang berlangsung...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
    	
    	protected void onPostExecute(String content) {
    		
    		pDialog.dismiss();
    		String msg;
    		
    		if (statbackup.equals("benar")){
    			msg ="Data berhasil dibackup...";
    		}else{
    			msg ="Data gagal dibackup...";
    		}
            	messageDialog(msg);
            	
            	statbackup="";
            	
            
        }
    	
		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
			try {
				backupdata();
			} catch (Exception e) {
				// TODO: handle exception
				Log.v("error upl noo", e.toString());
			}
			
			
			return null;
		}
    	
		
    } // akhir dari asyncTask
	
	
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
	
	@Override
	 protected Dialog onCreateDialog(int id) {
	 
	  AlertDialog dialogDetails = null;
	 
	  switch (id) {
	  case 1:
	   LayoutInflater inflater = LayoutInflater.from(this);
	   View dialogview = inflater.inflate(R.layout.lakt_adm, null);
	 
	   AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
	   dialogbuilder.setTitle("Aktivasi Administrator");	
	   dialogbuilder.setView(dialogview);
	   dialogDetails = dialogbuilder.create();
	 
	   break;
	   
	  case 2:
		  
		  LayoutInflater inflater2 = LayoutInflater.from(this);
		  View dialogview2 = inflater2.inflate(R.layout.lrubah_pwd, null);
		 
		   AlertDialog.Builder dialogbuilder2 = new AlertDialog.Builder(this);
		   dialogbuilder2.setTitle("Rubah Password");	
		   dialogbuilder2.setView(dialogview2);
		   dialogDetails = dialogbuilder2.create();
		  
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
				     .findViewById(R.id.btkeluar_akt);
		   
		   Button okbutton = (Button) alertDialog
				     .findViewById(R.id.btmasuk_akt);
		   
		  
		   
		   okbutton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					EditText tpwd = (EditText)dialog.findViewById(R.id.tpwd_akt);
					
					Integer passw = tpwd.getText().length();
					
					if (passw < 1){
						Toast.makeText(getApplicationContext(), "Password harus diisi",Toast.LENGTH_SHORT).show();
						tpwd.requestFocus();
						return;
					}
					
					try {
						db = dba.getReadableDatabase();
						
						String sql = "select nama_user from ms_user where nama_user='admin' and pwd='"+ tpwd.getText().toString() +"'";
						dbcursor = dba.SelectData(db,sql);
						dbcursor.moveToFirst(); 
						
						
						int jml_baris = dbcursor.getCount();
				        
				        if(jml_baris > 0)  {
				        	
				        	String nama_user= dbcursor.getString(0);
				        	
				        	if (nama_user.length()>=1){
				        		
				        		tpwd.setText("");
				        		
				        		db.close();
				        		dialog.dismiss();
				        		
				        		Intent myIntent = null;  
				        		
				        		myIntent = new Intent(getBaseContext(), Cuseradd.class);
								
				            	myIntent.putExtra("kd_sales", kd_sal);
				            	myIntent.putExtra("nama_sales", nama_sal);
				        		
				            	startActivity(myIntent);
				            	
				        	}else{
				        		Toast.makeText(getApplicationContext(), "Password salah",Toast.LENGTH_SHORT).show();
				        	}
				        	
				        	
				        }else{
				        	Toast.makeText(getApplicationContext(), "Password salah",Toast.LENGTH_SHORT).show();
				        }
						
				        
					} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
						db.close();
					}
					
					tpwd.setText("");
					
				}
		   }); //akhir dari ok button
		   
		   cancelbutton.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					alertDialog.dismiss();
				}});
			   
			   break;
		   
	  case 2:
	
		  final AlertDialog alertDialog2 = (AlertDialog) dialog;
		   Button cancelbutton2 = (Button) alertDialog2
				     .findViewById(R.id.btcancelpwd);
		   
		   Button okbutton2 = (Button) alertDialog2
				     .findViewById(R.id.btrubahpwd);
		  
		   final EditText tuser_rbh = (EditText)dialog.findViewById(R.id.tuser_rbh);
		   final EditText tpwd_rbh = (EditText)dialog.findViewById(R.id.tpwd_rbh1);
		   
		   tuser_rbh.setText(nama_sal);
		   tuser_rbh.setEnabled(false);
		   
		   tpwd_rbh.requestFocus();
		   
		   okbutton2.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					Integer isipwd = tpwd_rbh.getText().length();
					
					if (isipwd < 1) {
						Toast.makeText(getApplicationContext(), "Password harus diisi",Toast.LENGTH_SHORT).show();
						tpwd_rbh.requestFocus();
						return;
					}
					
					try {
						
						db = dba.getReadableDatabase();
						String sql="update ms_user set pwd='"+ tpwd_rbh.getText().toString() +"' where nama_user='"+ tuser_rbh.getText().toString() +"'";
						
						db.execSQL(sql);
						
						db.close();
						alertDialog2.dismiss();
						
						Toast.makeText(getApplicationContext(), "Password "+ tuser_rbh.getText().toString() +" telah dirubah..."  ,Toast.LENGTH_SHORT).show();
						
					} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
						db.close();
					}
					
					tpwd_rbh.setText("");
					
					
					
				}});
		   
		   cancelbutton2.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					alertDialog2.dismiss();
				}});
		   
		  break;
			   
	  }} // akhir dari on-prepare
	
}
