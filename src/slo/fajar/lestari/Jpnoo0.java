package slo.fajar.lestari;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class Jpnoo0 extends Activity {

	private DBAdapter dba=null;
	private SQLiteDatabase db=null;
	private Cursor dbcursor;
	protected ListAdapter  adapter;
	
	ListView list1;
	
	Button bttambah;
	Button btclose;
	
	String kd_sales;
	String nama_sales;
	String nopengajuan;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.pnoo0);
	        
	        
	        Bundle bnd= getIntent().getExtras();
        	
        	kd_sales = bnd.getString("kd_sales").toString();
        	nama_sales = bnd.getString("nama_sales").toString();
	        
        	dba = new DBAdapter(this);
        	
        	list1 = (ListView)findViewById(R.id.listnoo);
        	bttambah = (Button)findViewById(R.id.btadd_noo);
        	btclose = (Button)findViewById(R.id.btclose_noo);
        	
        	bttambah.setOnClickListener(bttambah_list);
        	btclose.setOnClickListener(btclose_list);
        	
        	list1.setOnItemClickListener(new OnItemClickListener() {
        		
        		public void onItemClick(AdapterView<?> parent, View view,
    					int position, long id) {
        			
        			nopengajuan = dbcursor.getString(dbcursor.getColumnIndex("no_pengajuan"));
        			
        			showDialog(1);
        			
        		}
        		
			}); // akhir dari event Onitemclick 
        	
	 } // akhir dari onCreate
	 
	 View.OnClickListener bttambah_list = new View.OnClickListener() {
		
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			Intent myIntent = null;
			myIntent = new Intent(getBaseContext(), jpnoo.class);
			
			//myIntent = new Intent(getBaseContext(), Jpnoo4.class);
			
        	myIntent.putExtra("kd_sales", kd_sales);
        	myIntent.putExtra("nama_sales", nama_sales);
        	myIntent.putExtra("nopengajuan", "");
			
        	startActivity(myIntent);
        	
		}
	};
	
	View.OnClickListener btclose_list = new View.OnClickListener() {
		
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
	};
	
	 private void isi_list(){
		 
		 String sql="SELECT _id,no_pengajuan, nama_outlet,alamat"+
				 " FROM noo"+
				 " WHERE kd_sales='"+ kd_sales +"' and upl=0";
		 
		 db = dba.getReadableDatabase(); 
		 
		 try {
			
			 dbcursor = dba.SelectData(db,sql);
	    		
	        	adapter = new SimpleCursorAdapter(
	                    this, 
	                    R.layout.pnoo0_view, 
	                    dbcursor,
	                    new String[] {"no_pengajuan","nama_outlet","alamat"}, 
	                    new int[] {R.id.tv_nopengajuanp0,R.id.tv_namap0,R.id.tv_alamatp0});
	            list1.setAdapter(adapter);
			 
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		 
		 
	 }; // akhir dari isi)list
	 
	 private void delete_pengajuan() {
		
		 db = dba.getWritableDatabase();
		 
		 try {
			
			 String sql = "delete from noo where no_pengajuan='"+ nopengajuan +"'";
			 db.execSQL(sql);
			 
			 Toast.makeText(getBaseContext(), "Data telah dihapus...", Toast.LENGTH_LONG).show();
			 
			 isi_list();
			 
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		 
		 
	 };
	 
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
	 
	    
		 @Override
		 protected Dialog onCreateDialog(int id) {
		     AlertDialog.Builder builder = new AlertDialog.Builder(Jpnoo0.this);
		     builder.setTitle(R.string.konf);
		     builder.setItems(R.array.sarr_konfr_noo, new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int which) {
		                // The 'which' argument contains the index position
		                // of the selected item
		                	
		                if (which == 0) {
		                	
		                	Intent myIntent = null;
		                	myIntent = new Intent(getBaseContext(), jpnoo.class);
							
			            	myIntent.putExtra("kd_sales", kd_sales);
			            	myIntent.putExtra("nama_sales", nama_sales);
			            	myIntent.putExtra("nopengajuan", nopengajuan);
		                	
			            	startActivity(myIntent);
			            	
		                }else if (which ==1) {
		                	delete_pengajuan();
		                }else {
		                	dialog.dismiss();
		                }
		                	
		            }
		     });
		     return builder.create();
		 }


	    
}
