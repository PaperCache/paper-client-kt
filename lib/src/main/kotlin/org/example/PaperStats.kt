class PaperStats(
	val max_size: Long,
	val used_size: Long,

	val total_gets: Long,
	val total_sets: Long,
	val total_dels: Long,

	val miss_ratio: Double,

	policy_byte: Byte,

	val uptime: Long,
) {
	val policy: PaperPolicy = PaperPolicy.from_byte(policy_byte)
}
