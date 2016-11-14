[![Build Status](https://travis-ci.org/codepitbull/multi-jvm-runner.svg?branch=master)](https://travis-ci.org/codepitbull/multi-jvm-runner)

#Multi-Jvm 
This projects provides a few helper classes to allow easy creation and 
destruction of multiple JVMs from inside an integration test.

The main inspiration came from working with the [sbt-multi-jvm-plugin](https://github.com/sbt/sbt-multi-jvm)

This is a cleanroom implementaion that is NOT based on the code from sbt-multi-jvm.

#Why?
When working with distributed systems (Vert.x, Akka, Quasar, ...) it is 
useful to test certain behavior from the outside (serialization, 
 leader election, memory usage, ...). Spawning a small cluster using the
 code included in this project is quite simple and should enable testing 
 of complex interaction scenarios (well, at least that's what I hope ...)

#Usage
Check out and do ```mvn clean install```
Add the dependency to your project:
```xml
<dependency>
    <groupId>de.codepitbull</groupId>
    <artifactId>multi-jvm-runner</artifactId>
    <version>1.0-SNAPSHOT</version>
    <scope>test</test>
</dependency>
```

In your test spawn the process:
```java
import de.codepitbull.maven.multijvm.JvmProcess

JvmProcess proc = JvmProcess.builder().addCurrentClasspath().mainClass(JvmTest.class.getName()).build();

proc.waitFor();

```

#Demo
Take a look into the test-sources in *src/test/java/de/codepitbull/multijvm/vertx* for a full example. 

#Why no Maven?
I initially thought about writing a Maven plugin to use this. It didn't 
really make sense as Maven's phases are a lot more static than those of
sbt. I might continue if someone comes up with a good case for such a 
plugin.