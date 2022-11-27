package io.github.toquery.example.spring.data.jpa.multiple.databases.bff;

import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.order.service.OrderService;
import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.pay.service.PayService;
import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.product.service.ProductService;
import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RequiredArgsConstructor
@RestController
@RequestMapping
public class IndexController {

    private final UserService userService;
    private final OrderService orderService;
    private final PayService payService;
    private final ProductService productService;

    @GetMapping(value = "/init")
    public ResultDTO init() {
        return new ResultDTO(userService.init(), orderService.init(), productService.init(), payService.init());
    }

    @GetMapping(value = {"/", "/index"})
    public ResultDTO index() {
        return new ResultDTO(userService.findById(1), orderService.findById(1), productService.findById(1), payService.findById(1));
    }

    @GetMapping(value = "/list")
    public ResultListDTO list() {
        return new ResultListDTO(userService.list(), orderService.list(), productService.list(), payService.list());
    }

    @GetMapping(value = "/save")
    public ResultDTO save() {
        return new ResultDTO(userService.init(), orderService.init(), productService.init(), payService.init());
    }

    @GetMapping(value = "/delete")
    public String delete() {
        userService.delete();
        orderService.delete();
        productService.delete();
        payService.delete();
        return "success";
    }

    @GetMapping(value = "/update")
    public ResultDTO update() {
        return new ResultDTO(userService.update(), orderService.update(), productService.update(), payService.update());

    }
}
