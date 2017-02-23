package microserver.bio


import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by OoO on 2017/1/21.
 *
 *
 * 服务器运行时状态
 */
class ServerState(private val mServer: MicroServer) {


    var isRunning = false


    var connectionTotal = 0
        private set

    private val connectionMap = ConcurrentHashMap<String, MicroConnection>()

    val localAddress: String
        get() = mServer.mConfig!!.localAddress

    val port: Int
        get() = mServer.mConfig!!.port

    fun cutConnection(connection: MicroConnection): Int {

        connectionMap.remove(connection.name)

        return --connectionTotal
    }

    fun addConnection(connection: MicroConnection): Int {

        connectionMap.put(connection.name, connection)

        return ++connectionTotal
    }

    /**
     * 获取一个连接
     */
    fun getConnection(name: String): MicroConnection? {
        return connectionMap[name]
    }

    /**
     * 获取全部连接
     */
    val allConnection: Map<String, MicroConnection>
        get() = connectionMap

    /**
     * 获取全部连接的名字
     */
    val allConnectionName: List<String>
        get() {

            val list = ArrayList<String>()

            val keys = connectionMap.keys()

            while (keys.hasMoreElements()) {
                val key = keys.nextElement()
                list.add(key)
            }
            return list
        }

    /**
     * 关闭一个连接
     */
    fun closeConnection(connection: MicroConnection) {
        connection.close()
    }

    /**
     * 关闭全部连接
     */
    fun closeAllConnection() {

        val set = connectionMap.entries

        for ((key, value) in set) {
            closeConnection(value)
        }

    }

}

