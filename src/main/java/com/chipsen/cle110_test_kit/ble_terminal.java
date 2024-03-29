/*<저작권>
본 프로그램 및 소스코드는 (주)칩센의 저작물입니다.
본 프로그램 및 소스코드는 (주)칩센의 블루투스 제품을 구입한 고객에게 제공되는 것 입니다.
당사의 블루투스 제품을 활용할 목적 이외의 용도로 사용하는 것을 금지합니다.

<License>
The program or internal source codes was created by CHIPSEN Co.,Ltd.
In order to use the program or internal source codes, you must buy CHIPSEN's Bluetooth products.
You are not allowed to use it for purposes other than CHIPSEN's Bluetooth Products

Copyright 2015. CHIPSEN all rights reserved.*/

package com.chipsen.cle110_test_kit;


import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.chipsen.bleservice.SampleGattAttributes;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
//import android.widget.TextView.OnEditorActionListener;

public class ble_terminal extends BleFragment{
	
	
	ble_terminal mble_terminal;
	final String[] fontSizeItems = {"20","25","30","35","40","45","50","55","60","70"};
	final String[] echoItems = {"Off","On"};
	public static int TEXT_fontsize=50;
	int TEXT_fontsize_add=0;
	public static boolean echoItems_check=false;
	
	/**
	 * Set to true to add debugging code and logging.
	 */
	public static final boolean DEBUG = true;

	/**
	 * Set to true to log each character received from the remote process to the
	 * android log, which makes it easier to debug some kinds of problems with
	 * emulating escape sequences and control codes.
	 */
	public static final boolean LOG_CHARACTERS_FLAG = DEBUG && false;

	/**
	 * Set to true to log unknown escape sequences.
	 */
	public static final boolean LOG_UNKNOWN_ESCAPE_SEQUENCES = DEBUG && false;

	/**
	 * The tag we use when logging, so that our messages can be distinguished
	 * from other messages in the log. Public because it's used by several
	 * classes.
	 */
	 private final static String TAG = NavigationActivity.class.getSimpleName();
	public static final String LOG_TAG = "BlueTerm";
	
	public boolean BlueMoveview = true; 
	public static final int WHITE = 0xffffffff;
	public static final int BLACK = 0xff000000;
	
	private TermKeyListener mKeyListener;
	
	public EmulatorView mEmulatorView;
	public LinearLayout exlinearLayoutView;
	public FrameLayout mlinearloayout_title;
	
	ImageView imageView_setting;
	EditText meditText1;
	Button mbutton1;
	 SampleGattAttributes mSampleGattAttributes;
	 

	 public int actionBar_Heigh=0;
	 public int editbox_Heigh=0;
	 public int title_heigh=0;

		
	@SuppressWarnings("deprecation")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.term_activity, container, false);
		
		
		
		
		 Log.d(TAG, "park ble_terminal= onCreateView_0");
		exlinearLayoutView = (LinearLayout) v.findViewById(R.id.linearLayoutedit1);
		mlinearloayout_title = (FrameLayout) v.findViewById(R.id.linearloayout_title);
		 Log.d(TAG, "park ble_terminal= onCreateView_1");
		meditText1 = (EditText) v.findViewById(R.id.editText1);
		actionBar_Heigh=NavigationActivity.title_bar.getHeight();
		
	

		imageView_setting=(ImageView)v.findViewById(R.id.imageView_setting); 
		imageView_setting.setOnClickListener(new OnClickListener() { // search
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					
					AlerEditor();
				}
			});
		 
		 
		
		 Log.d(TAG, "park ble_terminal= onCreateView_2="+actionBar_Heigh);
		meditText1.setPrivateImeOptions("defaultInputmode=english;"); 
		mbutton1 =(Button)v.findViewById(R.id.button1);
		mbutton1.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("Wakelock")
			public void onClick(View v) {
				String strbuff=meditText1.getText().toString()+"\r\n";
				datasend(strbuff.getBytes());
			}
		});
		
	

		
		meditText1.setOnKeyListener(new OnKeyListener() {                 
	        public boolean onKey(View v, int keyCode, KeyEvent event) {
	            //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
	        	switch (event.getKeyCode()) {
				case KeyEvent.KEYCODE_ENTER:
					if(meditText1.getText().toString().length()>=1){
					String strbuff=meditText1.getText().toString()+"\r\n";
					datasend(strbuff.getBytes());
					}
					meditText1.setText("");
				break;
				case KeyEvent.KEYCODE_FORWARD_DEL:
					meditText1.setText("");
				break;
				default:
				
				return false;
				}
				return true;
				
	            }
	    });
		mEmulatorView=(EmulatorView)v.findViewById(R.id.emulatorView);
		mble_terminal=this;
		mEmulatorView.initialize(mble_terminal);
		mDisgetHandler.sendEmptyMessageDelayed(0, 100); // ?�람 ?�행
	
		 return v;
	}
	
	private void AlerEditor() {
		
		 
		
		for (int j = 0 ; j< fontSizeItems.length; j++) { //font 위치 확인 
			if(TEXT_fontsize == Integer.parseInt(fontSizeItems[j])){
				TEXT_fontsize_add=j;
				break;
			}	
		}
				
		LinearLayout linearLayoutView = new LinearLayout(NavigationActivity.Mcontext);
		LinearLayout linearLayoutView1 = new LinearLayout(NavigationActivity.Mcontext);
		LinearLayout linearLayoutView2 = new LinearLayout(NavigationActivity.Mcontext);

		
		final Spinner font_list = new Spinner(NavigationActivity.Mcontext);
		final Spinner echo_list = new Spinner(NavigationActivity.Mcontext);
		final TextView tv1 = new TextView(NavigationActivity.Mcontext);//
		final TextView tv2 = new TextView(NavigationActivity.Mcontext);//
		
		LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
		tv1.setLayoutParams(layoutParams);
		tv1.setTextSize(20);
		font_list.setLayoutParams(layoutParams);
		tv2.setLayoutParams(layoutParams);
		tv2.setTextSize(20);
		echo_list.setLayoutParams(layoutParams);
		
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(NavigationActivity.Mcontext,   android.R.layout.simple_spinner_item, fontSizeItems);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
		font_list.setAdapter(spinnerArrayAdapter);
		font_list.setSelection(TEXT_fontsize_add);
		
		ArrayAdapter<String> echo_listArrayAdapter = new ArrayAdapter<String>(NavigationActivity.Mcontext,   android.R.layout.simple_spinner_item, echoItems);
		echo_listArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
		echo_list.setAdapter(echo_listArrayAdapter);
		if(echoItems_check){
			echo_list.setSelection(1);	
		}else{
			echo_list.setSelection(0);
		}
		
		tv1.setText(" Font Size :");
		tv2.setText(" Echo     :");
		linearLayoutView1.setOrientation(LinearLayout.HORIZONTAL);
		linearLayoutView1.addView(tv1);
		linearLayoutView1.addView(font_list);
		linearLayoutView2.setOrientation(LinearLayout.HORIZONTAL);
		linearLayoutView2.addView(tv2);
		linearLayoutView2.addView(echo_list);
		
		LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		linearLayoutView.setLayoutParams(layoutParams1);
		linearLayoutView.setOrientation(LinearLayout.VERTICAL);
		linearLayoutView.addView(linearLayoutView1);
		linearLayoutView.addView(linearLayoutView2);

		AlertDialog.Builder alert = new AlertDialog.Builder(NavigationActivity.Mcontext);
		alert.setTitle("Setting");
	

		// inputrpc.setText("park");
		alert.setView(linearLayoutView);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
				TEXT_fontsize = Integer.parseInt(fontSizeItems[font_list.getSelectedItemPosition()]);
				Log.d(TAG, "park ble_terminal +Ok= "+TEXT_fontsize);
				
				
				if(echo_list.getSelectedItemPosition()==0){
					echoItems_check=false;
				}else{
					echoItems_check=true;
				}
				 // Create a new fragment and specify the planet to show based on position
				Fragment fragment = Fragment.instantiate(NavigationActivity.Mcontext, NavigationActivity.fragments[1]);
			    // Insert the fragment by replacing any existing fragment
			    FragmentManager fragmentManager = getFragmentManager();
			    fragmentManager.beginTransaction()
			                   .replace(R.id.content_frame, fragment)
			                   .commit();
			    
			    
				
			
			}
		});
		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						return;
					}
				});
		alert.show();
	}
	
	
	private final Handler mDisgetHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
			Log.d(TAG, "park ble_terminal += "+mble_terminal);
			mEmulatorView.setTextSize(TEXT_fontsize);
			editbox_Heigh=exlinearLayoutView.getHeight();
			title_heigh = mlinearloayout_title.getHeight();
			mKeyListener = new TermKeyListener();
			mEmulatorView.setFocusable(true);
			mEmulatorView.setFocusableInTouchMode(true);
			mEmulatorView.requestFocus();
			mEmulatorView.register(mKeyListener);
			mEmulatorView.mKnownSize = true;
			mEmulatorView.updateSize();
			
		}
	};
		
	  @Override
		public void onResume() {
	        super.onResume();
	       
	    }
	  @Override
		public void onStart() {
	        super.onStart();
	    }

	  @Override
		public void onPause() {
	        super.onPause();
	        NavigationActivity.mInputManager.hideSoftInputFromWindow(mEmulatorView.getWindowToken(), 0); //?��보드 ?��기기
	    }

	 @Override
	    public void onDestroy() {
	        super.onDestroy();
	      
	        try {
	        	mDisgetHandler.removeMessages(0);
			} catch (Exception e) {
			}
	        
	        
	    }

	public static void toggleKeyboard() {
		//NavigationActivity.mInputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	}
	//UART ?��?��?�� ?��?��
	public void dataReceived(String  uudi_data, String data_string,byte[] row_data){ 
		// TODO Auto-generated method stub
		if(SampleGattAttributes.UART_READ_UUID.equals(uudi_data)){
			byte[] mBuffer = new byte[row_data.length+2];
			System.arraycopy(row_data, 0, mBuffer, 0, row_data.length);
			mBuffer[row_data.length]=0x0D;
			mBuffer[row_data.length+1]=0x0A;
			mEmulatorView.write(mBuffer, mBuffer.length);
		}
		
	}
	//UART 
	public void datasend(byte[] out) { 
		setUART_Write(out);
		if(echoItems_check){
		byte[] mBuffer = new byte[out.length+1];
		System.arraycopy(out, 0, mBuffer, 0, out.length);
		mBuffer[out.length]=0x0D;
		mEmulatorView.write(mBuffer, mBuffer.length);
		}
	}

	
}

/**
 * An abstract screen interface. A terminal screen stores lines of text. (The
 * reason to abstract it is to allow different implementations, and to hide
 * implementation details from clients.)
 */
interface Screen {

	/**
	 * Set line wrap flag for a given row. Affects how lines are logically
	 * wrapped when changing screen size or converting to a transcript.
	 */
	void setLineWrap(int row);

	/**
	 * Store byte b into the screen at location (x, y)
	 * 
	 * @param x
	 *            X coordinate (also known as column)
	 * @param y
	 *            Y coordinate (also known as row)
	 * @param b
	 *            ASCII character to store
	 * @param foreColor
	 *            the foreground color
	 * @param backColor
	 *            the background color
	 */
	void set(int x, int y, byte b, int foreColor, int backColor);

