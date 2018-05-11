<?
$json = $_SERVER['HTTP_JSON'];
$data = json_decode($json);
$status = $data->status;

switch($status){
	case "noo" :
		insert_noo($data);
	break;
	case "updateoutl" :
		updatecustomer($data);
	break;
	case "order" :
		order($data);
	break;
	case "order_detail" :
		order_detail($data);
	break;
	case "test" :
		test_aja($data);
	break;
}

function database($sql){

//	if (function_exists('sqlsrv_connect')) {

	$server = '(local)\SQL2008';
	$conn = array("UID"=>"sa",
				"PWD"=>"pegasus",
				"Database"=>"SLO");

	$koneksi=sqlsrv_connect($server,$conn) or die ('koneksi error');
	$data = sqlsrv_query($koneksi,$sql);
	sqlsrv_close($koneksi);
	
	return $data;
	
//	if (! $koneksi) {
//		die (sqlsrv_errors());
//		}

//	} else {
//		echo "Connection functions are not available.<br />\n";
//	}

//	$koneksi = mysql_connect('localhost', 'root','') or die ('koneksi error');
//	mysql_select_db('coba', $koneksi) or die('database error');
//	$data = mysql_query($sql);
//	mysql_close($koneksi);
//	return $data;

} // akhir dari database

function test_aja($data) {
	$no=$data->no;
	
	$sql="insert into test (no) values('$no')";
	$hasil = database($sql);
	
	if ($hasil) {echo "1"; }else{echo "0"; }
	
	
}

function order_detail($data) {
	$no_bukti=$data->no_bukti_h;
	$kd_barang=$data->kd_barang;
	$qty1=$data->qty1;
	$qty2=$data->qty2;
	$qty3=$data->qty3;
	$qty4=$data->qty4;
	$jam=$data->jam;

	$sql_select="select no_bukti_h from faktur_d where no_bukti_h='$no_bukti' and kd_barang='$kd_barang'";
	$temp=sqlsrv_fetch_array(database($sql_select));

	if ($temp[0]['no_bukti'] !='') {

		$sql_up="update faktur_d set qty1=$qty1,qty2=$qty2,qty3=$qty3,qty4=$qty4,jam='$jam' where
			kd_barang='$kd_barang' and no_bukti_h='$no_bukti'";
	
		$hasil = database($sql_up);
	
	}else{

		$sql="insert into faktur_d (trans,no_bukti_h,kd_barang,qty1,qty2,qty3,qty4,jam) values(
			0,'$no_bukti','$kd_barang',$qty1,$qty2,$qty3,$qty4,'$jam')";
			
		$hasil = database($sql);

	} // akhir dari if
	
	if( $hasil ==1 ) { echo "1"; }else{ echo "0"; }

} // akhir dari order detail

function order($data) {
	$nobukti=$data->nobukti;
	$kd_cust=$data->kd_cust;
	$kd_salesman=$data->kd_salesman;
	$cara_bayar=$data->cara_bayar;
	$lat=$data->lat;
	$long=$data->long;
	$jam=$data->jam;
	$tanggal=$data->tanggal;
	$jamselesai=$data->jamselesai;
	
	/*$tanggal_t='';
	if (strlen($tanggal) != 0) {
		$tanggal_t=date_create($tanggal);
		$tanggal_t= date_format($tanggal_t,'Y-m-d H:i:s');
	}*/
	
	$sql="select no_bukti from faktur_h where no_bukti='$nobukti'";
	$temp=sqlsrv_fetch_array(database($sql));

	if ($temp[0]['no_bukti'] !='') {

		$sqlup="update faktur_h set kd_cust='$kd_cust',kd_salesman='$kd_salesman',cara_bayar='$cara_bayar',lat='$lat',long='$long',jam='$jam'
			,tanggal='$tanggal',jamselesai='$jamselesai' where no_bukti='$nobukti'";

			$hasil = database($sqlup);
			if( $hasil ==1 ) { echo "1"; }else{ echo "0"; }
	
		 } else {
	
		$sql_ins="insert into faktur_h (no_bukti,kd_cust,kd_salesman,carabayar,lat,long,jam,tanggal,jamselesai) values(
			'$nobukti','$kd_cust','$kd_salesman','$cara_bayar','$lat','$long','$jam','$tanggal','$jamselesai')";
		
			$hasil = database($sql_ins);
			if( $hasil ==1 ) { echo "1"; }else{ echo "0"; }
		} // akhir dari if

} // akhir dari order

