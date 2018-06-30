<?php

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Route::get('/', function () {
    return view('welcome');
});


Route::get('/login', 'LoginController@create')->name('login');
Route::post('/login', 'LoginController@store');
Route::get('/logout', 'LoginController@destroy');

Route::get('/registration', 'RegistrationController@create');
Route::post('/registration', 'RegistrationController@store');

Route::get('/play', 'PlayController@index');

Route::get('/admin', 'AdminController@index');
Route::post('/admin/warning', 'AdminController@setWarning');
Route::post('/admin/suspend', 'AdminController@setSuspension');
Route::post('/admin/unsuspend', 'AdminController@unsuspend');
Route::post('/admin/ban', 'AdminController@ban');
Route::post('/admin/search','AdminController@search');

Route::get('/forgotpassword', 'ForgotPasswordController@create');
Route::post('/forgotpassword', 'ForgotPasswordController@store');
Route::get('/forgotpassword/reset', 'ForgotPasswordController@index')->name('reset');
Route::post('/forgotpassword/reset', 'ForgotPasswordController@update');

Route::get('/usersettings', 'UserSettingsController@index');
//Route::post('/usersettings', 'UserSettingsController@store');
Route::post('/usersettings/quote', 'UserSettingsController@updateWinnerQuote');
Route::post('/usersettings/password', 'UserSettingsController@updatePassword');
Route::post('/usersettings/settings', 'UserSettingsController@updateSettings');
Route::post('/usersettings/delete', 'UserSettingsController@closeAccount');
