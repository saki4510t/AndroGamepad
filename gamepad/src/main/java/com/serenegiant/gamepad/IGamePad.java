package com.serenegiant.gamepad;
/*
 * AndroGamepad
 * library and sample to access to various gamepad with common interface
 *
 * Copyright (c) 2015-2016 saki t_saki@serenegiant.com
 *
 * File name: IGamePad.java
 *
 * Distributed under the terms of the GNU Lesser General Public License (LGPL v3.0) License.
 * License details are in the file license.txt, distributed as part of this software.
*/

public abstract class IGamePad {
	/**
	 * キーの押し下げ状態と押し下げしている時間[ミリ秒]を取得
	 * @param downs KEY_NUMS個以上確保しておくこと
	 * @param down_times KEY_NUMS個以上確保しておくこと
	 * @param analog_sticks 4個以上確保しておくこと
	 */
	public abstract void updateState(final boolean[] downs, final long[] down_times, final int[] analog_sticks, final boolean force);

}
