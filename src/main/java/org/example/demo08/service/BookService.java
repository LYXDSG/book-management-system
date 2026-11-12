package org.example.demo08.service;

import org.example.demo08.common.PageResult;
import org.example.demo08.dto.BookQuery;
import org.springframework.stereotype.Service;
import org.example.demo08.entity.Book;
//import java.awt.print.Book;


public interface BookService {
//Q：这个接口类的核心作用是什么，为什么一般需要这么个接口，不能直接在实现类里定义方法注入Mapper吗
//A：不需要这个接口照样能跑，但会失去一些重要功能。
//这个接口类的最要作用有：
//1.多实现可切换，有PayService接口，AlipayServiceImpl/WechatPayServiceImpl->用@Qualifer("alipay")选一个
//2.对外契约，接口写清"我能做什么"实现随便换，前后端多人协作时，别人只看接口就能开发
//3.依赖倒置，Contorller依赖能力而不是某个具体的实现，换实现不动Controller
//4.测试替身更干净，mock一个方法只实现需要的方法
    PageResult<Book> page(BookQuery query);
    //分页返回统一分页体
    //单独练习一下这种多DTO的分页操作怎么敲，实现又是怎样的

    Book getById(Long id);
    //按id查一本，查不到抛BusinessException（404）

    Long create (Book book);
    //新增图书，返回值是新书id

    void update (Long id ,Book book);
    //按id修改，id不存在抛BusinessException（404）

    void delete (Long id);
    //按id删除，id不存在抛BusinessException（404）

}
