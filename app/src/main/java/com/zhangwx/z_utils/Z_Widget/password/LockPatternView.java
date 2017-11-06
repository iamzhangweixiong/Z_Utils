package com.zhangwx.z_utils.Z_Widget.password;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import com.zhangwx.z_utils.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays and detects the user's unlock attempt, which is a drag of a finger
 * across 9 regions of the screen.
 */
public class LockPatternView extends View {
	// Aspect to use when rendering this view
	private static final int ASPECT_SQUARE = 0; // View will be the minimum of width/height
	private static final int ASPECT_LOCK_WIDTH = 1; // Fixed width; height will be minimum of (w,h)
	private static final int ASPECT_LOCK_HEIGHT = 2; // Fixed height; width will be minimum of (w,h)

	private final CellState[][] mCellStates;

	private final float mDotSize;
	private final float mActivatedScale;
	private final float mPathWidth;

	private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
	private final Paint mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

	private static final int MILLIS_CLEAR_DELAY = 3000;

	/**
	 * This can be used to avoid updating the display for very small motions or noisy panels.
	 * It didn't seem to have much impact on the devices tested, so currently set to 0.
	 */
	private static final float DRAG_THRESHHOLD = 0.0f;

	private OnPatternListener mOnPatternListener;
	private ClearPatternRunnable mClearPatternRunnable = new ClearPatternRunnable();
	private final ArrayList<Cell> mPattern = new ArrayList<Cell>(9);

	/**
	 * Lookup table for the circles of the pattern we are currently drawing.
	 * This will be the cells of the complete pattern unless we are animating,
	 * in which case we use this to hold the cells we are drawing for the in
	 * progress animation.
	 */
	private final boolean[][] mPatternDrawLookup = new boolean[3][3];

	/**
	 * the in progress point:
	 * - during interaction: where the user's finger is
	 * - during animation: the current tip of the animating line
	 */
	private float mInProgressX = -1;
	private float mInProgressY = -1;

	private boolean mInputEnabled = true;
	private boolean mInStealthMode = false;
	private boolean mEnableHapticFeedback = false;
	private boolean mPatternInProgress = false;

	private float mHitFactor = 0.6f;

	private float mSquareWidth;
	private float mSquareHeight;

	private final Path mCurrentPath = new Path();
	private final Rect mInvalidate = new Rect();
	private final Rect mTmpInvalidateRect = new Rect();

	private int mAspect = ASPECT_LOCK_WIDTH;
	private int mRegularColor;
	private int mErrorColor;
	private int mSuccessColor;

	private final Interpolator mFastOutSlowInInterpolator;
	private final Interpolator mLinearOutSlowInInterpolator;

	private DisplayMode mPatternDisplayMode = DisplayMode.Correct;
	/**
	 * Represents a cell in the 3 X 3 matrix of the unlock pattern view.
	 */
	public static class Cell {
		int row;
		int column;

		// keep # objects limited to 9
		static Cell[][] sCells = new Cell[3][3];

