<#ftl encoding="utf-8">
<#macro menu addName>
    <link href="_menu_layout_style.css" rel="stylesheet" />
    <div class="menu">
        <button id="openFormButton">${addName}</button>
        <button id="returnToHomePageButton">Accueil</button>
        <button id="logoutButton">Se déconnecter</button>
    </div>
</#macro>