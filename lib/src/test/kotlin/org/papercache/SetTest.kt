import kotlin.test.Test
import kotlin.test.*

class SetTest: PaperClientTest() {
    @Test fun no_ttl() {
		val client = this.client
		if (client == null) fail()

		val response = client.set("key", "value")

		assertTrue(response.is_ok())
		assertNotNull(response.data())
		assertNull(response.err_data())
    }

    @Test fun ttl() {
		val client = this.client
		if (client == null) fail()

		val response = client.set("key", "value", 2)

		assertTrue(response.is_ok())
		assertNotNull(response.data())
		assertNull(response.err_data())
    }

	@Test fun expiry() {
		val client = this.client
		if (client == null) fail()

		val response = client.set("key", "value", 1)
		assertTrue(response.is_ok())
		assertNotNull(response.data())
		assertNull(response.err_data())

		val get_response = client.get("key")
		assertTrue(get_response.is_ok())
		assertEquals("value", get_response.data())
		assertNull(get_response.err_data())

		Thread.sleep(2000);

		val expired_response = client.get("key")
		assertFalse(expired_response.is_ok())
		assertNull(expired_response.data())
		assertNotNull(expired_response.err_data())
    }
}
