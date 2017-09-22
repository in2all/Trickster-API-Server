<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Sign in</title>
</head>
<body>
    <h1>${context.pathParams()["appId"]}</h1>
    <form method="post" action="${context.pathParams()["appId"]}">
        <input type="email" name="email" placeholder="E-mail">
        <input type="password" name="password" placeholder="Password">
        <input type="submit" value="Sign in">
    </form>
</body>
</html>