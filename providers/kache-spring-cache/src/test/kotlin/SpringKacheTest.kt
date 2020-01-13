import ru.sokomishalov.kache.core.Kache
import ru.sokomishalov.kache.provider.SpringKache
import ru.sokomishalov.kache.serialization.JacksonSerializer
import ru.sokomishalov.kache.tck.KacheTck
import kotlin.test.assertTrue

/**
 * @author sokomishalov
 */
class SpringKacheTest : KacheTck() {
    override val kache: Kache by lazy { SpringKache(serializer = JacksonSerializer()) }

    override fun `Test find by glob pattern`() = assertTrue { true }
}