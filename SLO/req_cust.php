<?
require_once("koneksi.php");

$nama_cust=$_REQUEST['nama_cust'];
$kd_sales=$_REQUEST['kd_sales'];

$sql="SELECT     MS_Customer.KD_Customer, MS_Customer.Alamat, MS_Customer.Perusahaan
FROM         MS_Terms INNER JOIN
                      MS_Customer ON MS_Terms.KD_Customer = MS_Customer.KD_Customer
WHERE     MS_Terms.KD_Salesman = '$kd_sales' and MS_Customer.Perusahaan like '%$nama_cust%'";

$result= sqlsrv_query($koneksi,$sql) or die("terjadi kesalahan saat eksekusi query");

  if (sqlsrv_has_rows($result)) {
		$rows = array();
			while ($r = sqlsrv_fetch_array($result,SQLSRV_FETCH_ASSOC)) {
		    	$rows[] = $r;
							}
				$data = json_encode($rows);
				
				echo $data;
		} 
		
sqlsrv_close($koneksi);		
			
?>