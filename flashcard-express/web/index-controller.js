/**
 * Index Controller (PageController)
 */
exports.index = (req, res) =>
    res.redirect(`${ req.baseUrl }/questionnaires`)
