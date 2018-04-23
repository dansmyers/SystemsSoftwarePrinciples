# REST Part 2


## Previously on CMS 330 Labs

The last lab focused on setting up a web server and controller using the Java Spring Boot framework. This required a fair amount of
assistance from the Maven build tool and its `pom.xml` file


## Create a Root Document


```
import java.nio.Files;
import java.nio.Paths;
```

```
@RequestMapping("/")
public String index() {
    String indexHtml = null;

    try {
        byte[] bytes = Files.readAllBytes(Paths.get("html/index.html"));
    	  indexHtml = new String(bytes);
    } catch(Exception e) {
        e.printStackTrace();
    }

    return indexHtml;
}
```
