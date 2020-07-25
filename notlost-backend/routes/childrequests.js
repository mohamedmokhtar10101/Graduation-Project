const auth = require('../middleware/auth')
const _ = require('lodash')
const { RequestChild, validate } = require('../models/requestchild')
const { Children } = require('../models/children')
const { User } = require('../models/user')
const express = require('express')
var admin = require('firebase-admin')
const router = express.Router()


var serviceAccount = require("../firebase-storage-sdk/fire-storage-sdk.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://notlost-7ce60.firebaseio.com"
});



// GET: All Requests
router.get('/:id', auth, async (req, res, next) => {
  const pageNumber = parseInt(req.query.pageNumber)
  const pageSize = parseInt(req.query.pageSize)

  const requests = await RequestChild.find({
    childrenId: req.params.id,
    founderId: req.user._id,
    isActive: true
  })
    .populate('user children')
    .skip(pageSize * pageNumber - pageSize)
    .limit(pageSize)

  res.send(requests)
})


// GET: All Requests
router.get('/auth/myRequests', auth, async (req, res, next) => {
  const pageNumber = parseInt(req.query.pageNumber)
  const pageSize = parseInt(req.query.pageSize)

  const requests = await RequestChild.find({ userId: req.user._id })
    .populate('founder children')
    .skip(pageSize * pageNumber - pageSize)
    .limit(pageSize)

  res.send(requests)
})

// GET: All Requests
router.get('/', async (req, res, next) => {
  const pageNumber = parseInt(req.query.pageNumber)
  const pageSize = parseInt(req.query.pageSize)

  const requests = await RequestChild.find()
    .populate('user children')
    .skip(pageSize * pageNumber - pageSize)
    .limit(pageSize)

  res.send(requests)
})

// Create New Request
router.post('/', auth, async (req, res) => {
  const { error } = validate(req.body)
  if (error) return res.status(400).send({ message: error.details[0].message })

  let request = await RequestChild.findOne({
    userId: req.user._id,
    childrenId: req.body.childrenId,
    state: "pending"
  })
  if (request)
    return res.status(400).send({ message: 'Request already exist.' })

  request = new RequestChild(
    _.pick(req.body, ['description', 'founderId', 'childrenId', 'image'])
  )
  request.userId = req.user._id
  request.user = req.user._id
  request.children = req.body.childrenId
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
  let request = await RequestChild.findOne({ _id: req.params.id })
  if (!request)
    return res.status(400).send({ message: 'Request no exist.' })

  request.state = 'accept'

  let children = await Children.findOne({ _id: request.childrenId })
  children.isCompleted = true


  let user = await User.findOne({ _id: request.userId })

  try {
    await request.save()
    await children.save()
    pushNotification(user.deviceToken, "Request updated", "Your requset for " + children.name + " has been accepted.")
    res.status(200).send(request)
  } catch (err) {
    res.send({ message: err.message })
  }
})

// Create New Request
router.post('/reject/:id', async (req, res) => {
  let request = await RequestChild.findOne({ _id: req.params.id })
  if (!request)
    return res.status(400).send({ message: 'Request already exist.' })

  request.state = 'reject'

  let user = await User.findOne({ _id: request.userId })
  let child = await Children.findOne({ _id: request.childrenId })

  try {
    await request.save()
    pushNotification(user.deviceToken, "Request updated", "Your requset for " + child.name + " has been rejected.")
    res.status(200).send(request)
  } catch (err) {
    res.send({ message: err.message })
  }
})

router.post('/block/:id', async (req, res) => {
  let request = await RequestChild.findOne({ _id: req.params.id })
  if (!request)
    return res.status(400).send({ message: 'Request not exist.' })

  request.isActive = false

  let user = await User.findOne({ _id: request.userId })
  let child = await Children.findOne({ _id: request.childrenId })

  try {
    await request.save()
    pushNotification(user.deviceToken, "Request updated", "Your requsets has been blocked for " + child.name)
    res.status(200).send(request)
  } catch (err) {
    res.send({ message: err.message })
  }
})

// Create delete Request
router.post('/delete/:id', async (req, res) => {
  let request = await RequestChild.findByIdAndDelete(req.params.id)
  try {
    await request.save()
    res.status(200).send(request)
  } catch (err) {
    res.send({ message: err.message })
  }
})

//Change Status
router.delete('/:id', async (req, res) => {
  let city = await RequestChild.findByIdAndDelete(req.params.id)
  await city.save()
  res.send(city)
})

// DELETE: admins
router.delete('/', async (req, res, next) => {
  await RequestChild.deleteMany()
  res.send({})
})

function pushNotification(
  registrationToken,
  title,
  body
) {
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