		static {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					sCells[i][j] = new Cell(i, j);
				}
			}
		}

		/**
		 * @param row    The row of the cell.
		 * @param column The column of the cell.
		 */
		private Cell(int row, int column) {
			checkRange(row, column);
			this.row = row;
			this.column = column;
		}

		public int getRow() {
			return row;
		}

		public int getColumn() {
			return column;
		}

		/**
		 * @param row    The row of the cell.
		 * @param column The column of the cell.
		 */
		public static synchronized Cell of(int row, int column) {
			checkRange(row, column);
			return sCells[row][column];
		}

		private static void checkRange(int row, int column) {
			if (row < 0 || row > 2) {
				throw new IllegalArgumentException("row must be in range 0-2");
			}
			if (column < 0 || column > 2) {
				throw new IllegalArgumentException("column must be in range 0-2");
			}
		}

		public String toString() {
			return "(row=" + row + ",clmn=" + column + ")";
		}
	}

	public static class CellState {
		public float scale = 1.0f;
		public float translateY = 0.0f;
		public float alpha = 1.0f;
		public float lineEndX = Float.MIN_VALUE;
		public float lineEndY = Float.MIN_VALUE;
		public SimpleAnimator lineAnimator;
	}

	/**
	 * The call back interface for detecting patterns entered by the user.
	 */
	public static interface OnPatternListener {

		/**
		 * A new pattern has begun.
		 */
		void onPatternStart();

		/**
		 * The pattern was cleared.
		 */
		void onPatternCleared();

		/**
		 * The user extended the pattern currently being drawn by one cell.
		 *
		 * @param pattern The pattern with newly added cell.
		 */
		void onPatternCellAdded(List<Cell> pattern);

		/**
		 * A pattern was detected from the user.
		 *
		 * @param pattern The pattern.
		 */
		void onPatternDetected(List<Cell> pattern);
	}

	public LockPatternView(Context context) {
		this(context, null);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@SuppressWarnings("ResourceType")
	public LockPatternView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setClickable(true);

		final Resources res = getResources();
		final TypedValue value = new TypedValue();
		context.getTheme().resolveAttribute(android.R.attr.textColorPrimary, value, true);
		final int color = res.getColor(value.resourceId);
		final float density = res.getDisplayMetrics().density;

		mRegularColor = color;
		mSuccessColor = res.getColor(R.color.colorPrimary);
		mErrorColor = res.getColor(R.color.colorAccent);

		int pathColor = mRegularColor;
		mPathPaint.setColor(pathColor);

		mPathPaint.setStyle(Paint.Style.STROKE);
		mPathPaint.setStrokeJoin(Paint.Join.ROUND);
		mPathPaint.setStrokeCap(Paint.Cap.ROUND);

		mPathWidth = density * 3;
		mPathPaint.setStrokeWidth(mPathWidth);

		mDotSize = density * 12;
		mActivatedScale = 28 / 12f;

		mCellStates = new CellState[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++)
				mCellStates[i][j] = new CellState();
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			mFastOutSlowInInterpolator = AnimationUtils.loadInterpolator(context, android.R.interpolator.fast_out_slow_in);
			mLinearOutSlowInInterpolator = AnimationUtils.loadInterpolator(context, android.R.interpolator.linear_out_slow_in);
		} else {
			mFastOutSlowInInterpolator = new AccelerateDecelerateInterpolator();
			mLinearOutSlowInInterpolator = new AccelerateDecelerateInterpolator();
		}
	}

	public CellState[][] getCellStates() {
		return mCellStates;
	}

	/**
	 * @return Whether the view is in stealth mode.
	 */
	public boolean isInStealthMode() {
		return mInStealthMode;
	}

	/**
	 * @return Whether the view has tactile feedback enabled.
	 */
	public boolean isTactileFeedbackEnabled() {
		return mEnableHapticFeedback;
	}

	/**
	 * Set whether the view is in stealth mode.  If true, there will be no
	 * visible feedback as the user enters the pattern.
	 *
	 * @param inStealthMode Whether in stealth mode.
	 */
	public void setInStealthMode(boolean inStealthMode) {
		mInStealthMode = inStealthMode;
	}

	/**
	 * Set whether the view will use tactile feedback.  If true, there will be
	 * tactile feedback as the user enters the pattern.
	 *
	 * @param tactileFeedbackEnabled Whether tactile feedback is enabled
	 */
	public void setTactileFeedbackEnabled(boolean tactileFeedbackEnabled) {
		mEnableHapticFeedback = tactileFeedbackEnabled;
	}

	/**
	 * Set the call back for pattern detection.
	 *
	 * @param onPatternListener The call back.
	 */
	public void setOnPatternListener(
			OnPatternListener onPatternListener) {
		mOnPatternListener = onPatternListener;
	}

	private void notifyCellAdded() {
		//sendAccessEvent("lockscreen_access_pattern_cell_added");
		if (mOnPatternListener != null) {
			mOnPatternListener.onPatternCellAdded(mPattern);
		}
	}

	private void notifyPatternStarted() {
		//sendAccessEvent("lockscreen_access_pattern_start");
		if (mOnPatternListener != null) {
			mOnPatternListener.onPatternStart();
		}
	}

	private void notifyPatternDetected() {
		//sendAccessEvent("lockscreen_access_pattern_detected");
		if (mOnPatternListener != null) {
			mOnPatternListener.onPatternDetected(mPattern);
		}
	}

	private void notifyPatternCleared() {
		//sendAccessEvent("lockscreen_access_pattern_cleared");
		if (mOnPatternListener != null) {
			mOnPatternListener.onPatternCleared();
		}
	}

	/**
	 * Clear the pattern.
	 */
	public void clearPattern() {
		resetPattern();
	}

	/**
	 * Reset all pattern state.
	 */
	private void resetPattern() {
		mPattern.clear();
		clearPatternDrawLookup();
		invalidate();
	}

	/**
	 * Clear the pattern lookup table.
	 */
	private void clearPatternDrawLookup() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				mPatternDrawLookup[i][j] = false;
			}
		}
	}

	/**
	 * Disable input (for instance when displaying a message that will
	 * timeout so user doesn't get view into messy state).
	 */
	public void disableInput() {
		mInputEnabled = false;
	}

	/**
	 * Enable input.
	 */
	public void enableInput() {
		mInputEnabled = true;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		final int width = w - getPaddingLeft() - getPaddingRight();
		mSquareWidth = width / 3.0f;

		final int height = h - getPaddingTop() - getPaddingBottom();
		mSquareHeight = height / 3.0f;
	}

	private int resolveMeasured(int measureSpec, int desired) {
		int result = 0;
		int specSize = MeasureSpec.getSize(measureSpec);
		switch (MeasureSpec.getMode(measureSpec)) {
			case MeasureSpec.UNSPECIFIED:
				result = desired;
				break;
			case MeasureSpec.AT_MOST:
				result = Math.max(specSize, desired);
				break;
			case MeasureSpec.EXACTLY:
			default:
				result = specSize;
		}
		return result;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int minimumWidth = getSuggestedMinimumWidth();
		final int minimumHeight = getSuggestedMinimumHeight();
		int viewWidth = resolveMeasured(widthMeasureSpec, minimumWidth);
		int viewHeight = resolveMeasured(heightMeasureSpec, minimumHeight);

		switch (mAspect) {
			case ASPECT_SQUARE:
				viewWidth = viewHeight = Math.min(viewWidth, viewHeight);
				break;
			case ASPECT_LOCK_WIDTH:
				viewHeight = Math.min(viewWidth, viewHeight);
				break;
			case ASPECT_LOCK_HEIGHT:
				viewWidth = Math.min(viewWidth, viewHeight);
				break;
		}
		// Log.v(TAG, "LockPatternView dimensions: " + viewWidth + "x" + viewHeight);
		setMeasuredDimension(viewWidth, viewHeight);
	}

	/**
	 * Determines whether the point x, y will add a new point to the current
	 * pattern (in addition to finding the cell, also makes heuristic choices
	 * such as filling in gaps based on current pattern).
	 *
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	private Cell detectAndAddHit(float x, float y) {
		final Cell cell = checkForNewHit(x, y);
		if (cell != null) {

			// check for gaps in existing pattern
			Cell fillInGapCell = null;
			final ArrayList<Cell> pattern = mPattern;
			if (!pattern.isEmpty()) {
				final Cell lastCell = pattern.get(pattern.size() - 1);
				int dRow = cell.row - lastCell.row;
				int dColumn = cell.column - lastCell.column;

				int fillInRow = lastCell.row;
				int fillInColumn = lastCell.column;

				if (Math.abs(dRow) == 2 && Math.abs(dColumn) != 1) {
					fillInRow = lastCell.row + ((dRow > 0) ? 1 : -1);
				}

				if (Math.abs(dColumn) == 2 && Math.abs(dRow) != 1) {
					fillInColumn = lastCell.column + ((dColumn > 0) ? 1 : -1);
				}

				fillInGapCell = Cell.of(fillInRow, fillInColumn);
			}

			if (fillInGapCell != null &&
					!mPatternDrawLookup[fillInGapCell.row][fillInGapCell.column]) {
				addCellToPattern(fillInGapCell);
			}
			addCellToPattern(cell);
			return cell;
		}
		return null;
	}

	private void addCellToPattern(Cell newCell) {
		mPatternDrawLookup[newCell.getRow()][newCell.getColumn()] = true;
		mPattern.add(newCell);
		if (!mInStealthMode) {
			startCellActivatedAnimation(newCell);
		}
		notifyCellAdded();
	}

	private void startCellActivatedAnimation(Cell cell) {
		final CellState cellState = mCellStates[cell.row][cell.column];
		startScaleAnimation(1, mActivatedScale, 96, mLinearOutSlowInInterpolator,
				cellState, new Runnable() {
					@Override
					public void run() {
						startScaleAnimation(mActivatedScale, 1, 192, mFastOutSlowInInterpolator,
								cellState, null);
					}
				});
		startLineEndAnimation(cellState, mInProgressX, mInProgressY,
				getCenterXForColumn(cell.column), getCenterYForRow(cell.row));
	}

	private void startLineEndAnimation(final CellState state,
									   final float startX, final float startY, final float targetX, final float targetY) {
		final SimpleAnimator animator = new SimpleAnimator(this, mFastOutSlowInInterpolator) {
			@Override
			public void onDoing(float interpolation) {
				state.lineEndX = (1 - interpolation) * startX + interpolation * targetX;
				state.lineEndY = (1 - interpolation) * startY + interpolation * targetY;
				invalidate();
			}

			@Override
			public void onEnd() {
				state.lineAnimator = null;
			}
		};
		animator.start(100, true);
		state.lineAnimator = animator;
	}

	private void startScaleAnimation(final float start, final float end, long duration, Interpolator interpolator,
									 final CellState state, final Runnable endRunnable) {
		final SimpleAnimator animator = new SimpleAnimator(this, interpolator) {
			@Override
			public void onDoing(float interpolation) {
				state.scale = start + (end - start) * interpolation;
				invalidate();
			}

			@Override
			public void onEnd() {
				if (endRunnable != null)
					endRunnable.run();
			}
		};
		animator.start(duration, true);
	}

	// helper method to find which cell a point maps to
	private Cell checkForNewHit(float x, float y) {

		final int rowHit = getRowHit(y);
		if (rowHit < 0) {
			return null;
		}
		final int columnHit = getColumnHit(x);
		if (columnHit < 0) {
			return null;
		}

		if (mPatternDrawLookup[rowHit][columnHit]) {
			return null;
		}
		return Cell.of(rowHit, columnHit);
	}

	/**
	 * Helper method to find the row that y falls into.
	 *
	 * @param y The y coordinate
	 * @return The row that y falls in, or -1 if it falls in no row.
	 */
	private int getRowHit(float y) {

		final float squareHeight = mSquareHeight;
		float hitSize = squareHeight * mHitFactor;

		float offset = getPaddingTop() + (squareHeight - hitSize) / 2f;
		for (int i = 0; i < 3; i++) {

			final float hitTop = offset + squareHeight * i;
			if (y >= hitTop && y <= hitTop + hitSize) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Helper method to find the column x fallis into.
	 *
	 * @param x The x coordinate.
	 * @return The column that x falls in, or -1 if it falls in no column.
	 */
	private int getColumnHit(float x) {
		final float squareWidth = mSquareWidth;
		float hitSize = squareWidth * mHitFactor;

		float offset = getPaddingLeft() + (squareWidth - hitSize) / 2f;
		for (int i = 0; i < 3; i++) {

			final float hitLeft = offset + squareWidth * i;
			if (x >= hitLeft && x <= hitLeft + hitSize) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public boolean onHoverEvent(MotionEvent event) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			AccessibilityManager accessibilityManager = (AccessibilityManager) getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
			if (accessibilityManager.isTouchExplorationEnabled()) {
				final int action = event.getAction();
				switch (action) {
					case MotionEvent.ACTION_HOVER_ENTER:
						event.setAction(MotionEvent.ACTION_DOWN);
						break;
					case MotionEvent.ACTION_HOVER_MOVE:
						event.setAction(MotionEvent.ACTION_MOVE);
						break;
					case MotionEvent.ACTION_HOVER_EXIT:
						event.setAction(MotionEvent.ACTION_UP);
						break;
				}
				onTouchEvent(event);
				event.setAction(action);
			}
			return super.onHoverEvent(event);
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!mInputEnabled || !isEnabled()) {
			return false;
		}

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				handleActionDown(event);
				return true;
			case MotionEvent.ACTION_UP:
				handleActionUp(event);
				return true;
			case MotionEvent.ACTION_MOVE:
				handleActionMove(event);
				return true;
			case MotionEvent.ACTION_CANCEL:
				if (mPatternInProgress) {
					mPatternInProgress = false;
					resetPattern();
					notifyPatternCleared();
				}
				return true;
		}
		return false;
	}

	private void handleActionMove(MotionEvent event) {
		// Handle all recent motion events so we don't skip any cells even when the device
		// is busy...
		final float radius = mPathWidth;
		final int historySize = event.getHistorySize();
		mTmpInvalidateRect.setEmpty();
		boolean invalidateNow = false;
		for (int i = 0; i < historySize + 1; i++) {
			final float x = i < historySize ? event.getHistoricalX(i) : event.getX();
			final float y = i < historySize ? event.getHistoricalY(i) : event.getY();
			Cell hitCell = detectAndAddHit(x, y);
			final int patternSize = mPattern.size();
			if (hitCell != null && patternSize == 1) {
				mPatternInProgress = true;
				notifyPatternStarted();
			}
			// note current x and y for rubber banding of in progress patterns
			final float dx = Math.abs(x - mInProgressX);
			final float dy = Math.abs(y - mInProgressY);
			if (dx > DRAG_THRESHHOLD || dy > DRAG_THRESHHOLD) {
				invalidateNow = true;
			}

			if (mPatternInProgress && patternSize > 0) {
				final ArrayList<Cell> pattern = mPattern;
				final Cell lastCell = pattern.get(patternSize - 1);
				float lastCellCenterX = getCenterXForColumn(lastCell.column);
				float lastCellCenterY = getCenterYForRow(lastCell.row);

				// Adjust for drawn segment from last cell to (x,y). Radius accounts for line width.
				float left = Math.min(lastCellCenterX, x) - radius;
				float right = Math.max(lastCellCenterX, x) + radius;
				float top = Math.min(lastCellCenterY, y) - radius;
				float bottom = Math.max(lastCellCenterY, y) + radius;

				// Invalidate between the pattern's new cell and the pattern's previous cell
				if (hitCell != null) {
					final float width = mSquareWidth * 0.5f;
					final float height = mSquareHeight * 0.5f;
					final float hitCellCenterX = getCenterXForColumn(hitCell.column);
					final float hitCellCenterY = getCenterYForRow(hitCell.row);

					left = Math.min(hitCellCenterX - width, left);
					right = Math.max(hitCellCenterX + width, right);
					top = Math.min(hitCellCenterY - height, top);
					bottom = Math.max(hitCellCenterY + height, bottom);
				}

				// Invalidate between the pattern's last cell and the previous location
				mTmpInvalidateRect.union(Math.round(left), Math.round(top),
						Math.round(right), Math.round(bottom));
			}
		}
		mInProgressX = event.getX();
		mInProgressY = event.getY();

		// To save updates, we only invalidate if the user moved beyond a certain amount.
		if (invalidateNow) {
			mInvalidate.union(mTmpInvalidateRect);
			invalidate(mInvalidate);
			mInvalidate.set(mTmpInvalidateRect);
		}
	}

	private void sendAccessEvent(String resId) {
		try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
				announceForAccessibility(getResources().getString(getResources().getIdentifier(resId, "string", "android")));
		} catch (Throwable e) {
		}
	}

	private void handleActionUp(MotionEvent event) {
		// report pattern detected
		if (!mPattern.isEmpty()) {
			mPatternInProgress = false;
			cancelLineAnimations();
			notifyPatternDetected();
			invalidate();
			postDelayed(mClearPatternRunnable, MILLIS_CLEAR_DELAY);
		}
	}

	private void cancelLineAnimations() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				CellState state = mCellStates[i][j];
				if (state.lineAnimator != null) {
					state.lineAnimator.stop();
					state.lineEndX = Float.MIN_VALUE;
					state.lineEndY = Float.MIN_VALUE;
				}
			}
		}
	}

	private void handleActionDown(MotionEvent event) {
		resetPattern();
		removeCallbacks(mClearPatternRunnable);
		mPatternDisplayMode = DisplayMode.Correct;
		final float x = event.getX();
		final float y = event.getY();
		final Cell hitCell = detectAndAddHit(x, y);
		if (hitCell != null) {
			mPatternInProgress = true;
			notifyPatternStarted();
		} else if (mPatternInProgress) {
			mPatternInProgress = false;
			notifyPatternCleared();
		}
		if (hitCell != null) {
			final float startX = getCenterXForColumn(hitCell.column);
			final float startY = getCenterYForRow(hitCell.row);

			final float widthOffset = mSquareWidth / 2f;
			final float heightOffset = mSquareHeight / 2f;

			invalidate((int) (startX - widthOffset), (int) (startY - heightOffset),
					(int) (startX + widthOffset), (int) (startY + heightOffset));
		}
		mInProgressX = x;
		mInProgressY = y;
	}

	private float getCenterXForColumn(int column) {
		return getPaddingLeft() + column * mSquareWidth + mSquareWidth / 2f;
	}

	private float getCenterYForRow(int row) {
		return getPaddingTop() + row * mSquareHeight + mSquareHeight / 2f;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		final ArrayList<Cell> pattern = mPattern;
		final int count = pattern.size();
		final boolean[][] drawLookup = mPatternDrawLookup;
		final Path currentPath = mCurrentPath;
		currentPath.rewind();

		// draw the circles
		for (int i = 0; i < 3; i++) {
			float centerY = getCenterYForRow(i);
			for (int j = 0; j < 3; j++) {
				final CellState cellState = mCellStates[i][j];
				float centerX = getCenterXForColumn(j);
				float size = cellState.scale * mDotSize;
				float translationY = cellState.translateY;
				drawCircle(canvas, (int) centerX, (int) centerY + translationY,
						size, drawLookup[i][j], cellState.alpha);
			}
		}

		// TODO: the path should be created and cached every time we hit-detect a cell
		// only the last segment of the path should be computed here
		// draw the path of the pattern (unless we are in stealth mode)
		final boolean drawPath = !mInStealthMode;

		if (drawPath) {
			mPathPaint.setColor(getCurrentColor(true /* partOfPattern */));

			boolean anyCircles = false;
			float lastX = 0f;
			float lastY = 0f;
			for (int i = 0; i < count; i++) {
				Cell cell = pattern.get(i);

				// only draw the part of the pattern stored in
				// the lookup table (this is only different in the case
				// of animation).
				if (!drawLookup[cell.row][cell.column]) {
					break;
				}
				anyCircles = true;

				float centerX = getCenterXForColumn(cell.column);
				float centerY = getCenterYForRow(cell.row);
				if (i != 0) {
					CellState state = mCellStates[cell.row][cell.column];
					currentPath.rewind();
					currentPath.moveTo(lastX, lastY);
					if (state.lineEndX != Float.MIN_VALUE && state.lineEndY != Float.MIN_VALUE) {
						currentPath.lineTo(state.lineEndX, state.lineEndY);
					} else {
						currentPath.lineTo(centerX, centerY);
					}
					canvas.drawPath(currentPath, mPathPaint);
				}
				lastX = centerX;
				lastY = centerY;
			}

			// draw last in progress section
			if (mPatternInProgress && anyCircles) {
				currentPath.rewind();
				currentPath.moveTo(lastX, lastY);
				currentPath.lineTo(mInProgressX, mInProgressY);

				mPathPaint.setAlpha((int) (calculateLastSegmentAlpha(
						mInProgressX, mInProgressY, lastX, lastY) * 255f));
				canvas.drawPath(currentPath, mPathPaint);
			}
		}
	}

	private float calculateLastSegmentAlpha(float x, float y, float lastX, float lastY) {
		float diffX = x - lastX;
		float diffY = y - lastY;
		float dist = (float) Math.sqrt(diffX * diffX + diffY * diffY);
		float frac = dist / mSquareWidth;
		return Math.min(1f, Math.max(0f, (frac - 0.3f) * 4f));
	}

	private int getCurrentColor(boolean partOfPattern) {
		if (!partOfPattern || mInStealthMode || mPatternInProgress) {
			// unselected circle
			return mRegularColor;
		} else {
			if (mPatternDisplayMode == DisplayMode.Correct) {
				return mSuccessColor;
			} else {
				return mErrorColor;
			}
		}
	}

	/**
	 * @param partOfPattern Whether this circle is part of the pattern.
	 */
	private void drawCircle(Canvas canvas, float centerX, float centerY, float size,
                            boolean partOfPattern, float alpha) {
		mPaint.setColor(getCurrentColor(partOfPattern));
		mPaint.setAlpha((int) (alpha * 255));
		canvas.drawCircle(centerX, centerY, size / 2, mPaint);
	}

	private static final char FIRST_CHAR = 'a';

	/**
	 * Deserialize a pattern.
	 *
	 * @param string The pattern serialized with {@link #patternToString}
	 * @return The pattern.
	 */
	public static List<Cell> stringToPattern(String string) {
		final List<Cell> result = new ArrayList();
		final int length = string.length();
		for (int i = 0; i < length; i++) {
			int n = string.charAt(i) - FIRST_CHAR;
			result.add(LockPatternView.Cell.of(n / 3, n % 3));
		}
		return result;
	}

	/**
	 * Serialize a pattern.
	 *
	 * @param pattern The pattern.
	 * @return The pattern in string form.
	 */
	public static String patternToString(List<Cell> pattern) {
		if (pattern == null)
			return "";

		final int patternSize = pattern.size();
		final StringBuilder builder = new StringBuilder(patternSize);
		for (int i = 0; i < patternSize; i++) {
			final Cell cell = pattern.get(i);
			int n = (cell.getRow() * 3 + cell.getColumn() + FIRST_CHAR);
			builder.append((char) n);
		}
		return builder.toString();
	}

	public void drawError() {
		mPatternDisplayMode = DisplayMode.Wrong;
		invalidate();
	}

	/**
	 * How to display the current pattern.
	 */
	public enum DisplayMode {

		/**
		 * The pattern drawn is correct (i.e draw it in a friendly color)
		 */
		Correct,

		/**
		 * The pattern is wrong (i.e draw a foreboding color)
		 */
		Wrong
	}

	private class ClearPatternRunnable implements Runnable {

		@Override
		public void run() {
			clearPattern();
		}
	}
}
