# mercury


## Installation

Download from https://github.com/app/mercury

## Create new projects

* Create a project directory structure

  ```bash
  clj -Tnew app :name mygituser/myappname
  ```

* Run tests or build.

  ```clojure
  clj -T:build test  # Test only.
  
  clj -T:build ci    # Test, write pom, build uberjar.
  ```

* Run the app's '-main' function.

  ```bash
  clj -M:run-m
  ```
