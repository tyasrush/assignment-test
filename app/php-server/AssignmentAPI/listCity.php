<?php

include './model/City.php';

$cities = array();
$cities[] = City::Kuningan;
$cities[] = City::Slipi;
$cities[] = City::Pluit;

echo json_encode($cities);

?>

