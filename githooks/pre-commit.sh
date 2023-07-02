# usr/bin/env sh
git stash -qku
./gradlew clean check
RESULT=$?
git stash pop -q
exit $RESULT
