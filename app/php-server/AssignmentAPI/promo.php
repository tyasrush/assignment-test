<?php

include './model/Data.php';
include './model/City.php';

$lengthItem = 15;
$listItem = array();

for ($i = 0; $i < $lengthItem; $i++) {
    $data = "";
    if ($i % 2 == 0) {
        $data = new Data($i + 1, "Promo " . $i, City::Slipi);
    } else if ($i % 3 == 0) {
        $data = new Data($i + 1, "Promo " . $i, City::Pluit);
    } else if ($i % 4 == 0) {
        $data = new Data($i + 1, "Promo " . $i, City::Slipi);
    } else {
        $data = new Data($i + 1, "Promo " . $i, City::Kuningan);
    }

    $listItem[] = $data;
}

if (isset($_POST['city'])) {
    $listFilterItem = array();
    foreach ($listItem as $value) {
        if ($value->getCity() == $_POST['city']) {
            $listFilterItem[] = $value;
        }
    }
    
    echo json_encode($listFilterItem);
} else {
    echo json_encode($listItem);
}
?>

