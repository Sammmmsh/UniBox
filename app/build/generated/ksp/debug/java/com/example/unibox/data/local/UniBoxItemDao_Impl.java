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

  private final SharedSQLiteStatement __preparedStmtOfSetFavorite;

  private final SharedSQLiteStatement __preparedStmtOfSaveToLibrary;

  private final SharedSQLiteStatement __preparedStmtOfApplyOrganizationSuggestions;

  private final SharedSQLiteStatement __preparedStmtOfSetOrganizationReviewed;

  private final SharedSQLiteStatement __preparedStmtOfApplyWebPreview;

  private final SharedSQLiteStatement __preparedStmtOfUpdateWebPreviewState;

  private final SharedSQLiteStatement __preparedStmtOfDeleteItemById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllItems;

  public UniBoxItemDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUniBoxItemEntity = new EntityInsertionAdapter<UniBoxItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `unibox_items` (`id`,`title`,`description`,`url`,`thumbnailUrl`,`extractedText`,`category`,`sourceApp`,`timestamp`,`latitude`,`longitude`,`locationLabel`,`imageUri`,`imageUrisJson`,`status`,`isFavorite`,`snoozedUntil`,`userNote`,`collectionName`,`tagsJson`,`organizationReviewed`,`enrichmentStatus`,`enrichmentProvider`,`enrichmentError`,`canonicalUrl`,`webSiteName`,`webAuthor`,`webPublishedAt`,`webLanguage`,`webReadingTimeMinutes`,`lastEnrichedAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
        statement.bindString(14, entity.getImageUrisJson());
        statement.bindString(15, entity.getStatus());
        final int _tmp = entity.isFavorite() ? 1 : 0;
        statement.bindLong(16, _tmp);
        if (entity.getSnoozedUntil() == null) {
          statement.bindNull(17);
        } else {
          statement.bindLong(17, entity.getSnoozedUntil());
        }
        statement.bindString(18, entity.getUserNote());
        if (entity.getCollectionName() == null) {
          statement.bindNull(19);
        } else {
          statement.bindString(19, entity.getCollectionName());
        }
        statement.bindString(20, entity.getTagsJson());
        final int _tmp_1 = entity.getOrganizationReviewed() ? 1 : 0;
        statement.bindLong(21, _tmp_1);
        statement.bindString(22, entity.getEnrichmentStatus());
        if (entity.getEnrichmentProvider() == null) {
          statement.bindNull(23);
        } else {
          statement.bindString(23, entity.getEnrichmentProvider());
        }
        if (entity.getEnrichmentError() == null) {
          statement.bindNull(24);
        } else {
          statement.bindString(24, entity.getEnrichmentError());
        }
        if (entity.getCanonicalUrl() == null) {
          statement.bindNull(25);
        } else {
          statement.bindString(25, entity.getCanonicalUrl());
        }
        if (entity.getWebSiteName() == null) {
          statement.bindNull(26);
        } else {
          statement.bindString(26, entity.getWebSiteName());
        }
        if (entity.getWebAuthor() == null) {
          statement.bindNull(27);
        } else {
          statement.bindString(27, entity.getWebAuthor());
        }
        if (entity.getWebPublishedAt() == null) {
          statement.bindNull(28);
        } else {
          statement.bindString(28, entity.getWebPublishedAt());
        }
        if (entity.getWebLanguage() == null) {
          statement.bindNull(29);
        } else {
          statement.bindString(29, entity.getWebLanguage());
        }
        if (entity.getWebReadingTimeMinutes() == null) {
          statement.bindNull(30);
        } else {
          statement.bindLong(30, entity.getWebReadingTimeMinutes());
        }
        if (entity.getLastEnrichedAt() == null) {
          statement.bindNull(31);
        } else {
          statement.bindLong(31, entity.getLastEnrichedAt());
        }
        statement.bindLong(32, entity.getUpdatedAt());
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
        return "UPDATE OR ABORT `unibox_items` SET `id` = ?,`title` = ?,`description` = ?,`url` = ?,`thumbnailUrl` = ?,`extractedText` = ?,`category` = ?,`sourceApp` = ?,`timestamp` = ?,`latitude` = ?,`longitude` = ?,`locationLabel` = ?,`imageUri` = ?,`imageUrisJson` = ?,`status` = ?,`isFavorite` = ?,`snoozedUntil` = ?,`userNote` = ?,`collectionName` = ?,`tagsJson` = ?,`organizationReviewed` = ?,`enrichmentStatus` = ?,`enrichmentProvider` = ?,`enrichmentError` = ?,`canonicalUrl` = ?,`webSiteName` = ?,`webAuthor` = ?,`webPublishedAt` = ?,`webLanguage` = ?,`webReadingTimeMinutes` = ?,`lastEnrichedAt` = ?,`updatedAt` = ? WHERE `id` = ?";
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
        statement.bindString(14, entity.getImageUrisJson());
        statement.bindString(15, entity.getStatus());
        final int _tmp = entity.isFavorite() ? 1 : 0;
        statement.bindLong(16, _tmp);
        if (entity.getSnoozedUntil() == null) {
          statement.bindNull(17);
        } else {
          statement.bindLong(17, entity.getSnoozedUntil());
        }
        statement.bindString(18, entity.getUserNote());
        if (entity.getCollectionName() == null) {
          statement.bindNull(19);
        } else {
          statement.bindString(19, entity.getCollectionName());
        }
        statement.bindString(20, entity.getTagsJson());
        final int _tmp_1 = entity.getOrganizationReviewed() ? 1 : 0;
        statement.bindLong(21, _tmp_1);
        statement.bindString(22, entity.getEnrichmentStatus());
        if (entity.getEnrichmentProvider() == null) {
          statement.bindNull(23);
        } else {
          statement.bindString(23, entity.getEnrichmentProvider());
        }
        if (entity.getEnrichmentError() == null) {
          statement.bindNull(24);
        } else {
          statement.bindString(24, entity.getEnrichmentError());
        }
        if (entity.getCanonicalUrl() == null) {
          statement.bindNull(25);
        } else {
          statement.bindString(25, entity.getCanonicalUrl());
        }
        if (entity.getWebSiteName() == null) {
          statement.bindNull(26);
        } else {
          statement.bindString(26, entity.getWebSiteName());
        }
        if (entity.getWebAuthor() == null) {
          statement.bindNull(27);
        } else {
          statement.bindString(27, entity.getWebAuthor());
        }
        if (entity.getWebPublishedAt() == null) {
          statement.bindNull(28);
        } else {
          statement.bindString(28, entity.getWebPublishedAt());
        }
        if (entity.getWebLanguage() == null) {
          statement.bindNull(29);
        } else {
          statement.bindString(29, entity.getWebLanguage());
        }
        if (entity.getWebReadingTimeMinutes() == null) {
          statement.bindNull(30);
        } else {
          statement.bindLong(30, entity.getWebReadingTimeMinutes());
        }
        if (entity.getLastEnrichedAt() == null) {
          statement.bindNull(31);
        } else {
          statement.bindLong(31, entity.getLastEnrichedAt());
        }
        statement.bindLong(32, entity.getUpdatedAt());
        statement.bindLong(33, entity.getId());
      }
    };
    this.__preparedStmtOfSetFavorite = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE unibox_items SET isFavorite = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSaveToLibrary = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE unibox_items SET status = 'SAVED', snoozedUntil = NULL, updatedAt = ?\n"
                + "        WHERE id = ? AND status = 'INBOX'\n"
                + "    ";
        return _query;
      }
    };
    this.__preparedStmtOfApplyOrganizationSuggestions = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE unibox_items SET category = COALESCE(?, category),\n"
                + "            tagsJson = ?, collectionName = COALESCE(?, collectionName),\n"
                + "            organizationReviewed = 1, updatedAt = ?\n"
                + "        WHERE id = ? AND category = ? AND tagsJson = ?\n"
                + "            AND collectionName IS ? AND organizationReviewed = 0\n"
                + "    ";
        return _query;
      }
    };
    this.__preparedStmtOfSetOrganizationReviewed = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE unibox_items SET organizationReviewed = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfApplyWebPreview = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE unibox_items SET\n"
                + "            title = CASE WHEN TRIM(title) = '' OR title = url\n"
                + "                THEN COALESCE(?, title) ELSE title END,\n"
                + "            description = CASE WHEN TRIM(description) = '' OR description = url\n"
                + "                THEN COALESCE(?, description) ELSE description END,\n"
                + "            thumbnailUrl = COALESCE(?, thumbnailUrl),\n"
                + "            extractedText = CASE WHEN imageUri IS NULL AND imageUrisJson = '[]'\n"
                + "                THEN COALESCE(?, extractedText) ELSE extractedText END,\n"
                + "            enrichmentStatus = ?,\n"
                + "            enrichmentProvider = ?,\n"
                + "            enrichmentError = ?,\n"
                + "            canonicalUrl = COALESCE(?, canonicalUrl),\n"
                + "            webSiteName = COALESCE(?, webSiteName),\n"
                + "            webAuthor = COALESCE(?, webAuthor),\n"
                + "            webPublishedAt = COALESCE(?, webPublishedAt),\n"
                + "            webLanguage = COALESCE(?, webLanguage),\n"
                + "            webReadingTimeMinutes = COALESCE(?, webReadingTimeMinutes),\n"
                + "            lastEnrichedAt = ?\n"
                + "        WHERE id = ? AND url = ?\n"
                + "    ";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateWebPreviewState = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE unibox_items SET enrichmentStatus = ?,\n"
                + "            enrichmentError = ?, lastEnrichedAt = ?\n"
                + "        WHERE id = ? AND url = ?\n"
                + "    ";
        return _query;
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
    this.__preparedStmtOfDeleteAllItems = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM unibox_items";
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
  public Object setFavorite(final long id, final boolean favorite, final long updatedAt,
      final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetFavorite.acquire();
        int _argIndex = 1;
        final int _tmp = favorite ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, updatedAt);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            final Integer _result = _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetFavorite.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object saveToLibrary(final long id, final long updatedAt,
      final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSaveToLibrary.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, updatedAt);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            final Integer _result = _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSaveToLibrary.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object applyOrganizationSuggestions(final long id, final String category,
      final String tagsJson, final String collectionName, final String expectedCategory,
      final String expectedTagsJson, final String expectedCollectionName, final long updatedAt,
      final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfApplyOrganizationSuggestions.acquire();
        int _argIndex = 1;
        if (category == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, category);
        }
        _argIndex = 2;
        _stmt.bindString(_argIndex, tagsJson);
        _argIndex = 3;
        if (collectionName == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, collectionName);
        }
        _argIndex = 4;
        _stmt.bindLong(_argIndex, updatedAt);
        _argIndex = 5;
        _stmt.bindLong(_argIndex, id);
        _argIndex = 6;
        _stmt.bindString(_argIndex, expectedCategory);
        _argIndex = 7;
        _stmt.bindString(_argIndex, expectedTagsJson);
        _argIndex = 8;
        if (expectedCollectionName == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, expectedCollectionName);
        }
        try {
          __db.beginTransaction();
          try {
            final Integer _result = _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfApplyOrganizationSuggestions.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setOrganizationReviewed(final long id, final boolean reviewed,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetOrganizationReviewed.acquire();
        int _argIndex = 1;
        final int _tmp = reviewed ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
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
          __preparedStmtOfSetOrganizationReviewed.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object applyWebPreview(final long id, final String expectedUrl, final String pageTitle,
      final String pageDescription, final String imageUrl, final String pageContent,
      final String previewStatus, final String provider, final String error, final String pageUrl,
      final String siteName, final String author, final String publishedAt, final String language,
      final Integer readingTimeMinutes, final long enrichedAt,
      final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfApplyWebPreview.acquire();
        int _argIndex = 1;
        if (pageTitle == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, pageTitle);
        }
        _argIndex = 2;
        if (pageDescription == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, pageDescription);
        }
        _argIndex = 3;
        if (imageUrl == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, imageUrl);
        }
        _argIndex = 4;
        if (pageContent == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, pageContent);
        }
        _argIndex = 5;
        _stmt.bindString(_argIndex, previewStatus);
        _argIndex = 6;
        _stmt.bindString(_argIndex, provider);
        _argIndex = 7;
        if (error == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, error);
        }
        _argIndex = 8;
        if (pageUrl == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, pageUrl);
        }
        _argIndex = 9;
        if (siteName == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, siteName);
        }
        _argIndex = 10;
        if (author == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, author);
        }
        _argIndex = 11;
        if (publishedAt == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, publishedAt);
        }
        _argIndex = 12;
        if (language == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, language);
        }
        _argIndex = 13;
        if (readingTimeMinutes == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, readingTimeMinutes);
        }
        _argIndex = 14;
        _stmt.bindLong(_argIndex, enrichedAt);
        _argIndex = 15;
        _stmt.bindLong(_argIndex, id);
        _argIndex = 16;
        _stmt.bindString(_argIndex, expectedUrl);
        try {
          __db.beginTransaction();
          try {
            final Integer _result = _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfApplyWebPreview.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateWebPreviewState(final long id, final String expectedUrl,
      final String previewStatus, final String error, final long attemptedAt,
      final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateWebPreviewState.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, previewStatus);
        _argIndex = 2;
        if (error == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, error);
        }
        _argIndex = 3;
        _stmt.bindLong(_argIndex, attemptedAt);
        _argIndex = 4;
        _stmt.bindLong(_argIndex, id);
        _argIndex = 5;
        _stmt.bindString(_argIndex, expectedUrl);
        try {
          __db.beginTransaction();
          try {
            final Integer _result = _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateWebPreviewState.release(_stmt);
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
  public Object deleteAllItems(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllItems.acquire();
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
          __preparedStmtOfDeleteAllItems.release(_stmt);
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
          final int _cursorIndexOfImageUrisJson = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrisJson");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfSnoozedUntil = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozedUntil");
          final int _cursorIndexOfUserNote = CursorUtil.getColumnIndexOrThrow(_cursor, "userNote");
          final int _cursorIndexOfCollectionName = CursorUtil.getColumnIndexOrThrow(_cursor, "collectionName");
          final int _cursorIndexOfTagsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "tagsJson");
          final int _cursorIndexOfOrganizationReviewed = CursorUtil.getColumnIndexOrThrow(_cursor, "organizationReviewed");
          final int _cursorIndexOfEnrichmentStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "enrichmentStatus");
          final int _cursorIndexOfEnrichmentProvider = CursorUtil.getColumnIndexOrThrow(_cursor, "enrichmentProvider");
          final int _cursorIndexOfEnrichmentError = CursorUtil.getColumnIndexOrThrow(_cursor, "enrichmentError");
          final int _cursorIndexOfCanonicalUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "canonicalUrl");
          final int _cursorIndexOfWebSiteName = CursorUtil.getColumnIndexOrThrow(_cursor, "webSiteName");
          final int _cursorIndexOfWebAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "webAuthor");
          final int _cursorIndexOfWebPublishedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "webPublishedAt");
          final int _cursorIndexOfWebLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "webLanguage");
          final int _cursorIndexOfWebReadingTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "webReadingTimeMinutes");
          final int _cursorIndexOfLastEnrichedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastEnrichedAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
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
            final String _tmpImageUrisJson;
            _tmpImageUrisJson = _cursor.getString(_cursorIndexOfImageUrisJson);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final Long _tmpSnoozedUntil;
            if (_cursor.isNull(_cursorIndexOfSnoozedUntil)) {
              _tmpSnoozedUntil = null;
            } else {
              _tmpSnoozedUntil = _cursor.getLong(_cursorIndexOfSnoozedUntil);
            }
            final String _tmpUserNote;
            _tmpUserNote = _cursor.getString(_cursorIndexOfUserNote);
            final String _tmpCollectionName;
            if (_cursor.isNull(_cursorIndexOfCollectionName)) {
              _tmpCollectionName = null;
            } else {
              _tmpCollectionName = _cursor.getString(_cursorIndexOfCollectionName);
            }
            final String _tmpTagsJson;
            _tmpTagsJson = _cursor.getString(_cursorIndexOfTagsJson);
            final boolean _tmpOrganizationReviewed;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfOrganizationReviewed);
            _tmpOrganizationReviewed = _tmp_1 != 0;
            final String _tmpEnrichmentStatus;
            _tmpEnrichmentStatus = _cursor.getString(_cursorIndexOfEnrichmentStatus);
            final String _tmpEnrichmentProvider;
            if (_cursor.isNull(_cursorIndexOfEnrichmentProvider)) {
              _tmpEnrichmentProvider = null;
            } else {
              _tmpEnrichmentProvider = _cursor.getString(_cursorIndexOfEnrichmentProvider);
            }
            final String _tmpEnrichmentError;
            if (_cursor.isNull(_cursorIndexOfEnrichmentError)) {
              _tmpEnrichmentError = null;
            } else {
              _tmpEnrichmentError = _cursor.getString(_cursorIndexOfEnrichmentError);
            }
            final String _tmpCanonicalUrl;
            if (_cursor.isNull(_cursorIndexOfCanonicalUrl)) {
              _tmpCanonicalUrl = null;
            } else {
              _tmpCanonicalUrl = _cursor.getString(_cursorIndexOfCanonicalUrl);
            }
            final String _tmpWebSiteName;
            if (_cursor.isNull(_cursorIndexOfWebSiteName)) {
              _tmpWebSiteName = null;
            } else {
              _tmpWebSiteName = _cursor.getString(_cursorIndexOfWebSiteName);
            }
            final String _tmpWebAuthor;
            if (_cursor.isNull(_cursorIndexOfWebAuthor)) {
              _tmpWebAuthor = null;
            } else {
              _tmpWebAuthor = _cursor.getString(_cursorIndexOfWebAuthor);
            }
            final String _tmpWebPublishedAt;
            if (_cursor.isNull(_cursorIndexOfWebPublishedAt)) {
              _tmpWebPublishedAt = null;
            } else {
              _tmpWebPublishedAt = _cursor.getString(_cursorIndexOfWebPublishedAt);
            }
            final String _tmpWebLanguage;
            if (_cursor.isNull(_cursorIndexOfWebLanguage)) {
              _tmpWebLanguage = null;
            } else {
              _tmpWebLanguage = _cursor.getString(_cursorIndexOfWebLanguage);
            }
            final Integer _tmpWebReadingTimeMinutes;
            if (_cursor.isNull(_cursorIndexOfWebReadingTimeMinutes)) {
              _tmpWebReadingTimeMinutes = null;
            } else {
              _tmpWebReadingTimeMinutes = _cursor.getInt(_cursorIndexOfWebReadingTimeMinutes);
            }
            final Long _tmpLastEnrichedAt;
            if (_cursor.isNull(_cursorIndexOfLastEnrichedAt)) {
              _tmpLastEnrichedAt = null;
            } else {
              _tmpLastEnrichedAt = _cursor.getLong(_cursorIndexOfLastEnrichedAt);
            }
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new UniBoxItemEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpUrl,_tmpThumbnailUrl,_tmpExtractedText,_tmpCategory,_tmpSourceApp,_tmpTimestamp,_tmpLatitude,_tmpLongitude,_tmpLocationLabel,_tmpImageUri,_tmpImageUrisJson,_tmpStatus,_tmpIsFavorite,_tmpSnoozedUntil,_tmpUserNote,_tmpCollectionName,_tmpTagsJson,_tmpOrganizationReviewed,_tmpEnrichmentStatus,_tmpEnrichmentProvider,_tmpEnrichmentError,_tmpCanonicalUrl,_tmpWebSiteName,_tmpWebAuthor,_tmpWebPublishedAt,_tmpWebLanguage,_tmpWebReadingTimeMinutes,_tmpLastEnrichedAt,_tmpUpdatedAt);
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
          final int _cursorIndexOfImageUrisJson = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrisJson");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfSnoozedUntil = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozedUntil");
          final int _cursorIndexOfUserNote = CursorUtil.getColumnIndexOrThrow(_cursor, "userNote");
          final int _cursorIndexOfCollectionName = CursorUtil.getColumnIndexOrThrow(_cursor, "collectionName");
          final int _cursorIndexOfTagsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "tagsJson");
          final int _cursorIndexOfOrganizationReviewed = CursorUtil.getColumnIndexOrThrow(_cursor, "organizationReviewed");
          final int _cursorIndexOfEnrichmentStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "enrichmentStatus");
          final int _cursorIndexOfEnrichmentProvider = CursorUtil.getColumnIndexOrThrow(_cursor, "enrichmentProvider");
          final int _cursorIndexOfEnrichmentError = CursorUtil.getColumnIndexOrThrow(_cursor, "enrichmentError");
          final int _cursorIndexOfCanonicalUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "canonicalUrl");
          final int _cursorIndexOfWebSiteName = CursorUtil.getColumnIndexOrThrow(_cursor, "webSiteName");
          final int _cursorIndexOfWebAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "webAuthor");
          final int _cursorIndexOfWebPublishedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "webPublishedAt");
          final int _cursorIndexOfWebLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "webLanguage");
          final int _cursorIndexOfWebReadingTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "webReadingTimeMinutes");
          final int _cursorIndexOfLastEnrichedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastEnrichedAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
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
            final String _tmpImageUrisJson;
            _tmpImageUrisJson = _cursor.getString(_cursorIndexOfImageUrisJson);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final Long _tmpSnoozedUntil;
            if (_cursor.isNull(_cursorIndexOfSnoozedUntil)) {
              _tmpSnoozedUntil = null;
            } else {
              _tmpSnoozedUntil = _cursor.getLong(_cursorIndexOfSnoozedUntil);
            }
            final String _tmpUserNote;
            _tmpUserNote = _cursor.getString(_cursorIndexOfUserNote);
            final String _tmpCollectionName;
            if (_cursor.isNull(_cursorIndexOfCollectionName)) {
              _tmpCollectionName = null;
            } else {
              _tmpCollectionName = _cursor.getString(_cursorIndexOfCollectionName);
            }
            final String _tmpTagsJson;
            _tmpTagsJson = _cursor.getString(_cursorIndexOfTagsJson);
            final boolean _tmpOrganizationReviewed;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfOrganizationReviewed);
            _tmpOrganizationReviewed = _tmp_1 != 0;
            final String _tmpEnrichmentStatus;
            _tmpEnrichmentStatus = _cursor.getString(_cursorIndexOfEnrichmentStatus);
            final String _tmpEnrichmentProvider;
            if (_cursor.isNull(_cursorIndexOfEnrichmentProvider)) {
              _tmpEnrichmentProvider = null;
            } else {
              _tmpEnrichmentProvider = _cursor.getString(_cursorIndexOfEnrichmentProvider);
            }
            final String _tmpEnrichmentError;
            if (_cursor.isNull(_cursorIndexOfEnrichmentError)) {
              _tmpEnrichmentError = null;
            } else {
              _tmpEnrichmentError = _cursor.getString(_cursorIndexOfEnrichmentError);
            }
            final String _tmpCanonicalUrl;
            if (_cursor.isNull(_cursorIndexOfCanonicalUrl)) {
              _tmpCanonicalUrl = null;
            } else {
              _tmpCanonicalUrl = _cursor.getString(_cursorIndexOfCanonicalUrl);
            }
            final String _tmpWebSiteName;
            if (_cursor.isNull(_cursorIndexOfWebSiteName)) {
              _tmpWebSiteName = null;
            } else {
              _tmpWebSiteName = _cursor.getString(_cursorIndexOfWebSiteName);
            }
            final String _tmpWebAuthor;
            if (_cursor.isNull(_cursorIndexOfWebAuthor)) {
              _tmpWebAuthor = null;
            } else {
              _tmpWebAuthor = _cursor.getString(_cursorIndexOfWebAuthor);
            }
            final String _tmpWebPublishedAt;
            if (_cursor.isNull(_cursorIndexOfWebPublishedAt)) {
              _tmpWebPublishedAt = null;
            } else {
              _tmpWebPublishedAt = _cursor.getString(_cursorIndexOfWebPublishedAt);
            }
            final String _tmpWebLanguage;
            if (_cursor.isNull(_cursorIndexOfWebLanguage)) {
              _tmpWebLanguage = null;
            } else {
              _tmpWebLanguage = _cursor.getString(_cursorIndexOfWebLanguage);
            }
            final Integer _tmpWebReadingTimeMinutes;
            if (_cursor.isNull(_cursorIndexOfWebReadingTimeMinutes)) {
              _tmpWebReadingTimeMinutes = null;
            } else {
              _tmpWebReadingTimeMinutes = _cursor.getInt(_cursorIndexOfWebReadingTimeMinutes);
            }
            final Long _tmpLastEnrichedAt;
            if (_cursor.isNull(_cursorIndexOfLastEnrichedAt)) {
              _tmpLastEnrichedAt = null;
            } else {
              _tmpLastEnrichedAt = _cursor.getLong(_cursorIndexOfLastEnrichedAt);
            }
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new UniBoxItemEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpUrl,_tmpThumbnailUrl,_tmpExtractedText,_tmpCategory,_tmpSourceApp,_tmpTimestamp,_tmpLatitude,_tmpLongitude,_tmpLocationLabel,_tmpImageUri,_tmpImageUrisJson,_tmpStatus,_tmpIsFavorite,_tmpSnoozedUntil,_tmpUserNote,_tmpCollectionName,_tmpTagsJson,_tmpOrganizationReviewed,_tmpEnrichmentStatus,_tmpEnrichmentProvider,_tmpEnrichmentError,_tmpCanonicalUrl,_tmpWebSiteName,_tmpWebAuthor,_tmpWebPublishedAt,_tmpWebLanguage,_tmpWebReadingTimeMinutes,_tmpLastEnrichedAt,_tmpUpdatedAt);
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
          final int _cursorIndexOfImageUrisJson = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrisJson");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfSnoozedUntil = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozedUntil");
          final int _cursorIndexOfUserNote = CursorUtil.getColumnIndexOrThrow(_cursor, "userNote");
          final int _cursorIndexOfCollectionName = CursorUtil.getColumnIndexOrThrow(_cursor, "collectionName");
          final int _cursorIndexOfTagsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "tagsJson");
          final int _cursorIndexOfOrganizationReviewed = CursorUtil.getColumnIndexOrThrow(_cursor, "organizationReviewed");
          final int _cursorIndexOfEnrichmentStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "enrichmentStatus");
          final int _cursorIndexOfEnrichmentProvider = CursorUtil.getColumnIndexOrThrow(_cursor, "enrichmentProvider");
          final int _cursorIndexOfEnrichmentError = CursorUtil.getColumnIndexOrThrow(_cursor, "enrichmentError");
          final int _cursorIndexOfCanonicalUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "canonicalUrl");
          final int _cursorIndexOfWebSiteName = CursorUtil.getColumnIndexOrThrow(_cursor, "webSiteName");
          final int _cursorIndexOfWebAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "webAuthor");
          final int _cursorIndexOfWebPublishedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "webPublishedAt");
          final int _cursorIndexOfWebLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "webLanguage");
          final int _cursorIndexOfWebReadingTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "webReadingTimeMinutes");
          final int _cursorIndexOfLastEnrichedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastEnrichedAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
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
            final String _tmpImageUrisJson;
            _tmpImageUrisJson = _cursor.getString(_cursorIndexOfImageUrisJson);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final Long _tmpSnoozedUntil;
            if (_cursor.isNull(_cursorIndexOfSnoozedUntil)) {
              _tmpSnoozedUntil = null;
            } else {
              _tmpSnoozedUntil = _cursor.getLong(_cursorIndexOfSnoozedUntil);
            }
            final String _tmpUserNote;
            _tmpUserNote = _cursor.getString(_cursorIndexOfUserNote);
            final String _tmpCollectionName;
            if (_cursor.isNull(_cursorIndexOfCollectionName)) {
              _tmpCollectionName = null;
            } else {
              _tmpCollectionName = _cursor.getString(_cursorIndexOfCollectionName);
            }
            final String _tmpTagsJson;
            _tmpTagsJson = _cursor.getString(_cursorIndexOfTagsJson);
            final boolean _tmpOrganizationReviewed;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfOrganizationReviewed);
            _tmpOrganizationReviewed = _tmp_1 != 0;
            final String _tmpEnrichmentStatus;
            _tmpEnrichmentStatus = _cursor.getString(_cursorIndexOfEnrichmentStatus);
            final String _tmpEnrichmentProvider;
            if (_cursor.isNull(_cursorIndexOfEnrichmentProvider)) {
              _tmpEnrichmentProvider = null;
            } else {
              _tmpEnrichmentProvider = _cursor.getString(_cursorIndexOfEnrichmentProvider);
            }
            final String _tmpEnrichmentError;
            if (_cursor.isNull(_cursorIndexOfEnrichmentError)) {
              _tmpEnrichmentError = null;
            } else {
              _tmpEnrichmentError = _cursor.getString(_cursorIndexOfEnrichmentError);
            }
            final String _tmpCanonicalUrl;
            if (_cursor.isNull(_cursorIndexOfCanonicalUrl)) {
              _tmpCanonicalUrl = null;
            } else {
              _tmpCanonicalUrl = _cursor.getString(_cursorIndexOfCanonicalUrl);
            }
            final String _tmpWebSiteName;
            if (_cursor.isNull(_cursorIndexOfWebSiteName)) {
              _tmpWebSiteName = null;
            } else {
              _tmpWebSiteName = _cursor.getString(_cursorIndexOfWebSiteName);
            }
            final String _tmpWebAuthor;
            if (_cursor.isNull(_cursorIndexOfWebAuthor)) {
              _tmpWebAuthor = null;
            } else {
              _tmpWebAuthor = _cursor.getString(_cursorIndexOfWebAuthor);
            }
            final String _tmpWebPublishedAt;
            if (_cursor.isNull(_cursorIndexOfWebPublishedAt)) {
              _tmpWebPublishedAt = null;
            } else {
              _tmpWebPublishedAt = _cursor.getString(_cursorIndexOfWebPublishedAt);
            }
            final String _tmpWebLanguage;
            if (_cursor.isNull(_cursorIndexOfWebLanguage)) {
              _tmpWebLanguage = null;
            } else {
              _tmpWebLanguage = _cursor.getString(_cursorIndexOfWebLanguage);
            }
            final Integer _tmpWebReadingTimeMinutes;
            if (_cursor.isNull(_cursorIndexOfWebReadingTimeMinutes)) {
              _tmpWebReadingTimeMinutes = null;
            } else {
              _tmpWebReadingTimeMinutes = _cursor.getInt(_cursorIndexOfWebReadingTimeMinutes);
            }
            final Long _tmpLastEnrichedAt;
            if (_cursor.isNull(_cursorIndexOfLastEnrichedAt)) {
              _tmpLastEnrichedAt = null;
            } else {
              _tmpLastEnrichedAt = _cursor.getLong(_cursorIndexOfLastEnrichedAt);
            }
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new UniBoxItemEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpUrl,_tmpThumbnailUrl,_tmpExtractedText,_tmpCategory,_tmpSourceApp,_tmpTimestamp,_tmpLatitude,_tmpLongitude,_tmpLocationLabel,_tmpImageUri,_tmpImageUrisJson,_tmpStatus,_tmpIsFavorite,_tmpSnoozedUntil,_tmpUserNote,_tmpCollectionName,_tmpTagsJson,_tmpOrganizationReviewed,_tmpEnrichmentStatus,_tmpEnrichmentProvider,_tmpEnrichmentError,_tmpCanonicalUrl,_tmpWebSiteName,_tmpWebAuthor,_tmpWebPublishedAt,_tmpWebLanguage,_tmpWebReadingTimeMinutes,_tmpLastEnrichedAt,_tmpUpdatedAt);
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
          final int _cursorIndexOfImageUrisJson = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrisJson");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfSnoozedUntil = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozedUntil");
          final int _cursorIndexOfUserNote = CursorUtil.getColumnIndexOrThrow(_cursor, "userNote");
          final int _cursorIndexOfCollectionName = CursorUtil.getColumnIndexOrThrow(_cursor, "collectionName");
          final int _cursorIndexOfTagsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "tagsJson");
          final int _cursorIndexOfOrganizationReviewed = CursorUtil.getColumnIndexOrThrow(_cursor, "organizationReviewed");
          final int _cursorIndexOfEnrichmentStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "enrichmentStatus");
          final int _cursorIndexOfEnrichmentProvider = CursorUtil.getColumnIndexOrThrow(_cursor, "enrichmentProvider");
          final int _cursorIndexOfEnrichmentError = CursorUtil.getColumnIndexOrThrow(_cursor, "enrichmentError");
          final int _cursorIndexOfCanonicalUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "canonicalUrl");
          final int _cursorIndexOfWebSiteName = CursorUtil.getColumnIndexOrThrow(_cursor, "webSiteName");
          final int _cursorIndexOfWebAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "webAuthor");
          final int _cursorIndexOfWebPublishedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "webPublishedAt");
          final int _cursorIndexOfWebLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "webLanguage");
          final int _cursorIndexOfWebReadingTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "webReadingTimeMinutes");
          final int _cursorIndexOfLastEnrichedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastEnrichedAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
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
            final String _tmpImageUrisJson;
            _tmpImageUrisJson = _cursor.getString(_cursorIndexOfImageUrisJson);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final Long _tmpSnoozedUntil;
            if (_cursor.isNull(_cursorIndexOfSnoozedUntil)) {
              _tmpSnoozedUntil = null;
            } else {
              _tmpSnoozedUntil = _cursor.getLong(_cursorIndexOfSnoozedUntil);
            }
            final String _tmpUserNote;
            _tmpUserNote = _cursor.getString(_cursorIndexOfUserNote);
            final String _tmpCollectionName;
            if (_cursor.isNull(_cursorIndexOfCollectionName)) {
              _tmpCollectionName = null;
            } else {
              _tmpCollectionName = _cursor.getString(_cursorIndexOfCollectionName);
            }
            final String _tmpTagsJson;
            _tmpTagsJson = _cursor.getString(_cursorIndexOfTagsJson);
            final boolean _tmpOrganizationReviewed;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfOrganizationReviewed);
            _tmpOrganizationReviewed = _tmp_1 != 0;
            final String _tmpEnrichmentStatus;
            _tmpEnrichmentStatus = _cursor.getString(_cursorIndexOfEnrichmentStatus);
            final String _tmpEnrichmentProvider;
            if (_cursor.isNull(_cursorIndexOfEnrichmentProvider)) {
              _tmpEnrichmentProvider = null;
            } else {
              _tmpEnrichmentProvider = _cursor.getString(_cursorIndexOfEnrichmentProvider);
            }
            final String _tmpEnrichmentError;
            if (_cursor.isNull(_cursorIndexOfEnrichmentError)) {
              _tmpEnrichmentError = null;
            } else {
              _tmpEnrichmentError = _cursor.getString(_cursorIndexOfEnrichmentError);
            }
            final String _tmpCanonicalUrl;
            if (_cursor.isNull(_cursorIndexOfCanonicalUrl)) {
              _tmpCanonicalUrl = null;
            } else {
              _tmpCanonicalUrl = _cursor.getString(_cursorIndexOfCanonicalUrl);
            }
            final String _tmpWebSiteName;
            if (_cursor.isNull(_cursorIndexOfWebSiteName)) {
              _tmpWebSiteName = null;
            } else {
              _tmpWebSiteName = _cursor.getString(_cursorIndexOfWebSiteName);
            }
            final String _tmpWebAuthor;
            if (_cursor.isNull(_cursorIndexOfWebAuthor)) {
              _tmpWebAuthor = null;
            } else {
              _tmpWebAuthor = _cursor.getString(_cursorIndexOfWebAuthor);
            }
            final String _tmpWebPublishedAt;
            if (_cursor.isNull(_cursorIndexOfWebPublishedAt)) {
              _tmpWebPublishedAt = null;
            } else {
              _tmpWebPublishedAt = _cursor.getString(_cursorIndexOfWebPublishedAt);
            }
            final String _tmpWebLanguage;
            if (_cursor.isNull(_cursorIndexOfWebLanguage)) {
              _tmpWebLanguage = null;
            } else {
              _tmpWebLanguage = _cursor.getString(_cursorIndexOfWebLanguage);
            }
            final Integer _tmpWebReadingTimeMinutes;
            if (_cursor.isNull(_cursorIndexOfWebReadingTimeMinutes)) {
              _tmpWebReadingTimeMinutes = null;
            } else {
              _tmpWebReadingTimeMinutes = _cursor.getInt(_cursorIndexOfWebReadingTimeMinutes);
            }
            final Long _tmpLastEnrichedAt;
            if (_cursor.isNull(_cursorIndexOfLastEnrichedAt)) {
              _tmpLastEnrichedAt = null;
            } else {
              _tmpLastEnrichedAt = _cursor.getLong(_cursorIndexOfLastEnrichedAt);
            }
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new UniBoxItemEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpUrl,_tmpThumbnailUrl,_tmpExtractedText,_tmpCategory,_tmpSourceApp,_tmpTimestamp,_tmpLatitude,_tmpLongitude,_tmpLocationLabel,_tmpImageUri,_tmpImageUrisJson,_tmpStatus,_tmpIsFavorite,_tmpSnoozedUntil,_tmpUserNote,_tmpCollectionName,_tmpTagsJson,_tmpOrganizationReviewed,_tmpEnrichmentStatus,_tmpEnrichmentProvider,_tmpEnrichmentError,_tmpCanonicalUrl,_tmpWebSiteName,_tmpWebAuthor,_tmpWebPublishedAt,_tmpWebLanguage,_tmpWebReadingTimeMinutes,_tmpLastEnrichedAt,_tmpUpdatedAt);
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
          final int _cursorIndexOfImageUrisJson = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrisJson");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfSnoozedUntil = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozedUntil");
          final int _cursorIndexOfUserNote = CursorUtil.getColumnIndexOrThrow(_cursor, "userNote");
          final int _cursorIndexOfCollectionName = CursorUtil.getColumnIndexOrThrow(_cursor, "collectionName");
          final int _cursorIndexOfTagsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "tagsJson");
          final int _cursorIndexOfOrganizationReviewed = CursorUtil.getColumnIndexOrThrow(_cursor, "organizationReviewed");
          final int _cursorIndexOfEnrichmentStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "enrichmentStatus");
          final int _cursorIndexOfEnrichmentProvider = CursorUtil.getColumnIndexOrThrow(_cursor, "enrichmentProvider");
          final int _cursorIndexOfEnrichmentError = CursorUtil.getColumnIndexOrThrow(_cursor, "enrichmentError");
          final int _cursorIndexOfCanonicalUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "canonicalUrl");
          final int _cursorIndexOfWebSiteName = CursorUtil.getColumnIndexOrThrow(_cursor, "webSiteName");
          final int _cursorIndexOfWebAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "webAuthor");
          final int _cursorIndexOfWebPublishedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "webPublishedAt");
          final int _cursorIndexOfWebLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "webLanguage");
          final int _cursorIndexOfWebReadingTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "webReadingTimeMinutes");
          final int _cursorIndexOfLastEnrichedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastEnrichedAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
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
            final String _tmpImageUrisJson;
            _tmpImageUrisJson = _cursor.getString(_cursorIndexOfImageUrisJson);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final Long _tmpSnoozedUntil;
            if (_cursor.isNull(_cursorIndexOfSnoozedUntil)) {
              _tmpSnoozedUntil = null;
            } else {
              _tmpSnoozedUntil = _cursor.getLong(_cursorIndexOfSnoozedUntil);
            }
            final String _tmpUserNote;
            _tmpUserNote = _cursor.getString(_cursorIndexOfUserNote);
            final String _tmpCollectionName;
            if (_cursor.isNull(_cursorIndexOfCollectionName)) {
              _tmpCollectionName = null;
            } else {
              _tmpCollectionName = _cursor.getString(_cursorIndexOfCollectionName);
            }
            final String _tmpTagsJson;
            _tmpTagsJson = _cursor.getString(_cursorIndexOfTagsJson);
            final boolean _tmpOrganizationReviewed;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfOrganizationReviewed);
            _tmpOrganizationReviewed = _tmp_1 != 0;
            final String _tmpEnrichmentStatus;
            _tmpEnrichmentStatus = _cursor.getString(_cursorIndexOfEnrichmentStatus);
            final String _tmpEnrichmentProvider;
            if (_cursor.isNull(_cursorIndexOfEnrichmentProvider)) {
              _tmpEnrichmentProvider = null;
            } else {
              _tmpEnrichmentProvider = _cursor.getString(_cursorIndexOfEnrichmentProvider);
            }
            final String _tmpEnrichmentError;
            if (_cursor.isNull(_cursorIndexOfEnrichmentError)) {
              _tmpEnrichmentError = null;
            } else {
              _tmpEnrichmentError = _cursor.getString(_cursorIndexOfEnrichmentError);
            }
            final String _tmpCanonicalUrl;
            if (_cursor.isNull(_cursorIndexOfCanonicalUrl)) {
              _tmpCanonicalUrl = null;
            } else {
              _tmpCanonicalUrl = _cursor.getString(_cursorIndexOfCanonicalUrl);
            }
            final String _tmpWebSiteName;
            if (_cursor.isNull(_cursorIndexOfWebSiteName)) {
              _tmpWebSiteName = null;
            } else {
              _tmpWebSiteName = _cursor.getString(_cursorIndexOfWebSiteName);
            }
            final String _tmpWebAuthor;
            if (_cursor.isNull(_cursorIndexOfWebAuthor)) {
              _tmpWebAuthor = null;
            } else {
              _tmpWebAuthor = _cursor.getString(_cursorIndexOfWebAuthor);
            }
            final String _tmpWebPublishedAt;
            if (_cursor.isNull(_cursorIndexOfWebPublishedAt)) {
              _tmpWebPublishedAt = null;
            } else {
              _tmpWebPublishedAt = _cursor.getString(_cursorIndexOfWebPublishedAt);
            }
            final String _tmpWebLanguage;
            if (_cursor.isNull(_cursorIndexOfWebLanguage)) {
              _tmpWebLanguage = null;
            } else {
              _tmpWebLanguage = _cursor.getString(_cursorIndexOfWebLanguage);
            }
            final Integer _tmpWebReadingTimeMinutes;
            if (_cursor.isNull(_cursorIndexOfWebReadingTimeMinutes)) {
              _tmpWebReadingTimeMinutes = null;
            } else {
              _tmpWebReadingTimeMinutes = _cursor.getInt(_cursorIndexOfWebReadingTimeMinutes);
            }
            final Long _tmpLastEnrichedAt;
            if (_cursor.isNull(_cursorIndexOfLastEnrichedAt)) {
              _tmpLastEnrichedAt = null;
            } else {
              _tmpLastEnrichedAt = _cursor.getLong(_cursorIndexOfLastEnrichedAt);
            }
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new UniBoxItemEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpUrl,_tmpThumbnailUrl,_tmpExtractedText,_tmpCategory,_tmpSourceApp,_tmpTimestamp,_tmpLatitude,_tmpLongitude,_tmpLocationLabel,_tmpImageUri,_tmpImageUrisJson,_tmpStatus,_tmpIsFavorite,_tmpSnoozedUntil,_tmpUserNote,_tmpCollectionName,_tmpTagsJson,_tmpOrganizationReviewed,_tmpEnrichmentStatus,_tmpEnrichmentProvider,_tmpEnrichmentError,_tmpCanonicalUrl,_tmpWebSiteName,_tmpWebAuthor,_tmpWebPublishedAt,_tmpWebLanguage,_tmpWebReadingTimeMinutes,_tmpLastEnrichedAt,_tmpUpdatedAt);
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
          final int _cursorIndexOfImageUrisJson = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrisJson");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfSnoozedUntil = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozedUntil");
          final int _cursorIndexOfUserNote = CursorUtil.getColumnIndexOrThrow(_cursor, "userNote");
          final int _cursorIndexOfCollectionName = CursorUtil.getColumnIndexOrThrow(_cursor, "collectionName");
          final int _cursorIndexOfTagsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "tagsJson");
          final int _cursorIndexOfOrganizationReviewed = CursorUtil.getColumnIndexOrThrow(_cursor, "organizationReviewed");
          final int _cursorIndexOfEnrichmentStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "enrichmentStatus");
          final int _cursorIndexOfEnrichmentProvider = CursorUtil.getColumnIndexOrThrow(_cursor, "enrichmentProvider");
          final int _cursorIndexOfEnrichmentError = CursorUtil.getColumnIndexOrThrow(_cursor, "enrichmentError");
          final int _cursorIndexOfCanonicalUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "canonicalUrl");
          final int _cursorIndexOfWebSiteName = CursorUtil.getColumnIndexOrThrow(_cursor, "webSiteName");
          final int _cursorIndexOfWebAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "webAuthor");
          final int _cursorIndexOfWebPublishedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "webPublishedAt");
          final int _cursorIndexOfWebLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "webLanguage");
          final int _cursorIndexOfWebReadingTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "webReadingTimeMinutes");
          final int _cursorIndexOfLastEnrichedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastEnrichedAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
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
            final String _tmpImageUrisJson;
            _tmpImageUrisJson = _cursor.getString(_cursorIndexOfImageUrisJson);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final Long _tmpSnoozedUntil;
            if (_cursor.isNull(_cursorIndexOfSnoozedUntil)) {
              _tmpSnoozedUntil = null;
            } else {
              _tmpSnoozedUntil = _cursor.getLong(_cursorIndexOfSnoozedUntil);
            }
            final String _tmpUserNote;
            _tmpUserNote = _cursor.getString(_cursorIndexOfUserNote);
            final String _tmpCollectionName;
            if (_cursor.isNull(_cursorIndexOfCollectionName)) {
              _tmpCollectionName = null;
            } else {
              _tmpCollectionName = _cursor.getString(_cursorIndexOfCollectionName);
            }
            final String _tmpTagsJson;
            _tmpTagsJson = _cursor.getString(_cursorIndexOfTagsJson);
            final boolean _tmpOrganizationReviewed;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfOrganizationReviewed);
            _tmpOrganizationReviewed = _tmp_1 != 0;
            final String _tmpEnrichmentStatus;
            _tmpEnrichmentStatus = _cursor.getString(_cursorIndexOfEnrichmentStatus);
            final String _tmpEnrichmentProvider;
            if (_cursor.isNull(_cursorIndexOfEnrichmentProvider)) {
              _tmpEnrichmentProvider = null;
            } else {
              _tmpEnrichmentProvider = _cursor.getString(_cursorIndexOfEnrichmentProvider);
            }
            final String _tmpEnrichmentError;
            if (_cursor.isNull(_cursorIndexOfEnrichmentError)) {
              _tmpEnrichmentError = null;
            } else {
              _tmpEnrichmentError = _cursor.getString(_cursorIndexOfEnrichmentError);
            }
            final String _tmpCanonicalUrl;
            if (_cursor.isNull(_cursorIndexOfCanonicalUrl)) {
              _tmpCanonicalUrl = null;
            } else {
              _tmpCanonicalUrl = _cursor.getString(_cursorIndexOfCanonicalUrl);
            }
            final String _tmpWebSiteName;
            if (_cursor.isNull(_cursorIndexOfWebSiteName)) {
              _tmpWebSiteName = null;
            } else {
              _tmpWebSiteName = _cursor.getString(_cursorIndexOfWebSiteName);
            }
            final String _tmpWebAuthor;
            if (_cursor.isNull(_cursorIndexOfWebAuthor)) {
              _tmpWebAuthor = null;
            } else {
              _tmpWebAuthor = _cursor.getString(_cursorIndexOfWebAuthor);
            }
            final String _tmpWebPublishedAt;
            if (_cursor.isNull(_cursorIndexOfWebPublishedAt)) {
              _tmpWebPublishedAt = null;
            } else {
              _tmpWebPublishedAt = _cursor.getString(_cursorIndexOfWebPublishedAt);
            }
            final String _tmpWebLanguage;
            if (_cursor.isNull(_cursorIndexOfWebLanguage)) {
              _tmpWebLanguage = null;
            } else {
              _tmpWebLanguage = _cursor.getString(_cursorIndexOfWebLanguage);
            }
            final Integer _tmpWebReadingTimeMinutes;
            if (_cursor.isNull(_cursorIndexOfWebReadingTimeMinutes)) {
              _tmpWebReadingTimeMinutes = null;
            } else {
              _tmpWebReadingTimeMinutes = _cursor.getInt(_cursorIndexOfWebReadingTimeMinutes);
            }
            final Long _tmpLastEnrichedAt;
            if (_cursor.isNull(_cursorIndexOfLastEnrichedAt)) {
              _tmpLastEnrichedAt = null;
            } else {
              _tmpLastEnrichedAt = _cursor.getLong(_cursorIndexOfLastEnrichedAt);
            }
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new UniBoxItemEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpUrl,_tmpThumbnailUrl,_tmpExtractedText,_tmpCategory,_tmpSourceApp,_tmpTimestamp,_tmpLatitude,_tmpLongitude,_tmpLocationLabel,_tmpImageUri,_tmpImageUrisJson,_tmpStatus,_tmpIsFavorite,_tmpSnoozedUntil,_tmpUserNote,_tmpCollectionName,_tmpTagsJson,_tmpOrganizationReviewed,_tmpEnrichmentStatus,_tmpEnrichmentProvider,_tmpEnrichmentError,_tmpCanonicalUrl,_tmpWebSiteName,_tmpWebAuthor,_tmpWebPublishedAt,_tmpWebLanguage,_tmpWebReadingTimeMinutes,_tmpLastEnrichedAt,_tmpUpdatedAt);
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

  @Override
  public Flow<List<String>> getCollectionNames() {
    final String _sql = "\n"
            + "        SELECT DISTINCT collectionName FROM unibox_items\n"
            + "        WHERE collectionName IS NOT NULL AND TRIM(collectionName) != ''\n"
            + "        ORDER BY collectionName COLLATE NOCASE\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"unibox_items"}, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            _item = _cursor.getString(0);
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
  public Object getAllItemsSync(final Continuation<? super List<UniBoxItemEntity>> $completion) {
    final String _sql = "SELECT * FROM unibox_items ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<UniBoxItemEntity>>() {
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
          final int _cursorIndexOfImageUrisJson = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrisJson");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfSnoozedUntil = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozedUntil");
          final int _cursorIndexOfUserNote = CursorUtil.getColumnIndexOrThrow(_cursor, "userNote");
          final int _cursorIndexOfCollectionName = CursorUtil.getColumnIndexOrThrow(_cursor, "collectionName");
          final int _cursorIndexOfTagsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "tagsJson");
          final int _cursorIndexOfOrganizationReviewed = CursorUtil.getColumnIndexOrThrow(_cursor, "organizationReviewed");
          final int _cursorIndexOfEnrichmentStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "enrichmentStatus");
          final int _cursorIndexOfEnrichmentProvider = CursorUtil.getColumnIndexOrThrow(_cursor, "enrichmentProvider");
          final int _cursorIndexOfEnrichmentError = CursorUtil.getColumnIndexOrThrow(_cursor, "enrichmentError");
          final int _cursorIndexOfCanonicalUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "canonicalUrl");
          final int _cursorIndexOfWebSiteName = CursorUtil.getColumnIndexOrThrow(_cursor, "webSiteName");
          final int _cursorIndexOfWebAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "webAuthor");
          final int _cursorIndexOfWebPublishedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "webPublishedAt");
          final int _cursorIndexOfWebLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "webLanguage");
          final int _cursorIndexOfWebReadingTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "webReadingTimeMinutes");
          final int _cursorIndexOfLastEnrichedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastEnrichedAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
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
            final String _tmpImageUrisJson;
            _tmpImageUrisJson = _cursor.getString(_cursorIndexOfImageUrisJson);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final Long _tmpSnoozedUntil;
            if (_cursor.isNull(_cursorIndexOfSnoozedUntil)) {
              _tmpSnoozedUntil = null;
            } else {
              _tmpSnoozedUntil = _cursor.getLong(_cursorIndexOfSnoozedUntil);
            }
            final String _tmpUserNote;
            _tmpUserNote = _cursor.getString(_cursorIndexOfUserNote);
            final String _tmpCollectionName;
            if (_cursor.isNull(_cursorIndexOfCollectionName)) {
              _tmpCollectionName = null;
            } else {
              _tmpCollectionName = _cursor.getString(_cursorIndexOfCollectionName);
            }
            final String _tmpTagsJson;
            _tmpTagsJson = _cursor.getString(_cursorIndexOfTagsJson);
            final boolean _tmpOrganizationReviewed;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfOrganizationReviewed);
            _tmpOrganizationReviewed = _tmp_1 != 0;
            final String _tmpEnrichmentStatus;
            _tmpEnrichmentStatus = _cursor.getString(_cursorIndexOfEnrichmentStatus);
            final String _tmpEnrichmentProvider;
            if (_cursor.isNull(_cursorIndexOfEnrichmentProvider)) {
              _tmpEnrichmentProvider = null;
            } else {
              _tmpEnrichmentProvider = _cursor.getString(_cursorIndexOfEnrichmentProvider);
            }
            final String _tmpEnrichmentError;
            if (_cursor.isNull(_cursorIndexOfEnrichmentError)) {
              _tmpEnrichmentError = null;
            } else {
              _tmpEnrichmentError = _cursor.getString(_cursorIndexOfEnrichmentError);
            }
            final String _tmpCanonicalUrl;
            if (_cursor.isNull(_cursorIndexOfCanonicalUrl)) {
              _tmpCanonicalUrl = null;
            } else {
              _tmpCanonicalUrl = _cursor.getString(_cursorIndexOfCanonicalUrl);
            }
            final String _tmpWebSiteName;
            if (_cursor.isNull(_cursorIndexOfWebSiteName)) {
              _tmpWebSiteName = null;
            } else {
              _tmpWebSiteName = _cursor.getString(_cursorIndexOfWebSiteName);
            }
            final String _tmpWebAuthor;
            if (_cursor.isNull(_cursorIndexOfWebAuthor)) {
              _tmpWebAuthor = null;
            } else {
              _tmpWebAuthor = _cursor.getString(_cursorIndexOfWebAuthor);
            }
            final String _tmpWebPublishedAt;
            if (_cursor.isNull(_cursorIndexOfWebPublishedAt)) {
              _tmpWebPublishedAt = null;
            } else {
              _tmpWebPublishedAt = _cursor.getString(_cursorIndexOfWebPublishedAt);
            }
            final String _tmpWebLanguage;
            if (_cursor.isNull(_cursorIndexOfWebLanguage)) {
              _tmpWebLanguage = null;
            } else {
              _tmpWebLanguage = _cursor.getString(_cursorIndexOfWebLanguage);
            }
            final Integer _tmpWebReadingTimeMinutes;
            if (_cursor.isNull(_cursorIndexOfWebReadingTimeMinutes)) {
              _tmpWebReadingTimeMinutes = null;
            } else {
              _tmpWebReadingTimeMinutes = _cursor.getInt(_cursorIndexOfWebReadingTimeMinutes);
            }
            final Long _tmpLastEnrichedAt;
            if (_cursor.isNull(_cursorIndexOfLastEnrichedAt)) {
              _tmpLastEnrichedAt = null;
            } else {
              _tmpLastEnrichedAt = _cursor.getLong(_cursorIndexOfLastEnrichedAt);
            }
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new UniBoxItemEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpUrl,_tmpThumbnailUrl,_tmpExtractedText,_tmpCategory,_tmpSourceApp,_tmpTimestamp,_tmpLatitude,_tmpLongitude,_tmpLocationLabel,_tmpImageUri,_tmpImageUrisJson,_tmpStatus,_tmpIsFavorite,_tmpSnoozedUntil,_tmpUserNote,_tmpCollectionName,_tmpTagsJson,_tmpOrganizationReviewed,_tmpEnrichmentStatus,_tmpEnrichmentProvider,_tmpEnrichmentError,_tmpCanonicalUrl,_tmpWebSiteName,_tmpWebAuthor,_tmpWebPublishedAt,_tmpWebLanguage,_tmpWebReadingTimeMinutes,_tmpLastEnrichedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
