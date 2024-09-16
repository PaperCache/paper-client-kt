import kotlin.test.Test
import kotlin.test.*

class AuthTest: PaperClientTest(false) {
    @Test fun incorrect() {
		val client = this.client
		if (client == null) fail()

		assertFalse(client.set("key", "value").is_ok())

		val response = client.auth("incorrect_auth_token")
		assertFalse(response.is_ok())

		assertFalse(client.set("key", "value").is_ok())
    }

	@Test fun correct() {
		val client = this.client
		if (client == null) fail()

		assertFalse(client.set("key", "value").is_ok())

		val response = client.auth("auth_token")
		assertTrue(response.is_ok())

		assertTrue(client.set("key", "value").is_ok())
    }
}
