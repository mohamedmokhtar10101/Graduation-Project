const auth = require('../middleware/auth')
const _ = require('lodash')
const { Item, validate, validateUpdate } = require('../models/item')
const express = require('express')
const router = express.Router()

// GET: All Items
router.get('/', async (req, res, next) => {
  const pageNumber = parseInt(req.query.pageNumber)
  const pageSize = parseInt(req.query.pageSize)
  const type = req.query.type || 'Lost'
  const isCompleted = req.query.completed || false

  const item = await Item.find({ type: type, isCompleted: isCompleted })
    .populate('city')
    .skip(pageSize * pageNumber - pageSize)
    .limit(pageSize)

  res.status(200).send(item)
})

// GET: Item By Id
router.get('/id/:id', async (req, res, next) => {
  const item = await Item.findOne({ _id: req.params.id }).populate('city')
  if (!item) return res.status(404).send({ message: 'No item with this id' })
  res.status(200).send(item)
})

// GET: My Items
router.get('/auth', auth, async (req, res, next) => {
  const pageNumber = parseInt(req.query.pageNumber)
  const pageSize = parseInt(req.query.pageSize)
  const type = req.query.type || 'Lost'

  const items = await Item.find({ type: type, userId: req.user._id })
    .populate('city')
    .skip(pageSize * pageNumber - pageSize)
    .limit(pageSize)

  res.status(200).send(items)
})

// Create New Item
router.post('/', auth, async (req, res) => {
  const { error } = validate(req.body)
  if (error) return res.status(400).send({ message: error.details[0].message })

  let item = new Item(
    _.pick(req.body, [
      'name',
      'type',
      'description',
      'images',
      'address',
      'cityId'
    ])
  )

  item.user = req.user._id
  item.userId = req.user._id
  item.city = item.cityId

  try {
    await item.save()
    res.status(200).send(item)
  } catch (err) {
    res.send({ message: err.message })
  }
})

// Update Item
router.put('/:id', async (req, res) => {
  const { error } = validateUpdate(req.body)
  if (error) return res.status(400).send({ message: error.details[0].message })

  let item = await Item.findById(req.params.id)
  if (!item) return res.status(400).send({ message: 'item not exist.' })

  item.name = req.body.name
  item.description = req.body.description
  item.images = req.body.images
  item.address = req.body.address

  try {
    await item.save()
    res.status(200).send(item)
  } catch (err) {
    res.send({ message: err.message })
  }
})

//Change Status
router.put('/complete/:id', async (req, res) => {
  let item = await Item.findById(req.params.id)
  if (!item) return res.status(400).send({ message: 'item not exist.' })

  item.isCompleted = true

  await item.save()

  res.send(item)
})

//Change Status
router.delete('/:id', async (req, res) => {
  let item = await Item.findByIdAndDelete(req.params.id)
  await item.save()
  res.send(item)
})

// DELETE:
router.delete('/', async (req, res, next) => {
  await Item.deleteMany()
  res.send({})
})

module.exports = router
