<?

if (function_exists('sqlsrv_connect')) {

	$server = '(local)\SQL2008';
	$conn = array("UID"=>"sa",
				"PWD"=>"pegasus",
				"Database"=>"SLO");

	$koneksi=sqlsrv_connect($server,$conn);

	if (! $koneksi) {
		die (sqlsrv_errors());
		}

} else {
echo "Connection functions are not available.<br />\n";
}

?>