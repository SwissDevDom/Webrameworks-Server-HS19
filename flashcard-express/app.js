
const config = require('dotenv-extended').load({
    schema: '.env.schema',
    errorOnMissing: true,
    errorOnExtra: true
});
const log4js = require('log4js');
const http = require('http');

const logger = log4js.getLogger('App');
logger.level = 'info';

const express = require('express');
const app = express();
const port = config.PORT;

app.get('/', (request, response) => response.send('Hello World!'));

app.listen(port, () => console.log(`Server is running on port ${port}!`));
