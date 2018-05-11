<?php
require_once("koneksi.php");

	$nopengajuan = trim($_POST['nopengajuan']);
	$kd_sales=trim($_POST['kd_sales']);
	$jenisnoo =trim($_POST['jenisnoo']);
	$nama =trim($_POST['nama']);
	$alamat= trim($_POST['alamat']);
	$noktp=trim($_POST['noktp']);
	$pemilik= trim($_POST['pemilik']);
	$tgl_lahir= trim($_POST['tgl_lahir']);
	$kontak= trim($_POST['kontak']);
	$email=trim($_POST['email']); 
	$telp1 =trim($_POST['telp1']);
	$telp2 = trim($_POST['telp2']);
	$hp1 = trim($_POST['hp1']);
	$hp2= trim($_POST['hp2']);
	$group_outl= trim($_POST['group_outl']);
	$tipe_outl = trim($_POST['tipe_outl']);
	$jenis_outl = trim($_POST['jenis_outl']);
	$rayon= trim($_POST['rayon']);
	$rute= trim($_POST['rute']);
	$area= trim($_POST['area']);
	$pasar= trim($_POST['pasar']);
	$lingkungan= trim($_POST['lingkungan']);
	$norek= trim($_POST['norek']);
	$bank= trim($_POST['bank']);
	$pemilikrek= trim($_POST['atasnama']);
	$npwp= trim($_POST['npwp']);
	$noseri=trim($_POST['noseri']);
	$namanpwp= trim($_POST['namanpwp']);
	$alamatnpwp= trim($_POST['alamatnpwp']);
	$longi= trim($_POST['longi']);
	$lati= trim($_POST['lati']);
	$tanggal= trim($_POST['tanggal']);

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
			
			$result= sqlsrv_query($koneksi,$sql) or die("0");
			
				if ($result){
	     			echo 1;
	 			}else {
	     			// print status message
	    			echo 0; }
					
					
	sqlsrv_close($koneksi);

?>