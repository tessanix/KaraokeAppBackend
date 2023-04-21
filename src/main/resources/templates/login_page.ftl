<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Page de connexion</title>
        <link href="login_style.css" rel="stylesheet" />
        <script defer src="login_functions.js"></script>

    </head>
    <body>
        <h1>Page de connexion</h1>

        <form id="login-form" method="POST" action="send_login">

            <label for="username">Nom d'utilisateur :</label>
            <input type="text" id="username" name="username"><br>

            <label for="password">Mot de passe :</label>
            <input type="password" id="password" name="password"><br>

            <input type="submit" value="Se connecter" id="login-button">
        </form>

        <div id="connectedContent">
            <h1>Tu es connecté</h1>
            <button id="goToSongsPageButton">Musiques</button>
            <button id="goToRequestsPageButton">Requêtes des clients</button>
        </div>

    </body>
</html>