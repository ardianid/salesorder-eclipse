<?php
require_once("koneksi.php");

	$no_bukti=trim($_POST['no_bukti_h']);
	$kd_barang=trim($_POST['kd_barang']);
	$qty1=trim($_POST['qty1']);
	$qty2=trim($_POST['qty2']);
	$qty3=trim($_POST['qty3']);
	$qty4=trim($_POST['qty4']);
	$jam=trim($_POST['jam']);

	$sql_select="select no_bukti_h from faktur_d where no_bukti_h='$no_bukti' and kd_barang='$kd_barang'";
	$temp=sqlsrv_fetch_array(sqlsrv_query($koneksi,$sql_select));

	if ($temp[0]['no_bukti'] !='') {

		$sql_up="update faktur_d set qty1='$qty1',qty2='$qty2',qty3='$qty3',qty4='$qty4',jam='$jam' where
			kd_barang='$kd_barang' and no_bukti_h='$no_bukti'";
	
		$hasil = sqlsrv_query($koneksi,$sql_up) or die("0");
		if($hasil) { echo 1; }else{ echo 0; }
		
	}else{

		$sql="insert into faktur_d (trans,no_bukti_h,kd_barang,qty1,qty2,qty3,qty4,jam) values(
			0,'$no_bukti','$kd_barang','$qty1','$qty2','$qty3','$qty4','$jam')";
			
		$hasil = sqlsrv_query($koneksi,$sql) or die("0");
		if($hasil) { echo 1; }else{ echo 0; }
		
	} // akhir dari if
	
	

	sqlsrv_close($koneksi);

?>