<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\User;

class UserSettingsController extends Controller
{
    public function __construct(){
      $this->middleware('auth');
    }

    public function index() {
    //  $mute = request()->user;
      return view('userSettings.index');
    }

    public function store() {
      if(request()->input('passwordSubmit')) {
        $this->updatePassword();
      }
      else if(request()->input('quoteSubmit')) {
        $this->updateWinnerQuote();
      }
      else if(request()->input('settingsSubmit')){
        $this->updateSettings();
      }
      else if(request()->input('closeSubmit')){
        $this->closeAccount();
      }
      else{
        return view('userSettings.index');
      }
    }

    public function updatePassword() {
      $this->validate(request(), [
        'password' => 'required|alpha_num|min:8|max:20|confirmed',
      ]);
      $user = request()->user();
      $user->password = bcrypt(request('password'));

      $user->save();

      session()->flash('message','Password updated!');


      return redirect('/usersettings');
    }

    public function updateWinnerQuote() {
      $this->validate(request(), [
        'winnerquote' => 'required|max:80',
      ]);
      $user = request()->user();
      $user->winnerquote = request('winnerquote');

      $user->save();

      session()->flash('message','Victory quote updated!');


      return redirect('/usersettings');
    }

    public function updateSettings() {
      $user = request()->user();
      $user->mute = request('mute');
      $user->leaderboard = request('leaderboard');
      $user->save();
      session()->flash('message','Settings updated!');


      return redirect('/usersettings');
    }

    public function closeAccount() {
      $this->validate(request(), [
        'currentPassword' => 'required|alpha_num|min:8|max:20',
        'close' => 'accepted|required',
      ]);
      $hashChecker = app('hash');
      if($hashChecker->check(request('currentPassword'),request()->user()->password) ){
        request()->user()->delete();
        return redirect('/usersettings');
      }
      else {
        return back()->withErrors([
          'currentPassword' => 'Password did not match'
        ]);
      }
    }
}
