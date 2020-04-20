# Value Sensitive Rating for Sustainable Consumption
A repository that illustrates the code and provides the Java code for the algorithm presented in the paper version of 20 April 2020.

## Java Project
The project in this repository is a slightly modified version of the project used for the rating calculation in the related field tests. 

### Requirements
- Java over 1.7. Tested and working up to 1.8.
- Code should be executable in Android with slight modifications.
- Guava and JUnit, which should be loaded automatically with Maven.

### Structure
The project found in folder `java_project` and its structure is:
- `src/main/java`: The folder containing the Java source for the project.
-- `ch.ethz.coss.algorithm`: The folder containing all the code related to the algorithm.
-- `ch.ethz.coss.algorithm.ontology`: A full implementation of the ontology in Java.
-- `ch.ethz.coss.algorithm.utilities`: Some Java utilities related to reading files, processing String objects etc. Most of the utilities rely on Java libraries and simple Java examples.
- `src/test/java`: Test files are found under in this folder
-- `algorithm`:  Some of the basic tests that were used when designing the algorithm and also checking if the implementation reproduces equation calculations.

### Run
To run the project, please run the file:
`/java_project/src/main/java/ch/ethz/coss/algorithm/Example.java`
To do so, please either compile the project with Maven and run the resulting jar from the console, or load the project as a Maven project to an IDE of your choice and run the main class.

## Future Work
- Python version of the project
- Python tools for analysing and visualising the ontology

## Contributions and Usage
Please feel free to contribute and more importantly use the project for implementing Value Sensitive Ratings to promote and extend sustainable consumption!
Please respect the MIT license and the owners of the packages this project depends on.

## Maintainance
This branch will be maintained regularly, when the owner schedule allows it!
