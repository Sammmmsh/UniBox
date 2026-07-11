# Features 🌟

UniBox is packed with smart features designed to centralize scattered links and images.

### 1. Android Share Target
UniBox registers itself in the Android OS Share Sheet. You can share links, text, and images from any app directly into UniBox. It handles `text/plain` and `image/*` MIME types.

### 2. Auto-Categorization
Items are automatically tagged (e.g., Food, Tech, Video).
- **Domain Matching**: `youtube.com` = Video, `amazon.com` = Shopping.
- **Keyword Matching**: Scans titles and extracted text for words like "recipe", "flight", "kotlin".

### 3. [[Machine Learning|ML Kit OCR]]
When a screenshot is shared, UniBox runs Google ML Kit to extract all text from the image, making it fully searchable.

### 4. [[Background Tasks|Rich Link Previews]]
If a URL is shared, a background worker uses JSoup to download the webpage and scrape OpenGraph tags (`og:title`, `og:image`) to generate a rich preview card.

### 5. [[Geofencing|Location Reminders]]
Users can attach a location to an item. When they physically walk within 200m of that location, a push notification reminds them about the item.

### 6. [[Database|Instant Full-Text Search]]
Using an FTS4 inverted index, searching across thousands of items (and extracted OCR text) is instantaneous.
