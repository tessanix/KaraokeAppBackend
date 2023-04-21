const loginButton = document.getElementById("login-button");
const loginForm = document.getElementById("login-form");
const connectedContent = document.getElementById("connectedContent");
const goToSongsPageButton = document.getElementById("goToSongsPageButton")
const goToRequestsPageButton = document.getElementById("goToRequestsPageButton")

loginButton.addEventListener('click', (event) => {

    event.preventDefault(); // Prevent the form from submitting
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    fetch('/send_login', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: `username=${username}&password=${password}`
    })
    .then( response => {
        if (response.status === 200) {
            console.log(response)
            connectedContent.style.display = 'block';
            loginForm.style.display = 'none'
        } else {
            throw new Error('Login failed');
        }
    })
    .catch(error => {
        console.error(error);
    });
});

goToSongsPageButton.addEventListener('click', (event) => {
     window.location.href = "http://192.168.0.11:8080/songs"
})

goToRequestsPageButton.addEventListener('click', (event) => {
     window.location.href = "http://192.168.0.11:8080/requests" //193.43.134.143
})

window.addEventListener('load', () => {
    fetch('session')
    .then( response => {
        if (response.status === 200) {
            connectedContent.style.display = 'block'
            loginForm.style.display = 'none'
        }
    })
    .catch( error => {
      console.error('There was a problem fetching the data:', error);
    });
});


