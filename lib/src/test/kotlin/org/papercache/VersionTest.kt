import kotlin.test.Test
import kotlin.test.*

class VersionTest: PaperClientTest() {
    @Test fun version() {
		val client = this.client
		if (client == null) fail()

		val version = client.version()
		assertNotNull(version)
    }
}
