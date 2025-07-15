class PaperStatus (
	val pid: UInt,

	val max_size: ULong,
	val used_size: ULong,
	val num_objects: ULong,

	val rss: ULong,
	val hwm: ULong,

	val total_gets: ULong,
	val total_sets: ULong,
	val total_dels: ULong,

	val miss_ratio: Double,

	val policies: Array<String>,
	val policy: String,
	val is_auto_policy: Boolean,

	val uptime: ULong,
)
