function fetchData(){
    fetch('/api/posts')
        .then(function(response) {
            return response.json();
        })
        .then(function(myJson) {
            allData = myJson;
        })
        .catch(function(error) {
            console.log('There has been a problem with your fetch operation: ', error.message);
        });
}

fetchData();

async function signIn(){

    let userName = document.getElementById('username').value;
    let password = document.getElementById('password').value;

    try {
        const response = await fetch("/api/players?username="+userName+"&password="+password, {
            method: "POST",
            credentials: 'include',
            headers: {
                'Accept' : 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({userName: userName, password: password})
        });
        const data = await response.json();

        if (response.status === 409 || response.status === 403 || response.status === 401 || response.status === 500) {
            console.log(data.error);
            console.log(response);
        }else {
            console.log("Signed up!", data);
        }
    } catch(error) {
        alert(error);
    }
}

async function logIn(){
    let loginName = document.getElementById('loginName').value;
    let loginPassword = document.getElementById('loginPassword').value;
    try {
        const response = await fetch("/api/login", {
            method: "POST",
            credentials: 'include',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body:`username=${loginName}&password=${loginPassword}`,
        });
        if (response.status === 200) {
          location.reload();
        } else {
            return Error;
        }
    } catch(error){
        alert(error);
        console.log("error", error);
    }

}

async function logOut(){
    try {
        const response = await fetch("/api/logout", {
            method: "POST",
            credentials: 'include',
            headers: {
                'Accept': 'application/json',
                'Content-type': 'application/json',
            }
        });
        if(response.status === 200){
            location.reload();
        }
    } catch(error){
        alert(error);
    }
}








