# Concurrency

This is a short lab to let you practice writing some code using the ` pthread` library.

Remember that you need to `#include <pthread.h>` at the start of your program and compile with `-lpthread` to link in the library.


## If You Want to Destroy My Sweater

Let's write a basic program that creates some threads.

```
#include <stdio.h>
#include <pthread.h>

#define NUM_THREADS 99


// Print the thread's input id argument
void * print_thread_id (void *arg) {
    long id = (long) arg;
    printf("I'm thread %ld!\n", id);
    return NULL;
}


int main() {

    // Declare an array of threads
    pthread_t threads[NUM_THREADS];
    
    // Create those threads!
    long i;
    for (i = 0; i < NUM_THREADS; i++) {
        pthread_create(&threads[i], NULL, print_thread_id, (void *) i);
    }
    
    // Use pthread_join to make the main thread pause
    for (i = 0; i < NUM_THREADS; i++) {
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

## Lockdown

Now write a program that creates two threads.

The first thread should run a function that **increments** a shared global variable
in a loop. The second should run a function that **decrements** the same shared global variable in a loop. Print the value of
the variable at the end of `main`: it should be 0.

The code that updates the shared variable is in a ***critical region*** of the program, so there might be a ***race condition*** if both 
threads update the variable at the same time. Create a lock that protects the critical region code.

```
pthread_mutex_t lock = PTHREAD_MUTEX_INITIALIZER;  // declare global mutex variable

pthread_mutex_lock(&lock);  // To lock the mutex
pthread_mutex_unlock(&lock);  // To unlock the mutex
```


