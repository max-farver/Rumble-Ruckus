<!doctype html>
<html>
    <head>

        <title>Play</title>

    </head>
    <body>
      @if ($flash = session('message'))
      <div id="flash-message" class="alert alert-success" role="alert">
        {{ $flash }}
      </div>
      @endif

      <h1>You are now logged in, {{ Auth::user()->username }}</h1>
      <h1>This takes you to the game!</h1>

      <a href="/usersettings">User Settings</a>
      <a href="/logout">Logout</a>
    </body>
</html>
