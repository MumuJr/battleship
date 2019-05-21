let game;
let playerOne;
let playerTwo;


var gameViewURL = "http://localhost:8080/api/game_view/";
let searchParams = new URLSearchParams(location.search);
let gameViewValue = searchParams.get('gp');



function fetchData(gameid){
    fetch(gameViewURL + gameid)
        .then(function(response) {
            return response.json();
        })
        .then(function(myJson) {
            game = myJson;
            console.log(game);
        })
        .then(function (){
            displayVsPlayers();
            whoIsPlaying();

            createTable("Player Ships Table", "shipsTable");
            placeShips();

            createTable("Player One Salvo Table", "salvoTable");
            fireSalvo();

            isMyShipHit();


        })
        .catch(function(error) {
            console.log('There has been a problem with your fetch operation: ', error.message);
        });
}

fetchData(gameViewValue);

function displayVsPlayers(){

    playerOne = game.gamePlayers.find(gp => gp.id === Number(gameViewValue)).player.firstName;


    playerTwo = game.gamePlayers.find(gp => gp.id !== Number(gameViewValue))
        ? game.gamePlayers.find(gp => gp.id !== Number(gameViewValue)).player.firstName
        : "Nobody!";


    console.log(playerOne);
    console.log(playerTwo);
}

function whoIsPlaying() {

    let li = document.createElement("LI");
    let textnode = document.createTextNode(playerOne.toString() + " (You) vs " + playerTwo.toString() );
    li.appendChild(textnode);
    document.getElementById('playerTable').appendChild(li);


}

function createTable(text1, text2){

    var column = [0,1,2,3,4,5,6,7,8,9,10];
    var cell = [1,2,3,4,5,6,7,8,9,10];
    var row = ["A","B","C","D","E","F","G","H","I","J"]

    var result = "<table border='1' class='table'>";
    result += "<p>" + text1 + "</p>";

    column.forEach(column => {
        result += "<td>" + column + "</td>";
    });

    row.forEach(row => {
        result += "<tr>"
        result += "<td>" + row.toString() + "</td>";
        cell.forEach(cell =>{
            result += `<td class=${row+cell}></td>`;
        })
        result += "</tr>"
    });

    result += "</table>";

    document.getElementById(text2).innerHTML = result;

}




function placeShips(){
    game.ships.forEach(ship => ship.location
        .forEach(loc => document.querySelectorAll('#shipsTable .' + loc)
            .forEach(cell => cell.classList.add("shipPlaced")))
    );
}


function isMyShipHit(){

    game.salvo.forEach(enemySalvo => { if(Number(gameViewValue) !== enemySalvo.playerId)
        enemySalvo.location
            .forEach(loc => document.querySelectorAll('#shipsTable .' + loc)
                .forEach(cell => cell.classList.add("shipHit") + (cell.innerHTML = enemySalvo.turn)))
    });

}


function fireSalvo(){
    game.salvo.forEach(salvos => { if(Number(gameViewValue) === salvos.playerId){
        salvos.location
            .forEach(loc => document.querySelectorAll('#salvoTable .' + loc)
                .forEach(cell => cell.classList.add("salvoShot") + (cell.innerHTML = salvos.turn)))}
                else {
                    console.log("No Salvos!");
        }
        }
    )
}







