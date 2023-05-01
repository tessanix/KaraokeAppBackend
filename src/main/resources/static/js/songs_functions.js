import { BASE_URL, logout } from './shared_variables.js'

const openFormButton = document.getElementById("openFormButton")
const closeFormButton = document.getElementById("closeFormButton")
const returnToHomePageButton = document.getElementById("returnToHomePageButton")
const selectElement = document.querySelector('select[name="category"]');

let songsMapByCategoriesJS = {}

fetch(BASE_URL+"/musics")
.then(response => response.json())
.then( data => {
  songsMapByCategoriesJS = data
  displayAllSongs(songsMapByCategoriesJS)
  addCategoryOptionsToForm()
})

function addCategoryOptionsToForm(){
  for (const category in songsMapByCategoriesJS) {
    const optionElement = document.createElement('option')
    optionElement.value = category
    optionElement.textContent = category
    selectElement.appendChild(optionElement)
  }
}

openFormButton.addEventListener("click", function() {
    addSongForm.style.display = "block"
})

closeFormButton.addEventListener("click", function() {
    addSongForm.style.display = "none"
})

returnToHomePageButton.addEventListener('click', () => {
    window.location.href = BASE_URL+"/login"
})

document.querySelector('#logoutButton').addEventListener('click', logout)


const deleteButtons = document.querySelectorAll('.deleteSongButton');

deleteButtons.forEach( button => {
    button.addEventListener('click', deleteListener)
  }
)

function deleteListener(event) {

  if (confirm('Êtes-vous sûr de vouloir supprimer cette chanson ?')) {
    const songId =  event.target.getAttribute('data-song-id')
    const category = event.target.getAttribute('data-category')
    fetch(
      BASE_URL + `/deleteSong?songId=${songId}`, 
      { method: 'DELETE' }
    )
    .then(response => {
      if (response.ok) {
        console.log(`Song with id ${songId} has been deleted successfully`)
        songDeletedResponse(songId, category)
      } else {
        throw new Error('An error occurred while deleting the song');
      }
    })
    .catch(error => {
      console.error('Error:', error);
    });
  }
}



document.querySelector('#addSongForm').addEventListener('submit', function(event) {
    event.preventDefault() // Empêche le comportement par défaut d'envoyer le formulaire à une nouvelle page
    const formData = new FormData(event.target) // Récupère les données du formulaire

    fetch(
        BASE_URL + `/addNewSong`, 
        { 
            method: 'POST' ,
            body : formData
        }
    )
    .then(response => {
        if (response.ok) {
            return response.json()
        }else {
            throw new Error('An error occurred while deleting the song')
        }
    })
    .then( data => {
        console.log(`Song has been added successfully`)
        console.log(`song id : ${data.id}`)
        songAddedResponse(formData.get('category'), data.id, formData.get('title'), formData.get('author')) 
    })
    .catch(error => { console.error('Error:', error) } )
})

function songDeletedResponse(songId, category) {
    for(let i=0; i < songsMapByCategoriesJS[category].length; i++){
        if(songsMapByCategoriesJS[category][i].songId === songId){
            songsMapByCategoriesJS[category].splice(i,1)
            break
        }
    }
    
    const deleteButtons = document.getElementsByClassName('deleteSongButton')

    for (let button of deleteButtons) {
        const buttonSongId = button.getAttribute('data-song-id')
        if (buttonSongId === songId) {
            const listSongsElement = button.parentNode.parentNode
            const liElement = button.parentNode
            listSongsElement.removeChild(liElement)
            break
       }
    }
}


