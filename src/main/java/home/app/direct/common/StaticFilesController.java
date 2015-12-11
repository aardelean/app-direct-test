package home.app.direct.common;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * No war, serving classpath files.
 */
//@Controller("/")
public class StaticFilesController {

    @RequestMapping(value = "/{file_name}", method = RequestMethod.GET)
    public String getThymeleafTemplate(@PathVariable("file_name")String fileName, Model model){
        return fileName;
    }
}
