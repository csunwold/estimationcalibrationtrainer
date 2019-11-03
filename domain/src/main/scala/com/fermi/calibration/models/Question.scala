package com.fermi.calibration.models

trait Question[A <: Answer] {
  def text: String

  def isCorrect(answer: A): Boolean
}