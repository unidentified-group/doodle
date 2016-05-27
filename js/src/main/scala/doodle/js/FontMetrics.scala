package doodle
package js

import doodle.core.font._
import doodle.backend.BoundingBox

import org.scalajs.dom

final case class FontMetrics(root: dom.svg.SVG) {
  //import scalatags.JsDom.tags._
  //import scalatags.JsDom.styles._

  def boundingBox(font: Font, characters: String): BoundingBox = {
    import scalatags.JsDom.short._
    import scalatags.JsDom.{svgTags => svg}

    val elt = svg.text(
      *.display:="none",
      *.font:=FontMetrics.toCss(font),
      characters
    ).render

    val txt = root.appendChild(elt)
    val bb = txt.asInstanceOf[dom.svg.Locatable].getBBox()
    root.removeChild(txt)

    BoundingBox(0, 0, bb.width, bb.height)
  }
}
object FontMetrics {
  import FontFamily._
  import FontFace._
  import FontSize._

  def toCss(font: Font): String = {
    val style =
      font.face match {
        case Italic => "italic"
        case _ => "normal"
      }

    val weight =
      font.face match {
        case Bold => "bold"
        case _ => "normal"
      }

    val size =
      font.size match {
        case Points(pts) => s"${pts}pt"
      }

    val family =
      font.family match {
        case Serif => "serif"
        case SansSerif => "sans-serif"
        case Monospaced => "monospaced"
        case Named(name) => name
      }

    s"${style} normal ${weight} ${size} ${family}"
  }
}
