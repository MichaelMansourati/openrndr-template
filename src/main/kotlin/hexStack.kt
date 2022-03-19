import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.DepthTestPass
import org.openrndr.draw.DrawPrimitive
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.extras.color.presets.DARK_SEA_GREEN
import org.openrndr.extras.color.presets.DARK_SLATE_BLUE
import org.openrndr.extras.color.presets.DIM_GRAY
import org.openrndr.extras.color.presets.SEA_GREEN
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.shape.contour
import kotlin.math.sin

fun main() = application {
	configure {
		width = 800
		height = 800
	}

	oliveProgram {
		extend {
			fun setup() {
				drawer.translate(0.0, 0.0, -400.0)
				drawer.perspective(90.0, 1.0, 1.0, -300.0)
				drawer.depthWrite = false
				drawer.depthTestPass = DepthTestPass.LESS_OR_EQUAL
				drawer.stroke = ColorRGBa.DARK_SEA_GREEN
				drawer.strokeWeight = 3.0
				drawer.fill = ColorRGBa.SEA_GREEN
			}
			drawer.clear(ColorRGBa.DARK_SLATE_BLUE)

			val layers = 300
			val layersFloat = layers * 1.0
			repeat(layers) { num ->
				val triangleWidth = num * 1.0 + 1.0
				val triangleHeight = num * 1.0 + 1.0
				setup()
				drawer.rotate(Vector3(1.0, 0.0, 0.0), 90.0)

				var polarity = 1.0
				if (num.mod(2) == 0) {
					polarity = -1.0
				}

				drawer.rotate(Vector3(0.0, 0.0, 1.0), (sin(seconds - ((num - polarity)/layersFloat)) * 100.0) )
				drawer.translate(0.0, triangleHeight/2, (polarity * (layersFloat - num))/2.0)

				val c = contour {
					moveTo(Vector2(-triangleWidth / 2.0, -triangleHeight))
					lineTo(cursor + Vector2(triangleWidth, 0.0))
					lineTo(cursor + Vector2(-triangleWidth / 2.0, triangleHeight))
					lineTo(anchor)
					close()
				}
				drawer.contour(c)
				drawer.defaults()
			}
		}
	}
}