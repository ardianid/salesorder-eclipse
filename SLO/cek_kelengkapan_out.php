<?

include_once("koneksi.php");

$kd_customer=$_REQUEST["kode_cust"];

$sql="SELECT Type_Customer
      ,Nama
      ,Perusahaan
      ,Alamat
      ,Kode_Pos
      ,Telepon1
      ,Telepon2
      ,Fax
      ,Non_Aktif
      ,NPWP
      ,Nama_NPWP
      ,Alamat_NPWP
      ,Kena_Pajak
      ,tanda_tangan
      ,KD_cabang
      ,Diskrit
      ,TGL_REGISTRASI
      ,Kota_Besar
      ,Kecamatan
      ,KD_Pasar
      ,cBlackList
      ,KD_Bank1
      ,KD_Bank2
      ,KD_Bank3
      ,No_Rekening1
      ,No_Rekening2
      ,No_Rekening3
      ,AtasNama_Rekening1
      ,AtasNama_Rekening2
      ,AtasNama_Rekening3
      ,Tgl_Lahir
      ,no_handphone
      ,Jenis_Customer
      ,nonpwpseri
      ,Jenistoko
      ,alamatkirim
      ,alamattagih
      ,JenisPPN
      ,nama_pemilik
      ,namapendek
  FROM MS_Customer
  WHERE KD_Customer='$kd_customer'";

$result= sqlsrv_query($koneksi,$sql) or die("terjadi kesalahan saat eksekusi query");

if (sqlsrv_has_rows($result)) {

	$rows = array();
			while ($r = sqlsrv_fetch_array($result)) {
		    	$rows[] = $r;
							}
				$row = "{" .json_encode($rows)."}";
	
	if ($row['Non_Aktif']='F') {
	
	$hasil='0';
	
	if ($row['Type_Customer'] ='' or   $row['Nama']='' or $row['Perusahaan']='' or 
		$row['Alamat']='' or $row['Kode_Pos']='' or ($row['Telepon1']='' and $row['Telepon2']='' and $row['no_handphone']='') or 
			$row['Fax']='' or $row['Diskrit']='' or $row['Kecamatan']='' or $row['KD_Pasar']='' or 
				$row['Tgl_Lahir']='' or $row['Jenis_Customer']='' or $row['nonpwpseri']='' or $row['Jenistoko']='' or 
					$row['alamatkirim']='' or $row['alamattagih']='' or $row['nama_pemilik']='') {
									$hasil='1'; }
									
		if (($row['KD_Bank1']='' and $row['KD_Bank2']='' and $row['KD_Bank3']='') or 
		($row['No_Rekening1']='' and $row['No_Rekening2']='' or $row[No_Rekening3]='') or
		($row['AtasNama_Rekening1']='' or $row['AtasNama_Rekening2']='' or $row['AtasNama_Rekening3']='')) {
		
		$hasil='1'; }
		
		if ($row['Kena_Pajak']='T') {
			
			
			if ($row['NPWP']='' or 
				$row['Nama_NPWP']='' or
					$row['Alamat_NPWP']='') {
					
					$hasil='1';}
		}



	if ($hasil <> '0') {
			echo $row; }
		else {
			echo "0"; }
			
			
		} else {
			echo "Outlet sudah tidak aktif lagi"; }
			


}
else {

	echo "0";

}

sqlsrv_close($koneksi);

?>