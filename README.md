# AndroGamepad
library and sample to access to various gamepad with common interface  
AndroidでUSB/Blutoothゲームパッドを繋いでも幾つかのボタンが使えなかったりしませんか？
このライブラリとサンプルはゲームパッド間の差異を吸収して統一的にアクセスできるようにします。
`InputManager`を使うタイプとUSB経由で直接データを読み取るタイプの2種類を同梱しています。  
通常は`InputManager`を使いタイプで十分だと思いますが中にはどうしてもボタン等を読めない物もあるのでUSBからのものも用意しています。
もし正常にボタンが読み込めないゲームパッドがあれば変換モジュールを追加していただけるとありがたいです。

Mavenリポジトリとしてアクセス出来るようにしていますので、使うだけであればクローンやサブモジュールにする必要はありません。

Copyright (c) 2015-2016 saki t_saki@serenegiant.com
Distributed under the terms of the GNU Lesser General Public License (LGPL v3.0) License.
License details are in the file license.txt, distributed as part of this software.

# How to use / 使い方
add followings to your build.gradle for you module(not a top level build.gradle).

```
repositories {
    maven { url 'http://raw.github.com/saki4510t/AndroGamepad/master/repository/' }
}

dependencies {
    compile 'com.serenegiant:androgamepad:0.1.0'
}
```

and add followings to you Activity.

```
public class MainActivity extends Activity {

  ...

	private Joystick mJoystick;

  ...

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mJoystick = Joystick.getInstance(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mJoystick != null) {
			mJoystick.register();
		}
	}

	@Override
	protected void onPause() {
		if (mJoystick != null) {
			mJoystick.unregister();
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (mJoystick != null) {
			mJoystick.release();
			mJoystick = null;
		}
		super.onDestroy();
	}

	@Override
	public boolean dispatchKeyEvent(final KeyEvent event) {
		if (mJoystick != null) {
			if (mJoystick.dispatchKeyEvent(event)) {
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean dispatchGenericMotionEvent(final MotionEvent event) {
		if (mJoystick != null) {
			mJoystick.dispatchGenericMotionEvent(event);
		}
		return super.dispatchGenericMotionEvent(event);
	}

  ...
}
```
