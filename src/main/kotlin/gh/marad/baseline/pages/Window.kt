package gh.marad.baseline.pages

import gh.marad.baseline.utils.btnClass
import gh.marad.baseline.utils.respond
import gh.marad.baseline.utils.windowId
import gh.marad.baseline.utils.windowType
import io.github.marad.html.dsl.*
import io.javalin.Javalin
import io.javalin.http.Context

fun installWindow(app: Javalin) {
    app.get("/window/{window-type}/{window-id}", ::createWindow)
}

// ================================================================================
// Handler

fun createWindow(ctx: Context) {
    respond(ctx) { windowUi(ctx) }
}

// ================================================================================
// UI

fun Html.windowUi(ctx: Context) {
    val windowId = ctx.windowId()
    val path = "/${ctx.windowType()}/$windowId"

    div("bg-slate-50 border-2 rounded-lg m-2 overflow-clip flex flex-col h-full w-full") {
        div("header flex-none flex bg-slate-200 border-b cursor-move") {
            span("title flex-1 px-3 py-2") { text(ctx.windowType().replaceFirstChar { it.uppercase() }) }
            windowHeaderButton("onclick" to "closeWindow($windowId)") { text("X") }
        }
        div("content grow p-2 overflow-auto", "id" to "content",
            "hx-get" to path,
            "hx-target" to "this",
            "hx-trigger" to "load",
            "hx-push-url" to "false"
        ) {
            text("Loading...")
        }
        div("footer flex-none flex bg-slate-200") {
            div("flex-1")
            div("resizer flex-none bg-slate-300 w-3 h-3 cursor-nwse-resize") { text("&nbsp;") }
        }
    }
}

fun Html.windowHeaderButton(vararg attrs: Pair<String, Any>, content: Html.() -> Unit) =
    windowHeaderButton("", attrs = attrs, content)

fun Html.windowHeaderButton(classes: String? = null, vararg attrs: Pair<String, Any>, content: Html.() -> Unit) =
    button("$btnClass $classes px-3 py-1 flex-none", attrs = attrs, content)
