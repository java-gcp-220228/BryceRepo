<h1>Week 2 Day 3</h1>

# External Links
|
[Miro link](https://miro.com/app/board/uXjVOGyD1rk=/?invite_link_id=421015992
)
|

****

# Multi-threading

- Where you can utilize multiple threads in an application
- Thread: A single sequential flow of control within a program
  - Sequence of instructions that can be managed individually
  - Allows the program to be split up into simultaneously running tasks

Concurrency vs. Parallelism: 
- Concurrency: multiple tasks in progress at the same time
  - Switching rapidly between each task (no over lap between thread processes)
- Parallelism: running many different tasks at the same time
  

  Thread Scheduler
  : tries to give threads running on the computer equal amounts of processing time
- You can give threads a certian prioirity so that the they have more precessing time than others. 
- Random assignment (not deterministic)
- Tries to be fair

Multi-threading happens behind the scenes A LOT in some libraries that you will use in java

Javalin is an example: 
- When we run our Javalin application, it will spin up an embedded Jetty server
- The application will then listen for HTTP requests
- When it receives HTTP requests, it will spin up a new thread to handle that request 


****

# EXAMPLE CODE


thread-Demo
fobbaci-Demo
synchronization-Demo





```java
public static void main(String[] args) {

    Counter c = new Counter();

    Thread[] incrementThreads = new Thread[100];



}

```


javalin-Demo

- logging using LogBack
    - src/main/resources/logback.xml