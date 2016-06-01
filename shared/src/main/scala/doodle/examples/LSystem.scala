package doodle
package examples

import doodle.core._
import doodle.syntax._
import doodle.turtle._

object LSystem {
  import Instruction._

  def iterate(steps: Int, seed: List[Instruction], rule: Instruction => List[Instruction]): List[Instruction] = {
    def rewrite(instructions: List[Instruction]): List[Instruction] =
      instructions.flatMap {
        case Branch(i) =>
          List(branch(rewrite(i)))
        case other =>
          rule(other)
      }

    steps match {
      case 0 =>
        seed
      case n =>
        val rewritten = rewrite(seed)
        iterate(steps - 1, rewritten, rule)
    }
  }

  object tree {
    val left = turn(25.degrees)
    val right = turn(-25.degrees)
    val f = forward(5)

    val rule = (i: Instruction) => {
      i match {
        case NoOp => //  F−[[X]+X]+F[+FX]−X
          List(f, left, branch(List(branch(List(noop)), right, noop)), right, f,
               branch(List(right, f, noop)), left, noop)

        case Forward(d) =>
          List(f, f)

        case other =>
          List(other)
      }
    }

    val image = Turtle.draw(iterate(6, List(noop), rule), 50.degrees).lineColor(Color.forestGreen)
  }

  object flowers {
    val f = forward(5)
    val spin = turn(51.degrees)
    val spur = List(spin, branch(List(f, noop)))

    val rule = (i: Instruction) => {
      i match {
        case Forward(d) => List(f, f, f)
        case NoOp => spur ++ spur ++ spur ++ spur ++ spur ++ spur ++ spur
        case other => List(other)
      }
    }

    val image = Turtle.draw(iterate(5, List(noop), rule))
  }
}
