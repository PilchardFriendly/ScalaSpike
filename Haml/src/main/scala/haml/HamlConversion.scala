package haml

import util.matching.Regex


/**
 * Created by IntelliJ IDEA.
 * User: ndrew
 * Date: Oct 29, 2009
 * Time: 11:09:14 PM
 * To change this template use File | Settings | File Templates.
 */


object Indentation {
  def unapply(s: String): Some[Int] = {
    Some(s.length / 2)
  }
}

object Template {
  val tagExpression = new Regex("""(\s*)\%(\w+)\s*(.*)""")
}

class Template(val source: String) {
  import haml.Implicits._
  import haml.Template._

  case class Expression(val indent: Int) {
    def render(innner: Stream[String]): Stream[String] = Stream.empty
  }

  def render: String = {
    val expressions = source.split("\n").zipWithIndex.map {
      case (e, idx) =>

        e match {
          case tagExpression(Indentation(nestCount), tag, text) => {
            new Expression(nestCount) {
              override def render(inner: Stream[String]) = {
                val indent = "  " repeat nestCount
                val content = (text, inner) match {
                  case (s: String, t:Stream[String]) if s.isEmpty && t.isEmpty =>
                    List(indent,"<",tag,"/>").toStream
                  case (s: String, t:Stream[String]) if t.isEmpty=>
                    List(indent,"<",tag,">",text,"</",tag,">").toStream
                  case (s: String, t:Stream[String]) =>
                    List(indent,"<",tag,">",text,"\n").toStream.append(
                      t.append(
                        List("\n",indent,"</",tag,">")
                      )
                    )
                }
                content
              }
            }

          }
          case "" => {
            new Expression(0)
          }
          case line => throw new Exception("Failed to parse line %d '%s'".format(idx, line))
        }
    }

    def renderSubtree(expressions: List[Expression], soFar : Stream[String]): Stream[String] = {
      expressions match {
        case root :: Nil => renderSubtree(Nil, soFar.append(root render Stream.empty))
        case root :: tail => {
          val (subtree, rest) = tail.span(_.indent > root.indent)
          val subOutput = subtree match {
            case expr :: _ => root.render(expr render Stream.empty)
            case _ => root render Stream.empty
          }
          renderSubtree(rest, soFar.append(subOutput))

        }
        case _ => soFar
      }
    }

    renderSubtree(expressions toList, Stream.empty).foldLeft(new StringBuffer()){
      (buf, s) => buf.append(s)
      buf
    }.toString();
  }
}

class StringDuplicator(val s: String) {
  def repeat(count: Int) = {
    val result = ((0 until count) toList).foldLeft("")((a, _) => a + s)
    result
  }
}

object Implicits {
  implicit def stringsCanBeDuplicated(s: String): StringDuplicator = {
    new StringDuplicator(s)
  }

  implicit def repeatAWholeNumberOfTimes(i: Int): Repetition = {
    new Repetition(i)

  }
}

class Repetition(val count: Int) {
  import Implicits._
  def times(s: String) {
    s repeat count
  }
}


object HamlConversion {
  class HamlSource(val source: String) {
    def asHaml = {
      new Template(source)
    }
  }

  implicit def stringsAreHamlSources(s: String): HamlSource = {
    new HamlSource(s)
  }
}


