<#ftl encoding="utf-8">
<#import "_menu_layout.ftl" as menu_layout />
<!DOCTYPE html>
<html lang="eng">
    <head>
        <meta charset="UTF-8">
        <title>Sons par Categories</title>
        <link href="songs_style.css" rel="stylesheet" />
        <script defer type="module" src="songs_functions.js"></script>
    </head>

    <body>
        <@menu_layout.menu addName="Ajouter un son">
        </@menu_layout.menu>


        <div class="content">
            <h1>Musiques par catégories</h1>

            <input type="text" id="searchInput" placeholder="Rechercher un song...">
            
            <div id="songsMapContainer">
                <ul>
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
                </select>

                <input type="submit" value="Envoyer">
            </form>
        </div>
    </body>
    
</html>