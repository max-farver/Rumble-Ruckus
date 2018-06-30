
<html lang="{{ app()->getLocale() }}">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <title>Forgot Password</title>

        <!-- Fonts -->
        <link href="https://fonts.googleapis.com/css?family=Raleway:100,600" rel="stylesheet" type="text/css">

        <!-- Styles -->


    </head>
    <body>
      <h1>Forgot Password</h1>

      <form method="POST" action="/forgotpassword">
        {{ csrf_field() }}


        <div class="form-group">
            <label for="email">Email:</label>
            <input type="email" class="form-control" id="email" placeholder="Email" name="email">
        </div>




        <div class="form-group">
          <button type="submit" class="btn btn-primary">Submit</button>
        </div>




        @include ('layouts.errors')

      </form>

    </body>
</html>
