/*
 * SPDX-License-Identifier: Apache-2.0
 */
export class BRS {
    public number: string;
    public entry_date: string;
    public debit_account: string;
    public credit_account: string;
    public ba?: BRSpart[];
    public lbo?: BRSpart[];
    public pofr?: BRSpart[];
}


class BRSpart {
    public code: string;
    public subCode?: string;
    public amount: number;
}