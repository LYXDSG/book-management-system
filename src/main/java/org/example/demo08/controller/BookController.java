package org.example.demo08.controller;

import jakarta.validation.Valid;
import org.example.demo08.common.PageResult;
import org.example.demo08.common.Result;
import org.example.demo08.dto.BookDTO;
import org.example.demo08.dto.BookQuery;
import org.example.demo08.entity.Book;
import org.example.demo08.service.BookService;
import org.example.demo08.service.BookServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {
    //注入接口，利用多态调用实现类方法，多实现可切换
    private final BookService bookService;

    public BookController (BookService bookService){
        this.bookService=bookService;
    }

    //A1+A2:分页查询＋关键字搜索（查询开放，无需登录）
    //GET/api/books?page=1&size=3&keyword=java
    //参数直接绑定到BookQuery对象上（字段名=URL参数名）@Valid触发BookQuery里的@Min/@Max校验
    @GetMapping
    public Result<PageResult<Book>> page(@Valid BookQuery query){
        return Result.ok(bookService.page(query));
    }

    //A3：按id查一本（查询开放）
    //GET/api/books/3 -> @PathVariable把路径里的3绑定到id参数上
    @GetMapping("/{id}")
    public Result<Book> detail (@PathVariable Long id){
        return Result.ok(bookService.getById(id));
    }

    //A4：新增（必须登录），由loginInterceptor把关，本类不用管登录逻辑
    //POST/api/books+JSON体
    //@RequestMapping把JSON反序列化成Book对象，@Valid触发BookDTO上的@NotBlank/@NotNull/@Min校验
    //校验不过会抛异常，被GlobalExceptionHandler接住，返回400+人话提示
    @PostMapping
    public Result<Long> create (@RequestBody@Valid BookDTO dto){
        //把点菜单（DTO）转成要入库的对象（实体），这一步必须写，两者不是一个类
        Book book =new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPrice(dto.getPrice());
        book.setStock(dto.getStock());
        Long id=bookService.create(book);
        return Result.ok("新增成功",id);
        //data是新增记录的id，前端可以立刻拿它查看详情
    }

    //A5：修改（必须登录）
    //PUT/api/books/3+JSON体
    //路径上的id才是权威id，先set进对象，防止请求体里带一个别的id把数据改错
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id ,@RequestBody@Valid BookDTO dto){
        Book book=new Book();
        book.setId(id);
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPrice(dto.getPrice());
        book.setStock(dto.getStock());
        bookService.update(id,book);
        return Result.ok("修改成功",null);
        //Q：为啥这里data返回null但新增要返回id
    }

    //A6；删除（必须登录）
    //DELETE/api/books/3
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id){
        bookService.delete(id);
        return Result.ok("删除成功",null);
    }
}
