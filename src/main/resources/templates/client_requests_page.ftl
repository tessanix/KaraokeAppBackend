<#ftl encoding="utf-8">
<#import "_menu_layout.ftl" as menu_layout />
<!DOCTYPE html>
<html lang="eng">
    <head>
        <meta charset="UTF-8">
        <title>Requêtes des clients</title>
        <link href="requests_style.css" rel="stylesheet" />
        <script defer type="module" src="requests_functions.js"></script>
    </head>

    <body>

        <@menu_layout.menu addName="Ajouter une requête">
        </@menu_layout.menu>

        <div class="content">
            <h1>Requêtes des clients</h1>
            <div id="clientRequestsListContainer">
                    <#list clientRequestsMap?keys as clientName>
                        <h3>${clientName}</h3>
                        <ul class="requestsOfAClient" data-client-name=${clientName}>
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
                <input type="text" id="clientName" name="clientName" required><br>

                <label for="title">Titre :</label>
                <input type="text" id="title" name="title"><br>

                <label for="author">Auteur :</label>
                <input type="text" id="author" name="author"><br>

                <input type="submit" value="Envoyer">
            </form>
        </div>
    </body>
</html>