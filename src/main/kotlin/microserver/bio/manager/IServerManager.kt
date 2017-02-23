package microserver.bio.manager;


import microserver.bio.MicroServer;
import microserver.bio.listener.ConnectionListener;

/**
 * Created by galaxy on 2017/2/9.
 */

interface IServerManager {

    fun open(port: Int): Boolean

    fun send(port: Int, args: String): Boolean

    fun addPortListen(port: Int, listener: ConnectionListener)

    fun removePortListen(port: Int)

    fun close(port: Int)

    fun closeAll()

    fun getServer(port: Int): MicroServer

}
