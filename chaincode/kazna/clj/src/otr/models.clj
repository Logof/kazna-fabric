(ns otr.models
  (:require [ malli.generator :as mg ]))

; describe schemas

(def Account [ :string {:min 1 :max 200} ])
(def KBK [ :re #"^\d{20}$" ])
(def Amount [ :int {:min 0} ])
(def subCode [ :string {:min 0 :max 10} ])

(def ReasonDocument
  [ :map
   [ "number" {:title "Document Number" :max 10} :string ]
   [ "caption" {:title "Document Caption" :max 250} :string ]
   [ "uri" {:title "Document URI"} [ :re #"^http://s3\.local/\d{5}/\d{10}$" ] ] ])

(def BO [ :map
          {:registry {:bcc-amount [ :map [ "code" KBK ] [ "subCode" subCode ] [ "amount" Amount ]  ]}}
          [ "personal_account" {:title "Personal Account"} Account ]
          [ "receiver_account" {:title "Receiver Account"} Account ]
          [ "reason_documents" {:title "Reason Documents"} [ :repeat {:min 0, :max 10} ReasonDocument  ] ]
          [ "decoding" {:title "Decoding"} [  :repeat  {:min 0, :max 10} :bcc-amount  ] ]
        ])

(def DO [ :map
          {:registry {:bcc-amount [ :map [ "code" KBK ] [ "subCode" subCode ] [ "amount" Amount ] [ "prepaidAmount" Amount ]  ]}}
          [ "personal_account" {:title "Personal Account"} Account ]
          [ "receiver_account" {:title "Receiver Account"} Account ]
          [ "reason_documents" {:title "Reason Documents"} [ :repeat {:min 0, :max 10} ReasonDocument ] ]
          [ "decoding" {:title "Decoding"}  [ :repeat {:min 1, :max 10} :bcc-amount ] ]
        ])

(def ZKR [ :map
          {:registry {:bcc-amount [ :map [ "code" KBK ] [ "subCode" subCode ] [ "amount" Amount ] [ "is_prepaid" :boolean ] ]}}
          [ "debit_account" {:title "Debit Account"} Account ]
          [ "receiver_account" {:title "Receiver Account"} Account ]
          [ "budget_obligation" {:title "Budget Obligation" :min 1 :max 10} :string ]
          [ "monetary_obligation" {:title "Monetary Obligation" :min 1 :max 10} :string ]
          [ "decoding" {:title "Decoding"} [ :repeat {:min 1} :bcc-amount  ] ] ])

(def RR [ :map {:registry {:bcc-amount [ :map [ "code" KBK ] [ "subCode" subCode ] [ "amount" Amount ] ]}}
         [ "entry_date" {:title "Entry Date"} [ :string {:min 10 :max 10} ] ]
         [ "debit_account" {:title "Debit Account"} Account ]
         [ "credit_account" {:title "Credit Account"} Account ]
         [ "ba" [ :repeat {:min 0, :max 10} :bcc-amount ] ]
         [ "lbo" [ :repeat {:min 0, :max 10} :bcc-amount ] ]
         [ "pofr" [ :repeat {:min 0, :max 10} :bcc-amount ] ] ])

; value sampling

(def BO-generator (mg/generator BO))
(def DO-generator (mg/generator DO))
(def ZKR-generator (mg/generator ZKR))
(def RR-generator (mg/generator RR))

(defn BO-sample
  ([  ] (mg/generate BO-generator))
  ([ n ] (mg/sample BO-generator {:size n})))

(defn DO-sample
  ([  ] (mg/generate DO-generator))
  ([ n ] (mg/sample DO-generator {:size n})))

(defn ZKR-sample
  ([  ] (mg/generate ZKR-generator))
  ([ n ] (mg/sample ZKR-generator {:size n})))

(defn RR-sample
  ([  ] (mg/generate RR-generator))
  ([ n ] (mg/sample RR-generator {:size n})))

(comment
  (require '[ jsonista.core :as j ])

  (j/write-value-as-string (RR-sample))
  (BO-sample)
  (BO-sample 5)

  (DO-sample)
  (DO-sample 5)

  (ZKR-sample)
  (ZKR-sample 5)

  (RR-sample)
  (RR-sample 5))
