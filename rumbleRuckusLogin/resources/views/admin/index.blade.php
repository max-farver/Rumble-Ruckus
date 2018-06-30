<!doctype html>
<html>
    <head>

        <title>Administration</title>

    </head>
    <body>
      <!--<h1>Administration: {{ Auth::user()->username }}</h1>-->
      <h1>Administration</h1>
      <h3>Search:</h3>
      <form method="POST" action="/admin/search">
          {{ csrf_field() }}
          <div class="form-group">
            <label for="search">Search:</label>
          <input type="text" class="form-control" id="search" placeholder="Enter username" name='search' value={{ old('search')}}>
          <button type="submit" class="btn btn-primary" value="searchSubmit">Search</button>
        </div>
      </form>
      <br>
      <div class="action-table">
        <table>
          <tr>
            <th>Username</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
      @foreach($users->all() as $tempuser)
        <tr>
          <td> {{$tempuser->username}}</td>

          @if($tempuser->admin  === 0 || $tempuser->admin === 5)
            <td>-</td>
          @elseif($tempuser->admin  === 2 || $tempuser->admin === 7)
            <td>Sent Warning</td>
          @elseif($tempuser->admin  === 3 || $tempuser->admin === 8)
            <td>Suspended</td>
          @else
            <td>Banned</td>
          @endif
          <td>
            @if($tempuser->admin  === 4 || $tempuser->admin  === 9)

            @else
            <form method="POST" action="/admin/warning">
                {{ csrf_field() }}
                <div class="action">
                  <input type="hidden" id="warning" value="{{$tempuser->username}}" name="warning">
                  <button type="submit" class="btn btn-primary" value="warningSubmit">Warning</button>
                </div>
              </form>
            @endif
          </td>
          <td>
            @if($tempuser->admin  === 4 || $tempuser->admin  === 9)
            @elseif($tempuser->admin  === 3 || $tempuser->admin === 8)
              <form method="POST" action="/admin/unsuspend">
                {{ csrf_field() }}

                <div class="action">
                  <input type="hidden" id="unsuspend" value="{{$tempuser->username}}" name="unsuspend">
                  <button type="submit" class="btn btn-primary" value="unsuspendSubmit">Unsuspend</button>
                </div>

              </form>
            @else
              <form method="POST" action="/admin/suspend">
                {{ csrf_field() }}

                <div class="action">
                  <input type="hidden" id="suspend" value="{{$tempuser->username}}" name="suspend">
                  <button type="submit" class="btn btn-primary" value="suspendSubmit">Suspend</button>
                </div>

              </form>
            @endif
          </td>
          <td>
            @if($tempuser->admin  === 4 || $tempuser->admin  === 9)
            @else
            <form method="POST" action="/admin/ban">
                {{ csrf_field() }}
                <div class="action">
                  <input type="hidden" id="ban" value="{{$tempuser->username}}" name="ban">
                  <button type="submit" class="btn btn-primary" value="banSubmit">Ban</button>
                </div>
              </form>
            @endif
          </td>

        </tr>

      @endforeach
    </table>
      <br>
      <br>
      <a href="/logout">Logout</a>
    </body>
</html>
