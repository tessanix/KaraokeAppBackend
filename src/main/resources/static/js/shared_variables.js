const BASE_ADDR = "192.168.0.11"  // "193.43.134.143"

export const BASE_URL = "http://"+BASE_ADDR+":8080"
export const BASE_SOCKET_URL = "ws://"+BASE_ADDR+":8080"

export function logout() {
    fetch('/logout', {method: 'GET'})
    .then( response => {
        if (response.status === 200) {
            console.log(response)
            window.location.href = response.url
        } else {
            throw new Error('Login failed')
        }
    })
    .catch(error => {console.error(error)} )
}