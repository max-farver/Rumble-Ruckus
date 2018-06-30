
<html lang="{{ app()->getLocale() }}">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">


        <title>Login</title>
        <!-- Fonts -->
        <link href="https://fonts.googleapis.com/css?family=Raleway:100,600" rel="stylesheet" type="text/css">

        <!-- Styles -->


    </head>
    <body>
      <h1>Login</h1>

      <form method="POST" action="/login">
        {{ csrf_field() }}


        <div class="form-group">
            <label for="username">Username:</label>
            <input type="text" class="form-control" id="username" placeholder="Username" name='username'>
        </div>

        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" class="form-control" id="password" placeholder="Password" name="password">
        </div>


        <div class="form-group">
          <button type="submit" class="btn btn-primary">Log in</button>
        </div>




        @include ('layouts.errors')

      </form>
      <div class="account">
        <a href="/registration">Create an Account</a>
        <a href="/forgotpassword">Forgot your Password?</a>
      </div>
    </body>
</html>
