package chiroito.sandbox.jfr;

import jdk.jfr.*;

@Label("Hello")
@Category({"chiroito","hello"})
public final class HelloEvent extends Event {

    @Label("Message")
    public String msg;
}
