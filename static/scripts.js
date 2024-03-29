
function setupEditor(element) {
    var e = ace.edit(element)
    e.setTheme("ace/theme/monokai")
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
