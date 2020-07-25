const winston = require("winston");

module.exports = function (err, req, res, next) {
  winston.error(err.message, err);

  // Error
  // Warn
  // Info
  // Verbose
  // Debug
  // Silly

  res.status(500).send("Something failed.");
};
