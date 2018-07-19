package chiroito.sample.osa.metoroadapter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/map")
public class GoogleMapController {

    @RequestMapping(method = RequestMethod.GET)
    public String show() {
        return "googleMap";
    }
}
