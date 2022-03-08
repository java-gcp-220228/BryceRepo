<h1>Week 2 Day 2</h1>

# External Links
|
[Miro link](https://miro.com/app/board/uXjVOHfoCIk=/?invite_link_id=300731594197)
|
[URL vs URI](https://www.geeksforgeeks.org/difference-between-url-and-uri/)
|
[javalin-demo]()
|
[Javalin Docs](https://javalin.io/documentation#getting-started)
|
[Mockito](https://site.mockito.org/)
|
[Mockito GitHub wiki](https://github.com/mockito/mockito/wiki/Features-And-Motivations)

****

# RESTFUL

Naming conventions
- Resources
- Action to perform on that typr of resource
  - GET: retrieve a resource
  - POST: add a resource
  - PUT: replace a resource (update)
  - PATCH: partially replace a resource (partial update)
  - DELETE: delete a resource


Singleton and collection resources   
- You can either have a single resource that you're dealing with, or a whole group   
- GET /clients <- collection of clients that we are retrieving   
- GET /clients/10 <- single client that we are retrieving

Sub-collection resources   
- GET /clients/10/accounts <- Accounts is a sub-collection (we are retrieving ALL accounts that belong to a particular client)


Sub-singleton resources   
- GET /clients/10/accounts/1 <- accounts/1 is an account that belongs to client with id 10


<h2>Best Practices</h2>

- Don't use verbs in your URI (use nouns instead)
- It is already implied by the HTTP verb what we are doing
  - Ex.don't do POST /addclientUse POST /clients
- Be consistent with the hierarchy, verbs being used, etc. 
  - Make the endpoints predictable

****
****

## Javalin-Demo

- This demo was copied from the crud-demo from Week 2 Day 1

- Project with dependencies (posted in Discord 3/8/2022)
    - Postgresql
    - Driver
    - Javalin
    - LogBack Classic
    - Jackson
  

### Overview:

```mermaid
graph WF1;
Database --> Data Access; 
Data Access ----> Service; 
Service ---> Controller; 
Controller ---> Client;
```

****
****
### CODE BLOCK 
****
****


Driver.java main()

```java
Javalin app = Javalin.create();

mapControllers(app, new StudentController());


```

#### Driver.java 

```mapControllers(Javalin app, Controller... controllers) {}```

```java
for(Controller c : controllers) {
    c.mapEndpoints(app);
}

```


#### com.revature.controller/Controller.java

```



```

#### StudentController.java 

```java
public class StudentController implements Controller {

    private StudentService studentService;
    

    public StudentController() {
        this.studentService = new StudentService();
    };

    private Handler getAllStudents = (ctx) -> {
        List<Student> students = studentService.getAllStudents();

        ctx.json(students);
    };


    private Handler getStundentById = (ctx) -> {

        String id = ctx.pathParam("studentId")

        Student student = studentService.getStudentById(id);

        ctx.json(student);

    };

    @Override
    public void mapEndpoints(Javalin app) {
        app.get("/students", getAllStudents);
        app.get("/students/{studentId}", getStudentById)
    };


}

```

#### StudentService.java

```java


```


****
****

# Mockito


::: mermaid
graph TD;
    A-->B;
    A-->C;
    B-->D;
    C-->D;
:::
