const auth = require('../middleware/auth')
const _ = require('lodash')
const { RequestItem, validate } = require('../models/requestItem')
const { User } = require('../models/user')
const express = require('express')
const { Item } = require('../models/item')

var admin = require('firebase-admin')

const router = express.Router()

// GET: All Requests
router.get('/:id', auth, async (req, res, next) => {
  const pageNumber = parseInt(req.query.pageNumber)
  const pageSize = parseInt(req.query.pageSize)

  const requests = await RequestItem.find({
    itemId: req.params.id,
    founderId: req.user._id,
    isActive: true
  })
    .populate('user item')
    .skip(pageSize * pageNumber - pageSize)
    .limit(pageSize)

  res.send(requests)
})

// GET: All Requests
router.get('/', async (req, res, next) => {
  const pageNumber = parseInt(req.query.pageNumber)
  const pageSize = parseInt(req.query.pageSize)

  const requests = await RequestItem.find()
    .populate('user item')
    .skip(pageSize * pageNumber - pageSize)
    .limit(pageSize)

  res.send(requests)
})

// GET: All Requests
router.get('/auth/myRequests', auth, async (req, res, next) => {
  const pageNumber = parseInt(req.query.pageNumber)
  const pageSize = parseInt(req.query.pageSize)

  const requests = await RequestItem.find({ userId: req.user._id })
    .populate('founder item')
    .skip(pageSize * pageNumber - pageSize)
    .limit(pageSize)

  res.send(requests)
})

// Create New Request
router.post('/', auth, async (req, res) => {
  const { error } = validate(req.body)
  if (error) return res.status(400).send({ message: error.details[0].message })

  let request = await RequestItem.findOne({
    userId: req.user._id,
    itemId: req.body.itemId,
    state: 'pending'
  })

  if (request)
    return res.status(400).send({ message: 'Request already exist.' })

  request = new RequestItem(
    _.pick(req.body, ['description', 'founderId', 'itemId'])
  )
  request.userId = req.user._id
  request.user = req.user._id
  request.item = req.body.itemId
  request.founder = req.body.founderId

  try {
    await request.save()
    res.status(200).send(request)
  } catch (err) {
    res.status(400).send({ message: err.message })
  }
})

// Create New Request
router.post('/accept/:id', async (req, res) => {
  let request = await RequestItem.findOne({ _id: req.params.id })
  if (!request)
    return res.status(400).send({ message: 'Request already exist.' })

  request.state = 'accept'

  let item = await Item.findOne({ _id: request.itemId })
  item.isCompleted = true

  let user = await User.findOne({ _id: request.userId })

  try {
    await request.save()
    await item.save()
    pushNotification(
      user.deviceToken,
      'Request updated',
      'Your requset for ' + item.name + ' has been accepted.'
    )
    res.status(200).send(request)
  } catch (err) {
    res.send({ message: err.message })
  }
})

// Create New Request
router.post('/reject/:id', async (req, res) => {
  let request = await RequestItem.findOne({ _id: req.params.id })
  if (!request)
    return res.status(400).send({ message: 'Request already exist.' })

  request.state = 'reject'

  let item = await Item.findOne({ _id: request.itemId })
  let user = await User.findOne({ _id: request.userId })

  try {
    await request.save()

    pushNotification(
      user.deviceToken,
      'Request updated',
      'Your requset for ' + item.name + ' has been rejected.'
    )
    res.status(200).send(request)
  } catch (err) {
    res.send({ message: err.message })
  }
})


// Create New Request
router.post('/block/:id', async (req, res) => {
  let request = await RequestItem.findOne({ _id: req.params.id })
  if (!request)
    return res.status(400).send({ message: 'Request not exist.' })

  request.isActive = false

  let user = await User.findOne({ _id: request.userId })
  let item = await Item.findOne({ _id: request.itemId })

  try {
    await request.save()
    pushNotification(user.deviceToken, "Request updated", "Your requsets has been blocked for " + item.name)
    res.status(200).send(request)
  } catch (err) {
    res.send({ message: err.message })
  }
})

// Create delete Request
router.post('/delete/:id', async (req, res) => {
  let request = await RequestItem.findByIdAndDelete(req.params.id)
  try {
    await request.save()
    res.status(200).send(request)
  } catch (err) {
    res.send({ message: err.message })
  }
})


//Change Status
router.delete('/:id', async (req, res) => {
  let city = await RequestItem.findByIdAndDelete(req.params.id)
  await city.save()
  res.send(city)
})

// DELETE: admins
router.delete('/', async (req, res, next) => {
  await RequestItem.deleteMany()
  res.send({})
})

function pushNotification(registrationToken, title, body) {
  var payload = {
    data: {
      title: title,
      body: body
    }
  }

  console.log(payload)
  try {
    admin
      .messaging()
      .sendToDevice(registrationToken, payload)
      .then(function (response) {
        console.log(response + ' notifications sent')
      })
      .catch(function (err) {
        console.log(err.message)
      })
  } catch (err) {
    console.log({ message: err.message })
  }
}

module.exports = router
