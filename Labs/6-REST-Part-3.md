# REST Part 3

This is the final portion of our exploration into building REST-based client-server systems using the Java Spring framework.

The previous section demonstrated implementing a front-end web page and sending a request to the server using `XMLHttpRequest`. This lab
will show you how to return objects from the server and interact with them on the front-end.

## Server Status

Let's add a new method to the server that will report on its status. This method will return a `Status` object.

For demonstration purposes, the status report will consist of only one value: the number of requests the server has satisfied.

Update `Controller.java` to the following:

```
package cms330;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
public class Controller {

    int count;  // Number of times server has been contacted

    // Constructor initializes class members
    public Controller() {
        this.count = 0;
    }


    @RequestMapping("/")
    public String index() {
        this.count++;

        String indexHtml = null;

        try {
            byte[] bytes = Files.readAllBytes(Paths.get("html/index.html"));
    	    indexHtml = new String(bytes);
        } catch(Exception e) {
            e.printStackTrace();
        }

    	return indexHtml;
    }

    @RequestMapping("/hello")
    public String hello(@RequestParam(value="name", defaultValue="World") String name) {
        this.count++;
        return "<p>Hello,</p> <p>" + name + "!</p>";
    }


    // Return an object reporting the status of the server
    // Demonstrates returning an object from a REST endpoint and using class
    // members to track persistent state
    @RequestMapping("/status")
    public Status status() {
        this.count++;
        return new Status(this.count);
    }
}
```

There are two important changes:

1. A new `/status` mapping and method.

2. A `count` class member that keeps track of the number of satisfied requests.

Next, create a `Status.java` class in `src/main/java/cms330`:

```
package cms330;

/*** A state blob reporting the status of the server ***/

public class Status {
    int count;

    public Status(int count) {
        this.count = count;
    }

    /*** Classes returned via REST must have get methods for class members ***/

    public int getCount() {
        return this.count;
    }
}
```

The `status` method of the `Controller` class returns `Status` object. `Status` is really a blob of related variables: in this case,
only the single `count` value, but it could be expanded to include other elements.

**IMPORTANT POINT**: any object that's used a return value from a `Controller` method must have `get` methods for its class members! The
Spring framework uses the getters to convert the object to a text representation prior to HTTP transmission.

Compile and run the project

```
$ ./mvnw clean package
$ java -jar target/cms330-rest-example-0.1.0.jar
```

Now, navigate to `https://cms330-YOURNAME.c9users.io/status`. You should see a string like the following:

```
{"count":0}
```

Refresh the page and you should see the count increment.

## JSON and JavaScript Objects

Objects in JavaScript are really **HashMaps**. Each object has a set of `name:value` pairs, where the `name` is a string and the
`value` can be any data type, including an array or another object. Python's dictionaries operate similarly.

*JavaScript Object Notation* (JSON) is a way of representing JS objects in text form. JSON is frequently used in REST-based
web applications to pass data between the client and server sides.

*Aside*: XML was the preferred text format for passing information in older implementations. This shows up in names like 
`XMLHttpRequest` and AJAX ("Asynchronous JavaScript and XML"), but JSON is the actually the most commonly used tool for information passing in modern applications.

The return string `{"count":0}` indicates that this is an object (denoted by the curly braces) with a field named `"count"` that is
associated with the value `0`.


## Front-End

Suppose that you want to present the status information as formatted HTML. You have two options:

1. Format the response data into HTML on the **server side** as a `String` and return it as a `String`. If you do this, the `String`
must be valid HTML to display properly, not plain text. In particular, you have to use `<p>` and `<br>` tags to implement paragraphs
and line breaks.

2. Return the JSON object to the client side and format it there.

Option 1 is straightforward Java text processing. Here's an example of how to implement option 2 on the front end:

```
<!DOCTYPE html>
<html>

    <!-- Head contains metadata on the whole document -->
    <head>
        <title>CMS 330 REST Demo</title>

        <style>
            body {
                font-family: "Helvetica", "Arial", sans-serif;
                font-size: 18pt;
                color: #333333;
                background-color: #FEFEFE;
                margin: 40px auto;
                max-width: 640px;
            }

            div {
                margin-top: 40px;
            }

            button {
                font-family: "Helvetica", "Arial", sans-serif;
                font-size: 14pt;
            }

            input {
                font-family: "Helvetica", "Arial", sans-serif;
                font-size: 14pt;
                width: 50%;
            }
        </style>
    </head>

    <!-- Body contains the page's content -->
    <body>
        <h1>Input</h1>

        <p>Type your name in the box below and press Submit.</p>

        <input type="text" id="inputBox" />
        <button type="button" id="submitButton"> Submit </button>
        <button type="button" id="statusButton"> Status </button>

        <!-- The div tag creates a named region of the page -->
        <div id="responseDiv"></div>

        <!-- script tag contains JavaScript that interacts with page elements -->
        <script>
            // Set a listener function for the button click
            document.getElementById('submitButton').onclick = function () {

                // Get the current string in the text box
                var input = document.getElementById('inputBox').value;

                var oReq = new XMLHttpRequest();
                oReq.addEventListener("load", responseListener);
                oReq.open("GET", "https://cms330-YOURNAME.c9users.io/hello?name=" + input);
                oReq.send();
            }

            // Listener function for the status button
            document.getElementById('statusButton').onclick = function () {

                var oReq = new XMLHttpRequest();
                oReq.addEventListener("load", statusListener);
                oReq.open("GET", "https://cms330-YOURNAME.c9users.io/status");
                oReq.send();
            }

            function responseListener() {
                document.getElementById('responseDiv').innerHTML = this.responseText;
            }

            function statusListener() {
                // Convert the text body of the response to a JavaScript object
                var obj = JSON.parse(this.responseText);

                var str = 'Number of requests: ' + obj['count'];
                document.getElementById('responseDiv').innerHTML = str;
            }


        </script>
    </body>
</html>
```

Take a look at what's changed.

- There is another button with `id=statusButton`.

- This button has an `onclick` function that creates a `XMLHttpRequest` to the `/status` URL. Note that `/status` does not take
any input arguments, so nothing is appended to the URL.

- The return listener for the status request is set to `statusListener`.

- The `statusListener` function uses the built-in `JSON.parse` method to convert the JSON response string to an object, named `obj`. The
count value is now accessible using `obj['count']` (this is similar to Python's dictionaries). The rest of the method formats a string
and puts that string into the `innerHTML` property of the `responseDiv` element.
