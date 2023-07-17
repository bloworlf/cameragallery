package io.drdroid.camera_gallery.utils

import android.os.Environment
import android.os.StatFs
import java.io.File
import java.text.DecimalFormat

class DiskUtils {
    var size: Long = 0
        private set

    constructor() {}
    constructor(size: Long) {
        this.size = size
    }

    /**
     * Returns the amount of total memory.
     *
     * @return `long` - Total space.
     */
    val totalInternalMemory: DiskUtils
        get() {
            size = getTotalMemory(Environment.getDataDirectory())
            return this
        }

    /**
     * Returns the amount of free memory.
     *
     * @return `long` - Free space.
     */
    val freeInternalMemory: DiskUtils
        get() {
            size = getFreeMemory(Environment.getDataDirectory())
            return this
        }

    /**
     * Returns the total amount in SDCARD.
     *
     * @return `long` - Total space.
     */
    val totalExternalMemory: DiskUtils
        get() {
            size = getTotalMemory(Environment.getExternalStorageDirectory())
            return this
        }

    /**
     * Returns the free amount in SDCARD.
     *
     * @return `long` - Free space.
     */
    val freeExternalMemory: DiskUtils
        get() {
            size = getFreeMemory(Environment.getExternalStorageDirectory())
            return this
        }

    /**
     * Returns the free amount in OS.
     *
     * @return `long` - Free space.
     */
    val freeSystemMemory: DiskUtils
        get() {
            size = getFreeMemory(Environment.getRootDirectory())
            return this
        }

    /**
     * Returns the free amount in mounted path
     *
     * @param path [File] - Mounted path.
     * @return `long` - Free space.
     */
    private fun getFreeMemory(path: File?): Long {
        if (null != path && path.exists() && path.isDirectory) {
            val stats = StatFs(path.absolutePath)
            return stats.availableBlocksLong * stats.blockSizeLong
        }
        return -1
    }

    /**
     * Returns the total amount in mounted path
     *
     * @param path [File] - Mounted path.
     * @return `long` - Total space.
     */
    private fun getTotalMemory(path: File?): Long {
        if (null != path && path.exists() && path.isDirectory) {
            val stats = StatFs(path.absolutePath)
            return stats.blockCountLong * stats.blockSizeLong
        }
        return -1
    }

    /**
     * Convert bytes to human format.
     * //     * @param totalBytes `long` - Total of bytes.
     *
     * @return [String] - Converted size.
     */
    fun bytesToHuman(): String {
//        String[] symbols = new String[]{"B", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB"};
        val symbols = arrayOf("B", "KB", "MB", "GB", "TB", "PB", "EB")
        var scale = 1L
        for (symbol in symbols) {
            if (size < scale * 1024L) {
                return String.format(
                    "%s %s",
                    DecimalFormat("#.##").format(size.toDouble() / scale),
                    symbol
                )
            }
            scale *= 1024L
        }
        return "-1 B"
    }

    override fun toString(): String {
        return bytesToHuman()
    }
}