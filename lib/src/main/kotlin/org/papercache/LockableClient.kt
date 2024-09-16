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
