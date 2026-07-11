# Machine Learning (ML Kit) 🧠

UniBox uses on-device Machine Learning to make images searchable.

## The Problem
When a user shares a screenshot of a recipe or a restaurant menu, the text is trapped in pixels. You can't search for "Lasagna" if it's just an image.

## The Solution: TextRecognition
UniBox uses Google's **ML Kit Text Recognition API** (v2).

When an image is shared:
1. `ShareViewModel` intercepts the `imageUri`.
2. `TextExtractor` converts the URI into an `InputImage`.
3. The ML Kit Recognizer processes the image to find text blocks.
4. The output is a single concatenated string of all detected text.
5. This text is saved to the `extractedText` field in the [[Database]].

### Coroutines Integration
ML Kit uses Play Services `Task` objects, which are callback-based. 
We use the `kotlinx-coroutines-play-services` library to convert these into suspending functions:
```kotlin
// Using .await() suspends the coroutine until ML Kit is done!
val visionText = recognizer.process(image).await() 
```

Because the extracted text is saved to the FTS4 table, the user can now type a word from the screenshot into the search bar, and the item will instantly appear.
