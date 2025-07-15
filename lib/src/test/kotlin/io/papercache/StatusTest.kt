/*
 * Copyright (c) Kia Shakiba
 *
 * This source code is licensed under the GNU AGPLv3 license found in the
 * LICENSE file in the root directory of this source tree.
 */

import kotlin.test.Test
import kotlin.test.*

class StatusTest: PaperClientTest() {
    @Test fun status() {
		val client = this.client
		if (client == null) fail()

		client.status()
    }
}
