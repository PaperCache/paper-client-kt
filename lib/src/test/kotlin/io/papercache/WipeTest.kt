/*
 * Copyright (c) Kia Shakiba
 *
 * This source code is licensed under the GNU AGPLv3 license found in the
 * LICENSE file in the root directory of this source tree.
 */

import kotlin.test.Test
import kotlin.test.*

class WipeTest: PaperClientTest() {
    @Test fun wipe() {
		val client = this.client
		if (client == null) fail()

		client.set("key", "value")
        client.wipe()

        val err = assertFailsWith<PaperError> {
            client.get("key")
        }

        assertEquals(err.type, PaperError.Type.KEY_NOT_FOUND)
    }
}
