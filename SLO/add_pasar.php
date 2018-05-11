<?

require_once("koneksi.php");

$KD_Pasar=$_REQUEST['KD_Pasar'];
$Nama_pasar=$_REQUEST['Nama_pasar'];
$User_Input=$_REQUEST['User_Input'];
$KD_Kelurahan=$_REQUEST['KD_Kelurahan'];

$sql="insert into MS_Pasar (KD_Pasar
      ,Nama_pasar
      ,User_Input
      ,KD_Kelurahan) values ('$KD_Pasar'
      ,'$Nama_pasar'
      ,'$User_Input'
      ,'$KD_Kelurahan')";
	  
$result= sqlsrv_query($koneksi,$sql) or die("0");		

echo "1";

sqlsrv_close($koneksi);	  

?>