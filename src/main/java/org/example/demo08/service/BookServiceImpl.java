package org.example.demo08.service;

import org.example.demo08.common.BusinessException;
import org.example.demo08.common.PageResult;
import org.example.demo08.dto.BookQuery;
import org.example.demo08.mapper.BookMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.demo08.entity.Book;
//import java.awt.print.Book;
//已在系统自动导入中排除此项导入，ctrl+Alt+s可快速调出设置
import java.util.List;

@Service

public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;

    public BookServiceImpl(BookMapper bookMapper){
        this.bookMapper=bookMapper;
    }

    @Override
    public PageResult<Book> page(BookQuery query){
        //(1)业务规则，关键词null统一转成空串，这样SQL里的'%%'才能匹配全部（=纯分页）
        String keyword =query.safeKeyWord();

        //(2)先查总数：为零就直接返回空列表，省掉一次没必要的查询
        long total =bookMapper.countByKeyword(keyword);
        if(total==0) return new PageResult<>(0,query.getPage(),query.getSize(), List.of());

        //(3)再查当前页数据： offset由BookQuery算好，Service不关心公式细节
        List<Book> list=bookMapper.pageByKeyword(keyword,query.offset(),query.getSize(),query.getSortBy(),query.getOrder());
        //Mapper接口类的实现类由MyBatis自动生成，直接用方法就行
        //MyBatis是如何生成具体实现类的，实现类里面的逻辑是啥，是SQL语句决定的吗
        return new PageResult<>(total,query.getPage(),query.getSize(),list);

    }

    @Override
    public Book getById(Long id){
//        if(bookMapper.findById(id)==null) throw new BusinessException(404,"图书不存在：id="+id);
//        return bookMapper.findById(id);
        Book book = bookMapper.findById(id);
        if(book == null) throw new BusinessException(404,"图书不存在：id="+id);
        return book;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    //@Transactional,这个方法里的数据库操作都在一个事务里
    //加了这个rollbackFor=Exception.class,受检异常也回滚(默认不回滚)
    public Long create (Book book){
        bookMapper.insert(book);
        return book.getId();
        //id由insert回填，详见bookMapper
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void update(Long id,Book book){
        //先确认存在再修改，让改一个不存在的id返回404而不是改成功但没变
        if(bookMapper.findById(id)==null) throw new BusinessException(404,"图书不存在：id="+id);
        book.setId(id);
        //再兜一层，以Service收到的id为准
        //Q；兜一层啥意思
        bookMapper.update(book);
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void delete(Long id){
        int rows =bookMapper.deleteById(id);
        //返回值=真正删掉的行数
        if(rows==0) throw new BusinessException(404,"要删除的图书不存在：id="+id);
    }
    //Q；为啥删除的要用这种写法，能不能也用修改的那种判断id存在与否的方法
}
