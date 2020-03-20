/**
 * class Index_
 * 完成了索引的建立部分：(步骤)
 * 1. 索引器的初始化：实例化IndexWriter
 * (需要通过IndexWriter建立索引)
 *
 * 2. 构造文档：文档由不同类型的field构成
 * 便于后续对邮件的不同部分进行检索
 *
 * 3. 写入文档：通过addDocument函数将索引写入文档
 * 根据FieldType创建不同的索引
 *
 * @author   江?- 1711430
 * @version  2019.10.22
 * @since    JDK 1.8
 */

package luceneExample;

import java.io.*;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;

public class Index_{
	private static int count = 0;
	/**索引器**/
	private IndexWriter writer;
	private List<String> illegal_addr_email;
	private List<String> deleted_email;
	private List<String> draft_email;

	/**
	 * 构造方法 实例化IndexWriter，3个List
	 * 初始化时设置好 存储目录 directory, 要使用的分词器 analyzer, 要使用的索引器 IndexWriter
	 * @param indexDir 存储目录
	 * @throws IOException 抛出IO异常
	 */
	public Index_(String indexDir) throws IOException {
		/*
		初始化3个List
		 */
		this.illegal_addr_email = new ArrayList<>();
		this.deleted_email = new ArrayList<>();
		this.draft_email = new ArrayList<>();
		//得到索引所在目录的路径
		/*
		directory 索引所在目录的路径
		analyzer  分词器，此处使用标准分词器
		iwConfig  索引器配置，有多种参数可供选择
		writer    索引器，通过Directory和IndexWriterConfig对其进行初始化
		 */
		Directory directory = FSDirectory.open(Paths.get(indexDir));
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwConfig = new IndexWriterConfig(analyzer);
		iwConfig.setOpenMode(OpenMode.CREATE); //设置索引的添加方式，这里选择覆盖原有索引  附加索引：APPEND
		writer = new IndexWriter(directory, iwConfig);
	}

	/**
	 * 获取文件流  为之后建立索引做准备
	 *
	 * @param file 要读取建立索引的文件
	 */
	public String get_fileStream(File file) {
		Long fileLengthLong = file.length();
		byte[] fileContent = new byte[fileLengthLong.intValue()];
		try {
			FileInputStream inputStream = new FileInputStream(file);
			inputStream.read(fileContent);
			inputStream.close();
		}catch (IOException e) {
			System.out.println("读取文件流出错：" + e.getMessage());
			// 查看读写错误的具体信息
		}
		return new String(fileContent);
	}

	/**
	 * 读取的文件资料，会被统一为Document对象，方便同一进行索引
	 * Document通过设置Field域确定进行索引的域（可以通过文件名、文件路径、文件内容等等索引）
	 * @param file 需要建立索引的文件
	 * @throws Exception 抛出异常
	 */

