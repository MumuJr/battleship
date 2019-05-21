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
            pushIndividualPlayer();

        })
        .catch(function(error) {
            console.log('There has been a problem with your fetch operation: ', error.message);
        });
}

fetchData();

var games;
let gamePlayers = [];
let gameDate = [];
var individualPlayer = [];


function addData(){
    games.forEach(addGamePlayers => {gamePlayers.push(addGamePlayers.gamePlayers)});
    games.forEach(addDate => {gameDate.push(addDate.date)});

}


function displayData(){



    gamePlayers.forEach(game => {
        var gamelist = document.createElement("LI");
        document.getElementById("gameData").appendChild(gamelist);

        if (game.length === 2) {

        gamelist.innerHTML = game[0].created + " : " + game[0].player.userName + ", " + game[1].player.userName
    } else {
        gamelist.innerHTML = game[0].created + " : " + game[0].player.userName
    }
    });

}


function pushIndividualPlayer(){

    individualPlayer = games.flatMap(g => g.gamePlayers.map(gp => gp.player));

    document.getElementById('table').innerHTML += individualPlayer.filter((player, index, self) =>
        index === self.findIndex((thisPlayer) => (
            thisPlayer.id === player.id && thisPlayer.firstName === player.firstName))
    ).map(player => {
        return `                    
                    <tr>
                    <td>${player.firstName} ${player.lastName}</td>
                    <td>${player.score}</td>
                    <td>${player.win}</td>
                    <td>${player.tie}</td>
                    <td>${player.loss}</td> `
    }).join('');
}
