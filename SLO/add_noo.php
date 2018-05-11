<?
require_once("koneksi.php");

$kd_sales=trim($_POST['kd_sales']);
$tanggal= date("Y/m/d"); //$_POST['tanggal'];
$nama_outlet=trim($_POST['nama_outlet']);
$nama_pemilik=trim($_POST['nama_pemilik']);
$nama_kontak=trim($_POST['nama_kontak']);
$tgl_lahir=date("Y/m/d",strtotime($_POST['tgl_lahir']));
$email=trim($_POST['email']);
$telp1=trim($_POST['telp1']);
$telp2=trim($_POST['telp2']);
$no_hp1=trim($_POST['no_hp1']);
$no_hp2=trim($_POST['no_hp2']);
$longitud=$_POST['longitud'];
$latitud=$_POST['latitud'];
$group_outlet=trim($_POST['group_outlet']);
$type_outlet=trim($_POST['type_outlet']);
$jenis_outlet=trim($_POST['jenis_outlet']);
$rayon=trim($_POST['rayon']);
$rute=trim($_POST['rute']);
$pasar=trim($_POST['pasar']);
$lingkungan=trim($_POST['lingkungan']);
$bank1=trim($_POST['bank1']);
$bank2='-'; //$_POST['bank2'];
$bank3= '-'; //$_POST['bank3'];
$no_rek_cc1=trim($_POST['no_rek_cc1']);
$no_rek_cc2= '-'; //$_POST['no_rek_cc2'];
$no_rek_cc3= '-'; //$_POST['no_rek_cc3'];
$atas_nama_rek1=trim($_POST['atas_nama_rek1']);
$atas_nama_rek2= '-'; //$_POST['atas_nama_rek2'];
$atas_nama_rek3= '-'; //$_POST['atas_nama_rek3'];
$status=trim($_POST['status']); // 1: add 2: edit

if ($status='1') {

$nopengajuan=0;
$no_noo=0;

$sqlcari="select max(no_noo) as no_noo from noo_baru where kd_sales='$kd_sales'";

$result_aja= sqlsrv_query($koneksi,$sqlcari) or die("0");	

if (sqlsrv_has_rows($result_aja)) {
	
	$row2 = sqlsrv_fetch_array($result_aja);
	$nopengajuan = $row2['no_noo'] +1;
	$no_noo=$nopengajuan;
	
}else {	
	$nopengajuan=0;
};

$nopengajuan = $kd_sales ."-". $nopengajuan;

$sql="insert into noo_baru (no_pengajuan,no_noo
		,kd_sales,tanggal,nama_outlet,nama_pemilik,nama_kontak,tgl_lahir,email,telp1
			,telp2,no_hp1,no_hp2,longitud,latitud,group_outlet,type_outlet,jenis_outlet,rayon
				,rute,pasar,lingkungan,bank1,bank2,bank3,no_rek_cc1,no_rek_cc2,no_rek_cc3,atas_nama_rek1,atas_nama_rek2,atas_nama_rek3)
		values('$nopengajuan','$no_noo'
		,'$kd_sales','$tanggal','$nama_outlet','$nama_pemilik','$nama_kontak','$tgl_lahir','$email','$telp1'
			,'$telp2','$no_hp1','$no_hp2','$longitud','$latitud','$group_outlet','$type_outlet','$jenis_outlet','$rayon'
				,'$rute','$pasar','$lingkungan','$bank1','$bank2','$bank3','$no_rek_cc1','$no_rek_cc2','$no_rek_cc3','$atas_nama_rek1','$atas_nama_rek2','$atas_nama_rek3')";

$result= sqlsrv_query($koneksi,$sql) or die("0");		


	if ($result){
	     echo 1;
	 }else {
	     // print status message
	    echo 0; }

				

} else {

$sqlcari="select no_pengajuan from noo_baru where no_pengajuan='$no_pengajuan'";

$result_cari= sqlsrv_query($koneksi,$sqlcari) or die("kesalahan query");

if (sqlsrv_has_rows($result_cari)) {

	$row = sqlsrv_fetch_array($result_cari);
	
	if ($row['verifikasi']='1') { echo("data telah diverifkasi, perubahan dibatalkan"); }
	
	$sql="update noo_baru set
		 kd_sales='$kd_sales'
		,tanggal='$tanggal'
		,nama_outlet='$nama_outlet'
		,nama_pemilik='$nama_pemilik'
		,nama_kontak='$nama_kontak'
		,tgl_lahir='$tgl_lahir'
		,email='$email'
		,telp1='$telp1'
		,telp2='$telp2'
		,no_hp1='$no_hp1'
		,no_hp2='$no_hp2'
		,group_outlet='$group_outlet'
		,type_outlet='$type_outlet'
		,jenis_outlet='$jenis_outlet'
		,rayon='$rayon'
		,rute='$rute'
		,pasar='$pasar'
		,lingkungan='$lingkungan'
		,bank1='$bank1'
		,bank2='$bank2'
		,bank3='$bank3'
		,no_rek_cc1='$no_rek_cc1'
		,no_rek_cc2='$no_rek_cc2'
		,no_rek_cc3='$no_rek_cc3'
		,atas_nama_rek1='$atas_nama_rek1'
		,atas_nama_rek2='$atas_nama_rek2'
		,atas_nama_rek3='$atas_nama_rek3'
	  where no_pengajuan='$no_pengajuan'";
	  
	$result= sqlsrv_query($koneksi,$sql) or die("0");		

	echo "1"; } else { echo("data yang akan diedit tidak ditemukan"); }
	
	
	  
}



sqlsrv_close($koneksi);

?>