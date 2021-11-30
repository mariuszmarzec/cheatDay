# CheatDay application

Application for putting some gamification into diet. Keep diet, do workout, get cheat day as reward.
Track your weight progress.
Traditional views with fragments, jetpack, MVI based on MVVM, backend written with ktor.
Done with intention of presenting coding
skills, my approach for app architecture, testing and having some playground to test new frameworks
and patterns.

Source code of backend for project: https://github.com/mariuszmarzec/fiteo/blob/master/src/jvmMain/kotlin/com/marzec/cheatday/CheatDay.kt

<img src="images/main.png" width="425"/> <img src="images/weights.png" width="425"/>

# Setup

1. Setup above mentioned backend locally or on some services like AWS Elastic Beanstalk
2. Before you will be able to run cheat day, declare variables in local.properties like below to
configure connection with backend and release key store

```properties
storeFile=~/key_releases/cheat_day_release.jks
keyAlias=<KEY_ALIAS>
keyPassword=<KEY_PASSWORD>
storePassword=<STORE_PASSWORD>

prod.apiUrl=http://127.0.0.1
prod.authHeader=Authorization
test.apiUrl=http://127.0.0.1/test
test.authHeader=Authorization-Test
```

3. Sync project and enjoy

