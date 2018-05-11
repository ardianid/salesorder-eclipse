<?

require_once("koneksi.php");

$kd_kelurahan=$_REQUEST['kd_kelurahan'];

$sql="SELECT     MS_Kelurahan.Kd_Kelurahan, MS_Kelurahan.Nama_Kelurahan, MS_Kecamatan.Nama_Kecamatan, MS_Kabupaten.Nama_Kabupaten
		FROM         MS_Kelurahan INNER JOIN
                      MS_Kecamatan ON MS_Kelurahan.Kd_Kecamatan = MS_Kecamatan.Kd_Kecamatan INNER JOIN
                      MS_Kabupaten ON MS_Kelurahan.Kd_Kabupaten = MS_Kabupaten.Kd_Kabupaten
		WHERE MS_Kelurahan.Kd_Kelurahan='$kd_kelurahan'";

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