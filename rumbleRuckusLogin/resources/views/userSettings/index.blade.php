<html lang="{{ app()->getLocale() }}">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <title>User Settings</title>

        <!-- Fonts -->
        <link href="https://fonts.googleapis.com/css?family=Raleway:100,600" rel="stylesheet" type="text/css">

        <!-- Styles -->


    </head>
    <body>
      <h1>User Settings</h1>

      @if ($flash = session('message'))
      <div id="flash-message" class="alert alert-success" role="alert">
        {{ $flash }}
      </div>
      @endif

      <form method="POST" action="/usersettings/password">
        {{ csrf_field() }}


        <hr>

        <div class="form-group">
            <label for="password">New Password:</label>
            <input type="password" class="form-control" id="password" placeholder="Password" name="password">
        </div>

        <div class="form-group">
               <label for="password_confirmation">Confirm New Password:</label>
               <input type="password" class="form-control" id="password_confirmation" placeholder="Re-enter your Password" name="password_confirmation">
        </div>


        <div class="form-group">
          <button type="submit" class="btn btn-primary" value="passwordSubmit">Update Password</button>
        </div>

        @if (count($errors))
        <div class="form-group">


          <div class="alert alert-danger">
            <u1>
              @foreach($errors->get('password') as $error)

                  <li>{{ $error }}</li>


              @endforeach
            </u1>
          </div>

          </div>
        @endif

      </form>

        <hr>
        <form method="POST" action="/usersettings/quote">
          {{ csrf_field() }}
        <div class="form-group">
            <p>Current Victory Quote: {{ Auth::user()->winnerquote }}</p>
            <label for="winnerquote">Update Victory Quote:</label>
            <input type="text" class="form-control" id="winnerquote" placeholder="I'm the best!" name='winnerquote'>
        </div>

        <div class="form-group">
          <button type="submit" class="btn btn-primary" value="quoteSubmit">Update Victory Quote</button>
        </div>

        @if (count($errors))
        <div class="form-group">


          <div class="alert alert-danger">
            <u1>
              @foreach($errors->get('winnerquote') as $error)

                  <li>{{ $error }}</li>


              @endforeach
            </u1>
          </div>

          </div>
        @endif
      </form>
      <hr>
      <form method="POST" action="/usersettings/settings">
          {{ csrf_field() }}

          <div class="form-group">

            <label for="mute">Mute Sound</label>

            <input type="hidden" id="mute" value="0" name="mute">
            @if ( Auth::user()->mute == '1')

              <input type="checkbox" id="mute" value="1" name="mute" checked>

            @else
            <input type="checkbox" id="mute" value="1" name="mute">

            @endif


          </div>

          <div class="form-group">

            <label for="mute">Displayed on Leaderboard</label>

            <input type="hidden" id="mute" value="0" name="leaderboard">
            @if ( Auth::user()->leaderboard == '1')

              <input type="checkbox" id="mute" value="1" name="leaderboard" checked>

            @else
            <input type="checkbox" id="mute" value="1" name="leaderboard">

            @endif


          </div>

          <div class="form-group">
            <button type="submit" class="btn btn-primary" value="settingsSubmit">Update Settings</button>
          </div>
      </form>
      <hr>
      <form method="POST" action="/usersettings/delete">
          {{ csrf_field() }}

          <div class="warning">
            <p>By closing my account, I understand I will lose all game statistics
              and ranks.  I understand my account can not be recoverd once it is closed.</p>
          </div>

          <div class="form-group">

            <label for="close">Enter your current password:</label>
            <input type="password" class="form-control" id="close" placeholder="Current Password" name='currentPassword'>
            <br>
            <label for="closeCheckbox">Yes, I want to close my account</label>
            <input type="checkbox" id="closeCheckbox" value="1" name="close">




          </div>


          <div class="form-group">
            <br>
            <button type="submit" class="btn btn-primary" value="closeSubmit">Close Account</button>
          </div>

          @if (count($errors))
          <div class="form-group">


            <div class="alert alert-danger">
              <u1>
                @foreach($errors->get('currentPassword') as $error)

                    <li>{{ $error }}</li>


                @endforeach
                @foreach($errors->get('close') as $error)

                    <li>{{ $error }}</li>


                @endforeach
              </u1>
            </div>

            </div>
          @endif
      </form>





      <div class="return">
        <a href="/play">Back to the game screen</a>
      </div>
    </body>
</html>
