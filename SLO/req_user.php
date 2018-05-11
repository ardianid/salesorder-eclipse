<?

require_once("koneksi.php");

$id_user= $_REQUEST['id_user'];
$pwd= $_REQUEST['pwd'];

$pwd2=md5($pwd);

$sql="SELECT usera.KD_SALES,MS_Salesman.Nama
FROM usera INNER JOIN
                      MS_Salesman ON usera.KD_SALES = MS_Salesman.KD_Salesman WHERE usera.ID_USER='$id_user' AND usera.PWD='$pwd2'";

//$s=md5('123');

//$sql2 ="insert into usera (id_user,pwd,kd_sales) values('DIAN','$s','001')";

//echo $sql2;

//$hasil= sqlsrv_query($koneksi,$sql2);

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