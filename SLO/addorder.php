<?php
require_once("koneksi.php");

	$nobukti=trim($_POST['nobukti']);
	$kd_cust=trim($_POST['kd_cust']);
	$kd_salesman=trim($_POST['kd_salesman']);
	$cara_bayar=trim($_POST['cara_bayar']);
	$lat=trim($_POST['lat']);
	$long=trim($_POST['long']);
	$jam=trim($_POST['jam']);
	$tanggal=trim($_POST['tanggal']);
	$jamselesai=trim($_POST['jamselesai']);
	$jarak=trim($_POST['jarak']);
	
	$sql="select no_bukti from faktur_h where no_bukti='$nobukti'";
	$temp=sqlsrv_fetch_array(sqlsrv_query($koneksi,$sql));

	if ($temp[0]['no_bukti'] !='') {

		$sqlup="update faktur_h set kd_cust='$kd_cust',kd_salesman='$kd_salesman',carabayar='$cara_bayar',lat='$lat',long='$long',jam='$jam'
			,tanggal='$tanggal',jamselesai='$jamselesai',jarak='$jarak' where no_bukti='$nobukti'";

			$hasil = sqlsrv_query($koneksi,$sqlup) or die("0");
			if($hasil) { echo 1; }else{ echo 0; }
	
		 } else {
	
		$sql_ins="insert into faktur_h (no_bukti,kd_cust,kd_salesman,carabayar,lat,long,jam,tanggal,jamselesai,jarak) values(
			'$nobukti','$kd_cust','$kd_salesman','$cara_bayar','$lat','$long','$jam','$tanggal','$jamselesai','$jarak')";
		
			$hasil =  sqlsrv_query($koneksi,$sql_ins) or die("0");
			if($hasil) { echo 1; }else{ echo 0; }
		}
		
		
		sqlsrv_close($koneksi);

?>