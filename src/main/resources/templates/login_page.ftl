<#ftl encoding="utf-8">
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Page de connexion</title>
        <link href="login_style.css" rel="stylesheet" />
        <script defer type="module" src="login_functions.js"></script>
    </head>
    <body>

        <form id="login-form" method="POST" action="send_login">
            <h1>Page de connexion</h1>

            <label for="username">Nom d'utilisateur :</label>
            <input type="text" id="username" name="username"><br>

            <label for="password">Mot de passe :</label>
            <input type="password" id="password" name="password"><br>

            <input type="submit" value="Se connecter" id="login-button">
        </form>

        <div id="contentWhenConnected">
            <h1>Tu es connecté</h1>
            <button id="goToSongsPageButton">Musiques</button>
            <button id="goToRequestsPageButton">Requêtes des clients</button>
            <button id="logoutButton">Se déconnecter</button>
        </div>
    </body>
</html>