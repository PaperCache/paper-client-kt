import kotlin.test.Test
import kotlin.test.*

class VersionTest: PaperClientTest() {
    @Test fun version() {
		val client = this.client
		if (client == null) fail()

		val response = client.version()

		assertTrue(response.is_ok())
		assertNotNull(response.data())
		assertNull(response.err_data())
    }
}
