const auth = require('../middleware/auth')
const _ = require('lodash')
const { Children, validate, validateUpdate } = require('../models/children')
const express = require('express')
const router = express.Router()

// GET: All Children
router.get('/', async (req, res, next) => {
  const pageNumber = parseInt(req.query.pageNumber)
  const pageSize = parseInt(req.query.pageSize)
  const type = req.query.type || 'Lost'
  const isCompleted = req.query.completed || false

  const children = await Children.find({ type: type, isCompleted: isCompleted })
    .populate('city')
    .skip(pageSize * pageNumber - pageSize)
    .limit(pageSize)

  res.status(200).send(children)
})

// GET: Children By Id
router.get('/id/:id', async (req, res, next) => {
  const children = await Children.findOne({ _id: req.params.id }).populate(
    'city'
  )
  if (!children)
    return res.status(404).send({ message: 'No Children with this id' })
  res.status(200).send(children)
})

// GET: My Children
router.get('/auth', auth, async (req, res, next) => {
  const pageNumber = parseInt(req.query.pageNumber)
  const pageSize = parseInt(req.query.pageSize)
  const type = req.query.type || 'Lost'

  const children = await Children.find({ type: type, userId: req.user._id })
    .populate('city')
    .skip(pageSize * pageNumber - pageSize)
    .limit(pageSize)

  res.status(200).send(children)
})

// Create New Children
router.post('/', auth, async (req, res) => {
  const { error } = validate(req.body)
  if (error) return res.status(400).send({ message: error.details[0].message })

  let child = new Children(
    _.pick(req.body, [
      'name',
      'type',
      'age',
      'description',
      'gender',
      'images',
      'address',
      'cityId'
    ])
  )

  child.user = req.user._id
  child.userId = req.user._id
  child.city = child.cityId

  try {
    await child.save()
    res.status(200).send(child)
  } catch (err) {
    res.send({ message: err.message })
  }
})

// Update Children
router.put('/:id', auth, async (req, res) => {
  const { error } = validateUpdate(req.body)
  if (error) return res.status(400).send({ message: error.details[0].message })

  let child = await Children.findById(req.params.id)
  if (!child) return res.status(400).send({ message: 'child not exist.' })

  child.name = req.body.name
  child.age = req.body.age
  child.description = req.body.description
  child.gender = req.body.gender
  child.images = req.body.images
  child.address = req.body.address

  try {
    await child.save()
    res.status(200).send(child)
  } catch (err) {
    res.send({ message: err.message })
  }
})

//Change Status
router.put('/complete/:id', async (req, res) => {
  let child = await Children.findById(req.params.id)
  if (!child) return res.status(400).send({ message: 'child not exist.' })

  child.isCompleted = true
  await child.save()
  res.send(child)
})

//Change Status
router.delete('/:id', async (req, res) => {
  let child = await Children.findByIdAndDelete(req.params.id)
  await child.save()
  res.send(child)
})

// DELETE:
router.delete('/', async (req, res, next) => {
  await Children.deleteMany()
  res.send({})
})

module.exports = router
