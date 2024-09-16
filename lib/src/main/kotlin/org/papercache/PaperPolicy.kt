enum class PaperPolicy(val value: Byte) {
	LFU(0),
	FIFO(1),
	LRU(2),
	MRU(3);

	companion object {
		fun from_byte(byte: Byte): PaperPolicy {
			return when (byte.toInt()) {
				0 -> PaperPolicy.LFU
				1 -> PaperPolicy.FIFO
				2 -> PaperPolicy.LRU
				3 -> PaperPolicy.MRU

				else -> throw PaperError("Invalid policy byte.")
			}
		}
	}
}
