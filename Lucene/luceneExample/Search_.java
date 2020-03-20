/**
 * class Search_
 * ����˼��������Ĳ��֣�(����)
 * 1.ѡ����Ҫ�����ķ�Χ(�磺���ġ��ռ��ˡ������˵ȵ�)
 * ������Ҫ���������ݣ���Ϊ��֮����������������ݽ��д���
 * 2. ������������������ѯ����һϵ�й���
 * ���������ݽ��н���
 * 3. �������������������ݽ��м���
 * ���ƥ������¼����ʱ��
 *
 * @author   ��? - 1711430
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
	 * ����������
	 */
	private HashMap<String, String> map;
	public Scanner scanner;

	private Search_() {
		construct_map();
		scanner = new Scanner(System.in);
	}

	public void search(String indexDir, String[] choices, StringBuffer query_text) throws Exception {

		// �ִ���
		Analyzer analyzer = new StandardAnalyzer();
		// �����洢Ŀ¼
		Directory directory = FSDirectory.open(Paths.get(indexDir));
		// ������ȡ��
		IndexReader indexReader = DirectoryReader.open(directory);
		// ����������
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		// ��һ��������Ҫ��ѯ���ֶΣ� �ڶ��������Ƿ�����Analyzer

		/**
		 * ������ѯ��
		 */
		String[] fields = new String[choices.length];
		for (int i = 0; i < choices.length; i++) {
			fields[i] = map.get(choices[i]);
		}
		QueryParser queryParser = new MultiFieldQueryParser(fields, analyzer);
		Query query = queryParser.parse(query_text.toString());

		long start = System.currentTimeMillis();
		// ��ʼ��ѯ
		/**
		 * ��ϲ�ѯ���ͼ��������õ������
		 * ��һ��������ͨ���������Ĳ��������ҵõ���query�� �ڶ���������Ҫ����ѯ������
		 */
		TopDocs hits = indexSearcher.search(query, 10);
		long end = System.currentTimeMillis();
		System.out.println("ƥ�� " + query_text + " ���ܹ�����" + (end - start) + "����" + "��ѯ��" + hits.totalHits + "����¼");

		/**
		 * ScoreDoc:�÷��ĵ�,���õ��ĵ� scoreDocs:�������topDocs����ĵ�����
		 *
		 * @throws Exception
		 */
		int i = 1;
		for(ScoreDoc scoreDoc : hits.scoreDocs){
			Document hitDoc = indexSearcher.doc(scoreDoc.doc);
			System.out.println("******   " + (i++) + "   *****");
			System.out.println("�ļ�·�� :" + hitDoc.get(Constants.FILE_PATH));
			System.out.println("������ :" + hitDoc.get(Constants.SENDER));
			System.out.println("�ռ��� :" + hitDoc.get(Constants.RECEIVERS));
			System.out.println("����  :" + hitDoc.get(Constants.SUBJECT));
			System.out.println("����  :" + hitDoc.get(Constants.DATE) + "\n");
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
				System.out.println("��������Ҫ������ʱ�����޺�ʱ�����ޣ��м��Կո����");
				input = scanner.nextLine();
				String[] date = input.split(" ");
				query_text.append(map.get(choice) + ":" + "[" + date[0] + " TO " + date[date.length-1] + "] ");

			}else {

				System.out.println(map.get(choice) + ": ��������Ҫ��ѯ�Ĵ���");
				input = scanner.nextLine();
				// �ڽ��ʲ�ѯ
				// ���input��ȥ���Ǹ����ʹ���ڽ��ʲ�ѯ�Ĳ��ԡ�  ����ʹ��ģ����ѯ
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
		//����Ҫ����������
		Search_ searcher = new Search_();

		System.out.println("****** Lucene��ѯϵͳ ******");
		System.out.println("��ѡ��Ҫ��ѯ�Ĳ��֣�");
		System.out.println("1. �ʼ����� 2. �ʼ����� 3. ������ 4. �ռ��� 5. ��������");
		System.out.println("֧�֣������Ҫ��ѯ�����������ֵ����ݣ��м�����һ���ո����");
		System.out.println("���磬Ҫͬʱ��ѯ�ʼ����ݺ��ʼ����ⲿ�֣������룺1 2");

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