	/**
	 * Scroll the screen down one line. To scroll the whole screen of a 24 line
	 * screen, the arguments would be (0, 24).
	 * 
	 * @param topMargin
	 *            First line that is scrolled.
	 * @param bottomMargin
	 *            One line after the last line that is scrolled.
	 */
	void scroll(int topMargin, int bottomMargin, int foreColor, int backColor);

	/**
	 * Block copy characters from one position in the screen to another. The two
	 * positions can overlap. All characters of the source and destination must
	 * be within the bounds of the screen, or else an InvalidParemeterException
	 * will be thrown.
	 * 
	 * @param sx
	 *            source X coordinate
	 * @param sy
	 *            source Y coordinate
	 * @param w
	 *            width
	 * @param h
	 *            height
	 * @param dx
	 *            destination X coordinate
	 * @param dy
	 *            destination Y coordinate
	 */
	void blockCopy(int sx, int sy, int w, int h, int dx, int dy);

	/**
	 * Block set characters. All characters must be within the bounds of the
	 * screen, or else and InvalidParemeterException will be thrown. Typically
	 * this is called with a "val" argument of 32 to clear a block of
	 * characters.
	 * 
	 * @param sx
	 *            source X
	 * @param sy
	 *            source Y
	 * @param w
	 *            width
	 * @param h
	 *            height
	 * @param val
	 *            value to set.
	 * @param foreColor
	 *            the foreground color
	 * @param backColor
	 *            the background color
	 */
	void blockSet(int sx, int sy, int w, int h, int val, int foreColor,
			int backColor);

	/**
	 * Get the contents of the transcript buffer as a text string.
	 * 
	 * @return the contents of the transcript buffer.
	 */
	String getTranscriptText();

	/**
	 * Resize the screen
	 * 
	 * @param columns
	 * @param rows
	 */
	void resize(int columns, int rows, int foreColor, int backColor);
}

/**
 * A TranscriptScreen is a screen that remembers data that's been scrolled. The
 * old data is stored in a ring buffer to minimize the amount of copying that
 * needs to be done. The transcript does its own drawing, to avoid having to
 * expose its internal data structures.
 */
class TranscriptScreen implements Screen {

	/**
	 * The width of the transcript, in characters. Fixed at initialization.
	 */
	private int mColumns;

	/**
	 * The total number of rows in the transcript and the screen. Fixed at
	 * initialization.
	 */
	private int mTotalRows;

	/**
	 * The number of rows in the active portion of the transcript. Doesn't
	 * include the screen.
	 */
	private int mActiveTranscriptRows;

	/**
	 * Which row is currently the topmost line of the transcript. Used to
	 * implement a circular buffer.
	 */
	private int mHead;

	/**
	 * The number of active rows, includes both the transcript and the screen.
	 */
	private int mActiveRows;

	/**
	 * The number of rows in the screen.
	 */
	private int mScreenRows;

	/**
	 * The data for both the screen and the transcript. The first mScreenRows *
	 * mLineWidth characters are the screen, the rest are the transcript. The
	 * low byte encodes the ASCII character, the high byte encodes the
	 * foreground and background colors, plus underline and bold.
	 */
	private char[] mData;

	/**
	 * The data's stored as color-encoded chars, but the drawing routines
	 * require chars, so we need a temporary buffer to hold a row's worth of
	 * characters.
	 */
	private char[] mRowBuffer;

	/**
	 * Flags that keep track of whether the current line logically wraps to the
	 * next line. This is used when resizing the screen and when copying to the
	 * clipboard or an email attachment
	 */

	private boolean[] mLineWrap;

	/**
	 * Create a transcript screen.
	 * 
	 * @param columns
	 *            the width of the screen in characters.
	 * @param totalRows
	 *            the height of the entire text area, in rows of text.
	 * @param screenRows
	 *            the height of just the screen, not including the transcript
	 *            that holds lines that have scrolled off the top of the screen.
	 */
	public TranscriptScreen(int columns, int totalRows, int screenRows,
			int foreColor, int backColor) {
		init(columns, totalRows, screenRows, foreColor, backColor);
	}

	private void init(int columns, int totalRows, int screenRows,
			int foreColor, int backColor) {
		mColumns = columns;
		mTotalRows = totalRows;
		mActiveTranscriptRows = 0;
		mHead = 0;
		mActiveRows = screenRows;
		mScreenRows = screenRows;
		int totalSize = columns * totalRows;
		mData = new char[totalSize];
		blockSet(0, 0, mColumns, mScreenRows, ' ', foreColor, backColor);
		mRowBuffer = new char[columns];
		mLineWrap = new boolean[totalRows];
		consistencyCheck();
	}

	/**
	 * Convert a row value from the public external coordinate system to our
	 * internal private coordinate system. External coordinate system:
	 * -mActiveTranscriptRows to mScreenRows-1, with the screen being
	 * 0..mScreenRows-1 Internal coordinate system: 0..mScreenRows-1 rows of
	 * mData are the visible rows. mScreenRows..mActiveRows - 1 are the
	 * transcript, stored as a circular buffer.
	 * 
	 * @param row
	 *            a row in the external coordinate system.
	 * @return The row corresponding to the input argument in the private
	 *         coordinate system.
	 */
	private int externalToInternalRow(int row) {
		if (row < -mActiveTranscriptRows || row >= mScreenRows) {
			throw new IllegalArgumentException();
		}
		if (row >= 0) {
			return row; // This is a visible row.
		}
		return mScreenRows
				+ ((mHead + mActiveTranscriptRows + row) % mActiveTranscriptRows);
	}

	private int getOffset(int externalLine) {
		return externalToInternalRow(externalLine) * mColumns;
	}

	private int getOffset(int x, int y) {
		return getOffset(y) + x;
	}

	public void setLineWrap(int row) {
		mLineWrap[externalToInternalRow(row)] = true;
	}

	/**
	 * Store byte b into the screen at location (x, y)
	 * 
	 * @param x
	 *            X coordinate (also known as column)
	 * @param y
	 *            Y coordinate (also known as row)
	 * @param b
	 *            ASCII character to store
	 * @param foreColor
	 *            the foreground color
	 * @param backColor
	 *            the background color
	 */
	public void set(int x, int y, byte b, int foreColor, int backColor) {
		mData[getOffset(x, y)] = encode(b, foreColor, backColor);
	}

	private char encode(int b, int foreColor, int backColor) {
		return (char) ((foreColor << 12) | (backColor << 8) | b);
	}

	/**
	 * Scroll the screen down one line. To scroll the whole screen of a 24 line
	 * screen, the arguments would be (0, 24).
	 * 
	 * @param topMargin
	 *            First line that is scrolled.
	 * @param bottomMargin
	 *            One line after the last line that is scrolled.
	 */
	public void scroll(int topMargin, int bottomMargin, int foreColor,
			int backColor) {
		if (topMargin > bottomMargin - 2 || topMargin > mScreenRows - 2
				|| bottomMargin > mScreenRows) {
			throw new IllegalArgumentException();
		}

		// Adjust the transcript so that the last line of the transcript
		// is ready to receive the newly scrolled data
		consistencyCheck();
		int expansionRows = Math.min(1, mTotalRows - mActiveRows);
		int rollRows = 1 - expansionRows;
		mActiveRows += expansionRows;
		mActiveTranscriptRows += expansionRows;
		if (mActiveTranscriptRows > 0) {
			mHead = (mHead + rollRows) % mActiveTranscriptRows;
		}
		consistencyCheck();

		// Block move the scroll line to the transcript
		int topOffset = getOffset(topMargin);
		int destOffset = getOffset(-1);
		System.arraycopy(mData, topOffset, mData, destOffset, mColumns);

		int topLine = externalToInternalRow(topMargin);
		int destLine = externalToInternalRow(-1);
		System.arraycopy(mLineWrap, topLine, mLineWrap, destLine, 1);

		// Block move the scrolled data up
		int numScrollChars = (bottomMargin - topMargin - 1) * mColumns;
		System.arraycopy(mData, topOffset + mColumns, mData, topOffset,
				numScrollChars);
		int numScrollLines = (bottomMargin - topMargin - 1);
		System.arraycopy(mLineWrap, topLine + 1, mLineWrap, topLine,
				numScrollLines);

		// Erase the bottom line of the scroll region
		blockSet(0, bottomMargin - 1, mColumns, 1, ' ', foreColor, backColor);
		mLineWrap[externalToInternalRow(bottomMargin - 1)] = false;
	}

	private void consistencyCheck() {
		checkPositive(mColumns);
		checkPositive(mTotalRows);
		checkRange(0, mActiveTranscriptRows, mTotalRows);
		if (mActiveTranscriptRows == 0) {
			checkEqual(mHead, 0);
		} else {
			checkRange(0, mHead, mActiveTranscriptRows - 1);
		}
		checkEqual(mScreenRows + mActiveTranscriptRows, mActiveRows);
		checkRange(0, mScreenRows, mTotalRows);

		checkEqual(mTotalRows, mLineWrap.length);
		checkEqual(mTotalRows * mColumns, mData.length);
		checkEqual(mColumns, mRowBuffer.length);
	}