function updatecustomer($data) {
	$kd_cust=$data->kd_cust;
	$lat=$data->lat;
	$long=$data->long;
	$kd_sales=$data->kd_sales;

	$sql="select kd_customer from ms_customer where kd_customer='$kd_cust'";
	$temp=sqlsrv_fetch_array(database($sql));

	if ($temp[0]['kd_customer'] !='') {
		echo '2';
	}else{
		$sql_up="update ms_customer set lat='$lat',long='$long',kd_sales_edit='$kd_sales' where kd_customer='$kd_cust'";
		$hasil = database($sql);
			if( $hasil ==1 ) { echo "1"; }else{ echo "0"; }
	} // akhir dari if

} // akhir dari update cust

function insert_noo($data) {
	$nopengajuan = $data->nopengajuan;
	$kd_sales=$data->kd_sales;
	$jenisnoo = $data->jenisnoo;
	$nama = $data->nama;
	$alamat= $data->alamat;
	$noktp= $data->noktp;
	$pemilik= $data->pemilik;
	$tgl_lahir= $data->tgl_lahir;
	$kontak= $data->kontak;
	$email= $data->email;
	$telp1 = $data->telp1;
	$telp2 = $data->telp2;
	$hp1 = $data->hp1;
	$hp2= $data->hp2;
	$group_outl= $data->group_outl;
	$tipe_outl = $data->tipe_outl;
	$jenis_outl = $data->jenis_outl;
	$rayon= $data->rayon;
	$rute=$data->rute;
	$area=$data->area;
	$pasar=$data->pasar;
	$lingkungan=$data->lingkungan;
	$norek=$data->norek;
	$bank=$data->bank;
	$pemilikrek=$data->atasnama;
	$npwp=$data->npwp;
	$noseri=$data->noseri;
	$namanpwp=$data->namanpwp;
	$alamatnpwp=$data->alamatnpwp;
	$longi=$data->longi;
	$lati=$data->lati;
	$tanggal=$data->tanggal;
	
	/*
	$tgl_lahir_t='';
	if (strlen($tgl_lahir) != 0) {
		$tgl_lahir_t=date_create($tgl_lahir);
		$tgl_lahir_t= date_format($tgl_lahir_t,'Y-m-d H:i:s');
	}
	
	$tanggal_t='';
	if (strlen($tanggal) != 0) {
		$tanggal_t=date_create($tanggal);
		$tanggal_t= date_format($tanggal_t,'Y-m-d H:i:s');
	} */
	
	$sql= "insert into noo (no_pengajuan,kd_sales,nama_outlet
			,alamat,no_ktp,nama_pemilik
			,nama_kontak,email,telp1
			,telp2,no_hp1,no_hp2
			,group_outl,tipe_outl,jenis_outl
			,rayon,rute,kd_kelurahan
			,pasar,lingkungan,jenis_noo
			,no_rek,atas_nama_rek,bank
			,npwp,no_npwp_seri,nama_npwp
			,alamat_npwp,upl
			,tanggal,tgl_lahir,longitude,latitude) values(
			'$nopengajuan','$kd_sales','$nama'
			,'$alamat','$noktp','$pemilik'
			,'$kontak','$email','$telp1'
			,'$telp2','$hp1','$hp2'
			,'$group_outl','$tipe_outl','$jenis_outl'
			,'$rayon','$rute','$area'
			,'$pasar','$lingkungan','$jenisnoo'
			,'$norek','$pemilikrek','$bank'
			,'$npwp','$noseri','$namanpwp'
			,'$alamatnpwp',0
			,'$tanggal','$tgl_lahir','$longi','$lati')";
			
	$hasil = database($sql);
	if ($hasil) {echo "1"; }else{echo "0"; }

} // akhir dari insert noo

?>