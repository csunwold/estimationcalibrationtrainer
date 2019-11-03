package com.fermi.calibration

import com.fermi.calibration.actions.ScoreAnswersToQuestions
import com.fermi.calibration.models.{AnonymousUser, NinetyPercentConfidenceIntervalRangeAnswer, NinetyPercentConfidenceIntervalRangeQuestion, Quiz}
import com.fermi.calibration.repositories.InMemoryQuizRepository

import scala.io.StdIn

object Main extends App {
  val repository = getQuizRepository()

  println("Welcome to the estimation calibration trainer")
  println("Ready to begin first quiz? y/n")
  val input = StdIn.readLine()
  if (input == "y") {
    // start quiz
    val user = new AnonymousUser("a user")
    val quiz = repository.buildQuiz(user)
    quiz match {
      case Some(q) => giveQuiz(q)
      case None => {
        println("Sorry I'm out of questions, bye!")
      }
    }
  } else {
    println("Ok try again later.")
  }

  // Get quiz
  // take quiz
  // output score
  // another quiz?
  // repeat

  def getQuizRepository(): InMemoryQuizRepository = {
    val repo = new InMemoryQuizRepository

    repo.addQuestions(Seq(
      NinetyPercentConfidenceIntervalRangeQuestion("What is the distance in miles from the earth to the moon?", correctAnswer = 238900),
      NinetyPercentConfidenceIntervalRangeQuestion("How tall is the space needle in feet?", correctAnswer = 605),
      NinetyPercentConfidenceIntervalRangeQuestion("How tall is the columbia tower to the tip in feet?", correctAnswer = 967),
      NinetyPercentConfidenceIntervalRangeQuestion("What is the distance in miles between Los Angeles and Las Vegas?", correctAnswer = 225),
      NinetyPercentConfidenceIntervalRangeQuestion("What is the distance in miles between Los Angeles and Washington DC?", correctAnswer = 2299),
      NinetyPercentConfidenceIntervalRangeQuestion("What is the population of Costa Rica as of 2019?", correctAnswer = 4906000),
      NinetyPercentConfidenceIntervalRangeQuestion("What is the population of Oklahoma City as of 2017?", correctAnswer = 643648),
      NinetyPercentConfidenceIntervalRangeQuestion("What is the population of Orlando, FL as of 2017?", correctAnswer = 280257),
      NinetyPercentConfidenceIntervalRangeQuestion("What is the population of Las Vegas, NV as of 2017?", correctAnswer = 641676),
      NinetyPercentConfidenceIntervalRangeQuestion("What is the population of Myrtle Beach, SC as of 2017?", correctAnswer = 32795),
      NinetyPercentConfidenceIntervalRangeQuestion("What is the population of Paris, France as of 2019?", correctAnswer = 2141000),
      NinetyPercentConfidenceIntervalRangeQuestion("How many square miles is Venice, Italy?", correctAnswer = 160),
      NinetyPercentConfidenceIntervalRangeQuestion("What is the population of Vancouver, BC as of 2017?", correctAnswer = 775218),
      NinetyPercentConfidenceIntervalRangeQuestion("How many square miles is Alaska?", correctAnswer = 663300)
    ))

    repo
  }

  def giveQuiz(quiz: Quiz[NinetyPercentConfidenceIntervalRangeAnswer, NinetyPercentConfidenceIntervalRangeQuestion]): Unit = {
    val questionsAndAnswers = for (q <- quiz.questions) yield {
      println(q.text)
      val line = StdIn.readLine()
      val (low, high) = {
        val split = line.split(' ')
        (split(0).toInt, split(1).toInt)
      }

      (q, NinetyPercentConfidenceIntervalRangeAnswer(low, high))
    }

    val calibration = ScoreAnswersToQuestions(questionsAndAnswers)

    println(s"Your result: ${calibration.toString}")
  }

}
