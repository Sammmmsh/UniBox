# Database (Room & FTS4) 🗄️

UniBox stores all items locally using **Room**, an abstraction layer over SQLite.

## Core Entities
- `UniBoxItemEntity`: The main table storing the raw data (URL, title, timestamp, category).
- `UniBoxItemFts`: An FTS4 virtual table that enables lightning-fast text searches.

## Why FTS4?
If we used standard SQL `LIKE '%query%'`, SQLite would have to perform a **full table scan**, checking every row one by one. This is extremely slow for large datasets.

FTS4 creates an **inverted index** (similar to a search engine). It maps every unique word to the row IDs where that word appears.

```sql
-- How we query the FTS table
SELECT unibox_items.* FROM unibox_items
JOIN unibox_items_fts ON unibox_items.rowid = unibox_items_fts.rowid
WHERE unibox_items_fts MATCH :query
```

Because `UniBoxItemFts` uses `contentEntity = UniBoxItemEntity::class`, Room automatically keeps the FTS index synchronized when items are added, updated, or deleted.

## Reactive Queries
The DAO returns `Flow<List<UniBoxItemEntity>>`. When the database changes, Room automatically emits a new list through the Flow, passing it up through the [[Architecture|Repository to the ViewModel]], and instantly updating the Compose UI.
