package gh.marad.baseline

import party.iroiro.luajava.ExternalLoader
import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.notExists

class CodeManager(val root: Path) {
    fun storeCode(domain: String, module: String, code: String) {
        val dir = root.resolve(domain)
        val path = dir.resolve("$module.lua")
        Files.createDirectories(dir)
        Files.writeString(path, code)
    }

    fun loadCode(domain: String, module: String): String? {
        val dir = root.resolve(domain)
        if (dir.notExists()) return null
        val path = dir.resolve("$module.lua")
        if (path.notExists()) return null
        return Files.readString(path)
    }

    fun loadByteBuffer(domain: String, module: String): ByteBuffer? {
        val dir = root.resolve(domain)
        if (dir.notExists()) return null
        val path = dir.resolve("$module.lua")
        if (path.notExists()) return null
        val bytes = Files.readAllBytes(path)
        val buffer = ByteBuffer.allocateDirect(bytes.size)
        buffer.put(bytes)
        return buffer
    }

    fun createExternalLoader(): ExternalLoader =
        ExternalLoader { modulePath, _ ->
            val (domain, module) = modulePath.split("::")
            loadByteBuffer(domain, module)
        }
}