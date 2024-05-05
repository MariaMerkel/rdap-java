for file in target/*.jar; do md5sum "$file" > "$file".md5; done
for file in target/*.jar; do sha1sum "$file" > "$file".sha1; done