	private void checkPositive(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("checkPositive " + n);
		}
	}

	private void checkRange(int a, int b, int c) {
		if (a > b || b > c) {
			throw new IllegalArgumentException("checkRange " + a + " <= " + b
					+ " <= " + c);
		}
	}

	private void checkEqual(int a, int b) {
		if (a != b) {
			throw new IllegalArgumentException("checkEqual " + a + " == " + b);
		}
	}

	/**
	 * Block copy characters from one position in the screen to another. The two
	 * positions can overlap. All characters of the source and destination must
	 * be within the bounds of the screen, or else an InvalidParemeterException
	 * will be thrown.
	 * 
	 * @param sx
	 *            source X coordinate
	 * @param sy
	 *            source Y coordinate
	 * @param w
	 *            width
	 * @param h
	 *            height
	 * @param dx
	 *            destination X coordinate
	 * @param dy
	 *            destination Y coordinate
	 */
	public void blockCopy(int sx, int sy, int w, int h, int dx, int dy) {
		if (sx < 0 || sx + w > mColumns || sy < 0 || sy + h > mScreenRows
				|| dx < 0 || dx + w > mColumns || dy < 0
				|| dy + h > mScreenRows) {
			throw new IllegalArgumentException();
		}
		if (sy <= dy) {
			// Move in increasing order
			for (int y = 0; y < h; y++) {
				int srcOffset = getOffset(sx, sy + y);
				int dstOffset = getOffset(dx, dy + y);
				System.arraycopy(mData, srcOffset, mData, dstOffset, w);
			}
		} else {
			// Move in decreasing order
			for (int y = 0; y < h; y++) {
				int y2 = h - (y + 1);
				int srcOffset = getOffset(sx, sy + y2);
				int dstOffset = getOffset(dx, dy + y2);
				System.arraycopy(mData, srcOffset, mData, dstOffset, w);
			}
		}
	}

	/**
	 * Block set characters. All characters must be within the bounds of the
	 * screen, or else and InvalidParemeterException will be thrown. Typically
	 * this is called with a "val" argument of 32 to clear a block of
	 * characters.
	 * 
	 * @param sx
	 *            source X
	 * @param sy
	 *            source Y
	 * @param w
	 *            width
	 * @param h
	 *            height
	 * @param val
	 *            value to set.
	 */
	public void blockSet(int sx, int sy, int w, int h, int val, int foreColor,
			int backColor) {
		if (sx < 0 || sx + w > mColumns || sy < 0 || sy + h > mScreenRows) {
			throw new IllegalArgumentException();
		}
		char[] data = mData;
		char encodedVal = encode(val, foreColor, backColor);
		for (int y = 0; y < h; y++) {
			int offset = getOffset(sx, sy + y);
			for (int x = 0; x < w; x++) {
				data[offset + x] = encodedVal;
			}
		}
	}

	/**
	 * Draw a row of text. Out-of-bounds rows are blank, not errors.
	 * 
	 * @param row
	 *            The row of text to draw.
	 * @param canvas
	 *            The canvas to draw to.
	 * @param x
	 *            The x coordinate origin of the drawing
	 * @param y
	 *            The y coordinate origin of the drawing
	 * @param renderer
	 *            The renderer to use to draw the text
	 * @param cx
	 *            the cursor X coordinate, -1 means don't draw it
	 */
	public final void drawText(int row, Canvas canvas, float x, float y,
			TextRenderer renderer, int cx) {

		// Out-of-bounds rows are blank.
		if (row < -mActiveTranscriptRows || row >= mScreenRows) {
			return;
		}

		// Copy the data from the byte array to a char array so they can
		// be drawn.

		int offset = getOffset(row);
		char[] rowBuffer = mRowBuffer;
		char[] data = mData;
		int columns = mColumns;
		int lastColors = 0;
		int lastRunStart = -1;
		final int CURSOR_MASK = 0x10000;
		for (int i = 0; i < columns; i++) {
			char c = data[offset + i];
			int colors = (char) (c & 0xff00);
			if (cx == i) {
				// Set cursor background color:
				colors |= CURSOR_MASK;
			}
			rowBuffer[i] = (char) (c & 0x00ff);
			if (colors != lastColors) {
				if (lastRunStart >= 0) {
					renderer.drawTextRun(canvas, x, y, lastRunStart, rowBuffer,
							lastRunStart, i - lastRunStart,
							(lastColors & CURSOR_MASK) != 0,
							0xf & (lastColors >> 12), 0xf & (lastColors >> 8));
				}
				lastColors = colors;
				lastRunStart = i;
			}
		}
		if (lastRunStart >= 0) {
			renderer.drawTextRun(canvas, x, y, lastRunStart, rowBuffer,
					lastRunStart, columns - lastRunStart,
					(lastColors & CURSOR_MASK) != 0, 0xf & (lastColors >> 12),
					0xf & (lastColors >> 8));
		}
	}

	/**
	 * Get the count of active rows.
	 * 
	 * @return the count of active rows.
	 */
	public int getActiveRows() {
		return mActiveRows;
	}

	/**
	 * Get the count of active transcript rows.
	 * 
	 * @return the count of active transcript rows.
	 */
	public int getActiveTranscriptRows() {
		return mActiveTranscriptRows;
	}

	public String getTranscriptText() {
		return internalGetTranscriptText(true);
	}

	private String internalGetTranscriptText(boolean stripColors) {
		StringBuilder builder = new StringBuilder();
		char[] rowBuffer = mRowBuffer;
		char[] data = mData;
		int columns = mColumns;

		for (int row = -mActiveTranscriptRows; row < mScreenRows; row++) {
			int offset = getOffset(row);
			int lastPrintingChar = -1;
			for (int column = 0; column < columns; column++) {
				char c = data[offset + column];
				if (stripColors) {
					c = (char) (c & 0xff);
				}
				if ((c & 0xff) != ' ') {
					lastPrintingChar = column;
				}
				rowBuffer[column] = c;
			}
			if (mLineWrap[externalToInternalRow(row)]) {
				builder.append(rowBuffer, 0, columns);
			} else {
				builder.append(rowBuffer, 0, lastPrintingChar + 1);
				builder.append('\n');// \n
			}
		}
		return builder.toString();
	}

	public void resize(int columns, int rows, int foreColor, int backColor) {
		init(columns, mTotalRows, rows, foreColor, backColor);
	}
}

/**
 * Renders text into a screen. Contains all the terminal-specific knowlege and
 * state. Emulates a subset of the X Window System xterm terminal, which in turn
 * is an emulator for a subset of the Digital Equipment Corporation vt100
 * terminal. Missing functionality: text attributes (bold, underline, reverse
 * video, color) alternate screen cursor key and keypad escape sequences.
 */

class TerminalEmulator {

	/**
	 * The cursor row. Numbered 0..mRows-1.
	 */
	private int mCursorRow;

	/**
	 * The cursor column. Numbered 0..mColumns-1.
	 */
	private int mCursorCol;

	/**
	 * The number of character rows in the terminal screen.
	 */
	private int mRows;

	/**
	 * The number of character columns in the terminal screen.
	 */
	private int mColumns;

	/**
	 * Stores the characters that appear on the screen of the emulated terminal.
	 */
	private Screen mScreen;

	/**
	 * Keeps track of the current argument of the current escape sequence.
	 * Ranges from 0 to MAX_ESCAPE_PARAMETERS-1. (Typically just 0 or 1.)
	 */
	private int mArgIndex;

	/**
	 * The number of parameter arguments. This name comes from the ANSI standard
	 * for terminal escape codes.
	 */
	private static final int MAX_ESCAPE_PARAMETERS = 16;

	/**
	 * Holds the arguments of the current escape sequence.
	 */
	private int[] mArgs = new int[MAX_ESCAPE_PARAMETERS];

	// Escape processing states:

	/**
	 * Escape processing state: Not currently in an escape sequence.
	 */
	private static final int ESC_NONE = 0;

	/**
	 * Escape processing state: Have seen an ESC character
	 */
	private static final int ESC = 1;

	/**
	 * Escape processing state: Have seen ESC POUND
	 */
	private static final int ESC_POUND = 2;

	/**
	 * Escape processing state: Have seen ESC and a character-set-select char
	 */
	private static final int ESC_SELECT_LEFT_PAREN = 3;

	/**
	 * Escape processing state: Have seen ESC and a character-set-select char
	 */
	private static final int ESC_SELECT_RIGHT_PAREN = 4;

	/**
	 * Escape processing state: ESC [
	 */
	private static final int ESC_LEFT_SQUARE_BRACKET = 5;

	/**
	 * Escape processing state: ESC [ ?
	 */
	private static final int ESC_LEFT_SQUARE_BRACKET_QUESTION_MARK = 6;

	/**
	 * True if the current escape sequence should continue, false if the current
	 * escape sequence should be terminated. Used when parsing a single
	 * character.
	 */
	private boolean mContinueSequence;

	/**
	 * The current state of the escape sequence state machine.
	 */
	private int mEscapeState;

	/**
	 * Saved state of the cursor row, Used to implement the save/restore cursor
	 * position escape sequences.
	 */
	private int mSavedCursorRow;

	/**
	 * Saved state of the cursor column, Used to implement the save/restore
	 * cursor position escape sequences.
	 */
	private int mSavedCursorCol;

	// DecSet booleans

	/**
	 * This mask indicates 132-column mode is set. (As opposed to 80-column
	 * mode.)
	 */
	private static final int K_132_COLUMN_MODE_MASK = 1 << 3;

	/**
	 * This mask indicates that origin mode is set. (Cursor addressing is
	 * relative to the absolute screen size, rather than the currently set top
	 * and bottom margins.)
	 */
	private static final int K_ORIGIN_MODE_MASK = 1 << 6;

	/**
	 * This mask indicates that wraparound mode is set. (As opposed to
	 * stop-at-right-column mode.)
	 */
	private static final int K_WRAPAROUND_MODE_MASK = 1 << 7;

	/**
	 * Holds multiple DECSET flags. The data is stored this way, rather than in
	 * separate booleans, to make it easier to implement the save-and-restore
	 * semantics. The various k*ModeMask masks can be used to extract and modify
	 * the individual flags current states.
	 */
	private int mDecFlags;

	/**
	 * Saves away a snapshot of the DECSET flags. Used to implement save and
	 * restore escape sequences.
	 */
	private int mSavedDecFlags;

	// Modes set with Set Mode / Reset Mode

	/**
	 * True if insert mode (as opposed to replace mode) is active. In insert
	 * mode new characters are inserted, pushing existing text to the right.
	 */
	private boolean mInsertMode;

	/**
	 * Automatic newline mode. Configures whether pressing return on the
	 * keyboard automatically generates a return as well. Not currently
	 * implemented.
	 */
	private boolean mAutomaticNewlineMode;

	/**
	 * An array of tab stops. mTabStop[i] is true if there is a tab stop set for
	 * column i.
	 */
	private boolean[] mTabStop;

	// The margins allow portions of the screen to be locked.

	/**
	 * The top margin of the screen, for scrolling purposes. Ranges from 0 to
	 * mRows-2.
	 */
	private int mTopMargin;

	/**
	 * The bottom margin of the screen, for scrolling purposes. Ranges from
	 * mTopMargin + 2 to mRows. (Defines the first row after the scrolling
	 * region.
	 */
	private int mBottomMargin;

	/**
	 * True if the next character to be emitted will be automatically wrapped to
	 * the next line. Used to disambiguate the case where the cursor is
	 * positioned on column mColumns-1.
	 */
	private boolean mAboutToAutoWrap;

	/**
	 * Used for debugging, counts how many chars have been processed.
	 */
	private int mProcessedCharCount;

	/**
	 * Foreground color, 0..7, mask with 8 for bold
	 */
	private int mForeColor;

	/**
	 * Background color, 0..7, mask with 8 for underline
	 */
	private int mBackColor;

	private boolean mInverseColors;

	private boolean mbKeypadApplicationMode;

	private boolean mAlternateCharSet;

	/**
	 * Construct a terminal emulator that uses the supplied screen
	 * 
	 * @param screen
	 *            the screen to render characters into.
	 * @param columns
	 *            the number of columns to emulate
	 * @param rows
	 *            the number of rows to emulate
	 * @param termOut
	 *            the output file descriptor that talks to the pseudo-tty.
	 */
	// public TerminalEmulator(Screen screen, int columns, int rows,
	// FileOutputStream termOut) {
	public TerminalEmulator(Screen screen, int columns, int rows) {
		mScreen = screen;
		mRows = rows;
		mColumns = columns;
		mTabStop = new boolean[mColumns];
		// mTermOut = termOut;
		reset();
	}

