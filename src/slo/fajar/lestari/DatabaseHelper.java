package slo.fajar.lestari;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{
	
	static final String ms_user ="ms_user";
	static final String ms_sales ="ms_sales";
	
	public static final String TABLE_COMMENTS = "comments";
	
	private static final String DATABASE_NAME = "mydb";
	private static final int DATABASE_VERSION = 3;
	
	// Database creation sql statement
	  private static final String DATABASE_CREATE = "";	

	  public DatabaseHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(DatabaseHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
	    onCreate(db);
	  }
	  
	  
	  public Cursor getIDUSEr(String usern,String pwd)
	  {
		  
		String sql;
		sql = "select "+ms_user+".kd_sales,"+ms_sales+".nama_sales from "+ms_user+" inner join "+ms_sales+" on "+ms_user+".kd_sales="+ 
				ms_sales+".kd_sales where "+ms_user+".nama_user="+"'"+usern+"'"+" and "+ms_user+".pwd="+"'"+pwd+"'";
				
		
		Log.v("buka user", sql);
		
	   SQLiteDatabase db=this.getReadableDatabase();
	   
	   Log.v("buka user", "get read");
	   
	   Cursor cursor = db.rawQuery(sql, null); 
	   
	   Log.v("buka user", "get from cursor");
	   
	   return cursor;
	   
	  }
	  
}
