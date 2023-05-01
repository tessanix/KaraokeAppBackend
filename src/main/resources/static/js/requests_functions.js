import { BASE_URL, BASE_SOCKET_URL, logout } from './shared_variables.js'

const socket = new WebSocket(BASE_SOCKET_URL+"/websocketrequestspage")

const returnToHomePageButton = document.getElementById("returnToHomePageButton")
const openFormButton = document.getElementById("openFormButton")
const logoutButton = document.querySelector('#logoutButton')
let deleteButtons = document.querySelectorAll('.deleteRequestButton')
const addRequestForm = document.getElementById('addRequestForm')

returnToHomePageButton.addEventListener('click', () => {
    window.location.href = BASE_URL+"/login"
})

openFormButton.addEventListener("click", () => {
    addRequestForm.style.display = "block"
})

logoutButton.addEventListener('click', logout)


deleteButtons.forEach( button => {
    button.addEventListener('click', deleteListener)
})

function deleteListener(event) {
    const requestId =  event.target.getAttribute('data-request-id')
    socket.send(JSON.stringify(
        {
          action: 'deleteRequest',
          data: { id: requestId }
        }
    ))
}


addRequestForm.addEventListener('submit', (event) => {
    event.preventDefault() // Empêche le comportement par défaut d'envoyer le formulaire à une nouvelle page
    const formData = new FormData(event.target) // Récupère les données du formulaire

    socket.send(JSON.stringify(
    {
        action: 'addRequest',
        data: {
            clientName: formData.get('clientName'),
            title: formData.get('title'),
            author: formData.get('author')
        }
    }
    ))
})


socket.onmessage = (event) => {

    const data = JSON.parse(event.data)
    //console.log(data)
    if(data.action === "deleteRequest"){
        removeRequestElementById(data.data.id)
    } else if (data.action === "addRequest"){
        addRequestElementOfAClient(data.data.clientName, data.data.id, data.data.title, data.data.author)
    }
}


function removeRequestElementById(requestId) {
    // Find the <li> element with the given request ID
    const liElement = document.querySelector(`li.request[data-request-id="${requestId}"]`)
    if (liElement) {
      // Find the parent <ul> element and remove the <li> element
      const ulElement = liElement.parentNode
      ulElement.removeChild(liElement)
  
      // If the <ul> element is empty, remove it and the corresponding <h3> element
      if (ulElement.children.length === 0) {
        const h3Element = ulElement.previousElementSibling
        ulElement.parentNode.removeChild(h3Element)
        ulElement.parentNode.removeChild(ulElement)
      }
    }
}


function addRequestElementOfAClient(clientName, requestId, title, author) {
    // Check if the <ul> element already exists for the given clientName
    let ulElement = document.querySelector(`ul.requestsOfAClient[data-client-name="${clientName}"]`)

  if (!ulElement) {
    // If the <ul> element doesn't exist, create a new one
    ulElement = document.createElement('ul')
    ulElement.classList.add('requestsOfAClient')
    ulElement.setAttribute('data-client-name', clientName)

    const containerElement = document.getElementById('clientRequestsListContainer')
    const h3Element = document.createElement('h3')
    h3Element.textContent = clientName
    containerElement.appendChild(h3Element)
    containerElement.appendChild(ulElement)
  }

  // Add a new <li> element to the <ul> element
  const liElement = document.createElement('li')
  liElement.classList.add('request')
  liElement.setAttribute('data-request-id', requestId)

  const pElement = document.createElement('p')
  pElement.classList.add('request_element')
  pElement.textContent = `${title} | ${author}`

  const buttonElement = document.createElement('button')
  buttonElement.classList.add('deleteRequestButton')
  buttonElement.setAttribute('data-client-name', clientName)
  buttonElement.setAttribute('data-request-id', requestId)
  buttonElement.textContent = 'Supprimer'
  buttonElement.addEventListener('click', deleteListener)

  liElement.appendChild(pElement)
  liElement.appendChild(buttonElement)
  ulElement.appendChild(liElement)
}
