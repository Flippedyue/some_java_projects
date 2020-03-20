/**
 * class Search_
 * 完成了检索索引的部分：(步骤)
 * 1.选择想要搜索的范围(如：正文、收件人、发件人等等)
 * 输入想要搜索的内容，并为了之后的搜索对搜索内容进行处理。
 * 2. 构架索引搜索器、查询器等一系列工具
 * 对搜索内容进行解析
 * 3. 利用搜索器对搜索内容进行检索
 * 输出匹配的项，记录检索时间
 *
 * @author   江? - 1711430
 * @version  2019.10.22
 * @since    JDK 1.8
 */

package luceneExample;

import java.nio.file.Paths;
import java.util.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Search_ {

	/**
	 * 构建索引器
	 */
	private HashMap<String, String> map;
	public Scanner scanner;

	private Search_() {
		construct_map();
		scanner = new Scanner(System.in);
	}

	public void search(String indexDir, String[] choices, StringBuffer query_text) throws Exception {

		// 分词器
		Analyzer analyzer = new StandardAnalyzer();
		// 索引存储目录
		Directory directory = FSDirectory.open(Paths.get(indexDir));
		// 索引读取器
		IndexReader indexReader = DirectoryReader.open(directory);
		// 索引搜索器
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		// 第一个参数是要查询的字段； 第二个参数是分析器Analyzer

		/**
		 * 构建查询器
		 */
		String[] fields = new String[choices.length];
		for (int i = 0; i < choices.length; i++) {
			fields[i] = map.get(choices[i]);
		}
		QueryParser queryParser = new MultiFieldQueryParser(fields, analyzer);
		Query query = queryParser.parse(query_text.toString());

		long start = System.currentTimeMillis();
		// 开始查询
		/**
		 * 结合查询器和检索器，得到结果集
		 * 第一个参数是通过传过来的参数来查找得到的query； 第二个参数是要出查询的行数
		 */
		TopDocs hits = indexSearcher.search(query, 10);
		long end = System.currentTimeMillis();
		System.out.println("匹配 " + query_text + " ，总共花费" + (end - start) + "毫秒" + "查询到" + hits.totalHits + "个记录");

		/**
		 * ScoreDoc:得分文档,即得到文档 scoreDocs:代表的是topDocs这个文档数组
		 *
		 * @throws Exception
		 */
		int i = 1;
		for(ScoreDoc scoreDoc : hits.scoreDocs){
			Document hitDoc = indexSearcher.doc(scoreDoc.doc);
			System.out.println("******   " + (i++) + "   *****");
			System.out.println("文件路径 :" + hitDoc.get(Constants.FILE_PATH));
			System.out.println("发件人 :" + hitDoc.get(Constants.SENDER));
			System.out.println("收件人 :" + hitDoc.get(Constants.RECEIVERS));
			System.out.println("标题  :" + hitDoc.get(Constants.SUBJECT));
			System.out.println("日期  :" + hitDoc.get(Constants.DATE) + "\n");
		}
		indexReader.close();
		directory.close();
	}

	private void construct_map() {
		map = new HashMap<>();
		map.put("1", Constants.CONTENTS);
		map.put("2", Constants.SUBJECT);
		map.put("3", Constants.SENDER);
		map.put("4", Constants.RECEIVERS);
		map.put("5", Constants.DATE);
	}

	private StringBuffer deal_query(String[] choices) {
		StringBuffer query_text = new StringBuffer();
		String input;
		for (String choice: choices) {
			if (choice.equals("5")) {
				System.out.println("请输入想要搜索的时间下限和时间上限，中间以空格隔开");
				input = scanner.nextLine();
				String[] date = input.split(" ");
				query_text.append(map.get(choice) + ":" + "[" + date[0] + " TO " + date[date.length-1] + "] ");

			}else {

				System.out.println(map.get(choice) + ": 请输入想要查询的词项");
				input = scanner.nextLine();
				// 邻近词查询
				// 如果input进去的是个短语：使用邻近词查询的策略。  否则使用模糊查询
				if (query_text.toString().split(" ").length > 1) {
					input = "\"" + input + "\"~10 ";
				}else {
					input = input + "~0.8 ";
				}
				query_text.append(map.get(choice) + ":" + input);
			}
		}
		return query_text;
	}

	public static void main(String[] args) {
		//我们要搜索的内容
		Search_ searcher = new Search_();

		System.out.println("****** Lucene查询系统 ******");
		System.out.println("请选择要查询的部分：");
		System.out.println("1. 邮件内容 2. 邮件标题 3. 发件人 4. 收件人 5. 发送日期");
		System.out.println("支持：如果想要查询两个或多个部分的内容，中间请用一个空格隔开");
		System.out.println("例如，要同时查询邮件内容和邮件标题部分，请输入：1 2");

		Scanner scanner = new Scanner(System.in);
		String choose = scanner.nextLine();
		String[] choices = choose.split(" ");
		StringBuffer query_text = searcher.deal_query(choices);

		try {
			System.out.println(query_text);
			searcher.search(Constants.DIR, choices, query_text);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}





