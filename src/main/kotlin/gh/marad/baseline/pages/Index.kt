package gh.marad.baseline.pages

import gh.marad.baseline.utils.btnClass
import io.github.marad.html.dsl.*
import io.javalin.Javalin
import io.javalin.http.Context

fun installIndex(app: Javalin) {
    app.get("/", ::index)
}

// ================================================================================
// Handler

fun index(ctx: Context) {
    ctx.html(layout {})
}


// ================================================================================
// UI

fun Html.navbarButton(text: String, vararg attrs: Pair<String, Any>) =
    button("$btnClass py-2 px-4", attrs = attrs) {
        text(text)
    }

fun layout(content: Html.() -> Unit): String {
    return html("dark") {
        head {
            script("src" to "https://unpkg.com/htmx.org@1.9.11")
            script("src" to "/scripts.js")
            script("src" to "https://cdn.tailwindcss.com")
            script(
                "src" to "https://cdnjs.cloudflare.com/ajax/libs/ace/1.32.8/ace.js",
                "integrity" to "sha512-u8Li+VRzprAsXVzZsUJ/BJm5fk6T4yFNFqh8t2QjiwtRopvsrTPss4rwOxsUMdksqngUgLjN6wrxD4DU9c3l7Q==",
                "crossorigin" to "anonymous",
                "referrerpolicy" to "no-referrer")
            script(
                "src" to "https://cdnjs.cloudflare.com/ajax/libs/ace/1.32.8/ext-language_tools.min.js",
                "integrity" to "sha512-ttkK0/fzjASB+dFxQ2sPN1y1KVRssD58a870e2PIRq36xeohjdRjwMgsfAY5s14gl6lOhv1qSx7Db5r3dCnhlQ==",
                "crossorigin" to "anonymous",
                "referrerpolicy" to "no-referrer"
            )
            script("src" to "https://unpkg.com/hyperscript.org@0.9.12")
        }
        body(
            "p-5",
            "hx-boost" to "true",
            "onload" to "createWindow('console')") {
            h1("text-2xl p-3") {
                text("Baseline")
            }
            nav("flex space-x-2") {
                navbarButton("New console", "onclick" to "createWindow('console')")
                navbarButton("Browser", "onclick" to "createWindow('browser')")
            }

            div("id" to "workspace", "hx-target" to "this") {
                content()
            }
        }
    }
}

