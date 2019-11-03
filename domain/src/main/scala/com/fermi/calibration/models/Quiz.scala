package com.fermi.calibration.models

class Quiz[A <: Answer, B <: Question[A]](val questions: Seq[B]) {
}
