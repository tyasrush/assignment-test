<?php

$result = array();

if (isset($_POST["username"]) && isset($_POST["password"])) {
    if ($_POST["username"] == "agus" && $_POST["password"] == "password") {
        $result['result'] = true;
        echo json_encode($result);
    } else {
        $result['result'] = false;
        echo json_encode($result);
    }
} else {
    echo "no params";
}
?>

