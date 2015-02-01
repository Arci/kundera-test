# kundera-test
The application provides a servlet to generate data and store them in Datastore through CPIM and Kundera GAE Datastore extension.

To easily cleanup the datastore a clean up servlet is provided, the servlet push a task on the default queue for each persisted table each task is then responsible of deleting all entities of the given table.

A remote API servlet is also available, see [web.xml](https://github.com/Arci/kundera-test/blob/master/src/main/webapp/WEB-INF/web.xml).

###Start the application
To start the app just run the command:

```
mvn appengine:devserver
```
