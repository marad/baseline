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
    val id = ctx.windowId()
    val type = ctx.windowType()

    respond(ctx) {
        windowUi(type) {
            div(
                "content grow p-2 overflow-auto", "id" to "content",
                "hx-get" to "/$type/$id",
                "hx-target" to "this",
                "hx-trigger" to "load",
                "hx-push-url" to "false"
            ) {
                text("Loading...")
            }
        }
    }
}

// ================================================================================
// UI

fun Html.windowUi(windowTitle: String, width: Int = 400, height: Int = 300, content: Html.() -> Unit) {
    div("window absolute",
//        "id" to "window-$windowId",
        "style" to "width: ${width}px; height: ${height}px",
        "_" to """
            install Draggable(dragHandle: .header in me) 
            install Resizable(resizeHandle: .resizer in me)
            on window:close remove me
        """.trimIndent()) {
        div(
            "bg-slate-50 dark:bg-slate-800 border-2 dark:border-slate-600 rounded-lg m-2 overflow-clip flex flex-col h-full w-full",
        ) {
            div("header flex-none flex bg-slate-200 dark:bg-slate-700 border-b dark:border-slate-600 cursor-move") {
                span("title flex-1 px-3 py-2") { text(windowTitle) }
                windowHeaderButton("_" to "on click send window:close to closest .window") { text("X") }
            }
            div("content grow p-2 overflow-auto",
                "hx-target" to "this",
                "hx-push-url" to "false"
            ) {
                content()
            }
            div("footer flex-none flex bg-slate-200 dark:bg-slate-700") {
                div("flex-1")
                div("resizer flex-none bg-slate-300 dark:bg-slate-600 w-3 h-3 cursor-nwse-resize") { text("&nbsp;") }
            }
        }
    }
}

fun Html.windowHeaderButton(vararg attrs: Pair<String, Any>, content: Html.() -> Unit) =
    windowHeaderButton("", attrs = attrs, content)

fun Html.windowHeaderButton(classes: String? = null, vararg attrs: Pair<String, Any>, content: Html.() -> Unit) =
    button("$btnClass $classes px-3 py-1 flex-none rounded-l-none rounded-b-none", attrs = attrs, content)
