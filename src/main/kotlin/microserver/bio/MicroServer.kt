package microserver.bio


import microserver.bio.listener.ConnectionListener
import microserver.bio.listener.ServerListener
import java.io.IOException
import java.net.ServerSocket

/**
 * Created by guozhong on 17/2/14.
 */
public final class MicroServer(var mConfig: ServerConfig? = ServerConfig()) {
//    private val DEFAULT_CONFIG: ServerConfig = ServerConfig()

    var mServer: ServerSocket? = null

//    var mConfig: ServerConfig? = null

    var mState: ServerState? = null

    var mServerListener: ServerListener? = null

    var mConnectionListener: ConnectionListener? = null

    internal fun onServerCreate(): Unit {
        mServerListener?.onCreate(mConfig, mState)
    }

    fun onServerStart() = mServerListener?.onStart(mConfig, mState)

    fun onServerClose() = mServerListener?.onClose(mConfig, mState)

    fun onServerAccept(connection: MicroConnection) = mServerListener?.onAccept(connection)

    fun removeConnectionListener() = mConnectionListener.to(null)

    fun onConnectionAccept(connection: MicroConnection) {
        mState?.addConnection(connection)
        mConnectionListener?.onAccept(connection)
    }

    fun onConnectionClose(connection: MicroConnection) {
        mState?.cutConnection(connection)
        mConnectionListener?.onClose(connection)
    }

    fun onConnectionSend(connection: MicroConnection, buffer: ByteArray) {
        mConnectionListener?.onSend(connection, buffer)
    }

    fun onConnectionReceive(connection: MicroConnection, buffer: ByteArray, length: Int) {
        mConnectionListener?.onReceive(connection, buffer, length)
    }

    private final val waitForConnectTread: Thread = Thread() {
        while (mState != null && mState!!.isRunning() && mServer != null) {
            // 连接数不能大于设定的最大值
            if ((mState?.getConnectionTotal() as Int) < (mConfig?.getConnectionMaximum() as Int)) {
                // 等待连接
                serverAccept()
            }

        }
    }

    fun initServer(): Boolean {

        try {

            SLog.error("Server initialize starting...")

            // 初始化ServerSocket服务
            mServer = ServerSocket(mConfig?.port as Int)

            // 初始化服务器信息
            mState = ServerState(this)

            // 设置服务器状态
            mState?.setRunning(true)

            SLog.error("Server initialize success...")

            // 服务器回调
            onServerCreate()

        } catch (e: IOException) {

            onError(e)

            SLog.error("Server initialize failed...")

            return false

        }

        return true

    }

    fun startServer() {

        try {

            waitForConnectAtThread()
        } finally {
            // 服务器启动回调
            onServerStart()
        }

    }

    private fun waitForConnectAtThread() {
        waitForConnectTread.start()
    }

    private fun serverAccept() {
        try {
            SLog.error("Waiting for client connect...")
            val socket = mServer?.accept()
            if (socket != null && socket.isConnected) {
                val name = socket.inetAddress.hostName
                val ip = socket.inetAddress.hostAddress
                val port = socket.port
                MicroConnection(this, socket).run()
                SLog.error("new client[$name($ip:$port)] connect success...")
            } else {
                SLog.error("new client error...")
            }
        } catch (e: IOException) {
            onError(e)
            SLog.error("Wait for connect stopped that Server has been closed...")
        }
    }

    fun closeServer() {

        SLog.error("Server is closing...")

        try {

            if (mServer != null) {
                mServer?.close()
            }

        } catch (e: IOException) {
            onError(e)
        }

        mState?.setRunning(false)
        mState?.closeAllConnection()

        mServer = null
        mState = null
        mConfig = null

        SLog.error("Server has closed...")

        // 服务器关闭回调
        onServerClose()

    }

    private fun onError(e: IOException) {

    }

    /**
     * 增加一个客户端连接
     */
    private fun addConnection(connection: MicroConnection) {
        if (mState != null) mState?.addConnection(connection)
    }

    /**
     * 关闭一个客户端连接
     */
    private fun removeConnection(connection: MicroConnection) {
        if (mState != null) mState?.cutConnection(connection)
    }

    /**
     * 获取一个客户端连接
     */
    fun getConnection(name: String): MicroConnection? {
        return if (mState == null) null else mState?.getConnection(name)
    }

    /**
     * 获取全部客户端连接
     */
    fun getAllConnection(): Map<String, MicroConnection>? {
        return if (mState == null) null else mState?.getAllConnection()
    }

    /**
     * 获取用户列表
     */
    fun getAllConnectionName(): List<String>? {
        return if (mState == null) null else mState?.getAllConnectionName()
    }

    /**
     * 获取服务器状态
     */
    fun getState(): ServerState? {
        return if (mState == null) null else mState
    }

}


