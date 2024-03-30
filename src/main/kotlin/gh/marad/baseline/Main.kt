package gh.marad.baseline

import gh.marad.baseline.pages.*
import io.javalin.Javalin
import io.javalin.http.staticfiles.Location
import java.nio.file.Path


fun main() {
    val codeManager = CodeManager(Path.of("modules"))
    lua.setExternalLoader(codeManager.createExternalLoader())

    val app = Javalin.create {
        it.staticFiles.add("static", Location.EXTERNAL)
    }
    installIndex(app)
    installWindow(app)
    installConsole(app, codeManager)
    installBrowser(app)
    app.start(1234)
}