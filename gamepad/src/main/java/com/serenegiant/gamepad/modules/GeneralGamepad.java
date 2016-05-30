package com.serenegiant.gamepad.modules;
/*
 * AndroGamepad
 * library and sample to access to various gamepad with common interface
 *
 * Copyright (c) 2015-2016 saki t_saki@serenegiant.com
 *
 * File name: GeneralGamepad.java
 *
 * Distributed under the terms of the GNU Lesser General Public License (LGPL v3.0) License.
 * License details are in the file license.txt, distributed as part of this software.
*/

import android.hardware.usb.UsbDevice;

import com.serenegiant.gamepad.HIDParser;

import static com.serenegiant.gamepad.GamePadConst.KEY_CENTER_LEFT;
import static com.serenegiant.gamepad.GamePadConst.KEY_CENTER_RIGHT;
import static com.serenegiant.gamepad.GamePadConst.KEY_LEFT_1;
import static com.serenegiant.gamepad.GamePadConst.KEY_LEFT_2;
import static com.serenegiant.gamepad.GamePadConst.KEY_LEFT_CENTER;
import static com.serenegiant.gamepad.GamePadConst.KEY_LEFT_DOWN;
import static com.serenegiant.gamepad.GamePadConst.KEY_LEFT_LEFT;
import static com.serenegiant.gamepad.GamePadConst.KEY_LEFT_RIGHT;
import static com.serenegiant.gamepad.GamePadConst.KEY_LEFT_UP;
import static com.serenegiant.gamepad.GamePadConst.KEY_RIGHT_1;
import static com.serenegiant.gamepad.GamePadConst.KEY_RIGHT_2;
import static com.serenegiant.gamepad.GamePadConst.KEY_RIGHT_A;
import static com.serenegiant.gamepad.GamePadConst.KEY_RIGHT_B;
import static com.serenegiant.gamepad.GamePadConst.KEY_RIGHT_C;
import static com.serenegiant.gamepad.GamePadConst.KEY_RIGHT_CENTER;
import static com.serenegiant.gamepad.GamePadConst.KEY_RIGHT_D;
import static com.serenegiant.gamepad.GamePadConst.KEY_RIGHT_DOWN;
import static com.serenegiant.gamepad.GamePadConst.KEY_RIGHT_LEFT;
import static com.serenegiant.gamepad.GamePadConst.KEY_RIGHT_RIGHT;
import static com.serenegiant.gamepad.GamePadConst.KEY_RIGHT_UP;

public class GeneralGamepad extends HIDParser {

	public GeneralGamepad(final UsbDevice device) {
		super(device);
	}

