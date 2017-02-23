package microserver.bio


/**
 * Created by OoO on 2017/1/21.
 *
 *
 * 服务器配置类
 */

class ServerConfig constructor(
        var localAddress: String = ServerConfig.UNKNOW_IP, var port: Int = ServerConfig.DEFAULT_PORT,
        var connectionMaximum: Int = ServerConfig.DEFAULT_MAX) {


    companion object {

        private val UNKNOW_IP = "unknow"

        private val DEFAULT_PORT = 9999

        private val DEFAULT_MAX = 1
    }

}