for file in target/*.jar; do md5sum "$file" | cut -c -32 > "$file".md5; done
for file in target/*.jar; do sha1sum "$file" | cut -c -40 > "$file".sha1; done
mkdir pom
cp pom.xml pom/pom.xml
md5sum pom/pom.xml | cut -c -32 > pom/pom.xml.md5
sha1sum pom/pom.xml | cut -c -40 > pom/pom.xml.sha1