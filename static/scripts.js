
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
            makeDraggable(div);
            makeResizable(div);
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

function makeResizable(elem) {
    var resizer = elem.querySelector(".resizer");
    resizer.onmousedown = resizeMouseDown;

    var startX = 0, startY = 0;
    var offsetX = 0, offsetY = 0;

    function resizeMouseDown(e) {
        e = e || window.event;
        e.preventDefault();
        startX = e.clientX;
        startY = e.clientY;
        document.onmouseup = closeResizeElement;
        document.onmousemove = elementResize;
    }

    function elementResize(e) {
        e = e || window.event;
        e.preventDefault();
        offsetX = e.clientX - startX;
        offsetY = e.clientY - startY;
        startX = e.clientX;
        startY = e.clientY;
        elem.style.width = (elem.offsetWidth + offsetX) + "px";
        elem.style.height = (elem.offsetHeight + offsetY) + "px";
    }

    function closeResizeElement() {
        document.onmouseup = null;
        document.onmousemove = null;
    }
}

function makeDraggable(elem) {
    var header = elem.querySelector(".header");
    header.onmousedown = dragMouseDown;

    var startX = 0, startY = 0;
    var offsetX = 0, offsetY = 0;

    function dragMouseDown(e) {
        e = e || window.event;
        e.preventDefault();
        startX = e.clientX;
        startY = e.clientY;
        document.onmouseup = closeDragElement;
        document.onmousemove = elementDrag;
    }

    function elementDrag(e) {
        e = e || window.event;
        e.preventDefault();
        offsetX = e.clientX - startX;
        offsetY = e.clientY - startY;
        startX = e.clientX;
        startY = e.clientY;
        elem.style.left = (elem.offsetLeft + offsetX) + "px";
        elem.style.top = (elem.offsetTop + offsetY) + "px";
    }

    function closeDragElement() {
        document.onmouseup = null;
        document.onmousemove = null;
    }
}