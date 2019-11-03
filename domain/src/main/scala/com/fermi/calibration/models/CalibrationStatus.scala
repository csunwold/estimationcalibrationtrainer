package com.fermi.calibration.models

sealed trait CalibrationStatus

case object Overconfident extends CalibrationStatus

case object Calibrated extends CalibrationStatus

case object Underconfident extends CalibrationStatus