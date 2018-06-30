<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\User;

class AdminController extends Controller
{
  public function __construct()

  {
    $this->middleware('isAdmin');
  }

  public function index(){

    $users = User::all()->whereIn('admin',[0,2,3,4,5,7,8,9]);

    return view('admin.index', compact('users'));
  }

  public function update() {
    if(request()->input('warningSubmit')){
      return $this->setWarning();
    }
    else if(request()->input('suspendSubmit')){
      return $this->setSuspension();
    }
    else if(request()->input('unsuspendSubmit')){
      return $this->unsuspend();
    }
    else if(request()->input('banSubmit')){
      return $this->ban();
    }
    else if(request()->input('searchSubmit')){
      return $this->search();
    }

    else {
      return view('admin.index');
    }

  }

  public function setWarning() {
    $user = User::all()->where('username',request('warning'))->first();
    if($user->admin > 4){
      $user->admin = 7;
    }
    else {
      $user->admin = 2;
    }


    $user->save();
    return $this->index();
  }

  public function setSuspension() {
    $user = User::all()->where('username',request('suspend'))->first();
    if($user->admin > 4){
      $user->admin = 8;
    }

    else {
      $user->admin = 3;
    }


    $user->save();
    return $this->index();
  }

  public function unsuspend() {
    $user = User::all()->where('username',request('unsuspend'))->first();

    if($user->admin > 4){
      $user->admin = 5;
    }

    else {
      $user->admin = 0;
    }


    $user->save();
    return $this->index();
  }

  public function ban() {
    $user = User::all()->where('username',request('ban'))->first();
    $user->admin = 4;

    $user->save();
    return $this->index();
  }

  public function search() {
    $users = User::whereIn('admin',[0,2,3,4,5,7,8,9])->where('username','like','%' . request('search') . '%')->get();

    return view('admin.index', compact('users'));
  }
}
