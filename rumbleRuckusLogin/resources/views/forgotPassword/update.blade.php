<html lang="{{ app()->getLocale() }}">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <title>Update Password</title>

        <!-- Fonts -->
        <link href="https://fonts.googleapis.com/css?family=Raleway:100,600" rel="stylesheet" type="text/css">

        <!-- Styles -->


    </head>
<body>


  <form method="POST" action="/forgotpassword/reset">
    {{ csrf_field() }}

    <h3>Update Password:</h3>

    <div class="form-group">
        <label for="password">New Password:</label>
        <input type="password" class="form-control" id="password" placeholder="Password" name="password">
    </div>

    <div class="form-group">
           <label for="password_confirmation">Re-enter your new password:</label>
           <input type="password" class="form-control" id="password_confirmation" placeholder="New Password" name="password_confirmation">
    </div>




    <div class="form-group">
      <button type="submit" class="btn btn-primary">Update Password</button>
    </div>




    @include ('layouts.errors')

  </form>

</body>
</html>
