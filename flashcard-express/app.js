
const config = require('dotenv-extended').load({
    schema: '.env.schema',
    errorOnMissing: true,
    errorOnExtra: true
});
const log4js = require('log4js');
const http = require('http');

const logger = log4js.getLogger('App');
logger.level = 'info';

const server = http.createServer((request, response) => {
    response.writeHead(200, { 'Content-Type': 'text/plain'});
    response.end('Hello World!\n')
});

server.listen(config.PORT);
logger.info(`Server is running on port ${config.PORT}`);
