<?

require_once("koneksi.php");

$sql="SELECT KD_Pasar
      ,Nama_pasar
  FROM MS_Pasar
  ORDER BY Nama_pasar";

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