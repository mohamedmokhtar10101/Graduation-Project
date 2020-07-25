const auth = require('../middleware/auth')
const _ = require('lodash')
const { City, validate } = require('../models/city')
const express = require('express')
const router = express.Router()

// GET: All Cities
router.get('/', async (req, res, next) => {
  const pageNumber = parseInt(req.query.pageNumber)
  const pageSize = parseInt(req.query.pageSize)

  const cities = await City.find({ isActive: true })
    .skip(pageSize * pageNumber - pageSize)
    .limit(pageSize)

  res.send(cities)
})

// Create New City
router.post('/', async (req, res) => {
  const { error } = validate(req.body)
  if (error) return res.status(400).send({ message: error.details[0].message })

  let city = await City.findOne({ name: req.body.name })
  if (city) return res.status(400).send({ message: 'City already exist.' })

  city = new City(_.pick(req.body, ['name']))

  try {
    await city.save()
    res.status(200).send(city)
  } catch (err) {
    res.send({ message: err.message })
  }
})

// Update City
router.put('/:id', async (req, res) => {
  const { error } = validate(req.body)
  if (error) return res.status(400).send({ message: error.details[0].message })

  let city = await City.findById(req.params.id)
  if (!city) return res.status(400).send({ message: 'city not exist.' })

  city.name = req.body.name

  try {
    await city.save()
    res.status(200).send(city)
  } catch (err) {
    res.send({ message: err.message })
  }
})

//Change Status
router.delete('/:id', async (req, res) => {
  let city = await City.findByIdAndDelete(req.params.id)
  await city.save()
  res.send(city)
})

// DELETE: admins
router.delete('/', async (req, res, next) => {
  await City.deleteMany()
  res.send({})
})

module.exports = router
