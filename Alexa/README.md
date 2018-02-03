# Alexa Application - Mobile Applications for Public Health :: Gruppe 4
https://developer.amazon.com/de/alexa-skills-kit/tutorials/fact-skill-1

## Alexa Skill entwickeln
1. Ablaufdesign des User Voice Interface
2. Set Up the Skill in the Developer Portal
2. Login in der Amazon Konsole und Erstellen des Skills im Developer Portal https://developer.amazon.com/edw/home.html#/
3. Neuen Alexa Skill Erstellen
4. Sprache (Deutsch) auswählen
5. Invocation Name zum aufrufen des Skills festlegen. Hier "Alexa öffne meine Stimmung"
4. Interaction Model anfertigen mit dem Builder oder einfügen einer JSON Datei (wie in diesem Projekt)
5. ARN Endpunkt im Alexa Skill eingeben, um den Skill mit der Lambda funktion zu verknüfen
6. Testen der Funktionalität


## AWS Lambda Funktion entwickeln
1. Einloggen in die AWS Management Konsole
2. Lambda als Service auswählen
3. Als Region US East (N. Virginia) auswählen4.
4. Alexa Node.js Blueprint auswählen
5. Trigger auf Alexa Skills Kit setzen
6. Funktion benennen
7. Node.js Datei als Funktionscode einfügen
8. Custom Role anlegen und einfügen, hier DB-All-Access auswählen für Access zu der DynamoDB
10. ARN Nummer kopieren und in Alexa Skill einfügen

## Dokumentation Interaction Model
* Anfragen werden durch Intents behandelt
* Argumente der Intents sind Slots
* Sample Utterances bilden die Worte ab, die ein Nutzer sagen kann um mit dem Alexa Skill zu interagieren
* Das Dialog model identifiziert Informationen, die der Skill braucht und kann diese in einem Gespräch mit dem Nutzer abfragen.



## Ablaufdesign
* User:  Alexa, öffne meine Stimmung.  
* Alexa: Hallo! Schön dass du da bist! Willst du mit deinem Stimmungstest beginnen?  
* User:  Ja
* Alexa: OK. Ich stelle dir nun 5 Fragen zu deiner heutigen Stimmung. Wie häufig warst du heute froh und guter Laune?  
* User:  Immer wieder.  
* Alexa: Ok. Wie häufig hast du dich heute ruhig und entspannt gefühlt?  
* User:  Mehr als den halben Tag.
* Alexa: Wie häufig hast du dich heute energisch und aktiv gefühlt?  
* User:  Zu keinem Zeitpunkt.  
* Alexa: Ok. Wie häufig hast du dich frisch und ausgeruht gefühlt?  
* User:  Manchmal.  
* Alexa: Wie häufig hast du dich heute für Dinge interessiert?  
* User:  Fast immer.  
* Alexa: Ok. Dein Ergebnis spricht für ein gutes Wohlbefinden. Danke für deine Zeit! Schau morgen wieder vorbei!  


## IAM Management Console
1. Role anlegen
2. Policy auswählen für Access zur DynamoDB
