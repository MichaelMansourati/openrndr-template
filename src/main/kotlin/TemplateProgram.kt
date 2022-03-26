import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.DepthTestPass
import org.openrndr.draw.rectangleBatch
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.extra.shadestyles.LinearGradient
import org.openrndr.extra.shadestyles.linearGradient
import org.openrndr.extras.color.presets.*
import org.openrndr.math.Polar
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.shape.Rectangle
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin

fun main() = application {
    configure {
        width = 1920
        height = 1080

    }
    oliveProgram {
        extend {
            fun setup() {
                drawer.translate(0.0, 0.0, -600.0)
                drawer.depthWrite = true
                drawer.strokeWeight = 0.0
                drawer.stroke = ColorRGBa.TRANSPARENT
                drawer.depthTestPass = DepthTestPass.LESS_OR_EQUAL
                drawer.perspective(90.0, 16.0/9.0, 1.0, -300.0)
            }
            fun prepHexagonSideDrawer(
                num: Int,
                apothem: Double,
                tempo: Double,
                yTrans: Double,
                color: ColorRGBa = ColorRGBa.WHITE,
                gradient: LinearGradient? = null
            ) {
                setup()
                drawer.fill = color
                drawer.shadeStyle = gradient
                val offset = (PI/3.0) * num
                drawer.translate(
                    (sin(tempo + offset)) * apothem,
                    yTrans,
                    sin(tempo + (0.5 * PI) + offset) * apothem)
                drawer.rotate(
                    Vector3(0.0, 1.0, 0.0),
                    tempo * (360.0/(2.0* PI)) + (360.0 * (num/6.0))
                )
            }
            fun drawRect(rectHeight: Double, rectWidth: Double) {
                drawer.rectangle(
                    -(rectWidth/2.0),
                    -(rectHeight/2.0),
                    rectWidth,
                    rectHeight
                )
            }

            val stripes = 25
            repeat (stripes) { numOuter ->
                if ( numOuter != 0 && numOuter != 24 ) {
                    val apothem = (stripes / 2 - abs(numOuter - (stripes / 2))) * 10.0
                    val rectHeight = 8.0
                    val tempo = sin(seconds * 0.5 + (numOuter / 23.5))
                    val yTrans = (sin(2.0 * seconds + (numOuter / 23.5)) * 8) + numOuter * 20.0 - 240.0

                    repeat(6) { num ->
                        prepHexagonSideDrawer(
                            num,
                            apothem,
                            tempo,
                            yTrans,
                            ColorRGBa(0.5, 0.0, 0.0),
                        )
                        val rectWidth = apothem / 0.866
                        drawRect(rectHeight, rectWidth)
                        drawer.defaults()
                    }
                    drawer.rectangles {
                        repeat(6) { num ->
                            prepHexagonSideDrawer(
                                num,
                                apothem - 0.5,
                                tempo,
                                yTrans,
                                ColorRGBa(0.8, 0.0, 0.0),
                            )
                            val rectWidthInner = (apothem-0.5)/0.866
                            val pos = Vector2(width/2.0, height/2.0 + yTrans)
                            val rect = Rectangle.fromCenter(pos, width = rectWidthInner, height = rectHeight)
                            drawRect(rectHeight, rectWidthInner)
//                            rectangle(rect, 0.0) // add rect to the batch
                            drawer.defaults()
                        }
                    }
                    drawer.defaults()
                }
            }

            fun drawFloor(floorHeight: Double, color: ColorRGBa) {
                setup()
                drawer.translate(0.0, floorHeight)
                drawer.rotate(Vector3(1.0, 0.0, 0.0), 90.0)
                drawer.fill = color
                drawRect(2000.0, 2000.0)
                drawer.defaults()
            }
            drawFloor(600.0, ColorRGBa.MIDNIGHT_BLUE)
            drawFloor(-600.0, ColorRGBa.GREEN)

            setup()
            drawer.translate(0.0, 0.0, -800.0)
            drawer.fill = ColorRGBa.DARK_SLATE_BLUE
            drawRect(200.0, 6000.0)
            drawer.defaults()

            val apothem = 700.0
            val rectHeight = 600.0
            repeat(6) {num ->
                prepHexagonSideDrawer(
                    num,
                    600.0,
                    seconds * 0.1,
                    300.0,
                    gradient = linearGradient(ColorRGBa.DARK_SLATE_BLUE, ColorRGBa.MIDNIGHT_BLUE, rotation = 0.0)
                )
                drawer.rotate(Vector3(1.0, 0.0, 0.0), -20.0)

                val rectWidth = apothem/0.866
                drawRect(rectHeight, rectWidth)
                drawer.defaults()
            }
            repeat(6) {num ->
                prepHexagonSideDrawer(
                    num,
                    600.0,
                    seconds * 0.1,
                    -300.0,
                    gradient = linearGradient(ColorRGBa.DARK_SLATE_BLUE, ColorRGBa.GREEN, rotation = 180.0)
                )
                drawer.rotate(Vector3(1.0, 0.0, 0.0), 20.0)
                val rectWidth = apothem/0.866
                drawRect(rectHeight, rectWidth)

                drawer.defaults()
            }
        }
    }
}
