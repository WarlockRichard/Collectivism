name := "testcase"

version := "0.1"

scalaVersion := "2.12.5"

libraryDependencies ++= Seq("org.scalatest" %% "scalatest" % "3.2.0-SNAP10", "org.scalatest" %% "scalatest" % "3.0.5" % "test")
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"