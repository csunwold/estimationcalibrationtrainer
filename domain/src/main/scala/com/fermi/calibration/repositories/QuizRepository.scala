package com.fermi.calibration.repositories

import com.fermi.calibration.models.{Answer, Question, Quiz, User}

trait QuizRepository[A <: Answer, Q <: Question[A]] {
  def buildQuiz(user: User) : Option[Quiz[A, Q]]
}
