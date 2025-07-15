/*
 * Copyright (c) Kia Shakiba
 *
 * This source code is licensed under the GNU AGPLv3 license found in the
 * LICENSE file in the root directory of this source tree.
 */

import java.util.concurrent.locks.ReentrantLock

class PaperPool(paper_addr: String, size: Int) {
	private val clients: Array<PaperClient> = Array<PaperClient>(size, { _ -> PaperClient(paper_addr) })
	private val locks: Array<ReentrantLock> = Array<ReentrantLock>(size, { _ -> ReentrantLock() })
	private var index: Int = 0

	fun auth(token: String) {
		for (i in this.clients.indices) {
			this.locks[i].lock()
			this.clients[i].auth(token)
			this.locks[i].unlock()
		}
	}

	fun client(): LockableClient {
		val index: Int = this.index
		this.index = (index + 1) % this.clients.size

		return LockableClient(this.clients[index], this.locks[index])
	}
}
