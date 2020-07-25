const mongoose = require('mongoose')
const Joi = require('joi')
Joi.objectId = require('joi-objectid')(Joi)

const config = require('config')
const jwt = require('jsonwebtoken')

const requestItemSchema = new mongoose.Schema(
  {
    description: { type: String, required: true },
    userId: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'User',
      required: true
    },
    user: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'User'
    },
    founderId: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'User',
      required: true
    },
    founder: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'User'
    },
    itemId: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'Item',
      required: true
    },
    item: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'Item'
    },
    state: { type: String, default: "pending" }, //pending, accept, reject
    isActive: { type: Boolean, default: true }
  },
  {
    timestamps: { createdAt: 'created_at', updatedAt: 'updated_at' }
  }
)

function validateRequest (Request) {
  const schema = {
    description: Joi.string().required(),
    founderId: Joi.objectId().required(),
    itemId: Joi.objectId().required()
  }

  return Joi.validate(Request, schema)
}

const RequestItem = mongoose.model('RequestItem', requestItemSchema)

exports.RequestItem = RequestItem
exports.validate = validateRequest