	public void updateSize(int columns, int rows) {

		if (mRows == rows && mColumns == columns) {
			return;
		}

		String transcriptText = mScreen.getTranscriptText();

		mScreen.resize(columns, rows, mForeColor, mBackColor);

		if (mRows != rows) {
			mRows = rows;
			mTopMargin = 0;
			mBottomMargin = mRows;
		}

		if (mColumns != columns) {
			int oldColumns = mColumns;
			mColumns = columns;
			boolean[] oldTabStop = mTabStop;
			mTabStop = new boolean[mColumns];
			int toTransfer = Math.min(oldColumns, columns);
			System.arraycopy(oldTabStop, 0, mTabStop, 0, toTransfer);
			while (mCursorCol >= columns) {
				mCursorCol -= columns;
				mCursorRow = Math.min(mBottomMargin - 1, mCursorRow + 1);
			}
		}

		mCursorRow = 0;
		mCursorCol = 0;
		mAboutToAutoWrap = false;

		int end = transcriptText.length() - 1;

		if ((end >= 0) && transcriptText.charAt(end) == '\n') {// \n
			end--;
		}

		for (int i = 0; i <= end; i++) {
			byte c = (byte) transcriptText.charAt(i);

			if (c == '\n') { // \n
				setCursorCol(0);
				doLinefeed();
			} else {

				emit(c);
			}
		}

	}

	/**
	 * Get the cursor's current row.
	 * 
	 * @return the cursor's current row.
	 */
	public final int getCursorRow() {
		return mCursorRow;
	}

	/**
	 * Get the cursor's current column.
	 * 
	 * @return the cursor's current column.
	 */
	public final int getCursorCol() {
		return mCursorCol;
	}

	public final boolean getKeypadApplicationMode() {
		return mbKeypadApplicationMode;
	}

	private void setDefaultTabStops() {
		for (int i = 0; i < mColumns; i++) {
			mTabStop[i] = (i & 7) == 0 && i != 0;
		}
	}

	/**
	 * Accept bytes (typically from the pseudo-teletype) and process them.
	 * 
	 * @param buffer
	 *            a byte array containing the bytes to be processed
	 * @param base
	 *            the first index of the array to process
	 * @param length
	 *            the number of bytes in the array to process
	 */
	public void append(byte[] buffer, int base, int length) {
		for (int i = 0; i < length; i++) {
			byte b = buffer[base + i];
			try {
				if (ble_terminal.LOG_CHARACTERS_FLAG) {
					char printableB = (char) b;
					if (b < 32 || b > 126) {
						printableB = ' ';
					}
					
				}
				process(b);
				mProcessedCharCount++;
			} catch (Exception e) {
				
			}
		}
	}

	private void process(byte b) {
		switch (b) {
		case 0: // NUL
			// Do nothing
			break;

		case 7: // BEL
			// Do nothing
			break;

		case 8: // BS
			setCursorCol(Math.max(0, mCursorCol - 1));
			break;

		case 9: // HT
			// Move to next tab stop, but not past edge of screen
			setCursorCol(nextTabStop(mCursorCol));
			break;

		case 13:
			setCursorCol(0);
			break;

		case 10: // CR
		case 11: // VT
		case 12: // LF
			doLinefeed();
			break;

		case 14: // SO:
			setAltCharSet(true);
			break;

		case 15: // SI:
			setAltCharSet(false);
			break;

		case 24: // CAN
		case 26: // SUB
			if (mEscapeState != ESC_NONE) {
				mEscapeState = ESC_NONE;
				emit((byte) 127);
			}
			break;

		case 27: // ESC
			// Always starts an escape sequence
			startEscapeSequence(ESC);
			break;

		case (byte) 0x9b: // CSI
			startEscapeSequence(ESC_LEFT_SQUARE_BRACKET);
			break;

		default:
			mContinueSequence = false;
			switch (mEscapeState) {
			case ESC_NONE:
				if (b >= 32) {
					emit(b);
				}
				break;

			case ESC:
				doEsc(b);
				break;

			case ESC_POUND:
				doEscPound(b);
				break;

			case ESC_SELECT_LEFT_PAREN:
				doEscSelectLeftParen(b);
				break;

			case ESC_SELECT_RIGHT_PAREN:
				doEscSelectRightParen(b);
				break;

			case ESC_LEFT_SQUARE_BRACKET:
				doEscLeftSquareBracket(b);
				break;

			case ESC_LEFT_SQUARE_BRACKET_QUESTION_MARK:
				doEscLSBQuest(b);
				break;

			default:
				unknownSequence(b);
				break;
			}
			if (!mContinueSequence) {
				mEscapeState = ESC_NONE;
			}
			break;
		}
	}

	private void setAltCharSet(boolean alternateCharSet) {
		mAlternateCharSet = alternateCharSet;
	}

	private int nextTabStop(int cursorCol) {
		for (int i = cursorCol; i < mColumns; i++) {
			if (mTabStop[i]) {
				return i;
			}
		}
		return mColumns - 1;
	}

	private void doEscLSBQuest(byte b) {
		int mask = getDecFlagsMask(getArg0(0));
		switch (b) {
		case 'h': // Esc [ ? Pn h - DECSET
			mDecFlags |= mask;
			break;

		case 'l': // Esc [ ? Pn l - DECRST
			mDecFlags &= ~mask;
			break;

		case 'r': // Esc [ ? Pn r - restore
			mDecFlags = (mDecFlags & ~mask) | (mSavedDecFlags & mask);
			break;

		case 's': // Esc [ ? Pn s - save
			mSavedDecFlags = (mSavedDecFlags & ~mask) | (mDecFlags & mask);
			break;

		default:
			parseArg(b);
			break;
		}

		// 132 column mode
		if ((mask & K_132_COLUMN_MODE_MASK) != 0) {
			// We don't actually set 132 cols, but we do want the
			// side effect of clearing the screen and homing the cursor.
			blockClear(0, 0, mColumns, mRows);
			setCursorRowCol(0, 0);
		}

		// origin mode
		if ((mask & K_ORIGIN_MODE_MASK) != 0) {
			// Home the cursor.
			setCursorPosition(0, 0);
		}
	}

	private int getDecFlagsMask(int argument) {
		if (argument >= 1 && argument <= 9) {
			return (1 << argument);
		}

		return 0;
	}

	private void startEscapeSequence(int escapeState) {
		mEscapeState = escapeState;
		mArgIndex = 0;
		for (int j = 0; j < MAX_ESCAPE_PARAMETERS; j++) {
			mArgs[j] = -1;
		}
	}

	private void doLinefeed() {
		int newCursorRow = mCursorRow + 1;
		if (newCursorRow >= mBottomMargin) {
			scroll();
			newCursorRow = mBottomMargin - 1;
		}
		setCursorRow(newCursorRow);
	}

	private void continueSequence() {
		mContinueSequence = true;
	}

	private void continueSequence(int state) {
		mEscapeState = state;
		mContinueSequence = true;
	}

	private void doEscSelectLeftParen(byte b) {
		doSelectCharSet(true, b);
	}

	private void doEscSelectRightParen(byte b) {
		doSelectCharSet(false, b);
	}

	private void doSelectCharSet(boolean isG0CharSet, byte b) {
		switch (b) {
		case 'A': // United Kingdom character set
			break;
		case 'B': // ASCII set
			break;
		case '0': // Special Graphics
			break;
		case '1': // Alternate character set
			break;
		case '2':
			break;
		default:
			unknownSequence(b);
		}
	}

	private void doEscPound(byte b) {
		switch (b) {
		case '8': // Esc # 8 - DECALN alignment test
			mScreen.blockSet(0, 0, mColumns, mRows, 'E', getForeColor(),
					getBackColor());
			break;

		default:
			unknownSequence(b);
			break;
		}
	}

	private void doEsc(byte b) {
		switch (b) {
		case '#':
			continueSequence(ESC_POUND);
			break;

		case '(':
			continueSequence(ESC_SELECT_LEFT_PAREN);
			break;

		case ')':
			continueSequence(ESC_SELECT_RIGHT_PAREN);
			break;

		case '7': // DECSC save cursor
			mSavedCursorRow = mCursorRow;
			mSavedCursorCol = mCursorCol;
			break;

		case '8': // DECRC restore cursor
			setCursorRowCol(mSavedCursorRow, mSavedCursorCol);
			break;

		case 'D': // INDEX
			doLinefeed();
			break;

		case 'E': // NEL
			setCursorCol(0);
			doLinefeed();
			break;

		case 'F': // Cursor to lower-left corner of screen
			setCursorRowCol(0, mBottomMargin - 1);
			break;

		case 'H': // Tab set
			mTabStop[mCursorCol] = true;
			break;

		case 'M': // Reverse index
			if (mCursorRow == 0) {
				mScreen.blockCopy(0, mTopMargin + 1, mColumns, mBottomMargin
						- (mTopMargin + 1), 0, mTopMargin);
				blockClear(0, mBottomMargin - 1, mColumns);
			} else {
				mCursorRow--;
			}

			break;

		case 'N': // SS2
			unimplementedSequence(b);
			break;

		case '0': // SS3
			unimplementedSequence(b);
			break;

		case 'P': // Device control string
			unimplementedSequence(b);
			break;

		case 'Z': // return terminal ID
			sendDeviceAttributes();
			break;

		case '[':
			continueSequence(ESC_LEFT_SQUARE_BRACKET);
			break;

		case '=': // DECKPAM
			mbKeypadApplicationMode = true;
			break;

		case '>': // DECKPNM
			mbKeypadApplicationMode = false;
			break;

		default:
			unknownSequence(b);
			break;
		}
	}

