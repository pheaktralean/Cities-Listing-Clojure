;
; Assignment 3 Clojure
; Written by: Sopheaktra Lean - 40225014
; Comp 348 Section AA Summer 2024
;

(ns menu
  (:require [db :refer :all]))

(defn display-menu []  ; Storing the main menu to interact with the user
  (println "\n                    *** City Information Menu ***")
  (println "---------------------------------------------------------------------------")
  (println "(1) List Cities")
  (println "     (1.1) List All Cities")
  (println "     (1.2) List All Cities For A Given Province (ordered by size and name)")
  (println "     (1.3) List All Cities For A Given Province (ordered by population)")
  (println "(2) Display City Information")
  (println "(3) List Provinces")
  (println "(4) Display Province Information")
  (println "(5) Exit")
  (println "---------------------------------------------------------------------------")
  (print "Please enter your choice: "))

(defn prompt-user [] ; Prompting the user for the option
  (flush) ; Flushing every time when we are about to prompt the user
  (read-line)) ; Reading the user's input

(defn handle-choice [choice data]
  (cond
    (= choice "1.1") ; Implementing this operation if user enters 1.1
    (do
      (println "List of all cities:")
      (print "[")
      (let [sorted-cities (db/list-cities-alphabetically data)]
        (doseq [city sorted-cities]
          (print "\"")
        (print city)
        (print "\"  ")))
      (print "]"))

    (= choice "1.2") ; Implementing this operation if user enters 1.2
    (do
      (print "Enter the province: ")
      (flush)
      (let [province (read-line)]
        (let [sorted-cities (list-cities-by-province data province)]
          (doseq [city-info sorted-cities]
            (println (select-keys city-info [:name :type :population]))))))

    (= choice "1.3") ; Implementing this operation if user enters 1.3
    (do
      (print "Enter the province: ")
      (flush)
      (let [province (read-line)]
        (let [sorted-cities (list-cities-by-population-density data province)]
          (doseq [city sorted-cities]
            (println (select-keys city [:name :area :population]))))))

    (= choice "2") ; Implementing this operation if user enters 2
    (do
      (print "Enter the city name: ")
      (flush)
      (let [city-name (read-line)]
        (display-city-information data city-name)))

    (= choice "3") ; Implementing this operation if user enters 3
    (do
      (let [province-city-counts (list-provinces-with-city-count data)]
        (println "Provinces with total number of cities:")
        (doseq [[province count] province-city-counts]
          (println (str (inc (.indexOf province-city-counts [province count])) ": [\"" province "\" " count "]")))
        (println (str "Total: " (count province-city-counts) " provinces, " (count data) " cities on file."))))

    (= choice "4") ; Implementing this operation if user enters 4
    (do
      (println "Provinces with total population, sorted by name:")
      (let [sorted-provinces (list-provinces-with-population data)]
        (doseq [province-info sorted-provinces]
          (println province-info))))

    (= choice "5") ; Implementing this operation if user enters 5
    (do
      (println "You have successfully exited the program. GOODBYE!")
      (System/exit 0))

    :else  ; Implementing this operation if user enters invalid option
    (println "You have entered an invalid choice. Please try again.")))
