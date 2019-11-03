package com.fermi.calibration

import com.fermi.calibration.actions.ScoreAnswersToQuestions
import com.fermi.calibration.models.{AnonymousUser, NinetyPercentConfidenceIntervalRangeAnswer, NinetyPercentConfidenceIntervalRangeQuestion, Quiz, User}
import com.fermi.calibration.repositories.InMemoryQuizRepository

import scala.io.StdIn
import scala.util.Try

object Main extends App {
  val repository = getQuizRepository()

  println("Welcome to the estimation calibration trainer")
  println("Ready to begin first quiz? y/n")
  val input = StdIn.readLine()
  if (input.toLowerCase() == "y") {
    val user = new AnonymousUser("a user")
    println("You will now receive a series of questions that will have an exact answer but you should respond with a range.")
    println("For each question, give both a low and high answer where you believe the correct answer is greater than or equal to the low answer and less than or equal to the high answer.")
    println("For example: If the question was 'how many feet do I have?' you could respond with '1 3'")
    startQuiz(user)
  } else {
    println("Ok try again later.")
  }

  def startQuiz(user: User): Unit = {
    // start quiz
    val quiz = repository.buildQuiz(user)
    quiz match {
      case Some(q) => {
        giveQuiz(q)
        println("Another? y/n")
        val response = StdIn.readLine()
        if (response.toLowerCase() == "y") {
          startQuiz(user)
        }
      }
      case None => {
        println("Sorry I'm out of questions, bye!")
      }
    }
  }

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
    def isInt(s: String): Boolean = Try({s.toInt}).isSuccess

    val rawQuestionsAndAnswers = for (q <- quiz.questions) yield {
      println(q.text)
      val line = StdIn.readLine()
      val response = {
        val split = line.split(' ')
        // Check to see if we got a valid response back.
        // If we didn't, we need to do the best we can with it
        if (split.isEmpty || split.size == 1) {
          None
        } else if (isInt(split(0)) && isInt(split(1))) {
          Some((split(0).toInt, split(1).toInt))
        } else {
          None
        }
      }

      (q, response)
    }

    val questionsAndAnswers = rawQuestionsAndAnswers
      .collect { case (q, Some((low, high))) => (q, NinetyPercentConfidenceIntervalRangeAnswer(low, high))}

    val calibration = ScoreAnswersToQuestions(questionsAndAnswers)

    println(s"Your result: ${calibration.toString}")
  }

}
