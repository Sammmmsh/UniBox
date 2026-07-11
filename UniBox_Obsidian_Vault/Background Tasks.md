# Background Tasks (WorkManager) ⚙️

UniBox uses **WorkManager** to ensure that data enrichment happens reliably, even if the app is closed.

## The MetadataWorker
When a user saves a URL (either via the Share Sheet or manually typing it), the app saves the raw URL immediately so the UI is responsive. 

Simultaneously, `ShareViewModel` enqueues a `OneTimeWorkRequest` for the `MetadataWorker`.

### Network Constraints
```kotlin
Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
```
This tells Android: *Only run this job when the device has an active internet connection.* If the user saves a link while in an airplane, the job will pause and automatically run when they land.

### JSoup OpenGraph Scraping
Inside the worker, the `OpenGraphParser` uses the **JSoup** library to download the HTML of the URL.
It looks for meta tags in this order:
1. `og:title` / `og:image` / `og:description`
2. `twitter:title` / `twitter:image`
3. Standard `<title>` tags

Once found, the worker updates the item in the [[Database]] with the rich metadata. Thanks to Kotlin Flow, the UI instantly updates to show the new thumbnail and title.
