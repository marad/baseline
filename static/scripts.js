
var nextWindowId = 0;

function setupEditor(element) {
    var e = ace.edit(element)
//    e.setTheme("ace/theme/monokai")
    e.setKeyboardHandler("ace/keyboard/vim");
    e.renderer.setOptions({
        fontSize: 17,
    })
    e.setOptions({
      enableBasicAutocompletion: true,
      enableLiveAutocompletion: true,
      showLineNumbers: false,
      showGutter: false,
      highlightActiveLine: false,
      enableAutoIndent: true,
    });
    e.session.setMode("ace/mode/lua");
}

function getCode(editorId) {
    return document.querySelector(editorId).env.editor.getValue();
}

function getNextWindowId() {
    var id = nextWindowId;
    nextWindowId += 1;
    return id;
}

function createWindow(windowType) {
    var div = document.createElement("div");
    var id = getNextWindowId();
    div.id = "window-" + id;
    div.className = "window absolute";

    var workspace = document.querySelector("#workspace");
    workspace.appendChild(div);
    div.style.width = 400 + "px";
    div.style.height = 300 + "px";
    htmx.ajax("GET", '/window/' + windowType + "/" + id, "#" + div.id)
        .then(() => {
            var editor = div.querySelector(".code")
            if (editor) {
                var e = ace.edit(editor)
                e.setTheme("ace/theme/monokai")
                e.session.setMode("ace/mode/lua")
            }
        });
}

function closeWindow(id) {
    document.querySelector("#window-" + id).remove();
}
