package slo.fajar.lestari;

import java.io.File;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CupdateData extends Activity {
	
	private DBAdapter dba=null;
	private SQLiteDatabase db=null;
	
	Button btbrowse;
	Button btback;
	Button btbrowse2;
	
	TextView st_cust;
	TextView st_groupcust;
	TextView st_barang;
	TextView st_area;
	TextView st_termsales;
	TextView st_pasar;
	TextView st_other;
	TextView statlu;
	TextView st_kondisi;
	TextView st_sales;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.lupdatedata);
        
        
        btbrowse = (Button)findViewById(R.id.btbrowsed2);
        btbrowse2 = (Button)findViewById(R.id.bt_browsedcl);
        btback = (Button)findViewById(R.id.btkembali_sinc);
        
        st_cust = (TextView)findViewById(R.id.tlu_customer);
        st_groupcust = (TextView)findViewById(R.id.tlu_groupcust);
        st_barang = (TextView)findViewById(R.id.tlu_barang);
        st_area = (TextView)findViewById(R.id.tlu_area);
        st_termsales = (TextView)findViewById(R.id.tlu_termsales);
        st_pasar = (TextView)findViewById(R.id.tlu_pasar);
        st_other = (TextView)findViewById(R.id.tlu_other);
        st_sales = (TextView)findViewById(R.id.tlu_sales);
        st_kondisi = (TextView)findViewById(R.id.tlu_kondisi);
        
        statlu = (TextView)findViewById(R.id.tlu_stat);
        
        btback.setOnClickListener(btback_click);
        btbrowse.setOnClickListener(btbrowse_click);
        btbrowse2.setOnClickListener(btbrowse2_click);
        
        st_cust.setText("");
        st_groupcust.setText("");
        st_barang.setText("");
        st_area.setText("");
        st_termsales.setText("");
        st_pasar.setText("");
        st_other.setText("");
        statlu.setText("");
        st_sales.setText("");
        st_kondisi.setText("");
        
        dba = new DBAdapter(this);
        
        
	} // akhir dari on create
	
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
	
	View.OnClickListener btback_click = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			finish();
		}
	}; // akhir dari event back on click
	
	View.OnClickListener btbrowse_click = new View.OnClickListener() {
		
	 @Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
	        
		  proses(1);
	 } 
	}; // akhir onclick browse
	
	
	View.OnClickListener btbrowse2_click = new View.OnClickListener() {
		
		 @Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			  proses(2);
		 } 
		}; // akhir onclick browse2
	
	private void proses(Integer jenis){
		
		 db = dba.getWritableDatabase();
		 
			try {
		     	String pathTujuan = Environment.getExternalStorageDirectory().getPath() + "/Mp";
		     	File f = new File(pathTujuan);
		     		if (!f.exists()) {
		     			Toast.makeText(getBaseContext(), "Data Master tidak ada (Hub admin)", Toast.LENGTH_LONG).show();
		     		}else{
		     			db.execSQL("attach database ? as userdb", new String[]{pathTujuan});
		     			
		     			String sql_delcust = "delete from ms_customer";
		     			String sql_delgroup = "delete from ms_groupcust";
		     			String sql_delbarang = "delete from ms_barang";
		     			String sql_delarea = "delete from ms_area";
		     			String sql_delterm = "delete from ms_term";
		     			String sql_delpasar = "delete from ms_pasar";
		     			String sql_delother = "delete from ms_other";
		     			String sql_delsales = "delete from ms_sales";
		     			String sql_delkondisi ="delete from ms_kondisi";
		     			
		     			String sql_delfaktur_d = "delete from faktur_d";
		     			String sql_delfaktur_h = "delete from faktur_h";
		     			String sql_delnoo = "delete from noo";
		     			
		     			
		     			String sqlcust ="insert into ms_customer (lat, long, kd_customer,"
		     							+ "nama, alamat, kontak,rayon, diskrit,diluarjalur,telp1,telp2,hp1,hp2,noktp) "
		     							+ "select userdb.ms_customer.lat, userdb.ms_customer.long, userdb.ms_customer.kd_customer,"
		     							+ "userdb.ms_customer.nama, userdb.ms_customer.alamat, userdb.ms_customer.kontak,"
		     							+ "userdb.ms_customer.rayon, userdb.ms_customer.diskrit,userdb.ms_customer.diluarjalur,userdb.ms_customer.telp1,userdb.ms_customer.telp2,userdb.ms_customer.hp1,userdb.ms_customer.hp2,userdb.ms_customer.noktp from userdb.ms_customer";	
		     			
		     			String sqlgroup = "INSERT INTO ms_groupcust (kode, nama)"
		     								+ " SELECT gs.kode, gs.nama FROM userdb.ms_groupcust gs";
		     			
		     			String sqlbarang = "INSERT INTO ms_barang (kd_barang, nama_barang,"
		     							+ "satuan1, satuan2, satuan3,"
		     							+ "satuan4, kd_supplier, kd_divisi,harga_kanvas)"
		     							+ " SELECT mb.kd_barang, mb.nama_barang,"
		     							+ "mb.satuan1, mb.satuan2, mb.satuan3,"
		     							+ "mb.satuan4, mb.kd_supplier, mb.kd_divisi,mb.harga_kanvas FROM userdb.ms_barang mb";	
		     			
		     			String sqlarea = "INSERT INTO ms_area (kd_kelurahan, nama_kelurahan,"
		     							+ "nama_kec, nama_kab)"
		     							+ " SELECT ma.kd_kelurahan, ma.nama_kelurahan,"
		     							+ "ma.nama_kec, ma.nama_kab FROM userdb.ms_area ma";
		     			
		     			String sqlterm = "INSERT INTO ms_term (kd_customer, kd_salesman)"
		     							+ " SELECT mt.kd_customer, mt.kd_salesman FROM userdb.ms_term mt";
		     			
		     			String sqlpasar = "INSERT INTO ms_pasar (kd_pasar, nama_pasar,kd_kelurahan)"
		     							+ " SELECT mp.kd_pasar, mp.nama_pasar,mp.kd_kelurahan FROM userdb.ms_pasar mp";
		     			
		     			String sqlother ="INSERT INTO ms_other (keterangan, tipe)"
		     							+ " SELECT mo.keterangan, mo.tipe FROM userdb.ms_other mo";
		     			
		     			String sqlsales ="INSERT INTO ms_sales (kd_sales, nama_sales)"
		     							+ " select ms.kd_sales, ms.nama_sales FROM userdb.ms_sales ms";
		     			
		     			String sqlkondisi ="INSERT INTO ms_kondisi (kode,keterangan,jenis)"
		     							+ " select ms.kode,ms.keterangan,ms.jenis FROM userdb.ms_kondisi ms";
		     			
		     			statlu.setText("Hapus Data Master....");
		     			
		     			db.execSQL(sql_delcust);
		     			db.execSQL(sql_delgroup);
		     			db.execSQL(sql_delbarang);
		     			db.execSQL(sql_delarea);
		     			db.execSQL(sql_delterm);
		     			db.execSQL(sql_delpasar);
		     			db.execSQL(sql_delother);
		     			db.execSQL(sql_delsales);
		     			db.execSQL(sql_delkondisi);
		     			
		     			if (!jenis.equals(1)){
		     				
		     				statlu.setText("Hapus Data Transaksi....");
		     				
		     				db.execSQL(sql_delfaktur_d);
		     				db.execSQL(sql_delfaktur_h);
		     				db.execSQL(sql_delnoo);
		     			}
		     			
		     			statlu.setText("Update Data Master....");
		     			
		     			db.execSQL(sqlcust);
		     			st_cust.setText("OK");
		     			
		     			db.execSQL(sqlgroup);
		     			st_groupcust.setText("OK");
		     			
		     			db.execSQL(sqlbarang);
		     			st_barang.setText("OK");
		     			
		     			db.execSQL(sqlarea);
		     			st_area.setText("OK");
		     			
		     			db.execSQL(sqlterm);
		     			st_termsales.setText("OK");
		     			
		     			db.execSQL(sqlpasar);
		     			st_pasar.setText("OK");
		     			
		     			db.execSQL(sqlother);
		     			st_other.setText("OK");
		     			
		     			db.execSQL(sqlkondisi);
		     			st_kondisi.setText("OK");
		     			
		     			db.execSQL(sqlsales);
		     			st_sales.setText("OK");
		     			
		     			statlu.setText("Update Data Selesai.....");
		     			
		     			File file = new File(pathTujuan);
		     			boolean deleted = file.delete();
		     			
		     			if (deleted){
		     				Toast.makeText(getBaseContext(),"Hapus File Ok...", Toast.LENGTH_SHORT).show();
		     			}else{
		     				Toast.makeText(getBaseContext(),"Hapus File Bermasalah", Toast.LENGTH_SHORT).show();
		     			}
		     			
		     			btbrowse.setEnabled(false);
		     			btbrowse2.setEnabled(false);
		     			
		     			
		     		}
		     } catch (Exception e) {
		    	 //Log.v("error upd", e.toString());
		     	Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
		     } 
			
			db.close();
			
		
	}
	
}
