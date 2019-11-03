package com.fermi.calibration.actions

import com.fermi.calibration.models.{Calibrated, CalibrationStatus, NinetyPercentConfidenceIntervalRangeAnswer, NinetyPercentConfidenceIntervalRangeQuestion, Overconfident, Underconfident}

object ScoreAnswersToQuestions {
  /**
   * Scores answers for ninety percent confidence interval questions and answers
   * @param submittedAnswers
   * @return
   */
  def apply(submittedAnswers: Seq[(NinetyPercentConfidenceIntervalRangeQuestion, NinetyPercentConfidenceIntervalRangeAnswer)]): CalibrationStatus = {
    val totalNumberOfQuestions = submittedAnswers.size

    val totalCorrect = submittedAnswers
      .filter { case(question, answer) => question.isCorrect(answer) }
      .size

    val percentCorrect = BigDecimal(totalCorrect) / BigDecimal(totalNumberOfQuestions)
    val target = BigDecimal("0.9")
    if (percentCorrect < target) {
      Underconfident
    } else if (percentCorrect > target) {
      Overconfident
    } else {
      Calibrated
    }
  }
}
