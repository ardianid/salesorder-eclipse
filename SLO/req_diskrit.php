<?

require_once("koneksi.php");

$sql="SELECT Keterangan as diskrit
  FROM MS_Other
  WHERE type='Diskrit'
  ORDER BY Keterangan asc";

$result= sqlsrv_query($koneksi,$sql) or die("terjadi kesalahan saat eksekusi query");			
   
   if (sqlsrv_has_rows($result)) {
		$rows = array();
			while ($r = sqlsrv_fetch_array($result,SQLSRV_FETCH_ASSOC)) {
		    	$rows[] = $r;
							}
				$data = json_encode($rows);
				
				echo $data;
		} else {
		
		echo "data tidak ditemukan";
		}

sqlsrv_close($koneksi);

?>