/*
 * Copyright (c) Kia Shakiba
 *
 * This source code is licensed under the GNU AGPLv3 license found in the
 * LICENSE file in the root directory of this source tree.
 */

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
