package microserver.bio.manager

import microserver.bio.MicroServer
import microserver.bio.ServerConfig
import microserver.bio.listener.ConnectionListener
import java.util.*

/**
 * Created by guozhong on 17/2/23.
 */
public class ServerManager(val address: String = "unknow") : IServerManager {
    val serverMap = Hashtable<String, MicroServer>()
    override fun open(port: Int): Boolean {
        val config = ServerConfig(address, port)
        val newServer = MicroServer(config)
        val init = newServer.initServer()
        if (!init) return false
        newServer.startServer()
        serverMap.put(autoKey(port), newServer)
        return true
    }

    override fun send(port: Int, args: String): Boolean {
        val server = serverMap.get(autoKey(port))
        val connections = server?.getAllConnectionName()
        if (connections == null || connections.isEmpty())
            return false
        val connection = server?.getConnection(connections.get(0))
        return connection != null && connection.sendMessageAtMain(args)
    }

    override fun addPortListen(port: Int, listener: ConnectionListener) {
        val server = serverMap.get(autoKey(port))
        server?.mConnectionListener = listener

    }

    override fun removePortListen(port: Int) {
        val server = serverMap.get(autoKey(port))
        server?.removeConnectionListener()
    }

    override fun close(port: Int) {
        close(autoKey(port))
    }

    fun close(name: String) {
        val oldServer = serverMap.remove(name)
        oldServer?.closeServer()
    }

    override fun closeAll() {
        serverMap.map { close(it.key) }
    }


    override fun getServer(port: Int): MicroServer {
        return serverMap.get(autoKey(port))!!
    }

    fun autoKey(port: Int) = "$address:$port"

}

