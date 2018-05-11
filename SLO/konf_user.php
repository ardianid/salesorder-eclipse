<?

require_once("koneksi.php");

$user= $_REQUEST['username'];
$pwd= $_REQUEST['pwd'];

$sql="select username,pwd from user_header where username='$user' and pwd='$pwd'";

$result= sqlsrv_query($koneksi,$sql) or die("terjadi kesalahan saat eksekusi query");

if (sqlsrv_has_rows($result)) {
	echo 1;
	}
	else {
	echo 0;
	}

sqlsrv_close($koneksi);

?>
