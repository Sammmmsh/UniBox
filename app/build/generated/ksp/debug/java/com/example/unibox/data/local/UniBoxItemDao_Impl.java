package com.example.unibox.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class UniBoxItemDao_Impl implements UniBoxItemDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UniBoxItemEntity> __insertionAdapterOfUniBoxItemEntity;

  private final EntityDeletionOrUpdateAdapter<UniBoxItemEntity> __deletionAdapterOfUniBoxItemEntity;

  private final EntityDeletionOrUpdateAdapter<UniBoxItemEntity> __updateAdapterOfUniBoxItemEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteItemById;

  public UniBoxItemDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUniBoxItemEntity = new EntityInsertionAdapter<UniBoxItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `unibox_items` (`id`,`title`,`description`,`url`,`thumbnailUrl`,`extractedText`,`category`,`sourceApp`,`timestamp`,`latitude`,`longitude`,`locationLabel`,`imageUri`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UniBoxItemEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getDescription());
        if (entity.getUrl() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getUrl());
        }
        if (entity.getThumbnailUrl() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getThumbnailUrl());
        }
        if (entity.getExtractedText() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getExtractedText());
        }
        statement.bindString(7, entity.getCategory());
        if (entity.getSourceApp() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getSourceApp());
        }
        statement.bindLong(9, entity.getTimestamp());
        if (entity.getLatitude() == null) {
          statement.bindNull(10);
        } else {
          statement.bindDouble(10, entity.getLatitude());
        }
        if (entity.getLongitude() == null) {
          statement.bindNull(11);
        } else {
          statement.bindDouble(11, entity.getLongitude());
        }
        if (entity.getLocationLabel() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getLocationLabel());
        }
        if (entity.getImageUri() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getImageUri());
        }
      }
    };
    this.__deletionAdapterOfUniBoxItemEntity = new EntityDeletionOrUpdateAdapter<UniBoxItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `unibox_items` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UniBoxItemEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfUniBoxItemEntity = new EntityDeletionOrUpdateAdapter<UniBoxItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `unibox_items` SET `id` = ?,`title` = ?,`description` = ?,`url` = ?,`thumbnailUrl` = ?,`extractedText` = ?,`category` = ?,`sourceApp` = ?,`timestamp` = ?,`latitude` = ?,`longitude` = ?,`locationLabel` = ?,`imageUri` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UniBoxItemEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getDescription());
        if (entity.getUrl() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getUrl());
        }
        if (entity.getThumbnailUrl() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getThumbnailUrl());
        }
        if (entity.getExtractedText() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getExtractedText());
        }
        statement.bindString(7, entity.getCategory());
        if (entity.getSourceApp() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getSourceApp());
        }
        statement.bindLong(9, entity.getTimestamp());
        if (entity.getLatitude() == null) {
          statement.bindNull(10);
        } else {
          statement.bindDouble(10, entity.getLatitude());
        }
        if (entity.getLongitude() == null) {
          statement.bindNull(11);
        } else {
          statement.bindDouble(11, entity.getLongitude());
        }
        if (entity.getLocationLabel() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getLocationLabel());
        }
        if (entity.getImageUri() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getImageUri());
        }
        statement.bindLong(14, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteItemById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM unibox_items WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertItem(final UniBoxItemEntity item,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfUniBoxItemEntity.insertAndReturnId(item);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteItem(final UniBoxItemEntity item,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfUniBoxItemEntity.handle(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateItem(final UniBoxItemEntity item,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfUniBoxItemEntity.handle(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteItemById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteItemById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteItemById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<UniBoxItemEntity>> getAllItems() {
    final String _sql = "SELECT * FROM unibox_items ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"unibox_items"}, new Callable<List<UniBoxItemEntity>>() {
      @Override
      @NonNull
      public List<UniBoxItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
          final int _cursorIndexOfThumbnailUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailUrl");
          final int _cursorIndexOfExtractedText = CursorUtil.getColumnIndexOrThrow(_cursor, "extractedText");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfSourceApp = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceApp");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfLocationLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "locationLabel");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
          final List<UniBoxItemEntity> _result = new ArrayList<UniBoxItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final UniBoxItemEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpUrl;
            if (_cursor.isNull(_cursorIndexOfUrl)) {
              _tmpUrl = null;
            } else {
              _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
            }
            final String _tmpThumbnailUrl;
            if (_cursor.isNull(_cursorIndexOfThumbnailUrl)) {
              _tmpThumbnailUrl = null;
            } else {
              _tmpThumbnailUrl = _cursor.getString(_cursorIndexOfThumbnailUrl);
            }
            final String _tmpExtractedText;
            if (_cursor.isNull(_cursorIndexOfExtractedText)) {
              _tmpExtractedText = null;
            } else {
              _tmpExtractedText = _cursor.getString(_cursorIndexOfExtractedText);
            }
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpSourceApp;
            if (_cursor.isNull(_cursorIndexOfSourceApp)) {
              _tmpSourceApp = null;
            } else {
              _tmpSourceApp = _cursor.getString(_cursorIndexOfSourceApp);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final Double _tmpLatitude;
            if (_cursor.isNull(_cursorIndexOfLatitude)) {
              _tmpLatitude = null;
            } else {
              _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            }
            final Double _tmpLongitude;
            if (_cursor.isNull(_cursorIndexOfLongitude)) {
              _tmpLongitude = null;
            } else {
              _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            }
            final String _tmpLocationLabel;
            if (_cursor.isNull(_cursorIndexOfLocationLabel)) {
              _tmpLocationLabel = null;
            } else {
              _tmpLocationLabel = _cursor.getString(_cursorIndexOfLocationLabel);
            }
            final String _tmpImageUri;
            if (_cursor.isNull(_cursorIndexOfImageUri)) {
              _tmpImageUri = null;
            } else {
              _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            }
            _item = new UniBoxItemEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpUrl,_tmpThumbnailUrl,_tmpExtractedText,_tmpCategory,_tmpSourceApp,_tmpTimestamp,_tmpLatitude,_tmpLongitude,_tmpLocationLabel,_tmpImageUri);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<UniBoxItemEntity>> getItemsByCategory(final String category) {
    final String _sql = "SELECT * FROM unibox_items WHERE category = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, category);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"unibox_items"}, new Callable<List<UniBoxItemEntity>>() {
      @Override
      @NonNull
      public List<UniBoxItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
          final int _cursorIndexOfThumbnailUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailUrl");
          final int _cursorIndexOfExtractedText = CursorUtil.getColumnIndexOrThrow(_cursor, "extractedText");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfSourceApp = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceApp");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfLocationLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "locationLabel");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
          final List<UniBoxItemEntity> _result = new ArrayList<UniBoxItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final UniBoxItemEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpUrl;
            if (_cursor.isNull(_cursorIndexOfUrl)) {
              _tmpUrl = null;
            } else {
              _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
            }
            final String _tmpThumbnailUrl;
            if (_cursor.isNull(_cursorIndexOfThumbnailUrl)) {
              _tmpThumbnailUrl = null;
            } else {
              _tmpThumbnailUrl = _cursor.getString(_cursorIndexOfThumbnailUrl);
            }
            final String _tmpExtractedText;
            if (_cursor.isNull(_cursorIndexOfExtractedText)) {
              _tmpExtractedText = null;
            } else {
              _tmpExtractedText = _cursor.getString(_cursorIndexOfExtractedText);
            }
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpSourceApp;
            if (_cursor.isNull(_cursorIndexOfSourceApp)) {
              _tmpSourceApp = null;
            } else {
              _tmpSourceApp = _cursor.getString(_cursorIndexOfSourceApp);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final Double _tmpLatitude;
            if (_cursor.isNull(_cursorIndexOfLatitude)) {
              _tmpLatitude = null;
            } else {
              _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            }
            final Double _tmpLongitude;
            if (_cursor.isNull(_cursorIndexOfLongitude)) {
              _tmpLongitude = null;
            } else {
              _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            }
            final String _tmpLocationLabel;
            if (_cursor.isNull(_cursorIndexOfLocationLabel)) {
              _tmpLocationLabel = null;
            } else {
              _tmpLocationLabel = _cursor.getString(_cursorIndexOfLocationLabel);
            }
            final String _tmpImageUri;
            if (_cursor.isNull(_cursorIndexOfImageUri)) {
              _tmpImageUri = null;
            } else {
              _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            }
            _item = new UniBoxItemEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpUrl,_tmpThumbnailUrl,_tmpExtractedText,_tmpCategory,_tmpSourceApp,_tmpTimestamp,_tmpLatitude,_tmpLongitude,_tmpLocationLabel,_tmpImageUri);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<UniBoxItemEntity> getItemById(final long id) {
    final String _sql = "SELECT * FROM unibox_items WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"unibox_items"}, new Callable<UniBoxItemEntity>() {
      @Override
      @Nullable
      public UniBoxItemEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
          final int _cursorIndexOfThumbnailUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailUrl");
          final int _cursorIndexOfExtractedText = CursorUtil.getColumnIndexOrThrow(_cursor, "extractedText");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfSourceApp = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceApp");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfLocationLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "locationLabel");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
          final UniBoxItemEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpUrl;
            if (_cursor.isNull(_cursorIndexOfUrl)) {
              _tmpUrl = null;
            } else {
              _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
            }
            final String _tmpThumbnailUrl;
            if (_cursor.isNull(_cursorIndexOfThumbnailUrl)) {
              _tmpThumbnailUrl = null;
            } else {
              _tmpThumbnailUrl = _cursor.getString(_cursorIndexOfThumbnailUrl);
            }
            final String _tmpExtractedText;
            if (_cursor.isNull(_cursorIndexOfExtractedText)) {
              _tmpExtractedText = null;
            } else {
              _tmpExtractedText = _cursor.getString(_cursorIndexOfExtractedText);
            }
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpSourceApp;
            if (_cursor.isNull(_cursorIndexOfSourceApp)) {
              _tmpSourceApp = null;
            } else {
              _tmpSourceApp = _cursor.getString(_cursorIndexOfSourceApp);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final Double _tmpLatitude;
            if (_cursor.isNull(_cursorIndexOfLatitude)) {
              _tmpLatitude = null;
            } else {
              _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            }
            final Double _tmpLongitude;
            if (_cursor.isNull(_cursorIndexOfLongitude)) {
              _tmpLongitude = null;
            } else {
              _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            }
            final String _tmpLocationLabel;
            if (_cursor.isNull(_cursorIndexOfLocationLabel)) {
              _tmpLocationLabel = null;
            } else {
              _tmpLocationLabel = _cursor.getString(_cursorIndexOfLocationLabel);
            }
            final String _tmpImageUri;
            if (_cursor.isNull(_cursorIndexOfImageUri)) {
              _tmpImageUri = null;
            } else {
              _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            }
            _result = new UniBoxItemEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpUrl,_tmpThumbnailUrl,_tmpExtractedText,_tmpCategory,_tmpSourceApp,_tmpTimestamp,_tmpLatitude,_tmpLongitude,_tmpLocationLabel,_tmpImageUri);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getItemByIdSync(final long id,
      final Continuation<? super UniBoxItemEntity> $completion) {
    final String _sql = "SELECT * FROM unibox_items WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<UniBoxItemEntity>() {
      @Override
      @Nullable
      public UniBoxItemEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
          final int _cursorIndexOfThumbnailUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailUrl");
          final int _cursorIndexOfExtractedText = CursorUtil.getColumnIndexOrThrow(_cursor, "extractedText");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfSourceApp = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceApp");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfLocationLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "locationLabel");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
          final UniBoxItemEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpUrl;
            if (_cursor.isNull(_cursorIndexOfUrl)) {
              _tmpUrl = null;
            } else {
              _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
            }
            final String _tmpThumbnailUrl;
            if (_cursor.isNull(_cursorIndexOfThumbnailUrl)) {
              _tmpThumbnailUrl = null;
            } else {
              _tmpThumbnailUrl = _cursor.getString(_cursorIndexOfThumbnailUrl);
            }
            final String _tmpExtractedText;
            if (_cursor.isNull(_cursorIndexOfExtractedText)) {
              _tmpExtractedText = null;
            } else {
              _tmpExtractedText = _cursor.getString(_cursorIndexOfExtractedText);
            }
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpSourceApp;
            if (_cursor.isNull(_cursorIndexOfSourceApp)) {
              _tmpSourceApp = null;
            } else {
              _tmpSourceApp = _cursor.getString(_cursorIndexOfSourceApp);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final Double _tmpLatitude;
            if (_cursor.isNull(_cursorIndexOfLatitude)) {
              _tmpLatitude = null;
            } else {
              _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            }
            final Double _tmpLongitude;
            if (_cursor.isNull(_cursorIndexOfLongitude)) {
              _tmpLongitude = null;
            } else {
              _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            }
            final String _tmpLocationLabel;
            if (_cursor.isNull(_cursorIndexOfLocationLabel)) {
              _tmpLocationLabel = null;
            } else {
              _tmpLocationLabel = _cursor.getString(_cursorIndexOfLocationLabel);
            }
            final String _tmpImageUri;
            if (_cursor.isNull(_cursorIndexOfImageUri)) {
              _tmpImageUri = null;
            } else {
              _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            }
            _result = new UniBoxItemEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpUrl,_tmpThumbnailUrl,_tmpExtractedText,_tmpCategory,_tmpSourceApp,_tmpTimestamp,_tmpLatitude,_tmpLongitude,_tmpLocationLabel,_tmpImageUri);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<UniBoxItemEntity>> searchItems(final String query) {
    final String _sql = "\n"
            + "        SELECT unibox_items.* FROM unibox_items\n"
            + "        JOIN unibox_items_fts ON unibox_items.rowid = unibox_items_fts.rowid\n"
            + "        WHERE unibox_items_fts MATCH ?\n"
            + "        ORDER BY unibox_items.timestamp DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"unibox_items",
        "unibox_items_fts"}, new Callable<List<UniBoxItemEntity>>() {
      @Override
      @NonNull
      public List<UniBoxItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
          final int _cursorIndexOfThumbnailUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailUrl");
          final int _cursorIndexOfExtractedText = CursorUtil.getColumnIndexOrThrow(_cursor, "extractedText");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfSourceApp = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceApp");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfLocationLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "locationLabel");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
          final List<UniBoxItemEntity> _result = new ArrayList<UniBoxItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final UniBoxItemEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpUrl;
            if (_cursor.isNull(_cursorIndexOfUrl)) {
              _tmpUrl = null;
            } else {
              _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
            }
            final String _tmpThumbnailUrl;
            if (_cursor.isNull(_cursorIndexOfThumbnailUrl)) {
              _tmpThumbnailUrl = null;
            } else {
              _tmpThumbnailUrl = _cursor.getString(_cursorIndexOfThumbnailUrl);
            }
            final String _tmpExtractedText;
            if (_cursor.isNull(_cursorIndexOfExtractedText)) {
              _tmpExtractedText = null;
            } else {
              _tmpExtractedText = _cursor.getString(_cursorIndexOfExtractedText);
            }
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpSourceApp;
            if (_cursor.isNull(_cursorIndexOfSourceApp)) {
              _tmpSourceApp = null;
            } else {
              _tmpSourceApp = _cursor.getString(_cursorIndexOfSourceApp);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final Double _tmpLatitude;
            if (_cursor.isNull(_cursorIndexOfLatitude)) {
              _tmpLatitude = null;
            } else {
              _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            }
            final Double _tmpLongitude;
            if (_cursor.isNull(_cursorIndexOfLongitude)) {
              _tmpLongitude = null;
            } else {
              _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            }
            final String _tmpLocationLabel;
            if (_cursor.isNull(_cursorIndexOfLocationLabel)) {
              _tmpLocationLabel = null;
            } else {
              _tmpLocationLabel = _cursor.getString(_cursorIndexOfLocationLabel);
            }
            final String _tmpImageUri;
            if (_cursor.isNull(_cursorIndexOfImageUri)) {
              _tmpImageUri = null;
            } else {
              _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            }
            _item = new UniBoxItemEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpUrl,_tmpThumbnailUrl,_tmpExtractedText,_tmpCategory,_tmpSourceApp,_tmpTimestamp,_tmpLatitude,_tmpLongitude,_tmpLocationLabel,_tmpImageUri);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<UniBoxItemEntity>> searchItemsByCategory(final String query,
      final String category) {
    final String _sql = "\n"
            + "        SELECT unibox_items.* FROM unibox_items\n"
            + "        JOIN unibox_items_fts ON unibox_items.rowid = unibox_items_fts.rowid\n"
            + "        WHERE unibox_items_fts MATCH ? AND unibox_items.category = ?\n"
            + "        ORDER BY unibox_items.timestamp DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    _argIndex = 2;
    _statement.bindString(_argIndex, category);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"unibox_items",
        "unibox_items_fts"}, new Callable<List<UniBoxItemEntity>>() {
      @Override
      @NonNull
      public List<UniBoxItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
          final int _cursorIndexOfThumbnailUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailUrl");
          final int _cursorIndexOfExtractedText = CursorUtil.getColumnIndexOrThrow(_cursor, "extractedText");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfSourceApp = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceApp");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfLocationLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "locationLabel");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
          final List<UniBoxItemEntity> _result = new ArrayList<UniBoxItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final UniBoxItemEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpUrl;
            if (_cursor.isNull(_cursorIndexOfUrl)) {
              _tmpUrl = null;
            } else {
              _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
            }
            final String _tmpThumbnailUrl;
            if (_cursor.isNull(_cursorIndexOfThumbnailUrl)) {
              _tmpThumbnailUrl = null;
            } else {
              _tmpThumbnailUrl = _cursor.getString(_cursorIndexOfThumbnailUrl);
            }
            final String _tmpExtractedText;
            if (_cursor.isNull(_cursorIndexOfExtractedText)) {
              _tmpExtractedText = null;
            } else {
              _tmpExtractedText = _cursor.getString(_cursorIndexOfExtractedText);
            }
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpSourceApp;
            if (_cursor.isNull(_cursorIndexOfSourceApp)) {
              _tmpSourceApp = null;
            } else {
              _tmpSourceApp = _cursor.getString(_cursorIndexOfSourceApp);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final Double _tmpLatitude;
            if (_cursor.isNull(_cursorIndexOfLatitude)) {
              _tmpLatitude = null;
            } else {
              _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            }
            final Double _tmpLongitude;
            if (_cursor.isNull(_cursorIndexOfLongitude)) {
              _tmpLongitude = null;
            } else {
              _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            }
            final String _tmpLocationLabel;
            if (_cursor.isNull(_cursorIndexOfLocationLabel)) {
              _tmpLocationLabel = null;
            } else {
              _tmpLocationLabel = _cursor.getString(_cursorIndexOfLocationLabel);
            }
            final String _tmpImageUri;
            if (_cursor.isNull(_cursorIndexOfImageUri)) {
              _tmpImageUri = null;
            } else {
              _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            }
            _item = new UniBoxItemEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpUrl,_tmpThumbnailUrl,_tmpExtractedText,_tmpCategory,_tmpSourceApp,_tmpTimestamp,_tmpLatitude,_tmpLongitude,_tmpLocationLabel,_tmpImageUri);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Integer> getItemCount() {
    final String _sql = "SELECT COUNT(*) FROM unibox_items";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"unibox_items"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
