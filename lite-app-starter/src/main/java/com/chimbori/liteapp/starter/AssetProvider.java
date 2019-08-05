package com.chimbori.liteapp.starter;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.os.ParcelFileDescriptor.AutoCloseOutputStream;
import android.provider.OpenableColumns;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.FileNotFoundException;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AssetProvider extends ContentProvider {
  private static final String TAG = "AssetProvider";

  /**
   * Reports the column names defined in {@link android.provider.OpenableColumns}.
   */
  private static final String[] COLUMNS = {OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE};

  @Override
  public boolean onCreate() {
    return true;  // Provider was successfully loaded.
  }

  @Nullable
  @Override
  public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
    if (projection == null) {
      projection = COLUMNS;
    }

    String[] cols = new String[projection.length];
    Object[] values = new Object[projection.length];

    int i = 0;
    for (String col : projection) {
      cols[i] = col;
      switch (col) {
        case OpenableColumns.DISPLAY_NAME:
          values[i] = uri.getLastPathSegment();
          break;

        case OpenableColumns.SIZE:
          try {
            values[i] = getContext().getAssets().openFd(getAssetPath(uri)).getLength();
          } catch (IOException e) {
            values[i] = 0;
          }
          break;

        case "width":
          try {
            values[i] = getBitmapDimensions(uri).outWidth;
          } catch (IOException e) {
            values[i] = 0;
          }
          break;

        case "height":
          try {
            values[i] = getBitmapDimensions(uri).outHeight;
          } catch (IOException e) {
            values[i] = 0;
          }
          break;

        default:
          values[i] = null;
          Log.e(TAG, "Unexpected column name: " + col);  // Don’t throw or crash.
          break;
      }
      i++;
    }

    cols = copyOf(cols, i);
    values = copyOf(values, i);
    final MatrixCursor cursor = new MatrixCursor(cols, 1);
    cursor.addRow(values);
    return cursor;
  }

  @Nullable
  @Override
  public ParcelFileDescriptor openFile(@NonNull Uri uri, @NonNull String mode) throws FileNotFoundException {
    ParcelFileDescriptor[] pipe = null;
    try {
      pipe = ParcelFileDescriptor.createPipe();
      new TransferOutThread(
          getContext().getAssets().open(getAssetPath(uri)),
          new AutoCloseOutputStream(pipe[1])).start();

    } catch (IOException e) {
      throw new FileNotFoundException(e.getMessage());
    }

    return pipe[0];
  }

  @Nullable
  @Override
  public String getType(@NonNull Uri uri) {
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(
        MimeTypeMap.getFileExtensionFromUrl(uri.toString()));
  }

  @Nullable
  @Override
  public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
    // Only reads are supported.
    throw new UnsupportedOperationException();
  }

  @Override
  public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
    // Only reads are supported.
    throw new UnsupportedOperationException();
  }

  @Override
  public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
    // Only reads are supported.
    throw new UnsupportedOperationException();
  }

  private static String getAssetPath(@NonNull Uri uri) {
    return uri.getPath().substring(1);  // Remove initial "/"
  }

  /**
   * Decodes the bitmap’s metadata (without allocating memory for the full bitmap) to obtain its
   * width, height, and MIME type.
   *
   * @param uri URI of a file in assets that is reasonably believed to be a bitmap.
   * @return image width, height, and MIME type. See documentation for {@link BitmapFactory.Options}.
   * @throws IOException if the specified Uri does not represent an image in assets, or could not
   *                     be decoded.
   */
  private BitmapFactory.Options getBitmapDimensions(@NonNull Uri uri) throws IOException {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeStream(getContext().getAssets().open(getAssetPath(uri)), null, options);
    return options;
  }

  /**
   * Copied from {@link androidx.core.content.FileProvider}
   * http://www.apache.org/licenses/LICENSE-2.0
   */
  private static String[] copyOf(String[] original, int newLength) {
    final String[] result = new String[newLength];
    System.arraycopy(original, 0, result, 0, newLength);
    return result;
  }

  /**
   * Copied from {@link androidx.core.content.FileProvider}
   * http://www.apache.org/licenses/LICENSE-2.0
   */
  private static Object[] copyOf(Object[] original, int newLength) {
    final Object[] result = new Object[newLength];
    System.arraycopy(original, 0, result, 0, newLength);
    return result;
  }
}
