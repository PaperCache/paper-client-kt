class PaperResposnse<T>(data: T?, err_data: String? = null) {
	private val data: T? = data
	private val err_data: String? = err_data

	fun is_ok(): Boolean {
		return this.data != null
	}

	fun data(): T? {
		return this.data
	}

	fun err_data(): String? {
		return this.err_data
	}
}
