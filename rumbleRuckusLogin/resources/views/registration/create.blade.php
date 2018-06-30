<html lang="{{ app()->getLocale() }}">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">


        <title>Registration</title>
        <!-- Fonts -->
        <link href="https://fonts.googleapis.com/css?family=Raleway:100,600" rel="stylesheet" type="text/css">

        <!-- Styles -->


    </head>
    <body>
      <h1>Registration</h1>

      <form method="POST" action="/registration">
        {{ csrf_field() }}
        <div class="form-group">
            <label for="email">Email:</label>
            <input type="email" class="form-control" id="email" placeholder="Email" name='email' value={{ old('email')}}>
        </div>

        <div class="form-group">
            <label for="username">Username:</label>
            <input type="text" class="form-control" id="username" placeholder="Username" name='username' value={{ old('username')}}>
        </div>

        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" class="form-control" id="password" placeholder="Password" name="password">
        </div>

        <div class="form-group">
               <label for="password_confirmation">Password Confirmation:</label>
               <input type="password" class="form-control" id="password_confirmation" placeholder="Re-enter your Password" name="password_confirmation">
        </div>



        <div class="form-group">
            <label for="winnerquote">Victory Quote:</label>
            <input type="text" class="form-control" id="winnerquote" placeholder="I'm the best!" name='winnerquote' value={{ old('winnerquote') }}>
        </div>


        <div class="form-group">
          <button type="submit" class="btn btn-primary">Register</button>
        </div>




        @include ('layouts.errors')

      </form>


    </body>
</html>
