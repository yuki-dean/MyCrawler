package com.lf.mycrawler.util;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符集自动检测
 */
public class CharsetDetector {

    private static final int CHUNK_SIZE = 2000;

    private static Pattern metaPattern = Pattern.compile(
            "<meta\\s+([^>]*http-equiv=(\"|')?content-type(\"|')?[^>]*)>",
            Pattern.CASE_INSENSITIVE);
    private static Pattern charsetPattern = Pattern.compile(
            "charset=\\s*([a-z][_\\-0-9a-z]*)", Pattern.CASE_INSENSITIVE);
    private static Pattern charsetPatternHTML5 = Pattern.compile(
            "<meta\\s+charset\\s*=\\*[\"']?([a-z][_\\-0-9a-z]*)[^>]*>",
            Pattern.CASE_INSENSITIVE);

    private static String guessEncodingByNutch(byte[] content) {
        int length = Math.min(content.length, CHUNK_SIZE);

        String str = "";
        try {
            str = new String(content, "ascii");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Matcher metaMatcher = charsetPattern.matcher(str);
        String encoding = null;
        if (metaMatcher.find()) {
            Matcher charsetMatcher = charsetPattern.matcher(metaMatcher.group(1));
            if (charsetMatcher.find()) {
                encoding = new String(charsetMatcher.group(1));
            }
        }
        if (encoding == null) {
            metaMatcher = charsetPatternHTML5.matcher(str);
            if (metaMatcher.find()) {
                encoding = new String(metaMatcher.group(1));
            }
        }
        if (encoding == null) {
            if (length >= 3 && content[0] == (byte)0XEF
                    && content[1] == (byte)0xBB && content[2] == (byte)0xBF) {
                encoding = "UTF-8";
            } else if (length >= 2) {
                if (content[0] == (byte)0xFF && content[1] == (byte)0xFE) {
                    encoding = "UTF-16LE";
                } else if (content[0] == (byte)0xFE
                        && content[1] == (byte)0xFF) {
                    encoding = "UTF-16BE";
                }
            }
        }
        return encoding;
    }

   /* *//**
     * 根据字节数组，猜测可能的字符集，如果检测失败返回utf-8
     * @parama bytes 待检测的字符数组
     * @return 可能的字符集，如果检测失败，返回utf-8
     */
    public static String guessEncodingByMoilla(byte[] bytes) {
        String DEFAULT_ENCODING = "UTF-8";
        UniversalDetector detector = new UniversalDetector(null);
        detector.handleData(bytes, 0, bytes.length); // 向检查器提供数据
        detector.dataEnd(); // 通知检查其没有更多数据要处理
        String encoding = detector.getDetectedCharset(); // 获取字符集名称
        detector.reset(); //重置检查器
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
        return encoding;
    }

    /**
     * 根据字节数组，猜测可能的字符集，如果检测失败返回utf-8
     * @parama bytes 待检测的字符数组
     * @return 可能的字符集，如果检测失败，返回utf-8
     */
    public static String guessEncoding(byte[] content) {
        String encoding;
        encoding = guessEncodingByNutch(content);
        if (encoding == null) {
            encoding = guessEncodingByMoilla(content);
            return encoding;
        }
        return encoding;
    }
}
