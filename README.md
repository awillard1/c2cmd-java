# c2cmd-java

```Urls have to be added to the Java source code.```

To create the jar and run within its own directory:

```
javac c2cmd.java
jar -cvmf exfil.mf exfil.jar *
java -jar exfil.jar
```