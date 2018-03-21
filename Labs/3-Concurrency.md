# Concurrency

This is a short lab to let you practice writing some code using the ` pthread` library.

Remember that you need to `#include <pthread.h>` at the start of your program and compile with `-lpthread` to link in the library.


## If You Want to Destroy My Sweater

Let's write a basic program that creates a thread and does something with it.

```
#include <stdio.h>
#include <pthread.h>


// Print the threads input id argument
void * print_thread_id (void *arg) {
    long id = (long) arg;
    printf("I'm thread %ld!\n", id);
    return NULL;
}


int main() {

    // Declare an array of threads
    pthread_t threads[99];
    
    // Create those threads!
    long i;
    for (i = 0; i < 99; i++) {
        pthread_create(&threads[i], NULL, print_thread_id, (void *) i);
    }
    
    // Use pthread_join to make the main thread pause
    for (i = 0; i < 99; i++) {
        pthread_join(threads[i], NULL);
    }

    return 0;
}
```

Consider a few questions about this program:

- When the program runs, does it print the threads in order from 0 to 98? What does that tell you about the
relationship between when threads are created and when they actually run?

- Look at the fourth argument to `pthread_create`. It takes the value of `i` and casts it to the generic pointer
type `void *`. This is okay: it's just a way to move the value of `i` into the `print_thread_id` function while going
through the required `void *` type.


## Pull This Thread As You Walk Away

Change two lines to the statements given below, compile and re-run the program, and see what happens:

```
pthread_create(&threads[i], NULL, print_thread_id, &i);  // In main

long id = * ((long *) arg);  // In print_thread_id
```

Explain how this code is now passing the value of `i` by **reference** rather than by **value**.

What is does the program print when you run it with these changes? Explain what you're seeing.
