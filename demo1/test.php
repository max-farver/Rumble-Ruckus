<?php
	//echo "Hi World";
	$groupName = "Rumble Ruckus\nMembers: ";
	echo nl2br("Hello World\n");
	echo nl2br($groupName);
	echo "Sam" . " " . "Zhang\n" . "!";
	$age = 2*10;
	echo nl2br("\n" . $age . "\n");
	print nl2br("This is me testing print \n does new line key work with print?\n");

	if($age > 18) {
		print nl2br("You're an adult.\n");
	} else {
		print nl2br("You're too young for college.\n");
	}
	
	if($age > 21) {
		print nl2br("You can drink alcohol.\n");
	} else if($age == 20){
		print nl2br("You're too young for alcohol, but almost.\n");
	} else {
		print nl2br("You're too young for alcohol.\n");
	}

	switch(309):
		case 227:
			echo nl2br("Too early for 309\n");
			break;
		case 228:
			echo nl2br("Too early for 309\n");
			break;
		case 309:
			echo nl2br("Prepare for demo 1.\n");
			break;
		default:
			echo nl2br("NAN\n");
			break;
	endswitch;

	$lunch = array("rice", "corn", "beef", "beans", "chili powder");
	$lunch[1] = "vast amount of corn";
	echo nl2br("My favorite part was the " . $lunch[1] . ".\n");
	unset($lunch[1]);
	echo nl2br("My favorite part was the " . $lunch[0] . ".\n");
	for ($i = 0; $i < 10; $i++) {
        	echo $i;
        }
	echo nl2br("\n");
	foreach($lunch as $food) {
		echo $food . " ";
	}
	echo nl2br("\n");
	
	$sam = "SamZhang";
	print nl2br($sam . "\n");
	$sam = substr($sam, 0, 3);
	print nl2br($sam . "\n");
	$a = strlen($sam);
    	print nl2br($a . "\n");
	$sam = strtoupper($sam);
	print nl2br($sam . "\n");
	$sam = strtolower($sam);
	print nl2br($sam . "\n");
?>