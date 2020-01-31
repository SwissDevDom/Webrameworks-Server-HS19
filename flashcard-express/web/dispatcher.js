/**
 * Dispatcher. Konfiguriert den FrontController.
 */
const dispatcher = require('express').Router()

const questionnaire_controller = require('./questionnaire-controller');
const index_controller = require('./index-controller');

// all routes
dispatcher.route('/').get(index_controller.index);

dispatcher.route('/questionnaires')
    .get(questionnaire_controller.findAll)
    .post(questionnaire_controller.create);

dispatcher.route('/questionnaires/:id')
    .get(questionnaire_controller.findById)
    .put(questionnaire_controller.update)
    .delete(questionnaire_controller.delete);

// export dispatcher to be able to use it outside of this module
module.exports = dispatcher;

