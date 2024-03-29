package gh.marad.baseline

import gh.marad.baseline.pages.*
import io.javalin.Javalin
import io.javalin.http.staticfiles.Location


fun main() {
    val app = Javalin.create {
        it.staticFiles.add("static", Location.EXTERNAL)
    }
    installIndex(app)
    installWindow(app)
    installConsole(app)
    installBrowser(app)
    app.start(1234)
}