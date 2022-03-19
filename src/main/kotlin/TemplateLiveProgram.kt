import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.DepthTestPass
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.extras.color.presets.*
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.shape.contour
import kotlin.math.PI
import kotlin.math.sin

fun main() = application {
    configure {
        width = 800
        height = 800

    }
    oliveProgram {

        extend {
            drawer.clear(ColorRGBa.BLACK)
            fun setup() {
                drawer.translate(0.0, 0.0, -400.0)
                drawer.perspective(90.0, 1.0, 1.0, -300.0)
                drawer.depthWrite = true
                drawer.depthTestPass = DepthTestPass.LESS_OR_EQUAL
                drawer.stroke = ColorRGBa.DARK_SEA_GREEN
                drawer.strokeWeight = 5.0
                drawer.fill = ColorRGBa.SEA_GREEN
            }

            val ratioAS = 0.866
            val apothem = 100
            val side = apothem/ratioAS
            val rectWidth = side
            val rectHeight = 80.0

            val tempo = seconds * 0.5

            repeat (6) { num ->
                setup()

                val offset = (PI/3.0) * num
                drawer.translate(
                    (sin(tempo + offset)) * apothem,
                    0.0,
                    sin(tempo + (0.5 * PI) + offset) * 100.0)
                drawer.rotate(
                    Vector3(0.0, 1.0, 0.0),
                    tempo * (360.0/(2.0* PI)) + (360.0 * (num/6.0))
                )

                drawer.rectangle(
                    -(rectWidth/2.0),
                    -(rectHeight/2.0),
                    rectWidth,
                    rectHeight
                )

                drawer.strokeWeight = 5.0
                val triangleWidth = rectWidth - drawer.strokeWeight

                drawer.translate(0.0, ((rectHeight + drawer.strokeWeight)/2.0));
                drawer.rotate(Vector3(1.0, 0.0, 0.0), -28.0)
                val c = contour {
                    moveTo(Vector2(-triangleWidth/2.0, 0.0))
                    lineTo(cursor + Vector2(triangleWidth, 0.0))
                    lineTo(cursor + Vector2(-triangleWidth/2.0, 200.0))
                    lineTo(anchor)
                    close()
                }
                drawer.contour(c)

                drawer.defaults()
            }

        }
    }
}