	private void doEscLeftSquareBracket(byte b) {
		switch (b) {
		case '@': // ESC [ Pn @ - ICH Insert Characters
		{
			int charsAfterCursor = mColumns - mCursorCol;
			int charsToInsert = Math.min(getArg0(1), charsAfterCursor);
			int charsToMove = charsAfterCursor - charsToInsert;
			mScreen.blockCopy(mCursorCol, mCursorRow, charsToMove, 1,
					mCursorCol + charsToInsert, mCursorRow);
			blockClear(mCursorCol, mCursorRow, charsToInsert);
		}
			break;

		case 'A': // ESC [ Pn A - Cursor Up
			setCursorRow(Math.max(mTopMargin, mCursorRow - getArg0(1)));
			break;

		case 'B': // ESC [ Pn B - Cursor Down
			setCursorRow(Math.min(mBottomMargin - 1, mCursorRow + getArg0(1)));
			break;

		case 'C': // ESC [ Pn C - Cursor Right
			setCursorCol(Math.min(mColumns - 1, mCursorCol + getArg0(1)));
			break;

		case 'D': // ESC [ Pn D - Cursor Left
			setCursorCol(Math.max(0, mCursorCol - getArg0(1)));
			break;

		case 'G': // ESC [ Pn G - Cursor Horizontal Absolute
			setCursorCol(Math.min(Math.max(1, getArg0(1)), mColumns) - 1);
			break;

		case 'H': // ESC [ Pn ; H - Cursor Position
			setHorizontalVerticalPosition();
			break;

		case 'J': // ESC [ Pn J - Erase in Display
			switch (getArg0(0)) {
			case 0: // Clear below
				blockClear(mCursorCol, mCursorRow, mColumns - mCursorCol);
				blockClear(0, mCursorRow + 1, mColumns, mBottomMargin
						- (mCursorRow + 1));
				break;

			case 1: // Erase from the start of the screen to the cursor.
				blockClear(0, mTopMargin, mColumns, mCursorRow - mTopMargin);
				blockClear(0, mCursorRow, mCursorCol + 1);
				break;

			case 2: // Clear all
				blockClear(0, mTopMargin, mColumns, mBottomMargin - mTopMargin);
				break;

			default:
				unknownSequence(b);
				break;
			}
			break;

		case 'K': // ESC [ Pn K - Erase in Line
			switch (getArg0(0)) {
			case 0: // Clear to right
				blockClear(mCursorCol, mCursorRow, mColumns - mCursorCol);
				break;

			case 1: // Erase start of line to cursor (including cursor)
				blockClear(0, mCursorRow, mCursorCol + 1);
				break;

			case 2: // Clear whole line
				blockClear(0, mCursorRow, mColumns);
				break;

			default:
				unknownSequence(b);
				break;
			}
			break;

		case 'L': // Insert Lines
		{
			int linesAfterCursor = mBottomMargin - mCursorRow;
			int linesToInsert = Math.min(getArg0(1), linesAfterCursor);
			int linesToMove = linesAfterCursor - linesToInsert;
			mScreen.blockCopy(0, mCursorRow, mColumns, linesToMove, 0,
					mCursorRow + linesToInsert);
			blockClear(0, mCursorRow, mColumns, linesToInsert);
		}
			break;

		case 'M': // Delete Lines
		{
			int linesAfterCursor = mBottomMargin - mCursorRow;
			int linesToDelete = Math.min(getArg0(1), linesAfterCursor);
			int linesToMove = linesAfterCursor - linesToDelete;
			mScreen.blockCopy(0, mCursorRow + linesToDelete, mColumns,
					linesToMove, 0, mCursorRow);
			blockClear(0, mCursorRow + linesToMove, mColumns, linesToDelete);
		}
			break;

		case 'P': // Delete Characters
		{
			int charsAfterCursor = mColumns - mCursorCol;
			int charsToDelete = Math.min(getArg0(1), charsAfterCursor);
			int charsToMove = charsAfterCursor - charsToDelete;
			mScreen.blockCopy(mCursorCol + charsToDelete, mCursorRow,
					charsToMove, 1, mCursorCol, mCursorRow);
			blockClear(mCursorCol + charsToMove, mCursorRow, charsToDelete);
		}
			break;

		case 'T': // Mouse tracking
			unimplementedSequence(b);
			break;

		case '?': // Esc [ ? -- start of a private mode set
			continueSequence(ESC_LEFT_SQUARE_BRACKET_QUESTION_MARK);
			break;

		case 'c': // Send device attributes
			sendDeviceAttributes();
			break;

		case 'd': // ESC [ Pn d - Vert Position Absolute
			setCursorRow(Math.min(Math.max(1, getArg0(1)), mRows) - 1);
			break;

		case 'f': // Horizontal and Vertical Position
			setHorizontalVerticalPosition();
			break;

		case 'g': // Clear tab stop
			switch (getArg0(0)) {
			case 0:
				mTabStop[mCursorCol] = false;
				break;

			case 3:
				for (int i = 0; i < mColumns; i++) {
					mTabStop[i] = false;
				}
				break;

			default:
				// Specified to have no effect.
				break;
			}
			break;

		case 'h': // Set Mode
			doSetMode(true);
			break;

		case 'l': // Reset Mode
			doSetMode(false);
			break;

		case 'm': // Esc [ Pn m - character attributes.
			selectGraphicRendition();
			break;

		case 'r': // Esc [ Pn ; Pn r - set top and bottom margins
		{
			// The top margin defaults to 1, the bottom margin
			// (unusually for arguments) defaults to mRows.
			//
			// The escape sequence numbers top 1..23, but we
			// number top 0..22.
			// The escape sequence numbers bottom 2..24, and
			// so do we (because we use a zero based numbering
			// scheme, but we store the first line below the
			// bottom-most scrolling line.
			// As a result, we adjust the top line by -1, but
			// we leave the bottom line alone.
			//
			// Also require that top + 2 <= bottom

			int top = Math.max(0, Math.min(getArg0(1) - 1, mRows - 2));
			int bottom = Math.max(top + 2, Math.min(getArg1(mRows), mRows));
			mTopMargin = top;
			mBottomMargin = bottom;

			// The cursor is placed in the home position
			setCursorRowCol(mTopMargin, 0);
		}
			break;

		default:
			parseArg(b);
			break;
		}
	}

	private void selectGraphicRendition() {
		for (int i = 0; i <= mArgIndex; i++) {
			int code = mArgs[i];
			if (code < 0) {
				if (mArgIndex > 0) {
					continue;
				} else {
					code = 0;
				}
			}
			if (code == 0) { // reset
				mInverseColors = false;
				mForeColor = 7;
				mBackColor = 0;
			} else if (code == 1) { // bold
				mForeColor |= 0x8;
			} else if (code == 4) { // underscore
				mBackColor |= 0x8;
			} else if (code == 7) { // inverse
				mInverseColors = true;
			} else if (code >= 30 && code <= 37) { // foreground color
				mForeColor = (mForeColor & 0x8) | (code - 30);
			} else if (code >= 40 && code <= 47) { // background color
				mBackColor = (mBackColor & 0x8) | (code - 40);
			} else {
				if (ble_terminal.LOG_UNKNOWN_ESCAPE_SEQUENCES) {
					Log.w(ble_terminal.LOG_TAG,String.format("SGR unknown code %d", code));
				}
			}
		}
	}

	private void blockClear(int sx, int sy, int w) {
		blockClear(sx, sy, w, 1);
	}

	private void blockClear(int sx, int sy, int w, int h) {
		mScreen.blockSet(sx, sy, w, h, ' ', getForeColor(), getBackColor());
	}

	private int getForeColor() {
		return mInverseColors ? ((mBackColor & 0x7) | (mForeColor & 0x8))
				: mForeColor;
	}

	private int getBackColor() {
		return mInverseColors ? ((mForeColor & 0x7) | (mBackColor & 0x8))
				: mBackColor;
	}

	private void doSetMode(boolean newValue) {
		int modeBit = getArg0(0);
		switch (modeBit) {
		case 4:
			mInsertMode = newValue;
			break;

		case 20:
			mAutomaticNewlineMode = newValue;
			break;

		default:
			unknownParameter(modeBit);
			break;
		}
	}

	private void setHorizontalVerticalPosition() {

		// Parameters are Row ; Column

		setCursorPosition(getArg1(1) - 1, getArg0(1) - 1);
	}

	private void setCursorPosition(int x, int y) {
		int effectiveTopMargin = 0;
		int effectiveBottomMargin = mRows;
		if ((mDecFlags & K_ORIGIN_MODE_MASK) != 0) {
			effectiveTopMargin = mTopMargin;
			effectiveBottomMargin = mBottomMargin;
		}
		int newRow = Math.max(effectiveTopMargin,
				Math.min(effectiveTopMargin + y, effectiveBottomMargin - 1));
		int newCol = Math.max(0, Math.min(x, mColumns - 1));
		setCursorRowCol(newRow, newCol);
	}

	private void sendDeviceAttributes() {
		// This identifies us as a DEC vt100 with advanced
		// video options. This is what the xterm terminal
		// emulator sends.
		byte[] attributes = {
		/* VT100 */
		(byte) 27, (byte) '[', (byte) '?', (byte) '1', (byte) ';', (byte) '2',
				(byte) 'c'

		/*
		 * VT220 (byte) 27, (byte) '[', (byte) '?', (byte) '6', (byte) '0',
		 * (byte) ';', (byte) '1', (byte) ';', (byte) '2', (byte) ';', (byte)
		 * '6', (byte) ';', (byte) '8', (byte) ';', (byte) '9', (byte) ';',
		 * (byte) '1', (byte) '5', (byte) ';', (byte) 'c'
		 */
		};

		write(attributes);
	}

	/**
	 * Send data to the shell process
	 * 
	 * @param data
	 */
	private void write(byte[] data) {
		/*
		 * try { mTermOut.write(data); mTermOut.flush(); } catch (IOException e)
		 * { // Ignore exception // We don't really care if the receiver isn't
		 * listening. // We just make a best effort to answer the query. }
		 */
	}

	private void scroll() {
		mScreen.scroll(mTopMargin, mBottomMargin, getForeColor(),
				getBackColor());
	}

	/**
	 * Process the next ASCII character of a parameter.
	 * 
	 * @param b
	 *            The next ASCII character of the paramater sequence.
	 */
	private void parseArg(byte b) {
		if (b >= '0' && b <= '9') {
			if (mArgIndex < mArgs.length) {
				int oldValue = mArgs[mArgIndex];
				int thisDigit = b - '0';
				int value;
				if (oldValue >= 0) {
					value = oldValue * 10 + thisDigit;
				} else {
					value = thisDigit;
				}
				mArgs[mArgIndex] = value;
			}
			continueSequence();
		} else if (b == ';') {
			if (mArgIndex < mArgs.length) {
				mArgIndex++;
			}
			continueSequence();
		} else {
			unknownSequence(b);
		}
	}

	private int getArg0(int defaultValue) {
		return getArg(0, defaultValue);
	}

	private int getArg1(int defaultValue) {
		return getArg(1, defaultValue);
	}

	private int getArg(int index, int defaultValue) {
		int result = mArgs[index];
		if (result < 0) {
			result = defaultValue;
		}
		return result;
	}

	private void unimplementedSequence(byte b) {
		if (ble_terminal.LOG_UNKNOWN_ESCAPE_SEQUENCES) {
			logError("unimplemented", b);
		}
		finishSequence();
	}

	private void unknownSequence(byte b) {
		if (ble_terminal.LOG_UNKNOWN_ESCAPE_SEQUENCES) {
			logError("unknown", b);
		}
		finishSequence();
	}

	private void unknownParameter(int parameter) {
		if (ble_terminal.LOG_UNKNOWN_ESCAPE_SEQUENCES) {
			StringBuilder buf = new StringBuilder();
			buf.append("Unknown parameter");
			buf.append(parameter);
			logError(buf.toString());
		}
	}

	private void logError(String errorType, byte b) {
		if (ble_terminal.LOG_UNKNOWN_ESCAPE_SEQUENCES) {
			StringBuilder buf = new StringBuilder();
			buf.append(errorType);
			buf.append(" sequence ");
			buf.append(" EscapeState: ");
			buf.append(mEscapeState);
			buf.append(" char: '");
			buf.append((char) b);
			buf.append("' (");
			buf.append(b);
			buf.append(")");
			boolean firstArg = true;
			for (int i = 0; i <= mArgIndex; i++) {
				int value = mArgs[i];
				if (value >= 0) {
					if (firstArg) {
						firstArg = false;
						buf.append("args = ");
					}
					buf.append(String.format("%d; ", value));
				}
			}
			logError(buf.toString());
		}
	}

	private void logError(String error) {
		if (ble_terminal.LOG_UNKNOWN_ESCAPE_SEQUENCES) {
			Log.e(ble_terminal.LOG_TAG, error);
		}
		finishSequence();
	}

	private void finishSequence() {
		mEscapeState = ESC_NONE;
	}

