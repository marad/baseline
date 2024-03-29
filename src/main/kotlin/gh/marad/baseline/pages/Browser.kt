package gh.marad.baseline.pages

import gh.marad.baseline.luaListChildren
import gh.marad.baseline.utils.respond
import io.github.marad.html.dsl.*
import io.javalin.Javalin
import io.javalin.http.Context

fun installBrowser(app: Javalin) {
    app.get("/browser/new", ::newBrowser)
    app.get("/browser", ::selectContent)
}

// ================================================================================
// Handler

fun newBrowser(ctx: Context) {
    respond(ctx) {
        windowUi("Browser") {
            browserUi(ctx.queryParam("selected") ?: "")
        }
    }
}

fun selectContent(ctx: Context) {
    respond(ctx) {
        browserUi(ctx.queryParam("selected") ?: "")
    }
}

// ================================================================================
// UI

fun Html.browserUi(path: String) {
    val items = luaListChildren("_ENV")

    val selections = path.removePrefix("/").trim().split("/").filter { it.isNotEmpty() }

    val baseStyle = "p-1 hover:bg-sky-300 dark:hover:bg-sky-800 cursor-pointer"
    fun genClass(name: String, level: Int): String =
        if (name == selections.getOrNull(level)) {
            "bg-sky-400 dark:bg-sky-700 $baseStyle"
        } else baseStyle

    div("flex w-full h-full space-x-2") {
        ul("flex-none overflow-auto") {
            items.forEach { (name, value) ->
                li(
                    genClass(name, 0),
                    "hx-get" to "/browser?selected=$name",
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
                        "hx-get" to "/browser?selected=${selections.take(index).joinToString("/")}/$name",
                    ) {
                        text("$name : ${value.type()}")
                    }
                }
            }
        }
    }
}

