package microserver.bio.listener


import microserver.bio.MicroConnection

/**
 * Created by galaxy on 2017/1/24.

 * 已建立连接的回调
 */
abstract class ConnectionListener {

    /**
     * 连接建立成功
     */
    abstract fun onAccept(connection: MicroConnection)

    /**
     * 连接断开成功
     */
    abstract fun onClose(connection: MicroConnection)

    /**
     * 连接收到数据
     */
    abstract fun onReceive(connection: MicroConnection, buffer: ByteArray, length: Int)

    /**
     * 连接发送数据
     */
    abstract fun onSend(connection: MicroConnection, buffer: ByteArray)

}