# kundera-test
The application provides a servlet to generate data and store them in Datastore through CPIM and Kundera GAE Datastore extension.

To easily cleanup the datastore, a clean servlet is provided, it lunches a task in the default queue for each persisted table which erase all entities related.

A remote API servlet is also available.


###Start the application
To start the app, use the [App Engine Maven Plugin](http://code.google.com/p/appengine-maven-plugin/).
Just run the command.

```
mvn appengine:devserver
```
