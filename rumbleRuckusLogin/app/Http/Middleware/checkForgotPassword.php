<?php

namespace App\Http\Middleware;

use Closure;

class checkForgotPassword
{
    /**
     * Handle an incoming request.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \Closure  $next
     * @return mixed
     */
    public function handle($request, Closure $next)
    {
      if($request->user() && $request->user()->admin > 4){
          return $next($request);
      }
      else {
        return redirect('/login');
      }
    }
}
