function fetchData(){
    fetch('/api/games')
        .then(function(response) {
            return response.json();
        })
        .then(function(myJson) {
            allData = myJson;
        })
        .then(function (){
            addData();
            displayData();
            showLeaderTable();
        })
        .catch(function(error) {
            console.log('There has been a problem with your fetch operation: ', error.message);
        });
}

fetchData();

async function signIn(){

    let userName = document.getElementById('username').value;
    let password = document.getElementById('password').value;

    console.log(typeof(userName));
    console.log(typeof(password));
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


var allData;
let gamePlayers = [];
let gameDate = [];
var individualPlayer = [];


function addData(){
    allData.games.forEach(addGamePlayers => {gamePlayers.push(addGamePlayers.gamePlayers)});
    allData.games.forEach(addDate => {gameDate.push(addDate.date)});

}


function displayData(){

    allData.games.map(game => {
        var gamelist = document.createElement("LI");
        document.getElementById("gameData").appendChild(gamelist);


        if (game.gamePlayers.length === 2) {

            gamelist.innerHTML = game.date + " : " + game.gamePlayers[0].player.userName + ", " + game.gamePlayers[1].player.userName
        } else if (game.gamePlayers.length === 1 && isAutOk(game.gamePlayers, allData.player)){
            console.log("Second if");
            gamelist.innerHTML = game.date + " : " + game.gamePlayers[0].player.userName + ` ` +`<button> Join Game </button>`
        }else if (game.gamePlayers.length === 2 && isAutOk(game.gamePlayers, allData.player) === false) {
            console.log("Third if");
            gamelist.innerHTML = game.date + " : " + game.gamePlayers[0].player.userName + ", " + game.gamePlayers[1].player.userName
        }else if (game.gamePlayers.length === 2 && allData.player === null){
            console.log("Fourth if");
            gamelist.innerHTML = game.date + " : " + game.gamePlayers[0].player.userName + ", " + game.gamePlayers[1].player.userName
        }else if (game.gamePlayers.length === 1 && allData.player === null){
            console.log("Fifth if");
            gamelist.innerHTML = game.date + " : " + game.gamePlayers[0].player.userName
        }
    });
}

function isAutOk(gamePlayers, player) {
    if(player) {
        return gamePlayers.map(gameplayer => gameplayer.player.id).includes(player.id);
    } else {
        return false;
    }
}


function showLeaderTable(){

    individualPlayer = allData.games.flatMap(g => g.gamePlayers.map(gp => gp.player));

    document.getElementById('table').innerHTML += individualPlayer.filter((player, index, self) =>
        index === self.findIndex((thisPlayer) => (
            thisPlayer.id === player.id && thisPlayer.firstName === player.firstName))
    ).map(player => {
        return `    <tr>
                        <td>Name</td>
                        <td>Total Score</td>
                        <td>Win</td>
                        <td>Tie</td>
                        <td>Loss</td>
                    </tr>                
                    <tr>
                        <td>${player.firstName} ${player.lastName}</td>
                        <td>${player.score}</td>
                        <td>${player.win}</td>
                        <td>${player.tie}</td>
                        <td>${player.loss}</td>
                    </tr> `
    }).join('');
}

