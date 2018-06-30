<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class LoginController extends Controller
{
    //
    public function __construct()

    {
      $this->middleware('guest', ['except' => 'destroy']);
    }


    public function create()
    {
      return view('login.create');
    }

    public function store() {
      if (! auth()->attempt(request(['username','password']))){
        return back()->withErrors([
          'message' => 'Wrong password/username'
        ]);
      }

      $userStatus = auth()->user()->admin;
      if($userStatus === 3 || $userStatus == 8){
        auth()->logout();
        return back()->withErrors([
          'message' => 'Your account has been suspended temporarily.'
        ]);
      }
      if($userStatus === 4 || $userStatus == 9){
        auth()->logout();
        return back()->withErrors([
          'message' => 'Your account has been banned.'
        ]);
      }


      return redirect('/admin');
    }
    public function destroy()
    {
      auth()->logout();
      return redirect('/');
    }
}
