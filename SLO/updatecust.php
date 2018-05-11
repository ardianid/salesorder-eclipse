<?php
require_once("koneksi.php");

	$kd_cust=trim($_POST['kd_cust']);
	$lat=trim($_POST['lat']);
	$long=trim($_POST['long']);
	$kd_sales=trim($_POST['kd_sales']);

		$sql="select kd_customer,lat,long,kd_sales_edit from ms_customer where kd_customer='$kd_cust'";
	$temp= sqlsrv_fetch_array(sqlsrv_query($koneksi,$sql));

	if ($temp[0]['kd_customer'] !='') {
	
		if ($temp[0]['lat'] !='') {
			$lati=$temp[0]['lat'];
		}else{
			$lati='';
		}
		
		if ($temp[0]['long'] !='') {
			$longi=$temp[0]['long'];
		}else{
			$longi='';
		}
		
		if (strlen($lati) > 0 && strlen($longi) > 0) {
		
		//	$salesedit=	$temp[0]['kd_sales_edit'];
			echo 2;
			
		
		}else{
	
		$sql_up="update ms_customer set lat='$lat',long='$long',kd_sales_edit='$kd_sales' where kd_customer='$kd_cust'";
		
		$hasil =  sqlsrv_query($koneksi,$sql_up) or die("0");
		if($hasil) { echo 1; }else{ echo 0; }
		
		}
		
	} else {echo 0; }// akhir dari if

	sqlsrv_close($koneksi);

?>