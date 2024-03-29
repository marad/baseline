package gh.marad.baseline

import party.iroiro.luajava.Lua
import party.iroiro.luajava.lua54.Lua54
import party.iroiro.luajava.value.LuaValue
import java.nio.ByteBuffer

val lua = run {
    val lua = Lua54()
    lua.openLibraries()
//    lua.run("System = java.import('java.lang.System')")
//    lua.register("greet") {
//        val name = it.get().toJavaObject() as String
//        println("Hello $name")
//        0
//    }
    lua
}

fun compileCode(code: String): ByteBuffer {
    val lua = Lua54()
    lua.load(code)
    return lua.dump()!!
}

fun luaListChildren(symbol: String): List<Pair<String, LuaValue>> {
    val env = lua.execute("""
        local x = {}
        for k, v in pairs($symbol) do
            table.insert(x, {k, v})
        end
        return x
    """.trimIndent()) ?: return emptyList()

    val data = env[0]
    var i = 1
    val elements = mutableListOf<Pair<String, LuaValue>>()
    while (true) {
        val tmp = data?.get(i++) ?: break
        if(tmp.type() == Lua.LuaType.NIL) break
        val name = tmp.get(1)!!.toJavaObject()!!.toString()
        val value = tmp.get(2)!!
        elements.add(name to value)
    }

    elements.sortBy { it.first }
    return elements.filter { it.second.type() == Lua.LuaType.TABLE } + elements.filter { it.second.type() != Lua.LuaType.TABLE }
}

