import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.DepthTestPass
import org.openrndr.draw.isolated
import org.openrndr.extra.shadestyles.linearGradient
import org.openrndr.extras.color.presets.*
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.shape.Rectangle
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin

fun main() = application {
    configure {
        width = 1280
        height = 720

    }
//    oliveProgram {
    program {
        val piFrac = PI/3.0

        val stripes = 25
        val stripeHeight = 8.0
        val colorOuter = ColorRGBa(0.5, 0.0, 0.0)
        val colorInner = ColorRGBa(0.8, 0.0, 0.0)

        val bgApothem = 700.0
        val bgRectHeight = 600.0
        val bgRectWidth = bgApothem/0.866

        fun drawFloor(floorHeight: Double, color: ColorRGBa) {
            drawer.isolated {
                translate(0.0, floorHeight)
                rotate(Vector3(1.0, 0.0, 0.0), 90.0)
                fill = color
                rectangle(Rectangle.fromCenter(Vector2.ZERO, 2000.0, 2000.0))
            }
        }
        fun drawBgSide(side: String): Unit {
            var rotation = -20.0
            var color1 = ColorRGBa.DARK_SLATE_BLUE
            var color2 = ColorRGBa.MIDNIGHT_BLUE
            var yTrans = 300.0

            if (side === "bottom") {
                rotation = 20.0
                color1 = ColorRGBa.GREEN
                color2 = ColorRGBa.DARK_SLATE_BLUE
                yTrans = -300.0
            }

            for(num in 0 until 6) {
                val offset = piFrac * num
                val tempo = seconds * 0.1
                val apothem = 600.0
                drawer.isolated {
                    shadeStyle = linearGradient(color1, color2, rotation = 0.0)
                    translate(
                        (sin(tempo + offset)) * apothem,
                        yTrans,
                        sin(tempo + (0.5 * PI) + offset) * apothem)
                    rotate(
                        Vector3(0.0, 1.0, 0.0),
                        tempo * (360.0/(2.0* PI)) + (360.0 * (num/6.0))
                    )
                    rotate(Vector3(1.0, 0.0, 0.0), rotation)
                    rectangle(Rectangle.fromCenter(Vector2.ZERO, bgRectWidth, bgRectHeight))
                }
            }
        }
        extend {
            drawer.apply {
                depthWrite = true
                strokeWeight = 0.0
                stroke = ColorRGBa.TRANSPARENT
                depthTestPass = DepthTestPass.LESS_OR_EQUAL
                perspective(90.0, 16.0/9.0, 1.0, -300.0)
                translate(0.0, 0.0, -600.0)
            }
            for (numOuter in 1 until stripes - 1) {
                val tempo = sin(seconds * 0.5 + (numOuter / 23.5))
                val yTrans = (sin(2.0 * seconds + (numOuter / 23.5)) * 8) + numOuter * 20.0 - 240.0

                val apothemOuter = (stripes / 2 - abs(numOuter - (stripes / 2))) * 10.0
                val apothemInner = apothemOuter - 0.5
                val rectWidthOuter = apothemOuter/0.866
                val rectWidthInner = apothemInner/0.866
                val rectOuter = Rectangle.fromCenter(Vector2.ZERO, rectWidthOuter, stripeHeight)
                val rectInner = Rectangle.fromCenter(Vector2.ZERO, rectWidthInner, stripeHeight)

                for (num in 0 until 6) {
                    val offset = piFrac * num
                    val rotation = tempo * (360.0/(2.0* PI)) + (360.0 * (num/6.0))
                    val zTransBase = sin(tempo + offset  + (0.5 * PI))
                    val xTransBase = sin(tempo + offset)

                    drawer.isolated {
                        fill = colorOuter
                        translate(
                            xTransBase * apothemOuter,
                            yTrans,
                            zTransBase * apothemOuter
                        )
                        rotate(Vector3.UNIT_Y, rotation)
                        rectangle(rectOuter)
                    }
                    drawer.isolated {
                        fill = colorInner
                        translate(
                            xTransBase * apothemInner,
                            yTrans,
                            zTransBase * apothemInner
                        )
                        rotate(Vector3.UNIT_Y, rotation)
                        rectangle(rectInner)
                    }
                }
            }

            drawFloor(600.0, ColorRGBa.MIDNIGHT_BLUE)
            drawFloor(-600.0, ColorRGBa.GREEN)

            drawer.isolated {
                translate(0.0, 0.0, -800.0)
                fill = ColorRGBa.DARK_SLATE_BLUE
                rectangle(Rectangle.fromCenter(Vector2.ZERO, 6000.0, 200.0))
            }

            drawBgSide("top")
            drawBgSide("bottom")
        }
    }
}
