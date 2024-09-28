(ns firstproject.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [firstproject.app :as core]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 (core/foo 1)))))
