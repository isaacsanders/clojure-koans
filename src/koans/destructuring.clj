(def test-address
  {:street-address "123 Test Lane"
   :city "Testerville"
   :state "TX"})

(defn join [coll string]
  (loop [lat (rest coll)
         at (first coll)
         acc (str)]
    (if (empty? lat)
      (str acc at)
      (recur (rest lat) (first lat) (str acc at string)))))

(meditations
  "Destructuring is an arbiter: it breaks up arguments"
  (= ":bar:foo" ((fn [[a b]] (str b a))
           [:foo :bar]))

  "Whether in function definitions"
  (= (str "First comes love, "
          "then comes marriage, "
          "then comes Clojure with the baby carriage")
     ((fn [[a b c]] (str (str "First comes " a ", ")
                         (str "then comes " b ", ")
                         (str "then comes " c
                              " with the baby carriage")))
       ["love" "marriage" "Clojure"]))

  "Or in let expressions"
  (= "Rich Hickey aka The Clojurer aka Go Time aka Macro Killah"
     (let [[first-name last-name & aliases]
             (list "Rich" "Hickey" "The Clojurer" "Go Time" "Macro Killah")]
       (join [first-name last-name "aka" (join aliases " aka ")] " ")))

  "You can regain the full argument if you like arguing"
  (= {:original-parts ["Steven" "Hawking"] :named-parts {:first "Steven" :last "Hawking"}}
     (let [[first-name last-name :as full-name] ["Steven" "Hawking"]]
         {:original-parts full-name
          :named-parts {:first first-name
                        :last last-name}} ))

  "Break up maps by key"
  (= "123 Test Lane, Testerville, TX"
     (let [{street-address :street-address, city :city, state :state} test-address]
            (join [street-address city state] ", ")))

  "Or more succinctly"
  (= "123 Test Lane, Testerville, TX"
     (let [{:keys [street-address city state]} test-address]
            (join [street-address city state] ", ")))

  "All together now!"
  (= "Test Testerson, 123 Test Lane, Testerville, TX"
     ((fn [full-name address]
        (str (join [(join full-name " ")
                    (address :street-address)
                    (address :city)
                    (address :state)] ", "))) ["Test" "Testerson"] test-address))
)