	private boolean autoWrapEnabled() {
		// Always enable auto wrap, because it's useful on a small screen
		return true;
		// return (mDecFlags & K_WRAPAROUND_MODE_MASK) != 0;
	}

	/**
	 * Send an ASCII character to the screen.
	 * 
	 * @param b
	 *            the ASCII character to display.
	 */
	private void emit(byte b) {
		boolean autoWrap = autoWrapEnabled();

		if (autoWrap) {
			if (mCursorCol == mColumns - 1 && mAboutToAutoWrap) {
				mScreen.setLineWrap(mCursorRow);
				mCursorCol = 0;
				if (mCursorRow + 1 < mBottomMargin) {
					mCursorRow++;
				} else {
					scroll();
				}
			}
		}

		if (mInsertMode) { // Move character to right one space
			int destCol = mCursorCol + 1;
			if (destCol < mColumns) {
				mScreen.blockCopy(mCursorCol, mCursorRow, mColumns - destCol,
						1, destCol, mCursorRow);
			}
		}

		mScreen.set(mCursorCol, mCursorRow, b, getForeColor(), getBackColor());

		if (autoWrap) {
			mAboutToAutoWrap = (mCursorCol == mColumns - 1);
		}

		mCursorCol = Math.min(mCursorCol + 1, mColumns - 1);
	}

	private void setCursorRow(int row) {
		mCursorRow = row;
		mAboutToAutoWrap = false;
	}

	private void setCursorCol(int col) {
		mCursorCol = col;
		mAboutToAutoWrap = false;
	}

	private void setCursorRowCol(int row, int col) {
		mCursorRow = Math.min(row, mRows - 1);
		mCursorCol = Math.min(col, mColumns - 1);
		mAboutToAutoWrap = false;
	}

	/**
	 * Reset the terminal emulator to its initial state.
	 */
	public void reset() {
		mCursorRow = 0;
		mCursorCol = 0;
		mArgIndex = 0;
		mContinueSequence = false;
		mEscapeState = ESC_NONE;
		mSavedCursorRow = 0;
		mSavedCursorCol = 0;
		mDecFlags = 0;
		mSavedDecFlags = 0;
		mInsertMode = false;
		mAutomaticNewlineMode = false;
		mTopMargin = 0;
		mBottomMargin = mRows;
		mAboutToAutoWrap = false;
		mForeColor = 7;
		mBackColor = 0;
		mInverseColors = false;
		mbKeypadApplicationMode = false;
		mAlternateCharSet = false;
		// mProcessedCharCount is preserved unchanged.
		setDefaultTabStops();
		blockClear(0, 0, mColumns, mRows);
	}

	public void reset1() {
		/*
		 * mCursorRow = 0; mCursorCol = 0; mArgIndex = 0; mContinueSequence =
		 * false; mEscapeState = ESC_NONE; mSavedCursorRow = 0; mSavedCursorCol
		 * = 0; mDecFlags = 0; mSavedDecFlags = 0; mInsertMode = false;
		 * mAutomaticNewlineMode = false; mTopMargin = 0; mBottomMargin = mRows;
		 * mAboutToAutoWrap = false; mForeColor = 7; mBackColor = 0;
		 * mInverseColors = false; mbKeypadApplicationMode = false;
		 * mAlternateCharSet = false; // mProcessedCharCount is preserved
		 * unchanged. // setDefaultTabStops();
		 */

		// blockClear(0, mTopMargin, mColumns, mBottomMargin - mTopMargin);
		// setCursorRowCol(0, mRows);//?�ㅼ�???��?��?�� ?���???��?��?��

		blockClear(0, 0, mColumns, mRows);

		// blockClear(0, 1, mColumns, 20);

	}

	public String getTranscriptText() {
		return mScreen.getTranscriptText();
	}
}

/**
 * Text renderer interface
 */

interface TextRenderer {
	int getCharacterWidth();

	int getCharacterHeight();

	void drawTextRun(Canvas canvas, float x, float y, int lineOffset,
			char[] text, int index, int count, boolean cursor, int foreColor,
			int backColor);
}

abstract class BaseTextRenderer implements TextRenderer {
	protected int[] mForePaint = { 0xff000000, // Black
			0xffff0000, // Red
			0xff00ff00, // green
			0xffffff00, // yellow
			0xff0000ff, // blue
			0xffff00ff, // magenta
			0xff00ffff, // cyan
			0xffffffff // white -- is overridden by constructor
	};
	protected int[] mBackPaint = { 0xff000000, // Black -- is overridden by
												// constructor
			0xffcc0000, // Red
			0xff00cc00, // green
			0xffcccc00, // yellow
			0xff0000cc, // blue
			0xffff00cc, // magenta
			0xff00cccc, // cyan
			0xffffffff // white
	};
	protected final static int mCursorPaint = 0xff808080;

	public BaseTextRenderer(int forePaintColor, int backPaintColor) {
		mForePaint[7] = forePaintColor;
		mBackPaint[0] = backPaintColor;

	}
}

class PaintRenderer extends BaseTextRenderer {
	public PaintRenderer(int fontSize, int forePaintColor, int backPaintColor) {
		super(forePaintColor, backPaintColor);
		mTextPaint = new Paint();
		mTextPaint.setTypeface(Typeface.MONOSPACE);
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTextSize(fontSize);

		mCharHeight = (int) Math.ceil(mTextPaint.getFontSpacing());
		mCharAscent = (int) Math.ceil(mTextPaint.ascent());
		mCharDescent = mCharHeight + mCharAscent;
		mCharWidth = (int) mTextPaint.measureText(EXAMPLE_CHAR, 0, 1);
	}

	public void drawTextRun(Canvas canvas, float x, float y, int lineOffset,
			char[] text, int index, int count, boolean cursor, int foreColor,
			int backColor) {
		if (cursor) {
			mTextPaint.setColor(mCursorPaint);
		} else {
			mTextPaint.setColor(mBackPaint[backColor & 0x7]);
		}
		float left = x + lineOffset * mCharWidth;
		canvas.drawRect(left, y + mCharAscent, left + count * mCharWidth, y
				+ mCharDescent, mTextPaint);
		boolean bold = (foreColor & 0x8) != 0;
		boolean underline = (backColor & 0x8) != 0;
		if (bold) {
			mTextPaint.setFakeBoldText(true);
		}
		if (underline) {
			mTextPaint.setUnderlineText(true);
		}
		mTextPaint.setColor(mForePaint[foreColor & 0x7]);
		canvas.drawText(text, index, count, left, y, mTextPaint);
		if (bold) {
			mTextPaint.setFakeBoldText(false);
		}
		if (underline) {
			mTextPaint.setUnderlineText(false);
		}
	}

	public int getCharacterHeight() {
		return mCharHeight;
	}

	public int getCharacterWidth() {
		return mCharWidth;
	}

	private Paint mTextPaint;
	private int mCharWidth;
	private int mCharHeight;
	private int mCharAscent;
	private int mCharDescent;
	private static final char[] EXAMPLE_CHAR = { 'X' };
}

/**
 * A multi-thread-safe produce-consumer byte array. Only allows one producer and
 * one consumer.
 */

class ByteQueue {
	public ByteQueue(int size) {
		mBuffer = new byte[size];
	}

	public int getBytesAvailable() {
		synchronized (this) {
			return mStoredBytes;
		}
	}

	public int read(byte[] buffer, int offset, int length)
			throws InterruptedException {
		if (length + offset > buffer.length) {
			throw new IllegalArgumentException(
					"length + offset > buffer.length");
		}
		if (length < 0) {
			throw new IllegalArgumentException("length < 0");

		}
		if (length == 0) {
			return 0;
		}
		synchronized (this) {
			while (mStoredBytes == 0) {
				wait();
			}
			int totalRead = 0;
			int bufferLength = mBuffer.length;
			boolean wasFull = bufferLength == mStoredBytes;
			while (length > 0 && mStoredBytes > 0) {
				int oneRun = Math.min(bufferLength - mHead, mStoredBytes);
				int bytesToCopy = Math.min(length, oneRun);
				System.arraycopy(mBuffer, mHead, buffer, offset, bytesToCopy);
				mHead += bytesToCopy;
				if (mHead >= bufferLength) {
					mHead = 0;
				}
				mStoredBytes -= bytesToCopy;
				length -= bytesToCopy;
				offset += bytesToCopy;
				totalRead += bytesToCopy;
			}
			if (wasFull) {
				notify();
			}
			return totalRead;
		}
	}

	public void write(byte[] buffer, int offset, int length)
			throws InterruptedException {
		if (length + offset > buffer.length) {
			throw new IllegalArgumentException(
					"length + offset > buffer.length");
		}
		if (length < 0) {
			throw new IllegalArgumentException("length < 0");

		}
		if (length == 0) {
			return;
		}
		synchronized (this) {
			int bufferLength = mBuffer.length;
			boolean wasEmpty = mStoredBytes == 0;
			while (length > 0) {
				while (bufferLength == mStoredBytes) {
					wait();
				}
				int tail = mHead + mStoredBytes;
				int oneRun;
				if (tail >= bufferLength) {
					tail = tail - bufferLength;
					oneRun = mHead - tail;
				} else {
					oneRun = bufferLength - tail;
				}
				int bytesToCopy = Math.min(oneRun, length);
				System.arraycopy(buffer, offset, mBuffer, tail, bytesToCopy);
				offset += bytesToCopy;
				mStoredBytes += bytesToCopy;
				length -= bytesToCopy;
			}
			if (wasEmpty) {
				notify();
			}
		}
	}

	private byte[] mBuffer;
	private int mHead;
	private int mStoredBytes;
}

/**
 * A view on a transcript and a terminal emulator. Displays the text of the
 * transcript and the current cursor position of the terminal emulator.
 */
@SuppressLint("Instantiatable")
class EmulatorView extends View implements GestureDetector.OnGestureListener {

	private int m_nPreTouchPosX = 0;
	Boolean dubleTouch = false;
	Timer dubleTouchtimer;
	float p_distanceY;
	float m_distanceY;


	
	/**
	 * We defer some initialization until we have been layed out in the view
	 * hierarchy. The boolean tracks when we know what our size is.
	 */
	public boolean mKnownSize;

	/**
	 * Our transcript. Contains the screen and the transcript.
	 */
	TranscriptScreen mTranscriptScreen;

	/**
	 * Number of rows in the transcript.
	 */
	public static int TRANSCRIPT_ROWS = 200; // 理쒙?��? ??��?��?��

	/**
	 * Total width of each character, in pixels
	 */
	private int mCharacterWidth;

	/**
	 * Total height of each character, in pixels
	 */
	private int mCharacterHeight;

	/**
	 * Used to render text
	 */
	private TextRenderer mTextRenderer;

	/**
	 * Text size. Zero means 4 x 8 font.
	 */
	private int mTextSize;

	/**
	 * Foreground color.
	 */
	private int mForeground;

	/**
	 * Background color.
	 */
	private int mBackground;

	/**
	 * Used to paint the cursor
	 */
	private Paint mCursorPaint;

	private Paint mBackgroundPaint;

	/**
	 * Our terminal emulator. We use this to get the current cursor position.
	 */
	private TerminalEmulator mEmulator;

