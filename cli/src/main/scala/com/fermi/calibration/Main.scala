package com.fermi.calibration

import com.fermi.calibration.actions.ScoreAnswersToQuestions
import com.fermi.calibration.models.{AnonymousUser, NinetyPercentConfidenceIntervalRangeAnswer, NinetyPercentConfidenceIntervalRangeQuestion, Quiz, User}
import com.fermi.calibration.repositories.InMemoryQuizRepository

import scala.io.{Source, StdIn}
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

    val questions = Source.fromResource("confidence_interval_questions.csv").getLines.map(str => {
      val split = str.split(',')
      NinetyPercentConfidenceIntervalRangeQuestion(split(0), split(1).trim().toDouble)
    })

    repo.addQuestions(questions)

    repo
  }

  def giveQuiz(quiz: Quiz[NinetyPercentConfidenceIntervalRangeAnswer, NinetyPercentConfidenceIntervalRangeQuestion]): Unit = {
    def isDouble(s: String): Boolean = Try({s.toDouble}).isSuccess

    val rawQuestionsAndAnswers = for (q <- quiz.questions) yield {
      println(q.text)
      val line = StdIn.readLine()
      val response = {
        val split = line.split(' ')
        // Check to see if we got a valid response back.
        // If we didn't, we need to do the best we can with it
        if (split.isEmpty || split.size == 1) {
          None
        } else if (isDouble(split(0)) && isDouble(split(1))) {
          Some((split(0).toDouble, split(1).toDouble))
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
