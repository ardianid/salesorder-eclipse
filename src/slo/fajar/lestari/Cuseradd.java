package slo.fajar.lestari;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Cuseradd extends Activity {
	
	private DBAdapter dba=null;
	private SQLiteDatabase db=null;
	private Cursor dbcursor;
	protected ListAdapter  adapter;
	
	ListView list1;
	Button btadd;
	Button btback;
	Button btadd_viaserver;
	
	String nobukti_d;
	
	EditText tuser;
	EditText tpwd;
	Spinner spsales;
	
	String[] kdsales;
	String[] nmsales;
	String kdsales1;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.luser);
        
        btadd = (Button)findViewById(R.id.btrubahpwd);
        btadd_viaserver = (Button)findViewById(R.id.btadd_viaserv);
        btback = (Button)findViewById(R.id.btcancelpwd);
        list1 = (ListView)findViewById(R.id.listuseradd);
        
        dba = new DBAdapter(this);
        
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
        // akhir dari bulder
    	
    	
    	list1.setOnItemClickListener(new OnItemClickListener() {
    		
    		public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
    			
    				
    					nobukti_d=dbcursor.getString(dbcursor.getColumnIndex("nama_user"));
    				
    				if (nobukti_d.trim().length() >0 ){
    						
    					builder.show();

    				}		
    		}}); // akhir dari on item click
    	
    	
    	btback.setOnClickListener(btbcack_list);
    	btadd.setOnClickListener(btadd_list);
    	btadd_viaserver.setOnClickListener(btaddviaserv_list);
    	
    	
	} // akhir dari on-create
	
	View.OnClickListener btaddviaserv_list = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent myIntent;
			myIntent = new Intent(getBaseContext(), Cuseraddbykod.class);
			startActivity(myIntent); 
		}
	};
	
	
	View.OnClickListener btadd_list = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			showDialog(1);
		}
	};
	
	
	View.OnClickListener btbcack_list = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
	}; // akhir listener btback_list
	
	
	@Override
    protected void onResume(){
    	super.onResume();
    	 isi_user();
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
	
	private void isi_user(){
		
		String sql="select _id,nama_user from ms_user where not(nama_user='admin')";
		
		db = dba.getReadableDatabase();
		
		try {
			
			dbcursor = dba.SelectData(db,sql);
			
			adapter = new SimpleCursorAdapter(
                    this, 
                    R.layout.luser2, 
                    dbcursor,
                    new String[] {"nama_user"}, 
                    new int[] {R.id.tuser_detail});
            list1.setAdapter(adapter); 
			
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		
		
		
	} // akhir isi user
	
	private void delete_item_Onlist(String namauser){
		
		db = dba.getWritableDatabase(); 
		
		try{
			
			String sql="delete from ms_user where nama_user='"+namauser+"'";
			
			db.execSQL(sql);
			
			dbcursor.requery();
			
			
		}catch(Exception e){
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		
	};
	
	@Override
	 protected Dialog onCreateDialog(int id) {
	 
	  AlertDialog dialogDetails = null;
	 
	  switch (id) {
	  case 1:
	   LayoutInflater inflater = LayoutInflater.from(this);
	   View dialogview = inflater.inflate(R.layout.luser3, null);
	 
	   AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
	   dialogbuilder.setTitle("Add User");	
	   dialogbuilder.setView(dialogview);
	   dialogDetails = dialogbuilder.create();
	 
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
			     .findViewById(R.id.btcanceladd);
	   
	   Button okbutton = (Button) alertDialog
			     .findViewById(R.id.btsimpanadd);
	   
	   tuser =(EditText)dialog.findViewById(R.id.tuser_rbh);
       tpwd =(EditText)dialog.findViewById(R.id.tpwd_rbh1);
       spsales =(Spinner)dialog.findViewById(R.id.spinadd);
	   
	   try {
		
		   getAllData();
		  // Set<String> set = getAllData();
		  // List<String> list = new ArrayList<String>(set);
		  // ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
	       //         android.R.layout.simple_spinner_item, list);
		   
		//   dataAdapter
         //  .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		   
		  // spsales.setAdapter(dataAdapter);
		 
	} catch (Exception e) {
		// TODO: handle exception
		Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
	}
	   
	   
	  spsales.setOnItemSelectedListener(new OnItemSelectedListener() {
		  
		  @Override
		  public void onItemSelected(AdapterView<?> parent, View v, int position,long id) {
			  Toast.makeText(getApplicationContext(), kdsales[position],Toast.LENGTH_SHORT).show();
			  kdsales1 = kdsales[position];
			  
		  }

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}  
	});
	  
	  okbutton.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			if (tuser.getText().toString().length() <1 ){
				Toast.makeText(getApplicationContext(), "User harus diisi",Toast.LENGTH_SHORT).show();
				tuser.requestFocus();
				return;
			}
			
			if (kdsales1 ==""){
				Toast.makeText(getApplicationContext(), "Sales harus diisi",Toast.LENGTH_SHORT).show();
				spsales.requestFocus();
				return;
			}
			
			if (tpwd.getText().toString().length() <1 ){
				Toast.makeText(getApplicationContext(), "Password harus diisi",Toast.LENGTH_SHORT).show();
				tpwd.requestFocus();
				return;
			}
			
			try {
				
				db = dba.getWritableDatabase();
				
				String sql2="select nama_user from ms_user where nama_user='"+ tuser.getText().toString() +"'";
				Cursor cursor2 = dba.SelectData(db,sql2);
				cursor2.moveToFirst(); 
				
				int jml_baris = cursor2.getCount();
		        
		        if(jml_baris > 0)  {
		        	
		        	String namaus= cursor2.getString(0);
		        	
		        	if (namaus.length() < 1){
		        		add_user(tuser.getText().toString(), tpwd.getText().toString());
		        		
		        		db.close();
						alertDialog.dismiss();
						
						isi_user();
		        		
		        	}else {
		        		Toast.makeText(getBaseContext(), "User sudah ada silahkan rubah", Toast.LENGTH_LONG).show();
		        		tuser.requestFocus();
		        		db.close();
		        		return;
		        	}
		        		
		        	
		        }else{
		        	add_user(tuser.getText().toString(), tpwd.getText().toString());
		        	
		        	db.close();
					alertDialog.dismiss();
					
					isi_user();
		        	
		        }
				
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
			}
			
			
			
			
		}
	});
	  
	  cancelbutton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}});
		   
		   break;
	   
	  }} // akhir dari on-prepare
	
	
	private void add_user(String namauser,String pwd){
		
		String sql="insert into ms_user (nama_user,kd_sales,nama_sales,pwd) values('"+ namauser + "','"+ kdsales1.trim() +"','"+ spsales.getSelectedItem().toString().trim() +"','"+ pwd +"')";
		db.execSQL(sql);
		
		
	}
	
	public void getAllData() {
		
			db = dba.getReadableDatabase(); 
		
		//  Set<String> set = new HashSet<String>();
		  String selectQuery = "select _id,kd_sales,nama_sales from ms_sales order by nama_sales";
		  
		  Cursor cursor = db.rawQuery(selectQuery, null);
		  
		  kdsales = new String[cursor.getCount()];
		  nmsales = new String[cursor.getCount()];
		  Integer i=0;
		  
		  if (cursor.moveToFirst()) {
		   do {
			   
			   nmsales[i]=cursor.getString(2);
			   kdsales[i]= cursor.getString(1);
			   
			  // Log.v(Integer.toString(i), cursor.getString(1) + " " + cursor.getString(2));
			   
			i++;
			
		   } while (cursor.moveToNext());
		  }
		  cursor.close();
		  db.close();
		  
		  ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,nmsales);
		  
		   dataAdapter
	           .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		  
		  spsales.setAdapter(dataAdapter);
		  
		 } // akhir dari on set data
	
		}
