const mongoose = require('mongoose')
const Joi = require('joi')
Joi.objectId = require('joi-objectid')(Joi)

const config = require('config')
const jwt = require('jsonwebtoken')

const requestSchema = new mongoose.Schema(
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
    childrenId: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'Children',
      required: true
    },
    children: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'Children'
    },
    image: { type: String },
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
    childrenId: Joi.objectId().required(),
    image: Joi.any()
  }

  return Joi.validate(Request, schema)
}

const RequestChild = mongoose.model('RequestChild', requestSchema)

exports.RequestChild = RequestChild
exports.validate = validateRequest
