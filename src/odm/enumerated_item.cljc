(ns odm.enumerated-item
  "3.1.1.3.7.3 - EnumeratedItem

  Defines an individual member value of a valid value list. Only the coded value
  is given. If print/display-forms are needed, CodeListItem should be used.
  CodeListItems and EnumeratedItems may not be mixed within a single codelist.
  The CodedValue must be an acceptable value of the DataType of the containing
  CodeList."
  (:require
    #?(:clj [clojure.spec :as s]
       :cljs [cljs.spec :as s])
            [odm.data-formats :as df]))

;; Value of the enumerated item (as it would occur in clinical data).
(s/def ::coded-value
  ::df/text)

;; Numeric significance of the EnumeratedItem relative to others in the CodeList.
;; Rank is optional, but if given for any EnumeratedItems in a CodeList it must
;; be given for all.
(s/def ::rank
  ::df/float)

(s/def :odm/enumerated-item
  (s/keys :req [::coded-value]
          :opt [::rank :odm/order-number :odm/aliases]))
