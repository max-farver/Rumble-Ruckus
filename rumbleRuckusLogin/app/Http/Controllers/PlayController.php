<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class PlayController extends Controller
{
    //
    public function __construct()

    {
      $this->middleware('auth');
    }

    public function index()
    {
      

      return view('play.index');
    }
}
