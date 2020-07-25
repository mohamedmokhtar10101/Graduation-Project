const mongoose = require("mongoose");
const Joi = require("joi");
Joi.objectId = require("joi-objectid")(Joi);

const config = require("config");
const jwt = require("jsonwebtoken");

const citySchema = new mongoose.Schema(
    {
        name: { type: String, required: true },
        isActive: { type: Boolean, default: true }
    },
    {
        timestamps: { createdAt: "created_at", updatedAt: "updated_at" }
    }
);

function validateCity(city) {
    const schema = {
        name: Joi.string().required()
    };

    return Joi.validate(city, schema);
}

const City = mongoose.model("City", citySchema);

exports.City = City;
exports.validate = validateCity;
