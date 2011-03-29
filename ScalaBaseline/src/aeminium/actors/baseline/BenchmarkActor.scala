package aeminium.actors.baseline

import scala.actors.Actor
import aeminium.actormodel.examples.dictionary.BenchmarkBackend

case object Stop
case class GetValue(val key:Int)
case class SetValue(val key:Int, val obj:Int)

class BenchmarkActor() extends Actor {
	val backend:BenchmarkBackend = new BenchmarkBackend(BenchmarkActor.messages);
	
	def act() {
		loop {
			react {
				case GetValue(i) => backend.getValue(i)
				case SetValue(i,o) => backend.setValue(i,o)
				case Stop =>
					sender ! Stop
					exit()
			}
		}
	}
	
}

object BenchmarkActor {
	val messages = 1000;
	
	def main(args: Array[String]) {
		val startTime = System.nanoTime()
		
		val ba = new BenchmarkActor
		ba.start
		1.to(messages).foreach { i =>
			if (i % 5 == 0) {
				ba ! SetValue(i,10)
			} else {
				ba ! GetValue(i)
			}
		}
		ba !? Stop
		java.lang.System.out.println("Took (nanoseconds): " + (System.nanoTime() - startTime))
	}
}