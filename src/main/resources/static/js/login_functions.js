import { BASE_URL, logout } from './shared_variables.js'


const loginButton = document.getElementById("login-button")
const loginForm = document.getElementById("login-form")
const contentWhenConnected = document.getElementById("contentWhenConnected")
const goToSongsPageButton = document.getElementById("goToSongsPageButton")
const goToRequestsPageButton = document.getElementById("goToRequestsPageButton")
const logoutButton = document.querySelector('#logoutButton')


goToSongsPageButton.addEventListener('click', () => {
    window.location.href = BASE_URL+'/songs'
})

goToRequestsPageButton.addEventListener('click', () => {
    window.location.href = BASE_URL+'/requests'
})

logoutButton.addEventListener('click', logout)

loginButton.addEventListener('click', (event) => {

    event.preventDefault() // Prevent the form from submitting
    const username = document.getElementById('username').value
    const password = document.getElementById('password').value

    fetch('/send_login', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: `username=${username}&password=${password}`
    })
    .then( response => {
        if (response.status === 200) {
            console.log(response)
            contentWhenConnected.style.display = 'block'
            loginForm.style.display = 'none'
        } else {
            throw new Error('Login failed')
        }
    })
    .catch(error => {console.error(error)} )
})

// if user has a session contentWhenConnected is displayed :
window.addEventListener('load', () => {
    fetch('session')
    .then( response => {
        if (response.status === 200) {
            contentWhenConnected.style.display = 'block'
            loginForm.style.display = 'none'
        }
    })
    .catch( error => {
      console.error('Session doesn\'t exist or is expired:', error)
    })
})
