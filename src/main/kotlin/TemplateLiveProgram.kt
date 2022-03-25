import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.DepthTestPass
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.extra.shadestyles.LinearGradient
import org.openrndr.extra.shadestyles.linearGradient
import org.openrndr.extras.color.presets.*
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.shape.contour
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin

fun main() = application {
    configure {
        width = 800
        height = 800

    }
    oliveProgram {

        extend {
            fun setup() {
                drawer.translate(0.0, 0.0, -600.0)
                drawer.depthWrite = true
                drawer.strokeWeight = 0.0
                drawer.stroke = ColorRGBa.TRANSPARENT
                drawer.depthTestPass = DepthTestPass.LESS_OR_EQUAL
                drawer.perspective(90.0, 1.0, 1.0, -300.0)
            }
            fun prepHexagonSideDrawer(
                num: Int,
                apothem: Double,
                tempo: Double,
                yTrans: Double,
                color: ColorRGBa = ColorRGBa.WHITE,
                gradient: LinearGradient = linearGradient(ColorRGBa.TRANSPARENT, ColorRGBa.TRANSPARENT)
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
            fun drawHexagonSide(
                num: Int,
                apothem: Double,
                rectHeight: Double,
                tempo: Double,
                yTrans: Double,
                color: ColorRGBa
            ) {
                setup()
                drawer.fill = color
                val rectWidth = apothem/0.866
                val offset = (PI/3.0) * num
                drawer.translate(
                    (sin(tempo + offset)) * apothem,
                    yTrans,
                    sin(tempo + (0.5 * PI) + offset) * apothem)
                drawer.rotate(
                    Vector3(0.0, 1.0, 0.0),
                    tempo * (360.0/(2.0* PI)) + (360.0 * (num/6.0))
                )
                drawRect(rectHeight, rectWidth)
                drawer.defaults()
            }

            val stripes = 24
            repeat (stripes) { numOuter ->
                val apothem = (stripes/2 - abs(numOuter - (stripes/2))) * 8.0
                val rectHeight = 8.0
                val tempo = sin(seconds * 0.5 + (numOuter/23.5))
                val yTrans = (sin(2.0 * seconds + (numOuter/23.5)) * 8) + numOuter * 20.0 - 240.0

                repeat(6) { num ->
                    drawHexagonSide(num, apothem, rectHeight, tempo, yTrans, ColorRGBa.PINK)
                    drawHexagonSide(num, apothem - 1.0, rectHeight, tempo, yTrans, ColorRGBa.POWDER_BLUE)
                }
                drawer.defaults()
            }

            fun drawFloor(floorHeight: Double) {
                setup()
                drawer.translate(0.0, floorHeight)
                drawer.rotate(Vector3(1.0, 0.0, 0.0), 90.0)
                drawer.fill = ColorRGBa.DARK_SLATE_BLUE
                drawRect(2000.0, 2000.0)
                drawer.defaults()
            }
            drawFloor(600.0)
            drawFloor(-600.0)

            setup()
            drawer.translate(0.0, 0.0, -800.0)
//            drawer.rotate(Vector3(1.0, 0.0, 0.0), 90.0)
            drawer.fill = ColorRGBa.GREEN
            drawRect(200.0, 3000.0)
            drawer.defaults()

            val apothem = 700.0
            val rectHeight = 600.0
            repeat(6) {num ->
                prepHexagonSideDrawer(
                    num,
                    600.0,
                    seconds * 0.1,
                    300.0,
                    gradient = linearGradient(ColorRGBa.GREEN, ColorRGBa.DARK_SLATE_BLUE, rotation = 0.0)
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
                    gradient = linearGradient(ColorRGBa.GREEN, ColorRGBa.DARK_SLATE_BLUE, rotation = 180.0)
                )
                drawer.rotate(Vector3(1.0, 0.0, 0.0), 20.0)
                val rectWidth = apothem/0.866
                drawRect(rectHeight, rectWidth)

                drawer.defaults()
            }
        }
    }
}
//                drawer.strokeWeight = 5.0
//                val triangleWidth = rectWidth - drawer.strokeWeight
//                drawer.translate(0.0, ((rectHeight + drawer.strokeWeight)/2.0));
//                drawer.rotate(Vector3(1.0, 0.0, 0.0), -28.0)
//                val c = contour {
//                    moveTo(Vector2(-triangleWidth/2.0, 0.0))
//                    lineTo(cursor + Vector2(triangleWidth, 0.0))
//                    lineTo(cursor + Vector2(-triangleWidth/2.0, 200.0))
//                    lineTo(anchor)
//                    close()
//                }
//                drawer.contour(c)
