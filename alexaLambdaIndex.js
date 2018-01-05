'use strict';
const Alexa = require('alexa-sdk');

const APP_ID = 'amzn1.ask.skill.433c6c8e-e231-4512-a0d9-2292f69d91f8';

function getQuestion(counter)
{

    switch(counter)
    {
        case 1:
            return "Wie häufig warst du heute froh und guter Laune?";
        case 2:
            return "Wie häufig hast du dich heute ruhig und entspannt gefühlt?";
        case 3:
            return "Wie häufig hast du dich heute energisch und aktiv gefühlt?";
        case 4:
            return "Wie häufig hast du dich frisch und ausgeruht gefühlt?";
        case 5:
            return "Wie häufig hast du dich heute für Dinge interessiert?";
        default:
            return "Wie häufig hast du dich heute für Dinge interessiert?";
    }

}

const speechCons = ["Ok", "Alles klar", "danke"];

const WELCOME_MESSAGE = "Hallo! Schön dass du da bist! Willst du mit deinem Stimmungstest beginnen?";

const START_QUIZ_MESSAGE = "OK.  Ich stelle dir nun 5 Fragen zu deiner heutigen Stimmung.";

const EXIT_SKILL_MESSAGE = "Danke für deine Zeit! Schau morgen wieder vorbei ! ";

const REPROMPT_SPEECH = "Möchtest du den Test nocheinmal machen?";

const HELP_MESSAGE = "Ich kann mit dir einen Stimmungstest machen um zu sehen wie es dir geht. Willst du starten?";


//This is the response a user will receive when they ask about something we weren't expecting.  For example, say "pizza" to your
//skill when it starts.  This is the response you will receive.
function getBadAnswer() { return "Tut mir leid das verstehe ich noch nicht. Versuche es mit einer anderen Antwort noch einmal " }

function getCurrentScore(score, counter) { return "Dein aktuelles Ergebnis ist " + score + " von " + counter + ". "; };

function getFinalScore(score, counter) { return "Dein Ergebnis ist " + score + " aus " + counter + ". "; };


const counter = 0;

const states = {
    START: "_START",
    QUIZ: "_QUIZ"
};

const handlers = {
     "LaunchRequest": function() {
        console.log('launchrequest');
        this.handler.state = states.START;
        this.emitWithState("Start");
     },
    "QuizIntent": function() {
        this.handler.state = states.QUIZ;
        this.emitWithState("Quiz");
    },
    "AMAZON.HelpIntent": function() {
        this.response.speak(HELP_MESSAGE).listen(HELP_MESSAGE);
        this.emit(":responseReady");
    },
    "Unhandled": function() {
        this.handler.state = states.START;
        this.emitWithState("Start");
    }
};

const startHandlers = Alexa.CreateStateHandler(states.START,{
    "LaunchRequest": function() {
        console.log('launchrequest');
        this.handler.state = states.START;
        this.emitWithState("Start");
     },
    "Start": function() {
        console.log('Start Handler');
        this.response.speak(WELCOME_MESSAGE).listen(HELP_MESSAGE);
        this.emit(":responseReady");
    },
    "AnswerIntent": function() {
        console.log("AnswerIntent startHandlers");
        this.response.speak(HELP_MESSAGE).listen(HELP_MESSAGE);
        this.emit(":responseReady");
    },
    "QuizIntent": function() {
        console.log("QuizIntent startHandlers");
        this.handler.state = states.QUIZ;
        this.emitWithState("Quiz");
    },
    "AMAZON.PauseIntent": function() {
        this.response.speak(EXIT_SKILL_MESSAGE);
        this.emit(":responseReady");
    },
    "AMAZON.StopIntent": function() {
        this.response.speak(EXIT_SKILL_MESSAGE);
        this.emit(":responseReady");
    },
    "AMAZON.CancelIntent": function() {
        this.response.speak(EXIT_SKILL_MESSAGE);
        this.emit(":responseReady");
    },
    "AMAZON.HelpIntent": function() {
        this.response.speak(HELP_MESSAGE).listen(HELP_MESSAGE);
        this.emit(":responseReady");
    },
    "Unhandled": function() {
        this.emitWithState("Start");
    }
});


