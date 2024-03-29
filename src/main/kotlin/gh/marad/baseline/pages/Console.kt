package gh.marad.baseline.pages

import gh.marad.baseline.lua
import gh.marad.baseline.utils.btnClass
import gh.marad.baseline.utils.respond
import gh.marad.baseline.utils.windowId
import io.github.marad.html.dsl.*
import io.javalin.Javalin
import io.javalin.http.Context
import party.iroiro.luajava.Lua

fun installConsole(app: Javalin) {
    app.get("/console/{window-id}", ::getConsole)
    app.post("/console/{window-id}", ::postConsole)
}

// ================================================================================
// Handlers

fun getConsole(ctx: Context) = respond(ctx) {
    consoleUi(ctx.windowId())
}

fun postConsole(ctx: Context) {
    val windowId = ctx.windowId()
    val code = ctx.formParam("code")
    val result = lua.run(code)
    val error = if (result == Lua.LuaError.OK) "" else result.name
    respond(ctx) { consoleUi(windowId, code ?: "", error) }
}

// ================================================================================
// UI

fun Html.consoleUi(windowId: String, code: String = "", error: String = "") {
    val codeEditorId = "console-code-$windowId"
    div("flex flex-col space-y-2 w-full h-full") {
        div("flex-1") {
            div("code w-full h-full border focus:outline focus:outline-slate-300",
                "id" to codeEditorId,
                "name" to "code",
                "_" to "on htmx:afterSettle from .content call setupEditor(me)"
            ) {
                text(code)
            }
        }
        div("flex-none") {
            text(error)
        }
        div("flex-none flex") {
            div("flex-1")
            form("hx-post" to "/console/$windowId", "hx-swap" to "none") {
                input(
                    "type" to "hidden",
                    "name" to "code",
                    "value" to code,
                )
                button("$btnClass p-2",
                    "_" to "on click get getCode('#$codeEditorId') set @value of previous <input/> to it") {
                    text("Send")
                }
            }
        }
    }
}

