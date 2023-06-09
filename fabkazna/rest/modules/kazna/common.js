// Setting for Hyperledger Fabric
const { Gateway, Wallets } = require('fabric-network');
const path = require('path');
const fs = require('fs');

const ccpPath = path.resolve(__dirname, '..', '..', '..', '..','test-network', 'organizations', 'peerOrganizations', 'org1.example.com', 'connection-org1.json');
const ccp = JSON.parse(fs.readFileSync(ccpPath, 'utf8'));

const walletPath = path.join(process.cwd(), 'wallet');

const appUser = 'appUser';

function saveAmountNumberic(key, value) {
  return key === "amount" || key === "prepaidAmount" ? +value : value;
}

module.exports = { walletPath, saveAmountNumberic }


