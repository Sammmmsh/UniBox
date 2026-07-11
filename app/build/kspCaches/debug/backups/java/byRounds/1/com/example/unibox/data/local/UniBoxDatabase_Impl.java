package com.example.unibox.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.FtsTableInfo;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class UniBoxDatabase_Impl extends UniBoxDatabase {
  private volatile UniBoxItemDao _uniBoxItemDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `unibox_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `url` TEXT, `thumbnailUrl` TEXT, `extractedText` TEXT, `category` TEXT NOT NULL, `sourceApp` TEXT, `timestamp` INTEGER NOT NULL, `latitude` REAL, `longitude` REAL, `locationLabel` TEXT, `imageUri` TEXT)");
        db.execSQL("CREATE VIRTUAL TABLE IF NOT EXISTS `unibox_items_fts` USING FTS4(`title` TEXT NOT NULL, `description` TEXT NOT NULL, `extractedText` TEXT, `url` TEXT, `sourceApp` TEXT, `category` TEXT NOT NULL, content=`unibox_items`)");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_unibox_items_fts_BEFORE_UPDATE BEFORE UPDATE ON `unibox_items` BEGIN DELETE FROM `unibox_items_fts` WHERE `docid`=OLD.`rowid`; END");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_unibox_items_fts_BEFORE_DELETE BEFORE DELETE ON `unibox_items` BEGIN DELETE FROM `unibox_items_fts` WHERE `docid`=OLD.`rowid`; END");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_unibox_items_fts_AFTER_UPDATE AFTER UPDATE ON `unibox_items` BEGIN INSERT INTO `unibox_items_fts`(`docid`, `title`, `description`, `extractedText`, `url`, `sourceApp`, `category`) VALUES (NEW.`rowid`, NEW.`title`, NEW.`description`, NEW.`extractedText`, NEW.`url`, NEW.`sourceApp`, NEW.`category`); END");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_unibox_items_fts_AFTER_INSERT AFTER INSERT ON `unibox_items` BEGIN INSERT INTO `unibox_items_fts`(`docid`, `title`, `description`, `extractedText`, `url`, `sourceApp`, `category`) VALUES (NEW.`rowid`, NEW.`title`, NEW.`description`, NEW.`extractedText`, NEW.`url`, NEW.`sourceApp`, NEW.`category`); END");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'e191406186b589d2640b662f7efe6680')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `unibox_items`");
        db.execSQL("DROP TABLE IF EXISTS `unibox_items_fts`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_unibox_items_fts_BEFORE_UPDATE BEFORE UPDATE ON `unibox_items` BEGIN DELETE FROM `unibox_items_fts` WHERE `docid`=OLD.`rowid`; END");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_unibox_items_fts_BEFORE_DELETE BEFORE DELETE ON `unibox_items` BEGIN DELETE FROM `unibox_items_fts` WHERE `docid`=OLD.`rowid`; END");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_unibox_items_fts_AFTER_UPDATE AFTER UPDATE ON `unibox_items` BEGIN INSERT INTO `unibox_items_fts`(`docid`, `title`, `description`, `extractedText`, `url`, `sourceApp`, `category`) VALUES (NEW.`rowid`, NEW.`title`, NEW.`description`, NEW.`extractedText`, NEW.`url`, NEW.`sourceApp`, NEW.`category`); END");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_unibox_items_fts_AFTER_INSERT AFTER INSERT ON `unibox_items` BEGIN INSERT INTO `unibox_items_fts`(`docid`, `title`, `description`, `extractedText`, `url`, `sourceApp`, `category`) VALUES (NEW.`rowid`, NEW.`title`, NEW.`description`, NEW.`extractedText`, NEW.`url`, NEW.`sourceApp`, NEW.`category`); END");
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsUniboxItems = new HashMap<String, TableInfo.Column>(13);
        _columnsUniboxItems.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUniboxItems.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUniboxItems.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUniboxItems.put("url", new TableInfo.Column("url", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUniboxItems.put("thumbnailUrl", new TableInfo.Column("thumbnailUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUniboxItems.put("extractedText", new TableInfo.Column("extractedText", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUniboxItems.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUniboxItems.put("sourceApp", new TableInfo.Column("sourceApp", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUniboxItems.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUniboxItems.put("latitude", new TableInfo.Column("latitude", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUniboxItems.put("longitude", new TableInfo.Column("longitude", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUniboxItems.put("locationLabel", new TableInfo.Column("locationLabel", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUniboxItems.put("imageUri", new TableInfo.Column("imageUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUniboxItems = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUniboxItems = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUniboxItems = new TableInfo("unibox_items", _columnsUniboxItems, _foreignKeysUniboxItems, _indicesUniboxItems);
        final TableInfo _existingUniboxItems = TableInfo.read(db, "unibox_items");
        if (!_infoUniboxItems.equals(_existingUniboxItems)) {
          return new RoomOpenHelper.ValidationResult(false, "unibox_items(com.example.unibox.data.local.UniBoxItemEntity).\n"
                  + " Expected:\n" + _infoUniboxItems + "\n"
                  + " Found:\n" + _existingUniboxItems);
        }
        final HashSet<String> _columnsUniboxItemsFts = new HashSet<String>(6);
        _columnsUniboxItemsFts.add("title");
        _columnsUniboxItemsFts.add("description");
        _columnsUniboxItemsFts.add("extractedText");
        _columnsUniboxItemsFts.add("url");
        _columnsUniboxItemsFts.add("sourceApp");
        _columnsUniboxItemsFts.add("category");
        final FtsTableInfo _infoUniboxItemsFts = new FtsTableInfo("unibox_items_fts", _columnsUniboxItemsFts, "CREATE VIRTUAL TABLE IF NOT EXISTS `unibox_items_fts` USING FTS4(`title` TEXT NOT NULL, `description` TEXT NOT NULL, `extractedText` TEXT, `url` TEXT, `sourceApp` TEXT, `category` TEXT NOT NULL, content=`unibox_items`)");
        final FtsTableInfo _existingUniboxItemsFts = FtsTableInfo.read(db, "unibox_items_fts");
        if (!_infoUniboxItemsFts.equals(_existingUniboxItemsFts)) {
          return new RoomOpenHelper.ValidationResult(false, "unibox_items_fts(com.example.unibox.data.local.UniBoxItemFts).\n"
                  + " Expected:\n" + _infoUniboxItemsFts + "\n"
                  + " Found:\n" + _existingUniboxItemsFts);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "e191406186b589d2640b662f7efe6680", "35fa72381484924b9e4f937e1ac2e051");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(1);
    _shadowTablesMap.put("unibox_items_fts", "unibox_items");
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "unibox_items","unibox_items_fts");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `unibox_items`");
      _db.execSQL("DELETE FROM `unibox_items_fts`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UniBoxItemDao.class, UniBoxItemDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public UniBoxItemDao uniBoxItemDao() {
    if (_uniBoxItemDao != null) {
      return _uniBoxItemDao;
    } else {
      synchronized(this) {
        if(_uniBoxItemDao == null) {
          _uniBoxItemDao = new UniBoxItemDao_Impl(this);
        }
        return _uniBoxItemDao;
      }
    }
  }
}
