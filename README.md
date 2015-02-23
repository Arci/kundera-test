# kundera-test
The application provides a __similar__ behaviour as [hegira-generator](https://github.com/Arci/hegira-generator) but due to the app engine 30 seconds servlet deadline, generation and cleanup are exploited using [Task Queues](https://cloud.google.com/appengine/docs/java/taskqueue/).

All operation can be initiated from `index.jsp`:

- to generate the entities use the form specifying the amount of entities to generate.
- to cleanup the datastore simply click on the provided link

A remote API servlet is also available, see [web.xml](https://github.com/Arci/kundera-test/blob/master/src/main/webapp/WEB-INF/web.xml).

###Kundera fix
From the [pom.xml](https://github.com/Arci/kundera-test/blob/master/pom.xml) can be noticed that Kundera core is excluded from the GAE Datastore client and instead a __2.16-SNAPSHOT__ is added manually.

This is due to a problem that Kundera 2.15 have in the hashCode generation in [Node.java](https://github.com/impetus-opensource/Kundera/blob/273c13342ddd1aceed0cd23504649926ce8fdb84/src/jpa-engine/core/src/main/java/com/impetus/kundera/graph/Node.java#L431). The problem is visible only in the App Engine runtime since it does not permit reflection on JRE classes and the _Node_ class generate its hashCode reflecting over `java.lang.String` causing the exception.

The __2.16-SNAPSHOT__ version included in the pom.xml is [this](https://github.com/Arci/Kundera/tree/appenginefix), a fork of Kundera in which the problem has been fixed.

###Start the application
To start the app just run the command:

```
mvn appengine:devserver
```

###Deploy to an App Engine application
Modify `webapp/WEB-INF/appengine-web.xml` file and set the application id inside of `<application>` tag.
To deploy run the command:

```
mvn appengine:update
```
