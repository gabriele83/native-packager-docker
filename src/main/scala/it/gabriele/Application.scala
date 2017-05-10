package it.gabriele

import com.typesafe.config.{Config, ConfigFactory}


/**
  * Created by gabriele on 16/05/16.
  */
object Application {

  def main(args: Array[String]): Unit = println(s"---> ${ConfigFactory.load.getString("env")} <---")

}