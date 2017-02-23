package util;

/**
 * 作者：tianjiayuan
 * 创建时间：2017-02-21 13:02
 * 类描述：
 * 修改人：
 * 修改时间：
 */
public class ContantUtil {

    public static String gradeId2Grade(String gradeId){
        switch (gradeId){
            case "1001":
                return "一年级";
            case "1002":
                return "二年级";
            case "1003":
                return "三年级";
            case "1004":
                return "四年级";
            case "1005":
                return "五年级";
            case "1006":
                return "六年级";
            case "1007":
                return "预初";
            case "1008":
                return "初一";
            case "1009":
                return "初二";
            case "1010":
                return "初三";
            case "1011":
                return "高一";
            case "1012":
                return "高二";
            case "1013":
                return "高三";
            default: return gradeId;
        }
    }

    public static String subjectId2Subject(String subjectId){
        switch (subjectId){
            case "10010":
                return "语文";
            case "10011":
                return "数学";
            case "10012":
                return "英语";
            case "10013":
                return "生物";
            case "10014":
                return "物理";
            case "10015":
                return "化学";
            case "10016":
                return "地理";
            case "10017":
                return "历史";
            case "10018":
                return "政治";
            case "10019":
                return "品德与生活";
            case "10020":
                return "美术";
            case "10021":
                return "音乐";
            case "10022":
                return "体育";
            case "10023":
                return "信息技术";
            case "10024":
                return "法制";
            case "10025":
                return "综合实践";
            case "10026":
                return "科学";
            case "10027":
                return "理综";
            case "10028":
                return "文综";
            case "10029":
                return "思想品德";
            default :
                return subjectId;
        }
    }
}
