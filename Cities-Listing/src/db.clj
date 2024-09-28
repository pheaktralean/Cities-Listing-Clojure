;
; Assignment 3 Clojure
; Written by: Sopheaktra Lean - 40225014
; Comp 348 Section AA Summer 2024
;

(ns db
  (:require [clojure.string :as str])) ; Import clojure.string library to manipulate string

(defn load-data [filename]  ; Function to load data from the input file
  (slurp filename))

(defn parse-int [s]
  (try
    (Integer/parseInt s)
    (catch NumberFormatException e
      (println "Error parsing integer:" s)
      nil)))

(defn remove-pipe [s]
  (str/replace s #"\|" " "))

(defn process-line [line]   ; Manipulating each line in the file
  (let [segments (str/split line #"\|")]   ; Removing pipe
    (let [name (str/trim (nth segments 0))  ; Assigning the first segment to be the city name
          province (str/trim (nth segments 1))  ; Assigning the second segment to be province
          type (str/trim (nth segments 2))  ; Assigning the third segment to be a type
          population (parse-int (nth segments 3)) ; Assigning the fourth segment to be the population
          area (Double/parseDouble (nth segments 4))] ; Assigning the fifth segment to be the area
      {:name       name
       :province   province
       :type       type
       :population population
       :area       area})))

(defn process-data [data]    ; Function to add the manipulated lines to data
  (->> (str/split-lines data)
       (map process-line)))

(defn extract-and-sort-cities [data]  ; Sorting the cities by the specific province
  (->> data
       (map :name)
       (sort)))

(defn list-cities-alphabetically [data]  ; Listing cities alphabetically
  (->> data
       (map :name)
       (distinct)
       (sort)))

(defn format-city-info [city]  ; Function to format city information as a string
  (str "[\"" (:name city) "\" \"" (:type city) "\" " (:population city) "]"))

(defn list-cities-by-province [data province] ; Function to list cities by province, ordered by size and name
  (let [filtered-data (filter #(= (:province %) province) data) ; Filtering cities by the given province
        sorted-data (sort-by
                      (fn [city]
                        [(case (:type city) ; Determining the order based on the city type
                           "Large urban" 1
                           "Medium" 2
                           "Small" 3)
                         (:name city)]) ; Sorting by city name in ascending order
                      filtered-data)]    ; Applying the sorting to the filtered data
    (if (seq sorted-data)  ; Checking if there are any cities in the sorted data
      (doseq [city sorted-data]  ; Iterating over each city in the sorted data
        (println (format-city-info city)))  ; Printing the formatted city information
      (println "You have entered an invalid province. Please try again!")) ; Printing an error message if no cities found
    ))

(defn calculate-population-density [city]  ; Function to calculate the population density of each city
  (let [population (double (:population city))
        area (double (:area city))]
    (if (zero? area)  ; If area is not 0, the population density = population /area
      0
      (/ population area))))

(defn list-cities-by-population-density [data province] ; Function to list cities by population density
  (let [filtered-data (filter #(= (:province %) province) data) ; Filtering the cities by the given province
        sorted-data (sort-by :population filtered-data)] ; Sorting the filtered data by population
    (if (seq sorted-data) ; Checking if there are any cities in the sorted data
      (doseq [city sorted-data]  ; Iterating over each city in the sorted data
        (println (format-city-info city)))  ; Displaying the formatted city information
      (println "You have entered an invalid province. Please try again!")))) ; Printing an error message if no cities found

(defn display-city-information [data city-name] ; Display city information in a formatted way
  (let [city (first (filter #(= (:name %) city-name) data))]
    (if city
      (do
        (print "City Information:  [\"")
        (print (:name city))
        (print "\"  \"")
        (print (:province city))
        (print "\"  \"")
        (print (:type city))
        (print "\"  ")
        (print (:population city))
        (print "   ")
        (print (:area city))
        (print "]"))
      (println "The city you have entered is not found."))))

(defn list-provinces-with-city-count [data]  ; Function to list provinces with their total number of cities found in the file
  (->> data
       (map :province)
       (frequencies)
       (sort-by (fn [[province count]] [(-> count -) province]))))

(defn calculate-province-populations [data] ; Function to calculation the total population of each province
  (reduce
    (fn [acc city]
      (let [current-population (or (get acc (:province city)) 0)]
        (assoc acc (:province city) (+ current-population (:population city)))))
    {}
    data))

; Function to list provinces with their total population, ordered by province name
(defn list-provinces-with-population [data]
  (let [province-populations (calculate-province-populations data)
        sorted-provinces (sort-by key province-populations)]
    (map-indexed
      (fn [index [province total-population]]
        (str (inc index) ": [\"" province "\" " total-population "]")) ; Format the output
      sorted-provinces)))
