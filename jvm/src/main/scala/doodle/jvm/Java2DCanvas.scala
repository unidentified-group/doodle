package doodle
package jvm

import doodle.core._
import doodle.backend.{Configuration, Draw, Interpreter, Save}
import doodle.backend.Formats.Png

object Java2DCanvas extends Draw with Save[Png] {
  implicit val java2DCanvas: Java2DCanvas.type =
    this

  def draw(interpreter: Configuration => Interpreter, image: Image): Unit = {
    new CanvasFrame(interpreter, image)
  }

  def save[F <: Png](fileName: String, interpreter: Configuration => Interpreter, image: Image): Unit = {
    import java.io.File
    import java.awt.image.BufferedImage
    import javax.imageio.ImageIO

    val metrics = Java2D.bufferFontMetrics
    val dc = DrawingContext.blackLines
    val renderable = interpreter(dc, metrics)(image)
    val bb = renderable.boundingBox

    val buffer = new BufferedImage(bb.width.ceil.toInt + 40, bb.height.ceil.toInt + 40, BufferedImage.TYPE_INT_ARGB)
    val bufferCenter = Point.cartesian( (bb.width.ceil + 40) / 2, (bb.height.ceil + 40) / 2 )
    val graphics = Java2D.setup(buffer.createGraphics())
    Java2D.draw(graphics, bufferCenter, renderable)

    val file = new File(fileName)
    ImageIO.write(buffer, "png", file);
    }
}
