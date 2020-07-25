const auth = require('../middleware/auth')
const bcrypt = require('bcryptjs')
const _ = require('lodash')
const Joi = require('joi')
const { User, validate, validateSubscription } = require('../models/user')
const { Item } = require('../models/item')
const { Children } = require('../models/children')
const { RequestItem } = require('../models/requestItem')
const { RequestChild } = require('../models/requestchild')
const express = require('express')
const router = express.Router()

// GET: Users
router.get('/', async (req, res, next) => {
  const pageNumber = parseInt(req.query.pageNumber)
  const pageSize = parseInt(req.query.pageSize)

  const users = await User.find()
    .select('-password')
    .skip(pageSize * pageNumber - pageSize)
    .limit(pageSize)

  res.status(200).send(users)
})

// GET: My account
router.get('/me', auth, async (req, res, next) => {
  const user = await User.findById(req.user._id).select('-password')
  res.status(200).send(user)
})

// POST: Edit profile
router.post('/editProfile', auth, async (req, res, next) => {
  let user = await User.findById(req.user._id).select('-password')
  if (!user) return res.status(404).send({ message: 'User not exist.' })
  const editedUser = _.pick(req.body, ['firstName', 'lastName', 'phone'])
  user.firstName = editedUser.firstName
  user.lastName = editedUser.lastName
  user.phone = editedUser.phone

  await user.save()
  res.status(200).send(user)
})

// POST: Register device
router.post('/registerDevice', auth, async (req, res, next) => {
  let user = await User.findById(req.user._id)
  if (!user) return res.status(404).send({ message: 'User not exist.' })

  user.deviceToken = req.body.deviceToken

  await user.save()
  res.send(user)
})

// POST: Login

router.post('/login', async (req, res) => {
  const { error } = validateLogin(req.body)
  if (error) res.status(400).send({ message: error.details[0].message })

  let user = await User.findOne({ username: req.body.username })
  if (!user)
    return res.status(400).send({ message: 'Invalid username or password.' })

  if (!user.isActive)
    return res.status(400).send({ message: 'Your account has been blocked' })

  const validPassword = await bcrypt.compare(req.body.password, user.password)
  if (!validPassword)
    res.status(400).send({ message: 'Invalid username or password.' })

  user.deviceToken = req.body.deviceToken

  const token = user.generateAuthToken()
  res.header('x-auth-token', token).send({
    'x-auth-token': token
  })
})

// Create New User
router.post('/register', async (req, res) => {
  const { error } = validate(req.body)
  if (error) return res.status(400).send(error.details[0].message)

  let user = await User.findOne({
    username: req.body.phone
  })
  if (user)
    return res.status(400).send({ message: 'Username already exist.' })

  user = new User(
    _.pick(req.body, [
      'firstName',
      'lastName',
      'phone',
      'email',
      'password',
      'deviceToken'
    ])
  )

  user.username = user.phone
  const salt = await bcrypt.genSalt(10)
  user.password = await bcrypt.hash(user.password, salt)
  await user.save()

  const token = user.generateAuthToken()
  res.header('x-auth-token', token).send({
    'x-auth-token': token
  })
})

// DELETE: User
router.delete('/:id', async (req, res) => {
  let user = await User.findByIdAndDelete(req.params.id)
  await user.save()
  res.send(user)
})

// Reset Password
router.post('/resetPassword', async (req, res, next) => {
  const email = req.body.email
  let user = await User.findOne({ email: email })
  if (!user) return res.status(400).send({ message: "User Doesn't Exist" })

  const oldPassword = req.body.oldPassword
  const newPassword = req.body.newPassword

  const validPassword = await bcrypt.compare(oldPassword, user.password)
  if (!validPassword)
    return res.status(400).send({ message: 'Invalid email or password.' })

  const salt = await bcrypt.genSalt(10)
  user.password = await bcrypt.hash(newPassword, salt)

  try {
    await user.save()
    return res
      .status(200)
      .send({ message: 'Password has been changed successfully' })
  } catch (err) {
    return res.send({ message: err.message })
  }
})

router.post('/:id/changeStatus', auth, async (req, res) => {
  let admin = await Admin.findById(req.admin._id)
  if (!admin) return res.status(404).send({ message: 'No Authorization.' })

  if (!admin.rule.match(/^(super|admin)$/))
    return res.status(404).send({ message: 'Access not allowed' })

  let user = await User.findById(req.params.id)
  if (!user) return res.status(404).send({ message: 'No User With This ID.' })

  user.isActive = !user.isActive
  await user.save()
  res.send(user)
})

// DELETE: users
router.delete('/', async (req, res, next) => {
  await User.deleteMany()
  res.send({})
})

// POST: Update Image
router.put('/auth/updateImage', auth, async (req, res, next) => {
  let user = await User.findById(req.user._id).select('-password')
  if (!user) return res.status(404).send({ message: 'User not exist.' })

  user.image = req.body.image
  await user.save()
  res.status(200).send(user)
})

router.get('/myPosts', auth, async (req, res, next) => {
  let type = req.query.type || 'children'
  let searchType = req.query.searchType || 'Lost'

  if (type == 'Children') {
    const children = await Children.find({
      userId: req.user._id,
      type: searchType
    }).populate('city')
    res.status(200).send(children)
  } else if (type == 'Items') {
    const items = await Item.find({
      userId: req.user._id,
      type: searchType
    }).populate('city')
    res.status(200).send(items)
  } else {
    return res.status(401).send({ message: 'Invalid type' })
  }
})


router.get('/hasNonCompletedRequest', auth, async (req, res, next) => {
  const itemsCount = await RequestItem.find({ founderId: req.user._id, state: "pending" }).countDocuments()
  const childrenCount = await RequestChild.find({ founderId: req.user._id, state: "pending" }).countDocuments()

  const total = itemsCount + childrenCount
  if (total > 0) {
    return res.status(200).send({ hasNonCompletedRequest: true, itemCount: itemsCount, childrenCount: childrenCount })
  } else {
    return res.status(200).send({ hasNonCompletedRequest: false, itemCount: itemsCount, childrenCount: childrenCount })
  }
})

function validateLogin(req) {
  const schema = {
    username: Joi.string()
      .min(5)
      .max(255)
      .required(),
    password: Joi.string()
      .min(5)
      .max(255)
      .required(),
    deviceToken: Joi.string().required()
  }

  return Joi.validate(req, schema)
}

module.exports = router
