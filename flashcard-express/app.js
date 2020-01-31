/**
 * Main Programm.
 */
const config = require('dotenv-extended').load({
    schema: '.env.schema',
    errorOnMissing: true,
    errorOnExtra: true
});

const log4js = require('log4js');
const mongoose = require('mongoose');
const express = require('express');
const bodyParser = require('body-parser');
const dispatcher = require('./web/dispatcher');
const cors = require('cors');

const url = `mongodb://${ process.env.MONGO_HOST }/${ process.env.MONGO_DATABASE }`;
mongoose.connect(url, { useNewUrlParser: true });

const logger = log4js.getLogger('App');
logger.level = 'info';

const app = express();

const modifyResponseBody = (req, res, next) => {
    var origSend = res.send;
    res.send = body => {
        // arguments[0] (or `body`) contains the response body
        body = "modified: " + body;
        origSend.apply(res, [body])
    };
    next()
};
//app.use(modifyResponseBody)

app.use(cors());
app.use(bodyParser.json());
app.use('/flashcard-express', dispatcher);

app.listen(config.PORT, function () {
    logger.info(`Server is running on port ${config.PORT}`)
});
