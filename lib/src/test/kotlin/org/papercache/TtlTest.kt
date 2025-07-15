import kotlin.test.Test
import kotlin.test.*

class TtlTest: PaperClientTest() {
    @Test fun non_existent() {
		val client = this.client
		if (client == null) fail()

        val err = assertFailsWith<PaperError> {
            client.ttl("key")
        }

        assertEquals(err.type, PaperError.Type.KEY_NOT_FOUND)
    }

    @Test fun existent() {
		val client = this.client
		if (client == null) fail()

		client.set("key", "value")
		client.ttl("key", 1U)
    }
}
