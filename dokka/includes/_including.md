You can include ${project.module} in your project by adding the following:

<div class="tabbed-set tabbed-alternate" data-tabs="1:4">
<input id="__tabbed_1_1" name="__tabbed_1" type="radio"/>
<input id="__tabbed_1_2" name="__tabbed_1" type="radio"/>
<input checked="checked" id="__tabbed_1_3" name="__tabbed_1" type="radio"/>
<input id="__tabbed_1_4" name="__tabbed_1" type="radio"/>
<div class="tabbed-labels">
<label for="__tabbed_1_1">Maven</label>
<label for="__tabbed_1_2">Gradle Groovy</label>
<label for="__tabbed_1_3">Gradle Kotlin</label>
<label for="__tabbed_1_4">Gradle Version Catalog</label>
</div>
<div class="tabbed-content">
<div class="tabbed-block">

```xml
<dependencies>
    <dependency>
        <groupId>${project.group}</groupId>
        <artifactId>${project.module}</artifactId>
        <version>${project.version}</version>
    </dependency>
</dependencies>
```

</div>
<div class="tabbed-block">

```gradle
dependencies {
    implementation '${project.group}:${project.module}:${project.version}'
}
```

</div>
<div class="tabbed-block">

```kotlin
dependencies {
    implementation("${project.group}:${project.module}:${project.version}")
}
```

</div>
<div class="tabbed-block">

```toml
[libraries]
${project.module} = { group = "${project.group}", name = "${project.module}", version = "${project.version}" }
```

</div>
</div>
</div>
