@component('mail::message')
# Introduction

Hello {{ $user->username }},

We have received a request to reset your password.

Here is your temporary password: {{ $password }}

You will be asked to reset your password once you login.

@component('mail::button', ['url' => '/login'])
Login
@endcomponent

Thanks,<br>
The {{ config('app.name') }} Team
@endcomponent
