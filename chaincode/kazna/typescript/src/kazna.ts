/*
 * SPDX-License-Identifier: Apache-2.0
 */

import { Context, Contract } from 'fabric-contract-api';
import { BRS } from './BRS';

export class Kazna extends Contract {

    public async initLedger(ctx: Context) {
        console.info('============= START : Initialize Ledger ===========');
        const rrs: BRS[] = [];
        console.info('============= END : Initialize Ledger ===========');
    }

    public async queryBRS(ctx: Context, rrNumber: string): Promise<string> {
        const rrAsBytes = await ctx.stub.getState(rrNumber); // get the rr from chaincode state
        if (!rrAsBytes || rrAsBytes.length === 0) {
            throw new Error(`${rrNumber} does not exist`);
        }
        console.log(rrAsBytes.toString());
        return rrAsBytes.toString();
    }

    public async saveBRS(ctx: Context, object: BRS) {
        console.log(object);
        console.log(object.number);
        console.log(object.entry_date);
        console.log(object.debit_account);
        console.log(object.credit_account);
        console.log(object.ba);
        console.log(object.lbo);
        console.log(object.pofr);
        console.info('============= START : Create BRS ===========');

        await ctx.stub.putState(object.number, Buffer.from(JSON.stringify(object)));
        console.info('============= END : Create BRS ===========');
    }

    public async queryAllBRSs(ctx: Context): Promise<string> {
        const startKey = '';
        const endKey = '';
        const allResults = [];
        for await (const {key, value} of ctx.stub.getStateByRange(startKey, endKey)) {
            const strValue = Buffer.from(value).toString('utf8');
            let record;
            try {
                record = JSON.parse(strValue);
            } catch (err) {
                console.log(err);
                record = strValue;
            }
            allResults.push({ Key: key, Record: record });
        }
        console.info(allResults);
        return JSON.stringify(allResults);
    }
}