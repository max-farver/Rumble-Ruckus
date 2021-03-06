<?php

namespace App\Http\Middleware;

use Closure;

class isAdmin
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
        if($request->user() && $request->user()->admin == 1){
            return $next($request);
        }

        if($request->user() && $request->user()->admin > 4){
            return redirect('/forgotpassword/reset');
        }



        else{
          //return redirect('/forgotpassword/reset');
          return redirect('/play');
        }

    }
}
