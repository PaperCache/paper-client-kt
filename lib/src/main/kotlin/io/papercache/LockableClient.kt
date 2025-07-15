/*
 * Copyright (c) Kia Shakiba
 *
 * This source code is licensed under the GNU AGPLv3 license found in the
 * LICENSE file in the root directory of this source tree.
 */

import java.util.concurrent.locks.ReentrantLock

class LockableClient(
	private val client: PaperClient,
	private val mutex: ReentrantLock,
) {
	fun lock(): PaperClient {
		this.mutex.lock()
		return this.client
	}

	fun unlock() {
		this.mutex.unlock()
	}
}
