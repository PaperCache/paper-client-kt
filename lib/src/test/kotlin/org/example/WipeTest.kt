import kotlin.test.Test
import kotlin.test.*

class WipeTest: PaperClientTest() {
    @Test fun wipe() {
		val client = this.client
		if (client == null) fail()

		assertTrue(client.set("key", "value").is_ok())

		val response = client.wipe()
		assertTrue(response.is_ok())
		assertNotNull(response.data())
		assertNull(response.err_data())

		val got = client.get("key")
		assertFalse(got.is_ok())
		assertNull(got.data())
		assertNotNull(got.err_data())
    }
}
