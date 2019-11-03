package com.fermi.calibration.models

/**
 * A question designed to test whether a person can accurately estimate a specific number with a 90% confidence interval
 * @param text            The text of the question
 * @param correctAnswer   The exact correct answer to the question
 */
case class NinetyPercentConfidenceIntervalRangeQuestion(text: String, correctAnswer: Int) extends Question[NinetyPercentConfidenceIntervalRangeAnswer] {
  def isCorrect(answer: NinetyPercentConfidenceIntervalRangeAnswer): Boolean =
    answer.high >= correctAnswer && answer.low <= correctAnswer
}

/**
 * A submitted answer to a confidence interval question
 * @param low The low end estimate. This number represents the belief that there is a less than 5% chance the correct
 *            answer is below this value.
 * @param high The high end estimate. This number represents the belief that there is a less than 5% change the correct
 *             answer is above this value.
 */
case class NinetyPercentConfidenceIntervalRangeAnswer(low: Int, high: Int) extends Answer