# native-packager-docker
Sbt-native-packager Docker example with dynamic mappings


Show case for using java system properties to change your build behaviour.
Used to create different output packages.


```bash
sbt -Denv=dev docker:publish
sbt -Denv=test docker:publish
sbt -Denv=prod docker:publish
```

The `BuildEnv` autoplugin is responsible for parsing the env parameter.
