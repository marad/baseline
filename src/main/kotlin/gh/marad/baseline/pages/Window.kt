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

fun Html.windowUi(windowType: String, content: Html.() -> Unit) {
    div("window absolute",
//        "id" to "window-$windowId",
        "style" to "width: 400px; height: 300px",
        "_" to """
            install Draggable(dragHandle: .header in me) 
            install Resizable(resizeHandle: .resizer in me)
            on window:close remove me
        """.trimIndent()) {
        div(
            "bg-slate-50 border-2 rounded-lg m-2 overflow-clip flex flex-col h-full w-full",
        ) {
            div("header flex-none flex bg-slate-200 border-b cursor-move") {
                span("title flex-1 px-3 py-2") { text(windowType.replaceFirstChar { it.uppercase() }) }
                windowHeaderButton("_" to "on click send window:close to closest .window") { text("X") }
            }
            content()
            div("footer flex-none flex bg-slate-200") {
                div("flex-1")
                div("resizer flex-none bg-slate-300 w-3 h-3 cursor-nwse-resize") { text("&nbsp;") }
            }
        }
    }
}

fun Html.windowHeaderButton(vararg attrs: Pair<String, Any>, content: Html.() -> Unit) =
    windowHeaderButton("", attrs = attrs, content)

fun Html.windowHeaderButton(classes: String? = null, vararg attrs: Pair<String, Any>, content: Html.() -> Unit) =
    button("$btnClass $classes px-3 py-1 flex-none", attrs = attrs, content)
