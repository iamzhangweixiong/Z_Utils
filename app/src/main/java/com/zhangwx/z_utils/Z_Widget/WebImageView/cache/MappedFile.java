package com.zhangwx.z_utils.Z_Widget.WebImageView.cache;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

public class MappedFile {
	private static final int EXPAND_SIZE = 1024;

	private FileChannel mChannel;
	private MappedByteBuffer mBuffer;

	public MappedFile() {
	}

	public void open(File file, boolean readOnly) throws Exception {
		if (mChannel != null)
			close();

		mChannel = new RandomAccessFile(file, readOnly ? "r" : "rw").getChannel();
		mBuffer = mChannel.map(readOnly ? MapMode.READ_ONLY : MapMode.READ_WRITE, 0, mChannel.size());
	}

	public void close() {
		mBuffer = null;
		try {
			mChannel.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mChannel = null;
	}

	public boolean isOpened() {
		return mBuffer != null;
	}

	public int size() {
		return mBuffer != null ? mBuffer.limit() : 0;
	}

	public void expand() throws Exception {
		final int size = mBuffer.limit();
		final int expand = getExpandSize();
		final int newSize = size + expand;

		// re-map the file with expanded size
		mBuffer = mChannel.map(MapMode.READ_WRITE, 0, newSize);
		// write 0 to expanded buffer
		for (int i = size; i < newSize - 4; i += 4)
			mBuffer.putLong(i, 0);
		for (int i = size + expand / 4 * 4; i < newSize; i++)
			mBuffer.put(i, (byte) 0);
	}

	public void force() {
		if (mBuffer != null)
			mBuffer.force();
	}

	public short readShort(int index) throws Exception {
		return mBuffer.getShort(index);
	}

	public void writeShort(int index, short value) throws Exception {
		for (;;) {
			try {
				mBuffer.putShort(index, value);
				return;
			} catch (IndexOutOfBoundsException e) {
				expand();
			}
		}
	}

	public int readInt(int index) throws Exception {
		return mBuffer.getInt(index);
	}

	public void writeInt(int index, int value) throws Exception {
		for (;;) {
			try {
				mBuffer.putInt(index, value);
				return;
			} catch (IndexOutOfBoundsException e) {
				expand();
			}
		}
	}

	public long readLong(int index) throws Exception {
		return mBuffer.getLong(index);
	}

	public void writeLong(int index, long value) throws Exception {
		for (;;) {
			try {
				mBuffer.putLong(index, value);
				return;
			} catch (IndexOutOfBoundsException e) {
				expand();
			}
		}
	}

	protected int getExpandSize() {
		return EXPAND_SIZE;
	}
}
