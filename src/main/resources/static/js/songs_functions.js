const socket = new WebSocket("ws://192.168.0.11:8080/websocketsongspage")

const openFormButton = document.getElementById("openFormButton");
const closeFormButton = document.getElementById("closeFormButton");
const returnToHomePageButton = document.getElementById("returnToHomePageButton");

openFormButton.addEventListener("click", function() {
    addSongForm.style.display = "block";
});

closeFormButton.addEventListener("click", function() {
    addSongForm.style.display = "none";
});

returnToHomePageButton.addEventListener('click', (event) => {
     window.location.href = "http://192.168.0.11:8080/login";
})

document.querySelector('#logoutButton').addEventListener('click', (event) => {
    fetch(
        '/logout',
        {method: 'GET'}
    )
    .then( response => {
         if (response.status === 200) {
             console.log(response)
             window.location.href = response.url;
         } else {
             throw new Error('Login failed');
         }
    })
     .catch(error => {
         console.error(error);
     });

})


const deleteButtons = document.querySelectorAll('.deleteSongButton');

deleteButtons.forEach( button => {
    button.addEventListener('click', deleteListener)
  }
);

function deleteListener(event) {

    if (confirm('Êtes-vous sûr de vouloir supprimer cette chanson ?')) {
    const songId =  event.target.getAttribute('data-song-id');
    const category =  event.target.getAttribute('data-category');
    //console.log(" delete button pressed : song id: " + songId + ", category : " + category )

        socket.send(JSON.stringify(
            {
              action: 'deleteSong',
              data: {
                  category: category,
                  id: songId
              }
            }
        ))
    }
}


document.querySelector('#addSongForm').addEventListener('submit', function(event) {
      event.preventDefault(); // Empêche le comportement par défaut d'envoyer le formulaire à une nouvelle page
      const formData = new FormData(event.target); // Récupère les données du formulaire

      socket.send(JSON.stringify(
      {
          action: 'addSong',
          data: {
              category: formData.get('category'),
              title: formData.get('title'),
              author: formData.get('author')
          }
      }
      ))
});

function songDeletedResponse(category, songId) {
    //console.log('Song deleted successfully!');
    const deleteButtons = document.getElementsByClassName('deleteSongButton');

    for (let i = 0; i < deleteButtons.length; i++) {
        const button = deleteButtons[i];
        const buttonSongId = button.getAttribute('data-song-id');
        if (buttonSongId === songId) {
            const listSongsElement = button.parentNode.parentNode
            const liElement = button.parentNode
            //console.log(liElement)
            listSongsElement.removeChild(liElement)
            break;
      }
    }

}


function songAddedResponse(category, id, title, author) {
    const categoryElements = document.getElementsByClassName("category");
    for (let i = 0; i < categoryElements.length; i++) {
        if (categoryElements[i].textContent === category) {
            const songListElement = categoryElements[i].nextElementSibling;

            // Create a new li element
            const liElement = document.createElement('li');
            //console.log(`${title} ${author}`)
            liElement.textContent = `${title} ${author}`;

            // Create a new button element
            const buttonElement = document.createElement('button');
            buttonElement.classList.add('deleteSongButton');
            buttonElement.dataset.songId = id; // Replace with the actual song ID
            buttonElement.dataset.category = category;
            buttonElement.textContent = 'Supprimer';

            buttonElement.addEventListener('click', deleteListener)

            // Append the button element to the li element
            liElement.appendChild(buttonElement);

            // Append the li element to the song list element
            songListElement.appendChild(liElement);
            break;
        }
    }
}

socket.onmessage = function(event) {

    const data = JSON.parse(event.data)
   // console.log(data)

    if(data.action === "deleteSong"){
        songDeletedResponse(data.data.category, data.data.id)
    } else if (data.action === "addSong"){
        songAddedResponse(data.data.category, data.data.id, data.data.title, data.data.author)
    }
}

socket.onclose = function(event) {
  console.log("WebSocket closed with code " + event.code + " and reason " + event.reason)
}

socket.onerror = function(error) {
  console.error("WebSocket error:", error)
}



var categories = document.querySelectorAll('.category');
categories.forEach((category) => {
    category.addEventListener('click', function() {
        //console.log("A songList have been clicked!")
        var songList = this.nextElementSibling;
        var display = window.getComputedStyle(songList).getPropertyValue('display')

        if (display === 'none') {
            songList.style.display = 'block';
        } else {
            songList.style.display = 'none';
        }
    }
    );
});
