(ns otr.demo
  (:require [jsonista.core :as j]
            [malli.core :as m]
            [malli.error :as me]
            [otr.models :as models])
  (:import [org.hyperledger.fabric.contract.annotation Contact Contract
            Default Info Transaction Transaction$TYPE]
           [org.hyperledger.fabric.contract ContractInterface ContractRouter]
           [net.openhft.hashing LongTupleHashFunction]
           [java.util UUID])
  (:gen-class))

(definterface IDemo
  (^String PutBO [^org.hyperledger.fabric.contract.Context ctx ^String data])
  (^String ListBO [^org.hyperledger.fabric.contract.Context ctx])
  (^String PutDO [^org.hyperledger.fabric.contract.Context ctx ^String data])
  (^String ListDO [^org.hyperledger.fabric.contract.Context ctx])
  (^String PutZKR [^org.hyperledger.fabric.contract.Context ctx ^String data])
  (^String ListZKR [^org.hyperledger.fabric.contract.Context ctx])
  (^String PutRR [^org.hyperledger.fabric.contract.Context ctx ^String data])
  (^String ListRR [^org.hyperledger.fabric.contract.Context ctx]))

(defn- parse-validate [model data]
  (let [item (j/read-value data)]
    (when-some [err (m/explain model item)]
      (throw (ex-info "format violations" (me/humanize err))))
    item))

(def id-field "_identity")

(defn- ensure-identity [item]
  (let [func (LongTupleHashFunction/xx128)
        input (j/write-value-as-bytes item)
        [msb lsb] (.hashBytes func input)
        id (.toString (UUID. msb lsb))]
    ; assoc identity, but only if the item does not contain one
    (merge {id-field id} item)))

; generic put
(defn- put-item [ctx data {:keys [model prefix]}]
  (let [stub (.getStub ctx)
        item (ensure-identity (parse-validate model data))
        key (.createCompositeKey stub prefix (into-array String [(get item id-field)]))]
    (.putStringState stub (.toString key) (j/write-value-as-string item))
    (get item id-field)))

; generic list
(defn- list-items [ctx {:keys [model prefix]}]
  (let [stub (.getStub ctx)
        key (.createCompositeKey stub prefix (into-array String []))
        results (.getStateByPartialCompositeKey stub key)
        parse-fn (fn [v] (parse-validate model (.getStringValue v)))]
    (j/write-value-as-string
     (into [] (map parse-fn results)))))

(defrecord ^{Default true
             Contract {:name "basic"
                       :info (Info {:title "Demo Contract"
                                    :description "Basic demo example"
                                    :version "0.0.1"
                                    :contact (Contact {:email "i.bc@otr.ru"
                                                       :name "Ivan Blockchainov"
                                                       :url "https://otr.ru"})})}}
 AssetContract []
  ContractInterface
  IDemo
  ; BO
  (^{Transaction {:intent Transaction$TYPE/SUBMIT}} PutBO [_this ctx data]
    (put-item ctx data {:model models/BO :prefix "BO"}))

  (^{Transaction {:intent Transaction$TYPE/EVALUATE}} ListBO [_this ctx]
    (list-items ctx {:model models/BO :prefix "BO"}))

  ; DO
  (^{Transaction {:intent Transaction$TYPE/SUBMIT}} PutDO [_this ctx data]
    (put-item ctx data {:model models/DO :prefix "DO"}))

  (^{Transaction {:intent Transaction$TYPE/EVALUATE}} ListDO [_this ctx]
    (list-items ctx {:model models/DO :prefix "DO"}))

  ; ZKR
  (^{Transaction {:intent Transaction$TYPE/SUBMIT}} PutZKR [_this ctx data]
    (put-item ctx data {:model models/ZKR :prefix "ZKR"}))

  (^{Transaction {:intent Transaction$TYPE/EVALUATE}} ListZKR [_this ctx]
    (list-items ctx {:model models/ZKR :prefix "ZKR"}))

  ; RR
  (^{Transaction {:intent Transaction$TYPE/SUBMIT}} PutRR [_this ctx data]
    (put-item ctx data {:model models/RR :prefix "RR"}))

  (^{Transaction {:intent Transaction$TYPE/EVALUATE}} ListRR [_this ctx]
    (list-items ctx {:model models/RR :prefix "RR"})))

(defn -main
  "Application entrypoint."
  [& args]
  (ContractRouter/main args))
