package gh.marad.baseline.pages

import gh.marad.baseline.luaListChildren
import gh.marad.baseline.utils.respond
import gh.marad.baseline.utils.windowId
import io.github.marad.html.dsl.*
import io.javalin.Javalin
import io.javalin.http.Context

fun installBrowser(app: Javalin) {
    app.get("/browser/{window-id}", ::getBrowser)
}

// ================================================================================
// Handler

fun getBrowser(ctx: Context) {
    respond(ctx) { browserUi(
        ctx.windowId(),
        ctx.queryParam("selected") ?: ""
    ) }
}

// ================================================================================
// UI

fun Html.browserUi(windowId: String, path: String) {
    val items = luaListChildren("_ENV")

    val selections = path.removePrefix("/").trim().split("/").filter { it.isNotEmpty() }

    val baseStyle = "p-1 hover:bg-sky-300 cursor-pointer"
    fun genClass(name: String, level: Int): String =
        if (name == selections.getOrNull(level)) {
            "bg-sky-400 $baseStyle"
        } else baseStyle

    div("flex w-full h-full space-x-2") {

        ul("flex-none overflow-auto") {
            items.forEach { (name, value) ->
                li(
                    genClass(name, 0),
                    "hx-get" to "/browser/$windowId?selected=$name",
//                    "hx-push-url" to true
                ) {
                    text("$name : ${value.type()}")
                }
            }
        }

        for (index in 1..selections.size) {
            ul("flex-none overflow-auto") {
                luaListChildren(selections.take(index).joinToString(".")).forEach { (name, value) ->
                    li(
                        genClass(name, index),
                        "hx-get" to "/browser/$windowId?selected=${selections.take(index).joinToString("/")}/$name",
                        "hx-push-url" to true
                    ) {
                        text("$name : ${value.type()}")
                    }
                }
            }
        }
    }
}

