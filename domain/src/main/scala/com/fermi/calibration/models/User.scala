package com.fermi.calibration.models

trait User {
  def name: String
}

case class AnonymousUser(name: String) extends User
