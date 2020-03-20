/**
 * class Index_
 * ����������Ľ������֣�(����)
 * 1. �������ĳ�ʼ����ʵ����IndexWriter
 * (��Ҫͨ��IndexWriter��������)
 *
 * 2. �����ĵ����ĵ��ɲ�ͬ���͵�field����
 * ���ں������ʼ��Ĳ�ͬ���ֽ��м���
 *
 * 3. д���ĵ���ͨ��addDocument����������д���ĵ�
 * ����FieldType������ͬ������
 *
 * @author   ��?- 1711430
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
	/**������**/
	private IndexWriter writer;
	private List<String> illegal_addr_email;
	private List<String> deleted_email;
	private List<String> draft_email;

	/**
	 * ���췽�� ʵ����IndexWriter��3��List
	 * ��ʼ��ʱ���ú� �洢Ŀ¼ directory, Ҫʹ�õķִ��� analyzer, Ҫʹ�õ������� IndexWriter
	 * @param indexDir �洢Ŀ¼
	 * @throws IOException �׳�IO�쳣
	 */
	public Index_(String indexDir) throws IOException {
		/*
		��ʼ��3��List
		 */
		this.illegal_addr_email = new ArrayList<>();
		this.deleted_email = new ArrayList<>();
		this.draft_email = new ArrayList<>();
		//�õ���������Ŀ¼��·��
		/*
		directory ��������Ŀ¼��·��
		analyzer  �ִ������˴�ʹ�ñ�׼�ִ���
		iwConfig  ���������ã��ж��ֲ����ɹ�ѡ��
		writer    ��������ͨ��Directory��IndexWriterConfig������г�ʼ��
		 */
		Directory directory = FSDirectory.open(Paths.get(indexDir));
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwConfig = new IndexWriterConfig(analyzer);
		iwConfig.setOpenMode(OpenMode.CREATE); //������������ӷ�ʽ������ѡ�񸲸�ԭ������  ����������APPEND
		writer = new IndexWriter(directory, iwConfig);
	}

	/**
	 * ��ȡ�ļ���  Ϊ֮����������׼��
	 *
	 * @param file Ҫ��ȡ�����������ļ�
	 */
	public String get_fileStream(File file) {
		Long fileLengthLong = file.length();
		byte[] fileContent = new byte[fileLengthLong.intValue()];
		try {
			FileInputStream inputStream = new FileInputStream(file);
			inputStream.read(fileContent);
			inputStream.close();
		}catch (IOException e) {
			System.out.println("��ȡ�ļ�������" + e.getMessage());
			// �鿴��д����ľ�����Ϣ
		}
		return new String(fileContent);
	}

	/**
	 * ��ȡ���ļ����ϣ��ᱻͳһΪDocument���󣬷���ͬһ��������
	 * Documentͨ������Field��ȷ�������������򣨿���ͨ���ļ������ļ�·�����ļ����ݵȵ�������
	 * @param file ��Ҫ�����������ļ�
	 * @throws Exception �׳��쳣
	 */

	//TODO:ɾ��
	/**
	 * �����������writer�Ѿ��ڳ�ʼ��ʱ���ú���Directory��Analyzer�ȣ�
	 * ��Щ�����ڹ�������ʱ�Զ���������
	 */
	public void index_doc(File file) throws Exception {
		/*
		�ʼ���ʽΪMime���ͣ������ض����������ʼ�
		 */
		String FileStream = get_fileStream(file);
		Session session = Session.getInstance(new Properties());
		InputStream is = new ByteArrayInputStream(FileStream.getBytes());
		MimeMessage message = new MimeMessage(session, is);
		System.out.println("�����ļ���" + file.getCanonicalPath());
		/*
		��ȡ�ļ����ϵĴ�����������Խ��и�ϸ�µĴ���
		CONTENTS  :  �ļ�����
		FILE_PATH :  �ļ�·��
		SUBJECT   :  ����
		SENDER    :  ������
		RECEIVERS :  �ռ���
		DATE      :  ����
		*/
		// ���������ʼ���  �û�����ַ���ԣ�
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
			MimeMessage��û��ר����ȡ�ռ��˵��йغ���
			�Լ�ʵ����һ��
			����������ռ��ˣ�����ݸ�����
			�����ռ��ˣ���������ȡ�ռ���
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
		������ת��ʱ���ʽʱ���ܵ�ʱ��Ӱ��
		������Զ�ת��ʱ����ת��Ϊ������ʱ��
		ʱ���ʽ���磺2000-01-01  ֻ�����ڽ����˴洢������֮��ͨ���������ڶ��ʼ�����
		 */
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		doc.add(new StringField(Constants.DATE, dateFormat.format(message.getSentDate()), Field.Store.YES));
		count++;
		writer.addDocument(doc);
	}

	/**
	 * ���ļ����е�ÿ���ļ�����index_doc
	 * @param fileList ��Ҫ�����������ļ���
	 * @throws Exception �׳��쳣
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
	 * ��������
	 * 
	 * @throws Exception
	 * @return Ŀǰ��RAM�л�����ļ�����
	 */
	public int index_construct(String dataDir) throws Exception {
		/*
		���ݼ�����ļ�ΪǶ�״洢
		ʹ��FileUtils.listFiles֧��Ƕ���г������ļ�
		 */
		File dir = new File(dataDir);
		List<File> fileList = (List<File>) FileUtils.listFiles(dir,null,true);
		index_docs(fileList);
		return writer.numRamDocs(); // ���ص���Ŀǰ��RAM�л�����ļ�����
	}
	
	/**
	 * �ر�д����
	 */
	public void close() throws IOException {
		writer.close();
	}

	public void record_trash_email(List<String> files, String filename) throws IOException {
		FileWriter file = new FileWriter(Constants.TRASH_DIR + filename); // ���·�������û����Ҫ����һ���µ�output��txt�ļ�
		BufferedWriter out = new BufferedWriter(file);
		for (String str : files) {
			out.write(str + "\n");
		}
		out.flush(); // �ѻ���������ѹ���ļ�
		out.close(); // ���ǵùر��ļ�
	}

	/**
	 * ������ʵ����������
	 * @param args
	 */
	public static void main(String[] args) {
		Index_ indexer = null;
		int numIndexed = 0;
		//������ʼʱ��  
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
		System.out.println("������" + count + " ���ļ� ������" + (end - start) + " ����");
	}
}

