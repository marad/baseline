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
            draggable()
            resizable()
        }
        body(
            "p-5",
            "hx-boost" to "true",
        ) {
            h1("text-2xl p-3") {
                text("Baseline")
            }
            nav("flex space-x-2") {
                navbarButton("Console", "hx-get" to "/console/new")
                navbarButton("Browser", "hx-get" to "/browser/new")
            }

            div("id" to "workspace", "hx-target" to "this") {
                content()
            }
        }
    }
}

fun Html.navbarButton(text: String, vararg attrs: Pair<String, Any>) {
    val attrList = attrs.toMutableList()
    attrList.add("hx-swap" to "beforeend")
    attrList.add("hx-target" to "#workspace")
    button("$btnClass py-2 px-4", attrs = attrList.toTypedArray()) {
        text(text)
    }
}

fun Html.draggable() {
    script("type" to "text/hyperscript") {
        text("""
            -- Usage: _="install Draggable(dragHandle: .titlebar in me)"

            behavior Draggable(dragHandle)
              init
                if no dragHandle set the dragHandle to me
              end
              on pointerdown(clientX, clientY) from dragHandle
                halt the event
                trigger draggable:start -- hooks, e.g. for adding a drop shadow while dragging
                measure my x, y
                set xoff to clientX - x
                set yoff to clientY - y
                repeat until event pointerup from document
                  wait for pointermove(pageX, pageY) or
                           pointerup  (pageX, pageY) from document
                  add { left: ${'$'}{pageX - xoff}px; top: ${'$'}{pageY - yoff}px; }
                  trigger draggable:move
                end
                trigger draggable:end
            end
        """.trimIndent())
    }
}

fun Html.resizable() {
    script("type" to "text/hyperscript") {
        text("""
            -- Usage: _="install Resizable(resizeHandle: .titlebar in me)"

            behavior Resizable(resizeHandle)
              init
                if no resizeHandle set the resizeHandle to me
              end
              on pointerdown(clientX, clientY) from resizeHandle
                halt the event
                trigger resizable:start -- hooks, e.g. for adding a drop shadow while resizing
                measure my width, height
                set xoff to clientX - width
                set yoff to clientY - height
                repeat until event pointerup from document
                  wait for pointermove(pageX, pageY) or
                           pointerup  (pageX, pageY) from document
                  add { width: ${'$'}{pageX - xoff}px; height: ${'$'}{pageY - yoff}px; }
                  trigger resizable:move
                end
                trigger resizable:end
            end
        """.trimIndent())
    }
}
