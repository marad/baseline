package gh.marad.baseline.pages

import gh.marad.baseline.lua
import gh.marad.baseline.utils.btnClass
import gh.marad.baseline.utils.respond
import io.github.marad.html.dsl.*
import io.javalin.Javalin
import io.javalin.http.Context
import party.iroiro.luajava.Lua

fun installConsole(app: Javalin) {
    app.get("/console/new", ::newConsoleWindow)
    app.post("/console/execute", ::postConsole)
}

// ================================================================================
// Handlers

fun newConsoleWindow(ctx: Context) {
    respond(ctx) {
        windowUi("Console") {
            consoleUi()
        }
    }
}

fun postConsole(ctx: Context) {
    val code = ctx.formParam("code")
    val result = lua.run(code)
    val error = if (result == Lua.LuaError.OK) "" else result.name
    respond(ctx) { text(error) }
}

// ================================================================================
// UI

fun Html.consoleUi(code: String = "", error: String = "") {
    div("flex flex-col space-y-2 w-full h-full") {
        div("flex-1") {
            div("code w-full h-full border focus:outline focus:outline-slate-300",
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
            div("flex-1 .result")
            form("hx-post" to "/console/execute",
                "hx-target" to "previous .result",
//                "hx-swap" to "innerHTML"
            ) {
                input(
                    "type" to "hidden",
                    "name" to "code",
                    "value" to code,
                )
                button("$btnClass p-2",
                    "_" to """
                            on click set 
                            editor to previous .code 
                            get editor.env.editor.getValue() set @value of previous <input/> to it
                        """.trimIndent()) {
                    text("Send")
                }
            }
        }
    }
}

