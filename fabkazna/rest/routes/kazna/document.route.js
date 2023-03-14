const express = require('express');
const router = express.Router();


const docBRSController = require('../../modules/kazna/docBRS');
const docZKRController = require('../../modules/kazna/docZKR');
const docBOController = require('../../modules/kazna/docBO');
const docDOController = require('../../modules/kazna/docDO');


//Get a list of all products
router.get('/brs', docBRSController.getAll);

//Create a new product
router.post('/brs', docBRSController.putBRS);

//Get a list of all products
router.get('/zkr', docZKRController.getAll);

//Create a new product
router.post('/zkr', docZKRController.putZKR);

//Get a list of all products
router.get('/bo', docBOController.getAll);

//Create a new product
router.post('/bo', docBOController.putBO);

//Get a list of all products
router.get('/do', docDOController.getAll);

//Create a new product
router.post('/do', docDOController.putDO);

module.exports = router;
