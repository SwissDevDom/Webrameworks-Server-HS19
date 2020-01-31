
const config = require('dotenv-extended').load({
    schema: '.env.schema',
    errorOnMissing: true,
    errorOnExtra: true
});
const log4js = require('log4js');
const express = require('express');
const mongoose = require('mongoose');

const url = `mongodb://${ config.MONGO_HOST }/${ config.MONGO_DATABASE }`
mongoose.connect(url, {useNewUrlParser: true});

const logger = log4js.getLogger('App');
logger.level = 'info';

const Questionnaire = require('./domain/questionnaire');
const app = express();
const port = config.PORT;

app.get('/', function(request, response) {
    Questionnaire.find((err, questionnaires) => {
        if (err) {
            response.status(500).send(err)
        }
        response.send(questionnaires)
    })
});

app.listen(port, function () {
    logger.info(`Server is running on port ${port}`)
})
