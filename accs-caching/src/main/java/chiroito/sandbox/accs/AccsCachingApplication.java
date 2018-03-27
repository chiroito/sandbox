package chiroito.sandbox.accs;

import com.oracle.cloud.cache.basic.Cache;
import com.oracle.cloud.cache.basic.RemoteSessionProvider;
import com.oracle.cloud.cache.basic.options.Transport;
import com.oracle.cloud.cache.basic.options.ValueType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@RestController
@SpringBootApplication
public class AccsCachingApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccsCachingApplication.class, args);
    }

    @GetMapping(path = "load/{id}")
    public Item load(@PathVariable(value = "id") long id) {

        Cache<Item> cache = this.getCache();
        Item item = cache.get(Long.toString(id));
        return item;

    }


    @PostMapping(path = "store/{id}")
    public void store(@PathVariable(value = "id") long id, @RequestBody String name) {

        Item item = new Item(id, name);

        Cache<Item> cache = this.getCache();
        cache.put(Long.toString(item.getId()), item);

    }

    private Cache<Item> getCache() {
        return new RemoteSessionProvider().createSession(Transport.rest()).getCache("cache-item", ValueType.of(Item.class));
    }

}
