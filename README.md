# kundera-test
The application provides the same behavior as [hegira-generator](https://github.com/Arci/hegira-generator) but due to the app engine 30 seconds servlet deadline, generation and cleanup are exploited using [Task Queues](https://cloud.google.com/appengine/docs/java/taskqueue/).

All operation can be initiated from `index.jsp`:

- to generate the entities use the form specifying the amount of entities to generate.
- to cleanup the datastore simply click on the provided link

A remote API servlet is also available, see [web.xml](https://github.com/Arci/kundera-test/blob/master/src/main/webapp/WEB-INF/web.xml).

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