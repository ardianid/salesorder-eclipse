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
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Jupload_fakt extends Activity {
	
	private DBAdapter dba=null;
	private SQLiteDatabase db=null;
	private Cursor dbcursor;
	// protected SimpleCursorAdapter  adapter;
	
	private static String url= "addorder.php";
	private static String url2= "addorder2.php";
	private static String urlFinish= "orderfinish.php";
	
	MyCustomAdapter dataAdapter = null;
	
	TextView tsales;
	CheckBox cekstat;
	ListView list1;
	
	Button btupload;
	Button btclose;
	
	String kd_sales;
	String nama_sales;
	
	String nobukti;
	String kd_barang;
	
	int jmler=0;
	int jmlok=0;
	
	int stathead=0;
	int statdet=0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {                           
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_faktur);
        
        tsales =(TextView)findViewById(R.id.tsales_upl_order);
        cekstat = (CheckBox)findViewById(R.id.cekupl_order);
        
        list1 = (ListView)findViewById(R.id.list_uplorder);
        
        btupload = (Button)findViewById(R.id.btkirim_uplorder);
        btclose = (Button)findViewById(R.id.btclose_uplorder);
        
        dba = new DBAdapter(this);
        db = dba.getWritableDatabase();
        
        /* String sql="update faktur_h set trans=0";
		db.execSQL(sql);
        
		String sql2="update faktur_d set trans=0";
		db.execSQL(sql2); */ 
		
        Bundle bnd= getIntent().getExtras();
        
        kd_sales = bnd.getString("kd_sales");
        nama_sales = bnd.getString("nama_sales");
        
        tsales.setText(nama_sales);
        
        btclose.setOnClickListener(btclose_list);
        
        cekstat.setOnCheckedChangeListener(cek_uncheck_list);
        
        btupload.setOnClickListener(btsimpan_list);
        
	} // akhir dari on create
	
	
	View.OnClickListener btclose_list = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};
	
	OnCheckedChangeListener cek_uncheck_list = new OnCheckedChangeListener() {
		
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) {
				isi_all(false, true);
				buttonView.setChecked(true);
			}else{
				isi_all(false, false);
				buttonView.setChecked(false);
			}
			
		}
	};
	
	View.OnClickListener btsimpan_list = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			new upload_sync().execute();
		}
	};
	
	@Override
	protected void onResume(){
		super.onResume();
		isi_all(true,false);
	}
	
	@Override
	protected void onPause(){
		super.onPause();
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		db.close();
		dbcursor.close();
	}
	
	private void simpan(){
		
		ArrayList<Cfaktur> cfakturList = dataAdapter.cfakturlist;
	    for(int i=0;i<cfakturList.size();i++){
	    	Cfaktur cfaktur = cfakturList.get(i);
	    	
	    	try {
				
	    		if (cfaktur.isSelected()){
	    			
	    			upload_header(cfaktur.getNopengajuan());
	    			
	    			try {
	    			if (stathead== 1 && statdet==1 ){
	    				
	    			//	Log.v("status", "sampe mau masuk detail");
	    				
	    				ArrayList<NameValuePair> postparam = new ArrayList<NameValuePair>();
						postparam.add(new BasicNameValuePair("no_bukti", cfaktur.getNopengajuan()));
	    				
						String res = CustomHttpClient.executeHttpPost(urlFinish, postparam);
						
						if (res.toString().trim().equals("1")){
							jmlok++;
							
							updatestat("header", cfaktur.getNopengajuan(), "");
							
						}else{
							jmler++;
						}
						
	    			}else{
	    				jmler++;
	    			}
	    			
	    			}catch (Exception e) {
						// TODO: handle exception
	    				jmler++;
	    				Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
					}
	    				
	    			
	    			
	    		}
	    		
	    		
			} catch (InterruptedException e) {
				// TODO: handle exception
				jmler++;
				Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
			}
	    	
	    }
		
	}
	
	private void upload_header(String nopengajuan) throws InterruptedException {
		
		String kdcust;
		String kdsalesman;
		String crbayar;
		String latt;
		String longg;
		String jamm;
		String tgal;
		String jmslesai;
		String jarak;
		
		String jamperubahan;
		String jamperubahan2;
		String jarakperubahan;
		String lat2;
		String long2;
		String kdkondisi;
		String jeniskondisi;
		
		Cursor cdetail = null;
		try {
			
			String sql_d="SELECT " +
					"faktur_h.jamselesai, "+
					"faktur_h._id, "+
					"faktur_h.trans, "+
					"faktur_h.lat, "+
					"faktur_h.long, "+
					"faktur_h.jam, "+
					"faktur_h.no_bukti, "+
					"faktur_h.kd_cust, "+
					"faktur_h.tanggal, "+
					"faktur_h.kd_salesman, "+
					"faktur_h.carabayar, "+
					"faktur_h.jarak, "+
					"faktur_h.jamperubahan, "+
					"faktur_h.jarakperubahan, "+
					"faktur_h.lat2, "+
					"faktur_h.long2, "+
					"faktur_h.jamperubahan2, "+
					"faktur_h.kd_kondisi, "+
					"ms_kondisi.jenis "+
					"FROM "+
					"faktur_h "+
					"INNER JOIN ms_kondisi ON faktur_h.kd_kondisi = ms_kondisi.kode where faktur_h.trans=0 and faktur_h.no_bukti='"+ nopengajuan +"'";
			cdetail = dba.SelectData(db, sql_d);
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
			cdetail.moveToFirst();
			if (cdetail.getCount() > 0) { 
				
				try {
					
					nobukti = cdetail.getString(cdetail.getColumnIndex("no_bukti"));
					kd_barang="";
					
					kdcust = cdetail.getString(cdetail.getColumnIndex("kd_cust"));
					kdsalesman = cdetail.getString(cdetail.getColumnIndex("kd_salesman"));
					crbayar = cdetail.getString(cdetail.getColumnIndex("carabayar"));
					latt = cdetail.getString(cdetail.getColumnIndex("lat"));
					longg	 = cdetail.getString(cdetail.getColumnIndex("long"));
					jamm = cdetail.getString(cdetail.getColumnIndex("jam"));
					tgal = cdetail.getString(cdetail.getColumnIndex("tanggal"));
					jmslesai = cdetail.getString(cdetail.getColumnIndex("jamselesai"));
					jarak = cdetail.getString(cdetail.getColumnIndex("jarak"));
					
					jamperubahan = cdetail.getString(cdetail.getColumnIndex("jamperubahan"));
					jamperubahan2 = cdetail.getString(cdetail.getColumnIndex("jamperubahan2"));
					jarakperubahan = cdetail.getString(cdetail.getColumnIndex("jarakperubahan"));
					lat2 = cdetail.getString(cdetail.getColumnIndex("lat2"));
					long2 = cdetail.getString(cdetail.getColumnIndex("long2"));
					kdkondisi = cdetail.getString(cdetail.getColumnIndex("kd_kondisi"));
					jeniskondisi = cdetail.getString(cdetail.getColumnIndex("jenis"));
					
					
					ArrayList<NameValuePair> postparam = new ArrayList<NameValuePair>();
					postparam.add(new BasicNameValuePair("nobukti", nobukti));
					postparam.add(new BasicNameValuePair("kd_cust", kdcust));
					postparam.add(new BasicNameValuePair("kd_salesman", kdsalesman));
					postparam.add(new BasicNameValuePair("cara_bayar", crbayar));
					postparam.add(new BasicNameValuePair("lat", latt));
					postparam.add(new BasicNameValuePair("long", longg));
					postparam.add(new BasicNameValuePair("jam", jamm));
					
					
					String rtgal="";
					if (tgal.length()>=8){
						
						rtgal = parseTOdate(tgal);
						
					}
					
				//	Log.v("insert", nobukti+","+kdcust+","+kdsalesman+","+crbayar+","+latt+","+longg+","+jamm+","+rtgal+","+jmslesai+","+jarak+","+jamperubahan+","+jamperubahan2+","+jarakperubahan+","+lat2+","+long2);
					
					postparam.add(new BasicNameValuePair("tanggal", rtgal));
					postparam.add(new BasicNameValuePair("jamselesai", jmslesai));
					postparam.add(new BasicNameValuePair("jarak", jarak));
					postparam.add(new BasicNameValuePair("jamperubahan", jamperubahan));
					postparam.add(new BasicNameValuePair("jamperubahan2", jamperubahan2));
					postparam.add(new BasicNameValuePair("jarakperubahan", jarakperubahan));
					postparam.add(new BasicNameValuePair("lat2", lat2));
					postparam.add(new BasicNameValuePair("long2", long2));
					postparam.add(new BasicNameValuePair("kd_kondisi", kdkondisi));
					
					String res = CustomHttpClient.executeHttpPost(url, postparam);
					
					if (res.toString().trim().equals("1")){
						 stathead=1;
						 
						 if (!jeniskondisi.equals("SESUAI RUTE") && !jeniskondisi.equals("MANUAL") ){
							 statdet=1;
						 }else{
							 upload_detail(nopengajuan);
						 }
						 
						 
					 }else {
						 stathead=0;
					 }
					
					
				}catch (Exception e) {
					// TODO: handle exception
					
					stathead =0;
					statdet=0;
					
					 e.printStackTrace();
					 Log.v("err head", e.toString());
					
				}
				
			}else {
				stathead=0;
				statdet=0;
			}
			
			cdetail.close();
		
	} // akhir dari upload_header
	
	private void upload_detail(String nopengajuan) throws InterruptedException {
		
		String qty1;
		String qty2;
		String qty3;
		String qty4;
		String jam;
		
		Cursor cdetail = null;
		String sql_d="select * from faktur_d where trans=0 and no_bukti_h='"+ nopengajuan +"'";
		
			try {
				cdetail = dba.SelectData(db, sql_d);
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			int i=0;
			
			while (cdetail.moveToNext()){
				
				try {
					
					nobukti = cdetail.getString(cdetail.getColumnIndex("no_bukti_h"));
					kd_barang = cdetail.getString(cdetail.getColumnIndex("kd_barang"));
					
					qty1 = cdetail.getString(cdetail.getColumnIndex("qty1"));
					qty2 = cdetail.getString(cdetail.getColumnIndex("qty2"));
					qty3 = cdetail.getString(cdetail.getColumnIndex("qty3"));
					qty4 = cdetail.getString(cdetail.getColumnIndex("qty4"));
					
					jam = cdetail.getString(cdetail.getColumnIndex("jam"));
					
					ArrayList<NameValuePair> postparam = new ArrayList<NameValuePair>();
					postparam.add(new BasicNameValuePair("no_bukti_h", nobukti));
					postparam.add(new BasicNameValuePair("kd_barang", kd_barang));
					postparam.add(new BasicNameValuePair("qty1", qty1));
					postparam.add(new BasicNameValuePair("qty2", qty2));
					postparam.add(new BasicNameValuePair("qty3", qty3));
					postparam.add(new BasicNameValuePair("qty4", qty4));
					postparam.add(new BasicNameValuePair("jam", jam));
					
					String res = CustomHttpClient.executeHttpPost(url2, postparam);
					 
					 if (res.toString().trim().equals("1")){
						
						 updatestat("detail", nobukti, kd_barang);
						 
						if (statdet==0) {
							if (i==0){
								statdet=1;
							}else {statdet=0;}
						}else{
							statdet=1;
						}
						
						 
					 }else {
						 statdet=0;
					 }
					
					

				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					statdet=0;
				}
				
				i++;
				
			}
			
			
			if (cdetail.getCount() <=0) {
				statdet=1;
			}
			
			cdetail.close();
			
		
	} // akhir dari upload_detail
	
	private void updatestat(String stattrans,String nobukti,String kodebarang) {
		
		if (stattrans.equals("detail")) {
			String sql="update faktur_d set trans=1 where no_bukti_h='"+ nobukti +"' and kd_barang='"+ kodebarang +"'";
			db.execSQL(sql);
		}else{
			String sql="update faktur_h set trans=1 where no_bukti='"+ nobukti +"'";
			db.execSQL(sql);
		}
		
		
	}
	
	private void messageDialog(String message){
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Alert Dialog");
		alertDialog.setMessage(message);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		        }
		});
		alertDialog.show();
	} // akhir dari messagedialog
	
	private void isi_all(Boolean refreshfromdb,Boolean isChecked){
		
		try {
			
			if (refreshfromdb == true){
			
			String sql="SELECT faktur_h._id,faktur_h.no_bukti, ms_customer.nama,faktur_h.tanggal, sum(CASE when faktur_d.qty1 is NULL then 0 else faktur_d.qty1 END) as qty1,"+
					"sum(CASE when faktur_d.qty2 is NULL then 0 else faktur_d.qty2 END) as qty2, sum(CASE when faktur_d.qty3 is NULL then 0 else faktur_d.qty3 END) as qty3,substr(faktur_h.carabayar,1,1) as carabayar"+
					" FROM faktur_h LEFT JOIN"+
					" faktur_d ON faktur_h.no_bukti = faktur_d.no_bukti_h INNER JOIN"+
					" ms_customer on faktur_h.kd_cust=ms_customer.kd_customer"+
					" where faktur_h.kd_salesman='"+ kd_sales  +"' and faktur_h.trans=0"+
					" group by faktur_h.no_bukti, ms_customer.nama,faktur_h.tanggal,faktur_h.carabayar";
			
			db = dba.getReadableDatabase();
			
			dbcursor = dba.SelectData(db,sql);
    		
			}else{
				dbcursor.requery();
			}
			
			String nopengajuan=null;
			String tgl=null;
			String nama=null;
			String carabayar=null;
			Integer qty1=0;
			Integer qty2=0;
			Integer qty3=0;
			
			ArrayList<Cfaktur> cfakturlist= new ArrayList<Jupload_fakt.Cfaktur>();
			
			while (dbcursor.moveToNext()){
				
				nopengajuan = dbcursor.getString(dbcursor.getColumnIndex("no_bukti"));
				tgl = dbcursor.getString(dbcursor.getColumnIndex("tanggal"));
				nama = dbcursor.getString(dbcursor.getColumnIndex("nama"));
				carabayar = dbcursor.getString(dbcursor.getColumnIndex("carabayar"));
				
				qty1 = Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndex("qty1")));
				qty2 = Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndex("qty2")));
				qty3 = Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndex("qty3")));
				
				Cfaktur cfaktur = new Cfaktur(nopengajuan,tgl,nama,qty1,qty2,qty3,carabayar,isChecked);
				cfakturlist.add(cfaktur);
			}
			
			
			  //create an ArrayAdaptar from the String Array
			  dataAdapter = new MyCustomAdapter(this,
			    R.layout.upload_faktur, cfakturlist);
			//  ListView listView = (ListView) findViewById(R.id.listView1);
			  // Assign adapter to ListView
			  list1.setAdapter(dataAdapter);
			
        	/* adapter = new SimpleCursorAdapter(
                    this, 
                    R.layout.upload_faktur0, 
                    dbcursor,
                    new String[] {"no_bukti","tanggal","nama","qty1","qty2","qty3","cek"}, 
                    new int[] {R.id.tv_nopeng_upl,R.id.tv_tgl_upl,R.id.tv_namacust_upl,R.id.tjml1_ord_upl,R.id.tjml_ord2_upl,R.id.tjml_ord3_upl,R.id.tv_checkupl});
        	
            list1.setAdapter(adapter); */ 
			
            
            
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		
		
	}// akhir dari isi all
	
	 private class MyCustomAdapter extends ArrayAdapter<Cfaktur> {
		 
		  private ArrayList<Cfaktur> cfakturlist;
		  
		  
		  public MyCustomAdapter(Context context, int textViewResourceId, 
		    ArrayList<Cfaktur> cfakturList) {
		   super(context, textViewResourceId, cfakturList);
		   this.cfakturlist = new ArrayList<Cfaktur>();
		   this.cfakturlist.addAll(cfakturList);
		  }
		 
		  private class ViewHolder {
		   TextView tNopengajuan;
		   TextView tTgl;
		   TextView tNama;
		   TextView tQty1;
		   TextView tQty2;
		   TextView tQty3;
		   TextView tcarabayar;
		   CheckBox stat;
		  }
		 
		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
		 
		   ViewHolder holder = null;
		 //  Log.v("ConvertView", String.valueOf(position));
		 
		   if (convertView == null) {
		   LayoutInflater vi = (LayoutInflater)getSystemService(
		     Context.LAYOUT_INFLATER_SERVICE);
		   convertView = vi.inflate(R.layout.upload_faktur0, null);
		 
		   holder = new ViewHolder();
		   holder.tNopengajuan = (TextView) convertView.findViewById(R.id.tv_nopeng_upl);
		   holder.tTgl = (TextView) convertView.findViewById(R.id.tv_tgl_upl);
		   holder.tNama = (TextView) convertView.findViewById(R.id.tv_namacust_upl);
		   holder.tQty1 = (TextView) convertView.findViewById(R.id.tjml1_ord_upl);
		   holder.tQty2 = (TextView) convertView.findViewById(R.id.tjml_ord2_upl);
		   holder.tQty3 = (TextView) convertView.findViewById(R.id.tjml_ord3_upl);
		   holder.tcarabayar = (TextView) convertView.findViewById(R.id.tv_carabayar_upl);
		   holder.stat = (CheckBox)convertView.findViewById(R.id.tv_checkupl);
		   convertView.setTag(holder);
		 
		    holder.stat.setOnClickListener( new View.OnClickListener() {  
		     public void onClick(View v) {  
		      CheckBox cb = (CheckBox) v ;  
		      Cfaktur cfaktur = (Cfaktur) cb.getTag();  
		     // Toast.makeText(getApplicationContext(),
		      // "Clicked on Checkbox: " + cb.getText() +
		      // " is " + cb.isChecked(), 
		      // Toast.LENGTH_LONG).show();
		      cfaktur.setSelected(cb.isChecked());
		     }  
		    });  
		   } 
		   else {
		    holder = (ViewHolder) convertView.getTag();
		   }
		 
		   Cfaktur cfaktur = cfakturlist.get(position);
		   holder.tNopengajuan.setText(cfaktur.getNopengajuan());
		   holder.tNama.setText(cfaktur.getNama());
		   holder.tTgl.setText(cfaktur.getTanggal());
		   holder.tQty1.setText(cfaktur.getQty1().toString());
		   holder.tQty2.setText(cfaktur.getQty2().toString());
		   holder.tQty3.setText(cfaktur.getQty3().toString());
		   holder.tcarabayar.setText(cfaktur.getCarabayar());
		   
		   holder.stat.setChecked(cfaktur.isSelected());
		   holder.stat.setTag(cfaktur);
		   
		   return convertView;
		 
		  }
		 
		 } // akhir dari MyCustomAdapter
	
	public class Cfaktur {
		  
		 String nopengajuan = null;
		 String tanggal = null;
		 String nama=null;
		 Integer qty1=0;
		 Integer qty2=0;
		 Integer qty3=0;
		 String carabayar=null;
		 boolean selected = false;
		  
		 public Cfaktur(String nopengajuan, String tanggal,String nama,Integer qty1,Integer qty2,Integer qty3,String carabayar, boolean selected) {
		  super();
		  this.nopengajuan = nopengajuan;
		  this.tanggal =tanggal;
		  this.nama = nama;
		  this.qty1 = qty1;
		  this.qty2 = qty2;
		  this.qty3 = qty3;
		  this.carabayar= carabayar;
		  this.selected = selected;
		 }
		  
		 public String getNopengajuan() {
		  return nopengajuan;
		 }
		 public void setNopengajuan(String nopengajuan) {
		  this.nopengajuan = nopengajuan;
		 }
		 
		 public String getTanggal() {
		  return tanggal;
		 }
		 public void setTanggal(String tanggal) {
		  this.tanggal = tanggal;
		 }
		 
		 public String getNama() {
			  return nama;
		 }
		 public void setNama(String nama) {
			  this.nama = nama;
		 }
		 
		 public Integer getQty1() {
			  return qty1;
		 }
		 public void setQty1(Integer qty1) {
			  this.qty1 = qty1;
		 }
		 
		 public Integer getQty2() {
			  return qty2;
		 }
		 public void setQty2(Integer qty2) {
			  this.qty2 = qty2;
		 }		 
		 
		 public Integer getQty3() {
			  return qty3;
		 }
		 public void setQty3(Integer qty3) {
			  this.qty3 = qty3;
		 }		 
		 
		 public String getCarabayar() {
			  return carabayar;
		 }
		 public void setCarabayar(String carabayString) {
			  this.carabayar = carabayString;
		 }	
		 
		 public boolean isSelected() {
		  return selected;
		 }
		 public void setSelected(boolean selected) {
		  this.selected = selected;
		 }
		  
		} // akhir dari class Cfaktur
	
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
	
    private class upload_sync extends AsyncTask<Void,Void, String>{
    	
    	private ProgressDialog pDialog;
    	
    	@Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Jupload_fakt.this);
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
            
            jmler =0;
            jmlok=0;
            
            isi_all(true, false);
            
        }
    	
		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
			try {
				simpan();
			} catch (Exception e) {
				// TODO: handle exception
				Log.v("error upl noo", e.toString());
			}
			
			
			return null;
		}
    	
		
    } // akhir dari asyncTask
	
	
}
