有一条建议”不要频繁去打开关闭硬盘索引”

“IndexReader的实例化过程是一个非常耗时的过程”

**创建索引的方法是IndexWriter，这里所使用的是读取所创建的
 \* 索引的方法IndexReader.
 \* 和他的子类
 \* MultiReader**

文档：讲解MIME格式！

可以设计出查看后十条的功能！

查询步骤：

(查看这些工具的构造方法)

1. 构建分词器   

   `Analyzer analyzer = new ...();`

2. 打开索引存储的目录  

   `Directory directory = FSDirectory.open(Paths.get(目录))`

3. 建立索引读取器

   `IndexReader indexReader = DirectoryReader.open(directory)`

4. 建立索引搜索器

   `IndexSearcher indexSearcher = new IndexSearcher(indexReader)`

5. 建立查询，进行查询！

   `Query query = new ...();`

   `search(query, indexSearcher)`

6. `indexReader.close(); directory.close()` 释放资源

查询：

1. 词项查询  TermQuery

```java
TermQuery tq = new TermQuery(new Term("name", "thinkpad"));
```

寻找name属性的匹配thinkpad的项

2. 布尔查询  BooleanQuery

搜索条件有多个，组合多个子查询，组合关系支持：或、且、且非

布尔查询默认的最大字句数为1024，在将通配符查询这样的查询rewriter为布尔查询时，往往会产生很多的字句，可能抛出TooManyClauses 异常。可通过BooleanQuery.setMaxClauseCount(int)设置最大字句数。

```java
// 布尔查询
Query query1 = new TermQuery(new Term(filedName, "thinkpad"));
Query query2 = new TermQuery(new Term("simpleIntro", "英特尔"));
BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
booleanQueryBuilder.add(query1, Occur.SHOULD);
booleanQueryBuilder.add(query2, Occur.MUST);
BooleanQuery booleanQuery = booleanQueryBuilder.build();

// 可像下一行这样写
// BooleanQuery booleanQuery = new BooleanQuery.Builder()
//     .add(query1, Occur.SHOULD).add(query2, Occur.MUST).build();
```

3. 短语查询  PhraseQuery

匹配特定序列的多个词项，所有加入的词项都匹配才可以

slop移动因子，默认值为0(两个词项中间没有别的词)

```java
PhraseQuery phraseQuery3 = new PhraseQuery("name", "笔记本电脑", "联想");

PhraseQuery phraseQuery4 = new PhraseQuery.Builder()
    .add(new Term("name", "笔记本电脑"), 4)
    .add(new Term("name", "联想"), 5).build();
```

4. 多重短语查询  MultiPhraseQuery

支持同位置多个词的or匹配

```java
MultiPhraseQuery multiPhraseQuery = new MultiPhraseQuery.Builder()
    .add(terms).add(t).build();  // terms是个数组
```

5. 前缀查询、通配符查询、正则查询

PrefixQuery,  WildcardQuery, RegexpQuery    会比较慢，谨慎使用

6. 模糊查询！！！  允许最大两个不同字符

`FuzzyQuery fuzzyQuery2 = new FuzzyQuery(new Term("name", "thinkd"), 2);`

学习下GUI