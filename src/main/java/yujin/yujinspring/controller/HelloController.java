package yujin.yujinspring.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
    // 1. static 방식 -> controller 없이 static/000.html
    // 2. mvc를 통해 동적으로 하는 방식 -> controller의 개입 o.
    @GetMapping("hello")
    public String hello(Model model){
        // model(data:hello!!) 를 생성해서 hello로 보내준다
        model.addAttribute("data", "hello!!");
        return "hello"; // 기본적으로 resources/templates/{ViewName}.html 로 매핑해준다.
    }

    // parameter를 받아야 할 때 @RequestParam 이용
    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam("name") String name, Model model) {
        model.addAttribute("name", name);
        return "hello-template";
    }

    // ResponseBody : http 통신 body 부분에 아래의 내용을 직접 넣어주겠다는 의미
    // view에 넘겨주는 것이 아니라 글자 그대로를 body 내요에 담아서 보냄
    // 3. API 방식.
    @GetMapping("hello-string")
    @ResponseBody
    public String helloString(@RequestParam("name") String name) {
        return "hello " + name;
    }

    // 보통 아래와 같이 api 방식을 활용함
    // json 형태로 넘어감.
    // 객체를 생성해서 값을 채워서 넘기는 방식.
    @GetMapping("hello-api")
    @ResponseBody
    public Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName(name);
        return hello;
    }
    public class Hello {
        private String name;
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
