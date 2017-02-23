package microserver.bio

/**
 * Created by galaxy on 2017/2/9.
 */
object SLog {

    private val isDebug = true

    private val TAG = "TAG"

    fun error(s: String) {
        if (isDebug) {
            System.err.println("$TAG-$s")
        }
    }


}