	private static final int EPS = 8;
	@Override
	protected void parse(final int n, final byte[] data) {
		if (n < 6) return;
		// 左アナログスティック。不感領域を設ける
		analogSticks[0] =(data[0] & 0xff) - 0x7f;
		if (Math.abs(analogSticks[0]) < EPS) analogSticks[0] = 0;
		analogSticks[1] = (data[1] & 0xff) - 0x7f;
		if (Math.abs(analogSticks[1]) < EPS) analogSticks[1] = 0;
		//
		final byte d5 = data[5];
		final byte d6 = data[6];

		keyCount[KEY_LEFT_1] = (d6 & 0x01) != 0 ? keyCount[KEY_LEFT_1] + 1 : 0;
		keyCount[KEY_RIGHT_1] = (d6 & 0x02) != 0 ? keyCount[KEY_RIGHT_1] + 1 : 0;
		keyCount[KEY_LEFT_2] = (d6 & 0x04) != 0 ? keyCount[KEY_LEFT_2] + 1 : 0;
		keyCount[KEY_RIGHT_2] = (d6 & 0x08) != 0 ? keyCount[KEY_RIGHT_2] + 1 : 0;

		keyCount[KEY_LEFT_CENTER] = (d6 & 0x10) != 0 ? keyCount[KEY_LEFT_CENTER] + 1 : 0;
		keyCount[KEY_RIGHT_CENTER] = (d6 & 0x20) != 0 ?+keyCount[KEY_RIGHT_CENTER] + 1 : 0;
		keyCount[KEY_CENTER_LEFT] = (d6 & 0x40) != 0 ? keyCount[KEY_CENTER_LEFT] + 1 : 0;
		keyCount[KEY_CENTER_RIGHT] = (d6 & 0x80) != 0 ?+keyCount[KEY_CENTER_RIGHT] + 1 : 0;

		// DPAD(左キーパッド)
		final int dpad = (d5 & 0x0f);
		if ((dpad == 0x0f) && (n < 7) || ((data[7] & 0x80) != 0)) {
			// n<7でdpadが押されていない時とデジタルモードの時は, 左アナログスティックからDPADデータを生成
			keyCount[KEY_LEFT_LEFT] = analogSticks[0] < 0 ? keyCount[KEY_LEFT_LEFT] + 1 : 0;
			keyCount[KEY_LEFT_UP] = analogSticks[1] < 0 ? keyCount[KEY_LEFT_UP] + 1 : 0;
			keyCount[KEY_LEFT_DOWN] = analogSticks[1] > 0 ? keyCount[KEY_LEFT_DOWN] + 1 : 0;
			keyCount[KEY_LEFT_RIGHT] = analogSticks[0] > 0 ? keyCount[KEY_LEFT_RIGHT] + 1 : 0;
			// 右キーパッド
			keyCount[KEY_RIGHT_LEFT] = (d5 & 0x80) != 0 ? keyCount[KEY_RIGHT_LEFT] + 1 : 0;
			keyCount[KEY_RIGHT_UP] = (d5 & 0x10) != 0 ? keyCount[KEY_RIGHT_UP] + 1 : 0;
			keyCount[KEY_RIGHT_DOWN] = (d5 & 0x40) != 0 ? keyCount[KEY_RIGHT_DOWN] + 1: 0;
			keyCount[KEY_RIGHT_RIGHT] = (d5 & 0x20) != 0 ? keyCount[KEY_RIGHT_RIGHT] + 1 : 0;
			// 右アナログスティック
			analogSticks[2] = keyCount[KEY_RIGHT_LEFT] != 0 ? -0x7f : (keyCount[KEY_RIGHT_RIGHT] != 0 ? 0x7f : 0);
			analogSticks[3] = keyCount[KEY_RIGHT_UP] != 0 ? -0x7f : (keyCount[KEY_RIGHT_DOWN] != 0 ? 0x7f : 0);
		} else {
			final int dir = DPAD_DIRECTIONS[dpad];
			keyCount[KEY_LEFT_LEFT] = (dir & DPAD_LEFT) != 0 ? keyCount[KEY_LEFT_LEFT] + 1 : 0;
			keyCount[KEY_LEFT_UP] = (dir & DPAD_UP) != 0 ? keyCount[KEY_LEFT_UP] + 1 : 0;
			keyCount[KEY_LEFT_DOWN] = (dir & DPAD_DOWN) != 0 ? keyCount[KEY_LEFT_DOWN] + 1 : 0;
			keyCount[KEY_LEFT_RIGHT] = (dir & DPAD_RIGHT) != 0 ? keyCount[KEY_LEFT_RIGHT] + 1 : 0;
		}

		if (n < 8) return;
		if ((data[7] & 0x80) == 0) {
			// アナログモード
			keyCount[KEY_RIGHT_A] = (d5 & 0x20) != 0 ? keyCount[KEY_RIGHT_A] + 1 : 0;
			keyCount[KEY_RIGHT_B] = (d5 & 0x80) != 0 ? keyCount[KEY_RIGHT_B] + 1 : 0;
			keyCount[KEY_RIGHT_C] = (d5 & 0x40) != 0 ? keyCount[KEY_RIGHT_C] + 1: 0;
			keyCount[KEY_RIGHT_D] = (d5 & 0x10) != 0 ? keyCount[KEY_RIGHT_D] + 1 : 0;
			//
			// 右アナログスティック。不感領域を設ける
			analogSticks[2] = (data[3] & 0xff) - 0x80;
			if (Math.abs(analogSticks[2]) < EPS) analogSticks[2] = 0;
			analogSticks[3] = (data[4] & 0xff) - 0x80;
			if (Math.abs(analogSticks[3]) < EPS) analogSticks[3] = 0;
		}
	}
}
