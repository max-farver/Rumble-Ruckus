<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\User;
use App\Mail\ForgotPassword;
class ForgotPasswordController extends Controller
{
    //
    public function __construct()

    {
     $this->middleware('auth')->only('update');
     $this->middleware('checkForgotPassword')->only('index');
      $this->middleware('guest')->only('create','store');
    }

    public function index() {
      return view('forgotPassword.update');
    }

    public function create() {
      return view ('forgotPassword.create');
    }

    public function store() {
      $this->validate(request(), [
        'email' => 'required|exists:users|email'
      ]);

      $user = User::where('email', request('email'))->first();

      $password = str_random(8);

      $user->password = bcrypt($password);
      if($user->admin < 5){
        $user->admin = $user->admin + 5;
      }

      $user->save();
      \Mail::to($user)->send(new ForgotPassword($user, $password));

      return redirect('/login');
    }

    public function update() {
      $this->validate(request(), [
        'password' => 'required|confirmed',
      ]);
      $user = request()->user();
      $user->password = bcrypt(request('password'));
      $user->admin = (($user->admin) - 5);
      $user->save();

      return redirect('/play');
    }
}
