;
; Assignment 3 Clojure
; Written by: Sopheaktra Lean - 40225014
; Comp 348 Section AA Summer 2024
;

(ns app
  (:require [db :refer :all]
            [menu :refer :all]))

; Implementing -main function to run the program
(defn -main []
  (print "Welcome to our program!") ; Displaying a welcome msg to the user
  (let [file-name "cities.txt"
        pre-processed-data (load-data file-name) ; Loading the input file into the system
        post-processed-data (process-data pre-processed-data)] ; Processing the data into a formatted one
    (loop [] ; Interacting with user by displaying a menu until user choose an option to exit
      (display-menu)
      (let [choice (prompt-user)]
        (handle-choice choice post-processed-data)) ; Handling user's option
      (recur))))

(-main)