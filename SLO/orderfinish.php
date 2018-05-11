<?php
require_once("koneksi.php");

	$nobukti=trim($_POST['no_bukti']);

$sql="update faktur_h set stat=1 where no_bukti='$nobukti'";
$hasil = sqlsrv_query($koneksi,$sql) or die("0");
			if($hasil) { echo 1; }else{ echo 0; }

	sqlsrv_close($koneksi);

?>