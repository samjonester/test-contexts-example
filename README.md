# Test Contexts

This is an example repo to accompany my Test Contexts blog post.

The example repo shows methods to define test contexts for languages that do not typically use spec libraries.

## C#  - NUnit / XUnit

Create a class for a test context, and refine it with a nested subclass. Be sure to declare the nested subclass as `public`, and each constructor as `public`. C#'s inheritance rules allow private methods to be accessed by nested classes, so use this to your advantage. A parent's accessible no-args constructor will be called before a subclasses, this allows a context to further refine what was defined by a parent.

## Java - Junit

Create a class for a test context, and refine it with a nested subclass. Be sure to declare the nested subclass as `public static` and each constructor as `public`. Java's inheritance rules allow protected methods to be accessed by subclasses, so use this to your advantage. Each constructor will act to refine the current context, and will be executed after it's parents. The constructor acts similarly a typical `@Before` method in JUnit, but adds one important thing. A parent's `public` accessible no-args constructor will be called before a subclasses, this allows a context to further refine what was defined by a parent.
