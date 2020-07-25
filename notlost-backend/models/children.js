const mongoose = require('mongoose')
const Joi = require('joi')
Joi.objectId = require('joi-objectid')(Joi)

const config = require('config')
const jwt = require('jsonwebtoken')

const childrenSchema = new mongoose.Schema(
  {
    name: { type: String, required: true },
    age: { type: Number, required: true },
    description: { type: String, required: true },
    gender: { type: String, required: true },
    images: [{ type: String, required: true }],
    type: { type: String, require: true }, // Lost, Found
    address: { type: String, required: true },
    userId: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'User',
      required: true
    },
    cityId: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'City',
      required: true
    },
    user: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
    city: { type: mongoose.Schema.Types.ObjectId, ref: 'City', required: true },
    isCompleted: { type: Boolean, default: false }
  },
  {
    timestamps: { createdAt: 'created_at', updatedAt: 'updated_at' }
  }
)

function validateChildren (children) {
  const schema = {
    name: Joi.string().required(),
    type: Joi.string().required(),
    description: Joi.string().required(),
    gender: Joi.string().required(),
    address: Joi.string().required(),
    age: Joi.number().required(),
    images: Joi.array().required(),
    cityId: Joi.objectId().required()
  }

  return Joi.validate(children, schema)
}

function validateUpdateChildren (children) {
  const schema = {
    name: Joi.string().required(),
    type: Joi.any(),
    description: Joi.string().required(),
    gender: Joi.string().required(),
    address: Joi.string().required(),
    age: Joi.number().required(),
    images: Joi.array().required()
  }

  return Joi.validate(children, schema)
}

const Children = mongoose.model('Children', childrenSchema)

exports.Children = Children
exports.validate = validateChildren 
exports.validateUpdate = validateUpdateChildren 

