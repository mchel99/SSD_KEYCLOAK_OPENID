//variabili per la selezione della classe e del robot
var classe = null;
var robot = null;
//variabili per il login
var user = null;
var password = null;
var classe =  null;

// Variabile per tenere traccia del bottone precedentemente selezionato
var bottonePrecedente1 = null;
// Variabile per tenere traccia del bottone precedentemente selezionato
var bottonePrecedente2 = null;

var selectedElement = null;

function Handlebuttonclass(id, button) {
  $(document).ready(function () {
    classe = id;
    console.log('Hai cliccato sul bottone delle classi con id: ' + classe);
    // Se il bottone precedentemente selezionato è diverso da null
    // allora rimuoviamo la classe highlighted
    if (bottonePrecedente1 != null) {
      bottonePrecedente1.classList.remove("highlighted");
    }
    if (button.classList.contains("highlighted")) {
      button.classList.remove("highlighted");
    } else {
      button.classList.add("highlighted");
    }

    bottonePrecedente1 = button;
  });
}

function Handlebuttonrobot(id, button) {
  $(document).ready(function () {
    robot = id;
    console.log('Hai cliccato sul bottone del robot con id: ' + robot);

    // Se il bottone precedentemente selezionato è diverso da null
    // allora rimuoviamo la classe highlighted
    if (bottonePrecedente2 != null) {
      bottonePrecedente2.classList.remove("highlighted");
    }

    if (button.classList.contains("highlighted")) {
      button.classList.remove("highlighted");
    } else {
      button.classList.add("highlighted");
    }
    bottonePrecedente2 = button;

  });
}

function redirectToPagereport() {
  console.log(classe);
  console.log(robot);
  if (classe && robot) {

    $.ajax({
      url: '/sendVariable', // L'URL del tuo endpoint sul server
      type: 'POST', // Metodo HTTP da utilizzare
      data: {
        myVariable: classe,
        myVariable2: robot
      }, // Dati da inviare al server
      success: function (response) {
        console.log('Dati inviati con successo');
        alert("Dati inviati con successo");
        // Gestisci la risposta del server qui
        window.location.href = "/report";
      },
      error: function (error) {
        console.error('Errore nell invio dei dati');
        alert("Dati non inviati con successo");
        // Gestisci l'errore qui
      }
    });
  }
  else {
    alert("Seleziona una classe e un robot");
    console.log("Seleziona una classe e un robot");
  }

}

function redirectToPagemain() {
  window.location.href = "/main";
}



function redirectToPagemainlogin() {

  user = document.getElementById("username").value;
  password = document.getElementById("password").value;
if(user && password ){
  alert("Login effettuato con successo");
  
  $.ajax({
    url:'/login-variabiles',
    type: 'POST',
    data: { 
      var1: user, 
      var2: password
    },

  })


  window.location.href = "/main";
}
else{
  alert("Inserisci username e password");
}
}

function redirectToPageeditor() {

  $.ajax({
    url:'/save-data',
    type:'POST'
  })
  window.location.href = "/editor";
}

// Funzione per gestire il click sul bottone di download
function downloadFile() {
  fileId = classe;
  if (fileId) {
    const downloadUrl = '/download';
    const formData = new FormData();
    formData.append('elementId', fileId);

    fetch(downloadUrl, {
      method: 'POST',
      body: formData
    })
      .then(function(response) {
        if (response.ok) {
          return response.blob();
        } else {
          throw new Error('Errore nella risposta del server');
        }
      })
      .then(function(blob) {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = "class.java";
        link.click();
        window.URL.revokeObjectURL(url);
      })
      .catch(function(error) {
        console.error('Errore nel download del file', error);
      });
  } else {
    console.log('Nessun file selezionato');
  }
}

function redirectToLogin() {
  window.location.href = "/login";
}

function saveLoginData() {
  var username = document.getElementById("username").value;

  localStorage.setItem("username", username);
}
