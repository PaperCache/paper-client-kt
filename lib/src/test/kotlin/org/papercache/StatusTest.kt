import kotlin.test.Test
import kotlin.test.*

class StatusTest: PaperClientTest() {
    @Test fun status() {
		val client = this.client
		if (client == null) fail()

		client.status()
    }
}
