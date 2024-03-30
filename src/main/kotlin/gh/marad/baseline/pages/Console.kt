package gh.marad.baseline.pages

import gh.marad.baseline.CodeManager
import gh.marad.baseline.lua
import gh.marad.baseline.utils.btnClass
import gh.marad.baseline.utils.respond
import io.github.marad.html.dsl.*
import io.javalin.Javalin
import io.javalin.http.Context
import party.iroiro.luajava.Lua
import java.util.*

fun installConsole(app: Javalin, codeManager: CodeManager) {
    val handlers = ConsoleHandlers(codeManager)
    app.get("/console/new", handlers::newConsoleWindow)
    app.post("/console/save", handlers::saveModule)
    app.post("/console/execute", handlers::execute)
}



// ================================================================================
// Handlers

class ConsoleHandlers(private val codeManager: CodeManager) {
    fun newConsoleWindow(ctx: Context) {
        val id = "x" + UUID.randomUUID().toString().replace("-", "")
        respond(ctx) {
            windowUi("Console") {
                consoleUi(id)
            }
        }
    }

    fun saveModule(ctx: Context) {
        val id = ctx.formParam("id")!!
        val module = ctx.formParam("module")
        val code = ctx.formParam("code")
        if (module.isNullOrBlank()) {
            respond(ctx) { consoleUi(id, code ?: "", "Module is empty.")}
            return
        }
        if (code.isNullOrBlank()) {
            respond(ctx) { consoleUi(id, error = "Code is empty.") }
            return
        }

        val (domain, moduleName) = module.split("::")
        codeManager.storeCode(domain, moduleName, code)
        respond(ctx) { consoleUi(id, code, error = "Saved.") }
    }

    fun execute(ctx: Context) {
        val id = ctx.formParam("id")!!
        val code = ctx.formParam("code")
        if (code.isNullOrBlank()) {
            respond(ctx) {
                consoleUi(id, error = "Code is empty!")
            }
            return
        }
        val result = lua.run(code)
        val error = if (result == Lua.LuaError.OK) "" else result.name
        respond(ctx) {
            consoleUi(id, code, error)
        }
    }
}

// ================================================================================
// UI

fun Html.consoleUi(id: String, code: String = "", error: String = "") {

    form("w-full h-full") {
        input(
            "type" to "hidden",
            "name" to "id",
            "value" to id,
            )
        div("flex flex-col space-y-2 w-full h-full") {
            div("flex-none flex") {
                label("p-1") { text("Module") }
                input("flex-1 p-1", "name" to "module", "placeholder" to "domain::module")
            }
            div("flex-1") {
                div("code w-full h-full border dark:border-slate-600 focus:outline focus:outline-slate-300",
                    "id" to "code-$id",
                    "hx-preserve" to "true",
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
                div("flex-1", "id" to id)
                input(
                    "type" to "hidden",
                    "name" to "code",
                    "value" to code,
                )
                button("$btnClass p-2",
                    "hx-post" to "/console/execute",
                    "hx-trigger" to "go",
                    "type" to "button",
                    "_" to """
                            on click set 
                            editor to previous .code 
                            get editor.env.editor.getValue() set @value of previous <input/> to it
                            send go to me
                        """.trimIndent()) {
                    text("Run")
                }
                button("$btnClass p-2",
                    "hx-post" to "/console/save",
                    "hx-trigger" to "go",
                    "type" to "button",
                    "_" to """
                            on click set 
                            editor to previous .code 
                            get editor.env.editor.getValue() set @value of previous <input/> to it
                            send go to me
                        """.trimIndent()) {
                    text("Save")
                }
            }
        }
    }
}