function songAddedResponse(category, id, title, author) {
    songsMapByCategoriesJS[category].push({"songId":id, "title":title, "author":author})

    const categoryHeaderLiElements = document.querySelectorAll('li.category')

    for( let categoryHeaderElement of categoryHeaderLiElements ){

        if(categoryHeaderElement.firstChild.textContent.trim() === category){

            const songListElement = categoryHeaderElement.children[1]
            console.log(songListElement)
            const liElement = document.createElement('li')
            liElement.textContent = `${title} ${author}`

            const buttonElement = document.createElement('button')
            buttonElement.classList.add('deleteSongButton')
            buttonElement.dataset.songId = id // Replace with the actual song ID
            buttonElement.dataset.category = category
            buttonElement.textContent = 'Supprimer'

            buttonElement.addEventListener('click', deleteListener)

            // Append the button element to the li element
            liElement.appendChild(buttonElement)

            // Append the li element to the song list element
            songListElement.appendChild(liElement)
            break
       }
    }
}

//////////////// SEARCH BAR QUERY /////////////////


function displayAllSongs(data) {
    let categoryHeaderLiElements = document.querySelectorAll(`#songsMapContainer > ul > li.category`)

    for (const category in data) {

        let categoryHeaderLiElement = null
        for (let categoryHeaderLiElementItem of categoryHeaderLiElements) {
            if (categoryHeaderLiElementItem.firstChild.firstChild.textContent.trim() === category) {
            categoryHeaderLiElement = categoryHeaderLiElementItem
            break
            }
        }
        if(!categoryHeaderLiElement){
            const textContentElement = document.createElement('span')
            textContentElement.textContent = category
            textContentElement.addEventListener('click', displayListOfSongs)

            categoryHeaderLiElement = document.createElement('li')
            categoryHeaderLiElement.classList.add("category")
            
            const ul = document.querySelector('#songsMapContainer ul')

            categoryHeaderLiElement.appendChild(textContentElement)
            ul.appendChild(categoryHeaderLiElement)
        }

        let songListElement = categoryHeaderLiElement.children[1]

        if(!songListElement){
            songListElement = document.createElement('ul')
            songListElement.classList.add("songList")
            categoryHeaderLiElement.appendChild(songListElement)
        }

        for(const song of data[category]){

            const liElement = document.createElement('li')
            liElement.textContent = song.title+" "+song.author

            // Create a new button element
            const buttonElement = document.createElement('button')
            buttonElement.classList.add('deleteSongButton')
            buttonElement.dataset.songId = song.id // Replace with the actual song ID
            buttonElement.dataset.category = category
            buttonElement.textContent = 'Supprimer'

            buttonElement.addEventListener('click', deleteListener)

            liElement.appendChild(buttonElement)

            songListElement.appendChild(liElement)
        }
    }
}

function displayListOfSongs() {
    const songList = this.nextElementSibling
    var display = window.getComputedStyle(songList).getPropertyValue('display')
    
    if (display === 'none') { songList.style.display = 'block'} 
    else { songList.style.display = 'none'}
}



const searchInput = document.querySelector('#searchInput');
const songsContainer = document.querySelector('#songsMapContainer')

searchInput.addEventListener('input', event => {
  //event.preventDefault()
  const searchQuery = event.target.value.toLowerCase()
  if(searchQuery.trim().length > 0){
    console.log("query: "+searchQuery)
    songsContainer.firstElementChild.innerHTML = ''
    const filteredSongs = filterSongsByQuery(songsMapByCategoriesJS, searchQuery)
    console.log("filtered songs: "+ filteredSongs)

    displayAllSongs(filteredSongs)
  }
})


function filterSongsByQuery(songsObject, query) {
  // convert the object to an array of key-value pairs
    const entries = Object.entries(songsObject);
    let filteredObject = {}
    entries.forEach( ([category, songs]) => {

        const filteredSongs = songs.filter( song => {
            const title = song.title.toLowerCase()
            const author = song.author.toLowerCase()
            const both1 = title+author
            const both2 = title+" "+author
            return both1.includes(query) || both2.includes(query)
        })

        if(filteredSongs.length > 0){
            filteredObject[category] = filteredSongs
        }
    })
    return filteredObject
}
