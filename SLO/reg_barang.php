<?

require_once("koneksi.php");

$kd_brg = $_REQUEST['kd_barang'];

$sql="SELECT TOP 1000 KD_Barang
      ,Nama_Barang
      ,Satuan4
      ,Satuan3
      ,Satuan2
      ,Satuan1
      ,Isi4
      ,Isi3
      ,Isi2
	  ,Non_Aktif
  FROM MS_Barang
  WHERE KD_Barang='$kd_brg'";
  
  $result= sqlsrv_query($koneksi,$sql) or die("terjadi kesalahan saat eksekusi query");
   
   if (sqlsrv_has_rows($result)) {
		$rows = array();
			while ($r = sqlsrv_fetch_array($result)) {
		    	$rows[] = $r;
							}
				$data = "{" .json_encode($rows)."}";
			
			
		if ($data['Non_Aktif']='T') {
				echo 'non_aktif'; }
			else {
				echo $data;
			}
		
	} else {
		echo "Data barang tidak ditemukan";
	}
	
	sqlsrv_close($koneksi);
	
?>
