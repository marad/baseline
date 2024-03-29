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
    app.get("/console/new", ::newConsoleWindow)
    app.get("/console/{window-id}", ::getConsole)
    app.post("/console/{window-id}", ::postConsole)
}

// ================================================================================
// Handlers

fun newConsoleWindow(ctx: Context) {
    respond(ctx) {
        windowUi("0", "console") {
            consoleUi("0")
        }
    }
}

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
    div("flex flex-col space-y-2 w-full h-full p-2") {
        div("flex-1") {
            div("code w-full h-full border focus:outline focus:outline-slate-300",
                "id" to codeEditorId,
                "name" to "code",
                "_" to "on load call setupEditor(me)"
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
                    "_" to """
                        on click set 
                        editor to previous .code 
                        get editor.env.editor.getValue() set @value of previous <input/> to it""") {
                    text("Send")
                }
            }
        }
    }
}

