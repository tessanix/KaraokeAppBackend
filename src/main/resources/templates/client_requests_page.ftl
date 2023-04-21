<!DOCTYPE html>
<html lang="eng">
    <head>
        <title>Requêtes des clients</title>
        <link href="requests_style.css" rel="stylesheet" />
        <script defer src="requests_functions.js"></script>
    </head>

    <body>
        <div class="menu">
            <button id="openFormButton">Ajouter une requete</button>
            <button id="returnToHomePageButton">Accueil</button>
            <button id="logoutButton">Se déconnecter</button>
        </div>

        <div class="content">
            <h1>Requêtes des clients</h1>
            <div id="clientRequestsListContainer">
                    <#list clientRequestsMap?keys as clientName>
                        <h3>${clientName} demande:</h3>
                        <ul class="requestsOfAClient">
                            <#list clientRequestsMap[clientName] as request>
                                <li class="request" data-request-id=${request.id}>
                                    <p class="request_element">${request.title} | ${request.author}</p>
                                    <button class="deleteRequestButton" data-client-name=${clientName} data-request-id=${request.id}>Supprimer</button>
                                </li>
                            </#list>
                        </ul>
                    </#list>
                </ul>
            </div>
        </div>

        <div id="addRequestForm">
            <button id="closeFormButton">X</button>

            <h2>Ajouter une requête:</h2>
            <form >
                <label for="clientName">Nom du client:</label>
                <input type="text" id="clientName" name="clientName" required><br><br>

                <label for="author">Auteur :</label>
                <input type="text" id="author" name="author" required><br><br>

                <input type="submit" value="Envoyer">
            </form>
        </div>
    </body>
</html>