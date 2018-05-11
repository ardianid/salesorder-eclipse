package slo.fajar.lestari;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter extends SQLiteOpenHelper {
	
	public static final String TABLE_COMMENTS = "comments";
	 public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_COMMENT = "comment";
	
	private static final String DATABASE_NAME = "mydb";
	private static final int DATABASE_VERSION = 3;
	
	
	// buat tabel ms_user
	  private static final String DATABASE_CREATE = "create table "
		      + "ms_user" + "(" + COLUMN_ID
		      + " integer primary key autoincrement, " + "nama_user"
		      + " text not null,kd_sales text,nama_sales text,pwd text not null);";	
	  
	  // buat tabel noo
	  private static final String create_tab_noo ="CREATE TABLE [noo] ("
			  + "[_id] INTEGER PRIMARY KEY, "
			  + "[no_pengajuan] TEXT, "
			  + "[kd_sales] TEXT, "
			  + "[tanggal] TEXT, "
			  + "[nama_outlet] TEXT, "
			  + "[alamat] TEXT, "
			  + "[no_ktp] TEXT, "
			  + "[nama_pemilik] TEXT, "
			  + "[nama_kontak] TEXT, "
			  + "[tgl_lahir] TEXT, "
			  + "[email] TEXT, "
			  + "[telp1] TEXT, "
			  + "[telp2] TEXT, "
			  + "[no_hp1] TEXT, "
			  + "[no_hp2] TEXT, "
			  + "[group_outl] TEXT, "
			  + "[tipe_outl] TEXT, "
			  + "[jenis_outl] TEXT, "
			  + "[rayon] TEXT, "
			  + "[rute] TEXT, "
			  + "[kd_kelurahan] TEXT, "
			  + "[pasar] TEXT, "
			  + "[lingkungan] TEXT, "
			  + "[jenis_noo] TEXT, "
			  + "[no_rek] TEXT, "
			  + "[atas_nama_rek] TEXT, "
			  + "[bank] TEXT, "
			  + "[npwp] TEXT, "
			  + "[no_npwp_seri] TEXT, "
			  + "[nama_npwp] TEXT, "
			  + "[alamat_npwp] TEXT, "
			  + "[longitude] TEXT, "
			  + "[latitude] TEXT, "
			  + "[step] INTEGER, "
			  + "[upl] INTEGER DEFAULT 0);"; // akhir dari create table noo
	  
	  // buat tabel term
	  private static final String create_tab_term = "CREATE TABLE ms_term (_id INTEGER PRIMARY KEY, kd_customer TEXT, kd_salesman TEXT);";
	  
	  // buat tabel sales
	  private static final String create_tab_sales ="CREATE TABLE ms_sales (_id INTEGER PRIMARY KEY, kd_sales TEXT, nama_sales TEXT);";
	  
	// buat tabel pasar
		  private static final String create_tab_pasar = "CREATE TABLE [ms_pasar] ( "
				  + "[_id] INTEGER PRIMARY KEY, "
				  + "[kd_pasar] TEXT, "
				  + "[nama_pasar] TEXT, "
				  + "[kd_kelurahan] TEXT);";
	  
	// buat tabel other
		  private static final String create_tab_other ="CREATE TABLE ms_other (_id INTEGER PRIMARY KEY, keterangan TEXT, tipe TEXT);";
		  
	// buat tabel group cust
		  private static final String create_tab_groupcust ="CREATE TABLE [ms_groupcust] ( "
				  + "[_id] INTEGER PRIMARY KEY, "
				  + "[kode] TEXT, "
				  + "[nama] TEXT);";
		  
	// buat tabel cust
		  private static final String create_tab_cust ="CREATE TABLE [ms_customer] ("
				  + "  [editd] TEXT, "
				  + "  [upl] INT DEFAULT 0, "
				  + "  [_id] INTEGER PRIMARY KEY, "
				  + "  [lat] TEXT, "
				  + "  [long] TEXT, "
				  + "  [kd_customer] TEXT, "
				  + "  [nama] TEXT, "
				  + "  [alamat] TEXT, "
				  + "  [kontak] TEXT, "
				  + "  [rayon] TEXT, "
				  + "  [diskrit] TEXT, "
				  + " [kd_sales_edit] TEXT, " 
				  + " [diluarjalur] INT DEFAULT 0, " 
				  + " [telp1] TEXT, " 
				  + " [telp2] TEXT, " 
				  + " [hp1] TEXT, " 
				  + " [hp2] TEXT, " 
				  + " [noktp] TEXT);";
		  
	// buat tabel barang
		  private static final String create_tab_barang = "CREATE TABLE ms_barang (_id INTEGER PRIMARY KEY, kd_barang TEXT, nama_barang TEXT, satuan1 TEXT, satuan2 TEXT, satuan3 TEXT, satuan4 TEXT, kd_supplier TEXT, kd_divisi TEXT,harga_kanvas DOUBLE DEFAULT 0);";

	// buat tabel area
		  private static final String create_tab_area =	"CREATE TABLE [ms_area] ("
				  + "[_id] INTEGER PRIMARY KEY, "
				  + "[kd_kelurahan] TEXT, "
				  + "[nama_kelurahan] TEXT, "
				  + "[nama_kec] TEXT, "
				  + "[nama_kab] TEXT);";	  
		
		// buat tabel faktur_h
		  private static final String create_tab_fakturh ="CREATE TABLE [faktur_h] ("
				  + "[jamselesai] TEXT, "
				  + "[_id] INTEGER PRIMARY KEY, "
				  + "[trans] NUMERIC, "
				  + "[lat] TEXT, "
				  + "[long] TEXT, "
				  + "[jam] TEXT, "
				  + "[no_bukti] TEXT, "
				  + "[kd_cust] TEXT, "
				  + "[tanggal] TEXT, "
				  + "[kd_salesman] TEXT, "
				  + "[carabayar] TEXT, "
				  + "[jarak] DOUBLE DEFAULT 0, "
				  + "[jamperubahan] TEXT DEFAULT 0, " 
				  + "[jarakperubahan] DOUBLE DEFAULT 0, " 
				  + "[lat2] TEXT, " 
				  + "[long2] TEXT, " 
				  + "[jamperubahan2] TEXT, "
				  + "[kd_kondisi] [TEXT(4)]);";
		  
		// buat tabel faktur_h
		  private static final String create_tab_fakturd ="CREATE TABLE [faktur_d] ("
				  + "[jam] TEXT, "
				  + "[trans] NUMERIC, "
				  + "[no_bukti_h] TEXT, "
				  + "[no_bukti] INTEGER PRIMARY KEY, "
				  + "[kd_barang] TEXT, "
				  + "[qty1] NUMERIC DEFAULT 0, "
				  + "[qty2] NUMERIC DEFAULT 0, "
				  + "[qty3] NUMERIC DEFAULT 0, "
				  + "[qty4] NUMERIC DEFAULT 0);";
		  
		  
		  // buat tabel kondisi
		  private static final String create_tab_kondisi ="CREATE TABLE [ms_kondisi] ("
				  + "[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " 
				  + "[kode] TEXT, "  
				  + "[keterangan] TEXT, " 
				  + "[jenis] TEXT);";
		
		  
	  public DBAdapter(Context ctx)
	  {
		  super(ctx, DATABASE_NAME, null, DATABASE_VERSION); 
	  }
	  
		  @Override
		  public void onCreate(SQLiteDatabase db) {
			  try {
				  db.execSQL(DATABASE_CREATE);
				  db.execSQL(create_tab_noo);
				  db.execSQL(create_tab_term);
				  db.execSQL(create_tab_sales);
				  db.execSQL(create_tab_pasar);
				  db.execSQL(create_tab_other);
				  db.execSQL(create_tab_groupcust);
				  db.execSQL(create_tab_cust);
				  db.execSQL(create_tab_barang);
				  db.execSQL(create_tab_area);
				  db.execSQL(create_tab_fakturh);
				  db.execSQL(create_tab_fakturd);
				  db.execSQL(create_tab_kondisi);
				  
			  } catch (SQLException e) {
				  e.printStackTrace();
			  }
		  }
		  
		  @Override
		  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		  {
		  Log.w("", "Upgrade database dari versi " + oldVersion + " ke "
		  + newVersion + ", yang akan menghapus semua data lama");
		  db.execSQL("DROP TABLE IF EXISTS kontak");
		  onCreate(db);
		  }
		  
		  public Cursor SelectData(SQLiteDatabase db, String sql){  
		        Cursor cursor = db.rawQuery(sql,null);  
		        return cursor;  
		    }
		  
		  public ArrayList<String> getRecords(String sql) {
			    ArrayList<String> recordList = new ArrayList<String>();
			    String selectQuery = sql;

			    SQLiteDatabase db = this.getWritableDatabase();
			    Cursor cursor = db.rawQuery(selectQuery, null);

			    if (cursor != null) {
			        if (cursor.moveToFirst()) {
			            do {
			                recordList.add(cursor.getString(0));
			            } while (cursor.moveToNext());
			        }
			    }

			    return recordList;
			}
		  
		  public List<String> getAllLabels(String sql){
		        List<String> labels = new ArrayList<String>();
		 
		        // Select All Query
		        String selectQuery = sql;
		 
		        SQLiteDatabase db = this.getReadableDatabase();
		        Cursor cursor = db.rawQuery(selectQuery, null);
		 
		        // looping through all rows and adding to list
		        if (cursor.moveToFirst()) {
		            do {
		                labels.add(cursor.getString(1));
		            } while (cursor.moveToNext());
		        }
		 
		        // closing connection
		        cursor.close();
		        db.close();
		 
		        // returning lables
		        return labels;
		    }
		  
		  
/* Query - Query
	  
	  public Cursor getIDUSEr(String usern,String pwd)
	  {
		  
		String sql;
		sql = "select "+ms_user+".kd_sales,"+ms_sales+".nama_sales from "+ms_user+" inner join "+ms_sales+" on "+ms_user+".kd_sales="+ 
				ms_sales+".kd_sales where "+ms_user+".nama_user="+"'"+usern+"'"+" and "+ms_user+".pwd="+"'"+pwd+"'";
				
		
		Log.v("buka user", sql);
		
	   db= DBHelper.getReadableDatabase();
	   
	   Log.v("buka user", "get read");
	   
	   Cursor cursor = db.rawQuery(sql, null); 
	   
	   if (cursor == null) {
		  cursor = null;
	   }
		   
	   
	   Log.v("buka user", "get from cursor");
	   
	   return cursor;
	   
	  } */
	  
	  
}
