(ns odm.code-list-item
  "3.1.1.3.7.1 - CodeListItem

  Defines an individual member value of a codelist including display format. The
  actual value is given, along with a set of print/display-forms. The CodedValue
  must be an acceptable value of the DataType of the containing CodeList."
  (:require
    #?(:clj [clojure.spec.alpha :as s]
       :cljs [cljs.spec.alpha :as s])
            [odm.data-formats :as df]))

;; Value of the codelist item (as it would occur in clinical data).
(s/def ::coded-value
  ::df/text)

;; Numeric significance of the CodeListItem relative to others in the CodeList.
;; Rank is optional, but if given for any CodeListItems in a CodeList it must
;; be given for all.
(s/def ::rank
  ::df/float)

;; The displayed value relating to the CodedValue.
(s/def ::decode
  ::df/translated-text)

(s/def :odm/code-list-item
  (s/keys :req [::coded-value ::decode]
          :opt [::rank :odm/order-number :odm/aliases]))
