package org.example.demo08.mapper;

import org.apache.ibatis.annotations.*;
import org.example.demo08.entity.Book;

import java.util.List;

@Mapper

public interface BookMapper {
    //分页+关键字搜索：关键字为空字符串时，like'%%'会匹配全部，正好等于"纯分页"
    //limit #{offset}，#{size}：offset 是跳过多少条=（页码数-1）*每页条数
    //Q：精析一下这段sql，这个分组还有limit什么用法，这整段话的目的是什么, 具体能实现一个怎样的效果
    //A：limit(offset,rowcount)等价于limit rowcount offset skipcount，
    //limit(20,10)就是limit 10 offset 20，也就是跳过20条再拿10条，表示20-30条数据
    //顺带复习一下group by
    @Select(" select * from book where title like concat ('%',#{keyword},'%')"+
    " or author like concat('%',#{keyword},'%') order by ${sortBy} ${order} limit #{offset},#{size}")
    //concat()拼接字符串
    //注意有两个参数是${},${}和#{}的区别：
    //#{}传值：里面的值不会写进SQL，而是在SQL里面的相应位置传一个?，再在参数列表记录传入的值
    //${}传关键字：直接传进SQL，因为SQL里的列名（属性名）不能是?所以这里用${}
    List<Book> pageByKeyword(@Param("keyword") String keyword,
                             @Param("offset") int offset,
                             @Param("size") int size,
                             @Param("sortBy") String sortBy,
                             @Param("order") String order);

    //上面那条查询"总共能查出多少条"，用于前端计算总页数，条件必须和上面完全一致
    //Q：这两个接口有啥区别，分别干啥的
    //A：第一个接口返回这一页的内容，第二个接口返回总条数
    @Select(" select count(*) from book where title like concat('%',#{keyword},'%')" +
            " or author like concat('%',#{keyword},'%')")
    long countByKeyword(@Param("keyword") String keyword);

    //按主键查一本，查不到返回null，Service负责把null变成"图书不存在"
    @Select(" select * from book where id =#{id}")
    Book findById(@Param("id") Long id);

    @Options(useGeneratedKeys=true,keyProperty="id")
    //这段表示让MyBatis把数据库自增主键id回填到book的属性上
    //返回值是修改元组数，影响了几行，插入成功就是1
    //Q：参数表示自增id，注解为什么能返回修改值？
    //A：返回修改元组数是JDBC的默认规则，用long，int时返回值就是修改值，
    //用void就丢弃返回值，用boolean返回值>0就是true。属于约定
    @Insert("insert into book (title,author,price,stock) values (#{title},#{author},#{price},#{stock})")
    int insert (Book book);


    //修改：create_time不动（插入时间），update_time由数据库自动刷新，所以都不填入参数
    @Update("update book set title =#{title},author=#{author},price=#{price},+" +
            "stock=#{stock} where id =#{id}")
    int update(Book book);

    //删除：返回0说明这条数据本来就不存在
    @Delete("delete from book where id=#{id}")
    int deleteById(@Param("id") Long id);
}
