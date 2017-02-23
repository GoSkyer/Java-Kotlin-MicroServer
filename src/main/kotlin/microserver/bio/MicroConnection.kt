package microserver.bio


import java.io.IOException
import java.net.Socket

/**
 * Created by OoO on 2017/1/21.
 *
 *
 * 客户端连接类
 */
class MicroConnection(private val mServer: MicroServer, private val mSocket: Socket) {

    /**
     * 客户端连接名称
     */
    val name: String

    /**
     * 客户端地址
     */
    val ip: String

    /**
     * 客户端端口号
     */
    val port: Int

    private var isConnect = false

    private var isSending = false

    /**
     * 监听在其他线程进行
     */
    private val listenStreamThread = object : Thread() {

        override fun run() {
            listenStream()
        }

    }

    init {


        this.ip = mSocket.getInetAddress().hostAddress
        this.port = mSocket.getPort()

        this.name = mSocket.getInetAddress().hostName + ":" + this.port

        this.mServer.onServerAccept(this)
        this.mServer.onConnectionAccept(this)

    }

    /**
     * 启动输入流监听、心跳守护线程
     */
    fun run() {

        isConnect = true

        listenStreamAtThread()
        loopLiveTestAtThread()
    }

    /**
     * 启动输入流监听线程
     */
    private fun listenStreamAtThread() {
        listenStreamThread.start()
    }

    /**
     * 输入流监听
     */
    private fun listenStream() {

        try {

            val stream = mSocket!!.inputStream

            val buffer = ByteArray(256)

            while (isConnected) {

                val length = stream.read(buffer)

                if (length > 0) {
                    onReceive(buffer, length)
                }

            }

            SLog.error("client[$name] end...")

        } catch (e: IOException) {
            onConnectError(e)
        }

    }

    /**
     * 输入流监听回调
     */
    private fun onReceive(buffer: ByteArray, length: Int) {

        val data = String(buffer, 0, length)

        SLog.error("client[$name] receive message on Port(" + mServer.mState!!.port + ")-> " + data)

        mServer.onConnectionReceive(this, buffer, length)
    }

    /**
     * 向客户端发送数据
     */
    fun sendMessageAtMain(data: String): Boolean {

        try {

            if (!isConnected) return false

            isSending = true

            val stream = mSocket!!.outputStream

            val buffer = data.toByteArray()

            stream.write(buffer)

            stream.flush()

            isSending = false

            onSend(buffer)

        } catch (e: IOException) {

            isSending = false

            onConnectError(e)

            return false
        }

        return true
    }

    /**
     * 输出流回调
     */
    private fun onSend(buffer: ByteArray) {
        mServer.onConnectionSend(this, buffer)
    }

    /**
     * 启动心跳守护线程
     */
    private fun loopLiveTestAtThread() {

        object : Thread() {

            override fun run() {

                try {

                    while (isConnected) {

                        if (!isSending) {
                            mSocket.sendUrgentData(0xFF)
                        }

                        try {
                            Thread.sleep(1000)
                        } catch (e: InterruptedException) {
                            SLog.error("thread sleep error...")
                        }

                    }

                } catch (e: IOException) {
                    onConnectError(e)
                }

            }

        }.start()

    }

    fun testConnection(): Boolean {

        if (mSocket == null) return false

        try {

            if (!isSending) {
                isSending = true
                mSocket.sendUrgentData(0xFF)
                isSending = false
            }

        } catch (e: IOException) {
            return false
        }

        return true
    }

    /**
     * 连接异常时处理
     */
    private fun onConnectError(e: IOException) {
        close()
    }

    /**
     * 关闭连接
     */
    fun close(): Boolean {

        if (!isConnect) return false

        isConnect = false

        try {

            mSocket!!.close()

        } catch (ignored: IOException) {
            // ignored exception
            return false
        }

        mServer!!.onConnectionClose(this)

        SLog.error("client[$name] connection closed...")

        return true
    }

    /**
     * 是否连接正常
     */
    private val isConnected: Boolean
        get() = isConnect
                && mServer!!.getState() != null && mServer.getState()!!.isRunning
                && mSocket != null && mSocket.isConnected

}