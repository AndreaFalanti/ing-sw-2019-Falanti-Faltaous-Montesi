# Progetto Ingegneria Softaware

> Realizzazione del gioco da tavolo Adrenalina in formato digitale con Java.

### Istruzioni per avviare il server:

* Scompattare lo zip adrenalina-server.zip.
* Avviare attraverso comando "java -jar" adrenalina-server.jar passando come parametri:
    1. porta per connessioni socket
    2. porta per connessioni rmi
* È possibile configurare il server cambiando i parametri dell'apposito file di configurazione estratto dallo zip. I possibile parametri modificabili sono:
    1. boardNum: valore da 1 a 4 per selezionare la desiderata configurazione della tavola da gioco.
    2. killNum: valore da 1 a 8 per indicare il numero di kill dopo il quale il gioco entrerà in final frenzy.
    3. timerDelay: valore in millisecondi dopo il quale una partita con almeno 3 giocatori starta se non al completo.
    4. useControllerTimer: valore booleano.

### Istruzioni per avviare il client grafico

* Scompattare lo zip adrenalina-server.zip.
* Avviare attraverso comando "java -jar adrenalina-gui.jar".
* è possibile configurare i parametri di connessione del client grafico cambiando i parametri dell'apposito file di configurazione estratto dallo zip. I parametri nel file sono:
    1. host: ip del server.
    2. socketPort: porta di connessione socket.
    3. rmiPort: porta di connessione rmi.


### Istruzioni per avviare il client grafico

* Avviare attraverso comando "java -jar adrenalina-cli.jar".
* È possibile configurare i parametri di connessione del client grafico cambiando i parametri dell'apposito file di configurazione estratto dallo zip. I parametri nel file sono:
    1. host: ip del server.
    2. socketPort: porta di connessione socket.
    3. rmiPort: porta di connessione rmi.
