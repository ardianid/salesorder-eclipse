<?

require_once("koneksi.php");

$kd_pasar=$_REQUEST['kd_pasar'];

$sql="SELECT     MS_Pasar.KD_Pasar, MS_Pasar.Nama_pasar, MS_Kelurahan.Nama_Kelurahan, MS_Kecamatan.Nama_Kecamatan
			FROM         MS_Pasar INNER JOIN
                      MS_Kelurahan ON MS_Pasar.KD_Kelurahan = MS_Kelurahan.Kd_Kelurahan INNER JOIN
                      MS_Kecamatan ON MS_Kelurahan.Kd_Kecamatan = MS_Kecamatan.Kd_Kecamatan
			WHERE MS_Pasar.KD_Pasar='$kd_pasar'";

$result= sqlsrv_query($koneksi,$sql) or die("terjadi kesalahan saat eksekusi query");			
   
   if (sqlsrv_has_rows($result)) {
		$rows = array();
			while ($r = sqlsrv_fetch_array($result)) {
		    	$rows[] = $r;
							}
				$data = "{" .json_encode($rows)."}";
				
				echo $data;
		} else {
		
		echo "data tidak ditemukan";
		}

sqlsrv_close($koneksi);

?>