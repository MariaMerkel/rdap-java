for file in target/*.jar; do md5sum "$file" > "$file".md5; done
for file in target/*.jar; do sha1sum "$file" > "$file".sha1; done
mkdir pom
cp pom.xml pom/pom.xml
md5sum pom/pom.xml > pom/pom.xml.md5
sha1sum pom/pom.xml > pom/pom.xml.sha1