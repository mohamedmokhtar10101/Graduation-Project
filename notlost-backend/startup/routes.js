const express = require("express");
const users = require("../routes/users");
const cities = require("../routes/cities");
const children = require("../routes/childrens");
const items = require("../routes/items");
const itemrequests = require("../routes/itemrequests");
const childrequests = require("../routes/childrequests");

module.exports = function (app) {
  app.use(express.json());
  app.use(function (req, res, next) {
    var oneof = false;
    if (req.headers.origin) {
      res.header("Access-Control-Allow-Origin", req.headers.origin);
      oneof = true;
    }

    if (req.headers["access-control-request-method"]) {
      res.header(
        "Access-Control-Allow-Methods",
        req.headers["access-control-request-method"]
      );
      oneof = true;
    }

    if (req.headers["access-control-request-headers"]) {
      res.header(
        "Access-Control-Allow-Headers",
        req.headers["access-control-request-headers"]
      );
      oneof = true;
    }

    if (oneof) {
      res.header("Access-Control-Max-Age", 60 * 60 * 24 * 365);
    }

    if (oneof && req.method == "OPTIONS") {
      res.send(200);
    } else {
      next();
    }
  });

  app.use("/api/users", users);
  app.use("/api/cities", cities);
  app.use("/api/children", children);
  app.use("/api/items", items);
  app.use("/api/childrequests", childrequests);
  app.use("/api/itemrequests", itemrequests);


};