const quizHandlers = Alexa.CreateStateHandler(states.QUIZ,{
    "Quiz": function() {
        console.log("Quiz quizHandlers");
        this.attributes["response"] = "";
        this.attributes["counter"] = 0;
        this.emitWithState("AskQuestion");
        if(undefined == this.attributes["timesDone"]){
            this.attributes["timesDone"] = 1;
        }else{
            this.attributes["timesDone"]++;
        }

    },
    "AskQuestion": function() {
        if (this.attributes["counter"] == 0)
        {
            this.attributes["response"] = START_QUIZ_MESSAGE + " ";
        }

        this.attributes["counter"]++;

        let question = getQuestion(this.attributes["counter"]);
        let speech = this.attributes["response"] + question;

        this.emit(":ask", speech, question);
    },
        "AnswerIntent": function() {
        let response = "";
        let speechOutput = "";
        let score = this.attributes["score"];
        let badanswer = getBadAnswer() + getQuestion(this.attributes["counter"]);
        let questionAgain = getQuestion(this.attributes["counter"]);

       console.log('AnswerIntent');
        if (this.event.request.intent != undefined)
        {

            if(this.event.request.intent.slots != undefined){

                let feeling = this.event.request.intent.slots;

                if(feeling.Always && feeling.Always.value != undefined){
                    score += 5;
                }
                else if (feeling.Usually && feeling.Usually.value != undefined){
                    score += 4;
                }
                else if (feeling.MoreThanHalfOfTheTime && feeling.MoreThanHalfOfTheTime.value != undefined){
                    score += 3;
                }
                else if (feeling.MoreThanHalfOfTheTime && feeling.LessThenHalfOfTheTime.value != undefined){
                    score += 2;
                }
                else if (feeling.MoreThanHalfOfTheTime && feeling.NowAndThen.value != undefined){
                    score += 1;
                }
                else if (feeling.MoreThanHalfOfTheTime && feeling.Never.value != undefined){
                    score += 0;
                }else{
                    this.emit(":ask", badanswer, questionAgain);
                }
            }else{
                    this.emit(":ask", badanswer, questionAgain);
            }

        }
        else
        {
                    this.emit(":ask", badanswer, questionAgain);

        }

        response += getSpeechCon();
        this.attributes["score"] = score;

        if (this.attributes["counter"] < 5)
        {
            this.attributes["response"] = response;
            this.emitWithState("AskQuestion");
        }
        else
        {
            let scoringValue = this.attributes["score"]/this.attributes["timesDone"];
            //let scoringValue = 20/2;
            if (scoringValue > 13) {
                response += "Dein Ergebnis spricht für ein gutes Wohlbefinden. ";
            } else {
                response += "Eine behandlungsbedürftige Überlastung, ein Burnout oder eine Depression können bei deinem Ergebnis nicht sicher ausgeschlossen werden." +
                            " Dies ist aber noch keine Diagnose, hierfür solltest du eine gezielte Diagnostik durchführen lassen. ";
            }
            speechOutput = response + EXIT_SKILL_MESSAGE;
            this.response.speak(speechOutput);
            this.emit(":responseReady");
            sessionEnd(this);

        }
    },
    "AMAZON.RepeatIntent": function() {
        console.log('repeatintent');
        let question = getQuestion(this.attributes["counter"]);
        this.response.speak(question).listen(question);
        this.emit(":responseReady");
    },
    "AMAZON.StartOverIntent": function() {
        this.emitWithState("Quiz");
    },
    "AMAZON.StopIntent": function() {
        sessionEnd(this);
    },
    "AMAZON.PauseIntent": function() {
        sessionEnd(this);
    },
    "AMAZON.CancelIntent": function() {
        sessionEnd(this);
    },
    "AMAZON.HelpIntent": function() {
        this.response.speak(HELP_MESSAGE).listen(HELP_MESSAGE);
        this.emit(":responseReady");
    },
    'SessionEndedRequest': function () {
        console.log('session ended!');
        this.emit(':saveState', true);
    },
    "Unhandled": function() {
        this.emitWithState("AnswerIntent");
    }
});


function getSpeechCon()
{
    let speechCon = "";
    return "<say-as interpret-as='interjection'>" + speechCons[0] + "! </say-as><break strength='strong'/>";
}

function sessionEnd(thisRef)
{
    thisRef.handler.state = states.START;
    delete thisRef.attributes['counter'];
    thisRef.response.speak(EXIT_SKILL_MESSAGE);
    thisRef.emit(":responseReady");
}

exports.handler = (event, context, callback) => {
    try {
        const alexa = Alexa.handler(event, context, callback);
        alexa.appId = APP_ID;

        alexa.dynamoDBTableName = 'WHOScoreTableMAPH';
        alexa.registerHandlers(handlers, startHandlers, quizHandlers);
        alexa.execute();
    }catch(error){
        callback(error.message);
    }
};
