<!DOCTYPE html>
<html lang="eng">
    <head>
        <title>Songs by Category</title>
        <link href="songs_style.css" rel="stylesheet" />
        <script defer src="songs_functions.js"></script>
    </head>

    <body>
        <div class="menu">
            <button id="openFormButton">Ajouter un son</button>
            <button id="returnToHomePageButton">Accueil</button>
            <button id="logoutButton">Se déconnecter</button>
        </div>

        <div class="content">
            <h1>Musiques par catégories</h1>
            <div id="songsMapContainer">
                <ul>
                    <#list songsMapByCategories?keys as category>
                        <li class="category">${category}</li>
                            <ul class="songList">
                            <#list songsMapByCategories[category] as song>
                                <li>
                                    ${song.title} by ${song.author}
                                    <button class="deleteSongButton" data-song-id="${song.id}" data-category="${category}">Supprimer</button>
                                </li>
                            </#list>
                        </ul>
                    </#list>
                </ul>
            </div>
        </div>

        <div id="addSongForm">
            <button id="closeFormButton">X</button>

            <h2>Formulaire de contact</h2>
            <form>
                <label for="title">Titre :</label>
                <input type="text" id="title" name="title" required><br><br>

                <label for="author">Auteur :</label>
                <input type="text" id="author" name="author" required><br><br>

                <select name="category">
                    <#list songsMapByCategories?keys as category>
                        <option value="${category}">${category}</option>
                    </#list>
                </select>

                <input type="submit" value="Envoyer">
            </form>
        </div>
    </body>
</html>