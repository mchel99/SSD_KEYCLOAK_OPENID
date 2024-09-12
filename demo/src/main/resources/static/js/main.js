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
  var username = document.getElementById("username").value;
  var password = document.getElementById("password").value;

  // Controlla se i campi username e password sono stati riempiti
  if (username && password) {
    // Creazione della stringa con i dati codificati per URL
    var formData = new URLSearchParams();
    formData.append("var1", username);
    formData.append("var2", password);

    // Invio della richiesta POST con fetch
    fetch('login-variabiles', {
      method: 'POST',  // Metodo HTTP
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'  // Indica che i dati sono codificati come URL
      },
      body: formData.toString()  // Conversione dei dati in stringa URL-encoded
    })
    .then(response => {
      // Verifica se la risposta del server è OK (status 200-299)
      if (!response.ok) {
        throw new Error('Errore nella risposta del server: ' + response.status);
      }
      return response.text();  // Restituisce la risposta in formato testo
    })
    .then(data => {
      // Controllo sulla risposta testuale del server
      if (data === "success") {  // Se il server ha risposto con "success"
        alert("Login effettuato con successo!");
        window.location.href = "/welcome";  // Reindirizzamento alla pagina di benvenuto
      } else {
        alert("Username o password non validi.");  // Risposta diversa da "success"
      }
    })
    .catch(error => {
      // Gestione degli errori durante la richiesta
      console.error('Errore:', error);
      alert("Errore durante il login: " + error.message);
    });
  } else {
    // Se i campi non sono stati riempiti
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

function redirectToSignupPage() {
  console.log('redirectToSignupPage function called');
  window.location.href ="/signup";
}