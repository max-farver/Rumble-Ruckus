<?php
  /*Making a Radio Class.
    Practicing simple object-orientation*/
  class Radio {
    //Private variables
    private $volume;
    private $channel;

    //Constructor
    public  function __construct($channel, $volume) {
      $this->volume = $volume;
      $this->channel = $channel;
    }

    public function getChannel() {
      print nl2br("Channel: " . $this->channel . ".\n");
    }

    public function getVolume() {
      print nl2br("Volume: " . $this->volume . ".\n");
    }

    public function upChannel() {
      $this->channel++;
    }

    public function downChannel() {
      $this->channel--;
      if($this->channel == 0) {
        $this->channel = 1;
      }
    }

    public function upVolume() {
      $this->volume++;
    }

    public function downVolume() {
      $this->volume--;
      if($this->volume == 0) {
        $this->volume = 1;
      }
    }
  }

  //Running some tests of the methods.
  $myRadio = new Radio(5, 2);
  $myRadio->getVolume();
  $myRadio->getChannel();
  $myRadio->upVolume();
  $myRadio->downChannel();
  $myRadio->getVolume();
  $myRadio->getChannel();
?>
