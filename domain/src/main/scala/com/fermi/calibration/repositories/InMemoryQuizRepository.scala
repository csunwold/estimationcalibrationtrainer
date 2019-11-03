package com.fermi.calibration.repositories

import com.fermi.calibration.models.{NinetyPercentConfidenceIntervalRangeAnswer, NinetyPercentConfidenceIntervalRangeQuestion, Quiz, User}

import scala.collection.mutable
import scala.util.Random

/**
 * Big stateful dump of Ninety Percent Confidence interval questions. Nothing is persisted.
 *
 * It does keep track of what questions have been given to the user previously so that they don't get the same question.
 *
 * This class is not thread safe.
 */
class InMemoryQuizRepository extends QuizRepository[NinetyPercentConfidenceIntervalRangeAnswer, NinetyPercentConfidenceIntervalRangeQuestion] {
  private val questionBank = new mutable.HashMap[NinetyPercentConfidenceIntervalRangeQuestion, mutable.Set[User]].empty
  private val targetNumberOfQuestions = 10

  def addQuestions(questions: Iterator[NinetyPercentConfidenceIntervalRangeQuestion]): Unit = {
    questions
      // We only want to add new questions that aren't already in the question bank
      // Otherwise we risk wiping out which user has seen this before.
      .filterNot(q => questionBank.contains(q))
      .foreach(q => questionBank.put(q, mutable.Set.empty))
  }
  /**
   * Builds a quiz based on Ninety percent confidence interval questions. Ensures that only questions are given to the
   * user if they haven't seen them before.
   * @param user The user who wants to take a quiz
   * @return A quiz of at most 10 unique questions that the user hasn't seen before. Will return None if there are no
   *         available questions.
   */
  override def buildQuiz(user: User): Option[Quiz[NinetyPercentConfidenceIntervalRangeAnswer, NinetyPercentConfidenceIntervalRangeQuestion]] = {
    val unseenQuestions = questionBank.filterNot { case (_, seenUsers) =>  seenUsers.contains(user)}
    if (unseenQuestions.isEmpty) {
      // Bail out early if there are no questions that haven't already been seen
      None
    } else if (unseenQuestions.size <= targetNumberOfQuestions) {
      // If we there aren't more than 10 questions left, return them all
      Some(new Quiz(unseenQuestions.keys.toSeq))
    } else {
      // Find 10 random questions
      var remainingQuestions = unseenQuestions.keys.toIndexedSeq
      val selectedQuestions = for {_ <- Range(0, 10)} yield {
        val selectedQuestion = remainingQuestions(Math.abs(Random.nextInt()) % remainingQuestions.size)
        questionBank(selectedQuestion).add(user)
        remainingQuestions = remainingQuestions.filterNot(q => q == selectedQuestion)
        selectedQuestion
      }

      Some(new Quiz(selectedQuestions))
    }
  }
}
