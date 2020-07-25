const mongoose = require('mongoose')
const Joi = require('joi')
Joi.objectId = require('joi-objectid')(Joi)

const config = require('config')
const jwt = require('jsonwebtoken')

const itemSchema = new mongoose.Schema(
  {
    name: { type: String, required: true },
    description: { type: String, required: true },
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

function validateItem (item) {
  const schema = {
    name: Joi.string().required(),
    type: Joi.string().required(),
    description: Joi.string().required(),
    address: Joi.string().required(),
    images: Joi.array().required(),
    cityId: Joi.objectId().required()
  }

  return Joi.validate(item, schema)
}

function validateUpdateItem (item) {
  const schema = {
    name: Joi.string().required(),
    type: Joi.any(),
    description: Joi.string().required(),
    address: Joi.string().required(),
    images: Joi.array().required()
  }

  return Joi.validate(item, schema)
}

const Item = mongoose.model('Item', itemSchema)

exports.Item = Item
exports.validate = validateItem
exports.validateUpdate = validateUpdateItem
