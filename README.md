# kundera-test
The application provides a servlet to generate data and store them in Datastore through CPIM and Kundera GAE Datastore extension.
To generate the entities visit `index.jsp` and use the form to generate the desired number of entities.
Entities generation is achieved issuing tasks on the default queue.

To easily cleanup the datastore a clean up servlet is provided.
The servlet queries the Datastore through the [Metadata API](https://cloud.google.com/appengine/docs/java/datastore/metadataqueries) retrieving all the persisted Kind(s).
A task per Kind is pushed responsible for deleting all the entities of that Kind.

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