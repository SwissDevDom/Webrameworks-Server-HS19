/**
 * Questionnaire Controller (Page Controller)
 */

const Questionnaire = require('../domain/questionnaire');

exports.findAll = (request, response) => {
    Questionnaire.find((err, questionnaires) => {
        if (err) {
            response.status(500).send('database error')
        }
        else {
            response.status(200).json(questionnaires)
        }
    })
};

exports.create = (request, response) => {
    let questionnaire = new Questionnaire();
    questionnaire.title = request.body.title;
    questionnaire.description = request.body.description;

    questionnaire.save((err, questionnaireCreated) => {
        if (err) {
            response.status(412).send('Precondition failed')
        } else {
            response.status(201).json(questionnaireCreated)
        }
    });
};

exports.findById = (request, response) => {
    Questionnaire.findById(request.params.id, (err, questionnaire) => {
        if (err) {
            response.status(404).send('Questionnaire not found')
        } else {
            response.status(200).json(questionnaire)
        }
    });
};

exports.update = (request, response) => {
    Questionnaire.findById(request.params.id, (err, questionnaire) => {
        if (err) {
            response.status(404).send('Questionnaire not found')
        } else {
            questionnaire.title = request.body.title;
            questionnaire.description = request.body.description;

            questionnaire.save((err, questionnaireUpdated) => {
                if (err) {
                    response.status(412).send('Precondition failed')
                } else {
                    response.status(200).json(questionnaireUpdated)
                }
            })
        }
    });
};

exports.delete =  (request, response) => {
    Questionnaire.findByIdAndDelete(request.params.id, (err, questionnaire) => {
        if (err) {
            response.status(404).send('Questionnaire not found')
        } else {
            response.status(204).json(questionnaire)
        }
    });
};
