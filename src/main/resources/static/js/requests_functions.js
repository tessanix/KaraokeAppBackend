const socket = new WebSocket("ws://192.168.0.11:8080/websocketrequestspage")

const returnToHomePageButton = document.getElementById("returnToHomePageButton");
const openFormButton = document.getElementById("openFormButton");
const clientRequestsListContainer = document.getElementById('clientRequestsListContainer');

returnToHomePageButton.addEventListener('click', (event) => {
     window.location.href = "http://192.168.0.11:8080/login";
})

openFormButton.addEventListener("click", function() {
    addSongForm.style.display = "block";
});

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

const deleteButtons = document.querySelectorAll('.deleteRequestButton');

deleteButtons.forEach( button => {
    button.addEventListener('click', deleteListener)
  }
);

function deleteListener(event) {
    const requestId =  event.target.getAttribute('data-request-id');
    console.log(requestId)
    socket.send(JSON.stringify(
        {
          action: 'deleteRequest',
          data: { id: requestId }
        }
    ))
}



socket.onmessage = (event) => {

    const data = JSON.parse(event.data)
    console.log(data)

    if(data.action === "deleteRequest"){
        requestDeletedResponse(data.data.id)
    } else if (data.action === "addRequest"){
        addRequestToContainer(data.data.clientName, data.data.id, data.data.title, data.data.author)
    }
}


function requestDeletedResponse(requestId) {
    const deleteButtons = document.getElementsByClassName('deleteRequestButton');

    for (let i = 0; i < deleteButtons.length; i++) {
        const button = deleteButtons[i];
        const buttonRequestId = button.getAttribute('data-request-id');
        if (buttonRequestId === requestId) {
            console.log(requestId)
            const listRequestElement = button.parentNode.parentNode
            const liElement = button.parentNode
            listRequestElement.removeChild(liElement)


            if (listRequestElement.childElementCount == 0) {
                const h3Element = clientRequestsListContainer.querySelector('h3');
                clientRequestsListContainer.removeChild(listRequestElement)
                clientRequestsListContainer.removeChild(h3Element)
            }

            break;

      }
    }

}

function addRequestToContainer(clientName, requestId, title, author) {
  const clientRequestsListContainer = document.getElementById("clientRequestsListContainer");
  let requestsOfAClient = null;

  // Look for existing 'requestsOfAClient' element
  for (let i = 0; i < clientRequestsListContainer.children.length; i++) {
    const child = clientRequestsListContainer.children[i];
    if (child.tagName === "UL" && child.classList.contains("requestsOfAClient") && child.previousSibling.textContent.trim() === `${clientName} demande:`) {
      requestsOfAClient = child;
      break;
    }
  }

  // If 'requestsOfAClient' not found, create it along with the h3 title
  if (!requestsOfAClient) {
    const h3 = document.createElement("h3");
    h3.textContent = `${clientName} demande:`;
    requestsOfAClient = document.createElement("ul");
    requestsOfAClient.classList.add("requestsOfAClient");
    clientRequestsListContainer.appendChild(h3);
    clientRequestsListContainer.appendChild(requestsOfAClient);
  }

  // Create new 'request' element and append to 'requestsOfAClient'
  const requestElement = document.createElement("li");
  requestElement.classList.add("request");
  requestElement.dataset.requestId = requestId

  const requestText = document.createElement("p");
  requestText.classList.add("request_element");
  requestText.textContent = `${title} | ${author}`;

  const deleteButton = document.createElement("button");
  deleteButton.classList.add("deleteRequestButton");
  deleteButton.dataset.clientName = clientName;
  deleteButton.dataset.requestId = requestId
  deleteButton.textContent = "Supprimer";

  requestElement.appendChild(requestText);
  requestElement.appendChild(deleteButton);
  requestsOfAClient.appendChild(requestElement);
}


