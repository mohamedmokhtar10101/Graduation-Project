const mongoose = require('mongoose')
const Joi = require('joi')
Joi.objectId = require('joi-objectid')(Joi)

const config = require('config')
const jwt = require('jsonwebtoken')

const userSchema = new mongoose.Schema(
  {
    firstName: { type: String, minlength: 1, maxlength: 40, required: true },
    lastName: { type: String, minlength: 1, maxlength: 40, required: true },
    username: { type: String, required: true, unique: true },
    email: { type: String, required: false, unique: true },
    password: { type: String, minlength: 6, required: true },
    phone: { type: String },
    image: { type: String },
    accessToken: { type: String },
    isActive: { type: Boolean, default: true },
    deviceToken: { type: String }
  },
  {
    timestamps: { createdAt: 'created_at', updatedAt: 'updated_at' }
  }
)

function validateUser (user) {
  const schema = {
    firstName: Joi.string()
      .min(1)
      .max(40)
      .required(),
    lastName: Joi.string()
      .min(1)
      .max(40)
      .required(),
    email: Joi.string(),
    phone: Joi.string().required(),
    password: Joi.string()
      .min(6)
      .required(),
    accessToken: Joi.string(),
    deviceToken: Joi.string().required()
  }

  return Joi.validate(user, schema)
}

userSchema.methods.generateAuthToken = function () {
  const token = jwt.sign(
    {
      _id: this._id,
      email: this.email
    },
    config.get('jwtPrivateKey')
  )
  return token
}

const User = mongoose.model('User', userSchema)

exports.User = User
exports.validate = validateUser
