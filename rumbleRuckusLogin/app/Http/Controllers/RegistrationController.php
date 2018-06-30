<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\User;
use App\Mail\Welcome;
class RegistrationController extends Controller
{
    //

    public function __construct()

    {
      $this->middleware('guest');
    }

    public function create()

    {
      return view('registration.create');
    }

    public function store()

    {
      //Validate form
      $this->validate(request(), [
        'email' => 'required|unique:users|email',
        'username' => 'required|unique:users|alpha_num|min:5|max:20',
        'password' => 'required|alpha_num|min:8|max:20|confirmed',
        'winnerquote' => 'required|max:80'
      ]);
      //Create and save username
      $user = User::create([
        'username' => request('username'),
        'email' => request('email'),
        'password' => bcrypt(request('password')),
        'winnerquote' => request('winnerquote')
      ]);


      //Sign them in
      auth()->login($user);
      //Send to game

      \Mail::to($user)->send(new Welcome($user));

      session()->flash('message','Thanks for signing up for Rumble Ruckus!');

      return redirect('/play');
    }
}
