var games;
let gamePlayers = [];
let gameDate = [];


function fetchData(){
    fetch('/api/games')
        .then(function(response) {
            return response.json();
        })
        .then(function(myJson) {
            games = myJson;
        })
        .then(function (){
            addData();
            displayData();
        })
        .catch(function(error) {
            console.log('There has been a problem with your fetch operation: ', error.message);
        });
}

fetchData();

function addData(){
    games.forEach(addPlayers => {gamePlayers.push(addPlayers.gamePlayers)});
    games.forEach(addDate => {gameDate.push(addDate.date)});
}


function displayData(){

    gamePlayers.forEach(game => {
        var gamelist = document.createElement("LI");
        document.getElementById("gameData").appendChild(gamelist);
        gamelist.innerHTML = game[0].created + ": " + game[0].player.userName + ", " + game[1].player.userName});
}



// Display Email :  games[0].gamePlayers[0].player.userName
// Display Created: games[0].gamePlayers[0].created