	//TODO:删掉
	/**
	 * 这里的索引器writer已经在初始化时设置好了Directory、Analyzer等，
	 * 这些都会在构建索引时自动发挥作用
	 */
	public void index_doc(File file) throws Exception {
		/*
		邮件格式为Mime类型，采用特定方法解析邮件
		 */
		String FileStream = get_fileStream(file);
		Session session = Session.getInstance(new Properties());
		InputStream is = new ByteArrayInputStream(FileStream.getBytes());
		MimeMessage message = new MimeMessage(session, is);
		System.out.println("索引文件：" + file.getCanonicalPath());
		/*
		读取文件资料的处理操作，可以进行更细致的处理
		CONTENTS  :  文件内容
		FILE_PATH :  文件路径
		SUBJECT   :  标题
		SENDER    :  发件人
		RECEIVERS :  收件人
		DATE      :  日期
		*/
		// 属于垃圾邮件！  用户名地址不对！
		Document doc = new Document();
		try {
			doc.add(new TextField(Constants.CONTENTS, (String) message.getContent(), Field.Store.YES));
			doc.add(new StringField(Constants.FILE_PATH, file.getCanonicalPath(), Field.Store.YES));
			doc.add(new StringField(Constants.SUBJECT, message.getSubject(), Field.Store.YES));
		}catch (IllegalArgumentException e) {
			deleted_email.add(file.getCanonicalPath());
		}

		try {
			doc.add(new StringField(
					Constants.SENDER, StringUtils.strip(Arrays.toString(message.getFrom()), "[]"
			).split("@")[0], Field.Store.YES));
			/*
			MimeMessage里没有专门提取收件人的有关函数
			自己实现了一下
			如果不存在收件人：放入草稿箱中
			存在收件人，则正常提取收件人
			 */
			String to_str = StringUtils.strip(Arrays.toString(message.getHeader("To")),"[]");
			if (to_str.equals("null")) {
				draft_email.add(file.getCanonicalPath());
			}
			else {
				String[] receivers = to_str.split(", ");
				for (String receiver: receivers) {
					doc.add(new StringField(Constants.RECEIVERS, receiver.split("@")[0], Field.Store.YES));
				}
			}
		}catch (AddressException e) {
			illegal_addr_email.add(file.getCanonicalPath());
		}

		/*
		设置在转换时间格式时不受到时区影响
		否则会自动转换时区，转换为东八区时间
		时间格式：如：2000-01-01  只对日期进行了存储，便于之后通过输入日期对邮件检索
		 */
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		doc.add(new StringField(Constants.DATE, dateFormat.format(message.getSentDate()), Field.Store.YES));
		count++;
		writer.addDocument(doc);
	}

	/**
	 * 对文件集中的每个文件调用index_doc
	 * @param fileList 需要建立索引的文件集
	 * @throws Exception 抛出异常
	 */
	public void index_docs(List<File> fileList) throws Exception {
		for (File file: fileList) {
			try {
				index_doc(file);
			}catch (NullPointerException e) {
				deleted_email.add(file.getCanonicalPath());
			}
		}
	}

	/**
	 * 构建索引
	 * 
	 * @throws Exception
	 * @return 目前在RAM中缓冲的文件数量
	 */
	public int index_construct(String dataDir) throws Exception {
		/*
		数据集里的文件为嵌套存储
		使用FileUtils.listFiles支持嵌套列出所有文件
		 */
		File dir = new File(dataDir);
		List<File> fileList = (List<File>) FileUtils.listFiles(dir,null,true);
		index_docs(fileList);
		return writer.numRamDocs(); // 返回的是目前在RAM中缓冲的文件数量
	}
	
	/**
	 * 关闭写索引
	 */
	public void close() throws IOException {
		writer.close();
	}

	public void record_trash_email(List<String> files, String filename) throws IOException {
		FileWriter file = new FileWriter(Constants.TRASH_DIR + filename); // 相对路径，如果没有则要建立一个新的output。txt文件
		BufferedWriter out = new BufferedWriter(file);
		for (String str : files) {
			out.write(str + "\n");
		}
		out.flush(); // 把缓存区内容压入文件
		out.close(); // 最后记得关闭文件
	}

	/**
	 * 主函数实现索引建立
	 * @param args
	 */
	public static void main(String[] args) {
		Index_ indexer = null;
		int numIndexed = 0;
		//索引开始时间  
		long start = System.currentTimeMillis();
		try {
			indexer = new Index_(Constants.DIR);
			numIndexed = indexer.index_construct(Constants.DATA_DIR);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				indexer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			indexer.record_trash_email(indexer.illegal_addr_email, Constants.ILLEGAL_BIN);
			indexer.record_trash_email(indexer.deleted_email, Constants.DELETED_BIN);
			indexer.record_trash_email(indexer.draft_email, Constants.DRAFT_BIN);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		long end = System.currentTimeMillis();
		System.out.println("索引：" + count + " 个文件 花费了" + (end - start) + " 毫秒");
	}
}

