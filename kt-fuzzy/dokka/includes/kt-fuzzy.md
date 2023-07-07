# Module kt-fuzzy

A dependency-less Kotlin Multiplatform library for fuzzy string matching

## Features

- [x] No dependencies
- [x] Simple to use
- [x] Performance friendly

## Including

You can include ${project.module} in your project by adding the following:

### Maven

```xml
<dependency>
  <groupId>${project.group}</groupId>
  <artifactId>${project.module}</artifactId>
  <version>${project.version}</version>
</dependency>
```

### Gradle Groovy DSL

```groovy
implementation '${project.group}:${project.module}:${project.version}'
```

### Gradle Kotlin DSL

```kotlin
implementation("${project.group}:${project.module}:${project.version}")
```

### Gradle Version Catalog

```toml
${project.module} = { group = "${project.group}", name = "${project.module}", version = "${project.version}" }
```

## Examples

