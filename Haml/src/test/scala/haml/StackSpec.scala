package haml

import collection.mutable.Stack
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec

/**
 * Created by IntelliJ IDEA.
 * User: ndrew
 * Date: Oct 28, 2009
 * Time: 1:16:35 PM
 * To change this template use File | Settings | File Templates.
 */

class StackSpec extends Spec with ShouldMatchers {
  describe("A stack"){
    describe("(when empty)"){
      val stack = new Stack[int];

      it("should be empty"){
        stack should be ('empty)
      }
    }
  }
}

class AHamlSpec extends Spec with ShouldMatchers
{
  import haml.HamlConversion._

  describe("A Haml template"){
    def renderHaml( template:String) : String = {
      template.asHaml render
    }

    describe("that contain junk"){
      val template = "!"
      it( "should throw a syntax error"){
        evaluating { renderHaml(template) } should produce[Exception]
      }
    }

    describe("with a single haml element %p")   {
      val template = "%p"
      it( "should output as a an single open/close p tag" ) {
        renderHaml(template) should be("<p/>")
      }
    }
    describe("with a single haml element %tag") {
      val template = "%tag"
      it( "should output as a single open/close tag element"){
        renderHaml(template) should be("<tag/>")
      }
    }
    describe("with a single haml element and text on the same line"){
      val template = "%p Hi there"
      it( "should output as a open/close tag with the text inside it"){
        renderHaml(template) should be("<p>Hi there</p>")
      }
    }
    describe("with a single haml element with one level of indentation"){
      val template = "  %p something"
      it( "should indent the open/close tag"){
        renderHaml(template) should be("  <p>something</p>")
      }
    }
    describe("with a single haml element %x and a indented element %y"){
      val template = """
%x
  %y
"""
      it( "should output as y element nested in an x"){
        renderHaml(template) should be("""<x>
  <y/>
</x>""")
      }
    }


  }
}