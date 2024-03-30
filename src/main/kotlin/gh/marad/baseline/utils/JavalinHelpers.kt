package gh.marad.baseline.utils

import gh.marad.baseline.pages.layout
import io.github.marad.html.dsl.Html
import io.javalin.http.Context

fun Context.windowType(): String = pathParam("window-type")
fun Context.windowId(): String = pathParam("window-id")

fun respond(ctx: Context, content: Html.() -> Unit) {
    val isHxRequest = ctx.header("Hx-Request") != null
    if (isHxRequest) {
        ctx.html(Html.create { content() })
    } else {
        ctx.html(layout { content() })
    }
}

