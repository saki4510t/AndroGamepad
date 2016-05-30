# AndroGamepad
library and sample to access to various gamepad with common interface

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
