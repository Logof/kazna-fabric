const common = require('./common');

const { Gateway, Wallets } = require('fabric-network');
const path = require('path');
const fs = require('fs');

const ccpPath = path.resolve(__dirname, '..', '..', '..', '..','test-network', 'organizations', 'peerOrganizations', 'org1.example.com', 'connection-org1.json');
const ccp = JSON.parse(fs.readFileSync(ccpPath, 'utf8'));

const walletPath = path.join(process.cwd(), 'wallet');

const appUser = 'appUser';

module.exports = {
  getAll: async (req, res, next) => {
    try {
      console.log(walletPath);

      const wallet = await Wallets.newFileSystemWallet(walletPath);
      const userExists = await wallet.get(appUser);
      console.log(userExists)
      if (!userExists) {
        res.json({status: false, error: {message: 'User not exist in the wallet'}});
        return;
      }
    
      const gateway = new Gateway();
      await gateway.connect(ccp, { wallet, identity: appUser, discovery: { enabled: true, asLocalhost: true } });
      
      const network = await gateway.getNetwork('mychannel');
      if (!network) {
        console.log("Error: Channel 'mychannel' not found");
        res.json({status: false, error: {message: "Channel 'mychannel' not found"}});
        return;
      }
      console.log("network: ", network)
    
      const contract = network.getContract('kazna');
      if (!contract) {
        console.log("Error: Contract 'kazna' not found");
        res.json({status: false, error: {message: "Contract 'kazna' not found"}});
        return;
      }
      console.log("contract: ", contract)
        
      const result = await contract.evaluateTransaction('ListBO');
      res.json({status: true, BO: JSON.parse(result.toString())});
    } catch (err) {
      console.log("Catch Exception: " + err)
      res.json({status: false, error: err});
    }
  },

  putBO: async (req, res, next) => {
    console.log(req.body)
    try {
      const wallet = await Wallets.newFileSystemWallet(walletPath);
      const userExists = await wallet.get(appUser);
      if (!userExists) {
        res.json({status: false, error: {message: 'User not exist in the wallet'}});
        return;
      }
    
      const gateway = new Gateway();
      await gateway.connect(ccp, { wallet, identity: appUser, discovery: { enabled: true, asLocalhost: true } });
            
      const network = await gateway.getNetwork('mychannel');
      const contract = network.getContract('kazna');
      
      const jsonString = JSON.stringify(req.body, common.saveAmountNumberic);
      
      console.log("------------------------------------ Начало ------------------------------------")
      let resp = await contract.submitTransaction('PutBO', jsonString);
      console.log("Ответ:" + resp);
      res.json({status: true, message: 'Transaction (create BO) has been submitted.'})
      console.log("------------------------------------ Конец ------------------------------------")
    } catch (err) {
      console.log("Catch Exception: " + err)
      res.json({status: false, error: err});
    }
  },
}