	public int mWidth;
	public int mHeight;

	/**
	 * The number of rows of text to display.
	 */
	private int mRows;

	/**
	 * The number of columns of text to display.
	 */
	private int mColumns;

	/**
	 * The number of columns that are visible on the display.
	 */

	private int mVisibleColumns;

	/**
	 * The top row of text to display. Ranges from -activeTranscriptRows to 0
	 */
	public int mTopRow;

	private int mLeftColumn;

	private ByteQueue mByteQueue;

	/**
	 * Used to temporarily hold data received from the remote process. Allocated
	 * once and used permanently to minimize heap thrashing.
	 */
	private byte[] mReceiveBuffer;

	/**
	 * Our private message id, which we use to receive new input from the remote
	 * process.
	 */
	public static final int UPDATE = 1;

	private GestureDetector mGestureDetector;
	private float mScrollRemainder;
	private TermKeyListener mKeyListener;

	public ble_terminal mble_terminal;

	public Runnable mCheckSize = new Runnable() {
		public void run() {
			updateSize();
			mHandler.postDelayed(this, 1000);
		}
	};

	/**
	 * Our message handler class. Implements a periodic callback.
	 */
	public final Handler mHandler = new Handler() {
		/**
		 * Handle the callback message. Call our enclosing class's update
		 * method.
		 * 
		 * @param msg
		 *            The callback message.
		 */
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == UPDATE) {
				update();
			}
		}
	};



	
	public void commonConstructor() {
		// TODO Auto-generated method stub
		
	}

	public void onResume() {
		updateSize();
		mHandler.postDelayed(mCheckSize, 1000);
	}

	public void onPause() {

		mHandler.removeCallbacks(mCheckSize);

	}

	public void onDestroy() {

		try {
			if (dubleTouchtimer != null) {
				dubleTouchtimer.cancel();
			}
		
		} catch (Exception e1) {

		}
		try {

			mHandler.removeMessages(0);
			
		} catch (Exception e1) {

		}

	}

	public void register(TermKeyListener listener) {
		mKeyListener = listener;
	}

	public void setColors(int foreground, int background) {
		mForeground = foreground;
		mBackground = background;
		updateText();
	}

	public String getTranscriptText() {
		return mEmulator.getTranscriptText();
	}

	public void resetTerminal() {
		mEmulator.reset();
		invalidate();
	}

	@Override
	public boolean onCheckIsTextEditor() {
		return true;
	}

	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
		outAttrs.inputType = EditorInfo.TYPE_NULL;
		return new BaseInputConnection(this, false) {

			private EmulatorView mEmulatorView;

			@Override
			public boolean beginBatchEdit() {
				return true;
			}

			@Override
			public boolean clearMetaKeyStates(int states) {
				return true;
			}

			@Override
			public boolean commitCompletion(CompletionInfo text) {
				return true;
			}

			@Override
			public boolean commitText(CharSequence text, int newCursorPosition) {
				sendText(text);
				return true;
			}

			@Override
			public boolean deleteSurroundingText(int leftLength, int rightLength) {
				return true;
			}

			@Override
			public boolean endBatchEdit() {
				return true;
			}

			@Override
			public boolean finishComposingText() {
				return true;
			}

			@Override
			public int getCursorCapsMode(int reqModes) {
				return 0;
			}

			@Override
			public ExtractedText getExtractedText(ExtractedTextRequest request,
					int flags) {
				return null;
			}

			@Override
			public CharSequence getTextAfterCursor(int n, int flags) {
				return null;
			}

			@Override
			public CharSequence getTextBeforeCursor(int n, int flags) {
				return null;
			}

			@Override
			public boolean performEditorAction(int actionCode) {
				if (actionCode == EditorInfo.IME_ACTION_UNSPECIFIED) {
					// The "return" key has been pressed on the IME.
					sendText("\r");
					// sendText("\n");
					return true;
				}
				return false;
			}

			@Override
			public boolean performContextMenuAction(int id) {
				return true;
			}

			@Override
			public boolean performPrivateCommand(String action, Bundle data) {
				return true;
			}

			/*
			 * @Override public boolean sendKeyEvent(KeyEvent event) { if
			 * (event.getAction() == KeyEvent.ACTION_DOWN) {
			 * switch(event.getKeyCode()) { case KeyEvent.KEYCODE_DEL:
			 * sendChar(127); break; } } return true; }
			 */
			// Code from
			@Override
			public boolean sendKeyEvent(KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					// Some keys are sent here rather than to commitText.
					// In particular, del and the digit keys are sent here.
					// (And I have reports that the HTC Magic also sends Return
					// here.)
					// As a bit of defensive programming, handle every
					// key with an ASCII meaning.
					int keyCode = event.getKeyCode();

					if (keyCode >= 0 && keyCode < KEYCODE_CHARS.length()) {
						Log.d("TEST", "ACTION_MOVE");
						char c = KEYCODE_CHARS.charAt(keyCode);
						if (c > 0) {
							sendChar(c);
						} else {
							// Handle IME arrow key events
							switch (keyCode) {
							case KeyEvent.KEYCODE_DPAD_UP: // Up Arrow
							case KeyEvent.KEYCODE_DPAD_DOWN: // Down Arrow
							case KeyEvent.KEYCODE_DPAD_LEFT: // Left Arrow
							case KeyEvent.KEYCODE_DPAD_RIGHT: // Right Arrow
								super.sendKeyEvent(event);
								break;
							default:
								break;
							} // switch (keyCode)
						}
					}
				}
				return true;
			}

			private final String KEYCODE_CHARS = "\000\000\000\000\000\000\000"
					+ "0123456789*#"
					+ "\000\000\000\000\000\000\000\000\000\000"
					+ "abcdefghijklmnopqrstuvwxyz,." + "\000\000\000\000"
					+ "\011 " // tab, space
					+ "\000\000\000" // sym .. envelope
					+ "\015\177" // enter, del
					+ "`-=[]\\;'/@" + "\000\000\000" + "+";

			@Override
			public boolean setComposingText(CharSequence text,
					int newCursorPosition) {
				return true;
			}

			@Override
			public boolean setSelection(int start, int end) {
				return true;
			}

			private void sendChar(int c) {
				try {
					mapAndSend(c);
				} catch (IOException ex) {
				}
			}

			private void sendText(CharSequence text) {
				int n = text.length();

				try {
					for (int i = 0; i < n; i++) {
						char c = text.charAt(i);
						mapAndSend(c);
					}
				} catch (IOException e) {
				}
			}

			private void mapAndSend(int c) throws IOException {
				byte[] mBuffer = new byte[1];
				mBuffer[0] = (byte) mKeyListener.mapControlChar(c);
				mble_terminal.datasend(mBuffer);

			}
		};
	}

	public void write(byte[] buffer, int length) {
		try {
			mByteQueue.write(buffer, 0, length);
			// if (BlueTerm.mFilesave) filesave(buffer, length);//??��?��?��???��?��?��
		} catch (InterruptedException e) {
		}
		mHandler.sendMessage(mHandler.obtainMessage(UPDATE));
	}

	// String logname = "log6.txt";

	/*
	 * public void filesave(byte[] msg, int length) { String folder =
	 * "/sdcard/"; String extname= ".txt";
	 * folder=folder+BlueTerm.mfolderSaveName+"/"+"BlueDATA"+"/"; //???��?��?�� ??��?��?��?���?? *
	 * String path = folder + BlueTerm.file_name_dd + extname;
	 * 
	 * // ??��?��?��??��?��?�� ??��?���? File dir = new File(folder); // dir.delete(); if
	 * (!dir.isDirectory()) { dir.mkdirs(); } FileOutputStream fos = null; try {
	 * fos = new FileOutputStream(path, true); // ??��?��?��??��?���? ?��뷂옙? 紐⑤�? fos.write(msg,
	 * 0, length); fos.close(); } catch (IOException e) { e.printStackTrace(); }
	 * }
	 */

	public boolean getKeypadApplicationMode() {
		return mEmulator.getKeypadApplicationMode();
	}

	public EmulatorView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public EmulatorView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(R.styleable.EmulatorView);
		initializeScrollbars(a);
		a.recycle();
		commonConstructor(context);
	}

	public void commonConstructor(Context context) {
		mTextRenderer = null;
		mCursorPaint = new Paint();
		mCursorPaint.setARGB(255, 128, 128, 128);
		mBackgroundPaint = new Paint();
		mTopRow = 0;
		mLeftColumn = 0;
		mGestureDetector = new GestureDetector(context, this, null);
		mGestureDetector.setIsLongpressEnabled(false);
		setVerticalScrollBarEnabled(true);
	}

	@Override
	protected int computeVerticalScrollRange() {
		if (mTranscriptScreen == null)
			return 0;

		return mTranscriptScreen.getActiveRows();
	}

	@Override
	protected int computeVerticalScrollExtent() {
		return mRows;
	}

	@Override
	protected int computeVerticalScrollOffset() {
		if (mTranscriptScreen == null)
			return 0;

		return mTranscriptScreen.getActiveRows() + mTopRow - mRows;
	}

	/**
	 * Call this to initialize the view.
	 * 
	 * @param termFd
	 *            the file descriptor
	 * @param termOut
	 *            the output stream for the pseudo-teletype
	 */
	public void initialize(ble_terminal blueTerm) {
		mble_terminal = blueTerm;
		mTextSize = 20;
		mForeground = ble_terminal.WHITE;
		mBackground = ble_terminal.BLACK;
		updateText();
		mReceiveBuffer = new byte[4 * 1024];
		mByteQueue = new ByteQueue(4 * 1024);
		
	}

	/**
	 * Accept a sequence of bytes (typically from the pseudo-tty) and process
	 * them.
	 * 
	 * @param buffer
	 *            a byte array containing bytes to be processed
	 * @param base
	 *            the index of the first byte in the buffer to process
	 * @param length
	 *            the number of bytes to process
	 */
	public void append(byte[] buffer, int base, int length) {
		mEmulator.append(buffer, base, length);
		ensureCursorVisible();
		invalidate();
	}

	/**
	 * Page the terminal view (scroll it up or down by delta screenfulls.)
	 * 
	 * @param delta
	 *            the number of screens to scroll. Positive means scroll down,
	 *            negative means scroll up.
	 */
	public void page(int delta) {
		mTopRow = Math.min(0, Math.max(
				-(mTranscriptScreen.getActiveTranscriptRows()), mTopRow + mRows
						* delta));
		invalidate();
	}

	/**
	 * Page the terminal view horizontally.
	 * 
	 * @param deltaColumns
	 *            the number of columns to scroll. Positive scrolls to the
	 *            right.
	 */
	public void pageHorizontal(int deltaColumns) {
		mLeftColumn = Math.max(0, Math.min(mLeftColumn + deltaColumns, mColumns
				- mVisibleColumns));
		invalidate();
	}

	/**
	 * Sets the text size, which in turn sets the number of rows and columns
	 * 
	 * @param fontSize
	 *            the new font size, in pixels.
	 */
	public void setTextSize(int fontSize) {
		mTextSize = fontSize;
		updateText();
	}

	/*
	 * // Begin GestureDetector.OnGestureListener methods public boolean
	 * onDoubleTapEvent(MotionEvent e) {
	 * 
	 * return false; }
	 */

	// public boolean onSingleTapUp(MotionEvent event) {
	public boolean onSingleTapUp(MotionEvent event) {
			return true;
	}

	public void onLongPress(MotionEvent e) {
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {

	
		if (((distanceY > 0) && ((distanceY <= p_distanceY) && (distanceY >= m_distanceY)))
				|| ((distanceY < 0) && ((distanceY >= p_distanceY) && (distanceY <= m_distanceY)))) {
		
			distanceY += mScrollRemainder * 2;
			int deltaRows = (int) distanceY / mCharacterHeight;
			mScrollRemainder = distanceY - deltaRows * mCharacterHeight;
			
			mTopRow = Math.min(0, Math.max(
					-(mTranscriptScreen.getActiveTranscriptRows()), mTopRow
							+ deltaRows));
			invalidate();
		}
		p_distanceY = distanceY + (70 * distanceY) / 100; // 理쒙?��??���????��?��?�� 5%
		m_distanceY = distanceY + (-70 * distanceY) / 100; // 理쒙?��??���????��?��?�� 5%
		return true;
	}

	public void onSingleTapConfirmed(MotionEvent e) {
	}

	public boolean onJumpTapDown(MotionEvent e1, MotionEvent e2) {
		// Scroll to bottom
		mTopRow = 0;
		invalidate();
		return true;
	}

	public boolean onJumpTapUp(MotionEvent e1, MotionEvent e2) {
		// Scroll to top
		mTopRow = -mTranscriptScreen.getActiveTranscriptRows();
		invalidate();
		return true;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO: add animation man's (non animated) fling
		mScrollRemainder = 0.0f;
		onScroll(e1, e2, 2 * velocityX, -2 * velocityY);
		return true;
	}

	public void onShowPress(MotionEvent e) {
	}

	public boolean onDown(MotionEvent e) {
		mScrollRemainder = 0.0f;
		return true;
	}

	// End GestureDetector.OnGestureListener methods

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			m_nPreTouchPosX = (int) event.getX();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			int nTouchPosX = (int) event.getX();
			if (nTouchPosX + 100 < m_nPreTouchPosX) {

				
			} else if (nTouchPosX > m_nPreTouchPosX + 100) {
				
			}
			m_nPreTouchPosX = nTouchPosX;
		}
		
		if ((event.getAction() == MotionEvent.ACTION_UP)) {

			if (dubleTouch == false) {
				dubleTouchtimer = new Timer();
				dubleTouchtimer.schedule(new mydubleTouch(), 500);
				dubleTouch = true;
			} else {
				ble_terminal.toggleKeyboard();

			}
		}

		return mGestureDetector.onTouchEvent(event);
	}

	class mydubleTouch extends TimerTask {
		@Override
		public void run() {
			try {
				dubleTouch = false;
			} catch (Exception ex2) {

			}

		}
	}

	private void updateText() {

		if (mTextSize > 0) {
			mTextRenderer = new PaintRenderer(mTextSize, mForeground,
					mBackground);
		}
		mBackgroundPaint.setColor(mBackground);
		mCharacterWidth = mTextRenderer.getCharacterWidth();
		mCharacterHeight = mTextRenderer.getCharacterHeight();

		if (mKnownSize) {
			// updateSize(getWidth(), getHeight());
			updateSize();
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {

		// updateSize(w, h);
		if (!mKnownSize) {

			mKnownSize = true;
		}
		updateSize();
	}

	private void updateSize(int w, int h) {
		if (w <= 0 || h <= 0)
			return;
		
		mColumns = w / mCharacterWidth;
		mRows = h / mCharacterHeight;
		if (mTranscriptScreen != null) {
			mEmulator.updateSize(mColumns, mRows);

		} else {
			mTranscriptScreen = new TranscriptScreen(mColumns, TRANSCRIPT_ROWS,mRows, 0, 7);
			mEmulator = new TerminalEmulator(mTranscriptScreen, mColumns, mRows);
		}

		// Reset our paging:
		mTopRow = 0;
		mLeftColumn = 0;
		this.layout(0, 0, w, h);
		invalidate();
	}

	void updateSize() {
		Rect visibleRect;
		if (mble_terminal == null)
			return;
		if (mKnownSize) {
			visibleRect = new Rect();
			getWindowVisibleDisplayFrame(visibleRect);
			int w = visibleRect.width();
			int h = visibleRect.height() -mble_terminal.actionBar_Heigh-mble_terminal.editbox_Heigh-mble_terminal.title_heigh+1;
			if ((w != mWidth || h != mHeight)) {

				mWidth = w;
				mHeight = h;
				updateSize(w, h);
			}
		}
	}

	/**
	 * Look for new input from the ptty, send it to the terminal emulator.
	 */
	private void update() {
		int bytesAvailable = mByteQueue.getBytesAvailable();
		int bytesToRead = Math.min(bytesAvailable, mReceiveBuffer.length);
		try {
			int bytesRead = mByteQueue.read(mReceiveBuffer, 0, bytesToRead);
			append(mReceiveBuffer, 0, bytesRead);
		} catch (InterruptedException e) {
		}

	}

	@Override
	protected void onDraw(Canvas canvas) {
		int w = getWidth();
		int h = getHeight();

		if (mCharacterWidth == 0)
			return;

		canvas.drawRect(0, 0, w, h, mBackgroundPaint);
		mVisibleColumns = w / mCharacterWidth;
		float x = -mLeftColumn * mCharacterWidth;
		float y = mCharacterHeight;

		int endLine = mTopRow + mRows;
		int cx = mEmulator.getCursorCol();
		int cy = mEmulator.getCursorRow();
		for (int i = mTopRow; i < endLine; i++) {
			int cursorX = -1;
			if (i == cy) {
				cursorX = cx;
			}
			mTranscriptScreen.drawText(i, canvas, x, y, mTextRenderer, cursorX);
			y += mCharacterHeight;
		}
	}

	private void ensureCursorVisible() {
		mTopRow = 0;
		if (mVisibleColumns > 0) {
			int cx = mEmulator.getCursorCol();
			int visibleCursorX = mEmulator.getCursorCol() - mLeftColumn;
			if (visibleCursorX < 0) {
				mLeftColumn = cx;
			} else if (visibleCursorX >= mVisibleColumns) {
				mLeftColumn = (cx - mVisibleColumns) + 1;
			}
		}
	}
}

/**
 * An ASCII key listener. Supports control characters and escape. Keeps track of
 * the current state of the alt, shift, and control keys.
 */
class TermKeyListener {
	/**
	 * The state engine for a modifier key. Can be pressed, released, locked,
	 * and so on.
	 * 
	 */
	private class ModifierKey {

		private int mState;

		private static final int UNPRESSED = 0;

		private static final int PRESSED = 1;

		private static final int RELEASED = 2;

		private static final int USED = 3;

		private static final int LOCKED = 4;

		/**
		 * Construct a modifier key. UNPRESSED by default.
		 * 
		 */
		public ModifierKey() {
			mState = UNPRESSED;
		}

		public void onPress() {
			switch (mState) {
			case PRESSED:
				// This is a repeat before use
				break;
			case RELEASED:
				mState = LOCKED;
				break;
			case USED:
				// This is a repeat after use
				break;
			case LOCKED:
				mState = UNPRESSED;
				break;
			default:
				mState = PRESSED;
				break;
			}
		}

		public void onRelease() {
			switch (mState) {
			case USED:
				mState = UNPRESSED;
				break;
			case PRESSED:
				mState = RELEASED;
				break;
			default:
				// Leave state alone
				break;
			}
		}

		public void adjustAfterKeypress() {
			switch (mState) {
			case PRESSED:
				mState = USED;
				break;
			case RELEASED:
				mState = UNPRESSED;
				break;
			default:
				// Leave state alone
				break;
			}
		}

		public boolean isActive() {
			return mState != UNPRESSED;
		}
	}

	private ModifierKey mAltKey = new ModifierKey();

	private ModifierKey mCapKey = new ModifierKey();

	private ModifierKey mControlKey = new ModifierKey();



	/**
	 * Construct a term key listener.
	 * 
	 */
	public TermKeyListener() {
	}

	public void handleControlKey(boolean down) {
		if (down) {
			mControlKey.onPress();
		} else {
			mControlKey.onRelease();
		}
	}

	public int mapControlChar(int ch) {
		int result = ch;
		if (mControlKey.isActive()) {
			// Search is the control key.
			if (result >= 'a' && result <= 'z') {
				result = (char) (result - 'a' + '\001');
			} else if (result == ' ') {
				result = 0;
			} else if ((result == '[') || (result == '1')) {
				result = 27;
			} else if ((result == '\\') || (result == '.')) {
				result = 28;
			} else if ((result == ']') || (result == '0')) {
				result = 29;
			} else if ((result == '^') || (result == '6')) {
				result = 30; // control-^
			} else if ((result == '_') || (result == '5')) {
				result = 31;
			}
		}

		if (result > -1) {
			mAltKey.adjustAfterKeypress();
			mCapKey.adjustAfterKeypress();
			mControlKey.adjustAfterKeypress();
		}
		return result;
	}

	/**
	 * Handle a keyDown event.
	 * 
	 * @param keyCode
	 *            the keycode of the keyDown event
	 * @return the ASCII byte to transmit to the pseudo-teletype, or -1 if this
	 *         event does not produce an ASCII byte.
	 */
	public int keyDown(int keyCode, KeyEvent event) {
		int result = -1;

		switch (keyCode) {
		case KeyEvent.KEYCODE_ALT_RIGHT:
		case KeyEvent.KEYCODE_ALT_LEFT:
			mAltKey.onPress();
			break;

		case KeyEvent.KEYCODE_SHIFT_LEFT:
		case KeyEvent.KEYCODE_SHIFT_RIGHT:
			mCapKey.onPress();
			break;

		case KeyEvent.KEYCODE_ENTER:
			// Convert newlines into returns. The vt100 sends a
			// '\r' when the 'Return' key is pressed, but our
			// KeyEvent translates this as a '\n'.
			result = '\n';
			break;

		case KeyEvent.KEYCODE_DEL:
			// Convert DEL into 127 (instead of 8)
			result = 127;
			break;

		default: {
			result = event
					.getUnicodeChar((mCapKey.isActive() ? KeyEvent.META_SHIFT_ON
							: 0)
							| (mAltKey.isActive() ? KeyEvent.META_ALT_ON : 0));
			break;
		}
		}

		result = mapControlChar(result);

		return result;
	}

	/**
	 * Handle a keyUp event.
	 * 
	 * @param keyCode
	 *            the keyCode of the keyUp event
	 */
	public void keyUp(int keyCode) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_ALT_LEFT:
		case KeyEvent.KEYCODE_ALT_RIGHT:
			mAltKey.onRelease();
			break;
		case KeyEvent.KEYCODE_SHIFT_LEFT:
		case KeyEvent.KEYCODE_SHIFT_RIGHT:
			mCapKey.onRelease();
			break;
		default:
			// Ignore other keyUps
			break;
		}
	}
}
