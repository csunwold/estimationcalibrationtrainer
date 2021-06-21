# Estimation Calibration Trainer
An application that helps calibrate you as an estimator. 

[![CircleCI](https://circleci.com/gh/csunwold/estimationcalibrationtrainer/tree/master.svg?style=svg)](https://circleci.com/gh/csunwold/estimationcalibrationtrainer/tree/master)

## Dependencies
* Maven
* Scala 2.13

## How to build
```
$ make
```
## How to run
```
$ make run
```

## Example run
```$xslt
Welcome to the estimation calibration trainer
Ready to begin first quiz? y/n
y
You will now receive a series of questions that will have an exact answer but you should respond with a range.
For each question, give both a low and high answer where you believe the correct answer is greater than or equal to the low answer and less than or equal to the high answer.
For example: If the question was 'how many feet do I have?' you could respond with '1 3'
What is the population of Vancouver, BC as of 2017?
400000 800000
What is the distance in miles from the earth to the moon?
34000000 100000000
What is the population of Costa Rica as of 2019?
2000000 10000000
What is the population of Paris, France as of 2019?
2000000 10000000
How tall is the space needle in feet?
400 800
What is the population of Oklahoma City as of 2017?
300000 800000
What is the population of Myrtle Beach, SC as of 2017?
25000 50000
What is the population of Las Vegas, NV as of 2017?
500000 900000
How many square miles is Venice, Italy?
140 190
What is the distance in miles between Los Angeles and Washington DC?
2000 4000
Your result: Calibrated
```
## See also
* https://en.wikipedia.org/wiki/Calibrated_probability_assessment
