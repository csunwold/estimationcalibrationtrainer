package com.fermi.calibration.scenarios

import com.fermi.calibration.actions.ScoreAnswersToQuestions
import com.fermi.calibration.models.{NinetyPercentConfidenceIntervalRangeAnswer, NinetyPercentConfidenceIntervalRangeQuestion, Overconfident, Quiz}
import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}

class NewUserTakingATestSpec extends FeatureSpec with GivenWhenThen with Matchers {
  feature("Taking a test") {
    info("As a new user")
    info("I want to be able to take a test and get a result")
    info("So that I can see how calibrated I am")

    scenario("A new user take a test they haven't taken before") {
      Given("A test with 3 questions")
      val quiz = new Quiz[NinetyPercentConfidenceIntervalRangeAnswer, NinetyPercentConfidenceIntervalRangeQuestion](questions = Seq(
        NinetyPercentConfidenceIntervalRangeQuestion("What is the distance in miles from the earth to the moon?", correctAnswer = 238900),
        NinetyPercentConfidenceIntervalRangeQuestion("How tall is the space needle in feet?", correctAnswer = 605),
        NinetyPercentConfidenceIntervalRangeQuestion("How tall is the columbia tower to the tip in feet?", correctAnswer = 967)
      ))

      When("The user submits answers that include the right answer to all three questions")
      val result = ScoreAnswersToQuestions(quiz.questions.zip(Seq(
        NinetyPercentConfidenceIntervalRangeAnswer(1, 100000000),
        NinetyPercentConfidenceIntervalRangeAnswer(1, 100000000),
        NinetyPercentConfidenceIntervalRangeAnswer(1, 100000000)
      )))

      Then("The calibration status of Overconfident should be returned")
      result should be (Overconfident)
    }
  }
}
