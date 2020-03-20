/**
 * class Constants
 * 提供实现功能的两个包所需要的字符串常量
 * Document中不同Field类型的名称
 * CONTENTS   : contents  内容    对应邮件中的正文
 * SENDER     : sender    发件人  对应邮件中"FROM"
 * RECEIVERS  : receivers 收件人  对应邮件中"TO"
 * SUBJECT    : subject   标题    对应邮件中"subject"
 * FILE_PATH  : fullPath  邮件的完整路径
 * DATE       : date      邮件发送时间 对邮件中的时间格式进行了解析
 *
 * @author   江玥 - 1711430
 * @version  2019.10.22
 * @since    JDK 1.8
 */

package luceneExample;


public class Constants {
    public static final String CONTENTS = "contents";
    public static final String SENDER = "sender";
    public static final String RECEIVERS = "receivers";
    public static final String SUBJECT = "subject";
    public static final String FILE_PATH = "fullPath";
    public static final String DATE = "date";
    public static final String DIR = "./test-files/";
    public static final String DATA_DIR = "./test-files/docs";
    public static final String TRASH_DIR = "./trashcan/";
    public static final String ILLEGAL_BIN = "illegal_files.txt";
    public static final String DELETED_BIN = "deleted_files.txt";
    public static final String DRAFT_BIN = "drafts.txt";
}
